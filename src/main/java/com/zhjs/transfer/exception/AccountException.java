package com.zhjs.transfer.exception;

/**
 * @ClassName: AccountException
 * @author: zhjs
 * @createDate: 2019/5/31 下午6:17
 * @JDK: 1.8
 * @Desc: 账户异常类
 */
public class AccountException extends Exception {

    public AccountException(String msg){
        super(msg);
    }
}
