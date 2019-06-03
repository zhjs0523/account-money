package com.zhjs.transfer.schedule;

import com.zhjs.transfer.contants.MQTags;
import com.zhjs.transfer.contants.TaskStatus;
import com.zhjs.transfer.dao.TransferTaskMapper;
import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.entity.TransferTask;
import com.zhjs.transfer.service.MQProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: TransferTaskDealSchedule
 * @author: zhjs
 * @createDate: 2019/6/3 下午3:27
 * @JDK: 1.8
 * @Desc: 补偿机制
 * 每隔两个小时查询一次消息表，状态为SUCCESS的消息，重新发送
 */
@Component
@Slf4j
public class TransferTaskDealSchedule {

    @Autowired
    private MQProducerService mqProducerService;

    @Autowired
    private TransferTaskMapper transferTaskMapper;

    @Value("${rocketmq.consumer.topics}")
    private String topics;

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void dealTransferTask(){
        try{
            List<TransferTask> transferTasks = transferTaskMapper.queryByStatus(TaskStatus.SUCCESS);
            if(!CollectionUtils.isEmpty(transferTasks)){
                for (TransferTask transferTask : transferTasks) {
                    TransferDTO transferDTO = new TransferDTO();
                    transferDTO.setAmount(transferTask.getAmount());
                    transferDTO.setPayeeAccount(transferTask.getPayAccountId());
                    transferDTO.setPayerAccount(transferTask.getRemark().split("-->")[1]);
                    transferDTO.setRequestId(transferTask.getTransactionId());
                    mqProducerService.send(topics, MQTags.INCREASE,String.valueOf(System.currentTimeMillis()),transferDTO);
                }
            }
        }catch(Exception e){
            log.error("补偿机制，发送mq失败",e);
        }
    }
}