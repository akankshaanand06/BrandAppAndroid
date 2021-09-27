package com.six.hats.brand.booking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.model.booking.QATResponseList;
import com.six.hats.brand.ui.RoundedCornersTransform;
import com.six.hats.brand.util.CommonUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FinalBookingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_IDLE = 0;
    private static final int TYPE_BOOKED = 1;
    private List<QATResponseList> mValues = new ArrayList<>();

    private Context mContext;

    public FinalBookingListAdapter(Context context, List<QATResponseList> items) {
        mValues = items;

        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == TYPE_IDLE) { // for call layout
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_waiting_time, viewGroup, false);
            return new IdleViewHolder(view);

        } else { // for email layout
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.final_qat_items, viewGroup, false);
            return new BookedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_IDLE) {
            ((IdleViewHolder) viewHolder).setIdleDetails(mValues.get(position));
        } else {
            ((BookedViewHolder) viewHolder).onBookedService(mValues.get(position));
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (!mValues.get(position).getIdleDiff().equalsIgnoreCase("0") && !mValues.get(position).getIdleDiff().equalsIgnoreCase("-1")) {
            return TYPE_IDLE;
        } else if (mValues.get(position).getIdleDiff().equalsIgnoreCase("0")) {
            return TYPE_IDLE;
        } else {
            return TYPE_BOOKED;
        }
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookedViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        ImageView staff_img, stamp_qat;
        TextView qat_time, staf_name, booked_services;
        public LinearLayout ll;
        public QATResponseList mItem;

        public BookedViewHolder(View view) {
            super(view);
            mView = view;
            qat_time = view.findViewById(R.id.qat_time);
            staf_name = view.findViewById(R.id.staff_name);
            staff_img = view.findViewById(R.id.staff_img);
           // stamp_qat = view.findViewById(R.id.stamp_qat);
            booked_services = view.findViewById(R.id.booked_services);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + staf_name.getText() + "'";
        }


        public void onBookedService(QATResponseList qatResponseList) {

            List<String> servNames = new ArrayList<>();
            for (int i = 0; i < qatResponseList.getServiceId().size(); i++) {
                ServicesData servicesData = BookingSingleton.getServicesData(qatResponseList.getServiceId().get(i));
                servNames.add(servicesData.getServiceName());
            }
            booked_services.setText(CommonUtility.convertListToCommaStringLineWise(servNames));

            qat_time.setText(CommonUtility.formatSpan(qatResponseList.getQatResponseList().getStartSpan().getHour(), qatResponseList.getQatResponseList().getStartSpan().getMinutes()));
            staf_name.setText(qatResponseList.getQatResponseList().getStaffName());
            try {
                Picasso.with(mContext).load(qatResponseList.getQatResponseList().getStaffPic())
                        .resize(100, 100)
                        .transform(new RoundedCornersTransform())
                        .into(staff_img, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap imageBitmap = ((BitmapDrawable) staff_img.getDrawable()).getBitmap();

                                staff_img.setImageBitmap(imageBitmap);
                            }

                            @Override
                            public void onError() {
                                staff_img.setImageResource(R.drawable.dummy_user);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public class IdleViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;


        public IdleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.idle_time);
        }

        void setIdleDetails(QATResponseList qatResponseList) {
            txtName.setText(qatResponseList.getIdleDiff() + " Mins Wait");
        }
    }

}
