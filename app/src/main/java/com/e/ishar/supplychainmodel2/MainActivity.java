package com.e.ishar.supplychainmodel2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    public void changeActivity(View view){
        Intent intent = new Intent(this,transitionActivity.class);
        startActivity(intent);
    }
    public void switchToRegister(View view){
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }
    public void switchToLogin(View view){
        Intent intent = new Intent(this,LoginScreenActivity.class);
        //Intent intent = new Intent(this,vendorScreen.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
