package com.six.hats.brand.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.AboutUsActivity;
import com.six.hats.brand.R;
import com.six.hats.brand.ServicesViewpagerActivity;
import com.six.hats.brand.StaffViewpagerActivity;
import com.six.hats.brand.fragments.HomeFragment;
import com.six.hats.brand.model.ImageItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


@Keep
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    List<ImageItem> homeItems = new ArrayList<>();
    Context context;

    public GalleryAdapter(List<ImageItem> homeItems, Context activity) {
        this.homeItems = homeItems;
        this.context = activity;
    }


    public void notifyData(List<ImageItem> items) {
        Log.d("notifyData ", items.size() + "");
        this.homeItems = items;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_layout_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        try {
            Picasso.with(context).load(homeItems.get(position).getImageUrl())
                    .into(holder.galler_img, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) holder.galler_img.getDrawable()).getBitmap();
                         /*   RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);*/
                            holder.galler_img.setImageBitmap(imageBitmap);
                        }

                        @Override
                        public void onError() {
                            holder.galler_img.setImageResource(R.drawable.dummy_user);
                        }
                    });
        } catch (Exception e) {
            holder.galler_img.setImageResource(R.drawable.dummy_user);
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return homeItems.size();
    }

    @Keep
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView galler_img;
        View mview;

        public MyViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            galler_img = (ImageView) itemView.findViewById(R.id.galler_img);
        }
    }


}
