package com.six.hats.brand.booking;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.six.hats.brand.BaseActivity;
import com.six.hats.brand.R;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.booking.ServicesItems;

public class BookSeatActivity extends BaseActivity implements PrebookingSlotsFragment.OnListFragmentInteractionListener, ServiceItemFragmentNew.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_seat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {

            CentreSingleton singleton = CentreSingleton.getInstance();
            replaceFragmentHistory(new BookingOptionsFragment().newInstance(singleton.getBranchId(), singleton.getBusinessName()), this);
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

                transaction.replace(R.id.bsContainer, fragment);
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

    /* @Override
     public void onBackPressed() {
         finish();
         super.onBackPressed();
     }

 */
    @Override
    public void onListFragmentInteraction(String item) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    @Override
    public void onListFragmentInteraction(ServicesItems item) {

    }
}
