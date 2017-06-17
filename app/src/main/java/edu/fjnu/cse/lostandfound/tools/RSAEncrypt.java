package edu.fjnu.cse.lostandfound.tools;

import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by zspmh on 2016-12-04.
 */
public class RSAEncrypt {
    public static final String
            DEFAULT_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz7szIMs9Ky3m56dIgriU\n" +
                    "UoThK2tVmfentY6cL6cO2aY4Yt2VXnzrSnZvBYskc64LUfXAjlWHBvaBK/vxSsMC\n" +
                    "zobJJJ84MltllPNYiNizSvmDYeYtwjs6NmvbnkERNcOJGXdiY5wj9F7jS2/4xJUT\n" +
                    "FLIii9VPdjQcax8KLiY77iR3FnbCxbD1HhSGew67reZxEzh9/6sTv8U7pmlx6JSA\n" +
                    "b90kAdx6XTPuvGXcRNJHYwaCIvaQ99n2WFsF8SF5+4ccU6BPHglfouxsa7z4S2M2\n" +
                    "KjwEyK0qHGmcs4wOlsaninL7LvwghaeSG02rNO3jitXWBmpEYg3IdIJzaIEACBq8\n" +
                    "7QIDAQAB\n";
    public static final String
            DEFAULT_PRIVATE_KEY =
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD2vRnUfqG3oEkY\n" +
                    "qWSFLUXFhog9PFCF+TpnwkUL5cmSvvEecIxmZr9z79tV2cPbU0V79SyqKWbhZsT6\n" +
                    "4fYESbBUzcPL6c79qNpiKfr4et3UtR/f+k/V3WasuMvJi/K2w8pwkXlA0TjOxWIh\n" +
                    "iHrNwFWzwMn3o/hqD5cBcfP5qWqnIWcD/hcQ2FNOffjXOtExh+/RxiIjutA6bdTH\n" +
                    "LAYiptwLtLUbz96kbOxw2RaQ+W2cjBb/giKlSCNBVRgoYZRk2XZwqrBodaiXO1JV\n" +
                    "ZrzIOZg4PcDG+Tt/VPBlGVLLERdCtk1wsXkM+y6Q7NX5U31lmijebxcv4IkJpSBV\n" +
                    "dbRuL51FAgMBAAECggEAaNghybGldimd5eTU2eJK64wnCB5A+SzWLJDYVh3mcXzB\n" +
                    "aHXaswimAdXCFdDa8K+JrwqXWBcPqGfK+/BYYaoOS3rD4DoRwqelZGECS+Opr2Dn\n" +
                    "kn0Cv5W8s1gzAAl/sTmxI/Qs2eYNhY9KC4W66QL5Z9BcAeajAC5vtCV/zm626xzo\n" +
                    "2dDRFKP9/7bc1MTrLmxPory4jvGEjIz80UCFVeixhpIrwga8ightLjjKKbxCgVAv\n" +
                    "wT3slNyJWTshbmelRlipkiJU1Uc5qN32nS/eh7zrGBjwVMpVvzPs29AEn/3Fx+jj\n" +
                    "LFniH3o6KiVqQNDWSFiPb1mU2ZAj9qO3KU+5LRWcYQKBgQD7+GcIINFxYWaH9wWq\n" +
                    "dPlOfE7Q1orBPLv+ejVzcc/5kVkhjdLSURkUu0A7fPX3RA809/84rgpHajba5ei3\n" +
                    "/lEUGZRtNWkOOnfgTNkmAH7xGrb6/i0+Ou6n0z5tK2G0GuDL6cOJSwWvZwmLdKgo\n" +
                    "TJePaVfqXeKYl9n1mrYkDg42cwKBgQD6r0eIYQOyKcrqVX284PcT69HRTfpEZnpf\n" +
                    "sYUJecZE9SOW5iR62ih1w4PkirmWSQTtDCp2eqasNJ9azzjFnvVf0GVBlcdo3iCg\n" +
                    "xvetESp7+Tuuo7UFV7nKOhMUP9Z1alQLJlJ7jHQx1KgMnLBqRFZK13sBshozvymw\n" +
                    "TyqeTec3ZwKBgQCzs5EnxuAY0LJjPdWYoBw90YIUhy4mar5Y26fLUdxVzUeYGqHE\n" +
                    "xXnxwyooQxLY6n099bDcJ6PFEwdUTqwOrR3+C9BDotfuAd0E50nnRGtEYR0nHKf/\n" +
                    "vd//aTUYftwLqy+vvmRqQksnsYCpKOXU9+sPqL2+tH6q/YHBHDnStQOF6QKBgEzu\n" +
                    "HXWs0AWM7cicE8oy/6ANboZc98Cl8kgLolgWFtJyqElWtzs8V85MAd3Q9MM6BVj5\n" +
                    "ss5JNX0DaSPlE/cRoRuWSHyoCloUUM8GQFvMOM1y3u3pL1REZ6+3wsMkFBxUVRZW\n" +
                    "3Fkt8TqZmDjGnRz/e7vcBNOwZHoc4B5LLTkHjz1hAoGAZCdTk24SPGrjw9gDFKLU\n" +
                    "erLV4PfZU1sQgKoBryaWVBX6z5wbn+hTAssqPFu9PjEMfRhQ3DuWUhFOLjPivZ2E\n" +
                    "Wu1x+oZnZ1GKd2M8fFznx2JLu0mMUcMVsMWi4xTJYH+DOhc34c4sDFcbNxj0GnBz\n" +
                    "Il3eDCjFI7+6kqCAV/xLsTI=\n";

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;
    /**
     * 公钥
     */
    private RSAPublicKey publicKey;
    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 获取私钥
     *
     * @return当前的私钥对象
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     *
     * @return当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @throws Exception
     * @return是否成功
     */
    public void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public void loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr, Base64.DEFAULT);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @throws Exception 解密过程中的异常信息
     * @return明文
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);

            /*if (i<data.length-1){

               stringBuilder.append(' ');

            }*/
        }
        return stringBuilder.toString();
    }

    /**
     * 得到密钥字符串（经过base64编码）
     *
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = Base64.encodeToString(keyBytes, Base64.DEFAULT);
        return s;
    }
}
