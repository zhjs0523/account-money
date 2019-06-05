package com.zhjs.transfer.config.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @ClassName: MQConsumerConfiguration
 * @author: zhjs
 * @createDate: 2019/5/31 上午10:22
 * @JDK: 1.8
 * @Desc: TODO
 */
@SpringBootConfiguration
public class MQConsumerConfiguration {
    public static final Logger LOGGER = LoggerFactory.getLogger(MQConsumerConfiguration.class);
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
    @Autowired
    private MQConsumeMsgListener mqMessageListener;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        try {
            if (StringUtils.isEmpty(groupName)){
                throw new MQClientException("groupName is null !!!",null);
            }
            if (StringUtils.isEmpty(namesrvAddr)){
                throw new MQClientException("namesrvAddr is null !!!",null);
            }
            if(StringUtils.isEmpty(topics)){
                throw new MQClientException("topics is null !!!",null);
            }

            consumer.setNamesrvAddr(namesrvAddr);
            consumer.setConsumeThreadMin(consumeThreadMin);
            consumer.setConsumeThreadMax(consumeThreadMax);
            consumer.registerMessageListener(mqMessageListener);
            /**
             * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
             * 如果非第一次启动，那么按照上次消费的位置继续消费
             */
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            /**
             * 设置消费模型，集群还是广播，默认为集群
             */
            //consumer.setMessageModel(MessageModel.CLUSTERING);
            /**
             * 设置一次消费消息的条数，默认为1条
             */
            consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);

            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
            consumer.subscribe(topics,"*");
            consumer.start();
            LOGGER.info("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr);
        }catch (MQClientException e){
            LOGGER.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr,e);
        }
        return consumer;
    }
}
