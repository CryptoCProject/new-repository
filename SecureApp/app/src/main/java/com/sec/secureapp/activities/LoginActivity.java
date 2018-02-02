package com.sec.secureapp.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sec.secureapp.R;
import com.sec.secureapp.database.DB;
import com.sec.secureapp.databinding.ActivityLoginBinding;
import com.sec.secureapp.general.InfoMessage;
import com.sec.secureapp.general.T;
import com.sec.secureapp.general.UserInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.registerLoginButton.setOnClickListener(this);
        binding.signinLoginButton.setOnClickListener(this);

        T.DB = new DB(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_login_button:
                Intent intent = new Intent(this, SignupActivity.class);
                this.startActivity(intent);
                finish();
                break;

            case R.id.signin_login_button:
                String n = binding.usernameLogin.getText().toString();
                String p = binding.passwordLogin.getText().toString();
                String otpId = String.valueOf(System.currentTimeMillis());
                new InfoMessage(this, T.LOG_IN, new UserInfo(n, p, null, otpId, null)).start();
                break;
            default:
                break;
        }
    }
}
