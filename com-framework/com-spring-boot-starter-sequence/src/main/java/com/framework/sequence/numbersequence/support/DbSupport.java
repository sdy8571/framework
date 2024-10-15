package com.framework.sequence.numbersequence.support;

import com.framework.sequence.exception.SequenceException;
import com.framework.sequence.numbersequence.DbNumberSequenceObject;
import com.framework.sequence.numbersequence.SequenceDbConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

/**
 * @author sdy
 * @date 2020/1/7
 * @Version 1.0
 * @Description
 */
@Slf4j
public class DbSupport {

    /**
     * 根据DatabaseMetaData确定数据库类型
     *
     * @return
     * @throws SQLException
     */
    public static String resolveDatabaseName(DataSource dataSource) throws SQLException {

        Connection connection = null;
        String databaseType = "";
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            String upperDatabaseProductName = databaseProductName.toUpperCase();
            if (upperDatabaseProductName.contains(SqlSupport.DATABASE_DB2)) {
                databaseType = SqlSupport.DATABASE_DB2;
            } else if (upperDatabaseProductName.contains(SqlSupport.DATABASE_ORACLE)) {
                databaseType = SqlSupport.DATABASE_ORACLE;
            } else if (upperDatabaseProductName.contains(SqlSupport.DATABASE_MYSQL)) {
                databaseType = SqlSupport.DATABASE_MYSQL;
            } else {
                throw new SequenceException("Unsupported database: " + databaseProductName);
            }
        } finally {
            if (connection != null) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
        return databaseType;
    }

    /**
     * 检查db是否有指定的序列号记录，如果没有则进行创建
     *
     * @throws SQLException
     */
    public static void checkDbSequence(
        DbNumberSequenceObject dbNumberSequenceObject) throws SQLException {

        DataSourceTransactionManager dataSourceTransactionManager =
            dbNumberSequenceObject.getDataSourceTransactionManager();
        // TODO DefaultTransactionDefinition做全局变量？
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();

        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus txStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        DataSource dataSource = dataSourceTransactionManager.getDataSource();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement statement = null;
        boolean foundResult = false;
        try {
            ps = connection.prepareStatement(SqlSupport.getSequenceQuerySql());
            ps.setString(1, dbNumberSequenceObject.getSeqName());
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new SequenceException(
                        "More than one DbSequenceConfig row found for id [" + dbNumberSequenceObject.getSeqName()
                            + "]. DbSequenceConfig id must be unique.");
                }
                BigDecimal current = rs.getBigDecimal(1);
                BigDecimal maximum = rs.getBigDecimal(2);
                if (current.compareTo(maximum) > 0) {
                    throw new SequenceException(
                        "Illegal DbSequenceConfig row found for id [" + dbNumberSequenceObject.getSeqName()
                            + "]. Current value must less than maximum.");
                }
                foundResult = true;
            }
            if (!foundResult) {
                statement = connection.createStatement();
                String insertSql = SqlSupport
                    .getSequenceCreateSql(dbNumberSequenceObject.getSeqName(),
                        dbNumberSequenceObject.getMinVal().toString(),
                        dbNumberSequenceObject.getMaxVal().toString(),
                        dbNumberSequenceObject.getStepVal().toString());
                int result = statement.executeUpdate(insertSql);
                if (result == 0) {
                    throw new SequenceException(
                        "cannot create sequence record <" + dbNumberSequenceObject.getSeqName() + "> in table "
                            + SqlSupport.DEFAULT_TABLE_NAME);
                }
            }
            dataSourceTransactionManager.commit(txStatus);
        } catch (RuntimeException cause) {
            // 避免后续的数据库异常淹没此异常
            log.error(cause.getMessage(), cause);

            dataSourceTransactionManager.rollback(txStatus);

            throw cause;
        } catch (SQLException cause) {
            // 避免后续的数据库异常淹没此异常
            log.error(cause.getMessage(), cause);

            dataSourceTransactionManager.rollback(txStatus);

            throw cause;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
    }

    /**
     * 获取数据库中序列号；在获取一个可用值后，会增加一个步长到数据库值。当序列号即将超过配置的最大值时,重置循环使用。
     * 本方法使用数据库事务，且扩散行为为强制开启一个新的事务，否则使用已有事务时多线程情况下会出现序列号重复的问题。
     */
    public static void loadSequence(DbNumberSequenceObject dbNumberSequenceObject) {

        DataSourceTransactionManager dataSourceTransactionManager =
            dbNumberSequenceObject.getDataSourceTransactionManager();

        // TODO DefaultTransactionDefinition做全局变量？
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();

        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus txStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        DataSource dataSource = dataSourceTransactionManager.getDataSource();

        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);

            SequenceDbConfig configInDb = getSequenceConfigFromDb(connection, dbNumberSequenceObject.getSeqName(),
                dbNumberSequenceObject.getDatabaseType());
            // 数据库记录的"当前值"
            BigDecimal currentInDb = configInDb.getCurVal();
            // 数据库记录的"最大值"
            BigDecimal maxInDb = configInDb.getMaxVal();

            // 准备放到本节点缓存中的最大值
            BigDecimal nextMaxInNodeCache =
                currentInDb.add(dbNumberSequenceObject.getStepVal());

            // nextMaxInNodeCache available
            if (nextMaxInNodeCache.compareTo(maxInDb) <= 0) {

                dbNumberSequenceObject.setCurValCache(currentInDb);
                dbNumberSequenceObject.setMaxValCache(nextMaxInNodeCache);

                // nextMaxInNodeCache unavailable
            } else {
                // 序列号使用完，如果可循环，重新从设定最小值开始
                if (dbNumberSequenceObject.getCycle().equals("Y")) {

                    nextMaxInNodeCache =
                        dbNumberSequenceObject.getMinVal()
                            .add(dbNumberSequenceObject.getStepVal());

                    dbNumberSequenceObject.setCurValCache(dbNumberSequenceObject.getMinVal());
                    dbNumberSequenceObject.setMaxValCache(nextMaxInNodeCache);

                } else {
                    throw new SequenceException(
                        "sequence name[" + dbNumberSequenceObject.getSeqName() + "] min[" +
                            dbNumberSequenceObject.getMinVal().toString() + "] max["
                            + dbNumberSequenceObject.getMaxVal().toString()
                            + "] have all used.");
                }

            }

            boolean success = updateSequenceConfig(connection, dbNumberSequenceObject.getSeqName(), nextMaxInNodeCache,
                dbNumberSequenceObject.getDatabaseType());
            if (!success) {
                throw new SequenceException(
                    "Fail to update DbSequenceConfig row for ID [" + dbNumberSequenceObject.getSeqName()
                        + "], CURRENT ["
                        + nextMaxInNodeCache + "].");
            }

            dataSourceTransactionManager.commit(txStatus);
        } catch (SequenceException tfe) {

            log.error(tfe.getMessage(), tfe);
            dataSourceTransactionManager.rollback(txStatus);
            throw tfe;
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            dataSourceTransactionManager.rollback(txStatus);
            throw new SequenceException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * 查询数据库中记录序列号配置情况
     *
     * @return
     * @throws SQLException
     */
    private static SequenceDbConfig getSequenceConfigFromDb(Connection connection, String id, String databaseType)
        throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        // Loop over results - although we are only expecting one result, since id should be unique
        boolean foundResult = false;
        SequenceDbConfig config = new SequenceDbConfig();
        try {
            ps = connection.prepareStatement(SqlSupport.getSequenceQuerySqlLocked(databaseType));
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (foundResult) {
                    throw new SequenceException("More than one DbSequenceConfig row found for id [" + id
                        + "]. DbSequenceConfig id must be unique.");
                }
                BigDecimal current = rs.getBigDecimal(1);
                BigDecimal maximum = rs.getBigDecimal(2);
                //                if (current > maximum) {
                //                    throw new SequenceException("Illegal DbSequenceConfig row found for id [" + id
                //                        + "]. Current value must less than maximum.");
                //                }
                config.setCurVal(current);
                config.setMaxVal(maximum);
                foundResult = true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        if (!foundResult) {
            throw new SequenceException("No DbSequenceConfig row found for id [" + id + "].");
        }
        return config;
    }

    /**
     * 更新当前可用序列号到服务器
     *
     * @throws SQLException
     */
    public static boolean updateSequenceConfig(Connection connection, String id, BigDecimal current,
        String databaseType)
        throws SQLException {

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SqlSupport.getSequenceUpdateSql(databaseType));
            ps.setBigDecimal(1, current);
            ps.setString(2, id);
            int affected = ps.executeUpdate();
            return affected == 1;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 检查sequence表是否存在
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    public static boolean checkSequenceTableExist(DataSource dataSource) throws SQLException {

        Connection connection = DataSourceUtils.getConnection(dataSource);
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeQuery(SqlSupport.getCheckTableSql());
        } catch (SQLException e) {

            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

        return true;
    }

    /**
     * 创建sequence表
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    public static void createSequenceTable(DataSource dataSource) throws SQLException {

        Connection connection = DataSourceUtils.getConnection(dataSource);
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.execute(SqlSupport.getCreateTableSql());
        } finally {
            if (statement != null) {
                statement.close();
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

}
