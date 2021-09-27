package com.six.hats.brand;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.six.hats.brand.model.LoginRequest;
import com.six.hats.brand.model.LoginResponse;
import com.six.hats.brand.model.OtpResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.register.RegistrationActivity;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link LoginActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginActivity extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = "Login - ";
    TextView forgot_pwd;
    TextInputEditText enter_ph_no;
    TextInputEditText enter_pwd;
    AppCompatActivity activity;
    private TextInputLayout input_layout_pwd, input_layout_ph_no;
    private ProgressDialog progressDialog;

    public LoginActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginActivity newInstance() {
        LoginActivity fragment = new LoginActivity();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getSupportActionBar().hide();

        Button login_btn = (Button) findViewById(R.id.login_btn);

        Button signup = (Button) findViewById(R.id.go_to_registration);
        activity = this;
        enter_ph_no = findViewById(R.id.enter_ph_no);
        enter_pwd = findViewById(R.id.enter_pwd);
        forgot_pwd = findViewById(R.id.forgot_pwd);

        input_layout_pwd = (TextInputLayout) findViewById(R.id.input_layout_pwd);
        input_layout_ph_no = (TextInputLayout) findViewById(R.id.input_layout_ph_no);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (submitForm()) {
                    // String imei = CommonUtility.getUserUniqueIdentifier();
                    login();
                } else {
                    closeProgressLoader();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent i = new Intent(activity, RegistrationActivity.class);
                i.putExtra("page", "signup");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_in_right);
                startActivity(i, options.toBundle());

                //Go to register step 1
                // RegistrationActivity.replaceFragmentHistory(new RegPersonelDetails(), (AppCompatActivity) activity);

            }
        });

        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ForgotPwdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_in_right);
                startActivity(intent, options.toBundle());
                //  askPhoneDetails();
            }
        });


    }


    /**
     * Validating form
     */
    private boolean submitForm() {
        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Logging in...."); // Setting Message
            progressDialog.setTitle(getString(R.string.app_name)); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!isValidMobile(enter_ph_no.getText().toString().trim())) {
            return false;
        }
        if (!validatePassword()) {
            return false;
        }
        return true;
    }


    private boolean validatePassword(TextInputEditText enter_pwd) {
        if (!CommonUtility.isValidPassword(enter_pwd.getText().toString().trim())) {
            if (enter_pwd.getText().toString().trim().length() < 8) {
                enter_pwd.setError(getString(R.string.pwd_length_error));

            } else {
                enter_pwd.setError(getString(R.string.err_msg_password));

            }
            return false;
        } else {
            enter_pwd.setError(null);
        }

        return true;
    }


    private boolean validatePassword() {
        if (!CommonUtility.chkString(enter_pwd.getText().toString().trim())) {
            input_layout_pwd.setError("Please enter the password.");
            return false;
        } else {
            input_layout_pwd.setErrorEnabled(false);
        }

        return true;
    }


    private boolean isValidForgotMobile(String phone, TextInputEditText view) {
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

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            /* if(phone.length() < 10) {*/
            if (phone.length() != 10) {
                // input_layout_ph.setError("Not Valid Number");
                input_layout_ph_no.setError(getString(R.string.err_msg_phone));
                return false;
            }
        } else {
            input_layout_ph_no.setErrorEnabled(false);

        }
        return true;
    }

    public void login() {

        final LoginRequest loginRequest = new LoginRequest();
        final String ph = enter_ph_no.getText().toString();
        final String pwd = enter_pwd.getText().toString();
        try {
            loginRequest.setPassword(pwd);
            loginRequest.setMobile(ph);
            //loginRequest.setImeiNumber(imei);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String payloadStr = CommonUtility.convertToJson("Login", loginRequest);

        CentralApis.getInstance().getAPIS().loginRequest(enter_ph_no.getText().toString(), enter_pwd.getText().toString(), "").enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getStatus()) {

                        saveUsersPrefs(loginResponse, enter_pwd.getText().toString());
                        //  CommonUtility.showErrorAlert(activity, loginResponse.getUserDetails().getUserId());
                        closeProgressLoader();

                        if (!loginResponse.getUserDetails().getOtpverfied()) {
                            //User not verified - Thus redirect to OTP verify page
                            PrefsWrapper.with(activity).save(JullayConstants.KEY_OTP, String.valueOf(loginResponse.getUserDetails().getOtp()));
                            resendOTPverify();
                            Intent i = new Intent(activity, RegistrationActivity.class);
                            i.putExtra("page", "otp");
                            i.putExtra("ph", ph);
                            i.putExtra("pwd", pwd);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ActivityOptions options =
                                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_in_right);
                            startActivity(i, options.toBundle());
                            finish();
                        } else {
                            PrefsWrapper.with(activity).save(JullayConstants.KEY_IS_VERIFIED, true);
                            PrefsWrapper.with(activity).save(JullayConstants.KEY_IS_LOGGED_IN, true);

                            closeProgressLoader();
                            //user is logged in land on home - and has no bookings
                            Intent i = new Intent(activity, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           /*     ActivityOptions options =
                                        ActivityOptions.makeCustomAnimation(activity, R.anim.slide_in_left, R.anim.slide_in_right);
                                startActivity(i, options.toBundle());*/
                            startActivity(i);
                            finish();


                        }
                    } else {
                        closeProgressLoader();
                        String message = "Please make sure that the details entered are correct. Try again!";
                        if (CommonUtility.correctStringNull(loginResponse.getToken()).contains("please login from your register phone")) {
                            message = "You are already logged on another phone. Please press OK to login on this mobile phone.";

                            CommonUtility.showAlertSingleButton(activity, getString(R.string.app_name), message, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        progressDialog = new ProgressDialog(activity);
                                        progressDialog.setMessage("Logging in...."); // Setting Message
                                        progressDialog.setTitle(getString(R.string.app_name)); // Setting Title
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    changeImeiLoginHere();
                                }
                            });
                        } else {
                            CommonUtility.showAlertRetryCancel(activity, getString(R.string.app_name), message, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //retry
                                    login();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                } else {
                    closeProgressLoader();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtility.showErrorAlert(activity, jObjError.getString("message"));
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(activity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                closeProgressLoader();

                Log.e(TAG, "Response: Failure");
                if (!CommonUtility.haveNetworkConnection(activity)) {
                    CommonUtility.showAlertRetryCancel(activity, getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            login();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(activity, getString(R.string.network_error_text));

                }
            }
        });

    }


    /**
     * Resend Otp i.e Otp not received
     */
    private void resendOTPverify() {

        CentralApis.getInstance().getAPIS().resendOtpRequest(PrefsWrapper.with(activity).getString(JullayConstants.KEY_USER_ID, "0"), PrefsWrapper.with(activity).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful()) {
                    closeProgressLoader();
                    if (response.body().getStatus()) {
                        CommonUtility.showErrorAlert(activity, "Otp send to your Email/Mobile.");
                    } else {
                        CommonUtility.showErrorAlert(activity, getString(R.string.resend_otp_error_msg));
                    }
                } else {
                    closeProgressLoader();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(activity);
                        } else {
                            CommonUtility.showErrorAlert(activity, jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(activity, e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                closeProgressLoader();
                Log.e("Retrofit call", "Response: Failure");
                if (!CommonUtility.haveNetworkConnection(activity)) {
                    CommonUtility.showAlertRetryCancel(activity, getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            resendOTPverify();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(activity, getString(R.string.network_error_text));

                }
            }
        });
    }

    private void changeImeiLoginHere() {
       // final String imei = CommonUtility.getUserUniqueIdentifier();
        final LoginRequest loginRequest = new LoginRequest();
        final String ph = enter_ph_no.getText().toString();
        final String pwd = enter_pwd.getText().toString();
        try {
            loginRequest.setPassword(pwd);
            loginRequest.setMobile(ph);
           // loginRequest.setImeiNumber(imei);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String payloadStr = CommonUtility.convertToJson("changeImei", loginRequest);

        CentralApis.getInstance().getAPIS().changeImei(enter_ph_no.getText().toString(), enter_pwd.getText().toString(), "").enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getStatus()) {
                        login();
                    } else {
                        closeProgressLoader();
                        String message = "Please make sure that the details entered are correct. Try again!";
                        if (loginResponse.getToken().equalsIgnoreCase("Bad Credentials")) {
                            CommonUtility.showAlertOK(activity, message);

                        } else {
                            CommonUtility.showAlertOK(activity, loginResponse.getToken());

                        }
                    }
                } else {
                    closeProgressLoader();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtility.showErrorAlert(activity, jObjError.getString("message"));
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(activity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                closeProgressLoader();

                Log.e(TAG, "Response: Failure");
                if (!CommonUtility.haveNetworkConnection(activity)) {
                    CommonUtility.showAlertRetryCancel(activity, getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            changeImeiLoginHere();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(activity, getString(R.string.network_error_text));

                }
            }
        });


    }


    private void saveUsersPrefs(LoginResponse loginResponse, String pwd) {
        String key = loginResponse.getToken();
        //save user necessary data in the preference
        PrefsWrapper.with(activity).save(JullayConstants.KEY_PWD, /*CommonUtility.encryption(*/pwd/*, key)*/);
        PrefsWrapper.with(activity).save(JullayConstants.KEY_USER_ID, String.valueOf(loginResponse.getUserDetails().getUsername()));
        PrefsWrapper.with(activity).save(JullayConstants.KEY_NAME, String.valueOf(loginResponse.getUserDetails().getName()));
        PrefsWrapper.with(activity).save(JullayConstants.KEY_USER_PH, String.valueOf(loginResponse.getUserDetails().getUserPhoneNo()));
        PrefsWrapper.with(activity).save(JullayConstants.KEY_USER_EMAIL, String.valueOf(loginResponse.getUserDetails().getMail()));
        PrefsWrapper.with(activity).save(JullayConstants.KEY_USER_IMG, String.valueOf(loginResponse.getUserDetails().getPhoto()));
        PrefsWrapper.with(activity).save(JullayConstants.KEY_USER_TOKEN, "Bearer " + String.valueOf(loginResponse.getToken()));
        PrefsWrapper.with(activity).save(JullayConstants.KEY_USER_IDENTIFIER, String.valueOf(loginResponse.getUserDetails().getImeiNumber()));

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

}
