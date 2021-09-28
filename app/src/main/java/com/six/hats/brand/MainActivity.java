package com.six.hats.brand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.six.hats.brand.booking.BookingOptionsFragment;
import com.six.hats.brand.booking.ServiceItemFragmentNew;
import com.six.hats.brand.fragments.HomeFragment;
import com.six.hats.brand.fragments.MyAppointmentList;
import com.six.hats.brand.fragments.RTPDialogFragment;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.booking.Appointment;
import com.six.hats.brand.model.booking.AppointmentsItems;
import com.six.hats.brand.model.booking.BookingLstDetails;
import com.six.hats.brand.model.booking.LiveBookingResponse;
import com.six.hats.brand.model.booking.ServicesItems;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RTPDialogFragment.DialogListener, ServiceItemFragmentNew.OnListFragmentInteractionListener, MyAppointmentList.OnListFragmentInteractionListener {
    AppCompatActivity appCompatActivity;
    private AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView navigation_bottom_;
    List<Appointment> bookingsList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    changeFragment(new HomeFragment()/*, activity*/, "HomeFragment", "replace");
                    return true;
               /* case R.id.reservations:
                    changeFragment(new MyAppointmentList()*//*, activity*//*, "MyBookings", "replace");
                    return true;*/
                case R.id.nav_book:

                    CentreSingleton singleton = CentreSingleton.getInstance();
                    changeFragment(BookingOptionsFragment.newInstance(singleton.getBranchId(), singleton.getBusinessName())/*, activity*/, "Book appointment", "replace");

                    return true;
                case R.id.nav_lqp:
                    changeFragment(new RTPFragment()/*, activity*/, "HomeFragment", "replace");
                    return true;
                // case R.id.nav_scan:
                //changeFragment(new SettingsFragment().newInstance()/*, activity*/, "HomeFragment", "replace");
                //  return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appCompatActivity = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        // NavController navController = Navigation.findNavController(this, R.id.fragContainer);
        //  NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //   NavigationUI.setupWithNavController(navigationView, navController);

        navigation_bottom_ = (BottomNavigationView) findViewById(R.id.navigation_bottom_);
        navigation_bottom_.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeFragment(new HomeFragment()/*, activity*/, "QMasterHomeFragment", "replace");

        loadMyBookingDetails();


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragContainer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_timeline:
                Intent appointments = new Intent(getApplicationContext(), MenuActivity.class);
                appointments.putExtra("menu", "appointments");
                appointments.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(appointments);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        int seletedItemId = navigation_bottom_.getSelectedItemId();
        if (R.id.nav_home != seletedItemId) {
            navigation_bottom_.setSelectedItemId(R.id.nav_home);
        } else {
            finish();
        }


        super.onBackPressed();
    }


    public void changeFragment(Fragment fragment, String tagFragmentName, String type) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            if (type.equalsIgnoreCase("replace")) {

                fragmentTemp = fragment;
                fragmentTransaction.replace(R.id.fragContainer, fragmentTemp, tagFragmentName);
            } else {

                fragmentTemp = fragment;
                fragmentTransaction.add(R.id.fragContainer, fragmentTemp, tagFragmentName);
            }
        } else {

            if (type.equalsIgnoreCase("replace")) {

                fragmentTemp = fragment;
                fragmentTransaction.replace(R.id.fragContainer, fragmentTemp, tagFragmentName);
            } else {

                fragmentTransaction.show(fragmentTemp);
            }
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    @Override
    public void onListFragmentInteraction(ServicesItems item) {

    }

    @Override
    public void onListFragmentInteraction(AppointmentsItems item) {


    }

    @Override
    public void onFinishEditDialog(String inputText) {

    }


    private void loadMyBookingDetails() {

        CentralApis.getInstance().getAPIS().loadMyBookingData(PrefsWrapper.with(appCompatActivity).getString(JullayConstants.KEY_USER_ID, "0"),
                PrefsWrapper.with(appCompatActivity).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BookingLstDetails>() {
            @Override
            public void onResponse(Call<BookingLstDetails> call, Response<BookingLstDetails> response) {
                if (response.isSuccessful()) {
                    BookingLstDetails myBookingsResponse = response.body();
                    if (myBookingsResponse.getAppointmentList() != null && myBookingsResponse.getAppointmentList().size() != 0) {
                        List<Appointment> bookingsList = new ArrayList<>();
                        bookingsList.addAll(myBookingsResponse.getAppointmentList());
                        if (CommonUtility.isTodaysBooking(bookingsList.get(0).getAppontmentEnitities().get(0).getBookingSlot().getDate()))
                            loadLiveStatusData(bookingsList.get(0).getAppointmentId());

                    } else {

                    }


                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        CommonUtility.showErrorAlert(appCompatActivity, jObjError.getString("message"));

                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(appCompatActivity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingLstDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /**
     * Check live booking status
     *
     * @param bookingID
     */
    private void loadLiveStatusData(final String bookingID) {
        CentralApis.getInstance().getAPIS().loadLiveStatus(PrefsWrapper.with(appCompatActivity).getString(JullayConstants.KEY_USER_ID, ""),
                bookingID, PrefsWrapper.with(appCompatActivity).getString(JullayConstants.KEY_USER_TOKEN, ""))
                .enqueue(new retrofit2.Callback<LiveBookingResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<LiveBookingResponse> call, Response<LiveBookingResponse> response) {

                        if (response.isSuccessful()) {

                            LiveBookingResponse data = response.body();

                            RTPDialogFragment dialogFragment = new RTPDialogFragment();

                            Bundle bundle = new Bundle();
                            bundle.putBoolean("notAlertDialog", true);
                            bundle.putSerializable("data", data);

                            dialogFragment.setArguments(bundle);

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);
                            dialogFragment.show(ft, "dialog");
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
                    public void onFailure(Call<LiveBookingResponse> call, Throwable t) {
                        t.printStackTrace();
                       /* if (!CommonUtility.haveNetworkConnection(getActivity())) {
                            CommonUtility.showAlertRetryCancel(appCompatActivity, getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //retry
                                    loadLiveStatusData("Appointment_258");
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            CommonUtility.showErrorAlert(appCompatActivity, getString(R.string.network_error_text));

                        }*/
                    }
                });

    }
}