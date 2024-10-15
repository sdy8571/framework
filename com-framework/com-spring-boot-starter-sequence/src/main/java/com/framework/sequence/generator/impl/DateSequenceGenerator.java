package com.framework.sequence.generator.impl;

import cn.hutool.core.date.DateUtil;
import com.framework.sequence.config.SegmentSequenceModel;
import com.framework.sequence.exception.SequenceException;
import com.framework.sequence.generator.ISequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author sdy
 * @date 2020/1/5
 * @Version 1.0
 * @Description
 */
@Service
public class DateSequenceGenerator implements ISequenceGenerator {

    private static final String type = "dateString";

    private static final String KEY_FORMAT = "format";
    private static final String DEFAULT_FORMAT = "yyyyMMdd";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void checkParams(SegmentSequenceModel model) {

    }

    @Override
    public void init(SegmentSequenceModel model) {

    }

    @Override
    public String getSequence(SegmentSequenceModel model) {

        Map<String, String> props = model.getProps();
        String format = props.get(KEY_FORMAT);
        if (!StringUtils.hasText(format)) {
            format = DEFAULT_FORMAT;
        }

        try {
            return DateUtil.format(new Date(), format);
        } catch (Exception e) {
            throw new SequenceException("create dateSequence error,maybe format is illegal");
        }

    }
}
