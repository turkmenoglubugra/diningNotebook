package com.yemekDefteri;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class yemekListePage  extends AppCompatActivity {
    private ListView veriListele;
    private AutoCompleteTextView edtCmpSearch;
    private int idBul = -1;
    private List<Yemek> list;
    private List<String> listTable = new ArrayList<String>();
    private List<Yemek> listChange = new ArrayList<Yemek>();
    private List<Kategori> listCategory = new ArrayList<Kategori>();
    private List<String> listCategoryStr = new ArrayList<String>();
    private AlertDialog.Builder alertDialogBuilderSaveKategori,alertDialogBuilderDeleteKategori;
    private View popupInputDialogSaveKategori,popupInputDialogDeleteKategori;
    private AlertDialog alertDialogAddKategori,alertDialogDeleteKategori;
    private Button button_cancel_category,button_save_category,button_cancel_delete_category,button_delete_category;
    private AutoCompleteTextView edtCmpDeleteCategory;
    private EditText txtCategory;
    private int positionSaveCat=-1;
    private String selection="";
    private int positionDeleteCat=-1;
    private String selectionDelete="";

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
        edtCmpSearch = (AutoCompleteTextView) findViewById(R.id.edtCmpSearch);
        edtCmpSearch.addTextChangedListener(new TextWatcher() {

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
                        int category = -1;
                        for (Kategori cat : listCategory){
                            if(s.toString().toUpperCase().trim().equals(cat.getName())){
                                category = cat.getId();
                            }
                        }
                        if(category == -1){
                            for(Yemek st : list) {
                                if(st.getYemekAdi().toUpperCase().trim().contains(s.toString().toUpperCase().trim())){
                                    listChange.add(st);
                                }
                            }
                        } else {
                            for(Yemek st : list) {
                                if(st.getYemekAdi().toUpperCase().trim().contains(s.toString().toUpperCase().trim()) || st.getKategori() == category){
                                    listChange.add(st);
                                }
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
        final String b = this.getResources().getString(R.string.kategoriVar);

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


        LayoutInflater layoutInflaterYeniYemek = LayoutInflater.from(yemekListePage.this);
        LayoutInflater layoutInflaterKategoriSil = LayoutInflater.from(yemekListePage.this);

        popupInputDialogSaveKategori = layoutInflaterYeniYemek.inflate(R.layout.popup_save_category, null);
        alertDialogBuilderSaveKategori = new AlertDialog.Builder(yemekListePage.this);
        popupInputDialogDeleteKategori = layoutInflaterKategoriSil.inflate(R.layout.popup_delete_category, null);
        alertDialogBuilderDeleteKategori = new AlertDialog.Builder(yemekListePage.this);


        alertDialogBuilderSaveKategori.setTitle(getResources().getString(R.string.kategoriEkle));
        alertDialogBuilderSaveKategori.setCancelable(false);
        alertDialogBuilderSaveKategori.setView(popupInputDialogSaveKategori);
        alertDialogAddKategori = alertDialogBuilderSaveKategori.create();

        alertDialogBuilderDeleteKategori.setTitle(getResources().getString(R.string.kategoriEkle));
        alertDialogBuilderDeleteKategori.setCancelable(false);
        alertDialogBuilderDeleteKategori.setView(popupInputDialogDeleteKategori);
        alertDialogDeleteKategori = alertDialogBuilderDeleteKategori.create();

        button_cancel_category = popupInputDialogSaveKategori.findViewById(R.id.button_cancel_category);
        button_save_category = popupInputDialogSaveKategori.findViewById(R.id.button_save_category);
        txtCategory = popupInputDialogSaveKategori.findViewById(R.id.txtCategory);
        button_cancel_delete_category = popupInputDialogDeleteKategori.findViewById(R.id.button_cancel_category);
        button_delete_category = popupInputDialogDeleteKategori.findViewById(R.id.button_delete_category);
        edtCmpDeleteCategory = popupInputDialogDeleteKategori.findViewById(R.id.edtCmpDeleteCategory);

        edtCmpDeleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectionDelete = (String)parent.getItemAtPosition(position);
                positionDeleteCat = position;
            }
        });

        button_cancel_delete_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDeleteKategori.cancel();
                edtCmpDeleteCategory.setText("");
                edtCmpDeleteCategory.setSelection(0);
                positionDeleteCat=-1;
                selectionDelete="";
            }
        });

        button_delete_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positionDeleteCat == -1) {
                    new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(getResources().getString(R.string.kategoriSecilmeli))
                            .show();
                    return;
                }
                boolean control = true;
                int silinecek = 0;
                for(Kategori cat : listCategory){
                    if(cat.getName().trim().equals(selectionDelete.trim())){
                        silinecek = cat.getId();
                    }
                }
                for(Yemek yemek : listChange){
                    if(yemek.getKategori() == silinecek) {
                        control = false;
                    }
                }
                if (!control) {
                    new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(getResources().getString(R.string.kategoriIliski))
                            .show();
                    alertDialogDeleteKategori.cancel();
                    edtCmpDeleteCategory.setSelection(0);
                    edtCmpDeleteCategory.setText("");
                    positionDeleteCat=-1;
                    selectionDelete="";
                    return;
                }
                Database vt = new Database(yemekListePage.this);
                vt.VeriSilKategori(silinecek);
                Listele();
                alertDialogDeleteKategori.cancel();
                edtCmpDeleteCategory.setSelection(0);
                edtCmpDeleteCategory.setText("");
                positionDeleteCat=-1;
                selectionDelete="";
            }
        });

        txtCategory.setHint(this.getResources().getString(R.string.burayaYaz));

        button_cancel_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAddKategori.cancel();
                positionSaveCat=-1;
                selection="";
                txtCategory.setText("");
            }
        });


        button_save_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtCategory.getText().toString().trim().equals("")){
                    new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(getResources().getString(R.string.kategoriYazilmalı))
                            .show();
                    return;
                }
                boolean save = true;
                for(Kategori ws : listCategory){
                    if(ws.getName().trim().equals(txtCategory.getText().toString().trim().toUpperCase())){
                        save = false;
                    }
                }
                if(save){
                    Database db = new Database(yemekListePage.this);
                    db.VeriEkleKategori(txtCategory.getText().toString().trim().toUpperCase(),"");
                    alertDialogAddKategori.cancel();
                    positionSaveCat=-1;
                    selection="";
                    txtCategory.setText("");
                    Listele();
                } else {
                    new SweetAlertDialog(yemekListePage.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(b)
                            .show();
                    alertDialogAddKategori.cancel();
                    positionSaveCat=-1;
                    selection="";
                    txtCategory.setText("");
                    Listele();
                    closeKeyboard();
                }
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

        listCategory = vt.VeriListeleKategori();
        if(listCategory.size() == 0){
            vt.VeriEkleKategori("SOUP","English");
            vt.VeriEkleKategori("MAIN DISH","English");
            vt.VeriEkleKategori("DESSERT","English");
            vt.VeriEkleKategori("SALAD","English");
            vt.VeriEkleKategori("ENTREE STARTER","English");
            vt.VeriEkleKategori("ÇORBA","Turkish");
            vt.VeriEkleKategori("ANA YEMEK","Turkish");
            vt.VeriEkleKategori("TATLI","Turkish");
            vt.VeriEkleKategori("SALATA","Turkish");
            vt.VeriEkleKategori("ARA SICAK","Turkish");
            listCategory = vt.VeriListeleKategori();
            listCategoryStr = new ArrayList<String>();
            for(Kategori cur : listCategory){
                listCategoryStr.add(cur.getName());
            }
        } else {
            listCategoryStr = new ArrayList<String>();
            for(Kategori cur : listCategory){
                listCategoryStr.add(cur.getName());
            }
        }
        String[] stockArr = new String[listCategory.size()];
        stockArr = listCategoryStr.toArray(stockArr);

        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stockArr);
        edtCmpSearch.setAdapter(adapterCurrency);

        ArrayAdapter<String> adapterCurrency2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stockArr);
        edtCmpDeleteCategory.setAdapter(adapterCurrency2);

    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(this.getCurrentFocus() != null){
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
            case R.id.kategoriSil:
                closeKeyboard();
                alertDialogDeleteKategori.show();
                return true;
            case R.id.kategoriEkle:
                closeKeyboard();
                alertDialogAddKategori.show();
                return true;
            case R.id.searchAction:
                closeKeyboard();
                if(!edtCmpSearch.getText().toString().trim().equals("")){
                    Uri uri = Uri.parse("https://www.google.com/search?q="+edtCmpSearch.getText().toString());
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
                closeKeyboard();
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
                closeKeyboard();
                edtCmpSearch.setSelection(0);
                edtCmpSearch.setText("");
                return true;
            case R.id.yemekEkleAction:
                closeKeyboard();
                Intent intent = new Intent(this, yeniYemekActivity.class );
                    startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
