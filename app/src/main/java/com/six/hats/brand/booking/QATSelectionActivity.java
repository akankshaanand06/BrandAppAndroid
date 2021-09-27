package com.six.hats.brand.booking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.BaseActivity;
import com.six.hats.brand.R;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.model.booking.CancelBooking;
import com.six.hats.brand.model.booking.CancelBookingItem;
import com.six.hats.brand.model.booking.SimpleStringItem;
import com.six.hats.brand.model.booking.TimeSpecQATResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class QATSelectionActivity extends BaseActivity implements SelectStaffFragmentSS.OnSSInteractionListener, TimeSpecificSelectionMulti.OnFragmentInteractionListener, TimeSpecificSelectionSingle.OnFragmentInteractionListener, BookingConfirmPage.OnFragmentInteractionListener {

    public int currentBookingPos;
    private PersonAdapter adapter;
    List<SimpleStringItem> personNames = new ArrayList<>();
    AppCompatActivity activity;
    LinearLayout timer_lay;
    private CountDownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_staff_selection_activity);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        currentBookingPos = PrefsWrapper.with(getApplicationContext()).getInt(JullayConstants.KEY_Current_BOOKING_Pos, 1);
        timer_lay = findViewById(R.id.timer_lay);
        // int bookingCount = CommonUtility.getListFromString(PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAMES_LIST, "")).size();

        final TextView countdown = (TextView) findViewById(R.id.countdown);
        downTimer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText(new SimpleDateFormat("ss").format(new Date(millisUntilFinished)));
            }

            public void onFinish() {
                try {
                    CommonUtility.showSingleButton(activity, getString(R.string.ops), getString(R.string.booking_time_over), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (downTimer != null) {
                                downTimer.cancel();
                            }

                            dialog.dismiss();
                            finish();


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "TIME OVER", Toast.LENGTH_LONG).show();
                    finish();

                    // CommonUtility.showErrorAlert(getActivity(), getString(R.string.booking_time_over));
                }

            }
        };
        if (savedInstanceState == null) {


            // ServiceItemFragment fragment= new ServiceItemFragment();
            //SelectQATFragment fragment= new SelectQATFragment();
            int bookingType = getIntent().getIntExtra("page", JullayConstants.TIME_SPECIFIC);
            final String mCentreID = getIntent().getStringExtra("mCentreID");
            final HashMap<Integer, List<String>> mServiceIDs = (HashMap<Integer, List<String>>) getIntent().getSerializableExtra("mServiceIDs");
            switch (bookingType) {
                case JullayConstants.STAFF_SPECIFIC:

                    replaceFragmentHistory(new SelectStaffFragmentSS().newInstance(mCentreID, mServiceIDs), activity);
                    break;
                case JullayConstants.TIME_SPECIFIC:
                    CommonUtility.showAlertOKSpannedListner(activity, getString(R.string.alert), Html.fromHtml("<font color=\"#E53935\">" + getString(R.string.timer_msg) + "</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Integer> totalDuration = (List<Integer>) getIntent().getSerializableExtra("totalDuration");
                            List<Integer> totalPrice = (List<Integer>) getIntent().getSerializableExtra("totalPrice");

                            List<String> names = CommonUtility.getListFromString(PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAMES_LIST, ""));

                            if (names.size() <= 1) {
                                replaceFragmentHistory(new TimeSpecificSelectionSingle().newInstance(mCentreID, mServiceIDs, totalDuration, totalPrice), activity);

                            } else {
                                replaceFragmentHistory(new TimeSpecificSelectionMulti().newInstance(mCentreID, mServiceIDs, totalDuration, totalPrice), activity);

                            }
                        }
                    });
                    break;
            }
            // replaceFragmentHistory(new ServiceItemFragment().newInstance(getIntent().getStringExtra("CentreId")), this);


        }
        List<String> names = CommonUtility.getListFromString(PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAMES_LIST, ""));

        for (int i = 0; i < names.size(); i++) {
            SimpleStringItem item = new SimpleStringItem();
            item.setName(names.get(i));
            if (i == 0) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
            personNames.add(item);
        }


        final RecyclerView preBookList = (RecyclerView) findViewById(R.id.person_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        preBookList.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(getApplicationContext(), personNames);
        preBookList.setAdapter(adapter);


    }


    private void addHeading(int currentPosition) {
        //    Toast.makeText(getApplicationContext(), "onButtonPressed - called--"+currentPosition, Toast.LENGTH_SHORT).show();

        currentBookingPos = currentPosition;
        switch (currentBookingPos) {

            case 1:
                for (int i = 0; i < personNames.size(); i++) {
                    if (i == 0) {
                        personNames.get(0).setSelected(true);
                    } else {
                        personNames.get(i).setSelected(false);
                    }

                }
                break;
            case 2:
                for (int i = 0; i < personNames.size(); i++) {
                    if (i == 1) {
                        personNames.get(1).setSelected(true);
                    } else {
                        personNames.get(i).setSelected(false);
                    }

                }
                break;
            case 3:
                for (int i = 0; i < personNames.size(); i++) {
                    if (i == 2) {
                        personNames.get(2).setSelected(true);
                    } else {
                        personNames.get(i).setSelected(false);
                    }

                }
                break;
            case 4:
                for (int i = 0; i < personNames.size(); i++) {
                    if (i == 3) {
                        personNames.get(3).setSelected(true);
                    } else {
                        personNames.get(i).setSelected(false);
                    }

                }
                break;
        }

        adapter.notifyData(personNames);
        //adapter.notifyDataSetChanged();
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

                transaction.replace(R.id.bsContainer, fragment);
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
    protected void onDestroy() {
        super.onDestroy();

        try {

            BookingSingleton.setServiceResponses(null);
            if (downTimer != null) {
                downTimer.cancel();
                downTimer = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFragmentInteraction(int currentBookingPos) {
        addHeading(currentBookingPos);
    }

    @Override
    public void onStartBookingCountdown() {
        if (downTimer != null) {
            downTimer.start();
            timer_lay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStopBookingCountdown() {
        if (downTimer != null) {
            downTimer.cancel();
            timer_lay.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressedFragmentMulti(final List<String> qatStaffSelected, final List<String> selectedBookingID, final List<TimeSpecQATResponse.QATList> qatList) {
        CommonUtility.showAlertTwoButtons(activity, "Are you sure you want to cancel this Reservation?", getString(R.string.yes), getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//yes
                // Toast.makeText(getActivity(), "ggggg", Toast.LENGTH_SHORT).show();
                List<CancelBookingItem> list = new ArrayList<>();
                for (int i = 0; i < qatStaffSelected.size(); i++) {
                    CancelBookingItem item = new CancelBookingItem();
                    item.setStaffId(qatStaffSelected.get(i));
                    item.setTempAppointmentId(selectedBookingID.get(i));
                    list.add(item);

                }
                for (int i = 0; i < qatList.size(); i++) {
                    CancelBookingItem item = new CancelBookingItem();
                    item.setStaffId(qatList.get(i).getStaffId());
                    item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                    list.add(item);

                }
                cancelTempBooking(list, "", false);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressedFragmentSingle(final String qatStaffSelected, final String selectedBookingID, final List<TimeSpecQATResponse.QATList> qatList) {
        CommonUtility.showAlertTwoButtons(activity, "Are you sure you want to cancel this Reservation?", getString(R.string.yes), getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//yes

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
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
/*

    @Override
    public void onBackPressedFragment(final List<String> qatStaffSelected, final List<String> selectedBookingID, final List<TimeSpecQATResponse.QATList> qatList) {

        CommonUtility.showAlertTwoButtons(activity, "Are you sure you want to cancel this Reservation?", getString(R.string.yes), getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
// Toast.makeText(getActivity(), "ggggg", Toast.LENGTH_SHORT).show();
                List<CancelBookingItem> list = new ArrayList<>();
                for (int i = 0; i < qatStaffSelected.size(); i++) {
                    CancelBookingItem item = new CancelBookingItem();
                    item.setStaffId(qatStaffSelected.get(i));
                    item.setTempAppointmentId(selectedBookingID.get(i));
                    list.add(item);

                }
                for (int i = 0; i < qatList.size(); i++) {
                    CancelBookingItem item = new CancelBookingItem();
                    item.setStaffId(qatList.get(i).getStaffId());
                    item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                    list.add(item);

                }
                cancelTempBooking(list, "", false);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

    }
*/

    @Override
    public void onSSInteraction(int currentPosition) {
        addHeading(currentPosition);
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
