package com.six.hats.brand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.six.hats.brand.model.CentreSingleton;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView brand_name = findViewById(R.id.brand_name);

        CentreSingleton singleton = CentreSingleton.getInstance();
        String branchName = singleton.getBusinessName();
        brand_name.setText(branchName);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        TextView short_desc = findViewById(R.id.short_desc);
        TextView long_desc = findViewById(R.id.long_desc);
        TextView ratings = findViewById(R.id.ratings);
        TextView reviews = findViewById(R.id.reviews);
        TextView feedback = findViewById(R.id.feedback);


        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rating = new Intent(getApplicationContext(), MenuActivity.class);
                rating.putExtra("menu", "rating");
                rating.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(rating);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rating = new Intent(getApplicationContext(), Feedbacks.class);
                rating.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(rating);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}