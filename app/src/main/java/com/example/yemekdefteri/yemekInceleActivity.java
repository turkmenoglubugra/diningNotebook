package com.example.yemekdefteri;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class yemekInceleActivity extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi, malzemeler= null;
    private ImageView yemekResmi;
    private  Intent myIntent;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_incele);
        myIntent = getIntent();

        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        malzemeler = (EditText) findViewById(R.id.malzemelerText);
        yemekResmi = (ImageView) findViewById(R.id.yemekResmiImageView);

        yemekResmi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(i == 0) {
                    View view = findViewById(R.id.yemekResmiImageView);
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    view.setLayoutParams(layoutParams);
                    i = 1;
                } else {
                    View view = findViewById(R.id.yemekResmiImageView);
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = 390;
                    layoutParams.height = 350;
                    view.setLayoutParams(layoutParams);
                    i = 0;
                }
            }
        });

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
                if(ws.getResim() != null){
                    Bitmap bitmap =  BitmapFactory.decodeByteArray(ws.getResim(), 0, ws.getResim().length);
                    Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
                    yemekResmi.setImageBitmap(circularBitmap);
                }

            }
        }
    }
}
