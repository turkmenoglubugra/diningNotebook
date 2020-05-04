package com.example.yemekdefteri;

import android.content.Intent;
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

public class yemekDuzenlePage extends AppCompatActivity {
    private int idBul;
    private ListView veriListele;
    private List<String> list;
    private List<String> listTable = new ArrayList<String>();
    private List<Integer> ids = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_duzenle);

        veriListele = (ListView) findViewById(R.id.yemekListe) ;
        Listele();

        veriListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tıklanan verimizi alıyoruz
                String item = list.get(position).toString();
                // - Göre bölüyoruz
                String[] itemBol = item.split(" - ");
                incele(ids.get(position).toString(),itemBol[1].toString(),itemBol[2].toString(), itemBol[3]);
            }
        });
    }

    public void Listele(){
        Database vt = new Database(yemekDuzenlePage.this);
        list = vt.VeriListele();
        listTable.clear();
        ids.clear();
        for (String ws : list) {
            String[] itemBol = ws.split(" - ");
            ids.add(Integer.valueOf(itemBol[0].toString()));
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
        Listele();
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
