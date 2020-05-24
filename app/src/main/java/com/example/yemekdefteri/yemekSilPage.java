package com.example.yemekdefteri;

import android.database.CursorWindow;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yemekSilPage  extends AppCompatActivity {
    private Button btnSil;
    private int idBul = -1;
    private ListView veriListele;
    private  List<Yemek> list;
    private List<Integer> ids = new ArrayList<Integer>();
    private EditText arama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_sil);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        veriListele = (ListView) findViewById(R.id.yemekListe) ;
        btnSil = (Button) findViewById(R.id.btnSil);

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
                            if(st.getYemekAdi().contains(s.toString().trim())){
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

        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                            .setTitleText("Silindi!")
                                            .setContentText("Kayıt başarıyla silindi!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
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

        veriListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idBul = ids.get(position);
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
