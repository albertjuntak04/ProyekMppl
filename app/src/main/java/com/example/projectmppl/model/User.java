package com.example.projectmppl.model;

/**
 * Created by root on 03/03/17.
 */

public class User {

    private String nama;
    private String email;
    private String noHP;
    private String pekerjaan;
    private String jenisKelamin;
    private String password;
    private String image;
    private int poin;

    public User(String nama, String email, String noHP, String pekerjaan, String jenisKelamin, String password, String image, int poin) {
        this.nama = nama;
        this.email = email;
        this.noHP = noHP;
        this.pekerjaan = pekerjaan;
        this.jenisKelamin = jenisKelamin;
        this.password = password;
        this.image = image;
        this.poin = poin;
    }

    public User(){


    }

    public int getPoin() {
        return poin;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}
