package com.e.ishar.supplychainmodel2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class transitionActivity extends AppCompatActivity {
    public void switchVendorScreen(View view){
        Intent intent = new Intent(this,vendorScreen.class);
        startActivity(intent);
    }
    public void switchToUserActivity(View view){
        Intent intent = new Intent(this,UserActivity.class);
        startActivity(intent);
    }
    public void switchToFamerStatus(View view){
        Intent intent = new Intent(this,FinalFarmerStatus.class);
        startActivity(intent);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
    }
}
