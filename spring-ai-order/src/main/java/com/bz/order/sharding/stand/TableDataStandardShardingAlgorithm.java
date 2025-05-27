package com.bz.order.sharding.stand;

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
public class TableDataStandardShardingAlgorithm implements StandardShardingAlgorithm {

    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        return "";
    }

    @Override
    public Collection<String> doSharding(Collection collection, RangeShardingValue rangeShardingValue) {
        return List.of();
    }

    @Override
    public String getType() {
        return StandardShardingAlgorithm.super.getType();
    }
}
