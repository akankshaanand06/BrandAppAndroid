package com.six.hats.brand.booking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.six.hats.brand.R;
import com.six.hats.brand.model.BusinessHours;
import com.six.hats.brand.model.Centre;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.booking.SearchQatRequest;
import com.six.hats.brand.networks.CentralApis;
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

import retrofit2.Call;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PrebookingSlotsFragment extends Fragment {


    List<String> slots_array = new ArrayList<>();
    private PreBookSlotsAdapter adapter;
    private static final String ARG_CENTRE_ID = "centreID";
    private static final String ARG_SERVICE_ID = "mServiceID";
    private static final String ARG_TOTAL_TIME = "mTotalTime";
    private static final String ARG_TOTAL_PRICE = "mTotalPrice";
    private static final String ARG_UN_Staffs = "unSelectedStaffs";
    private static final String ARG_STAFF_ID_LIST = "staffIDList";
    private String mCentreID;
    private HashMap<Integer, List<String>> mServiceIDs = new HashMap<>();
    private List<Integer> totalDuration = new ArrayList<>();
    private List<Integer> totalPrice = new ArrayList<>();
    public List<List<String>> unSelectedStaffsList = new ArrayList<>();
    private List<List<SearchQatRequest>> searchQatRequestList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PrebookingSlotsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCentreID = getArguments().getString(ARG_CENTRE_ID, "0");
            mServiceIDs = (HashMap<Integer, List<String>>) getArguments().getSerializable(ARG_SERVICE_ID);
            totalDuration = (List<Integer>) getArguments().getSerializable(ARG_TOTAL_TIME);
            totalPrice = (List<Integer>) getArguments().getSerializable(ARG_TOTAL_PRICE);
            searchQatRequestList = (List<List<SearchQatRequest>>) getArguments().getSerializable(ARG_STAFF_ID_LIST);
            unSelectedStaffsList = (List<List<String>>) getArguments().getSerializable(ARG_UN_Staffs);

        }


    }

    public static PrebookingSlotsFragment newInstance(String mCentreId, HashMap<Integer, List<String>> mServiceIdSelected, List<Integer> totalDuration, List<Integer> totalPrice, List<List<SearchQatRequest>> searchQatRequestList, List<List<String>> unSelectedStaffs) {
        PrebookingSlotsFragment fragment = new PrebookingSlotsFragment();
        Bundle args = new Bundle();

        args.putString(ARG_CENTRE_ID, mCentreId);
        args.putSerializable(ARG_SERVICE_ID, mServiceIdSelected);
        args.putSerializable(ARG_TOTAL_TIME, (Serializable) totalDuration);
        args.putSerializable(ARG_TOTAL_PRICE, (Serializable) totalPrice);
        args.putSerializable(ARG_STAFF_ID_LIST, (Serializable) searchQatRequestList);
        args.putSerializable(ARG_UN_Staffs, (Serializable) unSelectedStaffs);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slots_list, container, false);
        loadCentreData(mCentreID);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.prebook_list);
        RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
//        adapter = new PreBookSlotsAdapter(getActivity(), slots_array, mCentreID, mServiceIDs, totalDuration, totalPrice, searchQatRequestList, unSelectedStaffsList);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String item);

    }

    /**
     * Check live booking status
     *
     * @param centreId
     */
    private void loadCentreData(String centreId) {

        CentreSingleton singleton = CentreSingleton.getInstance();

        List<BusinessHours> businessHours = singleton.getBusinessHours();

        Calendar endCal = Calendar.getInstance();
        // endCal.add(Calendar.DATE, 1);
        endCal.set(Calendar.HOUR_OF_DAY, businessHours.get(0).getSlotList().get(0).getEndSpan().getHour());
        endCal.set(Calendar.MINUTE, businessHours.get(0).getSlotList().get(0).getEndSpan().getMinutes());
        endCal.set(Calendar.SECOND, 0);

        Date date = endCal.getTime();


        Calendar startCal = Calendar.getInstance();
        // endCal.add(Calendar.DATE, 1);
        startCal.set(Calendar.HOUR_OF_DAY, businessHours.get(0).getSlotList().get(0).getStartSpan().getHour());
        startCal.set(Calendar.MINUTE, businessHours.get(0).getSlotList().get(0).getStartSpan().getMinutes());
        startCal.set(Calendar.SECOND, 0);

        Date startCaldate = startCal.getTime();


        slots_array.addAll(CommonUtility.splitTime(CommonUtility.formatTime(startCaldate.getTime()), CommonUtility.formatTime(date.getTime())));
        adapter.notifyDataSetChanged();


    }


}
