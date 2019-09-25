package com.e.ishar.supplychainmodel2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserActivity extends AppCompatActivity {
    public void switchToVendorStatus1(View view){
        Intent intent = new Intent(UserActivity.this,vendorStatus.class);
        startActivity(intent);
    }
    public void switchToFinalFarmerStatus(View view){
        Intent intent = new Intent(UserActivity.this,FinalFarmerStatus.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
}
