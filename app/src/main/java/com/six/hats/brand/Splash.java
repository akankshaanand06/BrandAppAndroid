package com.six.hats.brand;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

       /*         Intent appotmnt_detail = new Intent(getApplicationContext(), RegistrationActivity
                        .class);
                appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appotmnt_detail.putExtra("calledFor", "register");
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_in_right);
                startActivity(appotmnt_detail, options.toBundle());
                finish();
*/
            }
        }, 2000);

    }
}