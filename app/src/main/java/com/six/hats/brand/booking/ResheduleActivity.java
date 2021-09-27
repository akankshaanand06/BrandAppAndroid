package com.six.hats.brand.booking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.six.hats.brand.R;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.model.booking.CancelBooking;
import com.six.hats.brand.model.booking.CancelBookingItem;
import com.six.hats.brand.model.booking.TimeSpecQATResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ResheduleActivity extends AppCompatActivity implements ResheduleTSselection.OnFragmentInteractionListener, ResheduleStaffSS.OnSSInteractionListener {
    AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reshedule);
        activity = this;
        if (savedInstanceState == null) {
            int bookingType = getIntent().getIntExtra("page", JullayConstants.TIME_SPECIFIC);
            String mCentreID = getIntent().getStringExtra("mCentreID");
            String CurrentBookingID = getIntent().getStringExtra("CurrentBookingID");


            HashMap<Integer, List<String>> services = (HashMap<Integer, List<String>>) getIntent().getSerializableExtra("services");
            switch (bookingType) {
                case JullayConstants.STAFF_SPECIFIC:

                    replaceFragmentHistory(new ResheduleStaffSS().newInstance(mCentreID, services.get(0), CurrentBookingID), this);
                    break;
                case JullayConstants.TIME_SPECIFIC:

                    String totalDuration = (String) getIntent().getSerializableExtra("totalDuration");
                    String totalPrice = (String) getIntent().getSerializableExtra("totalPrice");

                    replaceFragmentHistory(new ResheduleTSselection().newInstance(mCentreID, services, totalDuration, totalPrice, CurrentBookingID), this);
                    break;
            }
        }

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
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);

                transaction.replace(R.id.resheduleContainer, fragment);
                //transaction.addToBackStack(null);
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


    @Override
    public void onFragmentInteraction(int currentBookingPos) {

    }

    @Override
    public void onStartBookingCountdown() {

    }

    @Override
    public void onBackPressedFragment(final String qatStaffSelected, final String selectedBookingID, final List<TimeSpecQATResponse.QATList> qatList) {
        CommonUtility.showAlertSingleButton(activity, getString(R.string.app_name), "Are you sure you want to cancel this Reservation?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Toast.makeText(getActivity(), "ggggg", Toast.LENGTH_SHORT).show();
                List<CancelBookingItem> list = new ArrayList<>();

                for (int i = 0; i < qatList.size(); i++) {
                    CancelBookingItem item = new CancelBookingItem();
                    item.setStaffId(qatList.get(i).getStaffId());
                    item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                    list.add(item);

                }
                cancelTempBooking(list, "", false);

            }
        });
    }

    @Override
    public void onSSInteraction(int currentPosition) {

    }

    /**
     * Update the status of the Ongoing Booking
     *
     * @param itemList
     */
    public void cancelTempBooking(List<CancelBookingItem> itemList, final String selectedBookingID, final Boolean multi) {

        CommonUtility.showProgressLoader(activity, "Please wait", "Jullay");
        CancelBooking booking = new CancelBooking();
        booking.setAppSignalList(itemList);


        String payloadStr = CommonUtility.convertToJson("cancelTmpBookTS", booking);

        CentralApis.getInstance().getBookingAPIS().cancelTempBooking(booking, PrefsWrapper.with(activity).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    if (data.getStatus()) {
                        CommonUtility.closeProgressLoader();
                        finish();

                        // Toast.makeText(getContext(), "Booking cancelled", Toast.LENGTH_LONG).show();
                    } else {

                        CommonUtility.showErrorAlert(activity, data.getMessage());
                    }

                } else {
                    CommonUtility.closeProgressLoader();
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
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                CommonUtility.closeProgressLoader();
                CommonUtility.showErrorAlert(activity, getString(R.string.network_error_text));
            }
        });

    }
}