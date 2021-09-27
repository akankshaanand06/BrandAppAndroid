package com.six.hats.brand.booking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.model.booking.SimpleStringItem;

import java.util.ArrayList;
import java.util.List;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    List<SimpleStringItem> mValues = new ArrayList<>();
    String centreId;
    Context mContext;
    /*  List<Integer> mDuration = new ArrayList<>();
      List<Integer> mPrice = new ArrayList<>();*/
 /*   List<List<SearchQatRequest>> mSearchQatRequestList = new ArrayList<>();
    List<List<String>> mSelectedStaffs = new ArrayList<>();*/
    private static ClickListener clickListener;

    public PersonAdapter(Context context, List<SimpleStringItem> items/*, String mCentreId, HashMap<Integer, List<String>> mServiceIDs, List<Integer> duration, List<Integer> price, List<List<SearchQatRequest>> searchQatRequestList, List<List<String>> unSelectedStaffs*/) {
        mValues = items;
        /*centreId = mCentreId;
        ServiceIDs = mServiceIDs;
        mDuration = duration;
        mPrice = price;*/
        mContext = context;
       /* mSearchQatRequestList = searchQatRequestList;
        mSelectedStaffs = unSelectedStaffs;*/

    }

    public void notifyData(List<SimpleStringItem> items) {
        Log.d("notifyData ", items.size() + "");
        this.mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_items_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.getName());
        try {
            if (mValues.size() != 0 /*&& !selected*/) {
                if (/*row_index[0] == position && */holder.mItem.isSelected()) {

                    for (int i = 0; i < mValues.size(); i++) {
                        if (i != position) {
                            holder.mItem.setSelected(false);
                        }
                    }
                    holder.mIdView.setBackgroundResource(R.drawable.round_top_sided_selected_yellow);
                    holder.mIdView.setTextColor(mContext.getResources().getColor(R.color.darkBlue));

                } else {

                    holder.mIdView.setBackgroundResource(R.drawable.primary_rounded_button);
                    holder.mIdView.setTextColor(mContext.getResources().getColor(R.color.darkBlue));

                    // notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public SimpleStringItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.slot_ref_time);

            mView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }

    }

    public void setOnItemClickListener(PersonAdapter.ClickListener clickListener) {
        PersonAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
