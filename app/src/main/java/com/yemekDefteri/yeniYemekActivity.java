package com.yemekDefteri;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.ads.AdView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
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
import java.util.ArrayList;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class yeniYemekActivity  extends AppCompatActivity {
    private EditText yemekAdi, yemekTarifi, malzemeler = null;
    private ImageView yemekResmi;
    private int GALLERY_REQUEST = 1;
    private Bitmap bitmap = null;
    private int i = 0;
    private InterstitialAd mInterstitialAd;
    private File file;
    private AdView mAdView;
    private TextView yemekAdiTextView, yemekTarifiTextView, malzemelerTextView;
    private AlertDialog.Builder alertDialogBuilderSaveYemek;
    private View popupInputDialogSaveYemek;
    private AlertDialog alertDialogAddYemek;
    private Button button_cancel_category,button_save_category;
    private AutoCompleteTextView edtCmpSaveCategory;
    private int positionSaveCat=-1;
    private String selection="";
    private List<Kategori> listCategory = new ArrayList<Kategori>();
    private List<String> listCategoryStr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeni_yemek_page);

        LayoutInflater layoutInflaterYeniYemek = LayoutInflater.from(yeniYemekActivity.this);
        popupInputDialogSaveYemek = layoutInflaterYeniYemek.inflate(R.layout.popup_input_dialog, null);
        alertDialogBuilderSaveYemek = new AlertDialog.Builder(yeniYemekActivity.this);

        alertDialogBuilderSaveYemek.setTitle(getResources().getString(R.string.kategoriEkle));
        alertDialogBuilderSaveYemek.setCancelable(false);
        alertDialogBuilderSaveYemek.setView(popupInputDialogSaveYemek);
        alertDialogAddYemek = alertDialogBuilderSaveYemek.create();

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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete( InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        TextView textView = new TextView(this);
        textView.setText(this.getResources().getString(R.string.ekranIsmi));

        yemekAdiTextView = (TextView) findViewById(R.id.yemekAdiTextView);
        yemekAdiTextView.setText(this.getResources().getString(R.string.yemekAdi));
        malzemelerTextView = (TextView) findViewById(R.id.malzemelerTextView);
        malzemelerTextView.setText(this.getResources().getString(R.string.malzemeler));
        yemekTarifiTextView = (TextView) findViewById(R.id.yemekTarifiTextView);
        yemekTarifiTextView.setText(this.getResources().getString(R.string.yemekTarifi));
        yemekAdi = (EditText) findViewById(R.id.yemekAdiText);
        yemekAdi.setHint(this.getResources().getString(R.string.burayaYaz));
        yemekTarifi = (EditText) findViewById(R.id.yemekTarifiText);
        yemekTarifi.setHint(this.getResources().getString(R.string.burayaYaz));
        malzemeler = (EditText) findViewById(R.id.malzemelerText);
        malzemeler.setHint(this.getResources().getString(R.string.burayaYaz));
        yemekResmi = (ImageView) findViewById(R.id.yemekResmiImageView);
        button_cancel_category = popupInputDialogSaveYemek.findViewById(R.id.button_cancel_category);
        button_save_category = popupInputDialogSaveYemek.findViewById(R.id.button_save_category);
        edtCmpSaveCategory = popupInputDialogSaveYemek.findViewById(R.id.edtCmpSaveCategory);

        edtCmpSaveCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selection = (String)parent.getItemAtPosition(position);
                positionSaveCat = position;
            }
        });


        button_cancel_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAddYemek.cancel();
                edtCmpSaveCategory.setSelection(0);
                edtCmpSaveCategory.setText("");
                selection = "";
                positionSaveCat = -1;
            }
        });


        button_save_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positionSaveCat == -1) {
                    new SweetAlertDialog(yeniYemekActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Category Must Be Selected!")
                            .show();
                    return;
                }

                final String uyari = getResources().getString(R.string.uyari);
                final String yemekAlan = getResources().getString(R.string.yemekAlan);
                final String hata = getResources().getString(R.string.hata);
                final String error = getResources().getString(R.string.error);

                new SweetAlertDialog(yeniYemekActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.emin))
                        .setContentText(getResources().getString(R.string.yemekKaydet))
                        .setConfirmText(getResources().getString(R.string.evetKaydet))
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
                                                .setTitleText(uyari)
                                                .setContentText(yemekAlan)
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        alertDialogAddYemek.cancel();
                                        edtCmpSaveCategory.setSelection(0);
                                        edtCmpSaveCategory.setText("");
                                        selection = "";
                                        positionSaveCat = -1;
                                        return;
                                    } else {
                                        int idCategory = 0;
                                        for(Kategori cat : listCategory){
                                            if(cat.getName().trim().equals(selection.trim())){
                                                idCategory = cat.getId();
                                            }
                                        }
                                        Database vt = new Database(yeniYemekActivity.this);
                                        vt.VeriEkle(adi, malzeme, tarif, data, idCategory);
                                        yemekAdi.setText("");
                                        malzemeler.setText("");
                                        yemekTarifi.setText("");
                                        yemekResmi.setImageBitmap(null);
                                        listePage();
                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        } else {
                                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                                        }
                                        edtCmpSaveCategory.setSelection(0);
                                        edtCmpSaveCategory.setText("");
                                        alertDialogAddYemek.cancel();
                                        selection = "";
                                        positionSaveCat = -1;
                                    }
                                } catch (Exception e) {
                                    sDialog
                                            .setTitleText(hata)
                                            .setContentText(error)
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    alertDialogAddYemek.cancel();
                                    edtCmpSaveCategory.setSelection(0);
                                    edtCmpSaveCategory.setText("");
                                    selection = "";
                                    positionSaveCat = -1;
                                }
                            }
                        })
                        .show();
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

        listele();
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
                if(circularBitmap.getWidth() > circularBitmap.getHeight()) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    circularBitmap = Bitmap.createBitmap(circularBitmap, 0, 0, circularBitmap.getWidth(), circularBitmap.getHeight(), matrix, true);
                }
                circularBitmap = getResizedBitmap(circularBitmap, 600);
                yemekResmi.setImageBitmap(circularBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
   private void closeKeyboard() {
       InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
       if(this.getCurrentFocus() != null){
           inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
       }   }

   private void checkMermission() {
        final String yemekAdiBosText = this.getResources().getString(R.string.yemekAdiBos);
        final String malzemelerText = this.getResources().getString(R.string.malzemeler);
        final String yemekTarifiText = this.getResources().getString(R.string.yemekTarifi);
        final String pdfOlusturuldu = this.getResources().getString(R.string.pdfOlusturuldu);
        final String paylasmakIstiyor = this.getResources().getString(R.string.paylasmakIstiyor);
        final String evettPaylas = this.getResources().getString(R.string.evettPaylas);
        final String hayir = this.getResources().getString(R.string.hayir);
        final String evet = this.getResources().getString(R.string.evet);
        final String hata = this.getResources().getString(R.string.hata);
        final String error = this.getResources().getString(R.string.error);

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
                    if(yemekAdi.getText().toString().trim().equals("")){
                        new SweetAlertDialog(yeniYemekActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(yemekAdiBosText)
                                .show();
                        return;

                    }
                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();
                    myPaint.setTextSize(12);
                    myPaint.setStyle(Paint.Style.FILL);

                    Paint boldPaint = new Paint();
                    boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    int i = 0;
                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(400, 700, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();
                    canvas.drawText(yemekAdi.getText().toString().toUpperCase(), 10, 50, boldPaint);
                    canvas.drawText(malzemelerText, 10, 100, boldPaint);
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
                    canvas.drawText(yemekTarifiText,10, y, boldPaint);
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
                        new SweetAlertDialog(yeniYemekActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText(pdfOlusturuldu+" ("+path+")")
                                .setContentText(paylasmakIstiyor)
                                .setConfirmText(evettPaylas)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        try {
                                            if(file.exists()) {
                                                Uri pdfUri = FileProvider.getUriForFile(
                                                        yeniYemekActivity.this,
                                                        "com.yemekDefteri.provider", //(use your app signature + ".provider" )
                                                        file);

                                                Intent intent = ShareCompat.IntentBuilder.from(yeniYemekActivity.this)
                                                        .setType("application/pdf")
                                                        .setStream(pdfUri)
                                                        .setChooserTitle("Choose bar")
                                                        .createChooserIntent()
                                                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                                startActivity(intent);
                                            }
                                        } catch (Exception e){
                                            sDialog
                                                    .setTitleText(hata)
                                                    .setContentText(error)
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.yemekkaydet,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.imageAction:
                closeKeyboard();
                if((yemekResmi.getDrawable())!= null && ((BitmapDrawable)yemekResmi.getDrawable()).getBitmap() != null)  {
                    yemekResmi.setImageBitmap(null);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                }
                return true;
            case R.id.action_search_insides:
                checkMermission();
                return true;
            case R.id.temizleAction:
                closeKeyboard();
                SweetAlertDialog pDialog = new SweetAlertDialog(yeniYemekActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(this.getResources().getString(R.string.temizleniyor));
                pDialog.setCancelable(true);
                pDialog.show();
                yemekAdi.setText("");
                malzemeler.setText("");
                yemekTarifi.setText("");
                yemekResmi.setImageBitmap(null);
                pDialog.cancel();
                return true;
            case R.id.yemekKaydetAction:
                closeKeyboard();
                alertDialogAddYemek.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void listePage(){
        Intent intent = new Intent(this, yemekListePage.class );
        startActivity(intent);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void listele(){
        Database vt = new Database(yeniYemekActivity.this);
        listCategory = vt.VeriListeleKategori();
        if(listCategory.size() == 0){
            vt.VeriEkleKategori("SOUP","EN");
            vt.VeriEkleKategori("MAIN DISH","EN");
            vt.VeriEkleKategori("DESSERT","EN");
            vt.VeriEkleKategori("SALAD","EN");
            vt.VeriEkleKategori("ENTREE STARTER","EN");
            vt.VeriEkleKategori("ÇORBA","TR");
            vt.VeriEkleKategori("ANA YEMEK","TR");
            vt.VeriEkleKategori("TATLI","TR");
            vt.VeriEkleKategori("SALATA","TR");
            vt.VeriEkleKategori("ARA SICAK","TR");
            listCategory = vt.VeriListeleKategori();
            listCategoryStr = new ArrayList<String>();
            for(Kategori cur : listCategory){
                listCategoryStr.add(cur.getName());
            }
        } else {
            listCategoryStr = new ArrayList<String>();
            for(Kategori cur : listCategory){
                listCategoryStr.add(cur.getName());
            }
        }
        String[] stockArr = new String[listCategory.size()];
        stockArr = listCategoryStr.toArray(stockArr);

        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stockArr);
        edtCmpSaveCategory.setAdapter(adapterCurrency);
    }
}
