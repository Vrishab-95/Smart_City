package com.example.smart_city;

import java.util.List;

public class service_model {
    public String service_name, service_description, contact_no, location, category;
    public List<String> imageUrls; // Store image URLs from Firebase Storage

    public service_model() {
        // Default constructor required for Firestore
    }

    public service_model(String service_name, String service_description, String contact_no, String location, String category, List<String> imageUrls) {
        this.service_name = service_name;
        this.service_description = service_description;
        this.contact_no = contact_no;
        this.location = location;
        this.category = category;
        this.imageUrls = imageUrls;
    }
    public String getContactNo() {
        return contact_no;
    }


}
