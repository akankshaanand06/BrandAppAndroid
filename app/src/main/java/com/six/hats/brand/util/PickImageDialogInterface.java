package com.six.hats.brand.util;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Keep;

import java.io.File;

@Keep
public interface PickImageDialogInterface {

    void holdRecordingFile(Uri fileUri, File file);

    void handleIntent(Intent intent, int requestCode);

    void uploadPickedImage(File file);
}
