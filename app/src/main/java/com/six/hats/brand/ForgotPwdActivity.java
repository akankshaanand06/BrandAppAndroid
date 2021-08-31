package com.six.hats.brand;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;


import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;


public class ForgotPwdActivity extends AppCompatActivity {

    private static final String TAG = ForgotPwdActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    AppCompatActivity appCompatActivity;
    private LinearLayout forgotLay, resetLay;
    private String mobileNo;
    private TextInputEditText reset_token, enter_cfm_pwd, enter_new_pwd;
    TextInputLayout input_layout_cfm_pwd, input_layout_otp, input_layout_new_pwd;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        appCompatActivity = this;
        next = (Button) findViewById(R.id.next);
        final TextInputEditText mobile_no = findViewById(R.id.cust_ph);
        TextInputLayout input_layout_ph_no = findViewById(R.id.input_layout_ph_no);
        forgotLay = findViewById(R.id.forgotLay);
        resetLay = findViewById(R.id.resetLay);
        resetLay.setVisibility(View.GONE);

        enter_cfm_pwd = findViewById(R.id.enter_cfm_pwd);
        input_layout_cfm_pwd = findViewById(R.id.input_layout_cfm_pwd);
        enter_new_pwd = findViewById(R.id.enter_new_pwd);
        input_layout_new_pwd = findViewById(R.id.input_layout_new_pwd);
        reset_token = findViewById(R.id.enter_otp);
        input_layout_otp = findViewById(R.id.input_layout_otp);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(appCompatActivity, "tt", Toast.LENGTH_SHORT).show();

                if (forgotLay.getVisibility() == View.VISIBLE) {

                    if (isValidForgotMobile(mobile_no.getText().toString(), mobile_no)) {
                        callForgotPwd(mobile_no.getText().toString());

                    }
                } else {
                    if (CommonUtility.chkString(mobileNo)) {
                        if (submitReset()) {
                            resetPwd(mobile_no.getText().toString(), reset_token.getText().toString(), enter_new_pwd.getText().toString());
                        }
                    }
                }
            }
        });
    }

    private boolean isValidForgotMobile(String phone, EditText view) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            /* if(phone.length() < 10) {*/
            if (phone.length() != 10) {
                // input_layout_ph.setError("Not Valid Number");
                view.setError(getString(R.string.err_msg_phone));
                return false;
            }
        } else {
            view.setError(null);

        }
        return true;
    }

    private void callForgotPwd(final String mobile) {

        try {
            progressDialog = new ProgressDialog(appCompatActivity);
            progressDialog.setMessage("Please wait....."); // Setting Message
            progressDialog.setTitle(getString(R.string.app_name)); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            CentralApis.getInstance().getAPIS().forgotPasswordRequest(mobile).enqueue(new retrofit2.Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                    if (response.isSuccessful()) {

                        BasicResponse loginResponse = response.body();

                        if (loginResponse.getStatus()) {

                            closeProgressLoader();
                            String msg = getString(R.string.forgot_pwd_sucs_msg);
                            mobileNo = mobile;
                            forgotLay.setVisibility(View.GONE);
                            resetLay.setVisibility(View.VISIBLE);
                            next.setText(getResources().getString(R.string.done));
                            CommonUtility.showAlertSingleButton(appCompatActivity, getString(R.string.app_name), loginResponse.getMessage(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            closeProgressLoader();
                            CommonUtility.showAlertSingleButton(appCompatActivity, getString(R.string.app_name), loginResponse.getMessage(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    } else {
                        closeProgressLoader();
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            CommonUtility.showErrorAlert(appCompatActivity, jObjError.getString("message"));
                        } catch (Exception e) {
                            CommonUtility.showErrorAlert(appCompatActivity, e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {
                    closeProgressLoader();
                    Log.e(TAG, "Response: Failure");
                    CommonUtility.showErrorAlert(appCompatActivity, getString(R.string.network_error_text));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean submitReset() {

        if (!CommonUtility.chkString(reset_token.getText().toString())) {
            reset_token.setError("Please enter the OTP");
            return false;
        }
        if (!validatePassword()) {
            return false;
        }

        if (!isCheckPswd()) {
            return false;
        }

        return true;
    }


    public void closeProgressLoader() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPwd(String mobile, String token, String pwd) {

        try {
            progressDialog = new ProgressDialog((AppCompatActivity) appCompatActivity);
            progressDialog.setMessage("Logging in...."); // Setting Message
            progressDialog.setTitle(getString(R.string.app_name)); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CentralApis.getInstance().getAPIS().postResetPwd(mobile, token, pwd).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {

                    BasicResponse basicResponse = response.body();

                    if (basicResponse.getStatus()) {

                        closeProgressLoader();

                        String msg = "Your password reset is successful. Login with New Password.";

                        CommonUtility.showSingleButton(appCompatActivity, getString(R.string.app_name), msg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                    } else {
                        closeProgressLoader();

                        CommonUtility.showAlertSingleButton(appCompatActivity, getString(R.string.app_name), basicResponse.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                    }
                } else {
                    closeProgressLoader();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtility.showErrorAlert(appCompatActivity, jObjError.getString("message"));
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(appCompatActivity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                closeProgressLoader();
                Log.e(TAG, "Response: Failure");
                CommonUtility.showErrorAlert(appCompatActivity, getString(R.string.network_error_text));
            }
        });


    }

    private boolean validatePassword() {
        if (!CommonUtility.chkString(enter_new_pwd.getText().toString().trim())) {
            input_layout_new_pwd.setError(getString(R.string.err_msg_reg_password));
            return false;
        } else {

            if (enter_new_pwd.getText().toString().length() < 8) {
                input_layout_new_pwd.setError(getString(R.string.err_msg_reg_password));
                return false;
            }
            input_layout_new_pwd.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isCheckPswd() {
        if (!enter_cfm_pwd.getText().toString().trim().equals(enter_new_pwd.getText().toString().trim())) {
            if (enter_cfm_pwd.getText().toString().trim().length() < 8) {
                input_layout_cfm_pwd.setError(getString(R.string.pwd_length_error));

            } else {
                input_layout_cfm_pwd.setError(getString(R.string.err_Password_check));

            }
            return false;
        } else {
            input_layout_cfm_pwd.setError(null);
        }

        return true;
    }


}