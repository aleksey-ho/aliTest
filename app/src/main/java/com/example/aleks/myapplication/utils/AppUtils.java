package com.example.aleks.myapplication.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.example.aleks.myapplication.R;

/**
 * Created by aleks on 25.06.2017.
 */

public class AppUtils {
    public static void alert(Context context, String title, String message)
    {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(context);
        if(!TextUtils.isEmpty(title))
            messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setNeutralButton(R.string.alert_ok, null);
        messageBox.show();
    }

}
