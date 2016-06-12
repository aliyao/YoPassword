/**
 * @Title: DesUtils.java
 * @Package com.jzmjob.jzm.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Frank
 * @date 2014年8月14日 上午10:55:31
 * @version V1.0
 */
package com.yoyo.yopassword.common.util.safe;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 加解密
 */
public class DesUtils {

    /**
     * 3DS 加密
     *
     * @author liyunlong_88@126.com
     */

    private static final String Algorithm = "DESede/CBC/PKCS5Padding"; // 定义加密算法, 可用
    // DES,DESede,Blowfish，DESede/CBC/PKCS5Padding

    // keybyte 为加密密钥，长度为 24 字节

    // src 为被加密的数据缓冲区（源）
    /*
     * private static SecretKey deskey = null;
     *
     * public static void getKey(byte[] strKey) { try { KeyGenerator _generator
     * = KeyGenerator.getInstance("DES"); _generator.init(new
     * SecureRandom(strKey)); deskey = _generator.generateKey(); _generator =
     * null; } catch (Exception e) {e.printStackTrace(); } }
     */
    public static byte[] encryptMode(String iv, String key, String src) {

        try {
            byte[] keybyte = key.getBytes();
            byte[] rand = new byte[8];
            rand = iv.getBytes();
            // 用随即数生成初始向量

            /*
             * Random r=new Random(); r.nextBytes(rand);
             */
            IvParameterSpec ivp = new IvParameterSpec(rand);

            // 生成密钥

            // SecureRandom sr = new SecureRandom();
            DESedeKeySpec dks = new DESedeKeySpec(keybyte);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // IvParameterSpec iv = new IvParameterSpec(PASSWORD_IV.getBytes());
            /*
             * Cipher cipher = Cipher.getInstance("DESede");
             * cipher.init(Cipher.ENCRYPT_MODE, securekey, ivp, sr); return new
             * String(Hex.encodeHex(cipher.doFinal(str.getBytes())));
             */

            // 加密

            Cipher c1 = Cipher.getInstance(Algorithm);

            c1.init(Cipher.ENCRYPT_MODE, securekey, ivp);

            return c1.doFinal(src.getBytes());// 在单一方面的加密或解密

        } catch (java.security.NoSuchAlgorithmException e1) {

            // TODO: handle exception

            e1.printStackTrace();

        } catch (javax.crypto.NoSuchPaddingException e2) {

            e2.printStackTrace();

        } catch (java.lang.Exception e3) {

            e3.printStackTrace();

        }

        return null;

    }

    // keybyte 为加密密钥，长度为 24 字节

    // src 为加密后的缓冲区

    public static byte[] decryptMode(String iv, String key, byte[] src) {

        try {
            byte[] srcbytes = src;
            byte[] keybyte = key.getBytes();
            byte[] rand = new byte[8];
            rand = iv.getBytes();
            // 用随即数生成初始向量

            /*
             * Random r=new Random(); r.nextBytes(rand);
             */
            IvParameterSpec ivp = new IvParameterSpec(rand);

            // 生成密钥

            SecureRandom sr = new SecureRandom();
            DESedeKeySpec dks = new DESedeKeySpec(keybyte);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(dks);

            // 解密

            Cipher c1 = Cipher.getInstance(Algorithm);

            c1.init(Cipher.DECRYPT_MODE, securekey, ivp);

            /*
             * int len = src.getBytes().length; byte[] zero = { 0x00, 0x00,
             * 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }; if (len < 8) { srcbytes =
             * new byte[8]; System.arraycopy(src.getBytes(), 0, srcbytes, 0,
             * len); System.arraycopy(zero, len, srcbytes, len, 8 - len); } else
             * {srcbytes = src.getBytes(); }
             */

            return c1.doFinal(srcbytes);

        } catch (java.security.NoSuchAlgorithmException e1) {

            // TODO: handle exception

            e1.printStackTrace();

        } catch (javax.crypto.NoSuchPaddingException e2) {

            e2.printStackTrace();

        } catch (java.lang.Exception e3) {

            e3.printStackTrace();

        }

        return null;

    }

    // 转换成十六进制字符串

    public static String byte2Hex(byte[] b) {

        String hs = "";

        String stmp = "";

        for (int n = 0; n < b.length; n++) {

            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

            if (stmp.length() == 1) {

                hs = hs + "0" + stmp;

            } else {

                hs = hs + stmp;

            }

            if (n < b.length - 1)
                hs = hs + ":";

        }

        return hs.toUpperCase();

    }

    public static final String encodeHex(byte bytes[]) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 16)
                buf.append("0");
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static final byte[] decodeHex(String hex) {
        char chars[] = hex.toCharArray();
        byte bytes[] = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2) {
            int newByte = 0;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte) newByte;
            byteCount++;
        }
        return bytes;
    }

    private static final byte hexCharToByte(char ch) {
        switch (ch) {
            case 48: // '0'
                return 0;

            case 49: // '1'
                return 1;

            case 50: // '2'
                return 2;

            case 51: // '3'
                return 3;

            case 52: // '4'
                return 4;

            case 53: // '5'
                return 5;

            case 54: // '6'
                return 6;

            case 55: // '7'
                return 7;

            case 56: // '8'
                return 8;

            case 57: // '9'
                return 9;

            case 97: // 'a'
                return 10;

            case 98: // 'b'
                return 11;

            case 99: // 'c'
                return 12;

            case 100: // 'd'
                return 13;

            case 101: // 'e'
                return 14;

            case 102: // 'f'
                return 15;

            case 58: // ':'
            case 59: // ';'
            case 60: // '<'
            case 61: // '='
            case 62: // '>'
            case 63: // '?'
            case 64: // '@'
            case 65: // 'A'
            case 66: // 'B'
            case 67: // 'C'
            case 68: // 'D'
            case 69: // 'E'
            case 70: // 'F'
            case 71: // 'G'
            case 72: // 'H'
            case 73: // 'I'
            case 74: // 'J'
            case 75: // 'K'
            case 76: // 'L'
            case 77: // 'M'
            case 78: // 'N'
            case 79: // 'O'
            case 80: // 'P'
            case 81: // 'Q'
            case 82: // 'R'
            case 83: // 'S'
            case 84: // 'T'
            case 85: // 'U'
            case 86: // 'V'
            case 87: // 'W'
            case 88: // 'X'
            case 89: // 'Y'
            case 90: // 'Z'
            case 91: // '['
            case 92: // '\\'
            case 93: // ']'
            case 94: // '^'
            case 95: // '_'
            case 96: // '`'
            default:
                return 0;
        }
    }

    public static void main(String[] args) {

        // TODO Auto-generated method stub

        // 添加新安全算法, 如果用 JCE 就要把它添加进去

        // Security.addProvider(new com.sun.crypto.provider.SunJCE());

        /*
         * final byte[] keyBytes = { 0x01, 0x02, 0x03, 0x04,
         *
         * (byte) 0x05, 0x06, 0x07, 0x08, 0x09, 0x00, 0x01, 0x02,
         *
         * (byte) 0x03,
         *
         * (byte) 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
         *
         * (byte) 0x00, 0x01, 0x02, 0x03,
         *
         * (byte) 0x04
         *
         * }; // 24 字节的密钥
         */
        String szSrc = "1";

        System.out.println("加密前的字符串:" + szSrc);

        byte[] encoded = encryptMode("12345678", "123456789012345678943210",
                szSrc);

        System.out.println("加密后的字符串:" + encodeHex(encoded));

        byte[] srcBytes = decryptMode("12345678", "123456789012345678943210",
                "c5e8faaf1a0e52ae".getBytes());

        System.out.println("解密后的字符串:" + (new String(srcBytes)));

    }
}
