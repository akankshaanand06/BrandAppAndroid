package com.six.hats.brand.booking;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.R;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.model.booking.SimpleStringItem;

import java.util.ArrayList;
import java.util.List;


public class StringHoriListAdapter extends RecyclerView.Adapter<StringHoriListAdapter.ViewHolder> {

    List<SimpleStringItem> mValues = new ArrayList<>();
    String centreId;
    Context mContext;
    /*  List<Integer> mDuration = new ArrayList<>();
      List<Integer> mPrice = new ArrayList<>();*/
 /*   List<List<SearchQatRequest>> mSearchQatRequestList = new ArrayList<>();
    List<List<String>> mSelectedStaffs = new ArrayList<>();*/
    private static ClickListener clickListener;


    public void notifyData(List<SimpleStringItem> items) {
        Log.d("notifyData ", items.size() + "");
        this.mValues = items;
        notifyDataSetChanged();
    }


    public StringHoriListAdapter(Context context, List<SimpleStringItem> items/*, String mCentreId, HashMap<Integer, List<String>> mServiceIDs, List<Integer> duration, List<Integer> price, List<List<SearchQatRequest>> searchQatRequestList, List<List<String>> unSelectedStaffs*/) {
        this.mValues = items;
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
                .inflate(R.layout.filter_items_trans, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ServicesData servicesData = BookingSingleton.getServicesData(mValues.get(position).getName());
        holder.mItem = mValues.get(position).getName();
        holder.mIdView.setText(servicesData.getServiceName());
        if (mValues.size() != 0 /*&& !selected*/) {
            if (/*row_index[0] == position && */mValues.get(position).isSelected()) {

                for (int i = 0; i < mValues.size(); i++) {
                    if (i != position) {
                        mValues.get(position).setSelected(false);
                    }
                }
                holder.mIdView.setTextColor(mContext.getResources().getColor(R.color.pure_white));
                holder.mIdView.setTypeface(holder.mIdView.getTypeface(), Typeface.BOLD);
                holder.mIdView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                holder.mIdView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_white_dot);
            } else {
                holder.mIdView.setTextColor(mContext.getResources().getColor(R.color.darkBlue));
                holder.mIdView.setTypeface(holder.mIdView.getTypeface(), Typeface.NORMAL);
                holder.mIdView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                holder.mIdView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
      /*  if (mValues.size() != 0) {
            if (mValues.get(position).isStaffSelected() && !mValues.get(position).isSelected()) {

                //if staff is choosen but current selected postion is not this - then just show bold text
                holder.mIdView.setTextColor(mContext.getResources().getColor(R.color.white_normal));
                holder.mIdView.setTypeface(holder.mIdView.getTypeface(), Typeface.BOLD);
                holder.mIdView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                holder.mIdView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else if (mValues.get(position).isStaffSelected() && mValues.get(position).isSelected()) {
                //if staff is choosen and current selected postion is this - then show bold text with dot
                for (int i = 0; i < mValues.size(); i++) {
                    if (i != position) {
                        mValues.get(position).setSelected(false);
                    }
                }

                holder.mIdView.setTextColor(mContext.getResources().getColor(R.color.darkBlue));
                holder.mIdView.setTypeface(holder.mIdView.getTypeface(), Typeface.BOLD);
                holder.mIdView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                holder.mIdView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_white_dot);
            } else if (!mValues.get(position).isStaffSelected() && mValues.get(position).isSelected()) {
                //if staff is not choosen but current postion is currently selected
                for (int i = 0; i < mValues.size(); i++) {
                    if (i != position) {
                        mValues.get(position).setSelected(false);
                    }
                }

                holder.mIdView.setTextColor(mContext.getResources().getColor(R.color.darkBlue));
                holder.mIdView.setTypeface(holder.mIdView.getTypeface(), Typeface.BOLD);
                holder.mIdView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                holder.mIdView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_white_dot);

            }
        }
*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.idpersonName);

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

    public void setOnItemClickListener(StringHoriListAdapter.ClickListener clickListener) {
        StringHoriListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
