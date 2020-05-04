package com.example.yemekdefteri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainpage extends AppCompatActivity {
    private Button yeniYemek;
    private Button yemekListesi;
    private Button yemekSil;
    private Button yemekDuzenle;
    private Button hakkinda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        yeniYemek = findViewById(R.id.btnYeniYemek);
        yeniYemek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yemekEkleAc();
            }
        });

        yemekListesi = findViewById(R.id.btnYemekListesi);
        yemekListesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yemekListesiAc();
            }
        });

        yemekSil = findViewById(R.id.btnYemekKaldir);
        yemekSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yemekSilAc();
            }
        });

        yemekDuzenle = findViewById(R.id.btnYemekDuzenle);
        yemekDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yemekDuzenleAc();
            }
        });


        hakkinda = findViewById(R.id.btnHakkinda);
        hakkinda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iletisim();
            }
        });
    }

    public void yemekEkleAc(){
        Intent intent = new Intent(this, yeniYemekActivity.class );
        startActivity(intent);
    }

    public void yemekListesiAc(){
        Intent intent = new Intent(this, yemekListePage.class );
        startActivity(intent);
    }
    public void yemekSilAc(){
        Intent intent = new Intent(this, yemekSilPage.class );
        startActivity(intent);
    }

    public void yemekDuzenleAc(){
        Intent intent = new Intent(this, yemekDuzenlePage.class );
        startActivity(intent);
    }

    public  void iletisim () {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "bugrakaanturkmenoglu@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "YEMEK TARİFİ DEFTERİ APP");
        startActivity(Intent.createChooser(emailIntent, null));
    }
}
