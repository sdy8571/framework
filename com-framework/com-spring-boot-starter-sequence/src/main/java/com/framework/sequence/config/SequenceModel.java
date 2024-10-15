package com.framework.sequence.config;

import lombok.Data;

import java.util.List;

/**
 * @author sdy
 * @date 2020/1/4
 * @Version 1.0
 * @Description
 */
@Data
public class SequenceModel {

    private String id;
    private List<SegmentSequenceModel> segmentConfigs;
}
