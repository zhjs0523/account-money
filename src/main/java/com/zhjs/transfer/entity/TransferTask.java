package com.zhjs.transfer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TransferTask {
    /** 
     * id
     * @Author  zhjs
    **/
    private Long id;

    /** 
     * 交易单号
     * @Author  zhjs
    **/
    private String transactionId;

    /** 
     * 支付账户编号
     * @Author  zhjs
    **/
    private String payAccountId;

    /** 
     * 方向
     * @Author  zhjs
    **/
    private Integer direction;

    /** 
     * 交易金额(分)
     * @Author  zhjs
    **/
    private Long amount;

    /** 
     * 任务状态
     * @Author  zhjs
    **/
    private Integer status;

    /** 
     * 备注
     * @Author  zhjs
    **/
    private String remark;

    /** 
     * 创建时间
     * @Author  zhjs
    **/
    private Date created;

    /** 
     * 修改时间
     * @Author  zhjs
    **/
    private Date modified;
}