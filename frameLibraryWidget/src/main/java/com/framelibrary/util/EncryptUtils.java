package com.framelibrary.util;

import android.os.Build;

import com.framelibrary.util.logutil.LoggerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : 关于加密的工具
 * </pre>
 */
public final class EncryptUtils {

    private EncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    ///////////////////////////////////////////////////////////////////////////
    // hash encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 返回MD2加密的十六进制字符串.
     *
     * @param data The data.
     * @return the hex string of MD2 encryption
     */
    public static String encryptMD2ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptMD2ToString(data.getBytes());
    }

    /**
     * Return the hex string of MD2 encryption.
     *
     * @param data The data.
     * @return the hex string of MD2 encryption
     */
    public static String encryptMD2ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptMD2(data));
    }

    /**
     * Return the bytes of MD2 encryption.
     *
     * @param data The data.
     * @return the bytes of MD2 encryption
     */
    public static byte[] encryptMD2(final byte[] data) {
        return hashTemplate(data, "MD2");
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMD5ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptMD5ToString(data.getBytes());
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMD5ToString(final String data, final String salt) {
        if (data == null && salt == null) return "";
        if (salt == null) return ConvertUtils.bytes2HexString(encryptMD5(data.getBytes()));
        if (data == null) return ConvertUtils.bytes2HexString(encryptMD5(salt.getBytes()));
        return ConvertUtils.bytes2HexString(encryptMD5((data + salt).getBytes()));
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMD5ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptMD5(data));
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMD5ToString(final byte[] data, final byte[] salt) {
        if (data == null && salt == null) return "";
        if (salt == null) return ConvertUtils.bytes2HexString(encryptMD5(data));
        if (data == null) return ConvertUtils.bytes2HexString(encryptMD5(salt));
        byte[] dataSalt = new byte[data.length + salt.length];
        System.arraycopy(data, 0, dataSalt, 0, data.length);
        System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
        return ConvertUtils.bytes2HexString(encryptMD5(dataSalt));
    }

    /**
     * Return the bytes of MD5 encryption.
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    public static byte[] encryptMD5(final byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * Return the hex string of file's MD5 encryption.
     *
     * @param filePath The path of file.
     * @return the hex string of file's MD5 encryption
     */
    public static String encryptMD5File2String(final String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return encryptMD5File2String(file);
    }

    /**
     * Return the bytes of file's MD5 encryption.
     *
     * @param filePath The path of file.
     * @return the bytes of file's MD5 encryption
     */
    public static byte[] encryptMD5File(final String filePath) {
        File file = StringUtils.isSpace(filePath) ? null : new File(filePath);
        return encryptMD5File(file);
    }

    /**
     * Return the hex string of file's MD5 encryption.
     *
     * @param file The file.
     * @return the hex string of file's MD5 encryption
     */
    public static String encryptMD5File2String(final File file) {
        return ConvertUtils.bytes2HexString(encryptMD5File(file));
    }

    /**
     * Return the bytes of file's MD5 encryption.
     *
     * @param file The file.
     * @return the bytes of file's MD5 encryption
     */
    public static byte[] encryptMD5File(final File file) {
        if (file == null) return null;
        FileInputStream fis = null;
        DigestInputStream digestInputStream;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fis, md);
            byte[] buffer = new byte[256 * 1024];
            while (true) {
                if (!(digestInputStream.read(buffer) > 0)) break;
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    public static String encryptSHA1ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptSHA1ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    public static String encryptSHA1ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSHA1(data));
    }

    /**
     * Return the bytes of SHA1 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA1 encryption
     */
    public static byte[] encryptSHA1(final byte[] data) {
        return hashTemplate(data, "SHA-1");
    }

    /**
     * Return the hex string of SHA224 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA224 encryption
     */
    public static String encryptSHA224ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptSHA224ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA224 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA224 encryption
     */
    public static String encryptSHA224ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSHA224(data));
    }

    /**
     * Return the bytes of SHA224 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA224 encryption
     */
    public static byte[] encryptSHA224(final byte[] data) {
        return hashTemplate(data, "SHA224");
    }

    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    public static String encryptSHA256ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptSHA256ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    public static String encryptSHA256ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSHA256(data));
    }

    /**
     * Return the bytes of SHA256 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA256 encryption
     */
    public static byte[] encryptSHA256(final byte[] data) {
        return hashTemplate(data, "SHA-256");
    }

    /**
     * Return the hex string of SHA384 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA384 encryption
     */
    public static String encryptSHA384ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptSHA384ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA384 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA384 encryption
     */
    public static String encryptSHA384ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSHA384(data));
    }

    /**
     * Return the bytes of SHA384 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA384 encryption
     */
    public static byte[] encryptSHA384(final byte[] data) {
        return hashTemplate(data, "SHA-384");
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    public static String encryptSHA512ToString(final String data) {
        if (data == null || data.length() == 0) return "";
        return encryptSHA512ToString(data.getBytes());
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    public static String encryptSHA512ToString(final byte[] data) {
        return ConvertUtils.bytes2HexString(encryptSHA512(data));
    }

    /**
     * Return the bytes of SHA512 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA512 encryption
     */
    public static byte[] encryptSHA512(final byte[] data) {
        return hashTemplate(data, "SHA-512");
    }

    /**
     * Return the bytes of hash encryption.
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // hmac encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    public static String encryptHmacMD5ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) return "";
        return encryptHmacMD5ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    public static String encryptHmacMD5ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacMD5(data, key));
    }

    /**
     * Return the bytes of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacMD5 encryption
     */
    public static byte[] encryptHmacMD5(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacMD5");
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    public static String encryptHmacSHA1ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) return "";
        return encryptHmacSHA1ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    public static String encryptHmacSHA1ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSHA1(data, key));
    }

    /**
     * Return the bytes of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA1 encryption
     */
    public static byte[] encryptHmacSHA1(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA1");
    }

    /**
     * Return the hex string of HmacSHA224 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA224 encryption
     */
    public static String encryptHmacSHA224ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) return "";
        return encryptHmacSHA224ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA224 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA224 encryption
     */
    public static String encryptHmacSHA224ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSHA224(data, key));
    }

    /**
     * Return the bytes of HmacSHA224 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA224 encryption
     */
    public static byte[] encryptHmacSHA224(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA224");
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    public static String encryptHmacSHA256ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) return "";
        return encryptHmacSHA256ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    public static String encryptHmacSHA256ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSHA256(data, key));
    }

    /**
     * Return the bytes of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA256 encryption
     */
    public static byte[] encryptHmacSHA256(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA256");
    }

    /**
     * Return the hex string of HmacSHA384 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA384 encryption
     */
    public static String encryptHmacSHA384ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) return "";
        return encryptHmacSHA384ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA384 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA384 encryption
     */
    public static String encryptHmacSHA384ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSHA384(data, key));
    }

    /**
     * Return the bytes of HmacSHA384 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA384 encryption
     */
    public static byte[] encryptHmacSHA384(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA384");
    }

    /**
     * Return the hex string of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA512 encryption
     */
    public static String encryptHmacSHA512ToString(final String data, final String key) {
        if (data == null || data.length() == 0 || key == null || key.length() == 0) return "";
        return encryptHmacSHA512ToString(data.getBytes(), key.getBytes());
    }

    /**
     * Return the hex string of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA512 encryption
     */
    public static String encryptHmacSHA512ToString(final byte[] data, final byte[] key) {
        return ConvertUtils.bytes2HexString(encryptHmacSHA512(data, key));
    }

    /**
     * Return the bytes of HmacSHA512 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA512 encryption
     */
    public static byte[] encryptHmacSHA512(final byte[] data, final byte[] key) {
        return hmacTemplate(data, key, "HmacSHA512");
    }

    /**
     * Return the bytes of hmac encryption.
     *
     * @param data      The data.
     * @param key       The key.
     * @param algorithm The name of hmac encryption.
     * @return the bytes of hmac encryption
     */
    private static byte[] hmacTemplate(final byte[] data,
                                       final byte[] key,
                                       final String algorithm) {
        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DES encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the Base64-encode bytes of DES encryption
     */
    public static byte[] encryptDES2Base64(final byte[] data,
                                           final byte[] key,
                                           final String transformation,
                                           final byte[] iv) {
        return EncodeUtils.base64Encode(encryptDES(data, key, transformation, iv));
    }

    /**
     * Return the hex string of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the hex string of DES encryption
     */
    public static String encryptDES2HexString(final byte[] data,
                                              final byte[] key,
                                              final String transformation,
                                              final byte[] iv) {
        return ConvertUtils.bytes2HexString(encryptDES(data, key, transformation, iv));
    }

    /**
     * Return the bytes of DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of DES encryption
     */
    public static byte[] encryptDES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return symmetricTemplate(data, key, "DES", transformation, iv, true);
    }

    /**
     * Return the bytes of DES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of DES decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64DES(final byte[] data,
                                          final byte[] key,
                                          final String transformation,
                                          final byte[] iv) {
        return decryptDES(EncodeUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * Return the bytes of DES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of DES decryption for hex string
     */
    public static byte[] decryptHexStringDES(final String data,
                                             final byte[] key,
                                             final String transformation,
                                             final byte[] iv) {
        return decryptDES(ConvertUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * Return the bytes of DES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of DES decryption
     */
    public static byte[] decryptDES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return symmetricTemplate(data, key, "DES", transformation, iv, false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 3DES encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the Base64-encode bytes of 3DES encryption
     */
    public static byte[] encrypt3DES2Base64(final byte[] data,
                                            final byte[] key,
                                            final String transformation,
                                            final byte[] iv) {
        return EncodeUtils.base64Encode(encrypt3DES(data, key, transformation, iv));
    }

    /**
     * Return the hex string of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the hex string of 3DES encryption
     */
    public static String encrypt3DES2HexString(final byte[] data,
                                               final byte[] key,
                                               final String transformation,
                                               final byte[] iv) {
        return ConvertUtils.bytes2HexString(encrypt3DES(data, key, transformation, iv));
    }

    /**
     * Return the bytes of 3DES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES encryption
     */
    public static byte[] encrypt3DES(final byte[] data,
                                     final byte[] key,
                                     final String transformation,
                                     final byte[] iv) {
        return symmetricTemplate(data, key, "DESede", transformation, iv, true);
    }

    /**
     * Return the bytes of 3DES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64_3DES(final byte[] data,
                                            final byte[] key,
                                            final String transformation,
                                            final byte[] iv) {
        return decrypt3DES(EncodeUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * Return the bytes of 3DES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES decryption for hex string
     */
    public static byte[] decryptHexString3DES(final String data,
                                              final byte[] key,
                                              final String transformation,
                                              final byte[] iv) {
        return decrypt3DES(ConvertUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * Return the bytes of 3DES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of 3DES decryption
     */
    public static byte[] decrypt3DES(final byte[] data,
                                     final byte[] key,
                                     final String transformation,
                                     final byte[] iv) {
        return symmetricTemplate(data, key, "DESede", transformation, iv, false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AES encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the Base64-encode bytes of AES encryption
     */
    public static byte[] encryptAES2Base64(final byte[] data,
                                           final byte[] key,
                                           final String transformation,
                                           final byte[] iv) {
        return EncodeUtils.base64Encode(encryptAES(data, key, transformation, iv));
    }

    /**
     * Return the hex string of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the hex string of AES encryption
     */
    public static String encryptAES2HexString(final byte[] data,
                                              final byte[] key,
                                              final String transformation,
                                              final byte[] iv) {
        return ConvertUtils.bytes2HexString(encryptAES(data, key, transformation, iv));
    }

    /**
     * Return the bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of AES encryption
     */
    public static byte[] encryptAES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return symmetricTemplate(data, key, "AES", transformation, iv, true);
    }

    /**
     * 返回Base64编码字节的AES解密字节。
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64AES(final byte[] data,
                                          final byte[] key,
                                          final String transformation,
                                          final byte[] iv) {
        return decryptAES(EncodeUtils.base64Decode(data), key, transformation, iv);
    }

    /**
     * 返回Base64编码字节的字符串
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for Base64-encode bytes
     */
    public static String decryptBase64AES2String(final byte[] data,
                                                 final byte[] key,
                                                 final String transformation,
                                                 final byte[] iv) {

        byte[] decryptBase64AES = decryptAES(EncodeUtils.base64Decode(data), key, transformation, iv);
        return EncodeUtils.base64Encode2String(decryptBase64AES);
    }

    /**
     * 返回十六进制字符串的AES解密字节。
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for hex string
     */
    public static byte[] decryptHexStringAES(final String data,
                                             final byte[] key,
                                             final String transformation,
                                             final byte[] iv) {
        return decryptAES(ConvertUtils.hexString2Bytes(data), key, transformation, iv);
    }

    /**
     * 返回十六进制字符串的AES解密字节。
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for hex string
     */
    public static String decryptAESBytes2HexString(final String data,
                                                   final byte[] key,
                                                   final String transformation,
                                                   final byte[] iv) {
        byte[] decryptAES = decryptAES(ConvertUtils.string2Bytes(data), key, transformation, iv);
        return ConvertUtils.bytes2HexString(decryptAES);
    }

    /**
     * Return the bytes of AES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param iv             The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption
     */
    public static byte[] decryptAES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] iv) {
        return symmetricTemplate(data, key, "AES", transformation, iv, false);
    }

    /**
     * Return the bytes of symmetric encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param algorithm      The name of algorithm.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of symmetric encryption or decryption
     */
    private static byte[] symmetricTemplate(final byte[] data,
                                            final byte[] key,
                                            final String algorithm,
                                            final String transformation,
                                            final byte[] iv,
                                            final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
        try {
            SecretKey secretKey;
            if ("DES".equals(algorithm)) {
                DESKeySpec desKey = new DESKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
                secretKey = keyFactory.generateSecret(desKey);
            } else {
                secretKey = new SecretKeySpec(key, algorithm);
            }
            Cipher cipher = Cipher.getInstance(transformation);
            if (iv == null || iv.length == 0) {
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey);
            } else {
                AlgorithmParameterSpec params = new IvParameterSpec(iv);
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, params);
            }
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // RSA encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the Base64-encode bytes of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the Base64-encode bytes of RSA encryption
     */
    public static byte[] encryptRSA2Base64(final byte[] data,
                                           final byte[] publicKey,
                                           final int keySize,
                                           final String transformation) {
        return EncodeUtils.base64Encode(encryptRSA(data, publicKey, keySize, transformation));
    }

    /**
     * Return the hex string of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the hex string of RSA encryption
     */
    public static String encryptRSA2HexString(final byte[] data,
                                              final byte[] publicKey,
                                              final int keySize,
                                              final String transformation) {
        return ConvertUtils.bytes2HexString(encryptRSA(data, publicKey, keySize, transformation));
    }

    /**
     * Return the bytes of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA encryption
     */
    public static byte[] encryptRSA(final byte[] data,
                                    final byte[] publicKey,
                                    final int keySize,
                                    final String transformation) {
        return rsaTemplate(data, publicKey, keySize, transformation, true);
    }

    /**
     * Return the bytes of RSA decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA decryption for Base64-encode bytes
     */
    public static byte[] decryptBase64RSA(final byte[] data,
                                          final byte[] privateKey,
                                          final int keySize,
                                          final String transformation) {
        return decryptRSA(EncodeUtils.base64Decode(data), privateKey, keySize, transformation);
    }

    /**
     * Return the bytes of RSA decryption for hex string.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA decryption for hex string
     */
    public static byte[] decryptHexStringRSA(final String data,
                                             final byte[] privateKey,
                                             final int keySize,
                                             final String transformation) {
        return decryptRSA(ConvertUtils.hexString2Bytes(data), privateKey, keySize, transformation);
    }

    /**
     * Return the bytes of RSA decryption.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., <i>RSA/CBC/PKCS1Padding</i>.
     * @return the bytes of RSA decryption
     */
    public static byte[] decryptRSA(final byte[] data,
                                    final byte[] privateKey,
                                    final int keySize,
                                    final String transformation) {
        return rsaTemplate(data, privateKey, keySize, transformation, false);
    }

    /**
     * Return the bytes of RSA encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS1Padding</i>.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of RSA encryption or decryption
     */
    private static byte[] rsaTemplate(final byte[] data,
                                      final byte[] key,
                                      final int keySize,
                                      final String transformation,
                                      final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        try {
            Key rsaKey;
            KeyFactory keyFactory;
            if (Build.VERSION.SDK_INT < 28) {
                keyFactory = KeyFactory.getInstance("RSA", "BC");
            } else {
                keyFactory = KeyFactory.getInstance("RSA");
            }
            if (isEncrypt) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
                rsaKey = keyFactory.generatePublic(keySpec);
            } else {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
                rsaKey = keyFactory.generatePrivate(keySpec);
            }
            if (rsaKey == null) return null;
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, rsaKey);
            int len = data.length;
            int maxLen = keySize / 8;
            if (isEncrypt) {
                String lowerTrans = transformation.toLowerCase();
                if (lowerTrans.endsWith("pkcs1padding")) {
                    maxLen -= 11;
                }
            }
            int count = len / maxLen;
            if (count > 0) {
                byte[] ret = new byte[0];
                byte[] buff = new byte[maxLen];
                int index = 0;
                for (int i = 0; i < count; i++) {
                    System.arraycopy(data, index, buff, 0, maxLen);
                    ret = joins(ret, cipher.doFinal(buff));
                    index += maxLen;
                }
                if (index != len) {
                    int restLen = len - index;
                    buff = new byte[restLen];
                    System.arraycopy(data, index, buff, 0, restLen);
                    ret = joins(ret, cipher.doFinal(buff));
                }
                return ret;
            } else {
                return cipher.doFinal(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the bytes of RC4 encryption/decryption.
     *
     * @param data The data.
     * @param key  The key.
     */
    public static byte[] rc4(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null) return null;
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException("key must be between 1 and 256 bytes");
        }
        final byte[] iS = new byte[256];
        final byte[] iK = new byte[256];
        int keyLen = key.length;
        for (int i = 0; i < 256; i++) {
            iS[i] = (byte) i;
            iK[i] = key[i % keyLen];
        }
        int j = 0;
        byte tmp;
        for (int i = 0; i < 256; i++) {
            j = (j + iS[i] + iK[i]) & 0xFF;
            tmp = iS[j];
            iS[j] = iS[i];
            iS[i] = tmp;
        }

        final byte[] ret = new byte[data.length];
        int i = 0, k, t;
        for (int counter = 0; counter < data.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + iS[i]) & 0xFF;
            tmp = iS[j];
            iS[j] = iS[i];
            iS[i] = tmp;
            t = (iS[i] + iS[j]) & 0xFF;
            k = iS[t];
            ret[counter] = (byte) (data[counter] ^ k);
        }
        return ret;
    }

    private static byte[] joins(final byte[] prefix, final byte[] suffix) {
        byte[] ret = new byte[prefix.length + suffix.length];
        System.arraycopy(prefix, 0, ret, 0, prefix.length);
        System.arraycopy(suffix, 0, ret, prefix.length, suffix.length);
        return ret;
    }

    public static void main(String[] args) {
        /**
         * sdk加密密钥
         */
        String key = "/w0z~ E3Nwc*z#,=";
//        key = "11111111111111111111111111111111";
        /**
         * sdk加密向量
         */
        String iv = "OlFqp0I@`[Z>BEf!";
        String resAes = "9EgtWcCVtgBLX5lE0p8Zpy+zQMEh6qfUfMwikT5J9C0AGURSqcpcpPjFZYXK2jOPk1l7Ockq7vLWfQ80BV+Jl4gb+FanDcUPff3WYtd3WSA=";
//        resAes = "393FBBBC2C774BE50A106A50393E623AC3790781D015BB854359587256581F6D";
        EncryptUtils.decryptAESTest(resAes, key, iv);
    }

    // 测试解析AES加密
    public static void decryptAESTest(String resAES, String keyAes, String iv) {
        byte[] decryptAES = EncryptUtils.decryptAES(
                EncodeUtils.base64Decode(resAES), ConvertUtils.string2Bytes(keyAes), "AES/CBC/PKCS5Padding", ConvertUtils.string2Bytes(iv));
        String bytes2HexString = ConvertUtils.bytes2String(decryptAES);

    }
}