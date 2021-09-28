package com.six.hats.brand;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.six.hats.brand.fragments.MyAppointmentList;
import com.six.hats.brand.fragments.RatingFragment;
import com.six.hats.brand.model.booking.Appointment;
import com.six.hats.brand.model.booking.AppointmentsItems;
import com.six.hats.brand.model.booking.BookingLstDetails;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.qrscanner.GraphicOverlay;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements MyAppointmentList.OnListFragmentInteractionListener {

    List<Appointment> bookingsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
            if (getIntent().getStringExtra("menu").equals("rating")) {
                //getSupportActionBar().setTitle(R.string.change_password);
                getSupportActionBar().setTitle("Ratings");
                replaceFragmentHistory(new RatingFragment(), this);
            } else if (getIntent().getStringExtra("menu").equals("appointments")) {
                getSupportActionBar().setTitle("My Reservations");
                replaceFragmentHistory(new MyAppointmentList(), this);

            } /*else if (getIntent().getStringExtra("menu").equals("privacy")) {
                getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.privacy_policy) + "</font>")));
                replaceFragmentHistory(new PrivacyFragment(), this);

            } else if (getIntent().getStringExtra("menu").equals("campaign")) {
                getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.converation) + "</font>")));
                replaceFragmentHistory(new CampaignsFragment(), this);

            } else if (getIntent().getStringExtra("menu").equals("appt_details")) {
                String bookingID = getIntent().getStringExtra("BID");
                replaceFragmentHistory(new AppointmentDetail().newInstance(bookingID, ""), this);

            } else if (getIntent().getStringExtra("menu").equals("my_favs")) {
                getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.my_favourites) + "</font>")));
                replaceFragmentHistory(new MyFavouritesPage(), this);

            }else if (getIntent().getStringExtra("menu").equals("profile")) {
                getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.my_favourites) + "</font>")));
                replaceFragmentHistory(new MyAccountsDetails(), this);

            }  else if (getIntent().getStringExtra("menu").equals("update_profile")) {

                UserDetails userDetails = (UserDetails) getIntent().getSerializableExtra("userData");
                getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.update_persnl_details) + "</font>")));
                replaceFragmentHistory(new UpdatePersonelDetails().newInstance(userDetails), this);
            } else if (getIntent().getStringExtra("menu").equals("notification")) {
                getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.title_notifications) + "</font>")));
                replaceFragmentHistory(new NotificationList().newInstance(), this);

            }*/

        }
    }

    /**
     * Adds 1st fragment to activity without being added to the history stack
     *
     * @param fragment
     */
    public static void replaceFragmentHistory(Fragment fragment, AppCompatActivity context) {
       /* if (isUsedBundle) {
            Bundle args = new Bundle();
            args.putInt(bundleParameterName, bundleValue);
            fragment.setArguments(args);
        }*/
        if (context != null) {
            try {
                FragmentTransaction transaction =
                        context.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);

                transaction.replace(R.id.menuContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

            } catch (IllegalStateException e) {
                e.printStackTrace();
                context.finish();
            }
        }
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

    @Override
    public void onListFragmentInteraction(AppointmentsItems item) {

    }




}
