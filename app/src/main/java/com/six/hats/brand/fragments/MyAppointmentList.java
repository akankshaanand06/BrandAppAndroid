package com.six.hats.brand.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.R;
import com.six.hats.brand.adapters.MyappointmntAdapter;
import com.six.hats.brand.model.booking.Appointment;
import com.six.hats.brand.model.booking.AppointmentsItems;
import com.six.hats.brand.model.booking.BookingLstDetails;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyAppointmentList extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnListFragmentInteractionListener mListener;
    List<AppointmentsItems> appointmentsItems = new ArrayList<>();
    MyappointmntAdapter adapter;
    private ProgressBar mProgress;
    List<Appointment> bookingsList = new ArrayList<>();
    private RecyclerView apponmt_list;
    private RelativeLayout no_data;
    private TextView no_data_text;
    int selectedPage = 1;//upcoming
    private TextView upcoming_lst, history_list;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyAppointmentList() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyAppointmentList newInstance(int columnCount) {
        MyAppointmentList fragment = new MyAppointmentList();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // TODO: Customize parameters
            int mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointmntitem_list, container, false);
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((Html.fromHtml(title_color + getString(R.string.my_appointments) + "</font>")));

        upcoming_lst = view.findViewById(R.id.upcoming_lst);
        history_list = view.findViewById(R.id.history_list);
        no_data = view.findViewById(R.id.no_appt_lay);
        no_data_text = view.findViewById(R.id.no_data_text);
        //   upcoming_lst.setBackground(getResources().getDrawable(R.drawable.rounded_textview_selected));
        //  history_list.setBackground(getResources().getDrawable(R.drawable.rounded_textview_normal));

        upcoming_lst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPage = 1;
                loadBooking(selectedPage);

            }
        });
        history_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPage = 2;
                loadBooking(selectedPage);

            }
        });
       /* Button book = view.findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
*/
        // Set the adapter

        Context context = view.getContext();
        apponmt_list = (RecyclerView) view.findViewById(R.id.apponmt_list);
        mProgress = view.findViewById(R.id.insidePB);

        apponmt_list.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyappointmntAdapter(bookingsList, context);
        apponmt_list.setAdapter(adapter);
        //  DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        //  itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        // apponmt_list.addItemDecoration(itemDecorator);
        adapter.setOnItemClickListener(new MyappointmntAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectedPage != 2) {
                    //todo
                   /* Intent appotmnt_detail = new Intent(getActivity(), AppointmentDetailsActivity.class);
                    appotmnt_detail.putExtra("BID", bookingsList.get(position).getAppointmentId());

                    appotmnt_detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ActivityOptions options =
                            ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_left, R.anim.slide_in_right);
                    startActivity(appotmnt_detail, options.toBundle());*/
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        //populateFiltersList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedPage = 1;
        loadBooking(selectedPage);

    }

    private void loadBooking(int i) {
        switch (i) {
            case 1: // Upcoming
                loadMyBookingDetails();
                break;
            case 2: // Historic
                loadHistoricBookingData();
                break;

        }
    }

    private void populateFiltersList() {
        AppointmentsItems staff_1 = new AppointmentsItems("Sparkle Salon", "Haircut");
        AppointmentsItems staff_2 = new AppointmentsItems("Dr. Sharma Clinic", "Gynaecologist");


        if (appointmentsItems.size() == 0) {
            appointmentsItems.add(staff_1);
            appointmentsItems.add(staff_2);


        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AppointmentsItems item);
    }

    /**
     * Get My Favs centres list
     */
    private void loadMyBookingDetails() {
        mProgress.setVisibility(View.VISIBLE);

        CentralApis.getInstance().getAPIS().loadMyBookingData(PrefsWrapper.with(getContext()).getString(JullayConstants.KEY_USER_ID, "0"), PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BookingLstDetails>() {
            @Override
            public void onResponse(Call<BookingLstDetails> call, Response<BookingLstDetails> response) {
                if (response.isSuccessful()) {
                    mProgress.setVisibility(View.GONE);
                    BookingLstDetails myBookingsResponse = response.body();
                    if (myBookingsResponse.getAppointmentList() != null && myBookingsResponse.getAppointmentList().size() != 0) {
                        bookingsList.clear();
                        bookingsList.addAll(myBookingsResponse.getAppointmentList());
                        adapter.notifyDataSetChanged();
                        no_data.setVisibility(View.GONE);
                        apponmt_list.setVisibility(View.VISIBLE);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                        no_data_text.setText(getString(R.string.no_appointms));
                        apponmt_list.setVisibility(View.GONE);
                    }
                   // upcoming_lst.setBackground(getResources().getDrawable(R.drawable.rounded_textview_selected));
                    upcoming_lst.setTextColor(getResources().getColor(R.color.pure_white));
                //    history_list.setBackground(getResources().getDrawable(R.drawable.rounded_textview_normal));
                    history_list.setTextColor(getResources().getColor(R.color.pure_white));


                } else {
                    mProgress.setVisibility(View.GONE);
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(getActivity());
                        } else {
                            CommonUtility.showErrorAlert(getActivity(), jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getContext(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingLstDetails> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadMyBookingDetails();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });
    }


    /**
     * Get My Favs centres list
     */
    private void loadHistoricBookingData() {
        mProgress.setVisibility(View.VISIBLE);

        CentralApis.getInstance().getAPIS().loadHistoricBookingData(PrefsWrapper.with(getContext()).getString(JullayConstants.KEY_USER_ID, "0"), "COMPLETED", PrefsWrapper.with(getActivity()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<BookingLstDetails>() {
            @Override
            public void onResponse(Call<BookingLstDetails> call, Response<BookingLstDetails> response) {
                if (response.isSuccessful()) {
                    mProgress.setVisibility(View.GONE);
                    BookingLstDetails myBookingsResponse = response.body();
                    if (myBookingsResponse.getAppointmentList() != null && myBookingsResponse.getAppointmentList().size() != 0) {
                        bookingsList.clear();
                        bookingsList.addAll(myBookingsResponse.getAppointmentList());
                        adapter.notifyDataSetChanged();
                        no_data.setVisibility(View.GONE);
                        apponmt_list.setVisibility(View.VISIBLE);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                        no_data_text.setText(R.string.no_comp_appts);
                        apponmt_list.setVisibility(View.GONE);
                        bookingsList.clear();
                        // adapter.notifyDataSetChanged();

                    }

                 //   upcoming_lst.setBackground(getResources().getDrawable(R.drawable.rounded_textview_normal));
                    upcoming_lst.setTextColor(getResources().getColor(R.color.pure_white));
                    history_list.setTextColor(getResources().getColor(R.color.pure_white));
                  //  history_list.setBackground(getResources().getDrawable(R.drawable.rounded_textview_selected));
                } else {
                    mProgress.setVisibility(View.GONE);
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(getActivity());
                        } else {
                            CommonUtility.showErrorAlert(getActivity(), jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getContext(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingLstDetails> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                if (!CommonUtility.haveNetworkConnection(getActivity())) {
                    CommonUtility.showAlertRetryCancel(getActivity(), getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadHistoricBookingData();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getActivity(), getString(R.string.network_error_text));

                }
            }
        });
    }
}
