package com.example.projectmppl.model;

import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private String gambar;
    private ArrayList<String> idSampah;
    private String idPenukar;
    private String metode;
    private String status;
    private String taggalRequest;
    private int totalPoin;
    private String lokasi;


    public Transaksi(){

    }

    public Transaksi(String gambar, ArrayList<String> idSampah, String idPenukar, String metode, String status, String taggalRequest, int totalPoin, String lokasi) {
        this.gambar = gambar;
        this.idSampah = idSampah;
        this.idPenukar = idPenukar;
        this.metode = metode;
        this.status = status;
        this.taggalRequest = taggalRequest;
        this.totalPoin = totalPoin;
        this.lokasi = lokasi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public ArrayList<String> getIdSampah() {
        return idSampah;
    }

    public void setIdSampah(ArrayList<String> idSampah) {
        this.idSampah = idSampah;
    }

    public String getIdPenukar() {
        return idPenukar;
    }

    public void setIdPenukar(String idPenukar) {
        this.idPenukar = idPenukar;
    }

    public String getMetode() {
        return metode;
    }

    public void setMetode(String metode) {
        this.metode = metode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaggalRequest() {
        return taggalRequest;
    }

    public void setTaggalRequest(String taggalRequest) {
        this.taggalRequest = taggalRequest;
    }

    public int getTotalPoin() {
        return totalPoin;
    }

    public void setTotalPoin(int totalPoin) {
        this.totalPoin = totalPoin;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}
