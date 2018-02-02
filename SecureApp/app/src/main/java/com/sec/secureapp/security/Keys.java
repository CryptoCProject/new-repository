package com.sec.secureapp.security;

import android.util.Base64;

import java.io.File;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class Keys {

    private PublicKey serverPublicKey;
    private PrivateKey myPrivateKey;

    public Keys(String u, String r) {
        this.serverPublicKey = this.getServerPublicKeyFromString(u);
        this.myPrivateKey = this.getMyPrivateKeyFromString(r);
    }

    public PrivateKey getMyPrivateKey() {
        return this.myPrivateKey;
    }

    public PublicKey getServerPublicKey() {
        return this.serverPublicKey;
    }

    public PublicKey getServerPublicKeyFromString(String key) {
        try {
            byte[] byteKey = Base64.decode(key.getBytes(), Base64.NO_WRAP);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(X509publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getMyPrivateKeyFromString(String key) {
        try {
            byte[] byteKey = Base64.decode(key.getBytes(), Base64.NO_WRAP);
            X509EncodedKeySpec X509privateKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(X509privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getServerPublicKeyToString() {
        return new String(Base64.encode(this.serverPublicKey.getEncoded(), Base64.NO_WRAP));
    }

    public String getPrivateKeyToString() {
        return new String(Base64.encode(this.myPrivateKey.getEncoded(), Base64.NO_WRAP));
    }

    public String encrypt(String text) {
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.serverPublicKey);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
    }

    public String decrypt(String text) {
        byte[] dectyptedText = null;
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.myPrivateKey);
            dectyptedText = cipher.doFinal(Base64.decode(text, Base64.NO_WRAP));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new String(dectyptedText);
    }

    public static String sign(String message, PrivateKey privateKey){
        try {
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privateKey);
            signer.update(message.getBytes());
            return Base64.encodeToString(signer.sign(), Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
