package com.six.hats.brand.register;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.six.hats.brand.MainActivity;
import com.six.hats.brand.R;
import com.six.hats.brand.model.LoginRequest;
import com.six.hats.brand.model.LoginResponse;
import com.six.hats.brand.model.OtpResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegOTPVerifylDetails extends Fragment {

    private static String ARG_PARAM = "KEY_OTP";
    private static String ARG_PWD = "KEY_PWD";
    private static String ARG_PH = "KEY_PH";

    TextInputEditText enter_otp;
    TextInputLayout input_layout_dtp;
    private ProgressDialog progressDialog;

    public RegOTPVerifylDetails() {
        // Required empty public constructor
    }

    public static RegOTPVerifylDetails newInstance(String ph, String pwd) {
        RegOTPVerifylDetails fragment = new RegOTPVerifylDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PWD, pwd);
        args.putString(ARG_PH, ph);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String title_color = PrefsWrapper.with(getActivity()).getString(JullayConstants.DEFAULT_HEADING_COLOR, JullayConstants.DARK_HEADING);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.verify_otp) + "</font>")));
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp_verifications, container, false);
        enter_otp = view.findViewById(R.id.enter_otp);
        enter_otp.requestFocus();
        TextView resendOTP = (TextView) view.findViewById(R.id.resend_otp);
        input_layout_dtp = (TextInputLayout) view.findViewById(R.id.input_layout_dtp);

        Button verify = view.findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateOTP()) {
                    postverify();
                }
            }
        });
        resendOTP.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    resendOTPverify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    /**
     * Resend Otp i.e Otp not received
     */
    private void resendOTPverify() {

        try {
            progressDialog = new ProgressDialog((AppCompatActivity) getActivity());
            progressDialog.setMessage("Please wait, we are sending the OTP again."); // Setting Message
            progressDialog.setTitle(getString(R.string.app_name)); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CentralApis.getInstance().getAPIS()
                .resendOtpRequest(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, "0"),
                        PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, ""))
                .enqueue(new retrofit2.Callback<OtpResponse>() {
                    @Override
                    public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                        if (response.isSuccessful()) {
                            closeProgressLoader();
                            if (response.body().getStatus()) {
                                CommonUtility.showErrorAlert(getContext(), "Otp send to your Email.");
                            } else {
                                CommonUtility.showErrorAlert(getContext(), getString(R.string.resend_otp_error_msg));
                            }
                        } else {
                            closeProgressLoader();
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("message").contains("Unauthorized")) {
                                    CommonUtility.autoLogin(getActivity());
                                } else {
                                    CommonUtility.showErrorAlert(getActivity(), jObjError.getString("message"));
                                }
                            } catch (Exception e) {
                                CommonUtility.showErrorAlert(getContext(), e.getMessage());
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<OtpResponse> call, Throwable t) {
                        closeProgressLoader();
                        Log.e("Retrofit call", "Response: Failure");
                        if (!CommonUtility.haveNetworkConnection(getActivity())) {
                            CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //retry
                                    resendOTPverify();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                        }
                    }
                });
    }

    private boolean validateOTP() {
        if (!CommonUtility.chkString(enter_otp.getText().toString().trim())) {
            input_layout_dtp.setError(getString(R.string.err_msg_otp));
            return false;
        } else {
            input_layout_dtp.setErrorEnabled(false);
        }
        return true;

    }

    private void postverify() {

        try {
            progressDialog = new ProgressDialog((AppCompatActivity) getActivity());
            progressDialog.setMessage("Please wait, we are verifying the OTP"); // Setting Message
            progressDialog.setTitle(getString(R.string.app_name)); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String username = PrefsWrapper.with(getContext()).getString(JullayConstants.KEY_USER_ID, "0");
        // Snackbar.make(getView(), "UserId--" + username, Snackbar.LENGTH_SHORT).show();

        CentralApis.getInstance().getAPIS().verifyRequest(username, enter_otp.getText().toString()).enqueue(new retrofit2.Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful()) {
                    OtpResponse otpResponse = response.body();
                    closeProgressLoader();

                    if (otpResponse.getStatus()) {
                        login();
                    } else {
                        CommonUtility.showAlertOK(getActivity(), getString(R.string.incorrect_otp));

                    }

                } else {
                    closeProgressLoader();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(getActivity());
                        } else {
                            CommonUtility.showErrorAlert(getActivity(), jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getContext(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                closeProgressLoader();
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            postverify();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });


    }

    public void login() {

        final LoginRequest loginRequest = new LoginRequest();
        try {
            loginRequest.setPassword(getArguments().getString(ARG_PWD));
            loginRequest.setMobile(getArguments().getString(ARG_PH));
            //  loginRequest.setImeiNumber(CommonUtility.getUserUniqueIdentifier());

        } catch (Exception e) {
            e.printStackTrace();
        }
        String payloadStr = CommonUtility.convertToJson("Login", loginRequest);

        CentralApis.getInstance().getAPIS().loginRequest(getArguments().getString(ARG_PH), getArguments().getString(ARG_PWD), "").enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getStatus()) {
                        closeProgressLoader();
                        saveUsersPrefs(loginResponse);
                        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_IS_LOGGED_IN, true);
                        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_IS_VERIFIED, true);


                        //user is logged in land on home - and has no bookings
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        ActivityOptions options =
                                ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_in_right);
                        startActivity(i, options.toBundle());


                        getActivity().finish();
                    } else {
                        CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.network_error_text), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //retry
                                login();
                                dialog.dismiss();
                            }
                        });
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtility.showErrorAlert(getContext(), jObjError.getString("message"));
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getContext(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("RegOtp", "Response: Failure");
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            login();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });

    }


    private void saveUsersPrefs(LoginResponse loginResponse) {
        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_USER_ID, String.valueOf(loginResponse.getUserDetails().getUsername()));
        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_NAME, String.valueOf(loginResponse.getUserDetails().getName()));
        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_USER_PH, String.valueOf(loginResponse.getUserDetails().getUserPhoneNo()));
        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_USER_EMAIL, String.valueOf(loginResponse.getUserDetails().getMail()));
        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_USER_IMG, String.valueOf(loginResponse.getUserDetails().getPhoto()));
        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_USER_TOKEN, "Bearer " + String.valueOf(loginResponse.getToken()));
        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_USER_IDENTIFIER, String.valueOf(loginResponse.getUserDetails().getImeiNumber()));
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
