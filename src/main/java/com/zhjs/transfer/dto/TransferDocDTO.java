package com.zhjs.transfer.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: TransferDocDTO
 * @author: zhjs
 * @createDate: 2019/5/31 上午11:54
 * @JDK: 1.8
 * @Desc: 转账内容
 */
@Data
public class TransferDocDTO implements Serializable{

    /**
     * 应用编号
     */
    private String appId;


    /**
     * 转账信息
     */
    private String transferInfo;

    /**
     * 签名信息
     */
    private String signInfo;
}
