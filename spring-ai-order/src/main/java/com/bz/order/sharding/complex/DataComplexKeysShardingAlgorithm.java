package com.bz.order.sharding.complex;

import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/24
 */
public class DataComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm {


    @Override
    public Collection<String> doSharding(Collection collection, ComplexKeysShardingValue complexKeysShardingValue) {
        collection.stream().forEach(item -> {
            System.out.println(item);
        });
        Map columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        Map columnNameAndRangeValuesMap = complexKeysShardingValue.getColumnNameAndRangeValuesMap();
        String logicTableName = complexKeysShardingValue.getLogicTableName();
        return List.of();
    }
}
