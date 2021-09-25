package com.six.hats.brand;

import android.content.Context;

import androidx.multidex.MultiDexApplication;


import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class MyApplication extends MultiDexApplication {
    private Context mContext;
   // public LocaleManager localeManager;
    private String TAG = "Application";


    public Context getContext() {


        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        //CommonUtility.bypassHiddenApiRestrictions();
        PrefsWrapper.with(getApplicationContext()).save(JullayConstants.KEY_BranchQR, "SvBtE6RpC1");

        /*localeManager = new LocaleManager(getContext());
        localeManager.setLocale(getContext());*/
      /*  FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        sendRegistrationToServer(token);

                        if (PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_FIREBASE_TOKEN, "").length() != 0) {
                            if (!PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_FIREBASE_TOKEN, "").equals(token)) {
                                sendRegistrationToServer(token);

                            }
                        } else {

                            sendRegistrationToServer(token);
                        }
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("Instance Token----", msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });*/

    }


    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
   /* private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.

        SendFirebaseTokenParam params = new SendFirebaseTokenParam();
        params.setStaffId(PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_ID, "0"));
        params.setDeviceToken(token);
        CentralApis.getInstance().getAPIS().sendFirebaseTokenToServer(PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_ID, "0"), token*//*, PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN,"")*//*).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        BasicResponse data = response.body();
                        PrefsWrapper.with(getApplicationContext()).save(JullayConstants.KEY_FIREBASE_TOKEN, token);
                        // Toast.makeText(getApplicationContext(), "Token registered", Toast.LENGTH_LONG).show();

                    }

                } else {
                    // mInsideProgressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtility.showErrorAlert(getApplicationContext(), jObjError.getString("message"));
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getApplicationContext(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                //mInsideProgressBar.setVisibility(View.GONE);
                CommonUtility.showErrorAlert(getApplicationContext(), getString(R.string.network_error_text));
            }
        });


    }*/

}