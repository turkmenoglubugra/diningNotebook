package com.example.yemekdefteri;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class yemekDuzenlePage extends AppCompatActivity {
    private int idBul = -1;
    private ListView veriListele;
    private List<String> list;
    private List<String> listTable = new ArrayList<String>();
    private List<String> listChange = new ArrayList<String>();
    private EditText arama;
    private  Button guncelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_duzenle);
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
                        for(String st : list) {
                            String[] itemBol = st.split(" - ");
                            if(itemBol[1].contains(s.toString().trim())){
                                listChange.add(st);
                            }
                        }
                        listTable.clear();
                        for (String ws : listChange) {
                            String[] itemBol = ws.split(" - ");
                            listTable.add(itemBol[1].toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekDuzenlePage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
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
                    Toast.makeText(getApplicationContext(),"LÜTFEN LİSTEDEN BİR KAYIT SEÇİNİZ!",Toast.LENGTH_SHORT).show();
                    return;
                }
                String item = listChange.get(idBul).toString();
                String[] itemBol = item.split(" - ");
                incele(itemBol[0],itemBol[1].toString(),itemBol[2].toString(), itemBol[3]);
                listChange.clear();
                if(arama.getText().toString().trim().equals("")) {
                    Listele();
                } else {
                    {
                        Database vt = new Database(yemekDuzenlePage.this);
                        list = vt.VeriListele();
                        for(String st : list) {
                            String[] itemBol2 = st.split(" - ");
                            if(itemBol2[1].contains(arama.getText().toString().trim())){
                                listChange.add(st);
                            }
                        }
                        listTable.clear();
                        for (String ws : listChange) {
                            String[] itemBol3 = ws.split(" - ");
                            listTable.add(itemBol3[1].toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekDuzenlePage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
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
        listTable.clear();
        listChange.clear();
        for (String ws : list) {
            String[] itemBol = ws.split(" - ");
            listChange.add(ws);
            listTable.add(itemBol[1].toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekDuzenlePage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
        veriListele.setAdapter(adapter);
    }
    public void incele(String a0, String a1, String a2, String a3){
        Intent myIntent = new Intent(this, guncelle.class);
        myIntent.putExtra("id", a0);
        myIntent.putExtra("firstKeyName", a1);
        myIntent.putExtra("secondKeyName", a2);
        myIntent.putExtra("thirdKeyName", a3);
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
                for(String st : list) {
                    String[] itemBol2 = st.split(" - ");
                    if(itemBol2[1].contains(arama.getText().toString().trim())){
                        listChange.add(st);
                    }
                }
                listTable.clear();
                for (String ws : listChange) {
                    String[] itemBol3 = ws.split(" - ");
                    listTable.add(itemBol3[1].toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekDuzenlePage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
                veriListele.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
    }
}
