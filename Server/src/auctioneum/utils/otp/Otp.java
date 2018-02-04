package auctioneum.utils.otp;

import auctioneum.utils.hashing.SHA_256;
import com.sun.mail.util.MailSSLSocketFactory;

import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author Dimitris
 */
public class Otp {

    private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
    private String otp;
    private String email;
    private SHA_256 hash;

    public Otp(String salt, String email, SHA_256 hash) {
        this.email = email;
        this.hash = hash;
        createOTP(getRandomString(characters, 20), salt);
        System.out.println(otp);
    }

    private void createOTP(String initialPass, String salt) {
        String hashedPassword = hash.getHashedCode(new String[]{initialPass, salt});
        this.otp = getRandomString(hashedPassword, 10);
    }

    private String getRandomString(String str, int len) {
        String password = "";
        Random rnd = new Random();
        while (password.length() < len) { // length of the random string.
            int index = (int) (rnd.nextFloat() * (str.length() - 1));
            password = password + str.charAt(index);
        }
        return password;
    }

    public boolean sendMail() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "587");
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "587");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("panos.zafeiratos@gmail.com", "12345678!");
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("panos.zafeiratos@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("OTP message");
            message.setText("Your OTP is: " + otp);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getOtp() {
        return otp;
    }

}
