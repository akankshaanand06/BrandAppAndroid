package com.six.hats.brand.booking;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.R;
import com.six.hats.brand.model.booking.AppontmentEnitities;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class ApptsDetailItemAdapter extends RecyclerView.Adapter<ApptsDetailItemAdapter.ViewHolder> {
    Context context;
    private List<AppontmentEnitities> mValues = new ArrayList<>();
    // private final OnListFragmentInteractionListener mListener;
    final int[] row_index = new int[1];
    String mBookingId;
    private static ClickListener clickListener;

    public ApptsDetailItemAdapter(String bookingId, List<AppontmentEnitities> ServicesData, Context mcontext) {
        this.mValues = ServicesData;
        this.context = mcontext;
        this.mBookingId = bookingId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appt_details_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        List<String> services = new ArrayList<>();
        for (int i = 0; i < holder.mItem.getServiceModelList().size(); i++) {
            services.add(holder.mItem.getServiceModelList().get(i).getServiceName());
        }
        holder.service_name.setText((Html.fromHtml("<font color=\"#ffffff\">" + "Services: " + "</font>" + CommonUtility.convertListToCommaString(services))));

        holder.selectedStaff.setText((Html.fromHtml("<font color=\"#ffffff\">" + "Professional/Specialist: " + "</font>" + holder.mItem.getStaffName())));
        holder.service_duration.setText(/*context.getString(R.string.duration) +*/holder.mItem.getTotalServiceTime() + " " + context.getString(R.string.mins));

        holder.serviceTime.setText((Html.fromHtml("<font color=\"#ffffff\">" + "Time: " + "</font>" + CommonUtility.formatSpan(holder.mItem.getBookingSlot().getStartSpan().getHour(), holder.mItem.getBookingSlot().getStartSpan().getMinutes()))));

        if (mValues.size() == 1) {
            holder.delete_service.setVisibility(View.GONE);
        }
        holder.delete_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.showAlertSingleButton((AppCompatActivity) context, context.getString(R.string.app_name), "Are you sure you want to cancel this service?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mValues.size() > 1) {
                            cancelService(mBookingId, holder.mItem.getAppointmentBaseEntityId(), position);

                        } else {
                            cancelService(mBookingId, "NA", position);

                        }
                    }
                });
            }
        });
        try {
            String string = "\u20B9";
            byte[] utf8 = null;
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
            holder.service_cost.setText(/*context.getString(R.string.rate) + */  string + holder.mItem.getServiceAmount());

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
        public final TextView service_name, service_duration;
        public final TextView service_cost, selectedStaff, serviceTime;
        public AppontmentEnitities mItem;
        public LinearLayout ll;
        public ImageView delete_service;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            service_name = (TextView) view.findViewById(R.id.service_name);
            service_duration = (TextView) view.findViewById(R.id.ets);
            service_cost = (TextView) view.findViewById(R.id.fee);
            selectedStaff = (TextView) view.findViewById(R.id.selectedStaff);
            serviceTime = view.findViewById(R.id.serviceTime);
            delete_service = (ImageView) view.findViewById(R.id.delete_service);

            mView.setOnClickListener(null);

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
        ApptsDetailItemAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    /**
     * Update the status of the Ongoing Booking
     *
     * @param
     * @param bookingID
     * @param mEntityId
     */
    public void cancelService(final String bookingID, final String mEntityId, final int pos) {

        CommonUtility.showProgressLoader((AppCompatActivity) context, "Cancelling service....", context.getResources().getString(R.string.app_name));

        CentralApis.getInstance().getBookingAPIS().cancelService(PrefsWrapper.with(context).getString(JullayConstants.KEY_USER_ID, ""), bookingID,
                mEntityId, PrefsWrapper.with(context).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (response.isSuccessful()) {
                    final BasicResponse data = response.body();
                    CommonUtility.closeProgressLoader();
                    if (data.getStatus()) {
                        if (mEntityId.equalsIgnoreCase("NA")) {
                            ((AppCompatActivity) context).finish();
                        } else {
                            mValues.remove(pos);
                            notifyDataSetChanged();
                            CommonUtility.showErrorAlert(context, "Service Cancelled");
                        }

                    } else {

                        CommonUtility.showErrorAlert(context, data.getMessage());
                    }

                } else {
                    CommonUtility.closeProgressLoader();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(context);
                        } else {
                            CommonUtility.showErrorAlert(context, jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(context, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                CommonUtility.closeProgressLoader();
                if (!CommonUtility.haveNetworkConnection(context)) {
                    CommonUtility.showAlertRetryCancel(context, context.getString(R.string.app_name), context.getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            cancelService(bookingID, mEntityId, pos);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(context, context.getString(R.string.network_error_text));

                }
            }
        });

    }

}
