package com.six.hats.brand.register;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.six.hats.brand.R;

public class RegistrationActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (savedInstanceState == null) {
            // ServiceItemFragment fragment= new ServiceItemFragment();
            //SelectQATFragment fragment= new SelectQATFragment();
            String page = getIntent().getStringExtra("page");
            if (page.equalsIgnoreCase("signup")) {
                RegistrationActivity.replaceFragmentHistory(new RegPersonelDetails(), this);

            } else {
                String ph = getIntent().getStringExtra("ph");
                String pwd = getIntent().getStringExtra("pwd");
                RegistrationActivity.replaceFragmentHistory(new RegOTPVerifylDetails().newInstance(ph, pwd), this);


            }
        }
        ((AppCompatActivity) this).getSupportActionBar().setTitle(R.string.reg_persnl_details);


    }


    /**
     * Adds 1st fragment to activity without being added to the history stack
     *
     * @param fragment
     */
    public static void replaceFragmentHistory(Fragment fragment, AppCompatActivity context) {
       /* if (isUsedBundle) {
            Bundle args = new Bundle();
            args.putInt(bundleParameterName, bundleValue);
            fragment.setArguments(args);
        }*/
        if (context != null) {
            try {
                FragmentTransaction transaction =
                        context.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);

                transaction.replace(R.id.regContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

            } catch (IllegalStateException e) {
                e.printStackTrace();
                context.finish();
            }
        }
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

    public void showProgressLoader(AppCompatActivity mContext, String message, String title) {
        try {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(message); // Setting Message
            progressDialog.setTitle(title); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void closeProgressLoader() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
