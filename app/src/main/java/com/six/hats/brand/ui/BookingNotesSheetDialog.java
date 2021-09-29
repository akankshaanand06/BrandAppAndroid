package com.six.hats.brand.ui;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.six.hats.brand.R;
import com.six.hats.brand.booking.QATSelectionActivity;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class BookingNotesSheetDialog extends BottomSheetDialogFragment {


    int personCount = 0;
    private EditText booking_notes;
    String mCentreId;
    HashMap<Integer, List<String>> mSelectedItemIDs;
    List<Integer> totalDuration, totalPrice;


    public BookingNotesSheetDialog(String mCentreId, HashMap<Integer, List<String>> mSelectedItemIDs, List<Integer> totalDuration, List<Integer> totalPrice) {
        this.mCentreId = mCentreId;
        this.mSelectedItemIDs = mSelectedItemIDs;
        this.totalDuration = totalDuration;
        this.totalPrice = totalPrice;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_bottom_sheet,
                container, false);

        booking_notes = view.findViewById(R.id.booking_notes);

        Button done_next = view.findViewById(R.id.done_next);

        done_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openServicesPage();
            }
        });
        Button skip = view.findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openServicesPage();
            }
        });

        return view;
    }

    public void openServicesPage() {

        if (CommonUtility.chkString(booking_notes.getText().toString())) {
            PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_BOOKING_NOTES, booking_notes.getText().toString());
        } else {
            PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_BOOKING_NOTES, "");
        }

        int bookingType = PrefsWrapper.with(getActivity()).getInt(JullayConstants.KEY_BOOKING_TYPE, JullayConstants.TIME_SPECIFIC);

        Intent appotmnt_detail = new Intent(getActivity(), QATSelectionActivity.class);

        appotmnt_detail.putExtra("page", bookingType);
        appotmnt_detail.putExtra("mCentreID", mCentreId);
        appotmnt_detail.putExtra("mServiceIDs", (Serializable) mSelectedItemIDs);
        appotmnt_detail.putExtra("totalDuration", (Serializable) totalDuration);
        appotmnt_detail.putExtra("totalPrice", (Serializable) totalPrice);
        appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_in_right);
        startActivity(appotmnt_detail, options.toBundle());

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);

    }


}
