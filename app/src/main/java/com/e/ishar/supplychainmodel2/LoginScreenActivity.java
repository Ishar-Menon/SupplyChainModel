package com.e.ishar.supplychainmodel2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreenActivity extends AppCompatActivity {
    private EditText lgname,lgpassword;
    private Button btSignIn;
    private TextView tvRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        lgname = (EditText)findViewById(R.id.lgname);
        lgpassword = (EditText)findViewById(R.id.lgPassword);
        btSignIn = (Button)findViewById(R.id.btSignIn);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        validate(lgname.getText().toString(),lgpassword.getText().toString());

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();



        FirebaseUser user = firebaseAuth.getCurrentUser();
        /*if(user != null){
            finish();
            startActivity(new Intent(LoginScreenActivity.this,transitionActivity.class));
        }*/

        tvRegister = (TextView)findViewById(R.id.registerTextView);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreenActivity.this,RegistrationActivity.class));
            }
        });
    }
    private void validate(String username,String userpassword){
            firebaseAuth.signInWithEmailAndPassword(username,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    userProfile userprofile = dataSnapshot.getValue(userProfile.class);
                                    String cmpRole = userprofile.getRole();
                                    if(cmpRole.equals("Farmer")){
                                        startActivity(new Intent(LoginScreenActivity.this,FinalFarmerStatus.class));
                                    }
                                    else if(cmpRole.equals("Vendor")){
                                        startActivity(new Intent(LoginScreenActivity.this,vendorScreen.class));
                                        Toast.makeText(LoginScreenActivity.this,cmpRole, Toast.LENGTH_SHORT).show();
                                    }
                                    else if(cmpRole.equals("User")){
                                        startActivity(new Intent(LoginScreenActivity.this,UserActivity.class));
                                        Toast.makeText(LoginScreenActivity.this,cmpRole, Toast.LENGTH_SHORT).show();
                                    }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //startActivity(new Intent(LoginScreenActivity.this,transitionActivity.class));
                        Toast.makeText(LoginScreenActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginScreenActivity.this, "Login Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
