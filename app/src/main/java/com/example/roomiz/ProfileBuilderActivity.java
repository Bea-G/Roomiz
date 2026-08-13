package com.example.roomiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.button.MaterialButton;

public class ProfileBuilderActivity extends AppCompatActivity {
    private static final int STEP_COUNT = 5;
    private int currentStep = 0;
    private ViewFlipper profileSteps;
    private final int[] progressIds = {R.id.progress1, R.id.progress2, R.id.progress3, R.id.progress4, R.id.progress5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        profileSteps = findViewById(R.id.profileSteps);
        ImageButton backButton = findViewById(R.id.btnBack);
        MaterialButton continueButton = findViewById(R.id.btnProfileContinue);
        backButton.setOnClickListener(view -> goBack());
        continueButton.setOnClickListener(view -> goForward());
        updateProgress();
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
            finish();
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
        goBack();
    }
}