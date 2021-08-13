package com.gembodev.a04firstrunsettingspreferencesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("preferences", 0);
        if (getFirstRun()) {
            setRunned();
            Toast.makeText(this, "First run", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not first run", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean getFirstRun() {
        return sharedPreferences.getBoolean("firstRun", true);
    }

    private void setRunned() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstRun", false);
        editor.commit();
    }
}