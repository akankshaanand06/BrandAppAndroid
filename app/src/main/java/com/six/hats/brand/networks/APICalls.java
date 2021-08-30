package com.six.hats.brand.networks;

import com.jullay.users.models.bookingModels.Appointment;

public interface APICalls {

    public Appointment loadBookingDetailsByID(String mBookingId, String token);


}
