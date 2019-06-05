package com.zhjs.transfer.config.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @ClassName: MQProducerConfigutation
 * @author: zhjs
 * @createDate: 2019/5/31 上午10:07
 * @JDK: 1.8
 * @Desc: rocketmq生产者配置
 */
@SpringBootConfiguration
public class MQProducerConfiguration {
    public static final Logger LOGGER = LoggerFactory.getLogger(MQProducerConfiguration.class);
    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;
    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;
    /**
     * 消息最大大小，默认4M
     */
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize ;
    /**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Bean
    public DefaultMQProducer getRocketMQProducer(){
        DefaultMQProducer producer = null;
        try {
            if (StringUtils.isEmpty(this.groupName)) {
                throw new MQClientException("groupName is blank",null);
            }
            if (StringUtils.isEmpty(this.namesrvAddr)) {
                throw new MQClientException("nameServerAddr is blank",null);
            }

            producer = new DefaultMQProducer(this.groupName);
            producer.setNamesrvAddr(this.namesrvAddr);
//            producer.setCreateTopicKey(topics);
            //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
            //producer.setInstanceName(instanceName);
            if(this.maxMessageSize!=null){
                producer.setMaxMessageSize(this.maxMessageSize);
            }
            if(this.sendMsgTimeout!=null){
                producer.setSendMsgTimeout(this.sendMsgTimeout);
            }
            //如果发送消息失败，设置重试次数，默认为2次
            if(this.retryTimesWhenSendFailed!=null){
                producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
            }
            producer.start();

            LOGGER.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , this.groupName, this.namesrvAddr));

        } catch (MQClientException e) {
            LOGGER.error(String.format("producer is error {}"
                    , e.getMessage(),e));
        }
        return producer;
    }
}
