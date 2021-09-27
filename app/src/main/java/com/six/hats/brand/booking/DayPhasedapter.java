package com.six.hats.brand.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.R;
import com.six.hats.brand.model.ServicesData;

import java.util.ArrayList;
import java.util.List;


public class DayPhasedapter extends RecyclerView.Adapter<DayPhasedapter.ViewHolder> {
    Context context;
    private List<Boolean> mValues = new ArrayList<>();
    private List<String> dayPhases = new ArrayList<>();
    // private final OnListFragmentInteractionListener mListener;
    final int[] row_index = new int[1];

    private static ClickListener clickListener;

    public DayPhasedapter(List<String> dayPhas, List<Boolean> ServicesData, Context mcontext) {
        this.mValues = ServicesData;
        this.dayPhases = dayPhas;
        this.context = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_day_phase_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        switch (dayPhases.get(position)) {

            case "Morning":
                holder.day_img.setImageResource(R.drawable.a);
                holder.phase_name.setText(context.getString(R.string.morning));
                holder.phase_name.setTextColor(context.getResources().getColor(R.color.darkBlue));

                holder.phase_slot.setText("");
                if (mValues.get(position)) {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.cream));
                } else {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }

                break;
            case "Afternoon":
                holder.day_img.setImageResource(R.drawable.b);
                holder.phase_name.setText(context.getString(R.string.afternoon));
                holder.phase_name.setTextColor(context.getResources().getColor(R.color.darkBlue));

                holder.phase_slot.setText("");
                if (mValues.get(position)) {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.cream));
                } else {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }

                break;
            case "Evening":
                holder.day_img.setImageResource(R.drawable.c);
                holder.phase_name.setText(context.getString(R.string.evening));
                holder.phase_name.setTextColor(context.getResources().getColor(R.color.darkBlue));

                holder.phase_slot.setText("");
                if (mValues.get(position)) {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.cream));
                } else {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }

                break;
            case "Night":
                holder.day_img.setImageResource(R.drawable.d);
                holder.phase_name.setText(context.getString(R.string.night));
                holder.phase_name.setTextColor(context.getResources().getColor(R.color.pure_white));

                holder.phase_slot.setText("");
                if (mValues.get(position)) {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.cream));
                } else {
                    holder.selectionView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }

                break;

        }


    }

    @Override
    public int getItemCount() {
        return dayPhases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView, selectionView;
        public final TextView phase_slot;
        public final TextView phase_name;
        public ServicesData mItem;
        public ImageView day_img;
        //     public ImageView checkBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            phase_slot = (TextView) view.findViewById(R.id.phase_slot);
            phase_name = (TextView) view.findViewById(R.id.phase_name);
            //   checkBox = (ImageView) view.findViewById(R.id.checkb);
            selectionView = (View) view.findViewById(R.id.selectionView);

            day_img = (ImageView) view.findViewById(R.id.day_img);
            mView.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + phase_name.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DayPhasedapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

}
