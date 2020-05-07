package com.example.yemekdefteri;

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

public class yemekSilPage  extends AppCompatActivity {
    private Button btnSil;
    private int idBul = -1;
    private ListView veriListele;
    private  List<String> list;
    private List<String> listTable = new ArrayList<String>();
    private List<Integer> ids = new ArrayList<Integer>();
    private EditText arama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_sil);

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
                List<String> listChange = new ArrayList<String>();
                if(s.toString().trim().equals("")) {
                    Listele();
                } else {
                    {
                        Database vt = new Database(yemekSilPage.this);
                        list = vt.VeriListele();
                        for(String st : list) {
                            String[] itemBol = st.split(" - ");
                            if(itemBol[1].contains(s.toString().trim())){
                                listChange.add(st);
                            }
                        }
                        listTable.clear();
                        ids.clear();
                        for (String ws : listChange) {
                            String[] itemBol = ws.split(" - ");
                            ids.add(Integer.valueOf(itemBol[0].toString()));
                            listTable.add(itemBol[1].toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekSilPage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
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
                // VeriTabanı classımızı tanımlıyoruz
                if(idBul == -1){
                    Toast.makeText(getApplicationContext(),"LÜTFEN LİSTEDEN BİR KAYIT SEÇİNİZ!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Database vt = new Database(yemekSilPage.this);
                vt.VeriSil(idBul);
                //Sildikten Sonra tekrardan listeliyoruz
                List<String> listChange = new ArrayList<String>();
                if(arama.getText().toString().trim().equals("")) {
                    Listele();
                } else {
                    {
                        Database vt2 = new Database(yemekSilPage.this);
                        list = vt2.VeriListele();
                        for(String st : list) {
                            String[] itemBol = st.split(" - ");
                            if(itemBol[1].contains(arama.getText().toString().trim())){
                                listChange.add(st);
                            }
                        }
                        listTable.clear();
                        ids.clear();
                        for (String ws : listChange) {
                            String[] itemBol = ws.split(" - ");
                            ids.add(Integer.valueOf(itemBol[0].toString()));
                            listTable.add(itemBol[1].toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekSilPage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
                        veriListele.setAdapter(adapter);
                    }
                }
                Toast.makeText(getApplicationContext(),"BAŞARIYLA SİLİNDİ!",Toast.LENGTH_SHORT).show();
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
        listTable.clear();
        ids.clear();
        for (String ws : list) {
            String[] itemBol = ws.split(" - ");
            ids.add(Integer.valueOf(itemBol[0].toString()));
            listTable.add(itemBol[1].toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(yemekSilPage.this, android.R.layout.simple_list_item_1,android.R.id.text1,listTable);
        veriListele.setAdapter(adapter);
    }

}
