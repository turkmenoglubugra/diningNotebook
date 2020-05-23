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
import java.util.HashMap;
import java.util.List;

public class yemekListePage  extends AppCompatActivity {
    private ListView veriListele;
    private EditText arama;
    private Button incele;
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
        incele = (Button) findViewById(R.id.btnIncele);

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
                            if(st.getYemekAdi().contains(s.toString().trim())){
                                listChange.add(st);
                            }
                        }
                        listTable.clear();
                        for (Yemek ws : listChange) {
                            listTable.add(ws.getYemekAdi());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekListePage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
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

            }
        });

        incele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idBul == -1){
                    Toast.makeText(getApplicationContext(),"LÜTFEN LİSTEDEN BİR KAYIT SEÇİNİZ!",Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekListePage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
        veriListele.setAdapter(adapter);
    }

    public void incele(int a0){
        Intent myIntent = new Intent(this, yemekInceleActivity.class);
        myIntent.putExtra("id", a0);
        startActivity(myIntent);
    }

}
