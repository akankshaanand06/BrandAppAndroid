package com.six.hats.brand.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.six.hats.brand.R;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.ui.main.PageViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServicesPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    ServicesData servicesData = new ServicesData();

    private PageViewModel pageViewModel;

    public static ServicesPlaceholderFragment newInstance(ServicesData servicesData, int index) {
        ServicesPlaceholderFragment fragment = new ServicesPlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("servicesData", servicesData);
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
            servicesData = (ServicesData) getArguments().getSerializable("servicesData");
            index = getArguments().getInt("index");
        }
        pageViewModel.setIndex(index);


    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_services, container, false);
        ImageView serv_img = root.findViewById(R.id.serv_img);
        TextView serviceName = root.findViewById(R.id.serviceName);
        TextView service_cost = root.findViewById(R.id.service_cost);
        TextView service_ets = root.findViewById(R.id.service_ets);
        TextView long_des_serv = root.findViewById(R.id.long_des_serv);

        serviceName.setText(servicesData.getServiceName());
        service_cost.setText(servicesData.getCost());
        service_ets.setText(servicesData.getDuration() + " Mins");
        long_des_serv.setText(servicesData.getShortDesp());
        try {
            Picasso.with(getActivity()).load(servicesData.getServImgUrl())
                    .into(serv_img, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) serv_img.getDrawable()).getBitmap();
                         /*   RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);*/
                            serv_img.setImageBitmap(imageBitmap);
                        }

                        @Override
                        public void onError() {
                            serv_img.setImageResource(R.drawable.no_img);
                        }
                    });
        } catch (Exception e) {
            serv_img.setImageResource(R.drawable.no_img);
            e.printStackTrace();
        }


        return root;
    }
}