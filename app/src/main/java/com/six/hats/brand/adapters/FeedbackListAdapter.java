package com.six.hats.brand.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.Feedbacks;
import com.six.hats.brand.R;
import com.six.hats.brand.model.FeedBackResponse;

import java.util.ArrayList;
import java.util.List;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.FilterViewHolder> {
    private List<FeedBackResponse> horizontalFilterList;
    List<Feedbacks.ALL> quesList;
    Context context;
    private static FeedbackListAdapter.ClickListener clickListener;

    public FeedbackListAdapter(List<FeedBackResponse> horizontalFiltersList, List<Feedbacks.ALL> mquesList, Context context) {
        this.horizontalFilterList = horizontalFiltersList;
        this.quesList = mquesList;
        this.context = context;
    }

    public void notifyData(List<FeedBackResponse> items, List<Feedbacks.ALL> mquesList) {
        Log.d("notifyData ", items.size() + "");
        this.horizontalFilterList = items;
        this.quesList = mquesList;
        notifyDataSetChanged();
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feedback_item, parent, false);
        FilterViewHolder gvh = new FilterViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, final int position) {


        String key = (new ArrayList<String>(horizontalFilterList.get(position).getCustomerFeedBack().keySet())).get(0);

        holder.feedback_ques.setText(key);
        String value = (new ArrayList<String>(horizontalFilterList.get(position).getCustomerFeedBack().values())).get(0);
        holder.feedback_ans.setText(value);


    }


    @Override
    public int getItemCount() {
        return horizontalFilterList.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView user_img;
        View mView;
        TextView feedback_ques, feedback_ans;
        TextView cost;
        RatingBar ratingBar;

        public FilterViewHolder(View view) {
            super(view);
            mView = view;
            feedback_ques = view.findViewById(R.id.feedback_ques);
            feedback_ans = view.findViewById(R.id.feedback_ans);

            mView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(FeedbackListAdapter.ClickListener clickListener) {
        FeedbackListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }
}