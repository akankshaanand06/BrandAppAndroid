package com.six.hats.brand;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.six.hats.brand.fragments.HomeFragment;

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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView navigation_bottom_;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    changeFragment(new HomeFragment()/*, activity*/, "QMasterHomeFragment", "replace");
                    return true;
                case R.id.nav_social:
                    //changeFragment(new WalkinBookingsFragment().newInstance()/*, activity*/, "MyBookings", "replace");
                    return true;
                case R.id.nav_book:
                    //changeFragment(new EmergencyFragment().newInstance(PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_CENTRE_ID, ""))/*, activity*/, "ScanFragment", "replace");
                    return true;
                case R.id.nav_lqp:
                    //changeFragment(new SettingsFragment().newInstance()/*, activity*/, "HomeFragment", "replace");
                    return true;
                case R.id.nav_scan:
                    //changeFragment(new SettingsFragment().newInstance()/*, activity*/, "HomeFragment", "replace");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragContainer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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

}