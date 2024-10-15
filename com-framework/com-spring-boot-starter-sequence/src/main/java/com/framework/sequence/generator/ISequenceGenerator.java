package com.framework.sequence.generator;

import com.framework.sequence.config.SegmentSequenceModel;

/**
 * @author sdy
 * @date 2020/1/5
 * @Version 1.0
 * @Description
 */
public interface ISequenceGenerator {

    String getType();

    void checkParams(SegmentSequenceModel model);

    void init(SegmentSequenceModel model);

    String getSequence(SegmentSequenceModel model);
}
