package com.zhjs.transfer.service;

import com.zhjs.transfer.dto.TransferDTO;
import com.zhjs.transfer.entity.AccountInfo;
import com.zhjs.transfer.exception.AccountException;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @ClassName: AccountService
 * @author: zhjs
 * @createDate: 2019/5/30 下午3:13
 * @JDK: 1.8
 * @Desc: TODO
 */
public interface AccountService {

    AccountInfo queryById(Long id);

    void add(AccountInfo accountInfo);

    /**
     * 扣减金额
     * @param transferDTO
     * @return
     */
    boolean decrease(TransferDTO transferDTO) throws AccountException, MQClientException;

    /**
     * 增加金额
     * @param transferDTO
     * @return
     */
    boolean increase(TransferDTO transferDTO) throws AccountException, MQClientException;


    /**
     * 账户回滚
     * @param transferDTO
     * @return
     */
    boolean callbackAccount(TransferDTO transferDTO);

    /**
     * 修改消息状态
     * @param transferDTO
     * @return
     */
    boolean updateTask(TransferDTO transferDTO) throws AccountException;

}
