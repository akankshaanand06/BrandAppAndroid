package com.six.hats.brand.register;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.six.hats.brand.MainActivity;
import com.six.hats.brand.R;
import com.six.hats.brand.model.LoginRequest;
import com.six.hats.brand.model.LoginResponse;
import com.six.hats.brand.model.OtpResponse;
import com.six.hats.brand.model.RegPer;
import com.six.hats.brand.model.RegistPDRequest;
import com.six.hats.brand.model.UserDetails;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegPersonelDetails extends Fragment {

    TextInputEditText enter_name/*, enter_surname*/;
    TextInputEditText enter_age, enter_income;
    TextInputEditText enter_mail, enter_profesn;
    TextInputEditText enter_ph_no;
    TextInputEditText enter_pwd;
    TextInputEditText enter_cfm_pwd;
    String otp = "", selectedGender = "", selectedEdu = "";
    RadioGroup radioSexGroup, radioEdu;
    TextInputLayout input_layout_name/*, input_layout_surname*/, input_layout_income, input_layout_mail, input_layout_pwd, input_layout_prof, input_layout_age,
            input_layout_cfm_pwd, input_layout_ph;
    private CheckBox terms_checkbox;
    private ProgressDialog progressDialog;
    TextView gender_error;

    public RegPersonelDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_personel_details, container, false);
        String title_color = PrefsWrapper.with(getActivity()).getString(JullayConstants.DEFAULT_HEADING_COLOR, JullayConstants.LIGHT_HEADING);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.reg_persnl_details) + "</font>")));
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        Button signup = view.findViewById(R.id.signup_button);
        enter_name = view.findViewById(R.id.enter_name);
        //  enter_surname = view.findViewById(R.id.enter_surname);
        enter_ph_no = view.findViewById(R.id.enter_ph_no);
        enter_age = view.findViewById(R.id.enter_age);
        enter_income = view.findViewById(R.id.enter_income);
        radioSexGroup = (RadioGroup) view.findViewById(R.id.radioSex);
        enter_mail = view.findViewById(R.id.enter_mail);
        enter_profesn = view.findViewById(R.id.enter_profesn);
        enter_pwd = view.findViewById(R.id.enter_pwd);
        enter_cfm_pwd = view.findViewById(R.id.enter_cfm_pwd);
        gender_error = view.findViewById(R.id.gender_error);
        radioEdu = (RadioGroup) view.findViewById(R.id.radioEdu);
        input_layout_name = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        //input_layout_surname = (TextInputLayout) view.findViewById(R.id.input_layout_surname);
        input_layout_mail = (TextInputLayout) view.findViewById(R.id.input_layout_mail);
        input_layout_pwd = (TextInputLayout) view.findViewById(R.id.input_layout_pwd);
        input_layout_age = (TextInputLayout) view.findViewById(R.id.input_layout_age);
        input_layout_prof = (TextInputLayout) view.findViewById(R.id.input_layout_prof);
        input_layout_cfm_pwd = (TextInputLayout) view.findViewById(R.id.input_layout_cfm_pwd);
        input_layout_ph = (TextInputLayout) view.findViewById(R.id.input_layout_ph);
        input_layout_income = (TextInputLayout) view.findViewById(R.id.input_layout_income);
        terms_checkbox = (CheckBox) view.findViewById(R.id.checkedtnc);
        TextView text_tc = (TextView) view.findViewById(R.id.text_tc);

        text_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://jullaycms.s3-website.ap-south-1.amazonaws.com/privacy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        if (PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAME, "").length() != 0) {
            enter_name.setText(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAME, ""));
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitForm()) {

                    registerPersonalDetails();
                    // RegistrationActivity.replaceFragmentHistory(new RegOTPVerifylDetails(), (AppCompatActivity) getContext());
                } else {
                    closeProgressLoader();
                }
            }
        });

        return view;
    }

    /**
     * Validating form
     */
    private boolean submitForm() {


        if (!validateName()) {
            return false;
        }
        if (!isValidMobile(enter_ph_no.getText().toString().trim())) {
            return false;
        }

        /*if (!validateSurName()) {
            return false;
        }*/
        if (!validateAge()) {
            return false;
        }

        if (!validateGender()) {
            return false;
        }
        if (!validateEmail()) {
            return false;
        }

        if (!validatePassword()) {
            return false;
        }

        /*if (!validateProf()) {
            return false;
        }

        if (!validateEDU()) {
            return false;
        }*/

        if (!isCheckPswd()) {
            return false;
        }

        if (!terms_checkbox.isChecked()) {
            CommonUtility.showErrorAlert(getContext(), getString(R.string.err_msg_tnc));
            return false;
        }

        return true;
    }


    /*private boolean validateSurName() {
        if (!CommonUtility.chkString(enter_name.getText().toString())) {
            input_layout_surname.setError(getString(R.string.err_msg_name));
            return false;
        } else {
            input_layout_surname.setErrorEnabled(false);
        }
        return true;
    }
*/
    private boolean validateEDU() {


        int selectedId = radioEdu.getCheckedRadioButtonId();

        switch (selectedId) {
            case -1:
                Toast.makeText(getActivity(), "Please select the education.", Toast.LENGTH_SHORT).show();
                return false;
            case R.id.radio_high:
                selectedEdu = CommonUtility.Education.HIGH_SCHOOL.name();
                return true;
            case R.id.radioInter:
                selectedEdu = CommonUtility.Education.INTERMEDIATE.name();
                return true;
            case R.id.radioGrad:
                selectedEdu = CommonUtility.Education.Graduate.name();
                return true;
            case R.id.radioPostGrad:
                selectedEdu = CommonUtility.Education.POST_Graduate.name();
                return true;

            default:
                return false;
        }

    }

    private boolean validateProf() {

        if (CommonUtility.chkString(enter_profesn.getText().toString())) {
            return true;
        }
        return false;
    }

    private boolean isCheckPswd() {
        if (!enter_pwd.getText().toString().trim().equals(enter_cfm_pwd.getText().toString().trim())) {
            if (enter_pwd.getText().toString().trim().length() < 8) {
                input_layout_cfm_pwd.setError("Password length must be 8 digits");

            } else {
                input_layout_cfm_pwd.setError(getString(R.string.err_Password_check));

            }
            return false;
        } else {
            input_layout_cfm_pwd.setErrorEnabled(false);

        }

        return true;
    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            /* if(phone.length() < 10) {*/
            if (phone.length() != 10) {
                // input_layout_ph.setError("Not Valid Number");
                input_layout_ph.setError(getString(R.string.err_msg_phone));
                return false;
            }
        } else {
            input_layout_ph.setErrorEnabled(false);

        }
        return true;
    }

  /*  private boolean validateCnfPassword() {
        if (!enter_cfm_pwd.getText().toString().trim().equalsIgnoreCase(enter_pwd.getText().toString().trim())) {
            input_layout_cfm_pwd.setError("Password do not match. Please re-enter.");
            return false;
        } else {
            input_layout_cfm_pwd.setErrorEnabled(false);

        }
        return true;
    }*/

    private boolean validateGender() {
        // get selected radio button from radioGroup
        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        switch (selectedId) {
            case -1:
                gender_error.setVisibility(View.VISIBLE);
                Log.d("QAOD", "Gender is Null");
                return false;
            case R.id.radioM:
                selectedGender = "MALE";
                gender_error.setVisibility(View.GONE);
                Log.d("QAOD", "Gender is Selected");
                return true;
            case R.id.radioF:
                selectedGender = "FEMALE";
                gender_error.setVisibility(View.GONE);
                Log.d("QAOD", "Gender is Selected");
                return true;
            case R.id.radioT:
                selectedGender = "TRANSGENDER";
                gender_error.setVisibility(View.GONE);
                Log.d("QAOD", "Gender is Selected");
                return true;
            default:
                return false;
        }


    }

    private boolean validateAge() {
        if (!CommonUtility.chkString(enter_age.getText().toString()) || !(Integer.parseInt(enter_age.getText().toString().trim()) < 100)) {
            input_layout_age.setError(getString(R.string.err_msg_age));
            return false;
        } else {

            if (Integer.valueOf(enter_age.getText().toString()) < 16) {
                input_layout_age.setError("You are not permitted to use this App, as your age is below 16.");
                return false;
            }
            input_layout_age.setErrorEnabled(false);
        }

        return true;

    }

    private boolean validateName() {
        if (!CommonUtility.chkString(enter_name.getText().toString())) {
            input_layout_name.setError(getString(R.string.err_msg_name));
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateEmail() {
        String email = enter_mail.getText().toString().trim();

        if (email.isEmpty() || !CommonUtility.isEmailValid(email)) {
            input_layout_mail.setError(getString(R.string.err_msg_email));

            return false;
        } else {
            input_layout_mail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (!CommonUtility.chkString(enter_pwd.getText().toString().trim())) {
            input_layout_pwd.setError(getString(R.string.err_msg_reg_password));
            return false;
        } else {

            if (enter_pwd.getText().toString().length() < 8) {
                input_layout_pwd.setError(getString(R.string.err_msg_reg_password));
                return false;
            }
            input_layout_pwd.setErrorEnabled(false);
        }

        return true;
    }


    public void registerPersonalDetails() {
        try {
            progressDialog = new ProgressDialog((AppCompatActivity) getActivity());
            progressDialog.setMessage(getString(R.string.loader_signup_msg)); // Setting Message
            progressDialog.setTitle(getString(R.string.app_name)); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RegistPDRequest registPDRequest = new RegistPDRequest();
        registPDRequest.setAge(enter_age.getText().toString());

        registPDRequest.setEmail(enter_mail.getText().toString());
        if (selectedGender.length() != 0) {
            registPDRequest.setGender(selectedGender);
        }
        final String ph = enter_ph_no.getText().toString();
        registPDRequest.setMobile(ph);
        final String name = enter_name.getText().toString();
        registPDRequest.setName(name);
        final String pwd = enter_pwd.getText().toString();
        registPDRequest.setPassword(pwd);
        registPDRequest.setCountry("INDIA");
        registPDRequest.setCity("");
        registPDRequest.setImeiNumber("");
        registPDRequest.setIncome("");
        registPDRequest.setSurname("");
        registPDRequest.setEducation("NA");
        registPDRequest.setArea("");
        registPDRequest.setProfession("");
        registPDRequest.setOffline(false);
        String payloadStr = CommonUtility.convertToJson("register", registPDRequest);

        //Log.e(TAG, "registPDRequest: " + registPDRequest.toString());

        CentralApis.getInstance().getAPIS().regPDRequest(registPDRequest).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                       /*
                       Status is true, i.e user is created
                       Status is false
                       1. if token message is offline i.e there is offline user already existing in the system, thus thus in user response, user details will be fetched.
                       Call the Update User api from the App.
                       2. If message is not offline, show the token message in the popup.
                        */
                    if (response.body().getStatus()) {


                        String uId = String.valueOf(response.body().getToken());
                        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_USER_ID, uId);
                        closeProgressLoader();
                        RegistrationActivity.replaceFragmentHistory(new RegOTPVerifylDetails().newInstance(ph, pwd), (AppCompatActivity) getContext());

                    } else {
                        closeProgressLoader();
                        // CommonUtility.showErrorAlert(getActivity(), response.body().getMessage());

                        if (response.body().getToken().equalsIgnoreCase("offline")) {
                            UserDetails user = response.body().getUserDetails();
                            if (user != null) {
                                //final String imei = CommonUtility.getUserUniqueIdentifier();

                                UpdatePersonlDeatils(user, "");
                            }

                        } else {
                            CommonUtility.showAlertOK(getActivity(), response.body().getToken());
                        }
                    }

                } else {
                    closeProgressLoader();
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
                Log.e("Retrofit call", "Response: Failure");
                closeProgressLoader();
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            registerPersonalDetails();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });

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

    public void UpdatePersonlDeatils(final UserDetails user, final String imei) {
        RegistPDRequest registPDRequest = new RegistPDRequest();
        registPDRequest.setAge(enter_age.getText().toString());

        registPDRequest.setEmail(enter_mail.getText().toString());
        if (selectedGender.length() != 0) {
            registPDRequest.setGender(selectedGender);
        }
        final String ph = enter_ph_no.getText().toString();
        registPDRequest.setMobile(ph);
        final String name = enter_name.getText().toString();
        registPDRequest.setName(name);
        final String pwd = enter_pwd.getText().toString();
        registPDRequest.setPassword(pwd);
        registPDRequest.setCountry("INDIA");
        registPDRequest.setCity("");
        registPDRequest.setImeiNumber(imei);
        registPDRequest.setIncome("");
        registPDRequest.setSurname("");
        registPDRequest.setEducation("NA");
        registPDRequest.setArea("");
        registPDRequest.setProfession("");
        registPDRequest.setOffline(false);
        String payloadStr = CommonUtility.convertToJson("register", registPDRequest);

        CentralApis.getInstance().getAPIS().updatePDRequest(registPDRequest, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<RegPer>() {
            @Override
            public void onResponse(Call<RegPer> call, Response<RegPer> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {


                        if (!user.getOtpverfied()) {
                            //User not verified - Thus redirect to OTP verify page
                            resendOTPverify(user.getUsername());
                            Intent i = new Intent(getActivity(), RegistrationActivity.class);
                            i.putExtra("page", "otp");
                            i.putExtra("ph", ph);
                            i.putExtra("pwd", pwd);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ActivityOptions options =
                                    ActivityOptions.makeCustomAnimation(getActivity(), R.animator.slide_in_left, R.animator.slide_in_right);
                            startActivity(i, options.toBundle());

                        } else {
                            PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_IS_VERIFIED, true);
                            if (PrefsWrapper.with(getActivity()).getBoolean(JullayConstants.KEY_FIRST_OPEN, true)) {
                                closeProgressLoader();

                                login(ph, pwd, imei);


                            } else {
                                closeProgressLoader();
                                login(ph, pwd, imei);


                            }

                        }
                    } else {
                        CommonUtility.showErrorAlert(getContext(), response.body().getMessage());

                    }

                } else {
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
            public void onFailure(Call<RegPer> call, Throwable t) {
                Log.e("Retrofit call", "Response: Failure");
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            UpdatePersonlDeatils(user, imei);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });

    }

    /**
     * Resend Otp i.e Otp not received
     */
    private void resendOTPverify(final String uid) {

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

        CentralApis.getInstance().getAPIS().resendOtpRequest(uid, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<OtpResponse>() {
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
                            resendOTPverify(uid);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });
    }

    public void login(final String mobile, final String password, final String imei) {

        final LoginRequest loginRequest = new LoginRequest();
        try {
            loginRequest.setPassword(password);
            loginRequest.setMobile(mobile);
            loginRequest.setImeiNumber(imei);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String payloadStr = CommonUtility.convertToJson("Login", loginRequest);

        CentralApis.getInstance().getAPIS().loginRequest(mobile, password, imei).enqueue(new retrofit2.Callback<LoginResponse>() {
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
                                ActivityOptions.makeCustomAnimation(getActivity(), R.animator.slide_in_left, R.animator.slide_in_right);
                        startActivity(i, options.toBundle());

                        getActivity().finish();
                    } else {
                        CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.network_error_text), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //retry
                                login(mobile, password, imei);
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
                            login(mobile, password, imei);
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


}
