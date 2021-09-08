package com.hainu.system.config.sharding;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * @author lr
 * 分表的自定义规则类(精确)
 */
public class DefaultTableShardingAlgorithm  implements PreciseShardingAlgorithm<Timestamp> {

    public static final String DB_SHARD_TIME_FORMAT = "yyyyMM";


    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Timestamp> preciseShardingValue) {
        LocalDateTime createTime = preciseShardingValue.getValue().toLocalDateTime();
        String timeValue = createTime.format(DateTimeFormatter.ofPattern(DB_SHARD_TIME_FORMAT));
        String columnName = preciseShardingValue.getColumnName();
        // 需要分库的逻辑表
        String table = preciseShardingValue.getLogicTableName();
        if (StringUtils.isBlank(timeValue)) {
            throw new UnsupportedOperationException(columnName + ":列，分表精确分片值为NULL;");
        }
        for (String each : collection) {
            if (each.startsWith(table)) {
                return table + "_" + timeValue;
            }
        }
        return table;
    }
}