package com.six.hats.brand.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.fragments.ServicesPlaceholderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private final Context mContext;
    List<ServicesData> serviceResponses = new ArrayList<>();


    public SectionsPagerAdapter(Context context, List<ServicesData> servicesData, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.serviceResponses = servicesData;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return ServicesPlaceholderFragment.newInstance(serviceResponses.get(position), position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return serviceResponses.size();
    }
}