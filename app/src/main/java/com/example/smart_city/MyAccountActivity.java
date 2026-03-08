package com.example.smart_city;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAccountActivity extends AppCompatActivity {

    private TextView txtName, txtEmail;
    private Button btnchuname, btnchemail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        btnchuname = findViewById(R.id.change_uname);
        btnchemail = findViewById(R.id.change_email);

        btnchemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });

        btnchuname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });


        // Get Current User
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            txtEmail.setText("Email: " + user.getEmail());
            txtName.setText("Name: " + (user.getDisplayName() != null ? user.getDisplayName() : "Not set"));
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MyAccountActivity.this, MainActivity.class));
            finish();
        }

        // Logout Button Click
    }
}
