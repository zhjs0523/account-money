package com.zhjs.transfer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AccountInfo {
    /** 
     * id
     * @Author  zhjs
    **/
    private Long id;

    /** 
     * 支付账户编号
     * @Author  zhjs
    **/
    private String payAccountId;

    /** 
     * 金额(分)
     * @Author  zhjs
    **/
    private Long amount;

    /** 
     * 版本编号
     * @Author  zhjs
    **/
    private Long versionId;

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