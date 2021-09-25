package com.six.hats.brand.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.six.hats.brand.R;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView ratingValue;
    RatingBar ratingBar;

    public RatingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RatingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RatingFragment newInstance(String param1, String param2) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        ratingValue = view.findViewById(R.id.rating_Value);
        ratingBar = view.findViewById(R.id.ratingBar);
        CentreSingleton singleton = CentreSingleton.getInstance();

        loadBranchData(singleton.getBranchId());

        return view;
    }


    public void loadBranchData(String branchId) {

        CentralApis.getInstance().getAPIS().loadCentreRating(branchId, PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, ""))
                .enqueue(new retrofit2.Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        if (response.isSuccessful()) {

                            ratingValue.setText(response.body().getMessage());
                            if (!response.body().getMessage().equalsIgnoreCase("Not Available"))
                                ratingBar.setRating(Float.parseFloat(response.body().getMessage()));

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
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

    }

}