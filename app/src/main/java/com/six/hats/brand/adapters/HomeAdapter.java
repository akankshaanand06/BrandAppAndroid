package com.six.hats.brand.adapters;

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

import com.six.hats.brand.R;
import com.six.hats.brand.fragments.HomeFragment;

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
                switch (position) {
                    case 0:
                        /*Intent web = new Intent(context, WhatsWebActivity.class);
                        context.startActivity(web);*/

                        break;
                 /*   case 1:
                        Intent statusSaver = new Intent(context, StatusSaver.class);
                        context.startActivity(statusSaver);

                        break;*/
                    case 1:
                      /*  Intent noti = new Intent(context, SocialNotificationActivity.class);
                        context.startActivity(noti);*/
                        break;

                    case 2:
                       /* Intent directMsg = new Intent(context, DirectMessage.class);
                        context.startActivity(directMsg);*/
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
