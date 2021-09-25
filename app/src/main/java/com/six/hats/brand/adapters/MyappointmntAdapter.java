package com.six.hats.brand.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.model.Appointment;
import com.six.hats.brand.model.AppontmentEnitities;
import com.six.hats.brand.util.CommonUtility;

import java.util.List;


public class MyappointmntAdapter extends RecyclerView.Adapter<MyappointmntAdapter.ViewHolder> {

    private final List<Appointment> mValues;
    Context mContext;
    private ClickListener clickListener;

    public MyappointmntAdapter(List<Appointment> items, Context context) {
        mValues = items;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_appointmntitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position).getAppontmentEnitities().get(0);
        holder.user_name.setText(mValues.get(position).getUserName());
        holder.appt_time.setText((Html.fromHtml("<font color=\"#17202A\"> <font family=\"Helvetica\">" + "Time:" + " " + "</font>" + CommonUtility.formatSpan(holder.mItem.getBookingSlot().getStartSpan().getHour(), holder.mItem.getBookingSlot().getStartSpan().getMinutes()))));
        holder.serv_prov_name.setText(holder.mItem.getBranchName());
        holder.appt_date.setText(CommonUtility.getISOformatToNormalDate(holder.mItem.getBookingSlot().getDate()));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView appt_time;
        public final TextView serv_prov_name, appt_date, user_name;
        public AppontmentEnitities mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            appt_time = (TextView) view.findViewById(R.id.appt_time);
            serv_prov_name = (TextView) view.findViewById(R.id.serv_prov_name);
            appt_date = (TextView) view.findViewById(R.id.appt_date);
            user_name = (TextView) view.findViewById(R.id.user_name);
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + serv_prov_name.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
