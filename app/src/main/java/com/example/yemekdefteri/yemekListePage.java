package com.example.yemekdefteri;

import android.content.Intent;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yemekListePage  extends AppCompatActivity {
    private ListView veriListele;
    private EditText arama;
    private int idBul = -1;
    private List<Yemek> list;
    private List<String> listTable = new ArrayList<String>();
    private List<Yemek> listChange = new ArrayList<Yemek>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_listele);


        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
                e.printStackTrace();
        }
        veriListele = (ListView) findViewById(R.id.yemekListe);
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
                listChange.clear();
                if(s.toString().trim().equals("")) {
                    Listele();
                } else {
                    {
                        Database vt = new Database(yemekListePage.this);
                        list = vt.VeriListele();
                        for(Yemek st : list) {
                            if(st.getYemekAdi().toUpperCase().trim().contains(s.toString().toUpperCase().trim())){
                                listChange.add(st);
                            }
                        }
                        listTable.clear();
                        for (Yemek ws : listChange) {
                            listTable.add(ws.getYemekAdi());
                        }
                        yemekAdapter adapter = new yemekAdapter(yemekListePage.this, listChange);
                        veriListele.setAdapter(adapter);
                    }
                }
                idBul = -1;
            }
        });
        veriListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idBul = position;
                    if(idBul == -1){
                        new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Lütfen listeden bir kayıt seçiniz!")
                                .show();
                        return;
                    }
                    Yemek ws = listChange.get(idBul);
                    incele(ws.getId());
            }
        });
        Listele();
     }
    public void Listele(){
        Database vt = new Database(yemekListePage.this);
        list = vt.VeriListele();
        listTable.clear();
        listChange.clear();
        for (Yemek ws : list) {
            listChange.add(ws);
            listTable.add(ws.getYemekAdi());
        }
        yemekAdapter adapter = new yemekAdapter(yemekListePage.this, list);
        veriListele.setAdapter(adapter);
    }

    public void incele(int a0){
        Intent myIntent = new Intent(this, yemekInceleActivity.class);
        myIntent.putExtra("id", a0);
        startActivity(myIntent);
    }

}
