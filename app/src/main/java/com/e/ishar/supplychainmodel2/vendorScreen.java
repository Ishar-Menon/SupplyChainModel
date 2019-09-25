package com.e.ishar.supplychainmodel2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class vendorScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_screen);
    }
    public void switchToVEndorOwnStatus(View view){
        Intent intent = new Intent(this,vendorStatus.class);
        startActivity(intent);
    }
    public void switchToFarmerFinal(View view){
        Intent intent = new Intent(this,FinalFarmerStatus.class);
        startActivity(intent);
    }
}
