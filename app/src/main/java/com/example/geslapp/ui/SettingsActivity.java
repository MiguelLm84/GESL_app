package com.example.geslapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.geslapp.R;


@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}