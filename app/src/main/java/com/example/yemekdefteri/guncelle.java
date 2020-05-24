package com.example.yemekdefteri;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import java.util.List;

public class guncelle extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi,malzemeler = null;
    private Button btnGuncelle, btnTemizle = null;
    private int id;
    private ImageView yemekResmi;
    private int GALLERY_REQUEST = 1;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_guncelle);

        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        malzemeler = (EditText) findViewById(R.id.malzemelerText);
        btnGuncelle = (Button) findViewById(R.id.btnYeniYemekGuncelle);
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

        Intent myIntent = getIntent();
        Database vt = new Database(guncelle.this);
        List<Yemek> list = vt.VeriListele();
        id = myIntent.getIntExtra("id",0);
        for (Yemek ws : list) {
            if(ws.getId() == id){
                yemekAdi.setText(ws.getYemekAdi());
                malzemeler.setText(ws.getMalzeme());
                yemekTarifi.setText(ws.getTarif());
                yemekResmi.setImageBitmap(ws.getResim() == null ? null : BitmapFactory.decodeByteArray(ws.getResim(), 0, ws.getResim().length));
            }
        }

        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    byte [] data = null;
                    String adi = yemekAdi.getText().toString().trim();
                    String malzeme = malzemeler.getText().toString().trim();
                    String tarif = yemekTarifi.getText().toString().trim();
                    if(((BitmapDrawable)yemekResmi.getDrawable()).getBitmap() != null) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        ((BitmapDrawable)yemekResmi.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                        data = outputStream.toByteArray();
                    }
                    if(adi.equals("") || tarif.equals("") || malzeme.equals("")) {
                        Toast.makeText(getApplicationContext(),"YEMEK ADI, MALZEMELER VE YEMEK TARİFİ ALANLARI DOLDURULMALIDIR!", Toast.LENGTH_SHORT).show();
                    } else {
                        Database vt = new Database(guncelle.this);
                        vt.VeriDuzenle(id,adi ,malzeme, tarif,data);
                        Toast.makeText(getApplicationContext(),"BAŞARIYLA KAYIT GÜNCELLENDİ!", Toast.LENGTH_SHORT).show();
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
