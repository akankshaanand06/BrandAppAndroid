package com.six.hats.brand.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.six.hats.brand.R;
import com.six.hats.brand.model.StaffDetails;
import com.six.hats.brand.ui.main.PageViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class StaffPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    StaffDetails staffDetails = new StaffDetails();

    private PageViewModel pageViewModel;

    public static StaffPlaceholderFragment newInstance(StaffDetails staffDetails, int index) {
        StaffPlaceholderFragment fragment = new StaffPlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("staffDetails", staffDetails);
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 0;
        if (getArguments() != null) {
            staffDetails = (StaffDetails) getArguments().getSerializable("staffDetails");
            index = getArguments().getInt("index");
        }
        pageViewModel.setIndex(index);


    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staffs, container, false);
        ImageView staff_img = root.findViewById(R.id.staff_img);
        TextView staffName = root.findViewById(R.id.staffName);
        TextView appts_count = root.findViewById(R.id.appts_count);
        TextView service_list = root.findViewById(R.id.service_list);
        TextView long_des_serv = root.findViewById(R.id.long_des_serv);
        RatingBar staff_rating = root.findViewById(R.id.staff_rating);

        staffName.setText(staffDetails.getName());
        appts_count.setText("Count");
        service_list.setText("List");
        long_des_serv.setText("staffDetails.getLongDesc()");
      /*  pageViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                staffName.setText(s.getServiceName().toString());
            }
        });*/

        try {
            Picasso.with(getActivity()).load(staffDetails.getPhoto())
                    .resize(60, 60)
                    .into(staff_img, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) staff_img.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                            staff_img.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError() {
                            staff_img.setImageResource(R.drawable.dummy_user);
                        }
                    });
        } catch (Exception e) {
            staff_img.setImageResource(R.drawable.dummy_user);
            e.printStackTrace();
        }


        return root;
    }
}