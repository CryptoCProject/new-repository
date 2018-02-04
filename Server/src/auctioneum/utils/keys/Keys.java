package auctioneum.utils.keys;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Keys {
    
    private PublicKey public_key;
    private PrivateKey private_key;
    
    public void generateKeys() {
        KeyPair pair = null;
        try {
            KeyPairGenerator g = KeyPairGenerator.getInstance("RSA");
            g.initialize(2048);
            pair = g.generateKeyPair();
            this.private_key = pair.getPrivate();
            this.public_key = pair.getPublic();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }
    
    public PublicKey getPublicKey() {
        return this.public_key;
    }
    
    public PrivateKey getPrivateKey() {
        return this.private_key;
    }
    
    public PublicKey getPublicKeyFromString(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key.getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(X509publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKeyFromString(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8PrivateKey = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(pkcs8PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
    public static String sign(String message, PrivateKey privateKey){
        try {
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privateKey);
            signer.update(message.getBytes());
            return Base64.getEncoder().encodeToString(signer.sign());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static boolean verify(String message, String signature, PublicKey publicKey){
        try {
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update(message.getBytes());
            return verifier.verify(Base64.getDecoder().decode(signature));
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    
    
}
