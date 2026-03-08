package com.example.smart_city;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class AFragment extends Fragment {


    private CardView cateringCard, carRepairCard, plumbingCard, pesticideCard, supportCard;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        // Initialize CardViews
        cateringCard = view.findViewById(R.id.card_catering);
        carRepairCard = view.findViewById(R.id.card_car_repair);
        plumbingCard = view.findViewById(R.id.card_plumbing);
        pesticideCard = view.findViewById(R.id.card_pesticide);
        supportCard = view.findViewById(R.id.card_my_account);

        // Set click listeners for each CardView
        cateringCard.setOnClickListener(v -> openActivity(CateringActivity.class));
        carRepairCard.setOnClickListener(v -> openActivity(CarRepairActivity.class));
        plumbingCard.setOnClickListener(v -> openActivity(PlumbingActivity.class));
        pesticideCard.setOnClickListener(v -> openActivity(PesticideActivity.class));
        supportCard.setOnClickListener(v -> openActivity(Support.class));

        return view;
    }

    // Method to start a new activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }
}