package com.example.roomiz;
import android.os.Bundle;
import android.graphics.Paint;
import android.content.Intent;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signIn = findViewById(R.id.tvSignIn);
signIn.setPaintFlags(signIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        MaterialButton createAccount = findViewById(R.id.btnCreateAccount);
        createAccount.setOnClickListener(view -> startActivity(new Intent(this, ProfileBuilderActivity.class)));
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);
    }
}
