package com.six.hats.brand.fragments;

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
        ImageView camp_img = root.findViewById(R.id.camp_img);
        TextView serviceName = root.findViewById(R.id.serviceName);
        TextView service_cost = root.findViewById(R.id.service_cost);
        TextView service_ets = root.findViewById(R.id.service_ets);
        TextView long_des_serv = root.findViewById(R.id.long_des_serv);

        serviceName.setText(servicesData.getServiceName());
        service_cost.setText(servicesData.getCost());
        service_ets.setText(servicesData.getDuration());
        //long_des_serv.setText(servicesData.getLongDesc());
       /* pageViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                serviceName.setText(s.getServiceName().toString());
            }
        });*/


        return root;
    }
}