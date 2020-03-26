package com.example.projectmppl.data;

import com.example.projectmppl.model.Sampah;

import java.util.ArrayList;

public class KomputerData {
    private static String[] komputerNames= {
            "Hardisk",
            "Mouse",
            "Monitor",
            "Keyboard",
            "Speaker",
            "Printer",
            "CPU"
    };

    public static ArrayList<Sampah> getListData() {
        ArrayList<Sampah> list = new ArrayList<>();
        for (int position = 0; position < komputerNames.length; position++) {
            Sampah komputer = new Sampah();
            komputer.setName(komputerNames[position]);
            list.add(komputer);
        }
        return list;
    }
}
