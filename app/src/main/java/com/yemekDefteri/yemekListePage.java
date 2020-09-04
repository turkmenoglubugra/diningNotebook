package com.yemekDefteri;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        TextView textView = new TextView(this);
        textView.setText(this.getResources().getString(R.string.app_name));

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
                e.printStackTrace();
        }
        veriListele = (ListView) findViewById(R.id.yemekListe);
        arama = (EditText) findViewById(R.id.aramaText);
        arama.setHint(this.getResources().getString(R.string.yemekAdiAra));
        arama.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

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
        final String a = this.getResources().getString(R.string.listedenSec);
        veriListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idBul = position;

                    if(idBul == -1){
                        new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(a)
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
        Intent myIntent = new Intent(this, guncelle.class);
        myIntent.putExtra("id", a0);
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.yemekekle,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.searchAction:
                if(!arama.getText().toString().trim().equals("")){
                    Uri uri = Uri.parse("https://www.google.com/search?q="+arama.getText().toString());
                    Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(gSearchIntent);
                } else {
                    new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(this.getResources().getString(R.string.aramaKutucu))
                            .show();
                }
                return true;
            case R.id.yemekTavsiyeAction:
                try {
                    final int min = 0;
                    final int max = listTable.size()-1;
                    final int random = new Random().nextInt((max - min) + 1) + min;
                    new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setCustomImage(R.mipmap.s_round)
                            .setTitleText(this.getResources().getString(R.string.afiyetOlsun))
                            .setContentText(listTable.get(random))
                            .show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.temizleAction:
                arama.setText("");
                return true;
            case R.id.yemekEkleAction:
                Intent intent = new Intent(this, yeniYemekActivity.class );
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
