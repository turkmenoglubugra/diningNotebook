package com.yemekDefteri;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yemekDefteri.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yeniYemekActivity  extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi, malzemeler = null;
    private ImageView yemekResmi;
    private Button btnKaydet, btnTemizle,btnResim = null;
    private int GALLERY_REQUEST = 1;
    private Bitmap bitmap = null;
    private int i = 0;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeni_yemek_page);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2814589180123732/4578930417");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

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
                    btnKaydet.setVisibility(View.INVISIBLE);
                    btnResim.setVisibility(View.INVISIBLE);
                    btnTemizle.setVisibility(View.INVISIBLE);
                    i = 1;
                } else {
                    View view = findViewById(R.id.yemekResmiImageView);
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = 390;
                    layoutParams.height = 350;
                    view.setLayoutParams(layoutParams);
                    i = 0;
                    btnKaydet.setVisibility(View.VISIBLE);
                    btnResim.setVisibility(View.VISIBLE);
                    btnTemizle.setVisibility(View.VISIBLE);
                }
            }
        });

        btnResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((yemekResmi.getDrawable())!= null && ((BitmapDrawable)yemekResmi.getDrawable()).getBitmap() != null)  {
                    yemekResmi.setImageBitmap(null);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                }

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
                                    if((yemekResmi.getDrawable())!= null && ((BitmapDrawable)yemekResmi.getDrawable()).getBitmap() != null)  {
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
                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        } else {
                                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                                        }
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

    private void checkMermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()) {
                    checkMermission();
                } else if (report.areAllPermissionsGranted()) {
                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();
                    myPaint.setTextSize(12);
                    myPaint.setStyle(Paint.Style.FILL);

                    Paint boldPaint = new Paint();
                    boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    int i = 0;
                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(400, 650, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();
                    canvas.drawText(yemekAdi.getText().toString().toUpperCase(), 10, 50, boldPaint);
                    canvas.drawText("MALZEMELER", 10, 100, boldPaint);
                    String text = "";
                    int y=125;
                    for(String ws : malzemeler.getText().toString().split(" ")) {
                        i = i + 1;
                        if ((text + ws).length() > 60) {
                            canvas.drawText(text, 10, y, myPaint);
                            y = y + 20;
                            text = ws;
                        } else {
                            text = text +  " " + ws;
                            if(i == malzemeler.getText().toString().split(" ").length){
                                canvas.drawText(text, 10, y, myPaint);
                            }
                        }
                    }
                    y = y + 25;
                    canvas.drawText("YEMEK TARİFİ",10, y, boldPaint);
                    y = y + 20;
                    text ="";
                    int t = 0;
                    for(String ws2 : yemekTarifi.getText().toString().split(" ")) {
                        t = t + 1;
                        if ((text + ws2).length() > 60) {
                            canvas.drawText(text, 10, y, myPaint);
                            y = y + 20;
                            text = ws2;
                        } else {
                            text = text + " " +  ws2;
                            if(t == yemekTarifi.getText().toString().split(" ").length){
                                canvas.drawText(text, 10, y, myPaint);
                            }
                        }
                    }
                    canvas.save();
                    myPdfDocument.finishPage(myPage1);

                    File file = new File(Environment.getExternalStorageDirectory(), yemekAdi.getText().toString().toUpperCase()+".pdf");
                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    myPdfDocument.close();
                    // copy some things
                } else {
                    checkMermission();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.pdfmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search_insides:
                checkMermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
