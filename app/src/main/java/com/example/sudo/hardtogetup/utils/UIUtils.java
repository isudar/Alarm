package com.example.sudo.hardtogetup.utils;

import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;

public class UIUtils {

    //metoda za prikaz snackbara prilikom pisanja novog problema
    public static void showSnackBar( RelativeLayout relativeLayout, String message){
        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_SHORT ).show();
    }
}
