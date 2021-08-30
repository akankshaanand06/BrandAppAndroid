
package com.six.hats.brand.util;

public interface JullayConstants {


    String BASE_URL = "http://13.233.106.113:8080/";//aws testing
    //String BASE_URL = "https://sixhatsfathoms.com/";// AWS Live
    //String BASE_URL = "http://10.0.2.2:8080/";//local


    int STAFF_SPECIFIC = 1;
    int TIME_SPECIFIC = 2;

    int STATUS_CONFIRM = 1;

    String KEY_BOOKING_OFFLINE = "ONSITE"; //Constants
    String KEY_BOOKING_ONLINE = "ONLINE";
    int STATUS_CANCELLED = 4;
    String SERVICES_MAIN_CATEGORY = "SERVICES_MAIN_CATEGORY";

    int STATUS_PENDING = 0;
    int STATUS_CONFIRMED = 1;
    int STATUS_START = 2;
    int STATUS_COMPLETE = 3;

    int META_STATUS_PENDING = 0;
    int META_STATUS_START = 1;
    int META_STATUS_COMPLETE = 2;

    String KEY_SERVICES = "SERVICE";
    String COUNTRY_INDIA = "2";
    String CITY_VADODARA = "5";
    //Shared Preference Keys
    String KEY_LANG = "key_lang";
    String KEY_USER_ID = "user_id";
    String KEY_PWD = "pwd";
    String KEY_SOCIAL_ID = "social_id";
    String KEY_CENTRE_ID = "centre_id";
    String KEY_LOGIN_TYPE = "login_type";
    String KEY_NAME = "user_name";
    String KEY_USER_PH = "user_ph";
    String KEY_USER_EMAIL = "user_email";
    String KEY_USER_IDENTIFIER = "unique_identity";
    String KEY_USER_IMG = "user_img";
    String KEY_USER_TOKEN = "user_token";
    String KEY_IS_VERIFIED = "is_user_verified";
    String KEY_IS_LOGGED_IN = "is_user_logged";
    String KEY_OTP = "user_otp";
    String KEY_BOOKING_COUNT = "key_booking_count";
    String KEY_Current_BOOKING_Pos = "current_booking_pos";
    String KEY_BOOKING_TYPE = "booking_type";
    String KEY_PARENT_BOOKING_ID = "parent_booking_id";
    String KEY_FIREBASE_TOKEN = "fire_token";
    String KEY_BOOKING_STATUS = "booking_status";
    String KEY_CENTRE_END_TIME = "centre_end";
    String KEY_IS_PREBOOKING = "is_prebooking";
    String KEY_PREBOOKING_DATE = "prebooking_date";
    String KEY_FEEDBACK_COUNT = "user_ph";
    String KEY_FEEDBACK_STATUS = "user_ph";
    String KEY_RATING_STATUS = "rating_status";
    String KEY_RATE_BOOKING_ID = "rating_id";
    String KEY_FIRST_OPEN = "first_open";
    String KEY_FEEDBACK_FLAG = "feedbackFlag";
    String KEY_NAMES_LIST = "name_list";
    String KEY_CENTRE = "centre";
    String KEY_BIZ_CATEGORY = "BUSINESS_CATEGORY";
    String KEY_IMAGE_TYPE = "IMAGE_TYPE";
    String KEY_CENTRE_IMAGE_TYPE = "CENTRE_IMAGE_TYPE";
    String KEY_BIZ_SUB_CATEGORY = "BUSINESS_SUB_CATEGORY";
    String KEY_STATE = "STATE";
    String DARK_HEADING = "<font color=\"#000000\">";
    String LIGHT_HEADING = "<font color=\"#ffffff\">";
    String DEFAULT_HEADING_COLOR = "header_color";

    String Maintenance = "Routine maintenance going on. Try after 00:30 AM.";
}
