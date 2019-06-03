package com.zhjs.transfer.config.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @ClassName: SingleKeyDBSharding
 * @author: zhjs
 * @createDate: 2019/5/30 上午11:50
 * @JDK: 1.8
 * @Desc: 分库策略算法
 */
public class SingleKeyDBSharding implements SingleKeyDatabaseShardingAlgorithm<Integer>{

    private Integer dbCount = 2;

    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<Integer> shardingValue) {
        int hashCode = String.valueOf(shardingValue.getValue()).hashCode();
        for (String each : collection) {
            if(each.endsWith(Math.abs(hashCode) % dbCount + "")){
                return each;
            }
        }
        return null;
    }

    @Override
    public Collection<String> doInSharding(Collection<String> collection, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        Collection<Integer> values = shardingValue.getValues();
        for (Integer value : values) {
            int hashCode = String.valueOf(value).hashCode();
            for (String dataSourceName : collection) {
                if (dataSourceName.endsWith( Math.abs(hashCode) % dbCount + "")) {
                    result.add(dataSourceName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection collection, ShardingValue shardingValue) {
        return null;
    }
}
