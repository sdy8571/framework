package com.framework.sequence.sequence;

import com.framework.sequence.config.SegmentSequenceModel;
import com.framework.sequence.config.SequenceConfig;
import com.framework.sequence.config.SequenceModel;
import com.framework.sequence.exception.SequenceException;
import com.framework.sequence.generator.GeneratorLoader;
import com.framework.sequence.generator.ISequenceGenerator;
import com.framework.sequence.generator.impl.DbNumberStringSequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
@Slf4j
@Component
public class SequenceConfigLoader implements CommandLineRunner {

    @Resource
    private SequenceConfig sequenceConfig;
    @Resource
    private GeneratorLoader generatorLoader;
    @Resource
    private SequenceTableGenerator sequenceTableGenerator;

    private static final Map<String, SequenceModel> sequencesConfigCache = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {

        boolean hasDbSequence = false;
        List<SequenceModel> models = sequenceConfig.getSequenceConfigs();

        if (!CollectionUtils.isEmpty(models)) {
            for (SequenceModel item : models) {

                if (sequencesConfigCache.get(item.getId()) == null) {
                    sequencesConfigCache.put(item.getId(), item);
                } else {
                    log.error("sequence id[{}] 重复", item.getId());
                    System.exit(0);
                }

                List<SegmentSequenceModel> segmentSequenceModels = item.getSegmentConfigs();
                if (CollectionUtils.isEmpty(segmentSequenceModels)) {
                    log.error("sequence id[{}] 缺少属性[segmentConfigs]配置", item.getId());
                    System.exit(0);
                }

                for (SegmentSequenceModel segmentSequenceModel : segmentSequenceModels) {
                    ISequenceGenerator generator = generatorLoader.getSequenceGenerator(segmentSequenceModel.getType());
                    if (generator == null) {
                        log.error("no sequenceGenerator named {}", segmentSequenceModel.getType());
                        System.exit(0);
                    }
                    if (!hasDbSequence && DbNumberStringSequenceGenerator.type.equals(generator.getType())) {
                        hasDbSequence = true;
                    }
                    // 检查构造器配置
                    try {
                        generator.checkParams(segmentSequenceModel);
                    } catch (SequenceException e) {
                        log.error("sequence 配置有误：{}", e.getMessage());
                        System.exit(0);
                    }
                }

            }
        }

        // 配置项检查通过后，如果有DBNumber类型sequence，则检查表是否存在，不存在则创建
        if (hasDbSequence) {
            sequenceTableGenerator.getSequenceDbType();
            sequenceTableGenerator.generatorSequenceTable();
        }

        // sequence segment generator init
        if (!CollectionUtils.isEmpty(models)) {
            models.forEach(item -> {
                item.getSegmentConfigs().forEach(
                    subItem -> {
                        ISequenceGenerator generator = generatorLoader.getSequenceGenerator(subItem.getType());
                        generator.init(subItem);
                    }
                );
            });
        }

    }

    /**
     * sequence缓存
     *
     * @param id
     * @return
     */
    public SequenceModel getSequenceModel(String id) {

        return sequencesConfigCache.get(id);
    }

}
