package com.six.hats.brand.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.AboutUsActivity;
import com.six.hats.brand.BizGallery;
import com.six.hats.brand.Feedbacks;
import com.six.hats.brand.MenuActivity;
import com.six.hats.brand.R;
import com.six.hats.brand.ServicesViewpagerActivity;
import com.six.hats.brand.StaffViewpagerActivity;
import com.six.hats.brand.booking.BookSeatActivity;
import com.six.hats.brand.fragments.HomeFragment;
import com.six.hats.brand.model.CentreSingleton;

import java.util.ArrayList;
import java.util.List;


@Keep
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    List<HomeFragment.HomeItem> homeItems = new ArrayList<>();
    Context context;

    public HomeAdapter(List<HomeFragment.HomeItem> homeItems, Context activity) {
        this.homeItems = homeItems;
        this.context = activity;
    }


    public void notifyData(List<HomeFragment.HomeItem> items) {
        Log.d("notifyData ", items.size() + "");
        this.homeItems = items;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hom_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.home_icon.setImageResource(homeItems.get(position).getHome_icon());
        holder.home_name.setText(homeItems.get(position).getHome_name());
        holder.home_desc.setText(homeItems.get(position).getDesc());
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (homeItems.get(position).getHome_name()) {
                    case "About Us":
                        Intent about = new Intent(context, AboutUsActivity.class);
                        about.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(about);
                        break;
                    case "Services":
                        Intent servAc = new Intent(context, ServicesViewpagerActivity.class);
                        servAc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(servAc);

                        break;
                    case "Professionals":
                        Intent staff = new Intent(context, StaffViewpagerActivity.class);
                        staff.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(staff);

                        break;
                    case "Gallery":
                        Intent bizGallery = new Intent(context, BizGallery.class);
                        bizGallery.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(bizGallery);
                        break;
                    case "Ratings":
                        Intent rating = new Intent(context, MenuActivity.class);
                        rating.putExtra("menu", "rating");
                        rating.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(rating);
                        break;
                    case "Feedbacks":
                        Intent feedback = new Intent(context, Feedbacks.class);
                        feedback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(feedback);
                        break;

                    case "My Timeline":
                        Intent appointments = new Intent(context, MenuActivity.class);
                        appointments.putExtra("menu", "appointments");
                        appointments.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(appointments);
                        break;
                    case "Book Now":
                      /*  CentreSingleton singleton = CentreSingleton.getInstance();
                        Intent appotmnt_detail = new Intent(context, BookSeatActivity.class);
                        appotmnt_detail.putExtra("CentreId", singleton.getBranchId());
                        appotmnt_detail.putExtra("branch_name", singleton.getBusinessName());
                        appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ActivityOptions options =
                                ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_in_right);
                        context.startActivity(appotmnt_detail, options.toBundle());*/
                        break;


                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return homeItems.size();
    }

    @Keep
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView home_name, home_desc;
        ImageView home_icon;
        View mview;

        public MyViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            home_name = (TextView) itemView.findViewById(R.id.home_name);
            home_desc = (TextView) itemView.findViewById(R.id.home_desc);

            home_icon = (ImageView) itemView.findViewById(R.id.home_icon);
        }
    }


}
