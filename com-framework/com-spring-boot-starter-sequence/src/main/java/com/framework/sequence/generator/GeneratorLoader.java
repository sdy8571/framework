package com.framework.sequence.generator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sdy
 * @date 2020/1/6
 * @Version 1.0
 * @Description
 */
@Component
public class GeneratorLoader {

    @Resource
    private List<ISequenceGenerator> generators;

    private static final Map<String, ISequenceGenerator> generatorMap = new HashMap<>(5);

    public ISequenceGenerator getSequenceGenerator(String type) {

        if (!StringUtils.hasText(type)) {
            return null;
        } else {
            type = type.trim();
        }

        ISequenceGenerator sequenceGenerator = generatorMap.get(type);
        if (sequenceGenerator == null) {
            for (ISequenceGenerator item : generators) {
                if (item.getType().trim().equalsIgnoreCase(type)) {
                    sequenceGenerator = item;
                    generatorMap.put(type, sequenceGenerator);
                }
            }
        }

        return sequenceGenerator;
    }

}
