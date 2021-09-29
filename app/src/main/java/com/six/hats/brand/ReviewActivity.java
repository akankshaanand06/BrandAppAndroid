package com.six.hats.brand;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.adapters.ReviewsListAdapter;
import com.six.hats.brand.model.BizReviews;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {


    List<BizReviews> bizReviewsArrayList = new ArrayList<>();
    private ReviewsListAdapter reviewsListAdapter;
    AppCompatActivity appCompatActivity;
    String mCenterId, subCat;
    String staffId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.reviews) + "</font>")));
        appCompatActivity = this;
        RecyclerView feedBackList = findViewById(R.id.feedback_list);
        feedBackList.setLayoutManager(new LinearLayoutManager(this));
        reviewsListAdapter = new ReviewsListAdapter(getApplicationContext(), bizReviewsArrayList);
        feedBackList.setAdapter(reviewsListAdapter);

        CentreSingleton centreSingleton = CentreSingleton.getInstance();
        mCenterId = centreSingleton.getBranchId();
        subCat = centreSingleton.getSubCategory();
        reviewsListAdapter.setOnItemClickListener(new ReviewsListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        String pageCalled = getIntent().getStringExtra("reviewof");
        if (pageCalled.equalsIgnoreCase("staff")) {
            staffId = getIntent().getStringExtra("staffId");
        }
        loadReviewsData(pageCalled);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void loadReviewsData(String pageCalled) {
        CentreSingleton centreSingleton = CentreSingleton.getInstance();
        String centreId = centreSingleton.getBranchId();
        //pass centre id here....

        Call<List<BizReviews>> apiCall = null;
        if (pageCalled.equalsIgnoreCase("branch")) {
            apiCall = CentralApis.getInstance().getAPIS().loadAllReviewsForBranch(centreId,
                    PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN, ""));
        } else {
            if (CommonUtility.chkString(staffId))
                apiCall = CentralApis.getInstance().getAPIS().loadAllReviewsForStaff(centreId, staffId,
                        PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN, ""));
        }
        apiCall.enqueue(new retrofit2.Callback<List<BizReviews>>() {
            @Override
            public void onResponse(Call<List<BizReviews>> call, Response<List<BizReviews>> response) {

                if (response.isSuccessful()) {
                    bizReviewsArrayList.clear();
                    bizReviewsArrayList.addAll(response.body());

                    reviewsListAdapter.notifyData(bizReviewsArrayList);
                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {

                            CommonUtility.autoLogin(appCompatActivity);
                        } else {
                            CommonUtility.showErrorAlert(appCompatActivity, jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(appCompatActivity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BizReviews>> call, Throwable t) {
                CommonUtility.showErrorAlert(appCompatActivity, getString(R.string.network_error_text));
            }
        });

    }

}
