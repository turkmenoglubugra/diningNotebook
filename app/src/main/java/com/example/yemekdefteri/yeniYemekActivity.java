package com.example.yemekdefteri;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class yeniYemekActivity  extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi = null;
    private Button btnKaydet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeni_yemek_page);

        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        btnKaydet = (Button) findViewById(R.id.btnYeniYemekEkle);

        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String adi = yemekAdi.getText().toString().trim();
                    String tarif = yemekTarifi.getText().toString().trim();
                    if(adi.equals("") || tarif.equals("")) {
                        Toast.makeText(getApplicationContext(),"YEMEK ADI VE YEMEK TARİFİ ALANLARI DOLDURULMALIDIR!", Toast.LENGTH_SHORT).show();
                    } else {
                        Database vt = new Database(yeniYemekActivity.this);
                        vt.VeriEkle(adi,tarif);
                        yemekAdi.setText("");
                        yemekTarifi.setText("");
                        Toast.makeText(getApplicationContext(),"BAŞARIYLA KAYIT OLUŞTURULDU!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"HATA OLUŞTU!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
