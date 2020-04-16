package com.example.projectmppl.model;

import java.util.ArrayList;

public class KantongNonOrganik {

    private ArrayList<String> idSampah;
    private String jenisSampah;
    private int jumlah;
    private int jumlahPoint;

    public KantongNonOrganik() {

    }

    public KantongNonOrganik(ArrayList<String> idSampah, String jenisSampah, int jumlah, int jumlahPoint) {

        this.idSampah = idSampah;
        this.jenisSampah = jenisSampah;
        this.jumlah = jumlah;
        this.jumlahPoint = jumlahPoint;
    }


    public ArrayList<String> getIdSampah() {
        return idSampah;
    }

    public void setIdSampah(ArrayList<String> idSampah) {
        this.idSampah = idSampah;
    }

    public String getJenisSampah() {
        return jenisSampah;
    }

    public void setJenisSampah(String jenisSampah) {
        this.jenisSampah = jenisSampah;
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
