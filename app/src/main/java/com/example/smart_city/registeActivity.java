
package com.example.smart_city;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class registeActivity extends AppCompatActivity {

    private EditText editTextRegisterEmail, editTextRegisterPwd, editTextRegisterName;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registe);

//        TextView business_owner_login=findViewById(R.id.business_owner_login);
//        business_owner_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent to_business_owner_page=new Intent(registeActivity.this, Business_owner_login.class);
//                startActivity(to_business_owner_page);
//            }
//        });



            findViews();
            showHidePwd();

            Button ButtonRegister = findViewById(R.id.button_register);
            ButtonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String textEmail = editTextRegisterEmail.getText().toString();
                    String textPassword = editTextRegisterPwd.getText().toString();
                    String textName = editTextRegisterName.getText().toString();

                    //check if all data are present before registering user
                    if(TextUtils.isEmpty(textName)){
                        Toast.makeText(registeActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        editTextRegisterName.setError("Name is required");
                        editTextRegisterName.requestFocus();

                    }else if (TextUtils.isEmpty(textEmail)){
                        Toast.makeText(registeActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                        editTextRegisterEmail.setError("Email is required");
                        editTextRegisterEmail.requestFocus();
                    }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                        Toast.makeText(registeActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                        editTextRegisterEmail.setError("Valid email is required");
                        editTextRegisterEmail.requestFocus();
                    }else if(TextUtils.isEmpty(textPassword)){
                        Toast.makeText(registeActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                        editTextRegisterPwd.setError("Password is required");
                        editTextRegisterPwd.requestFocus();
                    }else if(textPassword.length() < 6) {
                        Toast.makeText(registeActivity.this, "Password must be of atleast 6 digits", Toast.LENGTH_SHORT).show();
                        editTextRegisterPwd.setError("Password is required");
                        editTextRegisterPwd.requestFocus();
                    }else{
                        progressBar.setVisibility(View.VISIBLE );
                        registerUser(textEmail,textName,textPassword);
                    }
                }
            });


    }

    private void registerUser(String textEmail, String textName, String textPassword) {
        auth=FirebaseAuth.getInstance();//Gets current instance for firebase authentication
        auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(registeActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             //Check if user was created successfully
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser= auth.getCurrentUser();
                    if (firebaseUser!=null){
                    //update name of user
                        UserProfileChangeRequest profileUpdates= new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(registeActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        //Open user profile activity
                        Intent userProfileActivity= new Intent(registeActivity.this,terms_and_conditions.class);

                        //stop user from going back to register screen
                        userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(userProfileActivity);
                         finish();
                    }
                }
                else
                {
                    try {
                        throw task.getException();
                    }
                    catch (Exception e){
                        Toast.makeText(registeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    private void showHidePwd() {
        ImageView imageViewShowHidepwd = findViewById(R.id.imageview_show_hide_pwd);
        imageViewShowHidepwd.setImageResource(R.drawable.ic_hide_pwd);

        imageViewShowHidepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if pwd is visible then hide it
                if(editTextRegisterPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextRegisterPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidepwd.setImageResource(R.drawable.ic_hide_pwd);    //change icon
                }else{
                    editTextRegisterPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidepwd.setImageResource(R.drawable.ic_show_pwd);
                }

            }
        });


    }

    private void findViews() {
        editTextRegisterEmail = findViewById(R.id.edittext_register_email);
        editTextRegisterPwd = findViewById(R.id.edittext_register_pwd);
        editTextRegisterName = findViewById(R.id.edittext_register_name);
        progressBar = findViewById(R.id.progressBar);
    }
}

