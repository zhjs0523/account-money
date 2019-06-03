package com.zhjs.transfer.utils;

import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 对称加密
 * @author zhjs
 *
 */
@Slf4j
public class DESUtils {
	
	
	/**
	 *@Desc 解密
	 *@Date 2017年10月25日 下午1:43:09
	 *@Author GuoFeng
	 *@Since jdk 1.7
	 *@param key 密钥
	 *@retrun String 注意解密出错时会返回null
	 */
	public static String decryption(String key, String content){
		try {
			SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DES, key.getBytes());
			String encryptHex = des.decryptStr(content);
			return encryptHex;
		} catch (Exception e) {
			log.error("DESUtils--->解密出错", e);
		}
		return null;
	}
	
	/**
	 *@Desc 加密
	 *@Date 2017年10月25日 下午1:43:19
	 *@Author GuoFeng
	 *@Since jdk 1.7
	 *@param key 密钥
	 *@param content 加密内容
	 *@retrun String 注意解密出错时会返回null
	 */
	public static String encryption(String key, String content){
		try {
			SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DES, key.getBytes());
			String encryptHex = des.encryptHex(content);
			return encryptHex;
		} catch (Exception e) {
			log.error("DESUtils--->加密出错", e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		try{
			String key1 = "54E56EE551A2C823F7A8CE9226A7mainD91531830EB954DCA452";
			String key2 = "赵金帅";
			String key3 = "18366181234";
			String key4 = "370881199407013023";
			String key5 = "6228480252789444444";
			String key6 = "54E56EE551A2C823F7A8CE9226A7mainD91531830EB954DCA452";
			String en = encryption(key1, key2);
			String en1 = encryption(key1, key3);
			String en2 = encryption(key1, key4);
			String en3 = encryption(key1, key5);
			String en4 = encryption(key1, key6);
			String en5 = encryption(key6, "d912189d97014b5c4ebf32e3390f9e8d");
			System.out.println(en);
			System.out.println(en1);
			System.out.println(en2);
			System.out.println(en3);
			System.out.println(en4);
			System.out.println(en5);
			String de = decryption(key1, en);
			System.out.println(de);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
}
