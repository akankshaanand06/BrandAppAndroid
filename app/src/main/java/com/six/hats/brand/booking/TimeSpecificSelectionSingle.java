package com.six.hats.brand.booking;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.BaseFragment;
import com.six.hats.brand.R;
import com.six.hats.brand.RTPFragment;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.model.TimeSpan;
import com.six.hats.brand.model.booking.Advance;
import com.six.hats.brand.model.booking.BookingAppointment;
import com.six.hats.brand.model.booking.CancelBooking;
import com.six.hats.brand.model.booking.CancelBookingItem;
import com.six.hats.brand.model.booking.CustomerDetails;
import com.six.hats.brand.model.booking.MultiBookingDetails;
import com.six.hats.brand.model.booking.MultiRequest;
import com.six.hats.brand.model.booking.RemoveSelectedStaff;
import com.six.hats.brand.model.booking.SearchQATBodyParam;
import com.six.hats.brand.model.booking.Services;
import com.six.hats.brand.model.booking.SimpleStringItem;
import com.six.hats.brand.model.booking.TimeSpecQATResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.ui.AlarmManagerBroadcastReceiver;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeSpecificSelectionSingle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeSpecificSelectionSingle extends BaseFragment {

    private static final String ARG_CENTRE_ID = "mCentreID";
    private static final String ARG_SERVICE_ID = "mServiceID";
    private static final String ARG_TOTAL_TIME = "mTotalTime";
    private static final String ARG_TOTAL_Price = "mTotalPrice";
    private String mCentreID;
    private List<String> mServiceIDs = new ArrayList<>();

    RecyclerView qatRecyclerView;
    private OnFragmentInteractionListener mListener;
    private TimeSpecificListAdapter qatAdapter;
    private ProgressBar mInsideProgressBar;

    List<TimeSpecQATResponse.QATList> qatList = new ArrayList<>();
    Boolean prebooking = false;

    int totalDuration = 0;
    int totalPrice = 0;
    private ProgressDialog progressDialog;
    private DialogInterface cancelDialog;
    //private BookingDetails bookingDetails = null;
    private Dialog waitingDialog = null;
    private StringHoriListAdapter servicesAdapter;
    private List<SimpleStringItem> servicesList = new ArrayList<>();
    private String qatStaffSelected = "";
    private String selectedBookingID = "";
    private List<CancelBookingItem> unSelectedStaffs = new ArrayList<>();
    private MultiBookingDetails multiBookingDetails = null;

    public TimeSpecificSelectionSingle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimeSpecificSelection.
     */
    public static TimeSpecificSelectionSingle newInstance(String mCentreId, HashMap<Integer, List<String>> mServiceIdSelected, List<Integer> totalDuration, List<Integer> totalPrice) {
        TimeSpecificSelectionSingle fragment = new TimeSpecificSelectionSingle();
        Bundle args = new Bundle();
        args.putString(ARG_CENTRE_ID, mCentreId);
        args.putSerializable(ARG_SERVICE_ID, mServiceIdSelected);
        args.putSerializable(ARG_TOTAL_TIME, (Serializable) totalDuration);
        args.putSerializable(ARG_TOTAL_Price, (Serializable) totalPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCentreID = getArguments().getString(ARG_CENTRE_ID, "0");
            HashMap<Integer, List<String>> selectedServicesMap = (HashMap<Integer, List<String>>) getArguments().getSerializable(ARG_SERVICE_ID);
            mServiceIDs.addAll(selectedServicesMap.get(0));
            List<Integer> durationList = (List<Integer>) getArguments().getSerializable(ARG_TOTAL_TIME);
            totalDuration = durationList.get(0);
            List<Integer> priceList = (List<Integer>) getArguments().getSerializable(ARG_TOTAL_Price);
            totalPrice = priceList.get(0);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(/*(Html.fromHtml(title_color +*/ "Select QAT" /*+ "</font>"))*/);

        View view = inflater.inflate(R.layout.fragment_qatselection, container, false);

        qatRecyclerView = (RecyclerView) view.findViewById(R.id.qat_list_staffs);
        mInsideProgressBar = view.findViewById(R.id.insidePB);
        final List<String> names = CommonUtility.getListFromString(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAMES_LIST, ""));

        Button confirm_Appointment = view.findViewById(R.id.confirm_booking_btn);
        confirm_Appointment.setVisibility(View.GONE);
        confirm_Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CommonUtility.chkString(qatStaffSelected)) {

                        List<CancelBookingItem> list = new ArrayList<>();

                        for (int i = 0; i < qatList.size(); i++) {
                            //if staff id is not present in this, then add this in unselected list

                            if (!qatList.get(i).getStaffId().contentEquals(qatStaffSelected)) {
                                CancelBookingItem item = new CancelBookingItem();
                                item.setStaffId(qatList.get(i).getStaffId());
                                item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                                list.add(item);
                            }
                        }

                        if (list.size() != 0) {
                            unSelectedStaffs.addAll(list);
                        }

                        if (unSelectedStaffs != null) {
                            for (int k = 0; k < unSelectedStaffs.size(); k++) {
                                if (unSelectedStaffs.get(k).getStaffId().equals(qatStaffSelected)
                                        && unSelectedStaffs.get(k).getTempAppointmentId().equals(selectedBookingID)) {
                                    unSelectedStaffs.remove(k);
                                }
                            }
                        }
                        onStopTimer();
                        //single person booking
                        confirmAppointment(selectedBookingID, qatStaffSelected);

                    } else {
                        CommonUtility.showAlertOKSpanned(getActivity(), getString(R.string.app_name), Html.fromHtml(getString(R.string.error_confirm_pressed) + " " + "<b>" + PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAME, "") + "</b>"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        qatAdapter = new TimeSpecificListAdapter(qatList, getContext());

        //  qatRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        qatRecyclerView.setLayoutManager(layoutManager);
        qatRecyclerView.smoothScrollToPosition(0);
       /*DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        qatRecyclerView.addItemDecoration(itemDecorator);*/

        qatAdapter.setOnItemClickListener(new TimeSpecificListAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                CommonUtility.showAlertTwoButtons(getActivity(), "Click on Confirm to reserve this QAT.", getString(R.string.confirm), "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //positive
                        qatStaffSelected = qatList.get(position).getStaffId();
                        selectedBookingID = qatList.get(position).getTempAppointmentId();
                        qatList.get(position).setSelected(true);
                        if (qatList.get(position).getSelected()) {
                            for (int i = 0; i < qatList.size(); i++) {
                                if (i != position) {
                                    qatList.get(i).setSelected(false);
                                }
                            }
                        }
                        qatAdapter.notifyData(qatList);

                        try {
                            if (CommonUtility.chkString(qatStaffSelected)) {

                                List<CancelBookingItem> list = new ArrayList<>();

                                for (int i = 0; i < qatList.size(); i++) {
                                    //if staff id is not present in this, then add this in unselected list

                                    if (!qatList.get(i).getStaffId().contentEquals(qatStaffSelected)) {
                                        CancelBookingItem item = new CancelBookingItem();
                                        item.setStaffId(qatList.get(i).getStaffId());
                                        item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                                        list.add(item);
                                    }
                                }

                                if (list.size() != 0) {
                                    unSelectedStaffs.addAll(list);
                                }

                                if (unSelectedStaffs != null) {
                                    for (int k = 0; k < unSelectedStaffs.size(); k++) {
                                        if (unSelectedStaffs.get(k).getStaffId().equals(qatStaffSelected)
                                                && unSelectedStaffs.get(k).getTempAppointmentId().equals(selectedBookingID)) {
                                            unSelectedStaffs.remove(k);
                                        }
                                    }
                                }
                                onStopTimer();
                                //single person booking
                                confirmAppointment(selectedBookingID, qatStaffSelected);
                            } else {
                                CommonUtility.showAlertOK(getActivity(), getString(R.string.error_confirm_pressed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //negative
                        dialog.dismiss();
                    }
                });


            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d("", "onItemLongClick pos = " + position);
            }
        });

        if (mServiceIDs.size() != 0) {

            if (PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_IS_PREBOOKING, 0) == 1) {
                prebooking = true;
            }

            final RecyclerView services_list = (RecyclerView) view.findViewById(R.id.services_list);
            final LinearLayoutManager layoutManager_servs = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
            services_list.setLayoutManager(layoutManager_servs);
            servicesAdapter = new StringHoriListAdapter(getActivity(), getServiceList());
            services_list.setAdapter(servicesAdapter);

            servicesAdapter.setOnItemClickListener(new StringHoriListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Log.d("service -- ", String.valueOf(position));
                }

                @Override
                public void onItemLongClick(int position, View v) {

                }
            });

            loadTimeSpecificData(mServiceIDs, prebooking, totalDuration);
        }


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("tag", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("tag", "onKey Back listener is working!!!");

                    CommonUtility.showAlertTwoButtons((AppCompatActivity) getActivity(), "Are you sure you want to cancel this Reservation?", getString(R.string.yes), getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//yes

                            cancelDialog = dialog;
                            // Toast.makeText(getActivity(), "ggggg", Toast.LENGTH_SHORT).show();
                            List<CancelBookingItem> list = new ArrayList<>();

                            for (int i = 0; i < qatList.size(); i++) {
                                CancelBookingItem item = new CancelBookingItem();
                                item.setStaffId(qatList.get(i).getStaffId());
                                item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                                list.add(item);

                            }
                            cancelTempBooking(list, "");

                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    public void onCloseBooking() {

        //Toast.makeText(getActivity(), "onButtonPressed - called", Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onBackPressedFragmentSingle(qatStaffSelected, selectedBookingID, qatList);
        }

    }

    private List<SimpleStringItem> getServiceList() {
        servicesList.clear();
        List<String> tempList = mServiceIDs;
        for (int i = 0; i < tempList.size(); i++) {
            SimpleStringItem item = new SimpleStringItem();
            item.setName(tempList.get(i));
            item.setSelected(false);
            servicesList.add(item);
        }


        return servicesList;
    }


    public void onButtonPressed(int currentBookingPos) {

        //Toast.makeText(getActivity(), "onButtonPressed - called", Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onFragmentInteraction(currentBookingPos);
        }
    }

    public void onStartTimer() {

        // Toast.makeText(getActivity(), "onButtonPressed - called", Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onStartBookingCountdown();
        }
    }

    public void onStopTimer() {

        // Toast.makeText(getActivity(), "onButtonPressed - called", Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onStopBookingCountdown();
        }
    }

    public List<String> getHashMapValueFromIndex(HashMap hashMap, int index) {
        List<String> value = new ArrayList<>();
        String key = null;
        HashMap<String, Object> hs = hashMap;
        int pos = 0;
        for (Map.Entry<String, Object> entry : hs.entrySet()) {
            if (index == pos) {
                key = entry.getKey();
                value = (List<String>) entry.getValue();
            }
            pos++;
        }
        return value;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cancelDialog == null) {
            onCloseBooking();
        }


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int currentBookingPos);

        void onStartBookingCountdown();

        void onStopBookingCountdown();

        void onBackPressedFragmentSingle(String qatStaffSelected, String selectedBookingID, List<TimeSpecQATResponse.QATList> qatList);
    }

    private void loadTimeSpecificData(final List<String> serviceIDs, final Boolean prebooking, final int totalDuration) {
        showProgressLoader((AppCompatActivity) getActivity(), getString(R.string.bkng_wait_msg), getString(R.string.please_wait));
        Services services = new Services();
        services.setServices(serviceIDs);
        services.setServicePrice(String.valueOf(totalPrice));
        Advance advance = new Advance();
        advance.setAdvance(prebooking);
        if (PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_PREBOOKING_DATE, "0").equalsIgnoreCase("0")) {
            advance.setApntDate("");
        } else {
            long date = CommonUtility.getTimestampFromString(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_PREBOOKING_DATE, "0"));
            String newDate = CommonUtility.formatFullDate(date);
            advance.setApntDate(newDate);
        }
        TimeSpan span = new TimeSpan();
        span.setHour(CommonUtility.getHrsFromStringDate(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_PREBOOKING_DATE, "0")));
        span.setMinutes(CommonUtility.getMinsFromStringDate(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_PREBOOKING_DATE, "0")));
        span.setTotal((span.getHour() * 60) + span.getMinutes());
        advance.setBkngRequestStartDateTime(span);

        MultiRequest multiRequest = new MultiRequest();
        int totalBooking = PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1);
        multiRequest.setTotalPerson(totalBooking);
        multiRequest.setCurrentPersonNumber(1);
        if (totalBooking > 1) {
            multiRequest.setIsMultiRequest(true);
        } else {
            multiRequest.setIsMultiRequest(false);
        }

        SearchQATBodyParam param = new SearchQATBodyParam();

        param.setMainCategory(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_BIZ_CATEGORY, ""));
        param.setSubCategory(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_BIZ_SUB_CATEGORY, ""));
        param.setUserId(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, ""));
        param.setAdvanceBookingRequest(advance);
        param.setServiceList(services);
        param.setMultiPersonRequestCounter(multiRequest);
        param.setIsNewBooking(true);
        param.setBkngMode(JullayConstants.KEY_BOOKING_ONLINE);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerName(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAME, ""));
        customerDetails.setCustomerPhoneNo(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_PH, ""));

        param.setCustomerRequest(customerDetails);

        String payloadStr = CommonUtility.convertToJson("searchTSQAT -" + mCentreID, param);

        //pass centre id here....
        CentralApis.getInstance().getBookingAPIS().searchTSQAT(mCentreID, totalDuration, param, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<TimeSpecQATResponse>() {
            @Override
            public void onResponse(Call<TimeSpecQATResponse> call, Response<TimeSpecQATResponse> response) {
                Log.d("respnse", response.toString());
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != null) {
                        if (response.body().getStatus()) {

                            if (response.body().getAnotherReqstInProgress()) {

                    /*    if (response.body().getQatResponseList().size() != 0) {
                            closeProgressLoader();
                            TimeSpecQATResponse staffServiceClass = response.body();
                            qatList.clear();
                            unSelectedStaffs.clear();
                            qatList.addAll(staffServiceClass.getQatResponseList());
                            qatRecyclerView.setAdapter(qatAdapter);

                            qatAdapter.notifyData(qatList);
                        } else {*/
                                //showWaitingProgressLoader(response.body().getNumberOfPersonAhedInQue());
                                if (!response.body().getNoQATsAvailGoToSSBkng()) {
                                    //Still waiting
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadTimeSpecificData(serviceIDs, prebooking, totalDuration);
                                        }
                                    }, 5000);
                                } else {
                                    closeProgressLoader();
                                    closeWaitingProgressLoader();
                                    String msg = getString(R.string.no_staffs_message);
                                    if (CommonUtility.chkString(response.body().getMessage())) {
                                        msg = response.body().getMessage();
                                    }

                                    CommonUtility.showAlertOKSpannedListner((AppCompatActivity) getActivity(), getString(R.string.app_name), Html.fromHtml(msg), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            getActivity().finish();
                                            //todo booking summary
                                            // BookSeatActivity.replaceFragmentHistory(new AppointmentDetail().newInstance(data.getData().getBookingId()), (AppCompatActivity) getContext());

                                        }
                                    });
                                }
                                /* }*/
                            } else {


                                // allbusy -- false
                                closeProgressLoader();
                                //closeWaitingProgressLoader();

                                if (response.body().getQatResponseList().size() != 0) {
                                    TimeSpecQATResponse staffServiceClass = response.body();
                                    qatList.clear();

                                    qatList.addAll(staffServiceClass.getQatResponseList());
                                    qatRecyclerView.setAdapter(qatAdapter);
                                    qatAdapter.notifyData(qatList);
                                    //downTimer.start();
                                    onStartTimer();

                                } else {

                                    String msg = getString(R.string.no_staffs_message);
                                    if (CommonUtility.chkString(response.body().getMessage())) {
                                        msg = response.body().getMessage();
                                    }

                                    CommonUtility.showAlertOKSpannedListner((AppCompatActivity) getActivity(), getString(R.string.app_name), Html.fromHtml(msg), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            getActivity().finish();
                                            //todo booking summary
                                            // BookSeatActivity.replaceFragmentHistory(new AppointmentDetail().newInstance(data.getData().getBookingId()), (AppCompatActivity) getContext());

                                        }
                                    });

                                }
                            }
                        } else {
                            closeProgressLoader();
                            CommonUtility.showAlertOKSpannedListner((AppCompatActivity
                                    ) getActivity(), getString(R.string.app_name), Html.fromHtml("<font color=\"#E53935\">" + response.body().getMessage() + "</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getActivity().finish();
                                }
                            });

                        }
                    } else {
                        closeProgressLoader();
                        CommonUtility.showAlertSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name), "Please Update the App.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getActivity().finish();
                                //todo booking summary
                                // BookSeatActivity.replaceFragmentHistory(new AppointmentDetail().newInstance(data.getData().getBookingId()), (AppCompatActivity) getContext());

                            }
                        });
                    }

                } else {
                    closeWaitingProgressLoader();
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
            public void onFailure(Call<TimeSpecQATResponse> call, Throwable t) {
                closeWaitingProgressLoader();
                closeProgressLoader();
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadTimeSpecificData(serviceIDs, prebooking, totalDuration);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));
                }
            }
        });

    }

    public void showWaitingProgressLoader(String count) {
        try {
           /* if (progressDialog == null) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage(message); // Setting Message
                progressDialog.setTitle(title); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
            } else {

            }*/
            TextView personCount = null;
            ImageView waiting_img = null;
            if (waitingDialog == null) {
                waitingDialog = new Dialog(getActivity());
                waitingDialog.setContentView(R.layout.qat_waiting_popup);
                waitingDialog.setCancelable(false);
                personCount = (TextView) waitingDialog.findViewById(R.id.personCount);
                waiting_img = (ImageView) waitingDialog.findViewById(R.id.waiting_img);
            }
            try {

                if (waiting_img != null) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 300);
                    layoutParams.gravity = Gravity.CENTER;
                    waiting_img.setLayoutParams(layoutParams);
                    waiting_img.setBackgroundResource(R.drawable.waiting_anim_1);
                    AnimationDrawable animationDrawable = (AnimationDrawable) waiting_img.getBackground();

                    if (!animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            if (personCount != null) {
                personCount.setText("There are " + count + " persons ahead of you in queue.");
            }
            if (!waitingDialog.isShowing()) {
                waitingDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void closeWaitingProgressLoader() {
        try {
           /* if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }*/

            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressLoader(AppCompatActivity activity, String message, String title) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(message); // Setting Message
                progressDialog.setTitle(title); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
            } else {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void closeProgressLoader() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Update the status of the Ongoing Booking
     *
     * @param itemList
     */
    public void cancelTempBooking(List<CancelBookingItem> itemList, final String selectedBookingID) {

        mInsideProgressBar.setVisibility(View.VISIBLE);

        CancelBooking booking = new CancelBooking();
        booking.setAppSignalList(itemList);


        String payloadStr = CommonUtility.convertToJson("cancelTmpBookTS", booking);

        CentralApis.getInstance().getBookingAPIS().cancelTempBooking(booking, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    if (data.getStatus()) {
                        mInsideProgressBar.setVisibility(View.GONE);
                        closeProgressLoader();

                        if (!CommonUtility.chkString(selectedBookingID)) {
                            if (cancelDialog != null) {
                                cancelDialog.dismiss();
                            }
                            getActivity().finish();

                        } else {
                            bookingDone(multiBookingDetails);

                        }

                        // Toast.makeText(getContext(), "Booking cancelled", Toast.LENGTH_LONG).show();
                    } else {
                        mInsideProgressBar.setVisibility(View.GONE);
                        closeProgressLoader();
                        CommonUtility.showErrorAlert(getContext(), data.getMessage());
                    }

                } else {
                    mInsideProgressBar.setVisibility(View.GONE);
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
                mInsideProgressBar.setVisibility(View.GONE);
                closeProgressLoader();
                CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));
            }
        });

    }

    private void bookingDone(MultiBookingDetails multiBookingDetails) {
        if (multiBookingDetails != null && multiBookingDetails.getAppointment() != null && multiBookingDetails.getAppointment().size() != 0) {

            Calendar c = Calendar.getInstance();
            if (PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_IS_PREBOOKING, 0) == 1) {
                long date = Long.parseLong(String.valueOf(multiBookingDetails.getAppointment().get(0).getAppontmentEnitities().get(0).getBookingSlot().getStartSpanDate()));
                c.setTime(new Date(date));
            } else {
                c.setTime(new Date(System.currentTimeMillis()));
            }

            c.set(Calendar.HOUR_OF_DAY, multiBookingDetails.getAppointment().get(0).getAppontmentEnitities().get(0).getBookingSlot().getStartSpan().getHour());
            c.set(Calendar.MINUTE, multiBookingDetails.getAppointment().get(0).getAppontmentEnitities().get(0).getBookingSlot().getStartSpan().getMinutes());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Date date = c.getTime();
            //set alarm for customer
            setOnetimeTimer(getActivity(), multiBookingDetails.getAppointment().get(0).getAppointmentId(),
                    date.getTime(),
                    CommonUtility.getStartRequestCode(multiBookingDetails.getAppointment().get(0).getAppointmentId()), String.valueOf(multiBookingDetails.getAppointment().get(0).getAppontmentEnitities().get(0).getBookingSlot().getStartSpanDate()));
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_in_right);
                              /*  if (prebooking) {
                                    Intent appotmnt_detail = new Intent(getActivity(), AppointmentDetailsActivity.class);
                                    appotmnt_detail.putExtra("BID", multiBookingDetails.getAppointment().get(0).getAppointmentId().replace("temp_", ""));
                                    appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(appotmnt_detail, options.toBundle());
                                } else {*/
            Intent appotmnt_detail = new Intent(getContext(), RTPFragment.class);
            appotmnt_detail.putExtra("bookingID", multiBookingDetails.getAppointment().get(0).getAppointmentId().replace("temp_", ""));
            appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(appotmnt_detail, options.toBundle());
            //}


            getActivity().finish();

        } else {
            getActivity().finish();
        }

        closeProgressLoader();
    }


    /**
     * Update the status of the Ongoing Booking
     */
    public void removeSelectedBooking(final String staffIds, final String temp_BookingId) {

        RemoveSelectedStaff removeSelectedStaff = new RemoveSelectedStaff();
        List<String> list = new ArrayList<>();
        list.add(staffIds);
        removeSelectedStaff.setStaffIdList(list);
        removeSelectedStaff.setTemp_BookingId(temp_BookingId);

        String json = CommonUtility.convertToJson("removeFromQueTS", removeSelectedStaff);
        CentralApis.getInstance().getBookingAPIS().removeFromQue(removeSelectedStaff, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    if (data.getStatus()) {
                   /*     currentBookingPos++;
                        loadTimeSpecificData(mServiceIDs.get(currentBookingPos - 1), prebooking, totalDuration.get(currentBookingPos - 1));
*/
                        //  Toast.makeText(getContext(), "Removed", Toast.LENGTH_LONG).show();

                    } else {
                        mInsideProgressBar.setVisibility(View.GONE);

                        CommonUtility.showErrorAlert(getContext(), data.getMessage());
                    }

                } else {
                    mInsideProgressBar.setVisibility(View.GONE);

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
                mInsideProgressBar.setVisibility(View.GONE);
                CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));
            }
        });

    }

    /**
     * Update the status of the Ongoing Booking
     *
     * @param mBookingId
     */
    public void confirmAppointment(final String mBookingId, final String staffID) {

        mInsideProgressBar.setVisibility(View.VISIBLE);

        showProgressLoader((AppCompatActivity) getActivity(), getString(R.string.confirm_appt_loader_text), getString(R.string.please_wait));
        BookingAppointment appointment = new BookingAppointment();
        appointment.setBookingType("TS");
        appointment.setTempAppointmentId(mBookingId);
        appointment.setUserId(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, ""));
        List<String> list = new ArrayList<>();
        list.add(staffID);
        appointment.setStaffIdList(list);


        String json = CommonUtility.convertToJson("confirmAppoTS", appointment);

        String userId = PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, "");
        CentralApis.getInstance().getBookingAPIS().bookAppointment(appointment, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<MultiBookingDetails>() {
            @Override
            public void onResponse(Call<MultiBookingDetails> call, Response<MultiBookingDetails> response) {

                if (response.isSuccessful()) {
                    multiBookingDetails = response.body();
                    if (multiBookingDetails != null) {
                        if (multiBookingDetails.getCorrect()) {


                            if (unSelectedStaffs != null && unSelectedStaffs.size() != 0) {


                                if (multiBookingDetails.getAppointment() != null && multiBookingDetails.getAppointment().size() != 0) {
                                    int size = multiBookingDetails.getAppointment().size();
                                    cancelTempBooking(unSelectedStaffs, multiBookingDetails.getAppointment().get(size - 1).getAppointmentId());
                                } else {
                                    cancelTempBooking(unSelectedStaffs, null);
                                }
                            } else {
                                bookingDone(multiBookingDetails);
                            }


                        } else {
                            closeProgressLoader();
                            mInsideProgressBar.setVisibility(View.GONE);
                            CommonUtility.showErrorAlert(getContext(), "");
                        }
                    }

                } else {
                    mInsideProgressBar.setVisibility(View.GONE);
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
            public void onFailure(Call<MultiBookingDetails> call, Throwable t) {
                closeProgressLoader();
                mInsideProgressBar.setVisibility(View.GONE);
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            confirmAppointment(mBookingId, staffID);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));

                }
            }
        });

    }


    public void setOnetimeTimer(Context context, String bookingId, long time, String requestCode, String originalTimestamp) {

        long updatedTime = time - (20 * 60 * 1000);

        //start alarm
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("bookingID", bookingId);
        bundle.putString("time", String.valueOf(originalTimestamp));
        intent.putExtras(bundle);
        PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(requestCode), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, updatedTime, pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, updatedTime, pi);
        }

        boolean alarmUp = (PendingIntent.getBroadcast(context, Integer.parseInt(requestCode),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT) != null);

        if (alarmUp) {
            Log.d("myTag", "Alarm is already active");
            Toast.makeText(getActivity(), "Alarm set for - " + CommonUtility.formatDateTimeBoth(updatedTime), Toast.LENGTH_LONG).show();

        } else {
            // Toast.makeText(getActivity(), "Could not create alarm.", Toast.LENGTH_LONG).show();

        }
    }

    /**
     * Back pressed send from activity.
     *
     * @return if event is consumed, it will return true.
     */
    @Override
    public boolean onBackPressed() {
        /*if (openedLine < 0) {
            return false;
        } else {
            //closeDetail();
            return true;
        }*/


        CommonUtility.showAlertTwoButtons((AppCompatActivity) getActivity(), "Do you want to cancel the Advance Reservation?", getString(R.string.yes), getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//yes

                cancelDialog = dialog;
                // Toast.makeText(getActivity(), "ggggg", Toast.LENGTH_SHORT).show();
                List<CancelBookingItem> list = new ArrayList<>();

                for (int i = 0; i < qatList.size(); i++) {
                    CancelBookingItem item = new CancelBookingItem();
                    item.setStaffId(qatList.get(i).getStaffId());
                    item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                    list.add(item);

                }
                cancelTempBooking(list, "");

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return true;
    }


}
