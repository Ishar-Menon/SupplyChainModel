package com.e.ishar.supplychainmodel2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText userName,userPassword,userEmail;
    private Button regButton;
    private RadioButton rdFarmer,rdVendor,rdUser;
    private FirebaseAuth firebaseAuth;
    String name,email,password;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        role = "";
        userName =(EditText)findViewById(R.id.rgname);
        userPassword = (EditText)findViewById(R.id.rgPassword);
        userEmail = (EditText)findViewById(R.id.rgEmail);
        regButton = (Button)findViewById(R.id.rdButton);
        rdFarmer = (RadioButton)findViewById(R.id.rdFarmer);
        rdVendor = (RadioButton)findViewById(R.id.rdVendor);
        rdUser = (RadioButton)findViewById(R.id.rdUser);
        firebaseAuth = FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //store in database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    sendUserData();
                                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegistrationActivity.this,LoginScreenActivity.class));
                                }
                                else{
                                    Toast.makeText(RegistrationActivity.this,"Registration unsucessful as" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                    Toast.makeText(RegistrationActivity.this, "data filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validate(){
            Boolean result = false;
            name = userName.getText().toString();
            password = userPassword.getText().toString();
            email = userEmail.getText().toString();
            if(rdFarmer.isChecked()){
                role = "Farmer";
            }
            else if(rdVendor.isChecked()){
                role = "Vendor";
            }
            else if(rdUser.isChecked()){
                role = "User";
            }
            if(name.isEmpty() || password.isEmpty() || email.isEmpty() ){
                Toast.makeText(RegistrationActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            }
            else{
                result = true;
            }
            return result;
    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = firebaseDatabase.getReference(firebaseAuth.getUid());
        userProfile userprofile = new userProfile(role,email,name);
        myref.setValue(userprofile);
    }
}
