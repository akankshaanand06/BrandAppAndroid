package com.six.hats.brand.booking;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.six.hats.brand.BaseFragment;
import com.six.hats.brand.R;
import com.six.hats.brand.ui.MultiPersonSheetDialog;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import me.toptas.fancyshowcase.FancyShowCaseView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingOptionsFragment extends BaseFragment {

    private static final String ARG_CENTRE_ID = "mCentreID";
    private static final String ARG_CENTRE_NAME = "mCentreNAME";

    private String mCentreID, mCentreName;

    int personValue = 1;
    private TextView centre_name;
    private ImageView check_ts, check_ss;
    private MultiPersonSheetDialog multpersonDialog;

    public BookingOptionsFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("unused")
    public static BookingOptionsFragment newInstance(String pkCentreId, String pkCentreName) {
        BookingOptionsFragment fragment = new BookingOptionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CENTRE_ID, pkCentreId);
        args.putString(ARG_CENTRE_NAME, pkCentreName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mCentreID = getArguments().getString(ARG_CENTRE_ID, "0");
            mCentreName = getArguments().getString(ARG_CENTRE_NAME, "");
        }
    }


    BroadcastReceiver doneSelectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (CommonUtility.haveNetworkConnection(context)) {
                try {

                    if (multpersonDialog != null) {
                        multpersonDialog.dismiss();
                    }

                    BookSeatActivity.replaceFragmentHistory(new ServiceItemFragmentNew().newInstance(mCentreID), (AppCompatActivity) getContext());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_options, container, false);
        String title_color = PrefsWrapper.with(getActivity()).getString(JullayConstants.DEFAULT_HEADING_COLOR, JullayConstants.DARK_HEADING);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.select_booking_type) + "</font>")));

        //final LinearLayout booking_typ_layout = view.findViewById(R.id.booking_typ_layout);
        //final RelativeLayout booking_person/s_layout = view.findViewById(R.id.booking_person/s_layout);
       /* Button stff_booking = view.findViewById(R.id.stff_booking);
        Button time_booking = view.findViewById(R.id.time_booking);*/
        Button done_selection = view.findViewById(R.id.done_selection);
        getActivity().registerReceiver(doneSelectionReceiver, new IntentFilter("doneSelectionReceiver"));

        //TextView help_text = view.findViewById(R.id.textview);
        final LinearLayout count_one = view.findViewById(R.id.count_one);
        final LinearLayout count_two = view.findViewById(R.id.count_two);
        final LinearLayout count_three = view.findViewById(R.id.count_three);
        final LinearLayout count_four = view.findViewById(R.id.count_four);
       // centre_name = view.findViewById(R.id.centre_name);

        check_ts = view.findViewById(R.id.check_ts);
        check_ss = view.findViewById(R.id.check_ss);
        /*help_text.setText((Html.fromHtml(getString(R.string.booking_opsn_head_msg1) + "<font color=\"#FF6E40\">" + " HELP " + "</font>" + getString(R.string.booking_opsn_head_msg2))));

        help_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
              /*  final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.help_dialog_bkng_otsn);
                dialog.show();*/
            /*}
        });*/


        count_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                personValue = 1;
                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, 1);

                count_one.setBackgroundResource(R.drawable.rounded_selected_underlined);
                count_two.setBackgroundResource(R.drawable.rounded_primary);
                count_three.setBackgroundResource(R.drawable.rounded_primary);
                count_four.setBackgroundResource(R.drawable.rounded_primary);
            }
        });
        count_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personValue = 2;
                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, 2);

                count_one.setBackgroundResource(R.drawable.rounded_primary);
                count_two.setBackgroundResource(R.drawable.rounded_selected_underlined);
                count_three.setBackgroundResource(R.drawable.rounded_primary);
                count_four.setBackgroundResource(R.drawable.rounded_primary);
            }
        });
        count_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personValue = 3;
                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, 3);

                count_one.setBackgroundResource(R.drawable.rounded_primary);
                count_two.setBackgroundResource(R.drawable.rounded_primary);
                count_three.setBackgroundResource(R.drawable.rounded_selected_underlined);
                count_four.setBackgroundResource(R.drawable.rounded_primary);
            }
        });
        count_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personValue = 4;
                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, 4);

                count_one.setBackgroundResource(R.drawable.rounded_primary);
                count_two.setBackgroundResource(R.drawable.rounded_primary);
                count_three.setBackgroundResource(R.drawable.rounded_primary);
                count_four.setBackgroundResource(R.drawable.rounded_selected_underlined);
            }
        });


        // centre_name.setText(mCentreName);

       /* stff_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.STAFF_SPECIFIC);
                booking_typ_layout.setVisibility(View.GONE);
                booking_person/s_layout.setVisibility(View.VISIBLE);


            }
        });
        time_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);
                booking_typ_layout.setVisibility(View.GONE);
                booking_person/s_layout.setVisibility(View.VISIBLE);

            }
        });*/
        // final LinearLayout lay_numpicker = view.findViewById(R.id.lay_numpicker);

        //Default Setting - Booking type Time specific and prebooking is 0 i.e off
        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);
        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_IS_PREBOOKING, 0);
        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, 1);
        final TextView ts_text = view.findViewById(R.id.ts_text);
        final TextView ss_text = view.findViewById(R.id.ss_text_);
        final LinearLayout img_ts = view.findViewById(R.id.type_ts);
        final LinearLayout type_ss = view.findViewById(R.id.type_ss);
        Spanned text = Html.fromHtml("\n\n\n\n" + "Focus on View");

        new FancyShowCaseView.Builder(getActivity())
                .focusOn(ts_text)
                .title(text)
                .titleGravity(Gravity.BOTTOM)
                .customView(R.layout.app_tour_item, null)
                .build()
                .show();

        check_ts.setImageResource(R.drawable.red_tick);
        check_ss.setImageResource(R.drawable.empty_tick);
        img_ts.setBackgroundResource(R.drawable.gradient_vertical_bg);
        type_ss.setBackgroundResource(R.drawable.primary_vertical_bg);
        img_ts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_ts.setImageResource(R.drawable.red_tick);
                check_ss.setImageResource(R.drawable.empty_tick);
                img_ts.setBackgroundResource(R.drawable.gradient_vertical_bg);
                type_ss.setBackgroundResource(R.drawable.primary_vertical_bg);
                ts_text.setTextColor(getResources().getColor(R.color.pure_white));
                ss_text.setTextColor(getResources().getColor(R.color.darkBlue));
                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);
            }
        });

        type_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_ts.setImageResource(R.drawable.empty_tick);
                check_ss.setImageResource(R.drawable.red_tick);
                img_ts.setBackgroundResource(R.drawable.primary_vertical_bg);
                type_ss.setBackgroundResource(R.drawable.gradient_vertical_bg);
                ts_text.setTextColor(getResources().getColor(R.color.darkBlue));
                ss_text.setTextColor(getResources().getColor(R.color.pure_white));

                PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.STAFF_SPECIFIC);
            }
        });


        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_IS_PREBOOKING, 0);

       /* Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != Calendar.FRIDAY || dayOfWeek != Calendar.SATURDAY) {
            radioBookingSchdule.setVisibility(View.GONE);
        }
*/
       /* radioBookingSchdule.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.book_later:
                        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_IS_PREBOOKING, 1);
                        break;
                    case R.id.book_now:
                        //   lay_numpicker.setVisibility(View.VISIBLE);
                        PrefsWrapper.with(getContext()).save(JullayConstants.KEY_IS_PREBOOKING, 0);

                        break;
                }

            }
        });*/



        /* NumberPicker np = view.findViewById(R.id.numberPicker);
         *//* String[] data = new String[]{"Berlin", "Moscow", "Tokyo", "Paris"};
        np.setMinValue(0);
        np.setMaxValue(data.length-1);
        np.setDisplayedValues(data);*//*
        np.setMinValue(1);
        np.setMaxValue(3);

        np.setOnValueChangedListener(onValueChangeListener);*/

        done_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personValue > 1) {
                    multpersonDialog = new MultiPersonSheetDialog(personValue, mCentreID);
                    multpersonDialog.show(getActivity().getSupportFragmentManager(),
                            "MultiPersonSheetDialog");
                } else {
                    PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_NAMES_LIST, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_NAME, ""));
                    BookSeatActivity.replaceFragmentHistory(new ServiceItemFragmentNew().newInstance(mCentreID), (AppCompatActivity) getContext());

                }
            }
        });

        return view;
    }


    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    PrefsWrapper.with(getContext()).save(JullayConstants.KEY_BOOKING_COUNT, numberPicker.getValue());
                }
            };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.help:
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.help_dialog_bkng_otsn);
                dialog.show();
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (doneSelectionReceiver != null) {
                getActivity().unregisterReceiver(doneSelectionReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

}
