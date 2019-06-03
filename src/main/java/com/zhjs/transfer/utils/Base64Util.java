package com.zhjs.transfer.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * @ClassName: Base64Util
 * @author: zhjs
 * @createDate: 2019/5/31 下午3:07
 * @JDK: 1.8
 * @Desc: Base64加解密
 */

public class Base64Util {

    /**
     * Base64加密
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * Base64解密
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

}
