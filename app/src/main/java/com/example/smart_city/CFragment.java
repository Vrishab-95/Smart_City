package com.example.smart_city;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CFragment extends Fragment {

    private static final int PICK_IMAGES_REQUEST = 1;
    private static final int LOCATION_PICKER_REQUEST = 2;
    private static final int MAX_IMAGES = 5;

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextBusinessName,
            editTextBusinessDesc, editTextContactNo, editTextLocation;
    private Spinner spinnerServiceType;
    private ImageView[] imageViews = new ImageView[MAX_IMAGES];
    private Button btnSelectImages, btnSelectLocation, btnRegister;
    private ProgressBar progressBar;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private double selectedLatitude, selectedLongitude;

    public CFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c, container, false);

        // Initialize Views
        editTextUsername = view.findViewById(R.id.edittext_username);
        editTextEmail = view.findViewById(R.id.edittext_email);
        editTextPassword = view.findViewById(R.id.edittext_password);
        editTextBusinessName = view.findViewById(R.id.edittext_business_name);
        editTextBusinessDesc = view.findViewById(R.id.edittext_business_desc);
        editTextContactNo = view.findViewById(R.id.edittext_contact_no);
        editTextLocation = view.findViewById(R.id.edittext_location);
        btnSelectImages = view.findViewById(R.id.btnSelectImages);
        btnSelectLocation = view.findViewById(R.id.btnSelectLocation);
        btnRegister = view.findViewById(R.id.button_register);
        progressBar = view.findViewById(R.id.progressBar);

        // Image Views
        imageViews[0] = view.findViewById(R.id.imageView1);
        imageViews[1] = view.findViewById(R.id.imageView2);
        imageViews[2] = view.findViewById(R.id.imageView3);
        imageViews[3] = view.findViewById(R.id.imageView4);
        imageViews[4] = view.findViewById(R.id.imageView5);

        spinnerServiceType = view.findViewById(R.id.spinner_service_type);

        showHidePassword(view); // Show/hide password function

        // Set up Spinner
        String[] serviceTypes = {"Catering", "Pesticide", "Car Repair", "Plumbing"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, serviceTypes);
        spinnerServiceType.setAdapter(adapter);

        // Image Selection
        btnSelectImages.setOnClickListener(v -> pickImagesFromGallery());

        // Location Picker
        btnSelectLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivityForResult(intent, LOCATION_PICKER_REQUEST);
        });

        // Register Button Click
        btnRegister.setOnClickListener(v -> registerUser());

        return view;
    }

    private void showHidePassword(View v) {
        ImageView imageViewShowHidepwd = v.findViewById(R.id.imageview_show_hide_pwd);
        imageViewShowHidepwd.setImageResource(R.drawable.ic_hide_pwd);

        imageViewShowHidepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidepwd.setImageResource(R.drawable.ic_hide_pwd); // Change icon
                } else {
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidepwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
    }


    private void pickImagesFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGES_REQUEST);
    }

    private void displaySelectedImages() {
        for (ImageView imageView : imageViews) {
            imageView.setImageDrawable(null);
        }

        for (int i = 0; i < imageUris.size() && i < MAX_IMAGES; i++) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUris.get(i));
                imageViews[i].setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Convert latitude & longitude into an address
    private void getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                editTextLocation.setText(address.getAddressLine(0));
            } else {
                editTextLocation.setText("Unknown Location");
            }
        } catch (IOException e) {
            e.printStackTrace();
            editTextLocation.setText("Error fetching address");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                imageUris.clear();

                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count && i < MAX_IMAGES; i++) {
                        imageUris.add(data.getClipData().getItemAt(i).getUri());
                    }
                } else if (data.getData() != null) {
                    imageUris.add(data.getData());
                }

                displaySelectedImages();
            }
        }

        if (requestCode == LOCATION_PICKER_REQUEST && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                selectedLatitude = data.getDoubleExtra("latitude", 0.0);
                selectedLongitude = data.getDoubleExtra("longitude", 0.0);
                getAddressFromCoordinates(selectedLatitude, selectedLongitude);
            }
        }
    }

    private void registerUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated!", Toast.LENGTH_SHORT).show();
            return;
        }

        String businessName = editTextBusinessName.getText().toString().trim();
        String businessDesc = editTextBusinessDesc.getText().toString().trim();
        String contactNo = editTextContactNo.getText().toString().trim();
        String location = editTextLocation.getText().toString();

        if (businessName.isEmpty() || businessDesc.isEmpty() || contactNo.isEmpty() || imageUris.isEmpty() || location.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields and select at least one image!", Toast.LENGTH_SHORT).show();
            return;
        }

        String documentId = db.collection("businesses").document().getId();

        for (Uri imageUri : imageUris) {
            StorageReference imageRef = storage.getReference("business_images/" + documentId + "/" + imageUri.getLastPathSegment());
            imageRef.putFile(imageUri);
        }

        Map<String, Object> businessData = new HashMap<>();
        businessData.put("userId", user.getUid());
        businessData.put("businessName", businessName);
        businessData.put("businessDesc", businessDesc);
        businessData.put("contactNo", contactNo);
        businessData.put("location", location);
        businessData.put("latitude", selectedLatitude);
        businessData.put("longitude", selectedLongitude);
        businessData.put("business_type", spinnerServiceType.getSelectedItem().toString());

        db.collection("businesses").document(documentId).set(businessData)
                .addOnSuccessListener(aVoid -> {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Business Setup Successful")
                            .setMessage("Your business has been registered successfully. It may take some time before it becomes visible in the app.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                Intent intent = new Intent(getActivity(), homepage.class);
                                startActivity(intent);
                                getActivity().finish();
                            })
                            .setCancelable(false)
                            .show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
