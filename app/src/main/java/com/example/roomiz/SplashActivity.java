package com.example.roomiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DURATION_MS = 1_000L;  // Splash delay.

    private final Runnable openLogin = () -> {
        startActivity(new Intent(this, LoginActivity.class));  // Opens login after splash.
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.white));
        getWindow().setNavigationBarColor(getColor(R.color.white));
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
                .setAppearanceLightNavigationBars(true);

        setContentView(R.layout.activity_splash);
        findViewById(android.R.id.content).postDelayed(openLogin, SPLASH_DURATION_MS);
    }

    @Override
    protected void onDestroy() {
        findViewById(android.R.id.content).removeCallbacks(openLogin);
        super.onDestroy();
    }
}