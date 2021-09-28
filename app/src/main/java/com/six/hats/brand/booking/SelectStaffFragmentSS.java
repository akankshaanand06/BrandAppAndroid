package com.six.hats.brand.booking;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.BaseFragment;
import com.six.hats.brand.R;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.model.TimeSpan;
import com.six.hats.brand.model.booking.Advance;
import com.six.hats.brand.model.booking.SearchQatRequest;
import com.six.hats.brand.model.booking.ServiceList;
import com.six.hats.brand.model.booking.Services;
import com.six.hats.brand.model.booking.SimpleStringItem;
import com.six.hats.brand.model.booking.StaffList;
import com.six.hats.brand.model.booking.StaffSpecificResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.ui.CenterZoomLayoutManager;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnSSInteractionListener}
 * interface.
 */
public class SelectStaffFragmentSS extends BaseFragment {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_CENTRE_ID = "mCentreID";
    private static final String ARG_SERVICE_ID = "mServiceID";
    public List<SearchQatRequest> searchQatRequestList = new ArrayList<>();
    public List<List<SearchQatRequest>> bookingRequestList = new ArrayList<>();
    public List<List<String>> unSelectedStaffs = new ArrayList<>();


    RecyclerView recyclerView;
    QATChildAdapter centreQATAdapter;
    int currentPosition = 1;

    private OnSSInteractionListener mListener;
    private List<StaffSpecificResponse.Data> staffServiceData = new ArrayList<StaffSpecificResponse.Data>();
    private HashMap<Integer, List<String>> mServiceIDs = new HashMap<>();
    private String mCentreID;
    private int currentBookingPos;
    private Animation animBlink;
    private ProgressBar mInsideProgressBar;
    private List<SimpleStringItem> servicesList = new ArrayList<>();
    private StringHoriListAdapter servicesAdapter;
    private List<StaffList> servWiseStaffList = new ArrayList<>();
    private String mStaffSelected;
    private String mSelectedServiceId;
    private String mSelectedServTemp;
    private int mServiceSelectePosition = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SelectStaffFragmentSS() {
    }


    public static SelectStaffFragmentSS newInstance(String mCentreId, HashMap<Integer, List<String>> mServiceIdSelected) {
        SelectStaffFragmentSS fragment = new SelectStaffFragmentSS();
        Bundle args = new Bundle();
        args.putString(ARG_CENTRE_ID, mCentreId);
        args.putSerializable(ARG_SERVICE_ID, mServiceIdSelected);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCentreID = getArguments().getString(ARG_CENTRE_ID, "0");
            mServiceIDs = (HashMap<Integer, List<String>>) getArguments().getSerializable(ARG_SERVICE_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_new, container, false);
        String title_color = PrefsWrapper.with(getActivity()).getString(JullayConstants.DEFAULT_HEADING_COLOR, JullayConstants.DARK_HEADING);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.slect_ny_profsnl) + "</font>")));
        currentBookingPos = PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_Current_BOOKING_Pos, 1);
        mInsideProgressBar = view.findViewById(R.id.insidePB);
        if (mServiceIDs.size() != 0) {

            loadStaffSpecific(/*CommonUtility.convertListToCommaString(*/mServiceIDs.get(currentBookingPos - 1)/*)*/);
        }
        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new CenterZoomLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);
        centreQATAdapter = new QATChildAdapter(getContext(), servWiseStaffList);
        recyclerView.setAdapter(centreQATAdapter);
        centreQATAdapter.notifyDataSetChanged();
        searchQatRequestList = new ArrayList<>();

        final RecyclerView services_list = (RecyclerView) view.findViewById(R.id.services_list);
        final LinearLayoutManager layoutManager_servs = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        services_list.setLayoutManager(layoutManager_servs);
        servicesAdapter = new StringHoriListAdapter(getActivity(), getServiceList(0));
        services_list.setAdapter(servicesAdapter);
        servicesAdapter.setOnItemClickListener(new StringHoriListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                mSelectedServiceId = servicesList.get(position).getName();
                mServiceSelectePosition = position;
                if (mSelectedServiceId.equalsIgnoreCase(mSelectedServTemp)) {
                    //if temp service id is same as selected, ie, same service selected again
                } else {
                    servWiseStaffList.clear();
                    servWiseStaffList.addAll(staffServiceData.get(position).getStaffList());
                    // centreQATAdapter.notifyData(staffServiceData);
                    centreQATAdapter.notifyDataSetChanged();
                    if (servWiseStaffList.size() == 0) {
                        CommonUtility.showAlertOKSpanned(getActivity(), getString(R.string.alert), Html.fromHtml(getString(R.string.no_staff_availablke)));
                    }
                }

                mSelectedServTemp = mSelectedServiceId;
                servicesList.get(position).setSelected(true);
                servicesAdapter.notifyDataSetChanged();

                /*if (searchQatRequestList.size() != 0) {


                    for (int i = 0; i < searchQatRequestList.size(); i++) {
                        if (searchQatRequestList.get(i).getServiceId().contains(servicesList.get(position).getName())) {
                            servicesList.get(position).setSelected(true);

                        } else {
                        }
                    }
                    servicesAdapter.notifyDataSetChanged();
                }*/

            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        centreQATAdapter.setOnItemClickListener(new QATChildAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    //Toast.makeText(getActivity(), "csgcisc", Toast.LENGTH_SHORT).show();
                    mStaffSelected = servWiseStaffList.get(position).getStaffId();
                    servicesList.get(mServiceSelectePosition).setStaffSelected(true);
                    if (mSelectedServiceId != null) {

                        Boolean prebooking = false;
                        ServicesData servicesData = BookingSingleton.getServicesData(mSelectedServiceId);
                        SearchQatRequest searchQatRequest = new SearchQatRequest();
                        searchQatRequest.setLast(servicesData.getLast());
                        searchQatRequest.setServicePriority(Integer.parseInt(servicesData.getPriority()));
                        searchQatRequest.setServiceTime(Integer.parseInt(servicesData.getDuration()));
                        searchQatRequest.setServicePrice(servicesData.getCost());
                        if (PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_IS_PREBOOKING, 0) == 1) {
                            prebooking = true;
                        }

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

                        searchQatRequest.setAdvanceBookingRequest(advance);
                        searchQatRequest.setStaffId(mStaffSelected);
                        List<String> mTempServ = new ArrayList<>();
                        mTempServ.add(mSelectedServiceId);
                        searchQatRequest.setServiceId(mTempServ);


                        int staffPresentPos = 0;
                        boolean isPrsent = false;
                        if (searchQatRequestList.size() != 0) {
                            for (int i = 0; i < searchQatRequestList.size(); i++) {
                                if (searchQatRequestList.get(i).getServiceId().contains(mSelectedServiceId)) {
                                    if (searchQatRequestList.get(i).getServiceId().size() > 1) {
                                        searchQatRequestList.get(i).getServiceId().remove(mSelectedServiceId);
                                        int updatedTime = searchQatRequestList.get(i).getServiceTime() - Integer.parseInt(servicesData.getDuration());
                                        int updatePrice = Integer.parseInt(searchQatRequestList.get(i).getServicePrice()) - Integer.parseInt(servicesData.getCost());
                                        searchQatRequestList.get(i).setServiceTime(updatedTime);
                                        searchQatRequestList.get(i).setServicePrice(String.valueOf(updatePrice));
                                    } else {
                                        searchQatRequestList.remove(i);
                                    }
                                }

                                if (searchQatRequestList.size() != 0) {
                                    if (i != searchQatRequestList.size()) {
                                        if (searchQatRequestList.get(i).getStaffId().equalsIgnoreCase(mStaffSelected)) {
                                            isPrsent = true;
                                            staffPresentPos = i;
                                        }
                                    }
                                }

                            }
                        }


                        //if (!servicesData.getLast()) {
                        if (isPrsent) {
                            //  if (!SelectStaffFragmentSS.searchQatRequestList.get(staffPresentPos).getServiceId().contains(serviceId)) {
                            searchQatRequestList.get(staffPresentPos).getServiceId().add(mSelectedServiceId);
                            int time = searchQatRequestList.get(staffPresentPos).getServiceTime();
                            searchQatRequestList.get(staffPresentPos).setServiceTime(time + Integer.parseInt(servicesData.getDuration()));
                            int price = Integer.parseInt(searchQatRequestList.get(staffPresentPos).getServicePrice());
                            searchQatRequestList.get(staffPresentPos).setServicePrice(String.valueOf(price + Integer.parseInt(servicesData.getCost())));
                            if (servicesData.getLast()) {
                                searchQatRequestList.get(staffPresentPos).setLast(true);
                            }
                            //}
                        } else {
                            searchQatRequestList.add(searchQatRequest);
                        }
                    /*} else {
                        searchQatRequestList.add(searchQatRequest);

                    }*/

                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < servWiseStaffList.size(); i++) {
                            if (i != position) {
                                list.add(servWiseStaffList.get(i).getStaffId());
                            }
                        }

                        // Toast.makeText(getActivity(), "selected", Toast.LENGTH_SHORT).show();
                        if (unSelectedStaffs != null) {
                            for (int j = 0; j < unSelectedStaffs.size(); j++) {
                                List<String> temp = unSelectedStaffs.get(j);
                                if (temp.get(0).equals(servWiseStaffList.get(position).getStaffId())) {
                                    unSelectedStaffs.remove(j);
                                }
                            }
                        }

                        if (list.size() != 0) {
                            unSelectedStaffs.add(list);
                        }
                        servWiseStaffList.get(position).setIsselected(true);
                        if (servWiseStaffList.get(position).getIsselected()) {
                            for (int i = 0; i < servWiseStaffList.size(); i++) {
                                if (i != position) {
                                    servWiseStaffList.get(i).setIsselected(false);
                                }
                            }
                        }


                   /*     for (int i = 0; i < servicesList.size(); i++) {
                            if (servicesList.get(i).getName().contains(servicesList.get(position).getName())) {
                                servicesList.get(position).setStaffSelected(true);
                            } else {

                            }
                        }
                        servicesAdapter.notifyDataSetChanged();*/
                        centreQATAdapter.notifyDataSetChanged();

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        Button confirm_Appointment = view.findViewById(R.id.confirm_booking);
        confirm_Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                try {
                    if (bookingRequestList == null) {
                        bookingRequestList = new ArrayList<>();
                    }

                    int tempServCount = 0;
                    for (int k = 0; k < searchQatRequestList.size(); k++) {
                        for (int m = 0; m < searchQatRequestList.get(k).getServiceId().size(); m++) {
                            tempServCount++;
                        }
                    }


                    if (tempServCount == mServiceIDs.get(currentPosition - 1).size()) {
                        int n = currentPosition;

                        switch (currentPosition) {
                            case 1:
                                List<SearchQatRequest> bookingRequests = new ArrayList<>();
                                bookingRequests.addAll(searchQatRequestList);
                                bookingRequestList.add(bookingRequests);

                                if (n == PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1)) {
                                    if (bookingRequestList.size() != 0) {
                                        confirmBookingPost();
                                    }
                                } else {
                                    //PrefsWrapper.with(getContext()).save(JullayConstants.KEY_Current_BOOKING_Pos, 2);//position update
                                    currentPosition = 2;
                                    staffServiceData.clear();
                                    mServiceSelectePosition = 0;

                                    //heading.setText("Select QAT for 2nd Person.");
                                    servicesList.clear();
                                    getServiceList(currentPosition - 1);
                                    onButtonPressed(currentPosition);
                                    loadStaffSpecific(mServiceIDs.get(1));

                                }
                                searchQatRequestList.clear();
                                break;

                            case 2:
                                List<SearchQatRequest> bookingRequests2 = new ArrayList<>();
                                bookingRequests2.addAll(searchQatRequestList);
                                bookingRequestList.add(bookingRequests2);


                                if (n == PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1)) {
                                    if (bookingRequestList.size() != 0) {
                                        confirmBookingPost();
                                    }
                                } else {
                                    // PrefsWrapper.with(getContext()).save(JullayConstants.KEY_Current_BOOKING_Pos, 3);//position update
                                    currentPosition = 3;
                                    staffServiceData.clear();
                                    mServiceSelectePosition = 0;
                                    //  heading.setText("Select QAT for 3rd Person.");
                                    servicesList.clear();
                                    getServiceList(currentPosition - 1);
                                    onButtonPressed(currentPosition);
                                    loadStaffSpecific(mServiceIDs.get(2));

                                }
                                searchQatRequestList.clear();
                                break;
                            case 3:

                                List<SearchQatRequest> bookingRequests3 = new ArrayList<>();
                                bookingRequests3.addAll(searchQatRequestList);
                                bookingRequestList.add(bookingRequests3);


                                if (n == PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1)) {
                                    if (bookingRequestList.size() != 0) {
                                        confirmBookingPost();
                                    }
                                } else {
                                    // PrefsWrapper.with(getContext()).save(JullayConstants.KEY_Current_BOOKING_Pos, 3);//position update
                                    currentPosition = 4;
                                    staffServiceData.clear();
                                    mServiceSelectePosition = 0;

                                    //  heading.setText("Select QAT for 4th Person.");
                                    servicesList.clear();
                                    getServiceList(currentPosition - 1);
                                    onButtonPressed(currentPosition);
                                    loadStaffSpecific(mServiceIDs.get(3));

                                }
                                searchQatRequestList.clear();
                                break;

                            case 4:
                                List<SearchQatRequest> bookingRequests4 = new ArrayList<>();
                                bookingRequests4.addAll(searchQatRequestList);
                                bookingRequestList.add(bookingRequests4);

                                if (n == PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1)) {
                                    if (bookingRequestList.size() != 0) {
                                        confirmBookingPost();
                                    }
                                }
                                searchQatRequestList.clear();


                                break;
                        }

                    } else {
                        List<String> servNames = new ArrayList<>();
                        for (int i = 0; i < servicesList.size(); i++) {
                            if (!servicesList.get(i).isStaffSelected()) {

                                servNames.add(BookingSingleton.getServiceResponses().get(servicesList.get(i).getName()).getServiceName());
                            }
                        }
                        Spanned text = Html.fromHtml("<b>" + CommonUtility.convertListToCommaString(servNames) + "</b>");
                        CommonUtility.showAlertOKSpanned(getActivity(), "Please select professional/specialist for:", text);

                    }
                /*int n = PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_Current_BOOKING_Pos, 1);

                if (n == PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1)) {
                    if (searchQatRequestList.size() != 0) {
                        confirmBookingPost();
                    }
                } else {
                    if ((n + 1) == 2) {
                        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_Current_BOOKING_Pos, n + 1);//updated QAT selection position

                        //2nd person
                        staffServiceData.clear();
                        loadQATData(CommonUtility.convertListToCommaString(mServiceIDs.get(1)));

                    }
                    if ((n + 1) == 3) {
                        //3rd person
                        if (searchQatRequestList.size() != 0) {
                            confirmBookingPost();
                        }
                    }
                }
*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    private List<SimpleStringItem> getServiceList(int currentBookingPos) {
        servicesList.clear();

        List<String> tempList = mServiceIDs.get(currentBookingPos);
        for (int i = 0; i < tempList.size(); i++) {
            SimpleStringItem item = new SimpleStringItem();
            item.setName(tempList.get(i));
            if (i == 0) {
                item.setSelected(true);

            } else {
                item.setSelected(false);
            }
            servicesList.add(item);
        }

        mSelectedServiceId = servicesList.get(0).getName();
        mSelectedServTemp = mSelectedServiceId;
        if (servicesAdapter != null) {
            servicesAdapter.notifyDataSetChanged();
        }

        return servicesList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSSInteractionListener) {
            mListener = (OnSSInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        searchQatRequestList = null;
        //   bookingRequestMap = null;

       /* bookingRequestMap = null;
        unSelectedStaffs = null;*/

    }

    public void onButtonPressed(int currentPosition) {

        if (mListener != null) {
            mListener.onSSInteraction(currentPosition);
        }
    }


    private void loadStaffSpecific(final List<String> serviceIDs) {
        mInsideProgressBar.setVisibility(View.VISIBLE);

        ServiceList list = new ServiceList();
        Services service = new Services();
        service.setServices(serviceIDs);
        list.setServiceList(service);

        String json = CommonUtility.convertToJson("loadSSstaffs", list);
        CentralApis.getInstance().getBookingAPIS().loadStaffSpecificData(list, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<StaffSpecificResponse>() {
            @Override
            public void onResponse(Call<StaffSpecificResponse> call, Response<StaffSpecificResponse> response) {

                if (response.isSuccessful()) {
                    StaffSpecificResponse staffServiceClass = response.body();
                    mInsideProgressBar.setVisibility(View.GONE);
                    servWiseStaffList.clear();
                    staffServiceData.clear();
                    staffServiceData.addAll(staffServiceClass.getStaffServiceResponses());
                    servWiseStaffList.addAll(staffServiceData.get(0).getStaffList());
                    // centreQATAdapter.notifyData(staffServiceData);
                    centreQATAdapter.notifyDataSetChanged();

                    if (servWiseStaffList.size() == 0) {
                        CommonUtility.showAlertOKSpanned(getActivity(), getString(R.string.alert), Html.fromHtml(getString(R.string.no_staff_availablke)));
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
            public void onFailure(Call<StaffSpecificResponse> call, Throwable t) {
                mInsideProgressBar.setVisibility(View.GONE);
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadStaffSpecific(serviceIDs);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));

                }
            }
        });

    }


    private void confirmBookingPost() {

        if (bookingRequestList != null) {
            if (bookingRequestList.size() != 0) {
                /*if (PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_IS_PREBOOKING, 0) == 1) {
                    // is prebooking is true open prebooking slots page
                    BookSeatActivity.replaceFragmentHistory(new PrebookingSlotsFragment().newInstance(mCentreID, mServiceIDs, null,bookingRequestMap, unSelectedStaffs), (AppCompatActivity) getContext());

                } else {*/
                CommonUtility.showAlertOKSpannedListner(getActivity(), getString(R.string.alert), Html.fromHtml("<font color=\"#E53935\">" + getString(R.string.timer_msg) + "</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        currentBookingPos = 1;
                        currentPosition = 1;
                        onButtonPressed(1);
                        BookSeatActivity.replaceFragmentHistory(new BookingConfirmPage().newInstance(mCentreID, mServiceIDs, bookingRequestList, unSelectedStaffs), (AppCompatActivity) getContext());

                    }
                });

                //}

            } else {
                CommonUtility.showAlertOK(getContext(), getString(R.string.error_staff_slectsn));
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
/*        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_Current_BOOKING_Pos, 1);
        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, 1);*/
        if (animBlink != null) {
            animBlink.cancel();
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
        getActivity().finish();
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSSInteractionListener {
        void onSSInteraction(int currentPosition);
    }
}
