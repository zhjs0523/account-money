package com.zhjs.transfer.dto;

import com.zhjs.transfer.entity.MQEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: TransferDTO
 * @author: zhjs
 * @createDate: 2019/5/31 上午11:50
 * @JDK: 1.8
 * @Desc: 转账信息DTO
 */
@Data
public class TransferDTO extends MQEntity implements Serializable{

    /**
     *  转入账户
     */
    private String payeeAccount;

    /**
     * 转出账户
     */
    private String payerAccount;

    /**
     * 转账金额
     */
    private Long amount;

    /**
     * 请求流水号
     */
    private String requestId;
}
