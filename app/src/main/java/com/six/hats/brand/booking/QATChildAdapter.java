package com.six.hats.brand.booking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.model.booking.StaffList;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QATChildAdapter extends RecyclerView.Adapter<QATChildAdapter.StaffViewHolder> {
    Context context;
    final int[] row_index = new int[1];
    private static ClickListener clickListener;
    public List<String> selectedTime = new ArrayList<>();
    String fkCentreId, serviceId, mServiceType;
    int mParentPos = 0;
    private List<StaffList> mValues = new ArrayList<>();

    //private final ServiceItemFragment.OnListFragmentInteractionListener mListener;
    public QATChildAdapter(Context context, List<StaffList> items) {
        this.mValues = items;
        this.context = context;
    }


    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new StaffViewHolder(groceryProductView);
    }

    @Override
    public void onBindViewHolder(final StaffViewHolder holder, final int position) {

        holder.horizontalFilterList = mValues.get(position);
        holder.staf_name.setText(holder.horizontalFilterList.getStaffName());
        // holder.qat_time.setText(CommonUtility.formatTimeOnly(Long.valueOf(horizontalFilterList.get(position).getQatTime())));

        try {
            Picasso.with(context).load(holder.horizontalFilterList.getStaffPic())
                    .into(holder.staff_img, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imageBitmap = ((BitmapDrawable) holder.staff_img.getDrawable()).getBitmap();
               /*     RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
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

        if (holder.horizontalFilterList.getIsselected() == null) {
            holder.horizontalFilterList.setIsselected(false);
        }

        if (holder.horizontalFilterList.getIsselected()) {
            holder.staff_item_lay.setBackgroundResource(R.drawable.shadow_background_selected);
            holder.checkb.setImageResource(R.drawable.yellow_tick);
            //holder.checkb.setChecked(true);

        } else {
            holder.staff_item_lay.setBackgroundResource(0);
            holder.checkb.setImageResource(R.drawable.empty_tick);
            //holder.checkb.setChecked(false);
        }

       /* for (int i = 0; i < horizontalFilterList.size(); i++) {
            if (i != position) {
                horizontalFilterList.get(i).setSelected(false);
            }
        }*/

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public StaffList horizontalFilterList = new StaffList();
        ImageView staff_img;
        TextView qat_time, staf_name;
        public FrameLayout staff_item_lay;
        public final View mView;
        public ImageView checkb;

        public StaffViewHolder(final View view) {
            super(view);
            mView = view;
            /*qat_time = view.findViewById(R.id.qat_time);*/
            staf_name = view.findViewById(R.id.staff_name);
            staff_img = view.findViewById(R.id.staffImage);
            checkb = view.findViewById(R.id.checkb);
            staff_item_lay = (FrameLayout) view.findViewById(R.id.staff_item_lay);
            mView.setOnClickListener(this);
           /* view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // run scale animation and make it bigger
                        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_in_tv);
                        view.startAnimation(anim);
                        anim.setFillAfter(true);
                    } else {
                        // run scale animation and make it smaller
                        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_out_tv);
                        view.startAnimation(anim);
                        anim.setFillAfter(true);
                    }
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        QATChildAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}