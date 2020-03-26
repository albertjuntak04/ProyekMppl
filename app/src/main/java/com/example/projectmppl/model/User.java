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

    public User(String nama, String email, String noHP, String pekerjaan, String jenisKelamin, String password) {
        this.nama = nama;
        this.email = email;
        this.noHP = noHP;
        this.pekerjaan = pekerjaan;
        this.jenisKelamin = jenisKelamin;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}
