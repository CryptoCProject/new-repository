package com.sec.secureapp.security;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hashing {

    public String getHashedCode (String code) {
        String sha256 = hashSHA256(code);
        return sha256;
    }

    public String hashSHA256(String ... codes) {
        MessageDigest crypt = null;
        try {
            crypt = MessageDigest.getInstance("SHA-256");
            crypt.reset();
            for (String code : codes) {
                crypt.update(code.getBytes());
            }
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytesToString(crypt.digest());
    }

    private String bytesToString(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

}
