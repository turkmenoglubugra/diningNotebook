package com.example.yemekdefteri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YEMEK";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLO_TARIFLER = "tarifler";
    private static final String ROW_ID = "id";
    private static final String ROW_YEMEK_ADI = "yemek_adi";
    private static final String ROW_MALZEME = "malzeme";
    private static final String ROW_TARIF = "tarif";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLO_TARIFLER + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_YEMEK_ADI + " TEXT NOT NULL, "
                + ROW_MALZEME + " TEXT NOT NULL, "
                + ROW_TARIF + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_TARIFLER);
        onCreate(db);
    }

    public void VeriEkle(String yemek_adi, String malzemeler, String tarif){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_YEMEK_ADI, yemek_adi);
            cv.put(ROW_MALZEME, malzemeler);
            cv.put(ROW_TARIF, tarif);
            db.insert(TABLO_TARIFLER, null,cv);
        }catch (Exception e){
        }
        db.close();
    }


    public List<String> VeriListele(){
        List<String> veriler = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] stunlar = {ROW_ID,ROW_YEMEK_ADI,ROW_MALZEME, ROW_TARIF};
            Cursor cursor = db.query(TABLO_TARIFLER, stunlar,null,null,null,null,null);
            while (cursor.moveToNext()){
                veriler.add(cursor.getInt(0)
                        + " - "
                        + cursor.getString(1)
                        + " - "
                        + cursor.getString(2)
                        + " - "
                        + cursor.getString(3));
            }
        }catch (Exception e){
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

    public void VeriDuzenle(int id, String ad, String malzeme, String tarif){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_YEMEK_ADI, ad);
            cv.put(ROW_MALZEME, malzeme);
            cv.put(ROW_TARIF, tarif);
            String where = ROW_ID +" = '"+ id + "'";
            db.update(TABLO_TARIFLER,cv,where,null);
        }catch (Exception e){
        }
        db.close();
    }
}