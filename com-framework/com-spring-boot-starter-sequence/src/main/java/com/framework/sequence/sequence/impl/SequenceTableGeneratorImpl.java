package com.framework.sequence.sequence.impl;

import com.framework.sequence.numbersequence.support.DbSupport;
import com.framework.sequence.numbersequence.support.SqlSupport;
import com.framework.sequence.sequence.SequenceTableGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author sdy
 * @date 2020/1/9
 * @Version 1.0
 * @Description
 */
@Slf4j
@Service
public class SequenceTableGeneratorImpl implements SequenceTableGenerator {

    @Resource
    private DataSource dataSource;

    private static String dbType;

    @Override
    public String getSequenceDbType() {

        if (dbType != null) {
            return dbType;
        }

        try {
            dbType = DbSupport.resolveDatabaseName(dataSource);
            log.info("sequence所在数据库类型: [{}]", dbType);
        } catch (Exception e) {
            log.error("检查数据库类型出现错误：", e);
            System.exit(0);
        }
        return dbType;
    }

    @Override
    public void generatorSequenceTable() {

        try {
            boolean sequenceTableExist = DbSupport.checkSequenceTableExist(dataSource);
            if (sequenceTableExist) {
                log.info("sequence配置表[{}]已存在，不再创建。", SqlSupport.DEFAULT_TABLE_NAME);
            } else {
                log.info("sequence配置表[{}]不存在，准备创建……", SqlSupport.DEFAULT_TABLE_NAME);
                try {
                    DbSupport.createSequenceTable(dataSource);
                } catch (SQLException e) {
                    log.error("sequence配置表[{}]，创建失败。", SqlSupport.DEFAULT_TABLE_NAME);
                    System.exit(0);
                }
                log.info("sequence配置表[{}]，创建成功。", SqlSupport.DEFAULT_TABLE_NAME);
            }
        } catch (Exception e) {
            log.error("sequence初始化失败：", e);
            System.exit(0);
        }

    }
}
