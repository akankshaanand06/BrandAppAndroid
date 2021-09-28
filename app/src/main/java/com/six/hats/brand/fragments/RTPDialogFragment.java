package com.six.hats.brand.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.DialogFragment;

import com.six.hats.brand.R;
import com.six.hats.brand.model.booking.Appointment;
import com.six.hats.brand.model.booking.BookingLstDetails;
import com.six.hats.brand.model.booking.LiveBookingResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RTPDialogFragment extends DialogFragment {
    TextView people_ahead, delay, checkin;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert Dialog");
        builder.setMessage("Alert Dialog inside DialogFragment");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rtp_dialog, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        people_ahead = view.findViewById(R.id.people_ahead);
        delay = view.findViewById(R.id.delay);
        checkin = view.findViewById(R.id.checkin);

        if (getArguments() != null) {
            LiveBookingResponse data = (LiveBookingResponse) getArguments().getSerializable("data");
            int bookedHr = data.getCurrentSlot().getEndSpan().getHour();
            int bookedMin = data.getCurrentSlot().getEndSpan().getMinutes();

            int mCt;
            if (bookedMin < 55) {
                mCt = bookedMin + 5;
            } else {
                mCt = bookedMin;
            }

            String mCheckinTime = data.getCurrentSlot().getStartSpan().getHour() + ":" + mCt;

            people_ahead.setText(data.getPeopleAheadCount() + " People Ahead");
            delay.setText(data.getDelayOnIt() + "Mins" + " Delay");
            checkin.setText("Updated Check-in Time: " + mCheckinTime);
        }


        ImageView rtpimg = view.findViewById(R.id.rtpimg);

        ImageView btnDone = view.findViewById(R.id.close);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogListener dialogListener = (DialogListener) getActivity();
                dialogListener.onFinishEditDialog("");
                dismiss();
            }
        });

        try {
            Picasso.with(getActivity()).load(R.drawable.rtp)
                    .resize(500, 500)
                    .into(rtpimg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) rtpimg.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                            rtpimg.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError() {
                            rtpimg.setImageResource(R.drawable.rtp);
                        }
                    });
        } catch (Exception e) {
            rtpimg.setImageResource(R.drawable.rtp);
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }


}
