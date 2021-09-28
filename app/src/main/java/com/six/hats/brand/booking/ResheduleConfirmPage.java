package com.six.hats.brand.booking;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.RTPFragment;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.model.booking.BookingAppointment;
import com.six.hats.brand.model.booking.BookingReq;
import com.six.hats.brand.model.booking.CancelBooking;
import com.six.hats.brand.model.booking.CancelBookingItem;
import com.six.hats.brand.model.booking.CustomerDetails;
import com.six.hats.brand.model.booking.MultiBookingDetails;
import com.six.hats.brand.model.booking.MultiBookingRequest;
import com.six.hats.brand.model.booking.MultiRequest;
import com.six.hats.brand.model.booking.QATResponseList;
import com.six.hats.brand.model.booking.RemoveSelectedStaff;
import com.six.hats.brand.model.booking.SearchQatRequest;
import com.six.hats.brand.model.booking.StaffSpecQATRequest;
import com.six.hats.brand.model.booking.StaffSpecQATResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class ResheduleConfirmPage extends Fragment {

    private static final String ARG_CENTRE_ID = "centreID";
    private static final String ARG_UN_Staffs = "cancelBookingItems";
    private static final String ARG_STAFF_ID_LIST = "staffIDList";
    private static final String ARG_SERVICE_IDs = "serviceIDs";

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private List<List<SearchQatRequest>> searchQatRequestList = new ArrayList<>();
    private int mColumnCount = 1;
    private FinalBookingListAdapter confirmAdapter;
    private String mCentreID;

    private List<String> selectedBookingID = new ArrayList<>();

    private int currentBookingPos = 1;
    private String parentBID = "0";
    private List<QATResponseList> qatResponseLists = new ArrayList<>();
    private ProgressBar mInsideProgressBar;
    private Animation animBlink;
    private String mRefTime = "";

    public List<List<String>> unSelectedStaffsDoubleList = new ArrayList<>();
    public List<List<String>> mSelectedStaffsDoubleList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    private List<CancelBookingItem> cancelBookingItems = new ArrayList<>();
    private DialogInterface cancelDialog;
    //private MultiBookingDetails bookingDetails = null;
    private MultiBookingDetails multiBookingDetails = null;
    private String currentBookingID;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResheduleConfirmPage() {
    }


    public static ResheduleConfirmPage newInstance(String mCentreId, ArrayList<String> mServiceIDs, List<List<SearchQatRequest>> searchQatRequestList,
                                                   List<List<String>> unSelectedStaffs, String currentBookingID) {
        ResheduleConfirmPage fragment = new ResheduleConfirmPage();
        Bundle args = new Bundle();
        args.putString(ARG_CENTRE_ID, mCentreId);
        args.putString("currentBookingID", currentBookingID);
        args.putStringArrayList(ARG_SERVICE_IDs, mServiceIDs);
        args.putSerializable(ARG_STAFF_ID_LIST, (Serializable) searchQatRequestList);
        args.putSerializable(ARG_UN_Staffs, (Serializable) unSelectedStaffs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCentreID = getArguments().getString(ARG_CENTRE_ID, "0");
            currentBookingID = getArguments().getString("currentBookingID", "");
            List<String> mServiceIDs = (List<String>) getArguments().getStringArrayList(ARG_SERVICE_IDs);
            searchQatRequestList = (List<List<SearchQatRequest>>) getArguments().getSerializable(ARG_STAFF_ID_LIST);
            unSelectedStaffsDoubleList = (List<List<String>>) getArguments().getSerializable(ARG_UN_Staffs);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_item_bkng_cfm, container, false);
        String title_color = PrefsWrapper.with(getActivity()).getString(JullayConstants.DEFAULT_HEADING_COLOR, JullayConstants.DARK_HEADING);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.confirm_appointment) + "</font>")));

        currentBookingPos = PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_Current_BOOKING_Pos, 1);
        mInsideProgressBar = view.findViewById(R.id.insidePB);
        parentBID = PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_PARENT_BOOKING_ID, "0");
        animBlink = AnimationUtils.loadAnimation(getActivity(),
                R.anim.blink);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        confirmAdapter = new FinalBookingListAdapter(context, qatResponseLists);
        recyclerView.setAdapter(confirmAdapter);

        int bookingType = PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);

        loadStaffSpecificQAT();

        Button confrm_booking = view.findViewById(R.id.confrm_booking);
        confrm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (qatResponseLists.size() != 0) {
                        //updateBookingStatus(Integer.parseInt(qatResponseLists.get(0).getBookingId()));
                        mInsideProgressBar.setVisibility(View.VISIBLE);

                        if (PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1) > 1) {
                            //Multi-person
                            List<String> staffIDs = new ArrayList<>();
                            /*for (int i = 0; i < searchQatRequestList.size(); i++) {*/
                            for (int j = 0; j < searchQatRequestList.get(currentBookingPos - 1).size(); j++) {
                                staffIDs.add(searchQatRequestList.get(currentBookingPos - 1).get(j).getStaffId());
                            }
                            /*}*/
                            mSelectedStaffsDoubleList.add(staffIDs);

                            if (selectedBookingID.size() == PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1)) {
                                confirmMultiAppointment(mSelectedStaffsDoubleList);
                            } else {
                                removeSelectedBooking(mSelectedStaffsDoubleList.get(currentBookingPos - 1), selectedBookingID.get(currentBookingPos - 1));


                                //do not all this for SS Booking
                          /*  cancelBookingItems.clear();
                            //for (int i = 0; i < unSelectedStaffsDoubleList.size(); i++) {
                                for (int j = 0; j < unSelectedStaffsDoubleList.get(currentBookingPos-1).size(); j++) {
                                    CancelBookingItem item = new CancelBookingItem();
                                    item.setStaffId(unSelectedStaffsDoubleList.get(currentBookingPos-1).get(j));
                                    item.setTempAppointmentId(selectedBookingID.get(currentBookingPos - 1));
                                    cancelBookingItems.add(item);
                                }
                           // }

                            cancelTempBooking(cancelBookingItems, "", true);*/

                            }
                        } else {
                            //single booking
                            List<String> staffIDs = new ArrayList<>();
                            for (int i = 0; i < qatResponseLists.size(); i++) {
                                staffIDs.add(qatResponseLists.get(i).getQatResponseList().getStaffId());
                            }
                            confirmAppointment(selectedBookingID.get(0), staffIDs);
                        }

                    } else {

                        CommonUtility.showAlertOK(getActivity(), getString(R.string.error_confirm_pressed));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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
                            cancelBookingItems.clear();

                            List<String> staffIDs = new ArrayList<>();
                            for (int i = 0; i < searchQatRequestList.size(); i++) {
                                for (int j = 0; j < searchQatRequestList.get(i).size(); j++) {
                                    staffIDs.add(searchQatRequestList.get(i).get(j).getStaffId());
                                }
                            }
                            mSelectedStaffsDoubleList.add(staffIDs);
                            if (mSelectedStaffsDoubleList != null) {
                                for (int i = 0; i < mSelectedStaffsDoubleList.size(); i++) {
                                    for (int j = 0; j < mSelectedStaffsDoubleList.get(i).size(); j++) {
                                        CancelBookingItem item = new CancelBookingItem();
                                        item.setStaffId(mSelectedStaffsDoubleList.get(i).get(j));
                                        item.setTempAppointmentId(selectedBookingID.get(i));
                                        cancelBookingItems.add(item);
                                    }
                                }

                            }

                            // }

                            Toast.makeText(getActivity(), "cbdgvuds", Toast.LENGTH_SHORT).show();
                            cancelTempBooking(cancelBookingItems, "", false);

                        }
                    });
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    /**
     * Update the status of the Ongoing Booking
     */
    public void removeSelectedBooking(final List<String> staffIds, final String temp_BookingId) {
        RemoveSelectedStaff removeSelectedStaff = new RemoveSelectedStaff();

        removeSelectedStaff.setStaffIdList(staffIds);
        removeSelectedStaff.setTemp_BookingId(temp_BookingId);

        String json = CommonUtility.convertToJson("removeFromQueSS", removeSelectedStaff);

        CentralApis.getInstance().getBookingAPIS().removeFromQue(removeSelectedStaff, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    if (data.getStatus()) {
                        currentBookingPos++;
                        onButtonPressed(currentBookingPos);
                        mInsideProgressBar.setVisibility(View.GONE);
                        loadStaffSpecificQAT();
                        //Toast.makeText(getContext(), "Removed", Toast.LENGTH_LONG).show();

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

    public void onButtonPressed(int currentBookingPos) {

        // Toast.makeText(getActivity(), "onButtonPressed - called", Toast.LENGTH_SHORT).show();
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

    /**
     * Update the status of the Ongoing Booking
     *
     * @param mBookingId
     */
    public void confirmAppointment(final String mBookingId, final List<String> staffID) {

        mInsideProgressBar.setVisibility(View.VISIBLE);
        String userId = PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, "");

        BookingAppointment appointment = new BookingAppointment();
        appointment.setBookingType("SS");
        appointment.setTempAppointmentId(mBookingId);
        appointment.setUserId(userId);
        appointment.setStaffIdList(staffID);


        String payloadStr = CommonUtility.convertToJson("confirmApptSS", appointment);


        CentralApis.getInstance().getBookingAPIS().bookAppointment(appointment, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<MultiBookingDetails>() {
            @Override
            public void onResponse(Call<MultiBookingDetails> call, Response<MultiBookingDetails> response) {

                if (response.isSuccessful()) {
                    multiBookingDetails = response.body();
                    if (multiBookingDetails.getCorrect()) {

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
     * @param itemList
     */
    public void cancelTempBooking(List<CancelBookingItem> itemList, final String selectedBookingID, final Boolean multi) {

        mInsideProgressBar.setVisibility(View.VISIBLE);

        CancelBooking booking = new CancelBooking();
        booking.setAppSignalList(itemList);


        String payloadStr = CommonUtility.convertToJson("cancelTempBookSS", booking);

        CentralApis.getInstance().getBookingAPIS().cancelTempBooking(booking, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    if (data.getStatus()) {
                        mInsideProgressBar.setVisibility(View.GONE);

                        if (multi) {
                            currentBookingPos++;
                            loadStaffSpecificQAT();
                        } else {
                            if (!CommonUtility.chkString(selectedBookingID)) {
                                if (cancelDialog != null)
                                    cancelDialog.dismiss();

                                getActivity().finish();

                            } else {
                                CommonUtility.showAlertSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name),/* getString(R.string.cfm_bkng_msg)*/ selectedBookingID, new DialogInterface.OnClickListener() {
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

                        //Toast.makeText(getContext(), "Booking cancelled", Toast.LENGTH_LONG).show();
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


    public void confirmMultiAppointment(final List<List<String>> mSelectedStaffsList) {

        mInsideProgressBar.setVisibility(View.VISIBLE);

        MultiBookingRequest request = new MultiBookingRequest();
        List<BookingReq> bookingReqList = new ArrayList<>();
        List<String> names = CommonUtility.getListFromString(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAMES_LIST, ""));

        for (int i = 0; i < selectedBookingID.size(); i++) {
            BookingReq req = new BookingReq();
            req.setStaffId(mSelectedStaffsList.get(i));
            req.setTempBookingId(selectedBookingID.get(i));
            req.setUserName(names.get(i));
            bookingReqList.add(req);

        }
        final int count = selectedBookingID.size();
        request.setMultiAppointmentBaseRequests(bookingReqList);
        request.setBookingType("SS");
        request.setUserId(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, "0"));

        String payloadStr = CommonUtility.convertToJson("cfmMultiApptSS", request);

        CentralApis.getInstance().getBookingAPIS().bookMultiAppointment(request, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<MultiBookingDetails>() {
            @Override
            public void onResponse(Call<MultiBookingDetails> call, Response<MultiBookingDetails> response) {

                if (response.isSuccessful()) {
                    multiBookingDetails = response.body();
                    if (multiBookingDetails.getCorrect()) {
                       /* cancelBookingItems.clear();
                        //  for (int i = 0; i < unSelectedStaffsDoubleList.size(); i++) {
                        for (int j = 0; j < unSelectedStaffsDoubleList.get(currentBookingPos - 1).size(); j++) {
                            CancelBookingItem item = new CancelBookingItem();
                            item.setStaffId(unSelectedStaffsDoubleList.get(currentBookingPos - 1).get(j));
                            item.setTempAppointmentId(selectedBookingID.get(currentBookingPos - 1));
                            cancelBookingItems.add(item);
                        }
                        //   }
                        cancelTempBooking(cancelBookingItems, CommonUtility.convertListToCommaString(selectedBookingID), false);*/

                        /* CommonUtility.showAlertSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name),*//* getString(R.string.cfm_bkng_msg)*//* CommonUtility.convertListToCommaString(selectedBookingID), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();*/
                        if (multiBookingDetails != null) {
                            getActivity().finish();
                            Intent appotmnt_detail = new Intent(getContext(), RTPFragment.class);
                            appotmnt_detail.putExtra("bookingID", multiBookingDetails.getAppointment().get(0).getAppointmentId());
                            appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ActivityOptions options =
                                    ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_in_left, R.anim.slide_in_right);
                            startActivity(appotmnt_detail, options.toBundle());
                            //  BookSeatActivity.replaceFragmentHistory(new AppointmentDetail().newInstance(multiBookingDetails.getAppointment().get(0).getAppointmentId(), ""), (AppCompatActivity) getContext());

                        } else {
                            getActivity().finish();
                        }
                          /*  }
                        });*/

                    } else {
                        CommonUtility.showErrorAlert(getContext(), "");
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
            public void onFailure(Call<MultiBookingDetails> call, Throwable t) {
                mInsideProgressBar.setVisibility(View.GONE);
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            confirmMultiAppointment(mSelectedStaffsList);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));

                }
            }
        });

    }

/*
    private void loadTimeSpecificQAT() {
        // mInsideProgressBar.setVisibility(View.VISIBLE);
        showProgressLoader((AppCompatActivity) getActivity(), getString(R.string.bkng_wait_msg), getString(R.string.please_wait));
        TimeSpecQATRequest timeSpecQATRequest = new TimeSpecQATRequest();
        timeSpecQATRequest.setFkCentreId(mCentreID);
        timeSpecQATRequest.setFkServiceId(CommonUtility.convertListToCommaString(mServiceIDs.get(currentBookingPos - 1)));
        timeSpecQATRequest.setFkStaffId(mStaffID);
        timeSpecQATRequest.setFkUserId(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, "0"));
        timeSpecQATRequest.setBookingType(JullayConstants.KEY_BOOKING_ONLINE);
        timeSpecQATRequest.setIsPreBooking(String.valueOf(PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_IS_PREBOOKING, 0)));
        timeSpecQATRequest.setPreBookingTime(mRefTime);

        if (currentBookingPos > 1) {

            if (!parentBID.equals("0")) {
                timeSpecQATRequest.setParentId(parentBID);
            }
            timeSpecQATRequest.setPerson(String.valueOf(currentBookingPos - 1));
        }

        //pass centre id here....
        CentralApis.getInstance().getAPIS().loadQatTimeSpecific(timeSpecQATRequest).enqueue(new retrofit2.Callback<TimeSpecQATResponse>() {
            @Override
            public void onResponse(Call<TimeSpecQATResponse> call, Response<TimeSpecQATResponse> response) {

                if (response.isSuccessful()) {
                    // mInsideProgressBar.setVisibility(View.GONE);
                    TimeSpecQATResponse staffServiceClass = response.body();
                    if (staffServiceClass.getStatus().equalsIgnoreCase("200")) {
                        closeProgressLoader();

                        TimeSpecQATResponse.Data data = staffServiceClass.getData();
                 */
/*   ConfirmBookingItems bookingItems = new ConfirmBookingItems();
                    bookingItems.setBookingMeta(data.getBookings().getBookingMeta());
                    bookingItems.setBookingId(data.getBookings().getBookingId());
                    bookingItems.setName(data.getName());
                    bookingItems.setQatTime(data.getQatTime());
                    bookingItems.setStaffImg("");
                    bookingItems.setUserId(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, ""));
                    qatResponseLists.add(bookingItems);*//*


                        if (data.getBookings().getBookingMeta() != null) {
                            for (int i = 0; i < data.getBookings().getBookingMeta().size(); i++) {
                                ConfirmBookingItems bookingItems = new ConfirmBookingItems();
                                bookingItems.setBookingMeta(data.getBookings().getBookingMeta());
                                bookingItems.setBookingId(data.getBookings().getBookingId());
                                bookingItems.setName(data.getBookings().getBookingMeta().get(i).getStaffDetail().getName());
                                bookingItems.setQatTime(data.getBookings().getBookingMeta().get(i).getStart_time());
                                int stfImg = data.getBookings().getBookingMeta().get(i).getStaffDetail().getStaffImage().size();
                                if (stfImg != 0) {
                                    bookingItems.setStaffImg(data.getBookings().getBookingMeta().get(i).getStaffDetail().getStaffImage().get(0).getImageId().getImageRemoteUrl());
                                }
                                bookingItems.setUserId(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, ""));
                                qatResponseLists.add(bookingItems);
                            }
                        }

                        if (currentBookingPos == 1) {
                            PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_PARENT_BOOKING_ID, data.getBookings().getBookingId());
                        }
                        recyclerView.setAdapter(confirmAdapter);
                        //confirmAdapter.notifyData(staffResponses);
                        confirmAdapter.notifyDataSetChanged();
                        //downTimer.start();
                    } else {
                        //  mInsideProgressBar.setVisibility(View.GONE);
                        closeProgressLoader();

                        CommonUtility.showAlertOK(getActivity(), staffServiceClass.getMessage() + " Try selecting another Staff or another time.");

                    }

                } else {
                    // mInsideProgressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        if (response.code() == 406) {
                            if (jObjError.getString("message").equalsIgnoreCase("We are processing your request.")) {
                                //Still waiting
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        loadTimeSpecificQAT();

                                    }
                                }, 3000);

                            } else {
                                CommonUtility.showAlertSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name), jObjError.getString("message"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                });
                                closeProgressLoader();

                            }
                        } else {
                            closeProgressLoader();

                        }
                    } catch (Exception e) {
                        closeProgressLoader();

                        CommonUtility.showErrorAlert(getActivity(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<TimeSpecQATResponse> call, Throwable t) {
                //mInsideProgressBar.setVisibility(View.GONE);
                closeProgressLoader();

                CommonUtility.showErrorAlert(getActivity(), t.getMessage()*/
    /*getString(R.string.network_error_text)*//*
);
            }
        });

    }
*/

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int currentBookingPos);

        void onStartBookingCountdown();


    }


    public void showProgressLoader(AppCompatActivity mContext, String message, String title) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(mContext);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    private void loadStaffSpecificQAT() {
        //mInsideProgressBar.setVisibility(View.VISIBLE);
        showProgressLoader((AppCompatActivity) getActivity(), getString(R.string.bkng_wait_msg), getString(R.string.please_wait));

        StaffSpecQATRequest staffSpecQATRequest = new StaffSpecQATRequest();
        staffSpecQATRequest.setMainCategory(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_BIZ_CATEGORY, ""));
        staffSpecQATRequest.setSubCategory(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_BIZ_SUB_CATEGORY, ""));
        staffSpecQATRequest.setUserId(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_ID, ""));
        staffSpecQATRequest.setStaffWiseQATReqList(searchQatRequestList.get(currentBookingPos - 1));

        MultiRequest multiRequest = new MultiRequest();
        int totalBooking = PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1);
        multiRequest.setTotalPerson(totalBooking);
        multiRequest.setCurrentPersonNumber(currentBookingPos);
        if (totalBooking > 1) {
            multiRequest.setIsMultiRequest(true);
            if (currentBookingPos == 1) {
                staffSpecQATRequest.setIsNewBooking(true);
            } else {
                staffSpecQATRequest.setIsNewBooking(false);
            }
        } else {
            multiRequest.setIsMultiRequest(false);
        }
        if (totalBooking > 1) {
            if (currentBookingPos == 1) {
                staffSpecQATRequest.setIsNewBooking(true);
            } else {
                staffSpecQATRequest.setIsNewBooking(false);
            }
        } else {
            staffSpecQATRequest.setIsNewBooking(true);
        }

        staffSpecQATRequest.setMultiPersonRequestCounter(multiRequest);
        staffSpecQATRequest.setBkngMode(JullayConstants.KEY_BOOKING_ONLINE);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerName(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAME, ""));
        customerDetails.setCustomerPhoneNo(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_PH, ""));

        staffSpecQATRequest.setCustomerRequest(customerDetails);

        String json = CommonUtility.convertToJson("searchSSQAT --" + mCentreID, staffSpecQATRequest);
        //pass centre id here....
        CentralApis.getInstance().getBookingAPIS().searchSSQAT(mCentreID, staffSpecQATRequest, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<StaffSpecQATResponse>() {
            @Override
            public void onResponse(Call<StaffSpecQATResponse> call, Response<StaffSpecQATResponse> response) {

                if (response.isSuccessful()) {
                    // mInsideProgressBar.setVisibility(View.GONE);
                    StaffSpecQATResponse staffServiceClass = response.body();
                    if (response.body().getStatus() != null) {
                        if (response.body().getStatus()) {

                            if (staffServiceClass.getAllBusy() == true) {

                                if (!staffServiceClass.getPleaseComeLater()) {
                                    //Still waiting
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadStaffSpecificQAT();
                                        }
                                    }, 5000);
                                } else {

                                    closeProgressLoader();
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

                                if (staffServiceClass.getStaffQATList().size() != 0) {
                                    qatResponseLists.clear();

                                    List<QATResponseList> qatResponseListsTemp = new ArrayList<>();

                                    qatResponseListsTemp.addAll(staffServiceClass.getStaffQATList());
                                    qatResponseLists.add(staffServiceClass.getStaffQATList().get(0));
                                    for (int i = 0; i < qatResponseListsTemp.size(); i++) {
                                        if ((i + 1) < qatResponseListsTemp.size()) {
                                            String second = CommonUtility.formatSpan(qatResponseListsTemp.get(i).getQatResponseList().getEndSpan().getHour(), qatResponseListsTemp.get(i).getQatResponseList().getStartSpan().getMinutes()).toString();
                                            String first = CommonUtility.formatSpan(qatResponseListsTemp.get(i - 1).getQatResponseList().getStartSpan().getHour(), qatResponseListsTemp.get(i - 1).getQatResponseList().getEndSpan().getMinutes()).toString();
                                            String diff = CommonUtility.getDurationBreakdown(second, first);


                                            if (!diff.equalsIgnoreCase("0") && !diff.equalsIgnoreCase("-1")) {
                                                QATResponseList temp = new QATResponseList();
                                                temp.setIdleDiff(String.valueOf(diff));
                                                qatResponseLists.add(temp);
                                                qatResponseLists.add(qatResponseListsTemp.get(i));
                                            }
                                            if (diff.equalsIgnoreCase("0")) {
                                                QATResponseList temp = new QATResponseList();
                                                temp.setIdleDiff(String.valueOf("0"));
                                                qatResponseLists.add(temp);
                                                qatResponseLists.add(qatResponseListsTemp.get(i));
                                            } else {

                                                if (!qatResponseLists.contains(qatResponseListsTemp.get(i))) {
                                                    qatResponseLists.add(qatResponseListsTemp.get(i));
                                                }

                                            }
                                        } else {

                                            if (!qatResponseLists.contains(qatResponseListsTemp.get(i))) {
                                                qatResponseLists.add(qatResponseListsTemp.get(i));
                                            }
                                        }
                                    }


                                    selectedBookingID.add(staffServiceClass.getStaffQATList().get(0).getQatResponseList().getTempAppointmentId());
                                    Log.d("JSON - getStaffWaitList", Arrays.toString(staffServiceClass.getStaffWaitList().toArray()));

                                    recyclerView.setAdapter(confirmAdapter);
                                    //confirmAdapter.notifyData(staffResponses);
                                    confirmAdapter.notifyDataSetChanged();
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
                            if (response.body().getMessage().equalsIgnoreCase("Only 1 appointment is allowed")) {
                                CommonUtility.showSingleButton((AppCompatActivity) getActivity(), getString(R.string.app_name), response.body().getMessage(), new DialogInterface.OnClickListener() {
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
                    closeProgressLoader();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(getActivity());
                        } else {
                            CommonUtility.showErrorAlert(getActivity(), jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        closeProgressLoader();
                        CommonUtility.showErrorAlert(getActivity(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<StaffSpecQATResponse> call, Throwable t) {
                //  mInsideProgressBar.setVisibility(View.GONE);
                closeProgressLoader();
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadStaffSpecificQAT();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));

                }
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (animBlink != null) {
            animBlink.cancel();
        }


    }

    /**
     * Update the status of the Ongoing Booking
     *
     * @param
     * @param bookingID
     * @param mEntityId
     */
    public void cancelService(final String bookingID, final String mEntityId) {

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
