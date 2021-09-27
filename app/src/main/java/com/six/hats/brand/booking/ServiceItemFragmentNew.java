package com.six.hats.brand.booking;


import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.BaseFragment;
import com.six.hats.brand.R;
import com.six.hats.brand.model.MyServiceResponse;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.model.booking.ServiceCategoryItem;
import com.six.hats.brand.model.booking.ServicesItems;
import com.six.hats.brand.model.booking.SimpleStringItem;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.ui.DayPhaseSheetDialog;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * j
 * A fragment representing a list of Items.
 * <p/>
 */
public class ServiceItemFragmentNew extends BaseFragment {

    private static final String ARG_COLUMN_COUNT = "CentreId";
    private int mColumnCount = 1;
    List<ServicesData> serviceResponses = new ArrayList<>();
    private final List<ServicesData> servicesData = new ArrayList<>();

    private String mSelectedStaffID;
    private String mCentreId;
    private MyServiceItemAdapter servicesAdapter;
    HashMap<Integer, List<String>> mSelectedItemIDs = new HashMap<>();
    List<String> servIDs = new ArrayList<>();
    int count = 1, pagePosition = 1;
    Animation animBlink;
    private ProgressBar mProgress;
    int duration = 0;
    List<Integer> totalDuration = new ArrayList<>();
    List<Integer> totalPrice = new ArrayList<>();
    int price = 0;
    private HorizontalScrollView person_lay;
    private MainServicesListAdapter stringHoriAdapter;
    HashMap<String, HashMap<String, String>> cndServices = new HashMap<>();
    List<String> mainServiceNames = new ArrayList<>();
    private List<SimpleStringItem> mainServicesList = new ArrayList<>();
    private String mSelectedMainService;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServiceItemFragmentNew() {
    }

    @SuppressWarnings("unused")
    public static ServiceItemFragmentNew newInstance(String centreId) {
        ServiceItemFragmentNew fragment = new ServiceItemFragmentNew();
        Bundle args = new Bundle();
        args.putString(ARG_COLUMN_COUNT, centreId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCentreId = getArguments().getString(ARG_COLUMN_COUNT, "");
        }
        loadJullayMainServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serviceitem_list_new, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(/*(Html.fromHtml(title_color +*/ getString(R.string.select_serv) /*+ "</font>"))*/);
        count = PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1);
        person_lay = (HorizontalScrollView) view.findViewById(R.id.person_lay);

        final TextView idpersonName1 = view.findViewById(R.id.idpersonName1);
        final TextView idpersonName2 = view.findViewById(R.id.idpersonName2);
        final TextView idpersonName3 = view.findViewById(R.id.idpersonName3);
        final TextView idpersonName4 = view.findViewById(R.id.idpersonName4);

        // load the animation
        animBlink = AnimationUtils.loadAnimation(getContext(),
                R.anim.blink);
        // heading.startAnimation(animBlink);
        mProgress = view.findViewById(R.id.insidePB);
        final List<String> names = CommonUtility.getListFromString(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAMES_LIST, ""));


        // set animation listener
        switch (count) {

            case 1:
                if (names.size() == 1) {
                    idpersonName1.setVisibility(View.VISIBLE);
                    idpersonName1.setText(names.get(0));
                    idpersonName2.setVisibility(View.GONE);
                    idpersonName3.setVisibility(View.GONE);
                    idpersonName4.setVisibility(View.GONE);
                }

                break;
            case 2:
                if (names.size() == 2) {

                    idpersonName1.setVisibility(View.VISIBLE);
                    idpersonName1.setText(names.get(0));
                    idpersonName2.setVisibility(View.VISIBLE);
                    idpersonName2.setText(names.get(1));
                    idpersonName3.setVisibility(View.GONE);
                    idpersonName4.setVisibility(View.GONE);
                }
                break;
            case 3:
                if (names.size() == 3) {

                    idpersonName1.setVisibility(View.VISIBLE);
                    idpersonName1.setText(names.get(0));
                    idpersonName2.setVisibility(View.VISIBLE);
                    idpersonName2.setText(names.get(1));
                    idpersonName3.setVisibility(View.VISIBLE);
                    idpersonName3.setText(names.get(2));
                    idpersonName4.setVisibility(View.GONE);
                }
                break;
            case 4:
                if (names.size() == 4) {

                    idpersonName1.setVisibility(View.VISIBLE);
                    idpersonName1.setText(names.get(0));
                    idpersonName2.setVisibility(View.VISIBLE);
                    idpersonName2.setText(names.get(1));
                    idpersonName3.setVisibility(View.VISIBLE);
                    idpersonName3.setText(names.get(2));
                    idpersonName4.setVisibility(View.VISIBLE);
                    idpersonName4.setText(names.get(3));

                }
                break;
        }

        //main services list
        final RecyclerView main_services_list = (RecyclerView) view.findViewById(R.id.main_services_list);
        final LinearLayoutManager layoutManager_servs = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        main_services_list.setLayoutManager(layoutManager_servs);
        stringHoriAdapter = new MainServicesListAdapter(getActivity(), mainServicesList);
        main_services_list.setAdapter(stringHoriAdapter);

        stringHoriAdapter.setOnItemClickListener(new MainServicesListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                servIDs.size();
                mSelectedMainService = mainServicesList.get(position).getName();
                for (int i = 0; i < mainServiceNames.size(); i++) {
                    SimpleStringItem item = new SimpleStringItem();
                    item.setName(mainServiceNames.get(i));
                    if (i == position) {
                        item.setSelected(true);
                    } else {
                        item.setSelected(false);
                    }
                    mainServicesList.set(i, item);
                }
                stringHoriAdapter.notifyDataSetChanged();
                loadServiceDetails(mSelectedMainService);

            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        // Set the adapter
        Context context = view.getContext();
        RecyclerView serviceList = view.findViewById(R.id.services_list);

        serviceList.setLayoutManager(new LinearLayoutManager(context));
        servicesAdapter = new MyServiceItemAdapter(serviceResponses, context);
        serviceList.setAdapter(servicesAdapter);
        servicesAdapter.setOnItemClickListener(new MyServiceItemAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (servIDs.contains(serviceResponses.get(position).getId())) {

                    if (mSelectedItemIDs.size() != 0) {
                        if (mSelectedItemIDs.size() == pagePosition) {
                            mSelectedItemIDs.remove(pagePosition - 1);
                        }
                    }
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
                //v.setBackgroundColor(getResources().getColor(R.color.mustard));

            }

            @Override
            public void onItemLongClick(int position, View v) {
//                Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });


        // durationList.setAdapter(new ServiceDurationAdapter(serviceDurations, context));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        serviceList.addItemDecoration(itemDecorator);

        Button confirm_service = view.findViewById(R.id.confirm_service);
        confirm_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_COUNT, 1);
                if (mSelectedItemIDs.size() != count) {
                    switch (count) {
                        case 1:

                            if (servIDs.size() != 0) {
                                autoSmoothScrollRight();
                                List<String> servIDsCopy = new ArrayList<>();
                                for (int j = 0; j < servIDs.size(); j++) {
                                    servIDsCopy.add(servIDs.get(j));
                                }
                                mSelectedItemIDs.put(0, servIDsCopy);
                                // CommonUtility.saveMapToPref(getContext(),mSelectedItemIDs);
                                totalDuration.add(duration);
                                totalPrice.add(price);

                                openDayBoottomSheet();
                            } else {
                                CommonUtility.showAlertOK(getActivity(), "Please select at least one service.");
                            }
                            break;

                        case 2:

                            if (mSelectedItemIDs.size() == 0) {
                                //means first position
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    List<String> servIDsCopy1 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy1.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(0, servIDsCopy1);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);
                                    pagePosition++;
                                    serviceResponses.clear();
                                    servicesAdapter.notifyDataSetChanged();

                                    for (int i = 0; i < servicesData.size(); i++) {
                                        servicesData.get(i).setSelected(false);
                                    }
                                    serviceResponses.addAll(servicesData);
                                    servicesAdapter.notifyDataSetChanged();
                                    servIDs.clear();
                                    idpersonName1.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName2.setBackgroundResource(R.drawable.rounded_button_yellow);
                                    idpersonName2.setPadding(0, 10, 0, 10);
                                    duration = 0;
                                    price = 0;
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(0) + "</b>"));
                                }
                            } else if (mSelectedItemIDs.size() == 1) {
                                if (servIDs.size() != 0) {

                                    autoSmoothScrollRight();
                                    //seconds position
                                    List<String> servIDsCopy2 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy2.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(1, servIDsCopy2);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);
                                    if (pagePosition == 2) {
                                        // CommonUtility.saveMapToPref(getContext(),mSelectedItemIDs);
                                        openDayBoottomSheet();
                                    }
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(1) + "</b>"));
                                }
                            }

                            break;
                        case 3:

                            if (mSelectedItemIDs.size() == 0) {
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    //means first position
                                    List<String> servIDsCopy4 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy4.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(0, servIDsCopy4);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);
                                    pagePosition++;
                                    serviceResponses.clear();
                                    servicesAdapter.notifyDataSetChanged();
                                    for (int i = 0; i < servicesData.size(); i++) {
                                        servicesData.get(i).setSelected(false);
                                    }
                                    serviceResponses.addAll(servicesData);
                                    servicesAdapter.notifyDataSetChanged();
                                    servIDs.clear();
                                    idpersonName1.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName2.setBackgroundResource(R.drawable.rounded_button_yellow);
                                    idpersonName2.setPadding(0, 10, 0, 10);

                                    duration = 0;
                                    price = 0;
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(0) + "</b>"));
                                }
                            } else if (mSelectedItemIDs.size() == 1) {
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    //seconds position
                                    List<String> servIDsCopy5 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy5.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(1, servIDsCopy5);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);
                                    pagePosition++;
                                    serviceResponses.clear();
                                    servicesAdapter.notifyDataSetChanged();
                                    for (int i = 0; i < servicesData.size(); i++) {
                                        servicesData.get(i).setSelected(false);
                                    }
                                    serviceResponses.addAll(servicesData);
                                    servicesAdapter.notifyDataSetChanged();
                                    servIDs.clear();
                                    idpersonName1.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName2.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName3.setBackgroundResource(R.drawable.rounded_button_yellow);
                                    idpersonName3.setPadding(0, 10, 0, 10);
                                    duration = 0;
                                    price = 0;
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(1) + "</b>"));


                                }
                            } else {
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    List<String> servIDsCopy6 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy6.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(2, servIDsCopy6);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);

                                    if (pagePosition == 3) {
                                        //CommonUtility.saveMapToPref(getContext(),mSelectedItemIDs);
                                        openDayBoottomSheet();
                                    }
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(2) + "</b>"));

                                }
                            }

                            break;


                        case 4:

                            if (mSelectedItemIDs.size() == 0) {
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    //means first position
                                    List<String> servIDsCopy4 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy4.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(0, servIDsCopy4);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);
                                    pagePosition++;
                                    serviceResponses.clear();
                                    servicesAdapter.notifyDataSetChanged();
                                    for (int i = 0; i < servicesData.size(); i++) {
                                        servicesData.get(i).setSelected(false);
                                    }
                                    serviceResponses.addAll(servicesData);
                                    servicesAdapter.notifyDataSetChanged();
                                    servIDs.clear();
                                    idpersonName1.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName2.setBackgroundResource(R.drawable.rounded_button_yellow);
                                    idpersonName2.setPadding(0, 10, 0, 10);

                                    duration = 0;
                                    price = 0;
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(0) + "</b>"));


                                }
                            } else if (mSelectedItemIDs.size() == 1) {
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    //seconds position
                                    List<String> servIDsCopy5 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy5.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(1, servIDsCopy5);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);
                                    pagePosition++;
                                    serviceResponses.clear();
                                    servicesAdapter.notifyDataSetChanged();
                                    for (int i = 0; i < servicesData.size(); i++) {
                                        servicesData.get(i).setSelected(false);
                                    }
                                    serviceResponses.addAll(servicesData);
                                    servicesAdapter.notifyDataSetChanged();
                                    servIDs.clear();
                                    idpersonName1.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName2.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName3.setBackgroundResource(R.drawable.rounded_button_yellow);
                                    idpersonName3.setPadding(0, 10, 0, 10);
                                    duration = 0;
                                    price = 0;
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(1) + "</b>"));

                                }
                            } else if (mSelectedItemIDs.size() == 2) {
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    //seconds position
                                    List<String> servIDsCopy5 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy5.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(2, servIDsCopy5);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);
                                    pagePosition++;
                                    serviceResponses.clear();
                                    servicesAdapter.notifyDataSetChanged();
                                    for (int i = 0; i < servicesData.size(); i++) {
                                        servicesData.get(i).setSelected(false);
                                    }
                                    serviceResponses.addAll(servicesData);
                                    servicesAdapter.notifyDataSetChanged();
                                    servIDs.clear();
                                    idpersonName1.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName2.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName3.setBackgroundResource(R.drawable.primary_rounded_button);
                                    idpersonName4.setBackgroundResource(R.drawable.rounded_button_yellow);
                                    idpersonName4.setPadding(0, 10, 0, 10);

                                    duration = 0;
                                    price = 0;
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(2) + "</b>"));


                                }
                            } else {
                                if (servIDs.size() != 0) {
                                    autoSmoothScrollRight();
                                    List<String> servIDsCopy6 = new ArrayList<>();
                                    for (int j = 0; j < servIDs.size(); j++) {
                                        servIDsCopy6.add(servIDs.get(j));
                                    }
                                    mSelectedItemIDs.put(3, servIDsCopy6);
                                    totalDuration.add(duration);
                                    totalPrice.add(price);

                                    if (pagePosition == 4) {
                                        //CommonUtility.saveMapToPref(getContext(),mSelectedItemIDs);
                                        openDayBoottomSheet();
                                    }
                                } else {
                                    CommonUtility.showAlertOKSpanned(getActivity(), "JULLAY", Html.fromHtml("Please select at least one service for " + "<b>" + names.get(3) + "</b>"));


                                }
                            }

                            break;
                    }
                } else {
                    openDayBoottomSheet();
                }

               /* List<String> selectedIDs = new ArrayList<>();
                for (int i = 0; i < serviceResponses.size(); i++) {
                    if (serviceResponses.get(i).getSelected()) {
                        selectedIDs.add(serviceResponses.get(i).getId());
                    }
                }*/

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

    public void autoSmoothScrollRight() {

        person_lay.postDelayed(new Runnable() {
            @Override
            public void run() {
                person_lay.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                person_lay.smoothScrollBy(500, 0);
            }
        }, 100);
    }

    public void autoSmoothScrollLeft() {

        person_lay.postDelayed(new Runnable() {
            @Override
            public void run() {
                person_lay.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                person_lay.smoothScrollBy(500, 0);
            }
        }, 100);
    }


    private void openDayBoottomSheet() {
        if (mSelectedItemIDs.size() != 0) {
            DayPhaseSheetDialog bottomSheet = new DayPhaseSheetDialog(mCentreId, mSelectedItemIDs, totalDuration, totalPrice);
            bottomSheet.show(getActivity().getSupportFragmentManager(),
                    "DayPhaseSheetDialog");
        } else {
            CommonUtility.showAlertOK(getActivity(), "Please select the services.");
        }
    }

/*
    public void openStaffSelection() {



        int bookingType = PrefsWrapper.with(getContext()).getInt(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);

        if (mSelectedItemIDs.size() != 0) {
            switch (bookingType) {
                case JullayConstants.STAFF_SPECIFIC:

                    BookSeatActivity.replaceFragmentHistory(new SelectStaffFragmentSS().newInstance(mCentreId, mSelectedItemIDs), (AppCompatActivity) getContext());
                    break;
                case JullayConstants.TIME_SPECIFIC:

                    BookSeatActivity.replaceFragmentHistory(new TimeSpecificSelection().newInstance(mCentreId, mSelectedItemIDs, totalDuration, totalPrice), (AppCompatActivity) getContext());

                    break;
            }
        } else {
            CommonUtility.showErrorAlert(getContext(), "Some error while selecting services");
        }

    }
*/

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
        if (context instanceof OnListFragmentInteractionListener) {
            //mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
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

        void onListFragmentInteraction(ServicesItems item);
    }


    public void loadServiceDetails(String mainService) {
        // pass centre id here in my service and  pass centre id in staff details
        mProgress.setVisibility(View.VISIBLE);
        String centerId = mCentreId;
        CentralApis.getInstance().getBookingAPIS().loadMyService(centerId, mainService, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<MyServiceResponse>() {
            @Override
            public void onResponse(Call<MyServiceResponse> call, Response<MyServiceResponse> response) {
                if (response.isSuccessful()) {
                    mProgress.setVisibility(View.GONE);
                    serviceResponses.clear();
                    servicesData.clear();
                    if (response.body().getData().size() == 0 || response.body().getData().isEmpty()) {
                        Toast.makeText(getContext(), "There is no data..", Toast.LENGTH_LONG).show();
                    } else {
                        serviceResponses.addAll(response.body().getData());
                        servicesData.addAll(response.body().getData());
                        servicesAdapter.notifyDataSetChanged();
                        BookingSingleton.getInstance().setServiceResponses(serviceResponses);

                        if (servIDs.size() != 0) {
                            for (int i = 0; i < serviceResponses.size(); i++) {
                                if (servIDs.contains(serviceResponses.get(i).getId())) {
                                    //if this exists -- then mar it as selected
                                    serviceResponses.get(i).setSelected(true);

                                }
                            }

                        }
                    }
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
            public void onFailure(Call<MyServiceResponse> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));
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


    private void loadJullayMainServices() {

        CentralApis.getInstance().getAPIS().loadSpinnerServices(JullayConstants.SERVICES_MAIN_CATEGORY, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_BIZ_SUB_CATEGORY, "")).enqueue(new retrofit2.Callback<ServiceCategoryItem>() {
            @Override
            public void onResponse(Call<ServiceCategoryItem> call, Response<ServiceCategoryItem> response) {

                if (response.isSuccessful()) {
                    ServiceCategoryItem serviceCategoryItem = response.body();
                    List<ServiceCategoryItem.ServicesStaticData> list = serviceCategoryItem.getBusinessStaticData();

                    mainServiceNames.clear();
                    mainServicesList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        SimpleStringItem item = new SimpleStringItem();
                        item.setName(list.get(i).getServiceName());
                        mainServiceNames.add(list.get(i).getServiceName());
                        if (i == 0) {
                            item.setSelected(true);
                        } else {
                            item.setSelected(false);
                        }
                        mainServicesList.add(item);
                    }
                    loadServiceDetails(mainServicesList.get(0).getName());

                    stringHoriAdapter.notifyDataSetChanged();

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtility.showErrorAlert(getContext(), jObjError.getString("message"));
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getContext(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceCategoryItem> call, Throwable t) {
                Log.e(TAG, "Response: Failure");
                CommonUtility.showErrorAlert(getContext(), getString(R.string.network_error_text));
            }
        });


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
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    /*// Container Activity or Fragment must implement this interface
    public interface OnServicesSelectionSetListener
    {
        public void onPlayerSelectionSet(List<Player> players_ist);
    }*/
}

