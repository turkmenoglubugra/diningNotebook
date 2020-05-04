package com.example.yemekdefteri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class yemekInceleActivity extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_incele);

        Intent myIntent = getIntent();
        String firstKeyName = myIntent.getStringExtra("firstKeyName");
        String secondKeyName= myIntent.getStringExtra("secondKeyName");

        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);

        yemekAdi.setText(firstKeyName);
        yemekTarifi.setText(secondKeyName);
    }
}
