package com.example.xingruili.zmqtt.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by XingruiLi on 2018/4/8.
 */

public class ToastUtil {

    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
