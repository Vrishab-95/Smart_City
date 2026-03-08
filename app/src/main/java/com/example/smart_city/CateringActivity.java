package com.example.smart_city;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CateringActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<service_model> array_service_info = new ArrayList<>();
    recycler_service_adapter adapter;

    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catering);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.Recyclerview);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new recycler_service_adapter(this, array_service_info);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        fetchCateringServices();
    }

    private void fetchCateringServices() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        CollectionReference servicesRef = db.collection("businesses");

        servicesRef.whereEqualTo("business_type", "Catering").get().addOnSuccessListener(queryDocumentSnapshots -> {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (queryDocumentSnapshots.isEmpty()) {
                Log.e("Firebase", "No Catering services found in Firestore.");
                Toast.makeText(this, "No services found!", Toast.LENGTH_SHORT).show();
                return;
            }

            array_service_info.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                String service_name = doc.getString("businessName");
                String service_description = doc.getString("businessDesc");
                String contact_no = doc.getString("contactNo");
                String location = doc.getString("location");
                String category = doc.getString("business_type");

                Log.d("Firebase", "Catering Service Found: " + service_name);

                List<String> imageUrls = new ArrayList<>();
                StorageReference imageRef = storage.getReference().child("business_images/" + doc.getId());

                imageRef.listAll().addOnSuccessListener(listResult -> {
                    for (StorageReference fileRef : listResult.getItems()) {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUrls.add(uri.toString());

                            if (imageUrls.size() == listResult.getItems().size()) {
                                array_service_info.add(new service_model(service_name, service_description, contact_no, location, category, imageUrls));
                                adapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(e -> Log.e("Firebase", "Image URL fetch failed", e));
                    }
                }).addOnFailureListener(e -> Log.e("Firebase", "Failed to fetch image list", e));
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Log.e("Firebase", "Error fetching services", e);
            Toast.makeText(this, "Error fetching services", Toast.LENGTH_SHORT).show();
        });
    }
}