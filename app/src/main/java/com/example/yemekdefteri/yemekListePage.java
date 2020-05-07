package com.example.yemekdefteri;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.HashMap;
import java.util.List;

public class yemekListePage  extends AppCompatActivity {
    private ListView veriListele;
    private EditText arama;
    private Button incele;
    private int idBul = -1;
    private List<String> list;
    private List<String> listTable = new ArrayList<String>();
    private List<String> listChange = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_listele);
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
                String ws = listChange.get(idBul);
                String[] itemBol = ws.split(" - ");
                incele(itemBol[1].toString(),itemBol[2].toString(),itemBol[3].toString());
            }
        });
        Listele();
    }
    public void Listele(){
        Database vt = new Database(yemekListePage.this);
        list = vt.VeriListele();
        listTable.clear();
        listChange.clear();
        for (String ws : list) {
            String[] itemBol = ws.split(" - ");
            listChange.add(ws);
            listTable.add(itemBol[1].toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekListePage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
        veriListele.setAdapter(adapter);
    }

    public void incele(String a1, String a2, String a3){
        Intent myIntent = new Intent(this, yemekInceleActivity.class);
        myIntent.putExtra("firstKeyName", a1);
        myIntent.putExtra("secondKeyName", a2);
        myIntent.putExtra("thirdKeyName", a3);
        startActivity(myIntent);
    }

}
