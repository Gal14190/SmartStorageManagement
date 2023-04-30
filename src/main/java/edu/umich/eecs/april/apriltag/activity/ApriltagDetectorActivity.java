package edu.umich.eecs.april.apriltag.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import edu.umich.eecs.april.apriltag.ApriltagNative;
import edu.umich.eecs.april.apriltag.R;
import edu.umich.eecs.april.apriltag.model.Model;
import edu.umich.eecs.april.apriltag.model.PanelItemPopupModel;
import edu.umich.eecs.april.apriltag.thread.CameraPreviewThread;
import edu.umich.eecs.april.apriltag.thread.DetectionThread;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class ApriltagDetectorActivity extends AppCompatActivity {
    private static final String TAG = "AprilTag";
    private DetectionThread mDetectionThread;
    private CameraPreviewThread mCameraPreviewThread;

    private Model model;

    // views
    private ImageButton detectSpecificItemsBtn;
    private ImageButton detectAllItemsBtn;

    // code request
    private static final int PERMISSIONS_REQUEST_CAMERA = 77;
    private static final int PERMISSION_REQUEST_INTERNET = 1;
    private int has_camera_permissions = 0;
    private int has_internet_permissions = 0;

    private void verifyPreferences() {
        // local storage memory
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int nthreads = Integer.parseInt(sharedPreferences.getString("nthreads_value", "0"));
        if (nthreads <= 0) {
            int nproc = Runtime.getRuntime().availableProcessors();
            if (nproc <= 0) {
                nproc = 1;
            }
            Log.i(TAG, "available processors: " + nproc);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("nthreads_value", Integer.toString(nproc)).apply();
        }
    }

    /**
     * Main create activity
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // network connectivity service request
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Log.i("Network", "connect");
        } else {
            Log.i("Network", "error");
        }

        // Add toolbar/actionbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Make the screen stay awake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Model init
        Model.init();
        Model.setPanelItemPopupModel(new PanelItemPopupModel());

        // Mode detection buttons
        // select detect specific item mode
        detectSpecificItemsBtn = (ImageButton) findViewById(R.id.detectSpecificItems_btn);
        detectSpecificItemsBtn.setOnClickListener(view -> {
            Model.setModeDetection(Model.mode.SPECIFIC_ITEM);
            modeSelectStyle(Model.mode.SPECIFIC_ITEM);

            Intent intent = new Intent(this, ListItemsActivity.class);
            this.startActivity(intent);
        });
        // select detect all items mode
        detectAllItemsBtn = (ImageButton) findViewById(R.id.detectAllItems_btn);
        detectAllItemsBtn.setOnClickListener(view -> {
            Model.setModeDetection(Model.mode.ITEMS);
            modeSelectStyle(Model.mode.ITEMS);
        });
        // set default mode
        Model.setModeDetection(Model.mode.ITEMS);
        modeSelectStyle(Model.mode.ITEMS);

        // get id from list item activity
        Model.setSpecificPartId(getIntent().getIntExtra("itemID", -1));
        if(Model.getSpecificPartId() != -1) {
            Model.setModeDetection(Model.mode.SPECIFIC_ITEM);
            modeSelectStyle(Model.mode.SPECIFIC_ITEM);
        }

        // bottom panel set click event
        ((LinearLayout) findViewById(R.id.panelPopup)).setOnClickListener(view -> {
            if(Model.isDetected()) { // open item activity if item id detected
                Intent intent = new Intent(this, ItemManageActivity.class);
                this.startActivity(intent);
            } else {    // open add item activity if item id is not detected
                Intent intent = new Intent(this, AddItemActivity.class);
                this.startActivity(intent);
            }
        });

        // Ensure we have permission to use the camera (Permission Requesting for Android 6.0/SDK 23 and higher)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Assume user knows enough about the app to know why we need the camera, just ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        } else {
            this.has_camera_permissions = 1;
        }

        // Ensure we have permission to use internet connection
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
        } else {
            this.has_internet_permissions = 1;
        }
    }

    /** Release the camera when the application is exited */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThreads();
        Log.i(TAG, "Finished destroying.");
    }

    /** Release the camera when application focus is lost */
    protected void onPause() {
        super.onPause();
        stopThreads();
        Log.i(TAG, "Finished pause.");
    }

    /**
     * Stop the detection threads
     */
    private void stopThreads() {
        if (mCameraPreviewThread != null) {
            mCameraPreviewThread.interrupt();
            mCameraPreviewThread.destroy();
            try {
                mCameraPreviewThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mCameraPreviewThread = null;
        }
        if (mDetectionThread != null) {
            mDetectionThread.interrupt();
            mDetectionThread.destroy();
            try {
                mDetectionThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mDetectionThread = null;
        }
    }

    /** (Re-)initialize the camera */
    protected void onResume() {
        super.onResume();

        // Check permissions
        if (this.has_camera_permissions == 0) {
            Log.w(TAG, "Missing camera permissions.");
            return;
        }

        // DETECTION INIT
        // Re-initialize the Apriltag detector as settings may have changed
        verifyPreferences();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        double decimation = Double.parseDouble(sharedPreferences.getString("decimation_list", "8"));
        double sigma = Double.parseDouble(sharedPreferences.getString("sigma_value", "0"));
        int nthreads = Integer.parseInt(sharedPreferences.getString("nthreads_value", "4"));
        int max_hamming_error = Integer.parseInt(sharedPreferences.getString("max_hamming_error", "0"));
        boolean diagnosticsEnabled = sharedPreferences.getBoolean("diagnostics_enabled", false);
        String tagFamily = sharedPreferences.getString("tag_family_list", "tag36h11");
        Log.i(TAG, String.format("decimation: %f | sigma: %f | nthreads: %d | tagFamily: %s",
                decimation, sigma, nthreads, tagFamily));
        ApriltagNative.apriltag_init(tagFamily, max_hamming_error, decimation, sigma, nthreads);

        // DIAGNOSTICS
        findViewById(R.id.detectionFpsTextView).setVisibility(diagnosticsEnabled ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.previewFpsTextView).setVisibility(diagnosticsEnabled ? View.VISIBLE : View.INVISIBLE);

        // THREAD INIT
        // Start fetching data process on a separate thread
        TextView itemNameTextView = (TextView) findViewById(R.id.itemName);
        TextView itemSerialNumberTextView = (TextView) findViewById(R.id.itemSerialNumber);

        Model.getPanelItemPopupModel().setItemNameTextView(itemNameTextView);
        Model.getPanelItemPopupModel().setItemSerialNumberTextView(itemSerialNumberTextView);

        model = new ViewModelProvider((ViewModelStoreOwner) this).get(Model.class);

        // Start the detection process on a separate thread
        TextureView detectionSurface = (TextureView) findViewById(R.id.tagView);
        TextView detectionFpsTextView = (TextView) findViewById(R.id.detectionFpsTextView);
        stylizeText(detectionFpsTextView);
        mDetectionThread = new DetectionThread(detectionSurface, detectionFpsTextView);
        mDetectionThread.initialize();
        mDetectionThread.start();

        // Start the camera preview on a separate thread
        SurfaceView previewSurface = (SurfaceView) findViewById(R.id.surfaceView);
        TextView previewFpsTextView = (TextView) findViewById(R.id.previewFpsTextView);
        stylizeText(previewFpsTextView);
        mCameraPreviewThread = new CameraPreviewThread(previewSurface.getHolder(), mDetectionThread, previewFpsTextView);
        mCameraPreviewThread.initialize();
        mCameraPreviewThread.start();
    }

    /**
     * Set style for text settings - not used
     * @param textView
     */
    private void stylizeText(TextView textView) {
        textView.setTextColor(Color.GREEN);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
    }

    /**
     * Option menu - not used
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Toolbar buttons click event
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent3 = new Intent(this, AddItemActivity.class);
                startActivity(intent3);
                return true;

            case R.id.settings:
                verifyPreferences();
                Intent intent = new Intent();
                intent.setClassName(this, "edu.umich.eecs.april.apriltag.activity.SettingsActivity");
                startActivity(intent);
                return true;

            case R.id.reset:
                Intent intent2 = new Intent(this, ListTrackActivity.class);
                startActivity(intent2);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Request permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "App GRANTED camera permissions");

                    // Set flag
                    this.has_camera_permissions = 1;

                    // Restart the camera
                    onPause();
                    onResume();
                } else {
                    Log.i(TAG, "App DENIED camera permissions");
                    this.has_camera_permissions = 0;
                }
                break;
            }

            case PERMISSION_REQUEST_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "App GRANTED internet connection permissions");

                    // Set flag
                    this.has_camera_permissions = 1;
                } else {
                    Log.i(TAG, "App DENIED internet connection permissions");
                    this.has_camera_permissions = 0;
                }
            }
            break;
        }
    }

    /**
     * Set buttons style background
     * @param modeSelect
     */
    private void modeSelectStyle(Model.mode modeSelect) {
        switch (modeSelect) {
            case ITEMS:
                detectAllItemsBtn.setBackgroundColor(Color.WHITE);
                detectSpecificItemsBtn.setBackgroundColor(Color.GRAY);
                break;
            case SPECIFIC_ITEM:
                detectAllItemsBtn.setBackgroundColor(Color.GRAY);
                detectSpecificItemsBtn.setBackgroundColor(Color.WHITE);
                break;
        }
    }
}
