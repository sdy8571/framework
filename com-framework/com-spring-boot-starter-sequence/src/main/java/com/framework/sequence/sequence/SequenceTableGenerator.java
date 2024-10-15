package com.framework.sequence.sequence;

/**
 * @author sdy
 * @date 2020/1/9
 * @Version 1.0
 * @Description
 */
public interface SequenceTableGenerator {

    /**
     * 获取sequence所在数据库类型
     *
     * @return
     */
    String getSequenceDbType();

    /**
     * 检查sequence配置表是否存在，不存在则创建
     */
    void generatorSequenceTable();
}
