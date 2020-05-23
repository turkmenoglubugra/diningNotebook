package com.example.yemekdefteri;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class yemekInceleActivity extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi, malzemeler= null;
    private ImageView yemekResmi;
    private  Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_incele);
        myIntent = getIntent();

        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        malzemeler = (EditText) findViewById(R.id.malzemelerText);
        yemekResmi = (ImageView) findViewById(R.id.yemekResmiImageView);
        Listele();

    }
    public void Listele(){
        Database vt = new Database(yemekInceleActivity.this);
        List<Yemek> list = vt.VeriListele();
        int i = myIntent.getIntExtra("id",0);
        for (Yemek ws : list) {
            if(ws.getId() == i){
                yemekAdi.setText(ws.getYemekAdi());
                malzemeler.setText(ws.getMalzeme());
                yemekTarifi.setText(ws.getTarif());
                yemekResmi.setImageBitmap(ws.getResim() == null ? null : BitmapFactory.decodeByteArray(ws.getResim(), 0, ws.getResim().length));
            }
        }
    }
}
