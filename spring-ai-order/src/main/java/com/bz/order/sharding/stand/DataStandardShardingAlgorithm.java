package com.bz.order.sharding.stand;

import org.apache.shardingsphere.infra.datanode.DataNodeInfo;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.List;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/24
 */
public class DataStandardShardingAlgorithm implements StandardShardingAlgorithm {
    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        Object value = preciseShardingValue.getValue();
        DataNodeInfo dataNodeInfo = preciseShardingValue.getDataNodeInfo();
        String columnName = preciseShardingValue.getColumnName();
        return "";
    }

    @Override
    public Collection<String> doSharding(Collection collection, RangeShardingValue rangeShardingValue) {
        return List.of();
    }

    @Override
    public String getType() {
        return "1";
    }
}
