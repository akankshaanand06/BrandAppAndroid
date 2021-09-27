// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.six.hats.brand.qrscanner;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.List;


public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

    private static final String TAG = "BarcodeScanProc";
    AppCompatActivity mContext;
    private final FirebaseVisionBarcodeDetector detector;
    private String mBookingID = "", mMetaID = "";

    public BarcodeScanningProcessor(AppCompatActivity context, String mMID, String mBID) {
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        mContext = context;
        mBookingID = mBID;
        mMetaID = mMID;
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionBarcode> barcodes,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        for (int i = 0; i < barcodes.size(); ++i) {
            FirebaseVisionBarcode barcode = barcodes.get(i);
            BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);
            graphicOverlay.add(barcodeGraphic);
            LivePreviewActivity.closeCameraSource();
            if (barcode.getRawValue().length() != 0) {

                verifyBookingPasscode(barcode.getRawValue());
            }


            Log.d("Barcode-----", barcode.getRawValue());
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Barcode detection failed " + e);
    }


    /**
     * Verify Passcode given by the customer
     */
    public Boolean verifyBookingPasscode(String staffID) {

     /*   if (bookingDetails.getPassCode().trim().equalsIgnoreCase(passcode)) {
            //pass-code Matched call start API

            int currentBHr = bookingDetails.getAppontmentEnitities().get(entityPos).getBookingSlot().getStartSpan().getHour();
            int currentBMin = bookingDetails.getAppontmentEnitities().get(entityPos).getBookingSlot().getStartSpan().getMinutes();

            Calendar rightNow = Calendar.getInstance();
            int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMin = rightNow.get(Calendar.MINUTE);
            if (currentHourIn24Format == currentBHr) {
                if (currentMin < currentBMin) {
                    int current_time = (currentHourIn24Format * 60) + currentMin;
                    int ori_tme = (currentBHr * 60) + currentBMin;

                    int time = ori_tme - current_time;
                    //Early mins
                    postEarlyStart(time, mBookingId, bookingDetails.getAppontmentEnitities().get(entityPos).getStaffId(), bookingDetails.getAppontmentEnitities().get(entityPos).getAppointmentBaseEntityId(), bookingDetails.getAppontmentEnitities().get(entityPos).getBookingSlot().getEndSpan());
                } else {

                    int current_time = (currentHourIn24Format * 60) + currentMin;
                    int ori_tme = (currentBHr * 60) + currentBMin;

                    int delayInStartService = current_time - ori_tme;

                    if (delayInStartService != 0) {
                        postAddDelay(String.valueOf(delayInStartService), bookingDetails.getAppontmentEnitities().get(entityPos).getAppointmentStatus(), bookingDetails.getAppointmentId(), bookingDetails.getBookingType(), bookingDetails.getAppontmentEnitities().get(entityPos).getStaffId(), bookingDetails.getAppontmentEnitities().get(entityPos).getAppointmentBaseEntityId(), bookingDetails.getAppontmentEnitities().get(entityPos).getBookingSlot().getEndSpan(), "start");
                    } else {
                        startService();
                    }
                }

            } else {
                //Early Hrs
                if (currentHourIn24Format < currentBHr) {
                    int current_time = (currentHourIn24Format * 60) + currentMin;
                    int ori_tme = (currentBHr * 60) + currentBMin;

                    int time = ori_tme - current_time;
                    //Early mins
                    postEarlyStart(time, mBookingId, bookingDetails.getAppontmentEnitities().get(entityPos).getStaffId(), bookingDetails.getAppontmentEnitities().get(entityPos).getAppointmentBaseEntityId(), bookingDetails.getAppontmentEnitities().get(entityPos).getBookingSlot().getEndSpan());

                } else {
                    int current_time = (currentHourIn24Format * 60) + currentMin;
                    int ori_tme = (currentBHr * 60) + currentBMin;

                    int delayInStartService = current_time - ori_tme;

                    if (delayInStartService != 0) {
                        postAddDelay(String.valueOf(delayInStartService), bookingDetails.getAppontmentEnitities().get(entityPos).getAppointmentStatus(), bookingDetails.getAppointmentId(), bookingDetails.getBookingType(), bookingDetails.getAppontmentEnitities().get(entityPos).getStaffId(), bookingDetails.getAppontmentEnitities().get(entityPos).getAppointmentBaseEntityId(), bookingDetails.getAppontmentEnitities().get(entityPos).getBookingSlot().getEndSpan(), "start");
                    } else {
                        startService();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
*/
        return false;
    }

    public void openStaffPage() {

       /* Intent view_as_staff = new Intent(mContext, ViewAsStaff.class);
        view_as_staff.putExtra("staffID", mStaffID);
        view_as_staff.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptions options1 =
                ActivityOptions.makeCustomAnimation(mContext, R.anim.slide_in_left, R.anim.slide_in_right);
        mContext.startActivity(view_as_staff, options1.toBundle());*/
        mContext.finish();
    }



}
