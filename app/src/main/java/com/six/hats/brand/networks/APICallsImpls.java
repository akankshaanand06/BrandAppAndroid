package com.six.hats.brand.networks;

import com.jullay.users.models.bookingModels.Appointment;

import retrofit2.Call;
import retrofit2.Response;

public class APICallsImpls implements APICalls {

    /**
     * load booking detailed from booking id
     *
     * @return
     */
    public Appointment loadBookingDetailsByID(String mBookingId, String token) {
        final Appointment[] appointment = {null};
        CentralApis.getInstance().getBookingAPIS().loadBookingByID(mBookingId, token).enqueue(new retrofit2.Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    appointment[0] = response.body();

                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                t.printStackTrace();

            }
        });

        return appointment[0];
    }

}
