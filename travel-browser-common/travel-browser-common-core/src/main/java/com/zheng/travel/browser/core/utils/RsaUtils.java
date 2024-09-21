package com.zheng.travel.browser.core.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密解密
 *
 * @author ruoyi
 **/
public class RsaUtils {

    /**
     * 公钥解密
     *
     * @param publicKeyString 公钥
     * @param text            待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.getDecoder().decode(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyString 私钥
     * @param text             待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text             待解密文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.getDecoder().decode(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString 公钥
     * @param text            待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return 生成后的公私钥信息
     */
    public static RsaKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        return new RsaKeyPair(publicKeyString, privateKeyString);
    }

    /**
     * RSA密钥对对象
     */
    public static class RsaKeyPair {
        private final String publicKey;
        private final String privateKey;

        public RsaKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }


    /**
     * 只需调用一次 生成/打印新的公钥私钥  并测试是否可用
     * 控制台打印结果，解密成功 则将打印的公钥私钥重新赋值给工具类的 privateKey 、 publicKey
     *
     * @throws NoSuchAlgorithmException
     */
    public static void printNewPubKeypriKey() {
        //调用 RsaUtils.generateKeyPair() 生成RSA公钥秘钥
        String tmpPriKey = "";//私钥
        String tmpPubKey = "";//公钥
        try {
            RsaKeyPair rsaKeyPair = RsaUtils.generateKeyPair();
            tmpPriKey = rsaKeyPair.getPrivateKey();
            tmpPubKey = rsaKeyPair.getPublicKey();
            System.out.println("私钥：" + tmpPriKey);
            System.out.println("公钥：" + tmpPubKey);
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("生成秘钥公钥失败");
        }
        //公钥加密、私钥解密
        try {
            String txt = "123456789,13000000001,oUpF8uMuAJO_M2pxb1Q9zNjWeS6oob1Q9zNjWeS6oQ9zNjW,1672914158,1672914158,啊";//注意需要加密的原文长度不要太长 过长的字符串会导致加解密失败
            System.out.println("加密前原文：" + txt);//加密后文本
            String rsaText = RsaUtils.encryptByPublicKey(tmpPubKey, txt);//公钥加密 ！！！
            System.out.println("密文：" + rsaText);//加密后文本
            System.out.println("解密后原文：" + RsaUtils.decryptByPrivateKey(tmpPriKey, rsaText));//私钥解密 ！！！
        } catch (BadPaddingException e) {
            System.out.println(e.getStackTrace());
            System.out.println("加解密失败");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        RsaUtils.printNewPubKeypriKey();
    }
}