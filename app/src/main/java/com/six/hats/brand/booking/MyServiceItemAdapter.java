package com.six.hats.brand.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.model.ServicesData;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ServicesData} and makes a call to the
 * specified {@link ServiceItemFragment.OnInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyServiceItemAdapter extends RecyclerView.Adapter<MyServiceItemAdapter.ViewHolder> {
    Context context;
    private List<ServicesData> mValues = new ArrayList<>();
    // private final OnListFragmentInteractionListener mListener;
    final int[] row_index = new int[1];

    private static ClickListener clickListener;

    public MyServiceItemAdapter(List<ServicesData> ServicesData, Context mcontext) {
        this.mValues = ServicesData;
        this.context = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_serviceitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.service_name.setText(mValues.get(position).getServiceName());

        holder.service_duration.setText(/*context.getString(R.string.duration) +*/mValues.get(position).getDuration() + " " + context.getString(R.string.mins));
        try {
            String string = "\u20B9";
            byte[] utf8 = null;
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
            holder.service_cost.setText(/*context.getString(R.string.rate) + *//*" | "+*/string + mValues.get(position).getCost());

        } catch (Exception e) {
            e.printStackTrace();
        }



        if (mValues.get(position).getSelected()) {
            holder.checkBox.setImageResource(R.drawable.yellow_tick);
        } else {
            holder.checkBox.setImageResource(R.drawable.empty_tick_black);
        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView service_name, service_duration;
        public final TextView service_cost;
        public ServicesData mItem;
        public LinearLayout ll;
        public ImageView checkBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            service_name = (TextView) view.findViewById(R.id.service_name);
            service_duration = (TextView) view.findViewById(R.id.service_duration);
            service_cost = (TextView) view.findViewById(R.id.service_cost);
            checkBox = (ImageView) view.findViewById(R.id.checkb);

            ll = (LinearLayout) view.findViewById(R.id.layout);
            mView.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + service_name.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MyServiceItemAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

}
