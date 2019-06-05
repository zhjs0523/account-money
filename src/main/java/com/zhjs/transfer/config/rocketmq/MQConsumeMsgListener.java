package com.zhjs.transfer.config.rocketmq;

import com.zhjs.transfer.contants.MQTags;
import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.service.AccountService;
import com.zhjs.transfer.utils.SerializableUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: MQConsumeMsgListener
 * @author: zhjs
 * @createDate: 2019/5/31 上午10:24
 * @JDK: 1.8
 * @Desc: 消费者监听器  无序消费
 */
@Component
public class MQConsumeMsgListener implements MessageListenerConcurrently {
    private static final Logger logger = LoggerFactory.getLogger(MQConsumeMsgListener.class);

    @Value("${rocketmq.consumer.topics}")
    private String topics;

    @Autowired
    private AccountService accountService;

    /**
     *  默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
     *  不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(msgs)){
            logger.info("接受到的消息为空，不处理，直接返回成功");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        try{
            MessageExt messageExt = msgs.get(0);
            logger.info("接受到的消息为："+messageExt.toString());
            if(topics.equals(messageExt.getTopic())){
                if(messageExt.getReconsumeTimes() == 3){
                    //重试消费了三次，无需再次消费，返回成功
                    logger.info("已经重发三次，无需再发");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                String tags = messageExt.getTags();
                TransferDTO transferDTO = SerializableUtil.parse(messageExt.getBody(), TransferDTO.class);
                switch (tags){
                    //增加余额
                    case MQTags.INCREASE:
                        accountService.increase(transferDTO);
                        break;
                    //增加余额成功
                    case MQTags.INCREASE_SUCCESS:
                        accountService.updateTask(transferDTO);
                        break;
                    //增加余额失败
                    case MQTags.INCREASE_FAIL:
                        break;
                    //账户余额回滚
                    case MQTags.ACCOUNT_CALLBACK:
                        accountService.callbackAccount(transferDTO);
                        break;
                    default:
                        break;
                }
            }
            // 如果没有return success ，consumer会重新消费该消息，直到return success
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch(Exception e){
            logger.error("consumer consume fail");
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}