package com.example.yemekdefteri;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yemekDuzenlePage extends AppCompatActivity {
    private int idBul = -1;
    private ListView veriListele;
    private List<Yemek> list;
    private List<Yemek> listChange = new ArrayList<Yemek>();
    private EditText arama;
    private  Button guncelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_duzenle);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        guncelle = (Button) findViewById(R.id.btnGuncelle);
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
                listChange.clear();
                if(s.toString().trim().equals("")) {
                    Listele();
                } else {
                    {
                        Database vt = new Database(yemekDuzenlePage.this);
                        list = vt.VeriListele();
                        for(Yemek st : list) {
                            if(st.getYemekAdi().toUpperCase().trim().contains(s.toString().toUpperCase().trim())){
                                listChange.add(st);
                            }
                        }
                        for (Yemek ws : listChange) {
                        }
                       yemekAdapter adapter = new yemekAdapter(yemekDuzenlePage.this, listChange);
                        veriListele.setAdapter(adapter);
                    }
                }
                idBul = -1;
            }
        });

        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idBul == -1){
                    new SweetAlertDialog(yemekDuzenlePage.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Lütfen Listeden Bir Kayıt Seçiniz!")
                            .show();
                    return;
                }
                Yemek item = listChange.get(idBul);
                incele(item.getId());
                listChange.clear();
                if(arama.getText().toString().trim().equals("")) {
                    Listele();
                } else {
                    {
                        Database vt = new Database(yemekDuzenlePage.this);
                        list = vt.VeriListele();
                        for(Yemek st : list) {
                            if(st.getYemekAdi().contains(arama.getText().toString().trim())){
                                listChange.add(st);
                            }
                        }
                        yemekAdapter adapter = new yemekAdapter(yemekDuzenlePage.this, listChange);
                        veriListele.setAdapter(adapter);
                    }
                }
            }
        });
        Listele();

        veriListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idBul = position;
            }
        });
    }

    public void Listele(){
        Database vt = new Database(yemekDuzenlePage.this);
        list = vt.VeriListele();
        listChange.clear();
        for (Yemek ws : list) {
            listChange.add(ws);
        }
        yemekAdapter adapter = new yemekAdapter(yemekDuzenlePage.this, listChange);
        veriListele.setAdapter(adapter);
    }
    public void incele(int a0){
        Intent myIntent = new Intent(this, guncelle.class);
        myIntent.putExtra("id", a0);
        startActivity(myIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(arama.getText().toString().trim().equals("")) {
            Listele();
        } else {
            {
                listChange.clear();
                Database vt = new Database(yemekDuzenlePage.this);
                list = vt.VeriListele();
                for(Yemek st : list) {
                    if(st.getYemekAdi().contains(arama.getText().toString().trim())){
                        listChange.add(st);
                    }
                }
                yemekAdapter adapter = new yemekAdapter(yemekDuzenlePage.this, listChange);
                veriListele.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        idBul = -1;
    }
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
    }
}
