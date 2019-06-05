package com.zhjs.transfer.service.impl;

import com.zhjs.transfer.entity.MQEntity;
import com.zhjs.transfer.service.MQProducerService;
import com.zhjs.transfer.utils.SerializableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ClassName: MQProducerServiceImpl
 * @author: zhjs
 * @createDate: 2019/5/31 上午10:56
 * @JDK: 1.8
 * @Desc: 消息发送接口实现
 */
@Service
@Slf4j
public class MQProducerServiceImpl implements MQProducerService{

    @Autowired
    private DefaultMQProducer producer;

    @Override
    public void send(String topic, String tags, String mqKey, MQEntity entity) throws MQClientException {
        entity.setMqKey(mqKey);
        log.info("业务:{},tags:{},mqKey:{},entity:{}",topic, tags, mqKey, entity);
        Message msg = new Message(topic,tags,mqKey, SerializableUtil.toByte(entity));

        try{
            producer.send(msg);
        }catch(Exception e){
            log.error("消息发送失败，msg:{}",msg);
            throw new MQClientException(0,"消息发送失败！");
        }
    }

    @Override
    public void send(String topic,  String tags, String mqKey, MQEntity entity, SendCallback sendCallback) {
        entity.setMqKey(mqKey);
        log.info("业务:{},tags:{},mqKey:{},entity:{}",topic, tags, mqKey, entity);
        Message msg = new Message(topic,tags,mqKey, SerializableUtil.toByte(entity));

        try{
            producer.send(msg,sendCallback);
        }catch(Exception e){
            log.error("消息发送失败，msg:{}",msg);
            throw new RuntimeException("消息发送失败！",e);
        }
    }

    @Override
    public void sendOneway(String topic,  String tags, String mqKey, MQEntity entity) {
        entity.setMqKey(mqKey);
        log.info("业务:{},tags:{},mqKey:{},entity:{}",topic, tags, mqKey, entity);
        Message msg = new Message(topic,tags,mqKey, SerializableUtil.toByte(entity));

        try{
            producer.sendOneway(msg);
        }catch(Exception e){
            log.error("消息发送失败，msg:{}",msg);
            throw new RuntimeException("消息发送失败！",e);
        }
    }
}
