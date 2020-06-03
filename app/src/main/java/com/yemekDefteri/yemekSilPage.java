package com.yemekDefteri;

import android.database.CursorWindow;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.yemekDefteri.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yemekSilPage  extends AppCompatActivity {
    private int idBul = -1;
    private ListView veriListele;
    private  List<Yemek> list;
    private List<Integer> ids = new ArrayList<Integer>();
    private EditText arama;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_sil);

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


        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        veriListele = (ListView) findViewById(R.id.yemekListe) ;

        arama = (EditText) findViewById(R.id.aramaText);
        arama.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                List<Yemek> listChange = new ArrayList<Yemek>();
                if(s.toString().trim().equals("")) {
                    Listele();
                } else {
                    {
                        Database vt = new Database(yemekSilPage.this);
                        list = vt.VeriListele();
                        for(Yemek st : list) {
                            if(st.getYemekAdi().toUpperCase().contains(s.toString().toUpperCase().trim())){
                                listChange.add(st);
                            }
                        }
                        ids.clear();
                        for (Yemek ws : listChange) {
                            ids.add(ws.getId());
                        }
                        yemekAdapter adapter = new yemekAdapter(yemekSilPage.this, listChange);
                        veriListele.setAdapter(adapter);
                    }
                }
                idBul = -1;
            }
        });
        Listele();

        veriListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idBul = ids.get(position);
                    new SweetAlertDialog(yemekSilPage.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Emin Misiniz?")
                            .setContentText("Kayıt geri getirilemeyecektir!")
                            .setConfirmText("Evet, kaydı sil!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    if(idBul == -1){
                                        sDialog
                                                .setTitleText("Uyarı!")
                                                .setContentText("Lütfen listeden bir kayıt seçiniz!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        return;
                                    }
                                    try {
                                        Database vt = new Database(yemekSilPage.this);
                                        vt.VeriSil(idBul);
                                        //Sildikten Sonra tekrardan listeliyoruz
                                        List<Yemek> listChange = new ArrayList<Yemek>();
                                        if(arama.getText().toString().trim().equals("")) {
                                            Listele();
                                        } else {
                                            {
                                                Database vt2 = new Database(yemekSilPage.this);
                                                list = vt2.VeriListele();
                                                for(Yemek st : list) {
                                                    if(st.getYemekAdi().toUpperCase().trim().contains(arama.getText().toString().toUpperCase().trim())){
                                                        listChange.add(st);
                                                    }
                                                }
                                                ids.clear();
                                                for (Yemek ws : listChange) {
                                                    ids.add(ws.getId());
                                                }
                                                yemekAdapter adapter = new yemekAdapter(yemekSilPage.this, listChange);
                                                veriListele.setAdapter(adapter);
                                            }
                                        }
                                        idBul = -1;
                                        sDialog
                                                .setTitleText("Başarılı!")
                                                .setContentText("Kayıt başarıyla silindi!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        } else {
                                            Log.d("TAG", "The interstitial wasn't loaded yet.");
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
                    // VeriTabanı classımızı tanımlıyoruz
            }
        });
    }

    public void Listele(){
        Database vt = new Database(yemekSilPage.this);
        list = vt.VeriListele();
        ids.clear();
        for (Yemek ws : list) {
            ids.add(ws.getId());
        }
        yemekAdapter adapter = new yemekAdapter(yemekSilPage.this, list);
        veriListele.setAdapter(adapter);
    }

}
