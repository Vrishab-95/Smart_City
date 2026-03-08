package com.example.smart_city;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private EditText edittextLoginemail, editTextLoginPwd;
    private ProgressBar progressBar;
    private Button button_login;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }

        edittextLoginemail = findViewById(R.id.edittext_login_email);
        editTextLoginPwd = findViewById(R.id.edittext_login_pwd);
        progressBar = findViewById(R.id.progressBar);
        button_login = findViewById(R.id.button_login);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Redirect to homepage if user is already logged in
            startActivity(new Intent(MainActivity.this, homepage.class));
            finish();
        }

        // Login button
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Forgot Password
        TextView textViewForgotPassword = findViewById(R.id.textview_forgot_password);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });

        // Register user
        TextView textViewRegister = findViewById(R.id.textview_link_register);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity.this, registeActivity.class);
                startActivity(registerActivity);
            }
        });

        showHidePassword();
    }

    // Login function
    private void loginUser() {
        String email = edittextLoginemail.getText().toString().trim();
        String password = editTextLoginPwd.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edittextLoginemail.setError("Email is required!");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextLoginPwd.setError("Password is required!");
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, homepage.class));
                            finish();
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(MainActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Forgot Password Dialog
    private void showForgotPasswordDialog() {
        EditText resetMail = new EditText(this);
        resetMail.setHint("Enter your email");

        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter your email to receive a password reset link")
                .setView(resetMail)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = resetMail.getText().toString().trim();
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Show/hide password
    private void showHidePassword() {
        ImageView imageViewShowHidepwd = findViewById(R.id.imageview_show_hide_pwd);
        imageViewShowHidepwd.setImageResource(R.drawable.ic_hide_pwd);

        imageViewShowHidepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidepwd.setImageResource(R.drawable.ic_hide_pwd);
                } else {
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidepwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
    }
}