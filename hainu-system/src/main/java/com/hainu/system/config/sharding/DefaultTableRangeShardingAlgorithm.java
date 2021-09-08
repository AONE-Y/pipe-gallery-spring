package com.hainu.system.config.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 分表的自定义规则类(范围)
 *
 * @author lr
 */
public class DefaultTableRangeShardingAlgorithm implements RangeShardingAlgorithm<LocalDateTime> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         RangeShardingValue<LocalDateTime> rangeShardingValue) {
        Range<LocalDateTime> ranges = rangeShardingValue.getValueRange();

        LocalDateTime start = ranges.lowerEndpoint();
        LocalDateTime end = ranges.upperEndpoint();

        int startYear = start.getYear();
        int endYear = end.getYear();

        int startMonth = start.getMonthValue();
        int endMonth = end.getMonthValue();


        Collection<String> tables = new LinkedHashSet<>();
        if (start.getNano() <= end.getNano()) {
            for (String c : availableTargetNames) {
                int cMonth = Integer.parseInt(c.substring(c.length() - 6));
                if (cMonth >= Integer.parseInt("" + startYear + getMonthStr(startMonth))
                        && cMonth <= Integer.parseInt("" + endYear + getMonthStr(endMonth))) {
                    tables.add(c);
                }
            }
        }
        return tables;
    }

    private String getMonthStr(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return "" + m;
    }
}