package com.example.roomiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleClient;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton emailContinueButton;
    private TextView authModeText;
    private boolean loginMode = false;

    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    signInToFirebase(task.getResult(ApiException.class));
                } catch (ApiException exception) {
                    Toast.makeText(this, R.string.google_sign_in_failed, Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);

        firebaseAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        emailContinueButton = findViewById(R.id.btnEmailContinue);
        authModeText = findViewById(R.id.tvAuthMode);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, options);

        emailContinueButton.setOnClickListener(view -> continueWithEmail());
        authModeText.setOnClickListener(view -> switchAuthMode());
        findViewById(R.id.btnGoogleSignIn).setOnClickListener(view -> startGoogleSignIn());
    }

    private void continueWithEmail() {
        String email = emailInput.getText() == null ? "" : emailInput.getText().toString().trim();
        String password = passwordInput.getText() == null ? "" : passwordInput.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError(getString(R.string.email_invalid));
            return;
        }
        if (password.length() < 6) {
            passwordInput.setError(getString(R.string.password_too_short));
            return;
        }

        emailContinueButton.setEnabled(false);
        if (loginMode) {
            loginWithEmail(email, password);
        } else {
            registerWithEmail(email, password);
        }
    }

    private void registerWithEmail(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(registerTask -> {
            if (registerTask.isSuccessful()) {
                openProfileBuilder();
            } else {
                showEmailAuthError(R.string.email_register_failed);
            }
        });
    }

    private void loginWithEmail(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(loginTask -> {
            if (loginTask.isSuccessful()) {
                openProfileBuilder();
            } else {
                showEmailAuthError(R.string.email_login_failed);
            }
        });
    }

    private void showEmailAuthError(int messageId) {
        emailContinueButton.setEnabled(true);
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }

    private void switchAuthMode() {
        loginMode = !loginMode;
        emailContinueButton.setText(loginMode ? R.string.login : R.string.register);
        authModeText.setText(loginMode ? R.string.dont_have_account : R.string.already_have_account);
    }

    private void startGoogleSignIn() {
        googleSignInLauncher.launch(googleClient.getSignInIntent());
    }

    private void signInToFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                openProfileBuilder();
            } else {
                Toast.makeText(this, R.string.google_sign_in_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openProfileBuilder() {
        startActivity(new Intent(this, ProfileBuilderActivity.class));
        finish();
    }
}