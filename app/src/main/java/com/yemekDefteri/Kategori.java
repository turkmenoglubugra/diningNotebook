package com.yemekDefteri;

public class Kategori {
    private int id;
    private String name;

    public Kategori(int id, String name, String lan) {
        this.id = id;
        this.name = name;
        this.lan = lan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kategori() {
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    private String lan;

}
