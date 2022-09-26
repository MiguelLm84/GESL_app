package com.example.geslapp.core.clases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class BarcodePreferences {

    private int num = 0;
    ArrayList<String> listCodes = new ArrayList<>();

    public void saveBarcodePref(Context context, String barcode) {

        SharedPreferences sharedpref = context.getSharedPreferences("BARCODE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();

        if(num >= 0) {
            editor.putString("barcode" + num, barcode);
            num++;
        }
        editor.apply();

        String code = sharedpref.getString("barcode" + num, "");
        listCodes.add(code);
    }

    public void setBarcodePref(Context context,String barcode) {

        SharedPreferences sp = context.getSharedPreferences("BARCODE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("barcode",barcode);

        editor.apply();
    }

    public void removeListBarcodePref(Context context) {

        SharedPreferences.Editor editor = (SharedPreferences.Editor) context.getSharedPreferences("BARCODE",Context.MODE_PRIVATE);
        editor.clear().apply();
    }

    public void removeBarcodePref(Context context,String barcode) {

        SharedPreferences.Editor editor = (SharedPreferences.Editor) context.getSharedPreferences("BARCODE",Context.MODE_PRIVATE);
        editor.remove(barcode).commit();
    }

    public String getBarcode(Context context) {

        SharedPreferences sp = context.getSharedPreferences("BARCODE",Context.MODE_PRIVATE);
        return sp.getString("barcode","");
    }

    public ArrayList<String> getListBarcode() {

        return listCodes;
    }
}
