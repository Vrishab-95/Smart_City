package com.example.smart_city;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BFragment extends Fragment {

    private TextView textName, textEmail;
    private Button btnChangeUname, btnChangeEmail;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Bind Views
        textName = view.findViewById(R.id.txtName);
        textEmail = view.findViewById(R.id.txtEmail);
        btnChangeUname = view.findViewById(R.id.change_uname);
        btnChangeEmail = view.findViewById(R.id.change_email);

        loadUserInfo();

        // Button Click Listeners
        btnChangeEmail.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show()
        );

        btnChangeUname.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show()
        );

        return view;
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            textEmail.setText("Email: " + user.getEmail());
            textName.setText("Name: " + (user.getDisplayName() != null ? user.getDisplayName() : "No Name Available"));
        } else {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}
