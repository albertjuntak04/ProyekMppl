package com.example.projectmppl.data;

import com.example.projectmppl.model.Pakaian;

import java.util.ArrayList;

public class PakaianData {
    private static String[] pakaianNames= {
            "Kaos",
            "Kemeja",
            "jaket",
            "Selimut",
            "Sepatu",
            "Tas"
    };

    public static ArrayList<Pakaian> getListData() {
        ArrayList<Pakaian> list = new ArrayList<>();
        for (int position = 0; position < pakaianNames.length; position++) {
            Pakaian pakaian = new Pakaian();
            pakaian.setName(pakaianNames[position]);
            list.add(pakaian);
        }
        return list;
    }
}
