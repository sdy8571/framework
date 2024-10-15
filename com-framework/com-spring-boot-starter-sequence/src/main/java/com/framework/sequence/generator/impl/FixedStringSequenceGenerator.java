package com.framework.sequence.generator.impl;

import com.framework.sequence.config.SegmentSequenceModel;
import com.framework.sequence.exception.SequenceException;
import com.framework.sequence.generator.ISequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author sdy
 * @date 2020/1/5
 * @Version 1.0
 * @Description
 */
@Service
public class FixedStringSequenceGenerator implements ISequenceGenerator {

    private static final String type = "fixedString";

    private static final String KEY_FIXEDSTRING = "fixedString";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void checkParams(SegmentSequenceModel model) {
        Map<String, String> props = model.getProps();
        String fixedString = props.get(KEY_FIXEDSTRING);
        if (!StringUtils.hasText(fixedString)) {
            throw new SequenceException("sequence type dateString need property [fixedString]");
        }
    }

    @Override
    public void init(SegmentSequenceModel model) {

    }

    @Override
    public String getSequence(SegmentSequenceModel model) {

        Map<String, String> props = model.getProps();
        return props.get(KEY_FIXEDSTRING);
    }
}
