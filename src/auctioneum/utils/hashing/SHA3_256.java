package auctioneum.utils.hashing;

import auctioneum.utils.formats.Formatter;
import org.bouncycastle.jcajce.provider.digest.SHA3;


public class SHA3_256 {


    public static String hash(String input){
        final SHA3.DigestSHA3 sha3 = new SHA3.Digest256();
        sha3.update(input.getBytes());
        return Formatter.bytesToHex(sha3.digest());
    }


}
