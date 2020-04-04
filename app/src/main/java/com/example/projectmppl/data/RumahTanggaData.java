package com.example.projectmppl.data;

import com.example.projectmppl.model.Sampah;

import java.util.ArrayList;

public class RumahTanggaData {
    private static String[] pakaianNames= {
            "TV",
            "Kulkas",
            "Mesin Cuci",
            "VacuumCleaner",
            "Kamera",
            "Jam",
            "Blender",
            "Dispencer",
            "Setrika"
    };

    public static ArrayList<Sampah> getListData() {
        ArrayList<Sampah> list = new ArrayList<>();
        for (int position = 0; position < pakaianNames.length; position++) {
            Sampah rumahTangga = new Sampah();
            rumahTangga.setName(pakaianNames[position]);
            list.add(rumahTangga);
        }
        return list;
    }
}
