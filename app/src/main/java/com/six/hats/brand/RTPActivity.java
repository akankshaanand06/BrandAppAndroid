package com.six.hats.brand;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.six.hats.brand.booking.ApptsDetailItemAdapter;
import com.six.hats.brand.model.booking.Appointment;
import com.six.hats.brand.model.booking.AppontmentEnitities;
import com.six.hats.brand.model.booking.LiveBookingResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class RTPActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener {
    AppCompatActivity activity;
    private ProgressBar mInsideProgressBar;
    private AnimationDrawable animationDrawable;
    TextView rtp_text, centre_name, delay_tv, uqat_tv, uchk_in_tv, bqat_tv, booked_tv;
    public String mCheckinTime = "", mDelayMins = "5", mQueuePersons = "0", mUAT = "", mMinsLeft = "0";
    private TextToSpeech text2Speech;
    private static final long FIVE_MINS_IN_MILLIS = 5 * 60 * 1000;
    private ImageView rtp_imgview;
    private TextView date;
    private ApptsDetailItemAdapter detailsAdapter;
    private List<AppontmentEnitities> bookingsList = new ArrayList<>();
    private String bookingID;
    private Appointment bookingsResponse;
    private List<Appointment> bookingObject = null;
    private Handler RTPhandler = null;
    private Runnable runnableCode = null;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtp);

        activity = this;
        bookingID = getIntent().getExtras().getString("bookingID", "");
        bookingObject = (List<Appointment>) getIntent().getExtras().getSerializable("bookingObject");

        mInsideProgressBar = findViewById(R.id.insidePB);

        text2Speech = new TextToSpeech(this, this);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        rtp_text = (believe.cht.fadeintextview.TextView) findViewById(R.id.rtp_tv);
        centre_name = (TextView) findViewById(R.id.centre_name);
        // close_rtp = (TextView) findViewById(R.id.close_rtp);
        date = findViewById(R.id.date);
        RecyclerView appt_details_list = findViewById(R.id.appt_details_list);
        appt_details_list.setNestedScrollingEnabled(false);
        appt_details_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        detailsAdapter = new ApptsDetailItemAdapter("", bookingsList, getApplicationContext());
        appt_details_list.setAdapter(detailsAdapter);
      /*  delay_tv = (believe.cht.fadeintextview.TextView) findViewById(R.id.delay_tv);
        uqat_tv = (believe.cht.fadeintextview.TextView) findViewById(R.id.uqat_tv);

        uchk_in_tv = (believe.cht.fadeintextview.TextView) findViewById(R.id.uchk_in_tv);
        booked_tv = (believe.cht.fadeintextview.TextView) findViewById(R.id.booked_tv);*/


        //Real time progress animation code------------------
        try {
            rtp_imgview = findViewById(R.id.rtp_image);
            /*if (rtp_imgview == null) throw new AssertionError();
            rtp_imgview.setBackgroundResource(R.drawable.rtp_animation);

            animationDrawable = (AnimationDrawable) rtp_imgview.getBackground();

            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* close_rtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    System.gc();
                    ////user is loggged in land on home - and has no bookings
                    Intent i = new Intent(RTPActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityOptions options =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_in_right);
                    startActivity(i, options.toBundle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });*/

        if (bookingObject == null || bookingObject.size() == 0) {
            loadMyBookingDetails();
        } else {
            loadLiveStatusData(bookingObject.get(0).getAppointmentId());
            centre_name.setText("@ " + bookingObject.get(0).getAppontmentEnitities().get(0).getBranchName());
            String bookingDate = bookingObject.get(0).getAppontmentEnitities().get(0).getBookingSlot().getDate();
            date.setText(CommonUtility.getISOformatToNormalDate(bookingDate));
            //      bookingsList.addAll(currentBookingDetails.getAppontmentEnitities());
            bookingsList.clear();
            bookingsList.addAll(bookingObject.get(0).getAppontmentEnitities());
            detailsAdapter.notifyDataSetChanged();
        }


    }


    /**
     * load booking detailed from booking id
     */
    private void loadMyBookingDetails() {
        mInsideProgressBar.setVisibility(View.VISIBLE);
        CentralApis.getInstance().getBookingAPIS().loadBookingByID(bookingID.replace("temp_", ""), PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    try {
                        mInsideProgressBar.setVisibility(View.GONE);
                        bookingsResponse = response.body();
                        loadLiveStatusData(bookingsResponse.getAppointmentId());
                        centre_name.setText("@ " + bookingsResponse.getAppontmentEnitities().get(0).getBranchName());
                        String bookingDate = bookingsResponse.getAppontmentEnitities().get(0).getBookingSlot().getDate();
                        date.setText(CommonUtility.getISOformatToNormalDate(bookingDate));
                        //      bookingsList.addAll(currentBookingDetails.getAppontmentEnitities());
                        bookingsList.clear();
                        bookingsList.addAll(bookingsResponse.getAppontmentEnitities());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    mInsideProgressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(activity);
                        } else {
                            CommonUtility.showErrorAlert(activity, jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(activity, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                mInsideProgressBar.setVisibility(View.GONE);
                if (!CommonUtility.haveNetworkConnection(activity)) {
                    CommonUtility.showAlertRetryCancel(activity, getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadMyBookingDetails();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(activity, getString(R.string.network_error_text));

                }
            }
        });
    }


    /**
     * Check live booking status
     *
     * @param bookingID
     */
    private void loadLiveStatusData(final String bookingID) {
        CentralApis.getInstance().getAPIS().loadLiveStatus(PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_ID, ""), bookingID, PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<LiveBookingResponse>() {
            @Override
            public void onResponse(Call<LiveBookingResponse> call, Response<LiveBookingResponse> response) {

                if (response.isSuccessful()) {

                    LiveBookingResponse data = response.body();
                   /* if (data.getAdvance()) {
                        if (!animationDrawable.isRunning()) {
                            animationDrawable.stop();
                        }

                        ////user is loggged in land on home - and has no bookings
                        Intent i = new Intent(RTPActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ActivityOptions options =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_in_right);
                        startActivity(i, options.toBundle());

                        finish();
                    } else {*/


                    startRealTimeProgressCycle(data);
                    //}


                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.getString("message").contains("Unauthorized")) {
                            CommonUtility.autoLogin(getApplicationContext());
                        } else {
                            CommonUtility.showErrorAlert(getApplicationContext(), jObjError.getString("message"));
                        }
                    } catch (Exception e) {
                        CommonUtility.showErrorAlert(getApplicationContext(), e.getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<LiveBookingResponse> call, Throwable t) {
                if (!CommonUtility.haveNetworkConnection(getApplicationContext())) {
                    CommonUtility.showAlertRetryCancel(activity, getString(R.string.app_name), getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            loadMyBookingDetails();
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(getApplicationContext(), getString(R.string.network_error_text));

                }
            }
        });

    }

    private void startRealTimeProgressCycle(LiveBookingResponse data) {
        mInsideProgressBar.setVisibility(View.GONE);

//todo
       /*----- if (!CommonUtility.isTodaysBooking(data.getAppointment().getAppontmentEnitities().get(0).getBookingSlot().getDate())) {
            //if appointment is advance then
            rtp_imgview.setImageResource(R.drawable.rtp_9);
            rtp_text.setVisibility(View.VISIBLE);
            String rtpText;
            rtpText = "Hi! " + PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAME, "") + "." + " Live queue progress of your turn will be available from tomorrow.";
            rtp_text.setText(rtpText);
            text2Speech.speak(rtpText, TextToSpeech.QUEUE_FLUSH, null);
            return;
        }------*/

        /*-------switch (Integer.valueOf(data.getPeopleAheadCount())) {
            case 0:
                rtp_imgview.setImageResource(R.drawable.rtp_9);
                break;
            case 1:
                rtp_imgview.setImageResource(R.drawable.rtp_8);
                break;
            case 2:
                rtp_imgview.setImageResource(R.drawable.rtp_7);
                break;
            case 3:
                rtp_imgview.setImageResource(R.drawable.rtp_6);
                break;
            case 4:
                rtp_imgview.setImageResource(R.drawable.rtp_5);
                break;
            case 5:
                rtp_imgview.setImageResource(R.drawable.rtp_4);
                break;
            case 6:
                rtp_imgview.setImageResource(R.drawable.rtp_3);
                break;
            case 7:
                rtp_imgview.setImageResource(R.drawable.rtp_2);
                break;
            default:
                rtp_imgview.setImageResource(R.drawable.rtp_1);
                break;
        }-------*/

        //  displayRTP(0, data, true);
        displayRtpHandler(0, data, true);
    }


/*
    public void displayRTP(int pos, final LiveBookingResponse data, boolean isFirstDisplay) {
        final String[] rtpText = new String[1];
        try {
            String mCheckinTime = "", mDelayMins = "5", mQueuePersons = "0", mUAT = "", mMinsLeft = "0";

            int mCt;
            int bookedHr = data.getCurrentSlot().getEndSpan().getHour();
            int bookedMin = data.getCurrentSlot().getEndSpan().getMinutes();


            if (bookedMin < 55) {
                mCt = bookedMin + 5;
            } else {
                mCt = bookedMin;
            }

            mCheckinTime = data.getCurrentSlot().getStartSpan().getHour() + ":" + mCt;

            mUAT = data.getCurrentSlot().getStartSpan().getHour() + ":" + data.getCurrentSlot().getStartSpan().getMinutes();

            final int[] rtp_pos = {pos};
            if (isFirstDisplay) {
                if (rtp_pos[0] == 0) {
                    rtp_text.setVisibility(View.VISIBLE);
                    rtpText[0] = "Hi! " + PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAME, "") + "." + " There are " + data.getPeopleAheadCount() + " People Ahead of you.";
                    rtp_text.setText(rtpText[0]);
                    text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);
                    rtp_pos[0] = rtp_pos[0] + 1;

                }

            }
            final String finalMCheckinTime = mCheckinTime;
            thread = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {

                            Thread.sleep(7000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (rtp_pos[0]) {
                                        case 0:

                                            rtp_text.setVisibility(View.VISIBLE);
                                            rtpText[0] = "Hi! " + PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAME, "") + "." + " There are " + data.getPeopleAheadCount() + " People Ahead of you.";
                                            rtp_text.setText(rtpText[0]);
                                            text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);
                                            int i0 = rtp_pos[0] + 1;
                                            displayRTP(i0, data, false);

                                            break;
                                        case 1:
                                            rtp_text.setVisibility(View.VISIBLE);

                                            if (Integer.parseInt(data.getDelayOnIt()) > 0) {
                                                rtpText[0] = "As there is a DELAY of " + data.getDelayOnIt() + " mins, your NEW Check-in Time is " + finalMCheckinTime;
                                                rtp_text.setText(rtpText[0]);
                                            } else {
                                                rtpText[0] = "There is NO Delay with your reservation.";
                                                rtp_text.setText(rtpText[0]);
                                            }
                                            text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                                            int i1 = rtp_pos[0] + 1;
                                            displayRTP(i1, data, false);
                                            break;
                                       */
/* case 2:
                                            rtp_text.setVisibility(View.VISIBLE);
                                            rtpText[0] = "With " + CommonUtility.formatDuration(diff) + " minutes left for CHECK-IN, do follow Live Queue Progress.";
                                            rtp_text.setText(rtpText[0]);
                                            text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                                            int i2 = rtp_pos[0] + 1;c
                                            displayRTP(i2, data);
                                            break;*//*

                                        case 2:
                                            rtp_text.setVisibility(View.VISIBLE);
                                            rtpText[0] = "We are eagerly waiting for your visit.";
                                            rtp_text.setText(rtpText[0]);
                                            text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                                            int i3 = 0;
                                           displayRTP(i3, data, false);
                                            break;
                                        case 3:
                                            stopThread();

                                            */
    /*  *//*
     */
/* if (animationDrawable.isRunning()) {
                                                animationDrawable.stop();
                                            }*//*
     */
/*
                                            rtp_text.setVisibility(View.VISIBLE);
                                            rtpText[0] = "Please press Jullay button to go back to Home Page.";

                                            rtp_text.setText(rtpText[0]);
                                            text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                                            int i4 = rtp_pos[0] + 1;*//*


                                            break;
                                        case 4:
                                            stopThread();
                                            break;

                                    }


                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        stopThread();
                        e.printStackTrace();
                    }
                }
            };
            if (!thread.isAlive()) {
                System.gc();
                thread.start();//todo crashed     java.lang.OutOfMemoryError: Could not allocate JNI Env: Failed anonymous mmap(0x0, 8192, 0x3, 0x2, 45, 0): Operation not permitted. See process maps in the log.

            }

            //-----------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public void displayRtpHandler(int pos, final LiveBookingResponse data, boolean isFirstDisplay) {
        final String[] rtpText = new String[1];

        String mCheckinTime = "", mDelayMins = "5", mQueuePersons = "0", mUAT = "", mMinsLeft = "0";

        int mCt;
        int bookedHr = data.getCurrentSlot().getEndSpan().getHour();
        int bookedMin = data.getCurrentSlot().getEndSpan().getMinutes();


        if (bookedMin < 55) {
            mCt = bookedMin + 5;
        } else {
            mCt = bookedMin;
        }

        mCheckinTime = data.getCurrentSlot().getStartSpan().getHour() + ":" + mCt;

        mUAT = data.getCurrentSlot().getStartSpan().getHour() + ":" + data.getCurrentSlot().getStartSpan().getMinutes();

        final int[] rtp_pos = {pos};
        if (isFirstDisplay) {
            if (rtp_pos[0] == 0) {
                rtp_text.setVisibility(View.VISIBLE);
                rtpText[0] = "Hi! " + PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAME, "") + "." + " There are " + data.getPeopleAheadCount() + " People Ahead of you.";
                rtp_text.setText(rtpText[0]);
                text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);
                rtp_pos[0] = rtp_pos[0] + 1;

            }

        }
        final String finalMCheckinTime = mCheckinTime;

        // Create the Handler object (on the main thread by default)
        RTPhandler = new Handler();
        // Define the code block to be executed
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object

                switch (rtp_pos[0]) {
                    case 1:

                        rtp_text.setVisibility(View.VISIBLE);
                        rtpText[0] = "Hi! " + PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAME, "") + "." + " There are " + data.getPeopleAheadCount() + " People Ahead of you.";
                        rtp_text.setText(rtpText[0]);
                        text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);
                        rtp_pos[0] = rtp_pos[0] + 1;
                        // displayRTP(i0, data, false);
                        // displayRtpHandler(i0, data, false);
                        break;
                    case 2:
                        rtp_text.setVisibility(View.VISIBLE);

                        if (Integer.parseInt(data.getDelayOnIt()) > 0) {
                            rtpText[0] = "As there is a DELAY of " + data.getDelayOnIt() + " mins, your NEW Check-in Time is " + finalMCheckinTime;
                            rtp_text.setText(rtpText[0]);
                        } else {
                            rtpText[0] = "There is NO Delay with your reservation.";
                            rtp_text.setText(rtpText[0]);
                        }
                        text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                        rtp_pos[0] = rtp_pos[0] + 1;
                        // displayRTP(i1, data, false);
                        // displayRtpHandler(i1, data, false);
                        break;

                    case 3:
                        rtp_text.setVisibility(View.VISIBLE);
                        rtpText[0] = "We are eagerly waiting for your visit.";
                        rtp_text.setText(rtpText[0]);
                        text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                        rtp_pos[0] = 1;
                        // displayRTP(i3, data, false);
                        //displayRtpHandler(i3, data, false);

                        break;


                }
                RTPhandler.postDelayed(this, 7000);


            }
        };
// Start the initial runnable task by posting through the handler
        RTPhandler.post(runnableCode);


       /* countDownTimer = new CountDownTimer(42000, 7000) {

            public void onTick(long millisUntilFinished) {
                switch (rtp_pos[0]) {
                    case 0:

                        rtp_text.setVisibility(View.VISIBLE);
                        rtpText[0] = "Hi! " + PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_NAME, "") + "." + " There are " + data.getPeopleAheadCount() + " People Ahead of you.";
                        rtp_text.setText(rtpText[0]);
                        text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);
                        int i0 = rtp_pos[0] + 1;
                        // displayRTP(i0, data, false);
                       // displayRtpHandler(i0, data, false);
                        break;
                    case 1:
                        rtp_text.setVisibility(View.VISIBLE);

                        if (Integer.parseInt(data.getDelayOnIt()) > 0) {
                            rtpText[0] = "As there is a DELAY of " + data.getDelayOnIt() + " mins, your NEW Check-in Time is " + finalMCheckinTime;
                            rtp_text.setText(rtpText[0]);
                        } else {
                            rtpText[0] = "There is NO Delay with your reservation.";
                            rtp_text.setText(rtpText[0]);
                        }
                        text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                        int i1 = rtp_pos[0] + 1;
                        // displayRTP(i1, data, false);
                        //displayRtpHandler(i1, data, false);
                        break;
                    case 2:
                        rtp_text.setVisibility(View.VISIBLE);
                        rtpText[0] = "We are eagerly waiting for your visit.";
                        rtp_text.setText(rtpText[0]);
                        text2Speech.speak(rtpText[0], TextToSpeech.QUEUE_FLUSH, null);

                        int i3 = 0;
                        // displayRTP(i3, data, false);
                       // displayRtpHandler(i3, data, false);

                        break;

                    case 3:
                        stopThread();
                        break;
                    case 4:
                        stopThread();
                        break;
                }
            }

            public void onFinish() {
                countDownTimer.cancel();
            }
        };

        countDownTimer.start();*/
    }


    public void onPause() {
        /*if (text2Speech != null) {
            text2Speech.stop();
            text2Speech.shutdown();
        }*/
        super.onPause();
    }

    private void stopThread() {
        /*if (thread != null) {
            thread.interrupt();
            thread = null;
        }*/
        if (RTPhandler != null) {
            if (runnableCode != null)
                RTPhandler.removeCallbacks(runnableCode);
        }


    }

    public void onDestroy() {
        stopThread();

        super.onDestroy();
    }


    public long fiveMinutesAgo(String epochDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long time = 0;
        try {
            time = df.parse(epochDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time - FIVE_MINS_IN_MILLIS;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        stopThread();
        finish();
        super.onBackPressed();
    }


    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            text2Speech.setLanguage(Locale.UK);
            text2Speech.setSpeechRate(0.85f);


        }
    }
}
