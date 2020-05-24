package com.example.yemekdefteri;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class guncelle extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi,malzemeler = null;
    private Button btnGuncelle, btnTemizle, btnResim = null;
    private int id;
    private ImageView yemekResmi;
    private int GALLERY_REQUEST = 1;
    private Bitmap bitmap = null;
    private int i = 0;

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
        btnResim = (Button) findViewById(R.id.btnResim);
        btnTemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog pDialog = new SweetAlertDialog(guncelle.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Temizleniyor ...");
                pDialog.setCancelable(true);
                pDialog.show();
                yemekAdi.setText("");
                malzemeler.setText("");
                yemekTarifi.setText("");
                yemekResmi.setImageBitmap(null);
                pDialog.cancel();
            }
        });
        btnResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
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
                new SweetAlertDialog(guncelle.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("EMİN MİSİNİZ?")
                        .setContentText("Kayıt Güncellenecektir!")
                        .setConfirmText("Evet, Güncelle!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
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
                        sDialog
                                .setTitleText("UYARI!")
                                .setContentText("Yemek Adı, Malzemeler ve Yemek Tarifi Alanları Doldurulmalıdır!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        return;
                    } else {
                        Database vt = new Database(guncelle.this);
                        vt.VeriDuzenle(id,adi ,malzeme, tarif,data);
                    }
                    sDialog
                            .setTitleText("GÜNCELLENDİ!")
                            .setContentText("Kayıt başarıyla güncellendi!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                } catch (Exception e) {
                    sDialog
                            .setTitleText("HATA!")
                            .setContentText("İşlem sırasında hata oluştu!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }

                            }
                        })
                        .show();
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
                Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
                yemekResmi.setImageBitmap(circularBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
