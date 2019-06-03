package com.zhjs.transfer.config.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @ClassName: SingleKeyTableSharding
 * @author: zhjs
 * @createDate: 2019/5/30 下午1:54
 * @JDK: 1.8
 * @Desc: 分表策略算法
 */
public class SingleKeyTableSharding implements SingleKeyTableShardingAlgorithm<Integer>{

    private int tbCount = 2;

    @Override
    public String doEqualSharding(final Collection<String> availableTargetNames, final ShardingValue<Integer> shardingValue) {
        int hashCode = String.valueOf(shardingValue.getValue()).hashCode();
        for (String each : availableTargetNames) {
            if (each.endsWith(Math.abs(hashCode) % tbCount + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doInSharding(final Collection<String> availableTargetNames, final ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Collection<Integer> values = shardingValue.getValues();
        for (Integer value : values) {
            int hashCode = String.valueOf(value).hashCode();
            for (String tableNames : availableTargetNames) {
                if (tableNames.endsWith(Math.abs(hashCode) % tbCount + "")) {
                    result.add(tableNames);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(final Collection<String> availableTargetNames, final ShardingValue<Integer> shardingValue) {
        return null;
    }
}
