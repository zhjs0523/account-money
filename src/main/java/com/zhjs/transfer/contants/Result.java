package com.zhjs.transfer.contants;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: Result
 * @author: zhjs
 * @createDate: 2019/5/31 下午2:05
 * @JDK: 1.8
 * @Desc: 返回结果
 */
@Data
public class Result implements Serializable{

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回码
     */
    private String code;

    /**
     * 成功标志
     */
    private Boolean success;
}
