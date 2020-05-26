package com.example.yemekdefteri;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yeniYemekActivity  extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi, malzemeler = null;
    private ImageView yemekResmi;
    private Button btnKaydet, btnTemizle,btnResim = null;
    private int GALLERY_REQUEST = 1;
    private Bitmap bitmap = null;
    private int i = 0;

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
        btnResim = (Button) findViewById(R.id.btnResim);
        btnTemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog pDialog = new SweetAlertDialog(yeniYemekActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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

        btnResim.setOnClickListener(new View.OnClickListener() {
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
                new SweetAlertDialog(yeniYemekActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Emin Misiniz?")
                        .setContentText("Yemek kaydedilecektir!")
                        .setConfirmText("Evet, Kaydet!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                try{
                                    String adi = yemekAdi.getText().toString().trim();
                                    String malzeme = malzemeler.getText().toString().trim();
                                    String tarif = yemekTarifi.getText().toString().trim();
                                    byte[] data = null;
                                    if((yemekResmi.getDrawable())!= null) {
                                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                        ((BitmapDrawable)yemekResmi.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                                        data = outputStream.toByteArray();
                                    }

                                    if(adi.equals("")) {
                                        sDialog
                                                .setTitleText("Uyarı!")
                                                .setContentText("Yemek adı alanlanı doldurulmalıdır!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        return;
                                    } else {
                                        Database vt = new Database(yeniYemekActivity.this);
                                        vt.VeriEkle(adi, malzeme, tarif, data);
                                        yemekAdi.setText("");
                                        malzemeler.setText("");
                                        yemekTarifi.setText("");
                                        yemekResmi.setImageBitmap(null);
                                        sDialog
                                                .setTitleText("Başarılı!")
                                                .setContentText("Yemek başarıyla kaydedildi!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                } catch (Exception e) {
                                    sDialog
                                            .setTitleText("Hata!")
                                            .setContentText("Hata meydana geldi!")
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
