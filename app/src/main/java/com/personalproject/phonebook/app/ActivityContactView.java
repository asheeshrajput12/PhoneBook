package com.personalproject.phonebook.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;

import com.personalproject.phonebook.R;

public class ActivityContactView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

    }
    public void gmailButton(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        if(intent != null){
            startActivity(intent);
        }
    }
    public void smsButton(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(this));
        if(intent != null){
            startActivity(intent);
        }
    }
}