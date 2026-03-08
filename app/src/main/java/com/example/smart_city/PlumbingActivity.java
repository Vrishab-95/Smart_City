    package com.example.smart_city;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ProgressBar;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.annotation.NonNull;
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

    public class PlumbingActivity extends AppCompatActivity {
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
            setContentView(R.layout.activity_plumbing);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            recyclerView = findViewById(R.id.Recyclerview);
            progressBar = findViewById(R.id.progressBar); // Initialize ProgressBar

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new recycler_service_adapter(this, array_service_info);
            recyclerView.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();
            storage = FirebaseStorage.getInstance();

            fetchServices();
        }

        private void fetchServices() {
            progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
            recyclerView.setVisibility(View.GONE); // Hide RecyclerView initially

            CollectionReference servicesRef = db.collection("businesses");

            servicesRef.whereEqualTo("business_type", "Plumbing").get().addOnSuccessListener(queryDocumentSnapshots -> {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar when data is fetched
                recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView

                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("Firebase", "No plumbing services found in Firestore.");
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

                    Log.d("Firebase", "plumbing Service Found: " + service_name);

                    List<String> imageUrls = new ArrayList<>();
                    StorageReference imageRef = storage.getReference().child("business_images/" + doc.getId());

                    imageRef.listAll().addOnSuccessListener(listResult -> {
                        if (listResult.getItems().isEmpty()) {
                            Log.e("Firebase", "No images found for " + service_name);
                        }

                        for (StorageReference fileRef : listResult.getItems()) {
                            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                imageUrls.add(uri.toString());
                                Log.d("Firebase", "Loaded Image: " + uri.toString());

                                if (imageUrls.size() == listResult.getItems().size()) {
                                    array_service_info.add(new service_model(service_name, service_description, contact_no, location, category, imageUrls));
                                    adapter.notifyDataSetChanged();
                                    Log.d("Firebase", "Added plumbing service to list: " + service_name);
                                }
                            }).addOnFailureListener(e -> Log.e("Firebase", "Image URL fetch failed", e));
                        }
                    }).addOnFailureListener(e -> Log.e("Firebase", "Failed to fetch image list", e));
                }
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar on failure
                recyclerView.setVisibility(View.VISIBLE);
                Log.e("Firebase", "Error fetching services", e);
                Toast.makeText(this, "Error fetching services", Toast.LENGTH_SHORT).show();
            });
        }
    }
