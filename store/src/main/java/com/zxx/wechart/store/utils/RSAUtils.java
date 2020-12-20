package com.zxx.wechart.store.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author: 周星星
 * @DateTime: 2020/12/20 10:45
 * @Description: RSA非对称加密签名
 */
public class RSAUtils {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     * @return KeyPair 密钥对
     * @throws Exception
     */
    public static KeyPair getKeyPari() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     * @param privateKey 私钥字符串
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     * @param publicKey 公钥字符串
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     * @param data 加密数据
     * @param publicKey 加密公钥
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        //数据加密
        while (inputLen - offset > 0) {
            if (inputLen- offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            outputStream.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = outputStream.toByteArray();
        outputStream.close();
        String encryptedStr = Base64.encodeBase64String(encryptedData);
        return encryptedStr;
    }

    /**
     * RSA解密
     * @param data 解密数据
     * @param privateKey 解密私钥
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        //数据解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            outputStream.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = outputStream.toByteArray();
        outputStream.close();
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     * @param data 签名数据
     * @param privateKey 签名私钥
     * @return 签名
     * @throws Exception
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception{
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     * @param srcData 原始数据
     * @param publicKey 验签公钥
     * @param sign 签名
     * @return 验签结果
     * @throws Exception
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception{
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static void main(String[] args) {
        try {
            //生成密钥对
            KeyPair keyPair = getKeyPari();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.err.println("私钥 = " + privateKey);
            System.err.println("公钥 = " + publicKey);

            //
            String data = "?openId=001&time=19993232";
            // RSA签名
            String sign = sign(data, getPrivateKey(privateKey));
            System.err.println("签名 = " + sign);
            data = data + "@" + sign;
            String encryptData = encrypt(data, getPublicKey(publicKey));
            System.out.println("加密后内容:" + encryptData);

            // RSA解密
            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
            System.out.println("解密后内容:" + decryptData);
            String[] split = decryptData.split("@");
            // RSA验签
            boolean result = verify(split[0], getPublicKey(publicKey), split[1]);
            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
