package com.six.hats.brand.ui;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.six.hats.brand.R;
import com.six.hats.brand.booking.DayPhasedapter;
import com.six.hats.brand.booking.QATSelectionActivity;
import com.six.hats.brand.model.BusinessHours;
import com.six.hats.brand.model.Centre;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.booking.PreBookDates;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class DayPhaseSheetDialog extends BottomSheetDialogFragment {

    private DayPhasedapter dayAdapter;
    List<Boolean> selectedValue = new ArrayList<>();
    List<String> dayPhases = new ArrayList<>();
    Date startCaldate = null;
    String mCentreId;
    HashMap<Integer, List<String>> mSelectedItemIDs;
    List<Integer> totalDuration, totalPrice;
    List<PreBookDates> slots_array = new ArrayList<>();
    int typeSelected = 0;
    private TimePicker timePicker;
    private String format;

    public DayPhaseSheetDialog(String mCentreId, HashMap<Integer, List<String>> mSelectedItemIDs, List<Integer> totalDuration, List<Integer> totalPrice) {
        this.mCentreId = mCentreId;
        this.mSelectedItemIDs = mSelectedItemIDs;
        this.totalDuration = totalDuration;
        this.totalPrice = totalPrice;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_day_phase_sheet,
                container, false);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));

        final TextView today = view.findViewById(R.id.today);
        final TextView tomorrow = view.findViewById(R.id.tomorrow);
        final TextView third_day = view.findViewById(R.id.third_day);
        Calendar rightNow = Calendar.getInstance();
        final int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        final Calendar startCal = Calendar.getInstance();
        // Set the adapter
        Context context = view.getContext();
        final RecyclerView phaseList = view.findViewById(R.id.phase_list);
        selectedValue.add(true);
        selectedValue.add(false);
        selectedValue.add(false);
        selectedValue.add(false);

        dayPhases.add("Morning");
        dayPhases.add("Afternoon");
        dayPhases.add("Evening");
        dayPhases.add("Night");


        if (currentHourIn24Format > 12) {
            //hide morning
            dayPhases.remove("Morning");
        }
        if (currentHourIn24Format > 16) {
            //hide morning and afternoon
            dayPhases.remove("Afternoon");
        }
        if (currentHourIn24Format > 19) {
            //hide morning , afternoon and evening
            dayPhases.remove("Evening");
        }
        phaseList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        dayAdapter = new DayPhasedapter(dayPhases, selectedValue, context);
        phaseList.setAdapter(dayAdapter);
        phaseList.addItemDecoration(itemDecorator);
        timePicker = (TimePicker) view.findViewById(R.id.prebooktimePicker);
        timePicker.setVisibility(View.VISIBLE);
        timePicker.setIs24HourView(false);
        Calendar c = Calendar.getInstance();
        timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
        timePicker.setVisibility(View.GONE);


        // endCal.add(Calendar.DATE, 1);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCaldate = startCal.getTime();

        dayAdapter.setOnItemClickListener(new DayPhasedapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                for (int i = 0; i < 4; i++) {
                    if (i == position) {
                        selectedValue.set(i, true);
                    } else {
                        selectedValue.set(i, false);
                    }
                }
                dayAdapter.notifyDataSetChanged();


                switch (dayPhases.get(position)) {

                    case "Morning":
                        //Morning
                        Calendar startCal = Calendar.getInstance();
                        if (currentHourIn24Format >= 0 && currentHourIn24Format < 12) {

                            startCaldate = startCal.getTime();

                        } else if (currentHourIn24Format < 0) {

                            // endCal.add(Calendar.DATE, 1);
                            startCal.set(Calendar.HOUR_OF_DAY, 0);
                            startCal.set(Calendar.MINUTE, 0);
                            startCal.set(Calendar.SECOND, 0);
                            startCaldate = startCal.getTime();

                        }
                        break;
                    case "Afternoon":
                        Calendar startCal2 = Calendar.getInstance();
                        if (currentHourIn24Format >= 12 && currentHourIn24Format < 16) {

                            startCaldate = startCal2.getTime();

                        } else if (currentHourIn24Format < 12) {

                            // endCal.add(Calendar.DATE, 1);
                            startCal2.set(Calendar.HOUR_OF_DAY, 12);
                            startCal2.set(Calendar.MINUTE, 0);
                            startCal2.set(Calendar.SECOND, 0);
                            startCaldate = startCal2.getTime();

                        }

                        break;
                    case "Evening":
                        Calendar startCal3 = Calendar.getInstance();

                        if (currentHourIn24Format >= 16 && currentHourIn24Format < 19) {

                            startCaldate = startCal3.getTime();

                        } else if (currentHourIn24Format < 16) {

                            // endCal.add(Calendar.DATE, 1);
                            startCal3.set(Calendar.HOUR_OF_DAY, 16);
                            startCal3.set(Calendar.MINUTE, 0);
                            startCal3.set(Calendar.SECOND, 0);
                            startCaldate = startCal3.getTime();

                        }
                        break;
                    case "Night":
                        Calendar startCal4 = Calendar.getInstance();

                        if (currentHourIn24Format >= 19 && currentHourIn24Format <= 23) {

                            startCaldate = startCal4.getTime();

                        } else if (currentHourIn24Format < 19) {

                            // endCal.add(Calendar.DATE, 1);
                            startCal4.set(Calendar.HOUR_OF_DAY, 19);
                            startCal4.set(Calendar.MINUTE, 0);
                            startCal4.set(Calendar.SECOND, 0);
                            startCaldate = startCal4.getTime();
                        }/*else if(currentHourIn24Format>23){

                        }*/
                        break;
                }

            }


            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        Button checkqat = view.findViewById(R.id.checkqat);
        int bookingType = PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);
        switch (bookingType) {
            case JullayConstants.TIME_SPECIFIC:
                checkqat.setText(getString(R.string.book_q_a_t_btn));
                break;
            case JullayConstants.STAFF_SPECIFIC:
                checkqat.setText(getString(R.string.next));
                break;
        }


        checkqat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeSelected == 1 || typeSelected == 2) {
                    try {

                        String reportDate = setTime();

                        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_PREBOOKING_DATE, reportDate);
                        PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_IS_PREBOOKING, 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (startCaldate != null) {
                    goToQATPage(startCaldate.getTime());
                }

            }
        });

      /*  final RecyclerView preBookList = (RecyclerView) view.findViewById(R.id.prebook_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        preBookList.setLayoutManager(layoutManager);


        adapter = new PreBookSlotsAdapter(getActivity(), slots_array);
        preBookList.setAdapter(adapter);


        adapter.setOnItemClickListener(new PreBookSlotsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    String format = "yyyy-MM-dd HH:mm a";

                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    String date1 = "";
                    switch (typeSelected) {
                        case 1://tomorrow
                            date1 = CommonUtility.formatDate(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
                            break;
                        case 2://third day
                            date1 = CommonUtility.formatDate(System.currentTimeMillis() + (48 * 60 * 60 * 1000));

                            break;
                    }
                    Date dateObj1 = sdf.parse(date1 + " " + slots_array.get(position).getmValues());
                    for (int i = 0; i < slots_array.size(); i++) {
                        if (i == position) {
                            slots_array.get(i).setSelected(true);
                        } else {
                            slots_array.get(i).setSelected(false);

                        }
                    }

                    adapter.notifyDataSetChanged();
                    String reportDate = sdf.format(dateObj1);

                    PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_PREBOOKING_DATE, reportDate);
                    PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_IS_PREBOOKING, 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });*/
        loadCentreData(mCentreId);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeSelected = 0;
                timePicker.setVisibility(View.GONE);
                phaseList.setVisibility(View.VISIBLE);
                today.setBackgroundResource(R.drawable.rounded_blue);
                tomorrow.setBackgroundResource(R.drawable.shadow_background);
                third_day.setBackgroundResource(R.drawable.shadow_background);


            }
        });

        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeSelected = 1;
                timePicker.setVisibility(View.VISIBLE);
                phaseList.setVisibility(View.GONE);
                today.setBackgroundResource(R.drawable.shadow_background);
                tomorrow.setBackgroundResource(R.drawable.rounded_blue);
                third_day.setBackgroundResource(R.drawable.shadow_background);


            }
        });

        third_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeSelected = 2;
                timePicker.setVisibility(View.VISIBLE);
                phaseList.setVisibility(View.GONE);
                today.setBackgroundResource(R.drawable.shadow_background);
                tomorrow.setBackgroundResource(R.drawable.shadow_background);
                third_day.setBackgroundResource(R.drawable.rounded_blue);
            }
        });
        String format = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        String date1 = "";
        date1 = CommonUtility.formatDate2(System.currentTimeMillis() + (48 * 60 * 60 * 1000));
        Date d = null;
        try {
            d = sdf.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String reportDate = sdf.format(d);
        third_day.setText(reportDate);


        return view;
    }


    public String setTime() {

        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        switch (typeSelected) {
            case 1://tomorrow
                calendar.add(Calendar.DATE, 1);
                break;
            case 2://third day
                calendar.add(Calendar.DATE, 2);
                break;
        }
        Date date = calendar.getTime();
        String format = "yyyy-MM-dd HH:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String reportDate = sdf.format(date);

        return reportDate;
    }


    public void goToQATPage(long time) {
        int bookingType = PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);
        String date = CommonUtility.formatLong(time);
        if (typeSelected == 0) {
            if (System.currentTimeMillis() < time) {
                PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_PREBOOKING_DATE, date);
                PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_IS_PREBOOKING, 1);
            } else {
                PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_PREBOOKING_DATE, date);
                PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_IS_PREBOOKING, 0);
            }
        }

        // getActivity().finish();


        BookingNotesSheetDialog bottomSheet = new BookingNotesSheetDialog(mCentreId, mSelectedItemIDs, totalDuration, totalPrice);
        bottomSheet.show(getActivity().getSupportFragmentManager(),
                "DayPhaseSheetDialog");


      /*  Intent appotmnt_detail = new Intent(getActivity(), QATSelectionActivity.class);

        appotmnt_detail.putExtra("page", bookingType);
        appotmnt_detail.putExtra("mCentreID", mCentreId);
        appotmnt_detail.putExtra("mServiceIDs", (Serializable) mSelectedItemIDs);
        appotmnt_detail.putExtra("totalDuration", (Serializable) totalDuration);
        appotmnt_detail.putExtra("totalPrice", (Serializable) totalPrice);
        appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_in_right);
        startActivity(appotmnt_detail, options.toBundle());*/

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
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
        List<String> list = CommonUtility.splitTime(CommonUtility.formatTime(startCaldate.getTime()), CommonUtility.formatTime(date.getTime()));


        for (int i = 0; i < list.size(); i++) {
            PreBookDates preBookDates = new PreBookDates();


            preBookDates.setmValues(list.get(i));
            preBookDates.setSelected(false);

            slots_array.add(preBookDates);

        }
        //  slots_array.addAll(list);


    }


}
