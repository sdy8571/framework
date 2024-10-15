package com.framework.sequence.numbersequence;

import com.framework.sequence.numbersequence.support.DbSupport;
import lombok.Data;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sdy
 * @date 2020/1/6
 * @Version 1.0
 * @Description
 */
@Data
public class DbNumberSequenceObject {

    private String seqName;
    private BigDecimal minVal = BigDecimal.ONE;
    private BigDecimal maxVal = BigDecimal.ONE;
    private BigDecimal curVal = BigDecimal.ONE;
    private BigDecimal stepVal = BigDecimal.ONE;
    private String cycle = "Y";

    private String length;
    private String leftPadding;

    private DataSourceTransactionManager dataSourceTransactionManager;
    private String databaseType;

    private BigDecimal curValCache;
    private BigDecimal maxValCache;
    private Lock lock;

    public DbNumberSequenceObject(String seqName) {
        super();
        this.seqName = seqName;
        this.curValCache = BigDecimal.ZERO;
        this.maxValCache = BigDecimal.ZERO;
        this.lock = new ReentrantLock();
    }

    public void reset() {
        DbSupport.loadSequence(this);
    }

    public BigDecimal getNextval() {
        lock.lock();
        try {
            if (curValCache.compareTo(maxValCache) >= 0) {
                reset();
            }
            BigDecimal value = curValCache;
            curValCache = curValCache.add(BigDecimal.ONE);
            return value;
        } finally {
            lock.unlock();
        }
    }

}
