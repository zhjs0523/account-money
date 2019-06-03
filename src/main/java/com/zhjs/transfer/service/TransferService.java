package com.zhjs.transfer.service;

import com.zhjs.transfer.contants.Result;
import com.zhjs.transfer.dto.TransferDocDTO;

/**
 * @ClassName: TransferService
 * @author: zhjs
 * @createDate: 2019/5/31 下午12:01
 * @JDK: 1.8
 * @Desc: 转账service
 */
public interface TransferService {

    /**
     * 转账
     * @param transferDocDTO 转账内容实体
     * @return
     */
    Result transferMoney(TransferDocDTO transferDocDTO);
}
