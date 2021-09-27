package com.six.hats.brand.booking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.R;
import com.six.hats.brand.model.booking.TimeSpecQATResponse;
import com.six.hats.brand.ui.RoundedCornersTransform;
import com.six.hats.brand.util.CommonUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TimeSpecificListAdapter extends RecyclerView.Adapter<TimeSpecificListAdapter.FilterViewHolder> {
    private List<TimeSpecQATResponse.QATList> servicesQATList = new ArrayList<>();
    Context context;
    final int[] row_index = new int[1];
    private static ClickListener clickListener;

    //private final ServiceItemFragment.OnListFragmentInteractionListener mListener;
    public TimeSpecificListAdapter(List<TimeSpecQATResponse.QATList> horizontalFiltersList, Context context) {
        this.servicesQATList = horizontalFiltersList;
        this.context = context;
    }

    public void notifyData(List<TimeSpecQATResponse.QATList> horizontalFiltersList) {
        Log.d("notifyData ", horizontalFiltersList.size() + "");
        this.servicesQATList = horizontalFiltersList;
        notifyDataSetChanged();
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qat_child_layout, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilterViewHolder holder, final int position) {
        TimeSpecQATResponse.QATList bookingItems = servicesQATList.get(position);
        holder.staff_name.setText(bookingItems.getStaffName());
        holder.qat_time.setText(CommonUtility.formatSpan(bookingItems.getStartSpan().getHour(), bookingItems.getStartSpan().getMinutes()));
        final int radius = 5;
        final int margin = 5;


        try {
            Picasso.with(context).load(bookingItems.getStaffPic())
                    .resize(100, 100)
                    .transform(new RoundedCornersTransform())
                    .into(holder.staff_img, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) holder.staff_img.getDrawable()).getBitmap();
                           /* RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                            imageDrawable.setCircular(false);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 1.0f);*/
                            holder.staff_img.setImageBitmap(imageBitmap);
                        }

                        @Override
                        public void onError() {
                            holder.staff_img.setImageResource(R.drawable.dummy_user);
                        }
                    });
        } catch (Exception e) {
            holder.staff_img.setImageResource(R.drawable.dummy_user);
            e.printStackTrace();
        }

        if (servicesQATList.get(position).getSelected() == null) {
            servicesQATList.get(position).setSelected(false);
        }
        if (servicesQATList.get(position).getSelected()) {
            holder.qat_item_lay.setBackgroundResource(R.drawable.round_one_sided_shadow_selected);
            for (int i = 0; i < servicesQATList.size(); i++) {
                if (i != position) {
                    servicesQATList.get(i).setSelected(false);
                }
            }
        } else {
            holder.qat_item_lay.setBackgroundResource(R.drawable.round_one_sided_shadow);
        }
    }

    @Override
    public int getItemCount() {
        return this.servicesQATList.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RecyclerView qat_child_list;
        public LinearLayout qat_item_lay;
        public TextView staff_name, qat_time;
        public ImageView staff_img;
        public final View mView;

        public FilterViewHolder(View view) {
            super(view);
            mView = view;
            staff_img = view.findViewById(R.id.staff_img);
            staff_name = view.findViewById(R.id.staff_name);
            qat_time = view.findViewById(R.id.qat_time);
            qat_item_lay = view.findViewById(R.id.qat_item_lay);
            mView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TimeSpecificListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}