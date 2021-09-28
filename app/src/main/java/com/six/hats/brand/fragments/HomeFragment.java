package com.six.hats.brand.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.R;
import com.six.hats.brand.adapters.HomeAdapter;
import com.six.hats.brand.model.CampaignItems;
import com.six.hats.brand.model.Centre;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.ui.TopRoundedCornersTransform;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match


    List<HomeItem> homeItemListAbout = new ArrayList<>();
    List<HomeItem> homeItemListForU = new ArrayList<>();
    List<HomeItem> homeItemListMore = new ArrayList<>();

    private HomeAdapter adapterAbout;
    private HomeAdapter adapterForU;
    private HomeAdapter adapterMore;
    private List<CampaignItems.Campaign> campaignItems = new ArrayList<>();
    LinearLayout scrollView;

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        GridLayoutManager gridLayoutManagerU = new GridLayoutManager(getActivity(), 3);
        GridLayoutManager gridLayoutManagerM = new GridLayoutManager(getActivity(), 3);

        RecyclerView home_options_about = (RecyclerView) view.findViewById(R.id.home_options_about);
        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        home_options_about.setLayoutManager(gridLayoutManager);
        adapterAbout = new HomeAdapter(homeItemListAbout, getActivity());
        home_options_about.setAdapter(adapterAbout);

        RecyclerView home_options_for_u = (RecyclerView) view.findViewById(R.id.home_options_for_u);
        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        home_options_for_u.setLayoutManager(gridLayoutManagerU);
        adapterForU = new HomeAdapter(homeItemListForU, getActivity());
        home_options_for_u.setAdapter(adapterForU);

        RecyclerView home_options_more = (RecyclerView) view.findViewById(R.id.home_options_more);
        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        home_options_more.setLayoutManager(gridLayoutManagerM);
        adapterMore = new HomeAdapter(homeItemListMore, getActivity());
        home_options_more.setAdapter(adapterMore);

        scrollView = view.findViewById(R.id.hsv_ll);
        populateHomeAbout();
        populateHomeForU();
        populateHomeMore();
        // if (CentreSingleton.getInstance().getBranchId() == null)
        loadBranchData(PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_BranchQR, "SvBtE6RpC1"));
       /* else
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(CentreSingleton.getInstance().getBusinessName());*/


        return view;
    }

    public List<HomeItem> populateHomeAbout() {

        homeItemListAbout.clear();
        HomeItem item1 = new HomeItem("Professionals", R.drawable.ic_professional, "Use multiple WhatsApp accounts on a\nsingle device.");
        HomeItem item2 = new HomeItem("Services", R.drawable.ic_services, "");
        HomeItem item3 = new HomeItem("Specialities", R.drawable.ic_speciality, "Read your friends messages and chat anonymously.");
        HomeItem item7 = new HomeItem("Facilities/Amenities ", R.drawable.ic_amenities, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item9 = new HomeItem("Gallery", R.drawable.ic_gallery, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem iteme = new HomeItem("About Us", R.drawable.ic_about, "Send WhatsApp messages to anyone without saving their contact number.");


        homeItemListAbout.add(item1);
        homeItemListAbout.add(item3);
        homeItemListAbout.add(item7);
        homeItemListAbout.add(item2);
        homeItemListAbout.add(item9);
        homeItemListAbout.add(iteme);


        adapterAbout.notifyData(homeItemListAbout);
        return homeItemListAbout;
    }


    public List<HomeItem> populateHomeForU() {

        homeItemListForU.clear();
        HomeItem item4 = new HomeItem("Personalised Offers", R.drawable.ic_offer, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item5 = new HomeItem("Personal Notes", R.drawable.ic_notes, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem item6 = new HomeItem("Membership Plans", R.drawable.ic_membership, "Send WhatsApp messages to anyone without saving their contact number.");


        homeItemListForU.add(item4);
        homeItemListForU.add(item5);
        homeItemListForU.add(item6);


        adapterForU.notifyData(homeItemListForU);
        return homeItemListForU;
    }

    public List<HomeItem> populateHomeMore() {

        homeItemListMore.clear();
        HomeItem itema = new HomeItem("Ratings", R.drawable.ic_rating, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemb = new HomeItem("Reviews", R.drawable.ic_reviews, "Send WhatsApp messages to anyone without saving their contact number.");
        HomeItem itemc = new HomeItem("Feedbacks", R.drawable.ic_feedback, "Send WhatsApp messages to anyone without saving their contact number.");

        homeItemListMore.add(itema);
        homeItemListMore.add(itemb);
        homeItemListMore.add(itemc);


        adapterMore.notifyData(homeItemListMore);
        return homeItemListMore;
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
                            PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_BIZ_CATEGORY, centre.getCategory());
                            PrefsWrapper.with(getActivity()).save(JullayConstants.KEY_BIZ_SUB_CATEGORY, centre.getSubCategory());
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(centre.getBusinessName());
                            populateCampaignsList(centre.getBranchId());



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

    private void populateCampaignsList(String branchId) {
        CentralApis.getInstance().getAPIS().loadHomeActiveCampaign(branchId, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    campaignItems.clear();
                    if (response.body().getBranchCampaginResponseHashMap().size() == 0 || response.body().getBranchCampaginResponseHashMap().isEmpty()) {
                        //no_offers_view.setVisibility(View.VISIBLE);
                    } else {
                        // no_offers_view.setVisibility(View.GONE);
                        List<CampaignItems.Campaign> temp = new ArrayList<>();
                        for (int i = 0; i < response.body().getBranchCampaginResponseHashMap().size(); i++) {
                            CampaignItems list = response.body().getBranchCampaginResponseHashMap().get(i);
                            temp.addAll(list.getBranchCampaginResponse().getCampaignArrayList());
                        }


                        campaignItems.addAll(temp);

                        for (int i = 0; i < campaignItems.size(); i++) {
                            ImageView imageView = new ImageView(getActivity());
                            imageView.setElevation(10);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lp.setMargins(0, 0, 16, 0);
                            imageView.setLayoutParams(lp);
                            float twelveDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                                    getActivity().getResources().getDisplayMetrics());
                            try {
                                Picasso.with(getActivity()).load(campaignItems.get(i).getImageURLLink())
                                        .transform(new TopRoundedCornersTransform(twelveDp, 0))
                                        .into(imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                                imageView.setImageBitmap(imageBitmap);
                                                scrollView.addView(imageView);
                                            }

                                            @Override
                                            public void onError() {
                                                imageView.setImageResource(R.drawable.dummy_user);
                                            }
                                        });
                            } catch (Exception e) {
                                imageView.setImageResource(R.drawable.dummy_user);
                                e.printStackTrace();
                            }
                        }


                    }
                } else {


                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.logoutApp(getActivity());
                        } else {
                            CommonUtility.showErrorAlert(getActivity(), jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getActivity(), e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));
            }
        });
    }

    @Keep
    public class APIResponse {
        @SerializedName("branchCampaginResponseHashMap")
        @Expose
        private List<CampaignItems> branchCampaginResponseHashMap = new ArrayList<>();

        public List<CampaignItems> getBranchCampaginResponseHashMap() {
            return branchCampaginResponseHashMap;
        }

        public void setBranchCampaginResponseHashMap(List<CampaignItems> branchCampaginResponseHashMap) {
            this.branchCampaginResponseHashMap = branchCampaginResponseHashMap;
        }
    }
}