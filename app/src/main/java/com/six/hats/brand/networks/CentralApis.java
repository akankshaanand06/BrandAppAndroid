package com.six.hats.brand.networks;

import android.annotation.SuppressLint;

import androidx.annotation.Keep;

import com.six.hats.brand.Feedbacks;
import com.six.hats.brand.fragments.HomeFragment;
import com.six.hats.brand.model.BizReviews;
import com.six.hats.brand.model.CndDataCategoryItem;
import com.six.hats.brand.model.booking.Appointment;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.model.booking.BookingAppointment;
import com.six.hats.brand.model.booking.BookingLstDetails;
import com.six.hats.brand.model.Centre;
import com.six.hats.brand.model.FeedBackResponse;
import com.six.hats.brand.model.ImagesResponse;
import com.six.hats.brand.model.LoginResponse;
import com.six.hats.brand.model.MyServiceResponse;
import com.six.hats.brand.model.OtpResponse;
import com.six.hats.brand.model.RegPer;
import com.six.hats.brand.model.RegistPDRequest;
import com.six.hats.brand.model.StaffApiResponse;
import com.six.hats.brand.model.UserDetails;
import com.six.hats.brand.model.booking.CancelBooking;
import com.six.hats.brand.model.booking.LiveBookingResponse;
import com.six.hats.brand.model.booking.MultiBookingDetails;
import com.six.hats.brand.model.booking.MultiBookingRequest;
import com.six.hats.brand.model.booking.RemoveSelectedStaff;
import com.six.hats.brand.model.booking.SearchQATBodyParam;
import com.six.hats.brand.model.booking.ServiceCategoryItem;
import com.six.hats.brand.model.booking.ServiceList;
import com.six.hats.brand.model.booking.StaffDisplayResponse;
import com.six.hats.brand.model.booking.StaffSpecQATRequest;
import com.six.hats.brand.model.booking.StaffSpecQATResponse;
import com.six.hats.brand.model.booking.StaffSpecificResponse;
import com.six.hats.brand.model.booking.TimeSpecQATResponse;
import com.six.hats.brand.util.JullayConstants;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Keep
public class CentralApis {

    private static CentralApis centralApis = null;
    private APIS apis;
    private Booking_APIS booking_apis;

    public static CentralApis getInstance() {
        if (centralApis == null) {
            centralApis = new CentralApis();
        }
        return centralApis;
    }

    private CentralApis() {

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        try {
            if (sslContext != null) {
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory())
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(JullayConstants.BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                apis = retrofit.create(APIS.class);

                booking_apis = retrofit.create(Booking_APIS.class);
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    //Facade Pattern!
    public interface APIS {

        //-------------------------------

        @Headers("Content-Type: application/json")
        @GET("/user/getUserDetails")
            //updated
        Call<UserDetails> loadMyProfile(@Query("userId") String userId, @Header("Authorization") String authHeader);

        @FormUrlEncoded
        @POST("/user/resetPassword")
            //updated
        Call<BasicResponse> postResetPwd(@Field("userName") String userName, @Field("token") String token, @Field("password") String password);

        @FormUrlEncoded
        @POST("/user/changePassword")
            //updated
        Call<BasicResponse> postChangePwd(@Field("userName") String userName, @Field("previousPassword") String previousPassword, @Field("Newpassword") String Newpassword, @Header("Authorization") String authHeader);

        @FormUrlEncoded
        @POST("/user/verifyOTP")
            //updated
        Call<OtpResponse> verifyRequest(@Field("userId") String userId, @Field("OTP") String otp);

        @FormUrlEncoded
        @POST("/user/sendOTP")
            //updated
        Call<OtpResponse> resendOtpRequest(@Field("userId") String userId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/user/signUp")
            //updated
        Call<LoginResponse> regPDRequest(@Body RegistPDRequest loginRequest);

        @Headers("Content-Type: application/json")
        @PUT("/user/updateUserDetails")
            //updated
        Call<RegPer> updatePDRequest(@Body RegistPDRequest loginRequest, @Header("Authorization") String authHeader);

        @FormUrlEncoded
        @POST("/user/login")
            // updated
        Call<LoginResponse> loginRequest(@Field("userName") String userName, @Field("password") String password, @Field("imeiNumber") String imeiNumber);

        @FormUrlEncoded
        @POST("/user/changeImei")
            // updated
        Call<LoginResponse> changeImei(@Field("userName") String userName, @Field("password") String password, @Field("imeiNumber") String imeiNumber);


        @FormUrlEncoded
        @POST("/user/forgetPassword")
            //updated
        Call<BasicResponse> forgotPasswordRequest(@Field("userName") String userName);


        @Headers("Content-Type: application/json")
        @POST("/user/addBranchAsFavorite")
            //updated
        Call<BasicResponse> addMyFavsRequest(@Query("userId") String userId, @Query("branchId") String branchId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/user/removeBranchAsFavorite")
            //updated
        Call<BasicResponse> removeFavsRequest(@Query("userId") String userId, @Query("branchId") String branchId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/user/updatePushNotificationToken")
            //updated
        Call<BasicResponse> sendFirebaseTokenToServer(@Query("userId") String userId, @Query("pushNotificationToken") String pushNotificationToken, @Header("Authorization") String authHeader);


        @Headers("Content-Type: application/json")
        @GET("/admin/getServicesByBranch?")
            //Updated
        Call<MyServiceResponse> loadAllServices(@Query("branchId") String branchId, @Query("mainService") String mainService, @Header("Authorization") String authHeader);


        @Headers("Content-Type: application/json")
        @GET("/common/getAllStaffDisplayByBranchId")
            //updated
        Call<List<StaffDisplayResponse>> loadAllStaffListByBranchId(@Query("branchId") String branchId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getBranchDetails")
            //updated
        Call<Centre> loadBranchDetailsByQr(@Query("branchQrId") String branchQrId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getImages")
            //updated
        Call<ImagesResponse> loadCentreImages(@Query("branchId") String branchId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getBranchRating")
            //updated
        Call<BasicResponse> loadCentreRating(@Query("branchId") String branchId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/cms/getAllFeedBackOfGivenCustomer")
            //updated
        Call<List<FeedBackResponse>> getAllFeedbacks(@Body Feedbacks.FeedbackRequest customerFilledFeedBackSearchRequest);


        @Headers("Content-Type: application/json")
        @POST("/common/getFeedBackQuestion/{subCategory}")
            //updated
        Call<Feedbacks.Root> getAllQuesFeedbacks(@Path("subCategory") String subCategory, @Body Feedbacks.Data fliterQuestionList);

        @Headers("Content-Type: application/json")
        @GET("/user/getAllUpcomingAppointment")
            //updated
        Call<BookingLstDetails> loadMyBookingData(@Query("userId") String userId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getAllReviews")
            //updated
        Call<List<BizReviews>> loadAllReviewsForBranch(@Query("branchId") String branchId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getAllReviewsByStaffId")
            //updated
        Call<List<BizReviews>> loadAllReviewsForStaff(@Query("branchId") String branchId, @Query("staffId") String staffId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/user/getHistoryOfAllAppoitmnetOfUser")
            //updated
        Call<BookingLstDetails> loadHistoricBookingData(@Query("userId") String userId, @Query("appointmentStatus") String appointmentStatus, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getImageHomeCampaignByBrand")
            //Updated
        Call<HomeFragment.APIResponse> loadHomeActiveCampaign(@Query("branchId") String branchId, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/cms/getData")
            //updated
        Call<CndDataCategoryItem> loadSpinner(@Query("serviceName") String type, @Query("additionalParam") String additionalParam, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/cms/getData")
            //updated
        Call<ServiceCategoryItem> loadSpinnerServices(@Query("serviceName") String type, @Query("additionalParam") String additionalParam);

        @Headers("Content-Type: application/json")
        @GET("/user/RTP")
            //updated
        Call<LiveBookingResponse> loadLiveStatus(@Query("userId") String userId, @Query("appointmentId") String appointmentId, @Header("Authorization") String authHeader);

    }

    public interface Booking_APIS {

        @Headers("Content-Type: application/json")
        @POST("/booking/bookAnAppointMent")
            //updated
        Call<MultiBookingDetails> bookAppointment(@Body BookingAppointment bookAppointmentRequest, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/booking/bookAnAppointMentForMultiPerson")
            //updated
        Call<MultiBookingDetails> bookMultiAppointment(@Body MultiBookingRequest mutiAppointmentRequest, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/booking/searchQAT/{branchId}/{totalTime}")
            //updated
        Call<TimeSpecQATResponse> searchTSQAT(@Path("branchId") String branchId, @Path("totalTime") int totalTime, @Body SearchQATBodyParam searchQATRequest, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getActiveServicesByBranch")
            //Updated
        Call<MyServiceResponse> loadMyService(@Query("branchId") String branchId, @Query("mainService") String mainService, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @GET("/common/getSpecialityServicesByBranch")
            //Updated
        Call<MyServiceResponse> loadMySpecialityService(@Query("branchId") String branchId, @Query("mainService") String mainService, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/booking/signalFromApp")
            //Updated
        Call<BasicResponse> cancelTempBooking(@Body CancelBooking appSignalList, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/booking/removeFromQue")
            //Updated
        Call<BasicResponse> removeFromQue(@Body RemoveSelectedStaff removeSelectedStaff, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/bookingStaff/searchStaff")
            //updated
        Call<StaffSpecificResponse> loadStaffSpecificData(@Body ServiceList searchStaffRequest, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/bookingStaff/searchQAT/{branchId}")
            //updated
        Call<StaffSpecQATResponse> searchSSQAT(@Path("branchId") String branchId, @Body StaffSpecQATRequest timeSpecQATRequest, @Header("Authorization") String authHeader);

      /*  @Headers("Content-Type: application/json")
        @GET("/user/getNotificationOfGivenUser")
            //updated
        Call<NotificationItems> getNotificationsList(@Query("userId") String userId, @Query("page") int page, @Query("size") int size, @Header("Authorization") String authHeader);
*/

        @Headers("Content-Type: application/json")
        @GET("/user/cancelBooking")
            //updated
        Call<BasicResponse> cancelService(@Query("userId") String userId, @Query("appointmentId") String appointmentId, @Query("appointmentBaseEntityId") String appointmentBaseEntityId, @Header("Authorization") String authHeader);

        //GET request
        @Headers("Content-Type: application/json")
        @GET("/appointment/getByAppointmentId")
        //updated
        Call<Appointment> loadBookingByID(@Query("appointmentId") String appointmentId, @Header("Authorization") String authHeader);

        //GET request
        @Headers("Content-Type: application/json")
        @GET("/appointment/getByAppointmentIdAndAppoitmentStatus")
        //updated
        Call<Appointment> loadBookingByStatus(@Query("appointmentId") String appointmentId, @Query("appointmentStatus") String appoitmentStatus, @Header("Authorization") String authHeader);


       /* @Headers("Content-Type: application/json")
        @POST("/user/getFeedBackQuestion")
            //updated
        Call<List<FeedbackItem.Feedback>> loadFeedbackQuestionare(@Query("mainCategory") String mainCategory, @Path("subCategory") String subCategory, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/user/saveRatingForgivenAppointmentId")
            //updated
        Call<BasicResponse> postRating(@Body RatingActivity.PostRatingParams appointmentRating, @Header("Authorization") String authHeader);

        @Headers("Content-Type: application/json")
        @POST("/user/saveCustomerFeedBackResponse")
            //updated
        Call<BasicResponse> postCustomerFeedback(@Body PostCustomerFeedback customerFilledFeedBackForm, @Header("Authorization") String authHeader);
*/

    }

    public APIS getAPIS() {
        return apis;
    }

    public Booking_APIS getBookingAPIS() {
        return booking_apis;
    }

}


