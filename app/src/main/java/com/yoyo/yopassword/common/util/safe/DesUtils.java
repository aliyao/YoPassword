/**
 * @Title: DesUtils.java
 * @Package com.jzmjob.jzm.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Frank
 * @date 2014年8月14日 上午10:55:31
 * @version V1.0
 */
package com.yoyo.yopassword.common.util.safe;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;


/**
 * 加解密
 */
public class DesUtils {

    public static String encryptThreeDESECB(String src, String keyStr) throws Exception {

        DESedeKeySpec dks = new DESedeKeySpec(keyStr.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        byte[] b = cipher.doFinal(src.getBytes("UTF-8"));

        //BASE64Encoder encoder = new BASE64Encoder();
        String resultrStr = Base64.encodeToString(b, Base64.DEFAULT).replaceAll("\r", "").replaceAll("\n", "");
        //resultrStr=replace(resultrStr,"+", "%2B");//空格换成加号(+)
        return resultrStr;

    }

    //3DESECB解密,key必须是长度大于等于 3*8 = 24 位
    public static String decryptThreeDESECB(String src, String keyStr) throws Exception {
        //--通过base64,将字符串转成byte数组
        // BASE64Decoder decoder = new BASE64Decoder();
        //  byte[] bytesrc = Base64.decodeBuffer(src);
        byte[] bytesrc = Base64.decode(src, Base64.DEFAULT);
        //--解密的key
        DESedeKeySpec dks = new DESedeKeySpec(keyStr.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);

        //--Chipher对象解密
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        byte[] retByte = cipher.doFinal(bytesrc);
        String resultrStr = new String(retByte);
        // resultrStr=replace(resultrStr,"+", "%2B");//空格换成加号(+)
        return resultrStr;
    }

    public static String replace(String source, String oldString,
                                 String newString) {
        StringBuffer output = new StringBuffer();
        int lengthOfSource = source.length();
        int lengthOfOld = oldString.length();
        int posStart = 0;
        int pos; //
        while ((pos = source.indexOf(oldString, posStart)) >= 0) {
            output.append(source.substring(posStart, pos));
            output.append(newString);
            posStart = pos + lengthOfOld;
        }
        if (posStart < lengthOfSource) {
            output.append(source.substring(posStart));
        }
        return output.toString();
    }

}
