package com.six.hats.brand.adapters;

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
import com.six.hats.brand.booking.BookingSingleton;
import com.six.hats.brand.model.BizReviews;
import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.util.CommonUtility;


import java.util.ArrayList;
import java.util.List;


public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ViewHolder> {

    List<BizReviews> mValues = new ArrayList<>();
    String centreId;
    Context mContext;
    /*  List<Integer> mDuration = new ArrayList<>();
      List<Integer> mPrice = new ArrayList<>();*/
 /*   List<List<SearchQatRequest>> mSearchQatRequestList = new ArrayList<>();
    List<List<String>> mSelectedStaffs = new ArrayList<>();*/
    private static ClickListener clickListener;


    public void notifyData(List<BizReviews> items) {
        Log.d("notifyData ", items.size() + "");
        this.mValues = items;
        notifyDataSetChanged();
    }


    public ReviewsListAdapter(Context context, List<BizReviews> items) {
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
                .inflate(R.layout.filter_review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.review.setText(holder.mItem.getReview());
        holder.date.setText(CommonUtility.formatDate(holder.mItem.getCreation().getTime()));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView review, date;
        public BizReviews mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            review = (TextView) view.findViewById(R.id.review);
            date = (TextView) view.findViewById(R.id.date);
            mView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + review.getText() + "'";
        }

    }

    public void setOnItemClickListener(ReviewsListAdapter.ClickListener clickListener) {
        ReviewsListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}
