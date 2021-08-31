package com.six.hats.brand;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.six.hats.brand.register.RegistrationActivity;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (PrefsWrapper.with(getApplicationContext()).getBoolean(JullayConstants.KEY_IS_LOGGED_IN, false)) {
                    Intent appotmnt_detail = new Intent(getApplicationContext(), MainActivity.class);
                    appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ActivityOptions options =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.animator.slide_in_left, R.animator.slide_in_right);
                    startActivity(appotmnt_detail, options.toBundle());
                    finish();
                } else {

                    Intent appotmnt_detail = new Intent(getApplicationContext(),  LoginActivity.class);
                    appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    appotmnt_detail.putExtra("calledFor", "register");
                    ActivityOptions options =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.animator.slide_in_left, R.animator.slide_in_right);
                    startActivity(appotmnt_detail, options.toBundle());
                    finish();
                }
            }
        }, 2000);

    }
}