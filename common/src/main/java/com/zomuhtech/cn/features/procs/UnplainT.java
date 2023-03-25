/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.procs;

/**
 *
 * @author Zomuh Tech
 */
import java.io.UnsupportedEncodingException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Base64;

public class UnplainT {

    PaddedBufferedBlockCipher cipher;

    //CipherParameters ivAndKey;
    //byte sKey[];
    //byte[] iv;
    //KeyParameter keyParam;
    public UnplainT() {
        /*try {
            cipher = new PaddedBufferedBlockCipher(
                    new CBCBlockCipher(new AESEngine()));
            sKey = "1.0Tec!Vot@r@Z#m".getBytes("UTF-8");
            iv = "Zom@Voter!Tec1.0".getBytes("UTF-8");
            keyParam = new KeyParameter(sKey);
        } catch (UnsupportedEncodingException e) {
        }*/

        cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESEngine()));
    }

    //STATIC skey&iv
    public String unPlain(String plainText) {

        String encText;
        try {

            byte sKey[] = "1.0Tec!Vot@r@Z#m".getBytes("UTF-8");
            byte[] iv = "Zom@Voter!Tec1.0".getBytes("UTF-8");

            KeyParameter keyParam = new KeyParameter(sKey);

            byte[] plainData = plainText.getBytes("UTF-8");
            CipherParameters ivAndKey = new ParametersWithIV(keyParam, iv);
            cipher.init(true, ivAndKey);
            byte[] cipherBytes = cipherData(plainData);
            encText = new String(Base64.encode(cipherBytes), "UTF-8");

        } catch (UnsupportedEncodingException | IllegalArgumentException
                | CryptoException e) {
            return "encryp failed";
        }
        return encText;
    }

    public String plainT(String cipherText) {

        String decText;

        try {

            byte sKey[] = "1.0Tec!Vot@r@Z#m".getBytes("UTF-8");
            byte[] iv = "Zom@Voter!Tec1.0".getBytes("UTF-8");

            KeyParameter keyParam = new KeyParameter(sKey);

            byte[] cipherData = Base64.decode(cipherText);
            CipherParameters ivAndKey = new ParametersWithIV(keyParam, iv);

            cipher.init(false, ivAndKey);

            decText = new String(cipherData(cipherData), "UTF-8");

        } catch (UnsupportedEncodingException | IllegalArgumentException
                | CryptoException e) {
            System.out.println("decryp failed " + e);
            return "decryp failed " + e.getMessage();
        }

        return decText;
    }

    //DYNAMIC skey&iv
    public String unPlainNet(String plainText, String genSKey, String genIV) {

        String encText;
        try {

            byte sKey[] = genSKey.getBytes("UTF-8");
            byte[] iv = genIV.getBytes("UTF-8");

            KeyParameter keyParam = new KeyParameter(sKey);

            byte[] plainData = plainText.getBytes("UTF-8");
            CipherParameters ivAndKey = new ParametersWithIV(keyParam, iv);
            cipher.init(true, ivAndKey);
            byte[] cipherBytes = cipherData(plainData);
            encText = new String(Base64.encode(cipherBytes), "UTF-8");

        } catch (UnsupportedEncodingException | IllegalArgumentException
                | CryptoException e) {
            return "encryp failed";
        }

        return encText;
    }

    public String plainNetT(String cipherText, String genSKey, String genIV) {

        String decText;

        try {

            byte sKey[] = genSKey.getBytes("UTF-8");
            byte[] iv = genIV.getBytes("UTF-8");

            KeyParameter keyParam = new KeyParameter(sKey);

            byte[] cipherData = Base64.decode(cipherText);
            CipherParameters ivAndKey = new ParametersWithIV(keyParam, iv);

            cipher.init(false, ivAndKey);

            decText = new String(cipherData(cipherData), "UTF-8");

        } catch (UnsupportedEncodingException | IllegalArgumentException
                | CryptoException e) {
            return "decryp failed";
        }

        return decText;
    }

    private byte[] cipherData(byte[] data) throws CryptoException {

        int minSize = cipher.getOutputSize(data.length);
        byte[] outBuf = new byte[minSize];
        int len1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
        int len2 = cipher.doFinal(outBuf, len1);
        int actualLen = len1 + len2;
        byte[] result = new byte[actualLen];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

}
