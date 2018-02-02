package com.sec.secureapp.general;

import java.util.ArrayList;

public class UserInfo {

    private String name;
    private String pwd;
    private String email;
    private String salt;
    private String otp;

    public UserInfo(String name, String pwd, String email, String salt, String otp) {
        this.name = name;
        this.pwd = pwd;
        this.email = email;
        this.salt = salt;
        this.otp = otp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
