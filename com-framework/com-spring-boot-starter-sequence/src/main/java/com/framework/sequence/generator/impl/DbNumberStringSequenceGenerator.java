package com.framework.sequence.generator.impl;

import cn.hutool.core.util.StrUtil;
import com.framework.sequence.config.SegmentSequenceModel;
import com.framework.sequence.exception.SequenceException;
import com.framework.sequence.generator.ISequenceGenerator;
import com.framework.sequence.numbersequence.DbNumberSequenceObject;
import com.framework.sequence.numbersequence.support.DbSupport;
import com.framework.sequence.sequence.SequenceTableGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sdy
 * @date 2020/1/5
 * @Version 1.0
 * @Description
 */
@Slf4j
@Service
public class DbNumberStringSequenceGenerator implements ISequenceGenerator {

    @Resource
    private DataSource dataSource;
    @Resource
    private SequenceTableGenerator sequenceTableGenerator;

    public static final String type = "dbNumberString";

    private static final Map<String, DbNumberSequenceObject> sequenceCacheMap = new HashMap<>();

    private static final String KEY_ID = "id";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_MAX = "max";
    private static final String KEY_STEP = "step";
    private static final String KEY_LEFTPADDING = "leftPadding";

    private static final String DEFAULT_LEFTPADDING = "0";
    private static final String DEFAULT_STEP = "100";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void checkParams(SegmentSequenceModel model) {

        Map<String, String> props = model.getProps();
        String id = props.get(KEY_ID);
        String length = props.get(KEY_LENGTH);
        String max = props.get(KEY_MAX);
        String step = props.get(KEY_STEP);
        String leftPadding = props.get(KEY_LEFTPADDING);

        if (!StringUtils.hasText(id)) {
            throw new SequenceException("sequence type dbNumberString need property [id]");
        }
        if (id.length() > 200) {
            throw new SequenceException("sequence type dbNumberString id property is too long ");
        }

        if (!StringUtils.hasText(length)) {
            throw new SequenceException("sequence type dbNumberString need property [length]");
        }
        if (!StrUtil.isNumeric(length)) {
            throw new SequenceException("sequence type dbNumberString length property must be number ");
        }
        if (length.length() > 3) {
            throw new SequenceException("sequence type dbNumberString length property is too long ");
        }

        if (!StringUtils.hasText(max)) {
            throw new SequenceException("sequence type dbNumberString need property [max]");
        }
        if (!StrUtil.isNumeric(max)) {
            throw new SequenceException("sequence type dbNumberString max property must be number ");
        }
        if (max.length() > 35) {
            throw new SequenceException("sequence type dbNumberString max property is too long ");
        }

        if (StringUtils.hasText(step)) {
            if (!StrUtil.isNumeric(step)) {
                throw new SequenceException("sequence type dbNumberString step property must be number ");
            }
            if (step.length() > 35) {
                throw new SequenceException("sequence type dbNumberString step property is too long ");
            }
        }

        if (StringUtils.hasText(leftPadding) && leftPadding.length() > 1) {
            throw new SequenceException("sequence type dbNumberString leftPadding property only can be one char ");
        }

    }

    @Override
    public void init(SegmentSequenceModel model) {

        try {
            Map<String, String> props = model.getProps();
            String id = props.get(KEY_ID);
            String length = props.get(KEY_LENGTH);
            String max = props.get(KEY_MAX);
            String step = props.get(KEY_STEP);
            String leftPadding = props.get(KEY_LEFTPADDING);

            DbNumberSequenceObject dso = new DbNumberSequenceObject(id);
            dso.setMaxVal(new BigDecimal(max));
            if (!StringUtils.hasText(step)) {
                step = DEFAULT_STEP;
            }
            dso.setStepVal(new BigDecimal(step));
            dso.setLength(length);

            if (!StringUtils.hasText(leftPadding)) {
                leftPadding = DEFAULT_LEFTPADDING;
            }
            dso.setLeftPadding(leftPadding);

            dso.setDatabaseType(sequenceTableGenerator.getSequenceDbType());
            dso.setDataSourceTransactionManager(getTransactionManager());

            DbSupport.checkDbSequence(dso);

            dso.reset();

            sequenceCacheMap.put(id, dso);

        } catch (Exception e) {
            log.error("", e);
            System.exit(0);
        }

    }

    @Override
    public String getSequence(SegmentSequenceModel model) {

        DbNumberSequenceObject dbNumberSequenceObject = sequenceCacheMap.get(model.getProps().get(KEY_ID));

        BigDecimal str = dbNumberSequenceObject.getNextval();

        String seq = str == null ? "" : str.toString();

        int length = Integer.parseInt(dbNumberSequenceObject.getLength());
        String leftPadding = dbNumberSequenceObject.getLeftPadding();

        StringBuilder sb = new StringBuilder();
        if (seq.length() < length) {

            for (int i = 0; i < length - seq.length(); i++) {
                sb.append(leftPadding);
            }
        }
        sb.append(seq);

        return sb.toString();
    }

    private DataSourceTransactionManager getTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        return transactionManager;
    }
}
