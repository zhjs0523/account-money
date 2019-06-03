package com.zhjs.transfer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.zhjs.transfer.contants.Result;
import com.zhjs.transfer.contants.ReturnCodeEnum;
import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.dto.TransferDocDTO;
import com.zhjs.transfer.exception.AccountException;
import com.zhjs.transfer.service.AccountService;
import com.zhjs.transfer.service.TransferService;
import com.zhjs.transfer.utils.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TransferServiceImpl
 * @author: zhjs
 * @createDate: 2019/5/31 下午12:01
 * @JDK: 1.8
 * @Desc: 转账接口实现 A->B
 */
@Service
@Slf4j
public class TransferServiceImpl implements TransferService {

    @Autowired
    private DefaultMQProducer producer;

    @Autowired
    private AccountService accountService;

    @Override
    public Result transferMoney(TransferDocDTO transferDocDTO) {
        /**
         * 1.解密验签
         * 2.验证参数的完整性
         * 3.存放转账任务记录，做幂等处理
         * 4.扣减A账户余额（做余额校验,乐观锁控制）
         * 5.发送消息到mq
         * 6.增加B余额
         *   6.1 增加B余额成功
         *   6.2 增加B余额失败，重试3次，回滚
         *   6.3 增加B余额成功，通知A修改最终的状态
         *   // TODO  非最终状态的自动补偿
         */

        Result result = new Result();
        Boolean flag = RSAUtil.verify(transferDocDTO.getTransferInfo(),transferDocDTO.getSignInfo());
        if(!flag){
            //验签失败
            result.setCode(ReturnCodeEnum.VERIFY_FAIL.getCode());
            result.setMsg(ReturnCodeEnum.VERIFY_FAIL.getMsg());
            result.setSuccess(false);
            return result;
        }
        TransferDTO transferDTO = JSON.parseObject(transferDocDTO.getTransferInfo(), TransferDTO.class);
        if(null == transferDTO
                || null == transferDTO.getAmount() || null == transferDTO.getRequestId()
                || null == transferDTO.getPayeeAccount() || null == transferDTO.getPayerAccount()){
            result.setCode(ReturnCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ReturnCodeEnum.PARAM_LACK.getMsg());
            result.setSuccess(false);
            return result;
        }

        try{
            boolean success = accountService.decrease(transferDTO);
            result.setSuccess(success);
            result.setCode(ReturnCodeEnum.SUCCESS.getCode());
            result.setMsg(ReturnCodeEnum.SUCCESS.getMsg());
        }catch (AccountException e){
            log.error("账户异常",e);
            result.setSuccess(false);
            result.setCode(ReturnCodeEnum.ACCOUNT_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.ACCOUNT_ERROR.getMsg());
        }catch (MQClientException e){
            log.error("消息发送失败",e);
            result.setSuccess(false);
            result.setCode(ReturnCodeEnum.MSG_SEND_FAIL.getCode());
            result.setMsg(ReturnCodeEnum.MSG_SEND_FAIL.getMsg());
        }catch(Exception e){
            log.error("系统异常",e);
            result.setSuccess(false);
            result.setCode(ReturnCodeEnum.FAIL.getCode());
            result.setMsg(ReturnCodeEnum.FAIL.getMsg());
        }
        return result;
    }

}
