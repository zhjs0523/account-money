package com.zhjs.transfer.service.impl;

import com.zhjs.transfer.contants.DirectionEnum;
import com.zhjs.transfer.contants.MQTags;
import com.zhjs.transfer.contants.TaskStatus;
import com.zhjs.transfer.dao.AccountInfoMapper;
import com.zhjs.transfer.dao.TransferTaskMapper;
import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.entity.AccountInfo;
import com.zhjs.transfer.entity.TransferTask;
import com.zhjs.transfer.exception.AccountException;
import com.zhjs.transfer.service.AccountService;
import com.zhjs.transfer.service.MQProducerService;
import com.zhjs.transfer.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ClassName: AccountServiceImpl
 * @author: zhjs
 * @createDate: 2019/5/30 下午3:13
 * @JDK: 1.8
 * @Desc: TODO
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private TransferTaskMapper transferTaskMapper;

    @Autowired
    private MQProducerService mqProducerService;

    @Value("${rocketmq.consumer.topics}")
    private String topics;

    @Override
    public AccountInfo queryById(Long id) {
        return accountInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(AccountInfo accountInfo) {
        accountInfoMapper.insert(accountInfo);
    }

    @Override
    public boolean decrease(TransferDTO transferDTO) throws AccountException, MQClientException {
        try{
            //存放转账任务记录，做幂等处理
            TransferTask transferTask = new TransferTask();
            transferTask.setId(SnowFlake.getSnowFlakeId());
            transferTask.setPayAccountId(transferDTO.getPayerAccount());
            transferTask.setTransactionId(transferDTO.getRequestId());
            transferTask.setDirection(DirectionEnum.DECREASE.getValue());
            transferTask.setStatus(TaskStatus.SUCCESS);
            transferTask.setAmount(transferDTO.getAmount());
            transferTask.setRemark(transferDTO.getPayerAccount() + "-->" + transferDTO.getPayeeAccount());
            transferTask.setCreated(new Date());
            transferTask.setModified(new Date());
            transferTaskMapper.insert(transferTask);
        }catch(Exception e){
            log.error("A系统添加转账任务记录失败",e);
        }
        //查询账户余额
        AccountInfo accountInfo = accountInfoMapper.queryBalance(transferDTO.getPayerAccount());
        if(null == accountInfo || accountInfo.getAmount() < transferDTO.getAmount()){
            //账户不存在或余额不足
            throw new AccountException("余额不足");
        }
        //扣减A账户余额  加乐观锁控制
        accountInfoMapper.updateAccountInfo(accountInfo.getVersionId(),accountInfo.getAmount() - transferDTO.getAmount(),transferDTO.getPayerAccount());
        //A账户扣减成功，发送消息到MQ，B账户增加余额，发送成功，提交事务，发送失败，事务回滚
        try{
            mqProducerService.send(topics, MQTags.INCREASE,String.valueOf(System.currentTimeMillis()), transferDTO);
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new MQClientException(0,"消息发送失败");
        }

        return true;
    }

    @Override
    public boolean increase(TransferDTO transferDTO) throws AccountException, MQClientException {

        try{
            //存放转账任务记录，做幂等处理
            TransferTask transferTask = new TransferTask();
            transferTask.setId(SnowFlake.getSnowFlakeId());
            transferTask.setPayAccountId(transferDTO.getPayeeAccount());
            transferTask.setTransactionId(transferDTO.getRequestId());
            transferTask.setDirection(DirectionEnum.INCREASE.getValue());
            transferTask.setStatus(TaskStatus.SUCCESS);
            transferTask.setAmount(transferDTO.getAmount());
            transferTask.setRemark(transferDTO.getPayerAccount() + "-->" + transferDTO.getPayeeAccount());
            transferTask.setCreated(new Date());
            transferTask.setModified(new Date());
            transferTaskMapper.insert(transferTask);
        }catch(Exception e){
            log.error("B系统添加转账任务记录失败",e);
        }


        //增加B账户的余额
        //发送MQ消息，更改本地消息表状态或回滚A账户余额
        try{
            AccountInfo accountInfo = accountInfoMapper.queryBalance(transferDTO.getPayeeAccount());
            //TODO  模拟一个业务失败的场景，使的A账户逆向操作
            if("风险账户".equals(accountInfo.getPayAccountId())){
                throw new AccountException("风险账户");
            }

            int result = accountInfoMapper.updateAccountInfo(accountInfo.getVersionId(),accountInfo.getAmount() + transferDTO.getAmount(),transferDTO.getPayeeAccount());
            if(result < 1){
                //回滚A账户余额
                throw new AccountException("增加余额失败，数据回滚");
            }else{
                //更新消息为最终状态
                mqProducerService.send(topics,MQTags.INCREASE_SUCCESS,String.valueOf(System.currentTimeMillis()),transferDTO);
            }
        }catch (MQClientException e){
            //自己把异常吃掉，不抛出
            //这样可以使B账户增加余额的事物提交成功，不影响后续流程，通知A系统使用补偿机制
            log.error("通知MQ失败",e);
        }catch (Exception e){
            //回滚A
            log.error("回滚A。。。",e);
            mqProducerService.send(topics,MQTags.ACCOUNT_CALLBACK,String.valueOf(System.currentTimeMillis()),transferDTO);
        }


        return true;
    }

    @Override
    public boolean callbackAccount(TransferDTO transferDTO) {

        AccountInfo accountInfo = accountInfoMapper.queryBalance(transferDTO.getPayerAccount());
        accountInfoMapper.updateAccountInfo(accountInfo.getVersionId(),accountInfo.getAmount() + transferDTO.getAmount(),transferDTO.getPayerAccount());
        //更新消息表状态为业务失败  和A账户在同一个事务中
        //处理失败的消息，定时任务处理(补偿措施  TCC)
        transferTaskMapper.updateByTransactionIdAndStatus(TaskStatus.FAILED,TaskStatus.SUCCESS,transferDTO.getRequestId());
        return true;
    }

    @Override
    public boolean updateTask(TransferDTO transferDTO) throws AccountException {
        try{
            TransferTask transferTask = transferTaskMapper.queryByTransactionIdAndPaymentId(transferDTO.getRequestId(),transferDTO.getPayerAccount());
            if(null != transferTask){
                transferTaskMapper.updateByTransactionIdAndStatus(TaskStatus.FINISHED,transferTask.getStatus(),transferTask.getTransactionId());
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            log.error("修改消息状态为最终状态失败");
            throw new AccountException("修改消息状态为最终状态失败");
        }
    }
}
