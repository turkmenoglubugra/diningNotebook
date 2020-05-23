package com.example.yemekdefteri;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class yeniYemekActivity  extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi, malzemeler = null;
    private ImageView yemekResmi;
    private Button btnKaydet, btnTemizle = null;
    private int GALLERY_REQUEST = 1;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeni_yemek_page);

        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        malzemeler = (EditText) findViewById(R.id.malzemelerText);
        btnKaydet = (Button) findViewById(R.id.btnYeniYemekEkle);
        yemekResmi = (ImageView) findViewById(R.id.yemekResmiImageView);
        btnTemizle = (Button) findViewById(R.id.btnTemizle);

        btnTemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yemekAdi.setText("");
                malzemeler.setText("");
                yemekTarifi.setText("");
                yemekResmi.setImageBitmap(null);
            }
        });
        yemekResmi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String adi = yemekAdi.getText().toString().trim();
                    String malzeme = malzemeler.getText().toString().trim();
                    String tarif = yemekTarifi.getText().toString().trim();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    byte[] data = outputStream.toByteArray();

                    if(adi.equals("") || tarif.equals("") || malzeme.equals("") ) {
                        Toast.makeText(getApplicationContext(),"YEMEK ADI, MALZEMELER VE YEMEK TARİFİ ALANLARI DOLDURULMALIDIR!", Toast.LENGTH_SHORT).show();
                    } else {
                        Database vt = new Database(yeniYemekActivity.this);
                        vt.VeriEkle(adi, malzeme, tarif, data);
                        yemekAdi.setText("");
                        malzemeler.setText("");
                        yemekTarifi.setText("");
                        yemekResmi.setImageBitmap(null);
                        Toast.makeText(getApplicationContext(),"BAŞARIYLA KAYIT OLUŞTURULDU!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"HATA OLUŞTU!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(imageUri);//You can get an inputStream using any IO API
                byte[] bytes;
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                try {
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bytes = output.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                yemekResmi.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
