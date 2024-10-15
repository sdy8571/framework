package com.framework.sequence.generator.impl;

import com.framework.sequence.config.SegmentSequenceModel;
import com.framework.sequence.generator.ISequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author sdy
 * @date 2020/1/5
 * @Version 1.0
 * @Description
 */
@Service
public class UUIDStringSequenceGenerator implements ISequenceGenerator {

    private static final String type = "uuid";

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

        return UUID.randomUUID().toString();
    }
}
