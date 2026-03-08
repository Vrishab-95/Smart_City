package com.example.smart_city;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Support extends AppCompatActivity {

    EditText ed1;
    Button bt1;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support);

        ed1 = findViewById(R.id.send_message);
        bt1 = findViewById(R.id.send_btn);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for SMS permission
                if (ContextCompat.checkSelfPermission(Support.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                    ed1.setText("");
                } else {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions(Support.this,
                            new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    // Function to send SMS
    private void sendSMS() {
        String message = ed1.getText().toString().trim();
        String phoneNumber = "9699039675"; // Change this to the actual recipient's number

        if (!message.isEmpty()) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(Support.this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(Support.this, "SMS Sending Failed!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Support.this, "Enter a message!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}