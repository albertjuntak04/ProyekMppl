package com.example.projectmppl.model;

public class Kantong {
    private String idPenukar;
    private String idSampah;
    private int jumlah;
    private int jumlahPoint;

    public Kantong(){

    }

    public Kantong(String idPenukar, String idSampah, int jumlah, int jumlahPoint) {
        this.idPenukar = idPenukar;
        this.idSampah = idSampah;
        this.jumlah = jumlah;
        this.jumlahPoint = jumlahPoint;
    }

    public String getIdPenukar() {
        return idPenukar;
    }

    public void setIdPenukar(String idPenukar) {
        this.idPenukar = idPenukar;
    }

    public String getIdSampah() {
        return idSampah;
    }

    public void setIdSampah(String idSampah) {
        this.idSampah = idSampah;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getJumlahPoint() {
        return jumlahPoint;
    }

    public void setJumlahPoint(int jumlahPoint) {
        this.jumlahPoint = jumlahPoint;
    }
}
