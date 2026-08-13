package com.example.roomiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.IOException;

public class ProfileBuilderActivity extends AppCompatActivity {
    private static final int STEP_COUNT = 6;
    private int currentStep = 0;
    private ViewFlipper profileSteps;
    private View introScreen;
    private ImageView profilePhoto;
    private ImageView cameraIcon;
    private Uri pendingPhotoUri;
    private final int[] progressIds = {R.id.progress1, R.id.progress2, R.id.progress3, R.id.progress4, R.id.progress5, R.id.progress6};

    private final ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) launchCamera();
            });
    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(), saved -> {
                if (saved && pendingPhotoUri != null) {
                    profilePhoto.setImageURI(pendingPhotoUri);
                    profilePhoto.setVisibility(View.VISIBLE);
                    cameraIcon.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        profileSteps = findViewById(R.id.profileSteps);
        introScreen = findViewById(R.id.introScreen);
        profilePhoto = findViewById(R.id.ivProfilePhoto);
        cameraIcon = findViewById(R.id.ivCamera);
        findViewById(R.id.btnStartProfile).setOnClickListener(view -> introScreen.setVisibility(View.GONE));
        findViewById(R.id.btnBack).setOnClickListener(view -> goBack());
        findViewById(R.id.btnProfileContinue).setOnClickListener(view -> goForward());
findViewById(R.id.photoCaptureArea).setOnClickListener(view -> requestCamera());

        TextView rentValue = findViewById(R.id.tvRentValue);
        Slider rentSlider = findViewById(R.id.sliderRent);
        rentSlider.addOnChangeListener((slider, value, fromUser) ->
                rentValue.setText(getString(R.string.rent_value_format, Math.round(value))));
        updateProgress();
    }

    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        try {
            File photoFile = File.createTempFile("profile_", ".jpg", getCacheDir());
            pendingPhotoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            cameraLauncher.launch(pendingPhotoUri);
        } catch (IOException ignored) {
            // The UI remains usable if temporary image storage is unavailable.
        }
    }

    private void goForward() {
        if (currentStep == STEP_COUNT - 1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }
        profileSteps.showNext();
        currentStep++;
        updateProgress();
    }

    private void goBack() {
        if (currentStep == 0) {
            introScreen.setVisibility(View.VISIBLE);
            return;
        }
        profileSteps.showPrevious();
        currentStep--;
        updateProgress();
    }

    private void updateProgress() {
        for (int index = 0; index < progressIds.length; index++) {
            View segment = findViewById(progressIds[index]);
            segment.setBackgroundColor(getColor(index <= currentStep ? R.color.black : R.color.profile_progress_inactive));
        }
    }

    @Override
    public void onBackPressed() {
        if (introScreen.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            goBack();
        }
    }
}