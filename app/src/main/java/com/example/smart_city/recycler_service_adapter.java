package com.example.smart_city;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class recycler_service_adapter extends RecyclerView.Adapter<recycler_service_adapter.ViewHolder> {
    Context context;
    ArrayList<service_model> array_service_info;

    public recycler_service_adapter(Context context, ArrayList<service_model> array_service_info) {
        this.context = context;
        this.array_service_info = array_service_info;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catering_recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        service_model service = array_service_info.get(position);
        holder.txt_service_name.setText(service.service_name);
        holder.txt_service_description.setText(service.service_description);
        holder.txt_service_contact_no.setText("Contact: " + service.contact_no);
        holder.txt_service_location.setText("Location: " + service.location);

        setupWhatsAppIntent(holder.txt_service_contact_no, service.getContactNo());

        // Load images dynamically
        List<String> imageUrls = service.imageUrls;
        ImageView[] imageViews = {holder.imageView1, holder.imageView2, holder.imageView3, holder.imageView4, holder.imageView5};

        for (int i = 0; i < imageViews.length; i++) {
            if (i < imageUrls.size()) {
                String imageUrl = imageUrls.get(i);
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.load_gif)
                        .into(imageViews[i]);


                // Set click listener for fullscreen image view
                imageViews[i].setOnClickListener(v -> {
                    if (context instanceof AppCompatActivity) {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        FullscreenImageFragment fragment = FullscreenImageFragment.newInstance(imageUrl);
                        fragment.show(activity.getSupportFragmentManager(), "fullscreenImage");
                    } else {
                        Toast.makeText(context, "Error: Cannot open fullscreen image", Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                imageViews[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return array_service_info.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_service_name, txt_service_description, txt_service_contact_no, txt_service_location;
        ImageView imageView1, imageView2, imageView3, imageView4, imageView5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_service_name = itemView.findViewById(R.id.service_name);
            txt_service_description = itemView.findViewById(R.id.service_description);
            txt_service_contact_no = itemView.findViewById(R.id.service_contact_no);
            txt_service_location = itemView.findViewById(R.id.service_location);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView3 = itemView.findViewById(R.id.imageView3);
            imageView4 = itemView.findViewById(R.id.imageView4);
            imageView5 = itemView.findViewById(R.id.imageView5);
        }
    }

    // Method to handle WhatsApp intent
    private void setupWhatsAppIntent(TextView textView, String phoneNumber) {
        textView.setOnClickListener(v -> {
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                Toast.makeText(textView.getContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String formattedNumber = phoneNumber.replaceAll("[^0-9]", ""); // Remove non-numeric characters
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + formattedNumber));
                textView.getContext().startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(textView.getContext(), "WhatsApp is not installed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
