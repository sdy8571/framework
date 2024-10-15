package com.framework.sequence.sequence.impl;

import com.framework.sequence.config.SegmentSequenceModel;
import com.framework.sequence.config.SequenceConfig;
import com.framework.sequence.config.SequenceModel;
import com.framework.sequence.exception.SequenceException;
import com.framework.sequence.generator.GeneratorLoader;
import com.framework.sequence.generator.ISequenceGenerator;
import com.framework.sequence.sequence.Sequence;
import com.framework.sequence.sequence.SequenceConfigLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sdy
 * @date 2020/1/6
 * @Version 1.0
 * @Description
 */
@Component
public class SequenceImpl implements Sequence {

    @Resource
    private SequenceConfig sequenceConfig;
    @Resource
    private GeneratorLoader generatorLoader;
    @Resource
    private SequenceConfigLoader sequenceConfigLoader;

    @Override
    public String getSequence(String id) {

        SequenceModel model = sequenceConfigLoader.getSequenceModel(id);

        if (model == null) {
            throw new SequenceException("sequence named [" + id + "] can not find");
        }

        List<SegmentSequenceModel> segments = model.getSegmentConfigs();
        StringBuilder sb = new StringBuilder();
        for (SegmentSequenceModel item : segments) {
            ISequenceGenerator generator = generatorLoader.getSequenceGenerator(item.getType());
            //            generator.init(item);
            sb.append(generator.getSequence(item));
        }

        return sb.toString();
    }
}
