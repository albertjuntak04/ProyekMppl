package com.example.projectmppl.data;

import com.example.projectmppl.model.Sampah;

import java.util.ArrayList;

public class HandphoneData {
    private static String[] sampahNames= {
            "TeleponSeluler",
            "Charger",
            "Casing",
            "PowerBank",
            "Earphone"
    };

    public static ArrayList<Sampah> getListData() {
        ArrayList<Sampah> list = new ArrayList<>();
        for (int position = 0; position < sampahNames.length; position++) {
            Sampah handphone = new Sampah();
            handphone.setName(sampahNames[position]);
            list.add(handphone);
        }
        return list;
    }
}
