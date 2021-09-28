package com.six.hats.brand.booking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
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
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
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
 * Use the {@link ResheduleTSselection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResheduleTSselection extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_CENTRE_ID = "mCentreID";
    private static final String ARG_SERVICE_ID = "mServiceID";
    private static final String ARG_TOTAL_TIME = "mTotalTime";
    private static final String ARG_TOTAL_Price = "mTotalPrice";
    private String mCentreID;
    private HashMap<Integer, List<String>> mServiceIDs = new HashMap<>();
    // private List<StaffResponse> staffResponses = new ArrayList<>();
    private String qatTimeSelected = "0";
    private String qatStaffSelected = "";
    private String selectedBookingID = "";
    private LinearLayoutManager layoutManager;
    int bookingCount = 1;
    RecyclerView qatRecyclerView;
    private OnFragmentInteractionListener mListener;
    private String parentBID = "0";
    private TimeSpecificListAdapter qatAdapter;
    private ProgressBar mInsideProgressBar;
    private int staffcurrentBookingPos = 1;
    int currentPosition = 1;

    // HashMap<String, ConfirmBookingItems> bookingItemsHashMap = new HashMap<>();
    // private ArrayList<ConfirmBookingItems> confirmBookingItems = new ArrayList<>();
    List<TimeSpecQATResponse.QATList> qatList = new ArrayList<>();
    Boolean prebooking = false;
    List<List<CancelBookingItem>> unSelectedStaffs = new ArrayList<>();

    private Animation animBlink;
    String totalDuration;
    String totalPrice;
    private ProgressDialog progressDialog;
    private DialogInterface cancelDialog;
    //private BookingDetails bookingDetails = null;
    private MultiBookingDetails multiBookingDetails = null;
    private Dialog waitingDialog = null;
    private StringHoriListAdapter servicesAdapter;
    private List<SimpleStringItem> servicesList = new ArrayList<>();
    private int currentBookingPos = 1;
    private String currentBookingID;

    public ResheduleTSselection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimeSpecificSelection.
     */
    public static ResheduleTSselection newInstance(String mCentreId, HashMap<Integer, List<String>> mServiceIdSelected, String totalDuration, String totalPrice, String currentBookingID) {
        ResheduleTSselection fragment = new ResheduleTSselection();
        Bundle args = new Bundle();
        args.putString(ARG_CENTRE_ID, mCentreId);
        args.putSerializable(ARG_SERVICE_ID, mServiceIdSelected);
        args.putString(ARG_TOTAL_TIME, totalDuration);
        args.putString(ARG_TOTAL_Price, totalPrice);
        args.putString("currentBookingID", currentBookingID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCentreID = getArguments().getString(ARG_CENTRE_ID, "0");
            mServiceIDs = (HashMap<Integer, List<String>>) getArguments().getSerializable(ARG_SERVICE_ID);
            totalDuration = (String) getArguments().getString(ARG_TOTAL_TIME, "");
            totalPrice = (String) getArguments().getString(ARG_TOTAL_Price, "");
            currentBookingID = getArguments().getString("currentBookingID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String title_color = PrefsWrapper.with(getActivity()).getString(JullayConstants.DEFAULT_HEADING_COLOR, JullayConstants.DARK_HEADING);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + "Select QAT" + "</font>")));

        View view = inflater.inflate(R.layout.fragment_qatselection, container, false);

        qatRecyclerView = (RecyclerView) view.findViewById(R.id.qat_list_staffs);
        mInsideProgressBar = view.findViewById(R.id.insidePB);

        Button confirm_Appointment = view.findViewById(R.id.confirm_booking_btn);
        confirm_Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CommonUtility.chkString(qatStaffSelected)) {
                        //single person booking
                        confirmAppointment(selectedBookingID, qatStaffSelected);

                    } else {

                        CommonUtility.showAlertOK(getActivity(), getString(R.string.error_confirm_pressed));

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

            loadTimeSpecificData(mServiceIDs.get(0), prebooking, Integer.parseInt(totalDuration));
        }


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("tag", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("tag", "onKey Back listener is working!!!");
                    CommonUtility.showAlertSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name), "Are you sure you want to cancel this Reservation?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            cancelDialog = dialog;
                            // Toast.makeText(getActivity(), "ggggg", Toast.LENGTH_SHORT).show();
                            List<CancelBookingItem> list = new ArrayList<>();

                            for (int i = 0; i < qatList.size(); i++) {
                                CancelBookingItem item = new CancelBookingItem();
                                item.setStaffId(qatList.get(i).getStaffId());
                                item.setTempAppointmentId(qatList.get(i).getTempAppointmentId());
                                list.add(item);

                            }
                            cancelTempBooking(list, "", false, "back");
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
            mListener.onBackPressedFragment(qatStaffSelected, selectedBookingID, qatList);
        }

    }

    private List<SimpleStringItem> getServiceList() {
        servicesList.clear();
        List<String> tempList = mServiceIDs.get(0);
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

        void onBackPressedFragment(String qatStaffSelected, String selectedBookingID, List<TimeSpecQATResponse.QATList> qatList);
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

        String payloadStr = CommonUtility.convertToJson("ReshedulesearchTSQAT", param);

        //pass centre id here....
        CentralApis.getInstance().getBookingAPIS().searchTSQAT(mCentreID, totalDuration, param, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<TimeSpecQATResponse>() {
            @Override
            public void onResponse(Call<TimeSpecQATResponse> call, Response<TimeSpecQATResponse> response) {
                Log.d("respnse", response.toString());
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != null) {
                        if (response.body().getStatus()) {

                            if (response.body().getAnotherReqstInProgress() == true) {

                    /*    if (response.body().getQatResponseList().size() != 0) {
                            closeProgressLoader();
                            TimeSpecQATResponse staffServiceClass = response.body();
                            qatList.clear();
                            unSelectedStaffs.clear();
                            qatList.addAll(staffServiceClass.getQatResponseList());
                            qatRecyclerView.setAdapter(qatAdapter);

                            qatAdapter.notifyData(qatList);
                        } else {*/
                                showWaitingProgressLoader(response.body().getNumberOfPersonAhedInQue());
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

                                    closeWaitingProgressLoader();
                                    CommonUtility.showAlertSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name), getString(R.string.no_staffs_message), new DialogInterface.OnClickListener() {
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
                                closeWaitingProgressLoader();

                                if (response.body().getQatResponseList().size() != 0) {
                                    TimeSpecQATResponse staffServiceClass = response.body();
                                    qatList.clear();

                                    qatList.addAll(staffServiceClass.getQatResponseList());
                                    qatRecyclerView.setAdapter(qatAdapter);
                                    qatAdapter.notifyData(qatList);
                                    //downTimer.start();
                                    onStartTimer();

                                } else {

                                    CommonUtility.showAlertSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name), getString(R.string.no_qats_message), new DialogInterface.OnClickListener() {
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
                            if (response.body().getMessage().equalsIgnoreCase("Only 1 appointment is allowed")) {
                                CommonUtility.showSingleButton((AppCompatActivity
                                        ) getActivity(), getString(R.string.app_name), response.body().getMessage(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getActivity().finish();
                                    }
                                });
                            }
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
                progressDialog.setMessage(title); // Setting Message
                progressDialog.setTitle(message); // Setting Title
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
     * @param back
     */
    public void cancelTempBooking(List<CancelBookingItem> itemList, final String selectedBookingID, final Boolean multi, String back) {

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


                        if (!CommonUtility.chkString(selectedBookingID)) {
                            if (cancelDialog != null) {
                                cancelDialog.dismiss();
                            }

                        }

                        getActivity().finish();

                        // Toast.makeText(getContext(), "Booking cancelled", Toast.LENGTH_LONG).show();
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
                    if (multiBookingDetails.getCorrect()) {
                        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_PREBOOKING_DATE, "0");
                        if (!CommonUtility.chkString(selectedBookingID)) {
                            if (cancelDialog != null) {
                                cancelDialog.dismiss();
                            }

                        }

                        cancelService(currentBookingID, "");
                    } else {
                        mInsideProgressBar.setVisibility(View.GONE);
                        CommonUtility.showErrorAlert(getContext(), "");
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
            public void onFailure(Call<MultiBookingDetails> call, Throwable t) {
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


    /**
     * Update the status of the Ongoing Booking
     *
     * @param
     * @param bookingID
     * @param mEntityId
     */
    public void
    cancelService(final String bookingID, final String mEntityId) {

        showProgressLoader((AppCompatActivity) getActivity(), "Please wait....", getResources().getString(R.string.app_name));

        CentralApis.getInstance().getBookingAPIS().cancelService(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, ""), bookingID, "NA", PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    closeProgressLoader();
                    if (data.getStatus()) {

                        CommonUtility.showErrorAlert(getActivity(), "Booking Cancelled");
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
}
