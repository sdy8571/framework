package com.framework.sequence.numbersequence;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author sdy
 * @date 2020/1/7
 * @Version 1.0
 * @Description
 */
@Data
public class SequenceDbConfig {

    private String seqName;
    private BigDecimal minVal = BigDecimal.ONE;
    private BigDecimal maxVal = BigDecimal.ONE;
    private BigDecimal curVal = BigDecimal.ONE;
    private BigDecimal stepVal = BigDecimal.ONE;
    private String cycle = "Y";

}
