package com.example.projectmppl.data;

import com.example.projectmppl.model.Sampah;

import java.util.ArrayList;

public class PakaianData {
    private static String[] pakaianNames= {
            "Kaos",
            "Kemeja",
            "Jaket",
            "Selimut",
            "Sepatu",
            "Tas"
    };

    public static ArrayList<Sampah> getListData() {
        ArrayList<Sampah> list = new ArrayList<>();
        for (int position = 0; position < pakaianNames.length; position++) {
            Sampah pakaian = new Sampah();
            pakaian.setName(pakaianNames[position]);
            list.add(pakaian);
        }
        return list;
    }
}
