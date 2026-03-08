package com.example.smart_city;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class userProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        CardView cateringCard = findViewById(R.id.cateringCard);
        CardView carRepairCard = findViewById(R.id.carRepairCard);
        CardView plumbingCard = findViewById(R.id.plumbingCard);
        CardView pesticideCard = findViewById(R.id.pesticideCard);
        CardView unknownServiceCard = findViewById(R.id.unknownServiceCard);

        // Set click listeners for each CardView
//        cateringCard.setOnClickListener(view -> openActivity(CateringActivity.class));
//        carRepairCard.setOnClickListener(view -> openActivity(CarRepairActivity.class));
//        plumbingCard.setOnClickListener(view -> openActivity(PlumbingActivity.class));
//        pesticideCard.setOnClickListener(view -> openActivity(PesticideActivity.class));

        // Show toast message for the unknown service card
        unknownServiceCard.setOnClickListener(view ->
                Toast.makeText(userProfileActivity.this, "COMING SOON", Toast.LENGTH_SHORT).show()
        );
    }

    // Method to start a new activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(userProfileActivity.this, activityClass);
        startActivity(intent);
    }
}