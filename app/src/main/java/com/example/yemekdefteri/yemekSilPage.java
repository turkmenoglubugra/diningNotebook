package com.example.yemekdefteri;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class yemekSilPage  extends AppCompatActivity {
    private Button btnSil;
    private int idBul;
    private ListView veriListele;
    private  List<String> list;
    private List<String> listTable = new ArrayList<String>();
    private List<Integer> ids = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_sil);

        veriListele = (ListView) findViewById(R.id.yemekListe) ;
        btnSil = (Button) findViewById(R.id.btnSil);
        Listele();

        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // VeriTabanı classımızı tanımlıyoruz
                if(idBul == 0) {
                    Toast.makeText(getApplicationContext(),"LÜTFEN TABLODAN BİR KAYIT SEÇİNİZ!",Toast.LENGTH_SHORT).show();
                } else {
                    Database vt = new Database(yemekSilPage.this);
                    vt.VeriSil(idBul);
                    //Sildikten Sonra tekrardan listeliyoruz
                    Listele();
                    idBul = 0;
                    Toast.makeText(getApplicationContext(),"BAŞARIYLA SİLİNDİ!",Toast.LENGTH_SHORT).show();
                }
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
