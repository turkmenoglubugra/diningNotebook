package com.yemekDefteri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YEMEKTARIFI";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLO_TARIFLER = "yemekTarifiTablo";
    private static final String TABLO_KATEGORI = "kategori";
    private static final String ROW_ID = "id";
    private static final String ROW_KATEGORI_ID = "id";
    private static final String ROW_KATEGORI_ID_TARIF = "kategori_id";
    private static final String ROW_KATEGORI_NAME = "name";
    private static final String ROW_LAN = "lan";
    private static final String ROW_YEMEK_ADI = "yemek_adi";
    private static final String ROW_MALZEME = "malzeme";
    private static final String ROW_TARIF = "tarif";
    private static final String ROW_RESIM = "resim";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLO_TARIFLER + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_YEMEK_ADI + " TEXT NOT NULL, "
                + ROW_MALZEME + " TEXT NOT NULL, "
                + ROW_TARIF + " TEXT NOT NULL, "
                + ROW_KATEGORI_ID_TARIF + " INTEGER NOT NULL DEFAULT -1, "
                + ROW_RESIM + " BLOB )");
        db.execSQL("CREATE TABLE " + TABLO_KATEGORI + "("
                + ROW_KATEGORI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_KATEGORI_NAME + " TEXT NOT NULL, "
                + ROW_LAN + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_TARIFLER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_KATEGORI);
        onCreate(db);
    }

    public void VeriEkle(String yemek_adi, String malzemeler, String tarif, byte[] resim, int kategori){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_YEMEK_ADI, yemek_adi);
            cv.put(ROW_MALZEME, malzemeler);
            cv.put(ROW_TARIF, tarif);
            cv.put(ROW_RESIM, resim);
            cv.put(ROW_KATEGORI_ID_TARIF, kategori);
            db.insert(TABLO_TARIFLER, null,cv);
        }catch (Exception e){
        }
        db.close();
    }

    public void VeriEkleKategori(String name,String language){
        SQLiteDatabase db = this.getWritableDatabase();
        if(language.equals("")){
            String lan = "ENGLISH";
            if(Locale.getDefault().getDisplayLanguage().trim().toUpperCase().equals("TURKISH")){
                lan = "TURKISH";
            }
            try {
                ContentValues cv = new ContentValues();
                cv.put(ROW_KATEGORI_NAME, name);
                cv.put(ROW_LAN, lan);
                db.insert(TABLO_KATEGORI, null,cv);
            }catch (Exception e){

            }
            db.close();
        } else {
            try {
                ContentValues cv = new ContentValues();
                cv.put(ROW_KATEGORI_NAME, name);
                cv.put(ROW_LAN, language.toUpperCase());
                db.insert(TABLO_KATEGORI, null,cv);
            }catch (Exception e){
            }
            db.close();
        }

    }


    public List<Yemek> VeriListele(){
        List<Yemek> veriler = new ArrayList<Yemek>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] stunlar = {ROW_ID,ROW_YEMEK_ADI,ROW_MALZEME, ROW_TARIF,ROW_RESIM,ROW_KATEGORI_ID_TARIF};
            Cursor cursor = db.query(TABLO_TARIFLER, stunlar,null,null,null,null,null);
            while (cursor.moveToNext()){
                Yemek obj = new Yemek(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getBlob(4),cursor.getInt(5));
                veriler.add(obj);
            }
        }catch (Exception e){
        }
        db.close();
        return veriler;
    }

    public List<Kategori> VeriListeleKategori(){
        String lan = Locale.getDefault().getDisplayLanguage();;

        List<Kategori> veriler = new ArrayList<Kategori>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] stunlar = {ROW_KATEGORI_ID,ROW_KATEGORI_NAME,ROW_LAN};
            Cursor cursor = db.query(TABLO_KATEGORI, stunlar,null,null,null,null,null);
            while (cursor.moveToNext()){
                Kategori obj = new Kategori(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                if(lan.equals("TURKISH")){
                    if(obj.getLan().equals("TURKISH")){
                        veriler.add(obj);
                    }
                } else {
                    if(obj.getLan().equals("ENGLISH")){
                         veriler.add(obj);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return veriler;
    }


    public void VeriSil(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // id ye göre verimizi siliyoruz
            String where = ROW_ID + " = " + id ;
            db.delete(TABLO_TARIFLER,where,null);
        }catch (Exception e){
        }
        db.close();
    }

    public void VeriSilKategori(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String where = ROW_KATEGORI_ID + " = " + id ;
            db.delete(TABLO_KATEGORI,where,null);
        }catch (Exception e){
        }
        db.close();
    }

    public void VeriDuzenle(int id, String ad, String malzeme, String tarif, byte[] resim, int kategori){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_YEMEK_ADI, ad);
            cv.put(ROW_MALZEME, malzeme);
            cv.put(ROW_TARIF, tarif);
            cv.put(ROW_RESIM, resim);
            cv.put(ROW_KATEGORI_ID_TARIF, kategori);
            String where = ROW_ID +" = '"+ id + "'";
            db.update(TABLO_TARIFLER,cv,where,null);
        }catch (Exception e){
        }
        db.close();
    }

}