package com.six.hats.brand;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.six.hats.brand.adapters.SectionsPagerAdapter;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.MyServiceResponse;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SpecialityViewpagerActivity extends AppCompatActivity {

    List<ServicesData> serviceResponses = new ArrayList<>();
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_viewpager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.services) + "</font>")));

        sectionsPagerAdapter = new SectionsPagerAdapter(this, serviceResponses, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.service_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        CentreSingleton singleton = CentreSingleton.getInstance();

        loadServiceDetails(singleton.getBranchId());

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


    public void loadServiceDetails(String branchId) {
        // pass centre id here in my service and  pass centre id in staff detaikls

        CentralApis.getInstance().getBookingAPIS().loadMySpecialityService(branchId, "NA", PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<MyServiceResponse>() {
            @Override
            public void onResponse(Call<MyServiceResponse> call, Response<MyServiceResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().size() == 0 || response.body().getData().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "There is no data..", Toast.LENGTH_LONG).show();
                    } else {
                        serviceResponses.clear();
                        serviceResponses.addAll(response.body().getData());
                        sectionsPagerAdapter.notifyDataSetChanged();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(getApplicationContext());
                        } else {
                            CommonUtility.showErrorAlert(getApplicationContext(), jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getApplicationContext(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MyServiceResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}