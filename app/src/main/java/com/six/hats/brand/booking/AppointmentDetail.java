package com.six.hats.brand.booking;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.R;
import com.six.hats.brand.model.booking.Appointment;
import com.six.hats.brand.model.booking.AppontmentEnitities;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.qrscanner.LivePreviewActivity;
import com.six.hats.brand.ui.AlarmManagerBroadcastReceiver;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class AppointmentDetail extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_APPT_DATA = "apptID";
    private static final String ARG_Entity_DATA = "entityID";

    // TextView centre_name;

    LinearLayout service_timings;
    TextView service_cost;

    // TextView peopleCount;s
    TextView appt_otp;
    private ProgressBar mProgress;
    private ApptsDetailItemAdapter detailsAdapter;
    private List<AppontmentEnitities> bookingsList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String mBookingId;
    private String mEntityId = "";
    private Appointment bookingsResponse;


    @SuppressWarnings("unused")
    public static AppointmentDetail newInstance(String bookingID, String entityId) {
        AppointmentDetail fragment = new AppointmentDetail();
        Bundle args = new Bundle();
        args.putSerializable(ARG_APPT_DATA, bookingID);
        args.putSerializable(ARG_Entity_DATA, entityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.appt_details_fragment_new, container, false);
        // centre_name = view.findViewById(R.id.centre_name);
        appt_otp = view.findViewById(R.id.passcode);
        // peopleCount = view.findViewById(R.id.peopleCount);
        mProgress = view.findViewById(R.id.insidePB);
        if (getArguments() != null) {
            mBookingId = getArguments().getString(ARG_APPT_DATA);
            mEntityId = getArguments().getString(ARG_Entity_DATA);
        }
        RecyclerView appt_details_list = view.findViewById(R.id.appt_details_list);

        appt_details_list.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new ApptsDetailItemAdapter(mBookingId, bookingsList, getContext());
        appt_details_list.setAdapter(detailsAdapter);


        Button cancelBooking = view.findViewById(R.id.cancelButton);
        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtility.showAlertTwoButtons(getActivity(), getString(R.string.cancel_Serv_msg), "Yes", getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //positive
                        if (CommonUtility.chkString(mBookingId)) {
                            //cancel booking
                            cancelService(mBookingId, mEntityId);

                        }
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel
                        dialog.dismiss();

                    }
                });

            }
        });
        Button reshedule = view.findViewById(R.id.reshedule);
        reshedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNewQAT();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMyBookingDetails();
    }

    private void findNewQAT() {
        if (bookingsResponse.getBookingType().equalsIgnoreCase("TS")) {
            //ts
            String mCentreID = bookingsResponse.getAppontmentEnitities().get(0).getBranchId();
            HashMap<Integer, List<String>> mServiceIDs = new HashMap<>();
            mServiceIDs.put(0, bookingsResponse.getAppontmentEnitities().get(0).getServiceIdList());
            Intent appotmnt_detail = new Intent(getActivity(), ResheduleActivity.class);
            appotmnt_detail.putExtra("CurrentBookingID", bookingsResponse.getAppointmentId());
            appotmnt_detail.putExtra("page", bookingsResponse.getBookingType());
            appotmnt_detail.putExtra("mCentreID", mCentreID);
            appotmnt_detail.putExtra("services", (Serializable) mServiceIDs);
            appotmnt_detail.putExtra("totalDuration", bookingsResponse.getAppontmentEnitities().get(0).getTotalServiceTime());
            appotmnt_detail.putExtra("totalPrice", bookingsResponse.getAppontmentEnitities().get(0).getServiceAmount());
            appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_in_right);
            startActivity(appotmnt_detail, options.toBundle());
            getActivity().finish();
        } else {
            //ss
        }
    }


    private void closeProgressLoader() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
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


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here

        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.scan_qr:
                if (CommonUtility.chkString(mBookingId)) {
                    Intent intent = new Intent(getActivity(), LivePreviewActivity.class);
                    intent.putExtra("bookingID", String.valueOf(mBookingId));
                    intent.putExtra("metaID", String.valueOf(mEntityId));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return true;
            default:
                break;
        }

        return false;
    }


    /**
     * load booking detailed from booking id
     */
    private void loadMyBookingDetails() {
        mProgress.setVisibility(View.VISIBLE);
        CentralApis.getInstance().getBookingAPIS().loadBookingByID(mBookingId, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    mProgress.setVisibility(View.GONE);
                    bookingsResponse = response.body();
                    // mBookingId = Integer.parseInt(bookingsResponse.getData().getBookingId());

                    appt_otp.setText("OTP - " + bookingsResponse.getPassCode());
                    //  centre_name.setText(bookingsResponse.getAppontmentEnitities().get(0).getBranchName());
                    bookingsList.clear();
                    bookingsList.addAll(bookingsResponse.getAppontmentEnitities());
                    detailsAdapter.notifyDataSetChanged();
                    //initUI(bookingsResponse);
                    //loadLiveStatusData(bookingsResponse.getAppointmentId());

                } else {
                    mProgress.setVisibility(View.GONE);
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
            public void onFailure(Call<Appointment> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadMyBookingDetails();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });
    }

/*
    private void initUI(SingleBookingsResponse bookingsResponse) {
        try {
            centre_name.setText("Centre Id - " + bookingsResponse.getData().getCentreId());
            int cost = 0;
            for (int i = 0; i < bookingsResponse.getData().getBookingMeta().size(); i++) {
                TextView tv = new TextView(getContext());
                tv.setText(bookingsResponse.getData().getBookingMeta().get(i).getServiceId().getServiceName());
                tv.setTextSize(18);
                tv.setTextColor(getContext().getResources().getColor(R.color.pure_white));
                cost = Integer.parseInt(cost + bookingsResponse.getData().getBookingMeta().get(i).getServiceId().getCost());

                service_timings.addView(tv);
            }

            try {
                String string = "\u20B9";
                byte[] utf8 = null;
                utf8 = string.getBytes("UTF-8");
                string = new String(utf8, "UTF-8");
                service_cost.setText(*/
    /*context.getString(R.string.rate) + *//*
" | " + string + cost);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    /**
     * Update the status of the Ongoing Booking
     *
     * @param
     * @param bookingID
     * @param mEntityId
     */
    public void cancelService(final String bookingID, final String mEntityId) {

        showProgressLoader((AppCompatActivity) getActivity(), "Finish service....", getResources().getString(R.string.app_name));

        CentralApis.getInstance().getBookingAPIS().cancelService(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, ""), bookingID, "NA", PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    closeProgressLoader();
                    if (data.getStatus()) {
                        cancelAlarm(getActivity(), String.valueOf(bookingID), CommonUtility.getStartRequestCode(String.valueOf(mBookingId)));

                        CommonUtility.showErrorAlert(getActivity(), "Reservation Cancelled");
                        getActivity().finish();
                    } else {

                        CommonUtility.showErrorAlert(getContext(), data.getMessage());
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
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                closeProgressLoader();
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            cancelService(bookingID, mEntityId);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });

    }

    public void cancelAlarm(Context context, String bookingId, String requestCode) {
        try {
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            intent.putExtra("bookingID", bookingId);
            PendingIntent sender = PendingIntent.getBroadcast(context, Integer.parseInt(requestCode), intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);

            //  Toast.makeText(activity, "Alarm cancel for - " + bookingId + " - " + requestCode, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
