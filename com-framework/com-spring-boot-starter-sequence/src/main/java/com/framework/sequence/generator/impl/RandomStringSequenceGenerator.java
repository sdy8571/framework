package com.framework.sequence.generator.impl;

import cn.hutool.core.util.StrUtil;
import com.framework.sequence.config.SegmentSequenceModel;
import com.framework.sequence.exception.SequenceException;
import com.framework.sequence.generator.ISequenceGenerator;
import com.framework.sequence.random.ThreadLocalRandom;
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
public class RandomStringSequenceGenerator implements ISequenceGenerator {

    private static final String type = "randomString";

    private static final String KEY_LENGTH = "length";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void checkParams(SegmentSequenceModel model) {

        Map<String, String> props = model.getProps();
        String length = props.get(KEY_LENGTH);
        if (!StringUtils.hasText(length)) {
            throw new SequenceException("sequence type randomString need property [length]");
        }
        if (!StrUtil.isNumeric(length)) {
            throw new SequenceException("sequence type randomString length property must be number ");
        }
        if (length.length() > 3) {
            throw new SequenceException("sequence type randomString length property is too long ");
        }

    }

    @Override
    public void init(SegmentSequenceModel model) {

    }

    @Override
    public String getSequence(SegmentSequenceModel model) {

        Map<String, String> props = model.getProps();
        String lengthStr = props.get(KEY_LENGTH);

        int length = Integer.parseInt(lengthStr);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        long minValue = getMinValue(length);
        long maxValue = getMaxValue(length);
        long segmentValue = random.nextLong(minValue, maxValue);
        String segmentString = String.valueOf(segmentValue);

        return segmentString;
    }

    /**
     * 获取指定位数的数据最小值
     *
     * @param length
     * @return
     */
    private long getMinValue(int length) {
        long minValue = 1;
        for (int i = 1; i < length; i++) {
            minValue *= 10;
        }
        return minValue;
    }

    /**
     * 获取指定位数的数据最大值
     *
     * @param length
     * @return
     */
    private long getMaxValue(int length) {
        long maxValue = 0;
        long sumValue = 1;
        for (int i = 0; i < length; i++) {
            sumValue *= 10;
            maxValue = sumValue - 1;
        }
        return maxValue;
    }
}
