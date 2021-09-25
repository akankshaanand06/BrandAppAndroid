package com.six.hats.brand.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.six.hats.brand.R;
import com.six.hats.brand.model.LoginResponse;
import com.six.hats.brand.networks.CentralApis;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class CommonUtility {

    public enum Education {
        HIGH_SCHOOL, INTERMEDIATE, Graduate, POST_Graduate;
    }


    public static final short REQUEST_CODE_SELECT_IMAGE = 10004;
    public static final short REQUEST_CODE_SELECT_IMAGE_GALLERY = 10005;
    private static ProgressDialog progressDialog;
    private static AlertDialog alertDialog;

    public static String convertListToCommaString(List<String> names) {
        try {
            StringBuilder namesStr = new StringBuilder();
            for (String name : names) {
                namesStr = namesStr.length() > 0 ? namesStr.append(", ").append(name) : namesStr.append(name);
            }
            return namesStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String convertListToCommaStringLineWise(List<String> names) {
        try {
            StringBuilder namesStr = new StringBuilder();
            for (String name : names) {
                namesStr = namesStr.length() > 0 ? namesStr.append("\n").append(name) : namesStr.append(name);
            }
            return namesStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatTimeOnly(long str) {


        DateFormat df = new SimpleDateFormat("hh:mm a");

        // Long ltym = Long.valueOf(str);
        Date date = new Date(str);
        TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        df.setTimeZone(t);
        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);


        //Converting milliseconds to Date using Calendar
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(str);
        System.out.println("Milliseconds to Date using Calendar:"
                + df.format(cal.getTime()));
        //System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return df.format(cal.getTime());
    }

    public static long getTimestampFromSpan(String hr, String mins, String time) {

        long timestampLong = Long.parseLong(time);
        Date d = new Date(timestampLong);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hr));
        c.set(Calendar.MINUTE, Integer.parseInt(mins));
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long updatedTime = c.getTimeInMillis();

        return updatedTime;
    }

    /**
     * Alert to be shown when api calls fails or some error occurs
     *
     * @param context
     */
    public static void showErrorAlert(final Context context, final String message) {

        try {

            /*View rootView = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(android.R.id.content);

            Snackbar.make(rootView.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();*/

            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(R.string.app_name);
                builder.setMessage(message);
                builder.setCancelable(true).
                        setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (!((Activity) context).isInMultiWindowMode()) {
                        alertDialog.show();
                    }
                } else {
                    alertDialog.show();
                }


            } catch (Exception e) {
                e.printStackTrace();


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static AlertDialog showAlertSingleButton(AppCompatActivity context, String title, String message,
                                                    DialogInterface.OnClickListener positiveButtonClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(R.string.app_name);
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setCancelable(false).setPositiveButton(context.getString(R.string.ok), positiveButtonClickListener)
                    .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!((Activity) context).isInMultiWindowMode()) {
                    alertDialog.show();
                }
            } else {
                alertDialog.show();
            }

            return alertDialog;

        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }

    }


    public static AlertDialog showSingleButton(AppCompatActivity context, String title, String message,
                                               DialogInterface.OnClickListener positiveButtonClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(R.string.app_name);
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setCancelable(false).setPositiveButton(context.getString(R.string.ok), positiveButtonClickListener);

            AlertDialog alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!((Activity) context).isInMultiWindowMode()) {
                    alertDialog.show();
                }
            } else {
                alertDialog.show();
            }

            return alertDialog;

        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }

    }

    /**
     * snack bar to be shown some error occurs
     *
     * @param context
     */
    public static void showShortErrorAlert(final Context context, final String message) {

        try {

            View rootView = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(android.R.id.content);

            Snackbar.make(rootView.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showAlertRetryCancel(Context context, String title, String message,
                                            DialogInterface.OnClickListener positiveButtonClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(R.string.app_name);
            builder.setMessage(Html.fromHtml("<font color=\"#FF5252\">" + message + "</font>"));

            builder.setCancelable(false).setPositiveButton(context.getString(R.string.retry), positiveButtonClickListener)
                    .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                                alertDialog = null;
                            }
                        }
                    });

            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!((Activity) context).isInMultiWindowMode()) {
                    alertDialog.show();
                }
            } else {
                alertDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static StringBuilder formatSpan(int hour, int min) {

        String format = "";
        if (hour == 0) {
            // hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            // hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        StringBuilder time = new StringBuilder().append(hour).append(':')
                .append(min).append(" ").append(format);

        return time;
    }

    public static void showAlertOK(Context context, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(context.getString(R.string.app_name));
            builder.setMessage(message);

            builder.setCancelable(true).setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (alertDialog != null) {
                        alertDialog = null;
                    }
                }
            });

            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!((Activity) context).isInMultiWindowMode()) {
                    alertDialog.show();
                }
            } else {
                alertDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showAlertOKSpanned(Context context, String title, Spanned message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(title);
            builder.setMessage(message);

            builder.setCancelable(true).setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!((Activity) context).isInMultiWindowMode()) {
                    alertDialog.show();
                }
            } else {
                alertDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showAlertOKSpannedListner(Context context, String title, Spanned message,
                                                 DialogInterface.OnClickListener positiveButtonClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(title);
            builder.setMessage(message);

            builder.setCancelable(true).setPositiveButton(context.getString(R.string.ok), positiveButtonClickListener);

            AlertDialog alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!((Activity) context).isInMultiWindowMode()) {
                    alertDialog.show();
                }
            } else {
                alertDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * replace with blank if data not present
     *
     * @param text
     * @return
     */
    public static String correctStringNull(String text) {
        if (text != null) {
            if (text == null || text.length() == 0 || text.equals("null")) {
                text = "";
            }
        } else {
            text = "";
        }

        return text;
    }

    public static boolean isEmailValid(String email) {
        if (null == email) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Za-z])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    /**
     * validation in edit text to check space null and enter
     *
     * @param text
     * @return
     */
    public static Boolean chkString(String text) {

        //returns true if string is presnet
        if (TextUtils.isEmpty(text) || text.length() == 0 || text.equals(" ")
                || text == null || text.equals("\n") || text.equalsIgnoreCase("null")) {

            return false;
        } else return !(text.length() > 0 && text.startsWith(" "));


    }


    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatTime(long str) {
        DateFormat df = new SimpleDateFormat("HH:mm a", Locale.US);
        Long ltym = Long.valueOf(str);
        Date date = new Date(ltym);
        TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        df.setTimeZone(t);

        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);

        System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return reportDate;
    }

    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatTimeUTC(long str) {
        DateFormat df = new SimpleDateFormat("hh:mm a", Locale.US);
        Long ltym = Long.valueOf(str);
        Date date = new Date(ltym);
        TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        //  df.setTimeZone(t);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);

        System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return reportDate;
    }


    public static String get12HrsFormat(String date) {


        SimpleDateFormat formatter_from = new SimpleDateFormat("HH:mm a");
        SimpleDateFormat formatter_to = new SimpleDateFormat("hh:mm a");

        try {
            Date d = formatter_from.parse(date);

            System.out.println(formatter_to.format(d));
            return formatter_to.format(d).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getHrsFromStringDate(String date) {


        SimpleDateFormat formatter_from = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        SimpleDateFormat formatter_to = new SimpleDateFormat("HH");

        try {
            Date d = formatter_from.parse(date);

            System.out.println(formatter_to.format(d));
            return Integer.parseInt(formatter_to.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getMinsFromStringDate(String date) {


        SimpleDateFormat formatter_from = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        SimpleDateFormat formatter_to = new SimpleDateFormat("mm");

        try {
            Date d = formatter_from.parse(date);

            System.out.println(formatter_to.format(d));
            return Integer.parseInt(formatter_to.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }


    }


    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatDate(long str) {

        DateFormat df = new SimpleDateFormat("dd MMM yyyy");

        Long ltym = Long.valueOf(str);
        Date date = new Date(ltym);
        TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        df.setTimeZone(t);
        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);

        System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return reportDate;
    }

    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatDateTimeBoth(long str) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);

        Long ltym = Long.valueOf(str);
        Date date = new Date(ltym);
        TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        df.setTimeZone(t);
        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);

        System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return reportDate;
    }

    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatDate2(long str) {

        DateFormat df = new SimpleDateFormat("dd MMM", Locale.US);

        Long ltym = Long.valueOf(str);
        Date date = new Date(ltym);
        TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        df.setTimeZone(t);
        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);

        System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return reportDate;
    }

    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatFullDate(long str) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.SSSZ", Locale.US);

        Long ltym = Long.valueOf(str);
        Date date = new Date(ltym);
        TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        df.setTimeZone(t);
        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);

        System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return reportDate;
    }

    public static String getISOformatToNormal(String value) {

       /* SimpleDateFormat formatter_from = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.SSSZ");
        SimpleDateFormat formatter_to = new SimpleDateFormat("HH:mm a");
       formatter_to.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date d = formatter_from.parse(date.replaceAll("Z$", "+0000"));
            System.out.println(formatter_to.format(d));
            return formatter_to.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }*/
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat formatter_to = new SimpleDateFormat("HH:mm a");
        formatter_to.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = format.parse(value.replaceAll("Z$", "+0000"));
            System.out.println(date);
            return formatter_to.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }


    public static String getISOformatToNormalDate(String value) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat formatter_to = new SimpleDateFormat("dd MMM yyyy");
        formatter_to.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = format.parse(value.replaceAll("Z$", "+0000"));
            System.out.println(date);
            return formatter_to.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }


    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatLong(long str) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a");

        // Long ltym = Long.valueOf(str);
        Date date = new Date(str);
        // TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        // df.setTimeZone(t);
        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);


        //Converting milliseconds to Date using Calendar
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(str);
       /* System.out.println("Milliseconds to Date using Calendar:"
                + df.format(cal.getTime()));*/
        //System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return df.format(cal.getTime());
    }

    /**
     * @param str
     * @return date format of timestamp
     */
    public static String formatDuration(long str) {


        DateFormat df = new SimpleDateFormat("hh:mm a");

        // Long ltym = Long.valueOf(str);
        Date date = new Date(str);
        // TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
        // df.setTimeZone(t);
        // String formattedDate = new
        // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
        String reportDate = df.format(date);


        //Converting milliseconds to Date using Calendar
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(str);
      /*  System.out.println("Milliseconds to Date using Calendar:"
                + df.format(cal.getTime()));*/
        //System.out.println("json:" + str + "  , reportDate:" + reportDate);
        return df.format(cal.getTime());
    }


    /**
     * String to time stamp
     *
     * @param mtime
     * @return
     */
    public static Long getTimestamp(String mtime) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Date date = null;
        try {
            date = (Date) formatter.parse(mtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * Check end time is not greater than start time
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isTimeAfter(Context mContext, String startTime, String endTime) {
        DateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        try {
            Date date = (Date) formatter.parse(startTime);
            Date date2 = (Date) formatter.parse(endTime);
            if (date2.before(date)) { //Same way you can check with after() method also.
                CommonUtility.showAlertOK(mContext, "Please enter correct time, Closing time cannot be greater than Opening time.");
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            /* if(phone.length() < 10) {*/
            return phone.length() == 10;
        }
        return true;
    }


    /**
     * String to time stamp
     *
     * @param str_date
     * @return
     */
    public static Long getTimestampFromString(String str_date) {

        Timestamp timestamp = null;
        try {
            //2019-10-25 16:50:10
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a", Locale.US);
            Date parsedDate = dateFormat.parse(str_date);
            timestamp = new Timestamp(parsedDate.getTime());
            Long d = timestamp.getTime();
            Long d1 = timestamp.getTime();
        } catch (Exception e) {//this generic but you can control another types of exception
            e.printStackTrace();
        }

        return timestamp.getTime();
    }


    public static String getSubLocalityFrom(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            // add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getSubLocality();
            // add = add + "\n" + obj.getPostalCode();
            // add = add + "\n" + obj.getSubAdminArea();
            //add = add + "\n" + obj.getLocality();
            // add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            return obj.getSubLocality();
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return "-";
    }

    public static void showAlertTwoButtons(Context context, String message, String posbtn, String negBtn,
                                           DialogInterface.OnClickListener positiveButtonClickListener,
                                           DialogInterface.OnClickListener negativeButtonClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(R.string.app_name);
            builder.setMessage(message);

            builder.setCancelable(false).setPositiveButton(posbtn, positiveButtonClickListener)
                    .setNegativeButton(negBtn, negativeButtonClickListener);

            AlertDialog alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!((Activity) context).isInMultiWindowMode()) {
                    alertDialog.show();
                }
            } else {
                alertDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static List<String> splitTime(String startTime, String endTime) {
        List<String> slotsArray = new ArrayList<>();
        /* HashMap<String, String> slotsArray = new HashMap<>();
         *//* long startTime = getTimestampFromString(StartTime); //Get Timestamp
        long endTime = getTimestampFromString(EndTime); //Get Timestamp*//*

        int AddMins = Duration * 60 * 1000;

        while (startTime <= endTime) //Run loop
        {
            slotsArray.put(formatTimeOnly(startTime), String.valueOf(startTime));
            startTime += AddMins; //Endtime check
        }*/


        //--------------------
        try {
            //   String date1 = formatDate(System.currentTimeMillis() + (24 * 60 * 60 * 1000))/*"26/02/2011"*/;
            String time1 = startTime/*formatTime(System.currentTimeMillis())*//*"00:00 AM"*/;
            //     String date2 = formatDate(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
            String time2 = endTime;

            String format = /*"yyyy-MM-dd */"HH:mm a";

            SimpleDateFormat sdf = new SimpleDateFormat(format);


            TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
            sdf.setTimeZone(t);
            // String formattedDate = new
            // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);

            Date dateObj1 = sdf.parse(/*date1 + " " +*/ time1);
            Date dateObj2 = sdf.parse(/*date2 + " " +*/ time2);
            System.out.println("Date Start: " + dateObj1);
            System.out.println("Date End: " + dateObj2);

//Date d = new Date(dateObj1.getTime() + 3600000);


            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {
                Date slot = new Date(dif);
                String reportDate = sdf.format(slot);


                slotsArray.add(reportDate.replace("GMT+05:30 2018", ""));
                //System.out.println("Hour Slot --->" + slot);
                dif += 3600000;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slotsArray;
    }

    public static void showProgressLoader(AppCompatActivity mContext, String message, String title) {
        try {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(message); // Setting Message
            progressDialog.setTitle(title); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void closeProgressLoader() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public static String convertToJson(String name, Object staffSpecQATRequest) {
        Gson gson = new GsonBuilder().create();
        String payloadStr = gson.toJson(staffSpecQATRequest);
        Log.d(name + "--JSON--", payloadStr);
        return payloadStr;
    }




    public static List<String> getListFromString(String stringList) {
        List<String> convertedCountriesList = new ArrayList<>();
        try {
            convertedCountriesList = Arrays.asList(stringList.split(",", -1));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertedCountriesList;
    }


    public static String getIdleDiffernece(long second, long first) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date1 = new Date(first);
            Date date2 = new Date(second);


            long difference = date2.getTime() - date1.getTime();

            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            Log.i("======= Hours", " :: " + hours);
            if ((int) hours != 0) {
                return hours + ":" + (int) min;
            } else {
                return String.valueOf((int) min);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param second duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(String second, String first) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
        try {
            Date date1 = simpleDateFormat.parse(first);
            Date date2 = simpleDateFormat.parse(second);

            long millis = date2.getTime() - date1.getTime();

            if (millis < 0) {
                throw new IllegalArgumentException("Duration must be greater than zero!");
            }
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            millis -= TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            millis -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
            millis -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

            StringBuilder sb = new StringBuilder(64);
            sb.append(days);
            sb.append(" Days ");
            sb.append(hours);
            sb.append(" Hours ");
            sb.append(minutes);
            sb.append(" Minutes ");
            sb.append(seconds);
            sb.append(" Seconds");

            if ((int) hours != 0) {
                return hours + " Hrs, " + (int) minutes;
            } else {
                return String.valueOf((int) minutes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }

    }


    public static String encryption(String strNormalText, String key) {
       /* String seedValue = key;
        String normalTextEnc = "";
        try {
            normalTextEnc = AESHelper.encrypt(seedValue, strNormalText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return normalTextEnc;*/
     /*   String encrypted = "";
        String sourceStr = "This is any source string";
        try {
            encrypted = AESHelper.encrypt(sourceStr);
            Log.d("TEST", "encrypted:" + encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return strNormalText;
    }

    public static String decryption(String strEncryptedText, String key) {
      /*  String seedValue = key;
        String strDecryptedText = "";
        try {
            strDecryptedText = AESHelper.decrypt(seedValue, strEncryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDecryptedText;*/
/*
        String encrypted = strEncryptedText;
        String decrypted = "";
        try {
            decrypted = AESHelper.decrypt(encrypted);
            Log.d("TEST", "decrypted:" + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return strEncryptedText;
    }

    public static String getStartRequestCode(String bookingID) {

        bookingID = bookingID.replace("Appointment_", "");
        //  metaID = metaID.replace("Appointment_Base_Entity_", "");

        //Booking id + metaID + Status
        return new StringBuilder().append(bookingID)/*.append(metaID)*/.append(String.valueOf(JullayConstants.STATUS_CONFIRMED)).toString();
    }


    /**
     * Checks that the staff booking is for today or not
     *
     * @param staffBookingDetails
     * @return
     */
    public static boolean isTodaysBooking(String staffBookingDetails) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date date = format.parse(staffBookingDetails);
            long crtDate = (date.getTime() / 86400000);
            long date2 = new Date().getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Long ltym = Long.valueOf(date2);
            Date dateFormated = new Date(ltym);
            TimeZone t = TimeZone.getTimeZone("Asia/Calcutta");
            df.setTimeZone(t);
            // String formattedDate = new
            // SimpleDateFormat("mm/dd/yy hh:mm:ss").format(date);
            String reportDate = df.format(dateFormated);
            Date date3 = format.parse(reportDate);
            long apDate = (date3.getTime() / 86400000);

            if (crtDate != apDate) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
        return true;
    }


    public static boolean isMaintainanceOn() {
        try {
            Calendar rightNow = Calendar.getInstance();
            int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMin = rightNow.get(Calendar.MINUTE);
            int totalMins = (currentHourIn24Format * 60) + currentMin;

            if (totalMins > 1410 && totalMins < 1440)
                return true;
            if (totalMins > 0 && totalMins < 30)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * Check network connection available or not
     *
     * @param context
     * @return
     */
    public static boolean haveNetworkConnection(Context context) {
        try {
            ConnectivityManager mCM = null;
            if (mCM == null) {
                if (context != null) {
                    mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                }
            }
            if (mCM != null) {
                NetworkInfo netInfo = mCM.getActiveNetworkInfo();
                return netInfo != null && netInfo.isConnectedOrConnecting();
            } else {
                return false;
            }
            // return ConnectionManager.getInstance().isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public static void autoLogin(final Context mContext) {

        String mobile = PrefsWrapper.with(mContext).getString(JullayConstants.KEY_USER_PH, "");
        final String PWD = /*CommonUtility.decryption(*/PrefsWrapper.with(mContext).getString(JullayConstants.KEY_PWD, "")/*, PrefsWrapper.with(mContext).getString(JullayConstants.KEY_USER_TOKEN, ""))*/;
        String imei = PrefsWrapper.with(mContext).getString(JullayConstants.KEY_USER_IDENTIFIER, "");
        CentralApis.getInstance().getAPIS().loginRequest(mobile, PWD, imei).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    Toast.makeText(mContext, "Autologin called", Toast.LENGTH_SHORT).show();
                    if (loginResponse.getStatus()) {
                        PrefsWrapper.with(mContext).save(JullayConstants.KEY_PWD, CommonUtility.encryption(PWD, loginResponse.getToken()));
                        PrefsWrapper.with(mContext).save(JullayConstants.KEY_USER_TOKEN, "Bearer " + loginResponse.getToken());

                    } else {
                        logoutApp(mContext);

                    }
                } else {
                    logoutApp(mContext);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                if (!CommonUtility.haveNetworkConnection(mContext)) {
                    CommonUtility.showAlertRetryCancel(mContext, mContext.getString(R.string.app_name), mContext.getString(R.string.internet_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //retry
                            logoutApp(mContext);
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonUtility.showErrorAlert(mContext, mContext.getString(R.string.network_error_text));

                }
            }
        });

    }



    public static void logoutApp(final Context mContext) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        //FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (Exception e) {
                        clearAppData(mContext);
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    // Call your Activity where you want to land after log out
                    clearAppData(mContext);
                }

            }.execute();

        } catch (Exception e) {
            clearAppData(mContext);
            e.printStackTrace();
        }
    }

    public static void clearAppData(Context mContext) {
        PrefsWrapper.with(mContext).clearAll();

        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(launchIntent);
        ((AppCompatActivity) mContext).finish();
    }



}
