package com.six.hats.brand.booking;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.R;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ServiceItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "CentreId";
    private int mColumnCount = 1;
    List<ServicesData> serviceResponses = new ArrayList<>();
    private final List<ServicesData> servicesData = new ArrayList<>();

    private String mSelectedStaffID;
    private String mCentreId;
    private MyServiceItemAdapter servicesAdapter;
    List<String> servIDs = new ArrayList<>();
    int count = 1, position = 1;
    Animation animBlink;
    HashMap<Integer, List<String>> mSelectedItemIDs = new HashMap<>();
    List<Integer> totalDuration = new ArrayList<>();
    List<Integer> totalPrice = new ArrayList<>();
    int duration = 0;

    int price = 0;
    private OnInteractionListener mListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServiceItemFragment() {
    }

    @SuppressWarnings("unused")
    public static ServiceItemFragment newInstance(String centreId, List<ServicesData> serviceResponses) {
        ServiceItemFragment fragment = new ServiceItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLUMN_COUNT, centreId);
        args.putSerializable("services", (Serializable) serviceResponses);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCentreId = getArguments().getString(ARG_COLUMN_COUNT, "0");
            serviceResponses = (List<ServicesData>) getArguments().getSerializable("services");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serviceitem_list, container, false);
        String title_color = PrefsWrapper.with(getActivity()).getString(JullayConstants.DEFAULT_HEADING_COLOR, JullayConstants.DARK_HEADING);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.select_serv) + "</font>")));
        count = PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1);
        //  final TextView heading = view.findViewById(R.id.heading_services);
        // load the animation
        animBlink = AnimationUtils.loadAnimation(getContext(),
                R.anim.blink);
        // heading.startAnimation(animBlink);


        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        // Set the adapter
        Context context = view.getContext();
        RecyclerView serviceList = view.findViewById(R.id.services_list);

        serviceList.setLayoutManager(new LinearLayoutManager(context));
        servicesAdapter = new MyServiceItemAdapter(serviceResponses, context);
        serviceList.setAdapter(servicesAdapter);
        serviceList.addItemDecoration(itemDecorator);
        servicesAdapter.setOnItemClickListener(new MyServiceItemAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (servIDs.contains(serviceResponses.get(position).getId())) {
                    servIDs.remove(serviceResponses.get(position).getId());
                    duration = duration - Integer.parseInt(serviceResponses.get(position).getDuration());
                    price = price - Integer.parseInt(serviceResponses.get(position).getCost());

                    serviceResponses.get(position).setSelected(false);
                } else {
                    servIDs.add(serviceResponses.get(position).getId());
                    duration = duration + Integer.parseInt(serviceResponses.get(position).getDuration());
                    price = price + Integer.parseInt(serviceResponses.get(position).getCost());

                    serviceResponses.get(position).setSelected(true);
                }
                servicesAdapter.notifyDataSetChanged();
                mListener.onInteraction(servIDs,duration,price);

            }

            @Override
            public void onItemLongClick(int position, View v) {
//                Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });




       /* final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(R.string.app_name);
        dialog.setContentView(R.layout.person_selector_popup);

        TextView ok_button = dialog.findViewById(R.id.ok_button);
        final LinearLayout lay_numpicker = dialog.findViewById(R.id.lay_numpicker);
        RadioGroup radioPersonGroup = (RadioGroup) dialog.findViewById(R.id.radioPersonCount);
        radioPersonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.single_booking:
                        lay_numpicker.setVisibility(View.GONE);
                        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, 1);
                        break;
                    case R.id.multi_booking:
                        lay_numpicker.setVisibility(View.VISIBLE);

                        break;
                }

            }
        });

        NumberPicker np = dialog.findViewById(R.id.numberPicker);
       *//* String[] data = new String[]{"Berlin", "Moscow", "Tokyo", "Paris"};
        np.setMinValue(0);
        np.setMaxValue(data.length-1);
        np.setDisplayedValues(data);*//*
        np.setMinValue(1);
        np.setMaxValue(3);

        np.setOnValueChangedListener(onValueChangeListener);

        dialog.show();
        ok_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });*/
        return view;
    }







    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    //PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, numberPicker.getValue());
                }
            };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInteractionListener) {
            mListener = (OnInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnInteractionListener {


        void onInteraction(List<String> servIDs, int duration, int price);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (animBlink != null) {
            animBlink.cancel();
        }

    }


}
