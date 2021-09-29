package com.six.hats.brand;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.six.hats.brand.adapters.StaffSectionsPagerAdapter;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.MyServiceResponse;
import com.six.hats.brand.model.StaffApiResponse;
import com.six.hats.brand.model.StaffDetails;
import com.six.hats.brand.model.booking.StaffDisplayResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class StaffViewpagerActivity extends AppCompatActivity {

    List<StaffDisplayResponse> staffDetails = new ArrayList<>();
    private StaffSectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_viewpager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.professionals) + "</font>")));

        sectionsPagerAdapter = new StaffSectionsPagerAdapter(this, staffDetails, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.service_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        CentreSingleton singleton = CentreSingleton.getInstance();

        loadAllStaffsList(singleton.getBranchId());

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

    public void loadAllStaffsList(String branchId) {
        // pass centre id here in my service and  pass centre id in staff details

        String centerId = branchId;
        CentralApis.getInstance().getAPIS().loadAllStaffListByBranchId(centerId,
                PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN, ""))
                .enqueue(new retrofit2.Callback<List<StaffDisplayResponse>>() {
                    @Override
                    public void onResponse(Call<List<StaffDisplayResponse>> call, Response<List<StaffDisplayResponse>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().size() == 0 || response.body().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "There is no data..", Toast.LENGTH_LONG).show();
                            } else {
                                staffDetails.clear();
                                staffDetails.addAll(response.body());
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
                    public void onFailure(Call<List<StaffDisplayResponse>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }
}