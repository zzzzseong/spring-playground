package me.jisung.springplayground.common.config.kafka;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if(keyBytes == null) throw new InvalidRecordException("key is null");

        // key 값이 "Custom"인 경우 0번 partition으로 전송
        if("Custom".equals(key.toString())) return 0;

        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        return Utils.toPositive(Utils.murmur2(keyBytes)) % partitions.size();
    }

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs) {}
}