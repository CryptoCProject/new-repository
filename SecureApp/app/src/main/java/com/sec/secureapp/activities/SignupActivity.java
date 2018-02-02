package com.sec.secureapp.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sec.secureapp.R;
import com.sec.secureapp.databinding.ActivitySignupBinding;
import com.sec.secureapp.general.InfoMessage;
import com.sec.secureapp.general.T;
import com.sec.secureapp.general.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        binding.signupSignupButton.setOnClickListener(this);
        binding.cancelSignupButton.setOnClickListener(this);
    }

    private boolean isEverythingRight(String n, String p, String cp, String e) {
        if (this.isValidUsername(n)) {
            if (this.isValidPassword(p)) {
                if (this.arePasswordsSame(p, cp)) {
                    if (this.isValidEmail(e)) {
                        return true;
                    } else {
                        T.VIEW_TOAST(this, "Wrong email.", Toast.LENGTH_LONG);
                    }
                } else {
                    T.VIEW_TOAST(this, "Wrong password confirmation.", Toast.LENGTH_LONG);
                }
            } else {
                T.VIEW_TOAST(this, "Wrong password. Allowable chars: a-z, A-Z, 0-9, @#$%^&+=. A lower case letter, an upper case letter, a digit and a special character must occur at least once. Length must be 10-40.", Toast.LENGTH_LONG);
            }
        } else {
            T.VIEW_TOAST(this, "Wrong useraname. Allowable chars: a-z, A-Z, 0-9. Length must be 3-15.", Toast.LENGTH_LONG);
        }
        return false;
    }

    /**
     * 1) a digit must occur at least once
     * 2) a lower case letter must occur at least once
     * 3) an upper case letter must occur at least once
     * 4) a special character must occur at least once
     * 5) no whitespace allowed in the entire string
     * 6) 10 to 40 characters password
     */
    private boolean isValidPassword(String pwd) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{10,40}$");
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }

    private boolean arePasswordsSame(String pwd, String confPwd) {
        if (pwd.equals(confPwd)) {
            return true;
        }
        return false;
    }

    /**
     * 1) Only letters and numbers are allowable
     * 2) 3 to 15 characters
     */
    private boolean isValidUsername(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{3,15}$");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * checks if the email format is right
     */
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^.+@.+(\\.[^\\.]+)+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, LoginActivity.class);
        startActivity(setIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_signup_button:
                String n = binding.usernameSignup.getText().toString();
                String p = binding.passwordSignup.getText().toString();
                String c = binding.confirmPasswordSignup.getText().toString();
                String e = binding.emailSignup.getText().toString();
                if (isEverythingRight(n, p, c, e)) {
                    new InfoMessage(SignupActivity.this, T.SIGN_UP, new UserInfo(n, p, e, null, null)).start();
                }
                break;

            case R.id.cancel_signup_button:
                Intent setIntent = new Intent(this, LoginActivity.class);
                startActivity(setIntent);
                break;
            default:
                break;
        }
    }
}
