package com.yemekDefteri;

public class Yemek {
    private int id;
    private String yemekAdi;
    private String tarif;

    public String getMalzeme() {
        return malzeme;
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

    public Yemek(int id, String yemekAdi, String malzeme, String tarif, byte[] resim) {
        this.id = id;
        this.yemekAdi = yemekAdi;
        this.tarif = tarif;
        this.malzeme = malzeme;
        this.resim = resim;
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
