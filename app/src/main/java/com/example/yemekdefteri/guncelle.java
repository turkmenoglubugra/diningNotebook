package com.example.yemekdefteri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class guncelle extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi = null;
    private Button btnGuncelle = null;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_guncelle);

        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        btnGuncelle = (Button) findViewById(R.id.btnYeniYemekGuncelle);

        Intent myIntent = getIntent();
        id = Integer.valueOf(myIntent.getStringExtra("id"));
        String firstKeyName = myIntent.getStringExtra("firstKeyName");
        String secondKeyName= myIntent.getStringExtra("secondKeyName");
        yemekAdi.setText(firstKeyName);
        yemekTarifi.setText(secondKeyName);

        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String adi = yemekAdi.getText().toString().trim();
                    String tarif = yemekTarifi.getText().toString().trim();
                    if(adi.equals("") || tarif.equals("")) {
                        Toast.makeText(getApplicationContext(),"YEMEK ADI VE YEMEK TARİFİ ALANLARI DOLDURULMALIDIR!", Toast.LENGTH_SHORT).show();
                    } else {
                        Database vt = new Database(guncelle.this);
                        vt.VeriDuzenle(id,adi,tarif);
                        Toast.makeText(getApplicationContext(),"BAŞARIYLA KAYIT GÜNCELLENDİ!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"HATA OLUŞTU!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
