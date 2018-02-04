package auctioneum.utils.otp;

import auctioneum.network.app.T;
import java.util.HashMap;
    
public class OtpStats {
    
    private Timer timer;
    private int tries;
    private String name;
    private HashMap <String, OtpStats> otpUsers;
    
    public OtpStats(String name, HashMap <String, OtpStats> otpUsers) {
        this.timer = new Timer();
        this.timer.start();
        this.tries = 0;
        this.name = name;
        this.otpUsers = otpUsers;
    }
    
    public boolean areThereMoreTries() {
        this.tries++;
        if (this.tries > 3) {
            if (otpUsers.containsKey(name)) {
                otpUsers.remove(name);
            }
            return false;
        }
        return true;
    }
    
    private class Timer extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(T.OTP_VALID_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (otpUsers.containsKey(name)) {
                otpUsers.remove(name);
            }
        }

    }

}
