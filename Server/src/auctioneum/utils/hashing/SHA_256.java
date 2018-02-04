package auctioneum.utils.hashing;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA_256 {
    
    public String getHashedCode (String ... codes) {
        String sha256 = hashSHA256(codes);
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
        return Base64.getEncoder().encodeToString(bytes);
    }
    
}
