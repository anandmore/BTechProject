package com.btech.project.technofeed.view;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.btech.project.technofeed.R;
import com.btech.project.technofeed.util.UtilityMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Typeface montserrat_regular;
    private TextView txtMain, txtHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        setContentView(R.layout.activity_reset_password);
        TextInputLayout emailLayout = (TextInputLayout) findViewById(R.id.email_reset);
        emailLayout.setTypeface(montserrat_regular);
        inputEmail = (EditText) findViewById(R.id.email);
        inputEmail.setTypeface(montserrat_regular);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnReset.setTypeface(montserrat_regular);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setTypeface(montserrat_regular);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtMain = (TextView) findViewById(R.id.text_forget);
        txtMain.setTypeface(montserrat_regular);
        txtHint = (TextView) findViewById(R.id.text_forget_hint);
        txtHint.setTypeface(montserrat_regular);
        auth = FirebaseAuth.getInstance();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "We have sent you mail to reset your password", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Failed to send reset password mail", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
        if (!UtilityMethods.isNetworkAvailable()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.reset_activity), "No internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}