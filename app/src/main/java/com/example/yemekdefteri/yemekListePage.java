package com.example.yemekdefteri;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class yemekListePage  extends AppCompatActivity {
    private ListView veriListele;
    private int idBul = 0;
    private List<String> list;
    private List<String> listTable = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_listele);
        veriListele = (ListView) findViewById(R.id.yemekListe);
        veriListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ws = list.get(position);
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
        for (String ws : list) {
            String[] itemBol = ws.split(" - ");
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
