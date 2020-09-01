package com.yemekDefteri;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yemekInceleActivity extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi, malzemeler = null;
    private ImageView yemekResmi;
    private Intent myIntent;
    private int i = 0;
    private AdView mAdView;
    private AdView adView;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_incele);
        myIntent = getIntent();


        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        malzemeler = (EditText) findViewById(R.id.malzemelerText);
        yemekResmi = (ImageView) findViewById(R.id.yemekResmiImageView);
        adView = (AdView) findViewById(R.id.adView);


        yemekResmi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (i == 0) {
                    View view = findViewById(R.id.yemekResmiImageView);
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    view.setLayoutParams(layoutParams);
                    i = 1;
                    adView.setVisibility(View.INVISIBLE);
                } else {
                    View view = findViewById(R.id.yemekResmiImageView);
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = 390;
                    layoutParams.height = 350;
                    view.setLayoutParams(layoutParams);
                    i = 0;
                    adView.setVisibility(View.VISIBLE);
                }
            }
        });

        Listele();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

                    file = new File(Environment.getExternalStorageDirectory(), "/"+yemekAdi.getText().toString().toUpperCase()+".pdf");
                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                        myPdfDocument.close();
                        String path = Environment.getExternalStorageDirectory() + "/" + yemekAdi.getText().toString().toUpperCase() + ".pdf";
                        new SweetAlertDialog(yemekInceleActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Pdf Oluşturuldu ("+path+")")
                                .setContentText("Paylaşmak istiyor musunuz?")
                                .setConfirmText("Evet, paylaş!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        try {
                                            if(file.exists()) {
                                                Uri pdfUri = FileProvider.getUriForFile(
                                                        yemekInceleActivity.this,
                                                        "com.yemekDefteri.provider", //(use your app signature + ".provider" )
                                                        file);

                                                Intent intent = ShareCompat.IntentBuilder.from(yemekInceleActivity.this)
                                                        .setType("application/pdf")
                                                        .setStream(pdfUri)
                                                        .setChooserTitle("Choose bar")
                                                        .createChooserIntent()
                                                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                                startActivity(intent);
                                            }
                                        } catch (Exception e){
                                            sDialog
                                                    .setTitleText("Hata!")
                                                    .setContentText("İşlem sırasında hata oluştu!")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(null)
                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        }

                                    }
                                })
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public void Listele() {
        Database vt = new Database(yemekInceleActivity.this);
        List<Yemek> list = vt.VeriListele();
        int i = myIntent.getIntExtra("id", 0);
        for (Yemek ws : list) {
            if (ws.getId() == i) {
                yemekAdi.setText(ws.getYemekAdi());
                malzemeler.setText(ws.getMalzeme());
                yemekTarifi.setText(ws.getTarif());
                if (ws.getResim() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(ws.getResim(), 0, ws.getResim().length);
                    Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
                    yemekResmi.setImageBitmap(circularBitmap);
                }

            }
        }

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
