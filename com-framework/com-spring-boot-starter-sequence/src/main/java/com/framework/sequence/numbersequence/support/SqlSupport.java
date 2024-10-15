package com.framework.sequence.numbersequence.support;

import com.framework.sequence.exception.SequenceException;
import org.springframework.util.StringUtils;

/**
 * @author sdy
 * @date 2020/1/7
 * @Version 1.0
 * @Description
 */
public class SqlSupport {

    /**
     * 数据库名称ORACLE
     */
    public static final String DATABASE_ORACLE = "ORACLE";
    /**
     * 数据库名称DB2
     */
    public static final String DATABASE_DB2 = "DB2";
    /**
     * 数据库名称MYSQL
     */
    public static final String DATABASE_MYSQL = "MYSQL";

    /**
     * 序列号配置表名称默认值
     */
    public final static String DEFAULT_TABLE_NAME = "SEQUENCE_CONFIG";

    private final static String DEFAULT_COL_NAME_SEQ_NAME = "SEQ_NAME";
    private final static String DEFAULT_COL_NAME_MIN_VAL = "MIN_VAL";
    private final static String DEFAULT_COL_NAME_MAX_VAL = "MAX_VAL";
    private final static String DEFAULT_COL_NAME_CUR_VAL = "CUR_VAL";
    private final static String DEFAULT_COL_NAME_STEP_VAL = "STEP_VAL";
    private final static String DEFAULT_COL_NAME_CYCLE = "CYCLE";
    private final static String DEFAULT_COL_NAME_UPDATE_TIME = "UPDATE_TIME";

    /**
     * 建序列号模板
     */
    private final static String SEQUENCE_CREATE_TEMPLATE =
        "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES ('%s', %s, %s, %s, %s)";
    /**
     * DB2查询模板
     */
    private final static String SEQUENCE_CONFIG_QUERY_TEMPLATE_DB2 =
        "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE WITH RS";
    /**
     * DB2更新模板
     */
    private final static String SEQUENCE_CONFIG_UPDATE_TEMPLATE_DB2 = "UPDATE %s SET %s=? WHERE %s=?";
    /**
     * ORACLE查询模板
     */
    private final static String SEQUENCE_CONFIG_QUERY_TEMPLATE_ORACLE = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * ORACLE更新模板
     */
    private final static String SEQUENCE_CONFIG_UPDATE_TEMPLATE_ORACLE = "UPDATE %s SET %s=? WHERE %s=?";
    /**
     * MYSQL查询模板
     */
    private final static String SEQUENCE_CONFIG_QUERY_TEMPLATE_MYSQL = "SELECT %s,%s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * MYSQL更新模板
     */
    private final static String SEQUENCE_CONFIG_UPDATE_TEMPLATE_MYSQL = "UPDATE %s SET %s=? WHERE %s=?";

    /**
     * DB2查询上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_QUERY_TEMPLATE_DB2 =
        "SELECT %s FROM %s WHERE %s=? FOR UPDATE WITH RS";
    /**
     * DB2更新上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_DB2 = "UPDATE %s SET %s=?,%s=? WHERE %s=?";
    /**
     * ORACLE查询上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_QUERY_TEMPLATE_ORACLE = "SELECT %s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * ORACLE更新上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_ORACLE = "UPDATE %s SET %s=?,%s=? WHERE %s=?";
    /**
     * MYSQL查询上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_QUERY_TEMPLATE_MYSQL = "SELECT %s FROM %s WHERE %s=? FOR UPDATE";
    /**
     * MYSQL更新上次重置时间模板
     */
    private final static String SEQUENCE_RESETTIME_UPDATE_TEMPLATE_MYSQL = "UPDATE %s SET %s=?,%s=? WHERE %s=?";

    /**
     * 查询模板
     */
    private final static String SEQUENCE_QUERY_SQL_TEMPLATE = "SELECT %s,%s FROM %s WHERE %s=?";

    /**
     * 根据数据库类型和配置准备查询语句和更新语句
     *
     * @param databaseType
     * @param currentSequenceKey
     * @param maximumSequenceKey
     * @param idKey
     * @param tableName
     */
    public String[] prepareSql(String databaseType, String currentSequenceKey, String maximumSequenceKey, String idKey,
        String tableName, String seqName, String minVal, String maxVal, String lastRestTimeSequenceKey) {

        String sequenceCreateSql = null;
        String sequenceConfigQuery = null;
        String sequenceConfigUpdate = null;
        String lastResetTimeQuery = null;
        String lastResetTimeUpdate = null;
        String sequenceQuerySql = null;

        sequenceCreateSql = String.format(SEQUENCE_CREATE_TEMPLATE, tableName,
            idKey, currentSequenceKey, maximumSequenceKey, "'" + seqName + "'", minVal, maxVal);

        if (DATABASE_DB2.equals(databaseType)) {
            sequenceConfigQuery = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_DB2, currentSequenceKey,
                maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_DB2, tableName, currentSequenceKey,
                idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_DB2, lastRestTimeSequenceKey,
                tableName, idKey);
            lastResetTimeUpdate =
                String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_DB2, tableName, lastRestTimeSequenceKey,
                    currentSequenceKey, idKey);
        } else if (DATABASE_ORACLE.equals(databaseType)) {
            sequenceConfigQuery = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_ORACLE, currentSequenceKey,
                maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_ORACLE, tableName, currentSequenceKey,
                idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_ORACLE, lastRestTimeSequenceKey,
                tableName, idKey);
            lastResetTimeUpdate =
                String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_ORACLE, tableName, lastRestTimeSequenceKey,
                    currentSequenceKey, idKey);
        } else if (DATABASE_MYSQL.equals(databaseType)) {
            sequenceConfigQuery = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_MYSQL, currentSequenceKey,
                maximumSequenceKey, tableName, idKey);
            sequenceConfigUpdate = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_MYSQL, tableName, currentSequenceKey,
                idKey);
            lastResetTimeQuery = String.format(SEQUENCE_RESETTIME_QUERY_TEMPLATE_MYSQL, lastRestTimeSequenceKey,
                tableName, idKey);
            lastResetTimeUpdate =
                String.format(SEQUENCE_RESETTIME_UPDATE_TEMPLATE_MYSQL, tableName, lastRestTimeSequenceKey,
                    currentSequenceKey, idKey);
        }
        if (!StringUtils.hasText(sequenceConfigQuery)) {
            throw new SequenceException("sequenceConfigQuery is empty");
        }
        if (!StringUtils.hasText(sequenceConfigUpdate)) {
            throw new SequenceException("sequenceConfigUpdate is empty");
        }
        if (!StringUtils.hasText(lastResetTimeQuery)) {
            throw new SequenceException("lastResetTimeQuery is empty");
        }
        if (!StringUtils.hasText(lastResetTimeUpdate)) {
            throw new SequenceException("lastResetTimeUpdate is empty");
        }

        sequenceQuerySql = String.format(SEQUENCE_QUERY_SQL_TEMPLATE, currentSequenceKey,
            maximumSequenceKey, tableName, idKey);

        return new String[] {sequenceCreateSql, sequenceConfigQuery, sequenceConfigUpdate, lastResetTimeQuery,
            lastResetTimeUpdate, sequenceQuerySql};
    }

    public static String getSequenceCreateSql(String seqName, String minVal, String maxVal, String step) {

        return String.format(SEQUENCE_CREATE_TEMPLATE, DEFAULT_TABLE_NAME,
            DEFAULT_COL_NAME_SEQ_NAME, DEFAULT_COL_NAME_MIN_VAL, DEFAULT_COL_NAME_MAX_VAL, DEFAULT_COL_NAME_CUR_VAL,
            DEFAULT_COL_NAME_STEP_VAL, seqName, minVal, maxVal, minVal, step);
    }

    public static String getSequenceQuerySql() {

        return String.format(SEQUENCE_QUERY_SQL_TEMPLATE, DEFAULT_COL_NAME_CUR_VAL,
            DEFAULT_COL_NAME_MAX_VAL, DEFAULT_TABLE_NAME, DEFAULT_COL_NAME_SEQ_NAME);
    }

    public static String getSequenceQuerySqlLocked(String databaseType) {

        String sql = null;
        if (DATABASE_MYSQL.equals(databaseType)) {
            sql = String.format(SEQUENCE_CONFIG_QUERY_TEMPLATE_MYSQL, DEFAULT_COL_NAME_CUR_VAL,
                DEFAULT_COL_NAME_MAX_VAL, DEFAULT_TABLE_NAME, DEFAULT_COL_NAME_SEQ_NAME);

        }
        return sql;
    }

    public static String getSequenceUpdateSql(String databaseType) {

        String sql = null;
        if (DATABASE_MYSQL.equals(databaseType)) {
            sql = String.format(SEQUENCE_CONFIG_UPDATE_TEMPLATE_MYSQL, DEFAULT_TABLE_NAME, DEFAULT_COL_NAME_CUR_VAL,
                DEFAULT_COL_NAME_SEQ_NAME);
        }
        return sql;

    }

    public static String getCreateTableSql() {

        String sql = "CREATE TABLE `" + DEFAULT_TABLE_NAME + "` (\n"
            + "  `SEQ_NAME` varchar(255) NOT NULL COMMENT '序列名称',\n"
            + "  `MIN_VAL` decimal(35,0) NOT NULL COMMENT '最小值',\n"
            + "  `MAX_VAL` decimal(35,0) NOT NULL COMMENT '最大值',\n"
            + "  `CUR_VAL` decimal(35,0) NOT NULL COMMENT '当前使用值',\n"
            + "  `STEP_VAL` int(10) NOT NULL DEFAULT '1' COMMENT '递增值',\n"
            + "  `CYCLE` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否循环,默认为Y表示循环',\n"
            + "  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',\n"
            + "  PRIMARY KEY (`SEQ_NAME`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        return sql;
    }

    public static String getCheckTableSql() {

        return "SELECT COUNT(*) FROM " + DEFAULT_TABLE_NAME + ";";
    }

}
