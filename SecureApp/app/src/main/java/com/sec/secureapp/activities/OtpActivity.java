package com.sec.secureapp.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sec.secureapp.R;
import com.sec.secureapp.databinding.ActivityOtpBinding;
import com.sec.secureapp.general.InfoMessage;
import com.sec.secureapp.general.T;
import com.sec.secureapp.general.UserInfo;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityOtpBinding binding;

    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        name = getIntent().getStringExtra("name");

        binding.cancelOtpButton.setOnClickListener(this);
        binding.continueOtpButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, LoginActivity.class);
        startActivity(setIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continue_otp_button:
                String otp = binding.otp.getText().toString();
                new InfoMessage(this, T.OTP, new UserInfo(name, null, null, null, otp)).start();
                break;

            case R.id.cancel_otp_button:
                Intent setIntent = new Intent(this, LoginActivity.class);
                startActivity(setIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
