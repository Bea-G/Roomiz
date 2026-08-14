package com.example.roomiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileBuilderActivity extends AppCompatActivity {
    private static final int STEP_COUNT = 6;

    private int currentStep = 0;
    private ViewFlipper profileSteps;
    private View introScreen;
    private ImageView profilePhoto;
    private ImageView cameraIcon;
    private MaterialButton continueButton;
    private Spinner citySpinner;
    private TextInputEditText nameInput;
    private Uri pendingPhotoUri;
    private String livingSpace = "";
    private String stayDuration = "";
    private final List<String> amenities = new ArrayList<>();
    private final int[] progressIds = {R.id.progress1, R.id.progress2, R.id.progress3,
            R.id.progress4, R.id.progress5, R.id.progress6};

    private final ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    launchCamera();
                } else {
                    Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(), saved -> {
                if (saved && pendingPhotoUri != null) {
                    savePhotoInsideApp();
                    profilePhoto.setImageURI(pendingPhotoUri);
                    profilePhoto.setVisibility(View.VISIBLE);
                    cameraIcon.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);

        profileSteps = findViewById(R.id.profileSteps);
        introScreen = findViewById(R.id.introScreen);
        profilePhoto = findViewById(R.id.ivProfilePhoto);
        cameraIcon = findViewById(R.id.ivCamera);
        continueButton = findViewById(R.id.btnProfileContinue);
        nameInput = findViewById(R.id.etFullName);
        citySpinner = findViewById(R.id.spinnerCity);
        setupCitySpinner();

        findViewById(R.id.btnStartProfile).setOnClickListener(view -> introScreen.setVisibility(View.GONE));
        findViewById(R.id.btnBack).setOnClickListener(view -> goBack());
        continueButton.setOnClickListener(view -> goForward());
        findViewById(R.id.photoCaptureArea).setOnClickListener(view -> requestCamera());

        setupChoiceButtons();
        setupRentSlider();
        updateProgress();
    }


    private void setupCitySpinner() {
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, R.layout.item_city_spinner);
        cityAdapter.setDropDownViewResource(R.layout.item_city_spinner_dropdown);
        citySpinner.setAdapter(cityAdapter);
    }
    private void setupChoiceButtons() {
        MaterialButton apartment = findViewById(R.id.btnApartment);
        MaterialButton house = findViewById(R.id.btnHouse);
        MaterialButton studio = findViewById(R.id.btnStudio);
        apartment.setOnClickListener(view -> selectSingleChoice(apartment, house, studio));
        house.setOnClickListener(view -> selectSingleChoice(house, apartment, studio));
        studio.setOnClickListener(view -> selectSingleChoice(studio, apartment, house));

        MaterialButton oneYear = findViewById(R.id.btnStayOneYear);
        MaterialButton sixMonths = findViewById(R.id.btnStaySixMonths);
        MaterialButton threeMonths = findViewById(R.id.btnStayThreeMonths);
        MaterialButton shortStay = findViewById(R.id.btnStayShort);
        oneYear.setOnClickListener(view -> selectSingleChoice(oneYear, sixMonths, threeMonths, shortStay));
        sixMonths.setOnClickListener(view -> selectSingleChoice(sixMonths, oneYear, threeMonths, shortStay));
        threeMonths.setOnClickListener(view -> selectSingleChoice(threeMonths, oneYear, sixMonths, shortStay));
        shortStay.setOnClickListener(view -> selectSingleChoice(shortStay, oneYear, sixMonths, threeMonths));

        setupAmenityButton(findViewById(R.id.btnLaundry));
        setupAmenityButton(findViewById(R.id.btnDishwasher));
        setupAmenityButton(findViewById(R.id.btnBalcony));
    }

    private void selectSingleChoice(MaterialButton selected, MaterialButton... otherButtons) {
        for (MaterialButton button : otherButtons) {
            setButtonSelected(button, false);
        }
        setButtonSelected(selected, true);

        if (currentStep == 1) {
            livingSpace = selected.getText().toString();
        } else if (currentStep == 4) {
            stayDuration = selected.getText().toString();
        }
    }

    private void setupAmenityButton(MaterialButton button) {
        button.setOnClickListener(view -> {
            boolean selected = !amenities.contains(button.getText().toString());
            setButtonSelected(button, selected);
            if (selected) {
                amenities.add(button.getText().toString());
            } else {
                amenities.remove(button.getText().toString());
            }
        });
    }

    private void setButtonSelected(MaterialButton button, boolean selected) {
        int color = selected ? R.color.profile_option_selected : R.color.profile_option;
        button.setBackgroundTintList(ColorStateList.valueOf(getColor(color)));
    }

    private void setupRentSlider() {
        TextView rentValue = findViewById(R.id.tvRentValue);
        Slider rentSlider = findViewById(R.id.sliderRent);
        rentSlider.addOnChangeListener((slider, value, fromUser) ->
                rentValue.setText(getString(R.string.rent_value_format, Math.round(value))));
    }

    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        try {
            File photoFile = File.createTempFile("profile_", ".jpg", getCacheDir());
            pendingPhotoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", photoFile);
            cameraLauncher.launch(pendingPhotoUri);
        } catch (IOException exception) {
            Toast.makeText(this, R.string.photo_capture_failed, Toast.LENGTH_SHORT).show();
        }
    }
    // Keeps the camera photo on this device
    private void savePhotoInsideApp() {
        File savedPhoto = new File(getFilesDir(), "profile_photo.jpg");
        try (InputStream input = getContentResolver().openInputStream(pendingPhotoUri);
             OutputStream output = new FileOutputStream(savedPhoto)) {
            if (input == null) {
                throw new IOException("The camera image could not be read.");
            }
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            pendingPhotoUri = Uri.fromFile(savedPhoto);
        } catch (IOException exception) {
            Toast.makeText(this, R.string.photo_capture_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void goForward() {
        if (!isCurrentStepValid()) {
            return;
        }
        if (currentStep == STEP_COUNT - 1) {
            saveProfile();
            return;
        }
        profileSteps.showNext();
        currentStep++;
        updateProgress();
    }

    private boolean isCurrentStepValid() {
        if (currentStep == 0 && (nameInput.getText() == null
                || nameInput.getText().toString().trim().isEmpty())) {
            nameInput.setError(getString(R.string.name_required));
            return false;
        }
        if (currentStep == 1 && livingSpace.isEmpty()) {
            Toast.makeText(this, R.string.choose_living_space, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (currentStep == 2 && citySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.choose_city, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (currentStep == 4 && stayDuration.isEmpty()) {
            Toast.makeText(this, R.string.choose_stay_duration, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveProfile() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, R.string.profile_save_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        continueButton.setEnabled(false);
        saveProfileDocument();
    }

    private void saveProfileDocument() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Slider rentSlider = findViewById(R.id.sliderRent);
        Map<String, Object> profile = new HashMap<>();
        profile.put("name", nameInput.getText().toString().trim());
        profile.put("livingSpace", livingSpace);
        profile.put("city", citySpinner.getSelectedItem().toString());
        profile.put("rent", Math.round(rentSlider.getValue()));
        profile.put("stayDuration", stayDuration);
        profile.put("amenities", amenities);
        profile.put("updatedAt", FieldValue.serverTimestamp());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        WriteBatch batch = firestore.batch();
        batch.set(firestore.collection("users").document(userId), profile);
        // Demo profiles are managed separately in Firestore and are shown in Home and Chats.
        batch.delete(firestore.collection("profiles").document(userId));
        batch.commit()
                .addOnSuccessListener(unused -> openMainScreen())
                .addOnFailureListener(error -> showSaveError());
    }

    private void showSaveError() {
        continueButton.setEnabled(true);
        Toast.makeText(this, R.string.profile_save_failed, Toast.LENGTH_SHORT).show();
    }

    private void openMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
            segment.setBackgroundColor(getColor(index <= currentStep
                    ? R.color.black : R.color.profile_progress_inactive));
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
