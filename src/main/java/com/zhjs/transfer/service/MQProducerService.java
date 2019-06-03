package com.zhjs.transfer.service;

import com.alibaba.rocketmq.client.producer.SendCallback;
import com.zhjs.transfer.entity.MQEntity;

/**
 * @ClassName: MQProducerService
 * @author: zhjs
 * @createDate: 2019/5/31 上午10:56
 * @JDK: 1.8
 * @Desc: 消息发送接口
 */
public interface MQProducerService {

    /**
     * 同步发送MQ
     * @param topic  主题
     * @param tags   标签
     * @param mqKey   mq关键词
     * @param entity 消息实体
     */
    public void send(String topic, String tags, String mqKey,MQEntity entity);

    /**
     * 发送MQ,提供回调函数，超时时间默认3s
     * @param topic  主题
     * @param tags   标签
     * @param mqKey   mq关键词
     * @param entity 消息实体
     * @param sendCallback 回调函数
     */
    public void send(String topic,  String tags, String mqKey,MQEntity entity, SendCallback sendCallback );

    /**
     * 单向发送MQ，不等待服务器回应且没有回调函数触发，适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
     * 不需要关注消息是否发送成功
     * @param topic  主题
     * @param tags   标签
     * @param mqKey   mq关键词
     * @param entity 消息实体
     */
    public void sendOneway(String topic,  String tags,String mqKey,MQEntity entity);

}
