package com.six.hats.brand;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.six.hats.brand.adapters.GalleryAdapter;
import com.six.hats.brand.adapters.HomeAdapter;
import com.six.hats.brand.model.CentreSingleton;
import com.six.hats.brand.model.ImageItem;
import com.six.hats.brand.model.ImagesResponse;
import com.six.hats.brand.networks.CentralApis;
import com.six.hats.brand.util.CommonUtility;
import com.six.hats.brand.util.JullayConstants;
import com.six.hats.brand.util.PickImageDialog;
import com.six.hats.brand.util.PrefsWrapper;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import rx.Subscription;

public class BizGallery extends AppCompatActivity {

    private List<ImageItem> imgData = new ArrayList<>();
    private String mCentreId;
    AppCompatActivity appCompatActivity;
    private File mImageFile = null;
    private Uri mImageFileUri;
    private Subscription mPicSubscription;

    private PickImageDialog mPickImageDialog;
    /**
     * Permissions required to read and write contacts.
     */
    private static String[] permissionsCameraGallery = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    /**
     * Permissions required to read camera.
     */
    public static final int REQUEST_CAMERA = 0;
    private GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_biz_images);
        appCompatActivity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.gallery) + "</font>")));

        // primary sections of the activity.
        CentreSingleton singleton = CentreSingleton.getInstance();

        mCentreId = singleton.getBranchId();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery_images_list);
        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new GalleryAdapter(imgData, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CommonUtility.chkString(mCentreId)) {
            loadCentreImagesDetails();
        }
    }

    private void loadCentreImagesDetails() {

        String centreId = mCentreId;
        CentralApis.getInstance().getAPIS().loadCentreImages(centreId, PrefsWrapper.with(getApplicationContext()).getString(JullayConstants.KEY_USER_TOKEN, "")).enqueue(new retrofit2.Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                if (response.isSuccessful()) {
                    imgData.clear();
                    imgData.addAll(response.body().getImageList());
                    adapter.notifyData(imgData);

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
            public void onFailure(Call<ImagesResponse> call, Throwable t) {
                CommonUtility.showErrorAlert(getApplicationContext(), getString(R.string.network_error_text));
            }
        });
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
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPicSubscription != null) {
            mPicSubscription.unsubscribe();
        }
    }
}
