package com.yemekDefteri;

public class Yemek {
    private int id;
    private String yemekAdi;
    private String tarif;
    private  int kategori;

    public String getMalzeme() {
        return malzeme;
    }

    public int getKategori() {
        return kategori;
    }

    public void setKategori(int kategori) {
        this.kategori = kategori;
    }

    public void setMalzeme(String malzeme) {
        this.malzeme = malzeme;
    }

    private String malzeme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYemekAdi() {
        return yemekAdi;
    }

    public void setYemekAdi(String yemekAdi) {
        this.yemekAdi = yemekAdi;
    }

    public Yemek(int id, String yemekAdi, String malzeme, String tarif, byte[] resim, int kategori) {
        this.id = id;
        this.yemekAdi = yemekAdi;
        this.tarif = tarif;
        this.malzeme = malzeme;
        this.resim = resim;
        this.kategori = kategori;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public byte[] getResim() {
        return resim;
    }

    public void setResim(byte[] resim) {
        this.resim = resim;
    }

    private byte[] resim;
}
