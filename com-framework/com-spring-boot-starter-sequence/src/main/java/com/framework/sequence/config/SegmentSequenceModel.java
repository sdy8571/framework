package com.framework.sequence.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sdy
 * @date 2020/1/4
 * @Version 1.0
 * @Description
 */
@Data
public class SegmentSequenceModel {

    private String type;
    private Map<String, String> props = new HashMap<>();
}
