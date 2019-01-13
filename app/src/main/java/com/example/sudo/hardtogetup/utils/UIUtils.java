package com.example.sudo.hardtogetup.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.sudo.hardtogetup.R;

import co.revely.gradient.RevelyGradient;

public class UIUtils {

    public static void showSnackBar( RelativeLayout relativeLayout, String message){
        Snackbar.make(relativeLayout, message, Snackbar.LENGTH_SHORT ).show();
    }

    public static void setGradientBackground (Context context, int firstColor, int secondColor, View view) {
        RevelyGradient
                .linear()
                .colors(new int[] {ContextCompat.getColor(context, firstColor),  ContextCompat.getColor(context,secondColor)})
                .onBackgroundOf(view);
    }
}
