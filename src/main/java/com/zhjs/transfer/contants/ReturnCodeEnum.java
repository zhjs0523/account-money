package com.zhjs.transfer.contants;


/**
 * @ClassName: ReturnCodeEnum
 * @author: zhjs
 * @createDate: 2019/5/31 下午5:15
 * @JDK: 1.8
 * @Desc: TODO
 */
public enum ReturnCodeEnum {

    SUCCESS("00","成功"),
    PARAM_LACK("01","参数缺失"),
    MSG_SEND_FAIL("02","消息发送失败"),
    ACCOUNT_ERROR("03","账户异常"),
    SIGN_FAIL("97","签名失败"),
    VERIFY_FAIL("98","验签成功"),
    FAIL("99","失败");

    private String code;

    private String msg;

    ReturnCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
