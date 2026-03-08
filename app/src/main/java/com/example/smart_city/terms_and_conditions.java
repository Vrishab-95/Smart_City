package com.example.smart_city;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class terms_and_conditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terms_and_conditions);

        Button b1, b2;
        b1 = findViewById(R.id.btn_agree); // agree
        b2 = findViewById(R.id.btn_disagree); // disagree

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(terms_and_conditions.this, homepage.class);
                intent.putExtra("load_fragment", "AFragment");
                startActivity(intent);
                finish();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(terms_and_conditions.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}