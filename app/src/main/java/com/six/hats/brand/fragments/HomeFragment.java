package com.six.hats.brand.fragments;

import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.six.hats.brand.R;
import com.six.hats.brand.adapters.HomeAdapter;
import com.six.hats.brand.model.Centre;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.MyServiceResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match


    List<HomeItem> homeItemList = new ArrayList<>();
    private HomeAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        //  args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.home_options);
        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new HomeAdapter(homeItemList, getActivity());
        recyclerView.setAdapter(adapter);

        populateHome();

        if (CentreSingleton.getInstance() == null)
            loadBranchData(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_BranchQR, "SvBtE6RpC1"));
        else
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(CentreSingleton.getInstance().getBusinessName());


        return view;
    }

    public List<HomeItem> populateHome() {

        homeItemList.clear();
        HomeItem item1 = new HomeItem("Professionals", R.drawable.ic_back_white, "Use multiple WhatsApp accounts on a\nsingle device.");
        HomeItem item2 = new HomeItem("Services", R.drawable.ic_back_white, "");
        HomeItem item3 = new HomeItem("Specialities", R.drawable.ic_back_white, "Read your friends messages and chat anonymously.");
        HomeItem item4 = new HomeItem("Personalised Offers", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item5 = new HomeItem("Personal Notes", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item6 = new HomeItem("Membership Plans", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item7 = new HomeItem("Facilities/Amenities ", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item8 = new HomeItem("Order Online", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item9 = new HomeItem("Gallery", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itema = new HomeItem("Ratings", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemb = new HomeItem("Reviews", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemc = new HomeItem("Feedbacks", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemd = new HomeItem("My Timeline", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem iteme = new HomeItem("About Us", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemf = new HomeItem("Get Social", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemg = new HomeItem("Book Now", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemh = new HomeItem("Live Tracking of Queue", R.drawable.ic_back_white, "Send WhatsApp messages to anyone without saving their contact number.");


        homeItemList.add(item1);
        homeItemList.add(item2);
        homeItemList.add(item3);
        homeItemList.add(item4);
        homeItemList.add(item5);
        homeItemList.add(item6);
        homeItemList.add(item7);
        homeItemList.add(item8);
        homeItemList.add(item9);
        homeItemList.add(itema);
        homeItemList.add(itemb);
        homeItemList.add(itemc);
        homeItemList.add(itemd);
        homeItemList.add(iteme);
        homeItemList.add(itemf);
        homeItemList.add(itemg);
        homeItemList.add(itemh);
        adapter.notifyData(homeItemList);
        return homeItemList;
    }

    @Keep
    public class HomeItem {

        public String home_name;
        public int home_icon;
        public String desc;

        public HomeItem() {

        }

        public HomeItem(String home_name, int icon, String desc) {

            this.home_icon = icon;
            this.home_name = home_name;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getHome_name() {
            return home_name;
        }

        public void setHome_name(String home_name) {
            this.home_name = home_name;
        }

        public int getHome_icon() {
            return home_icon;
        }

        public void setHome_icon(int home_icon) {
            this.home_icon = home_icon;
        }
    }


    public void loadBranchData(String QR) {

        CentralApis.getInstance().getAPIS().loadBranchDetailsByQr(QR, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, ""))
                .enqueue(new retrofit2.Callback<Centre>() {
                    @Override
                    public void onResponse(Call<Centre> call, Response<Centre> response) {
                        if (response.isSuccessful()) {

                            Centre centre = response.body();

                            CentreSingleton singleton = CentreSingleton.getInstance();


                            singleton.setBranchId(centre.getBranchId());
                            singleton.setAddress(centre.getAddress());
                            singleton.setAdminId(centre.getAdminId());
                            singleton.setBranchQrId(centre.getWalkinQRID());
                            singleton.setBranchRating(centre.getBranchRating());
                            singleton.setBusinessHours(centre.getBusinessHours());
                            singleton.setBusinessId(centre.getBusinessId());
                            singleton.setBusinessName(centre.getBusinessName());
                            singleton.setCategory(centre.getCategory());
                            singleton.setCompleted(centre.isCompleted());
                            singleton.setCordinates(centre.getCordinates());
                            singleton.setExperience(centre.getExperience());
                            singleton.setFav(centre.getFav());
                            singleton.setHomeService(centre.isHomeService());
                            singleton.setLocation(centre.getLocation());
                            singleton.setManagerId(centre.getManagerId());
                            singleton.setMaxAppointment(centre.getMaxAppointment());
                            singleton.setNoOfUploadedImages(centre.getNoOfUploadedImages());
                            singleton.setNumberOfStaff(centre.getNumberOfStaff());
                            singleton.setPinCode(centre.getPinCode());
                            singleton.setQrPath(centre.getQrPath());
                            singleton.setStaffId(centre.getStaffId());
                            singleton.setSubCategory(centre.getSubCategory());

                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(centre.getBusinessName());


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("message").contains("Unauthorized")) {
                                    CommonUtility.autoLogin(getActivity());
                                } else {
                                    CommonUtility.showErrorAlert(getActivity(), jObjError.getString("message"));
                                }
                            } catch (Exception e) {
                                CommonUtility.showErrorAlert(getActivity(), e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Centre> call, Throwable t) {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

    }
}