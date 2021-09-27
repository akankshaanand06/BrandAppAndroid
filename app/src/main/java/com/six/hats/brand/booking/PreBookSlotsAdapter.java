package com.six.hats.brand.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.model.booking.PreBookDates;

import java.util.HashMap;
import java.util.List;


public class PreBookSlotsAdapter extends RecyclerView.Adapter<PreBookSlotsAdapter.ViewHolder> {

    private final List<PreBookDates> mValues;
    String centreId;
    private HashMap<Integer, List<String>> ServiceIDs;
    Context mContext;
    /*  List<Integer> mDuration = new ArrayList<>();
      List<Integer> mPrice = new ArrayList<>();*/
 /*   List<List<SearchQatRequest>> mSearchQatRequestList = new ArrayList<>();
    List<List<String>> mSelectedStaffs = new ArrayList<>();*/
    private static ClickListener clickListener;

    public PreBookSlotsAdapter(Context context, List<PreBookDates> items/*, String mCentreId, HashMap<Integer, List<String>> mServiceIDs, List<Integer> duration, List<Integer> price, List<List<SearchQatRequest>> searchQatRequestList, List<List<String>> unSelectedStaffs*/) {
        mValues = items;
        /*centreId = mCentreId;
        ServiceIDs = mServiceIDs;
        mDuration = duration;
        mPrice = price;*/
        mContext = context;
       /* mSearchQatRequestList = searchQatRequestList;
        mSelectedStaffs = unSelectedStaffs;*/

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_prebook_slots, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.mItem = mValues.get(position)/*.getMessage()*/;
        holder.mIdView.setText(holder.mItem.getmValues());
        holder.mIdView.setBackgroundResource(R.drawable.shadow_bg_padding);

        if (mValues.get(position).getSelected()) {
            holder.mIdView.setBackgroundResource(R.drawable.shadow_bg_selected_padding);
        } else {

        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public PreBookDates mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.slot_ref_time);

            mView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }

    }

    public void setOnItemClickListener(PreBookSlotsAdapter.ClickListener clickListener) {
        PreBookSlotsAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
