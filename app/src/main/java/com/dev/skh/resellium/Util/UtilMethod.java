package com.dev.skh.resellium.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Seogki on 2018. 6. 18..
 */

public class UtilMethod {

    @Nullable
    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    public static String getMemoryUsage(int i) {
        String heapSize = String.format("%.3f", (float) (Runtime.getRuntime().totalMemory() / 1024.00 / 1024.00));
        String freeMemory = String.format("%.3f", (float) (Runtime.getRuntime().freeMemory() / 1024.00 / 1024.00));

        String allocatedMemory = String
                .format("%.3f", (float) ((Runtime.getRuntime()
                        .totalMemory() - Runtime.getRuntime()
                        .freeMemory()) / 1024.00 / 1024.00));
        String heapSizeLimit = String.format("%.3f", (float) (Runtime.getRuntime().maxMemory() / 1024.00 / 1024.00));

        String nObjects = "Objects Allocated: " + i;

        return "Current Heap Size: " + heapSize
                + "\n Free memory: "
                + freeMemory
                + "\n Allocated Memory: "
                + allocatedMemory
                + "\n Heap Size Limit:  "
                + heapSizeLimit
                + "\n" + nObjects;
    }

//    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());


    public static String currencyFormat(String amount) {
        if (amount != null) {
            if (amount.isEmpty()) {
                return "";
            }
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            return formatter.format(Double.parseDouble(amount));
        } else {
            return "";
        }
    }

    public static String formatTimeString(long regTime, String result) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg;
        if (diffTime < Const.Companion.getSEC()) {
            msg = "방금 전";
        } else if ((diffTime /= Const.Companion.getSEC()) < Const.Companion.getMIN()) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= Const.Companion.getMIN()) < Const.Companion.getHOUR()) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= Const.Companion.getHOUR()) < Const.Companion.getDAY()) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= Const.Companion.getDAY()) < Const.Companion.getMONTH()) {
            msg = (diffTime) + "달 전";
        } else {
            String time = result.replace("-", "").substring(0, 8);
            String year = time.substring(2, 4);
            String month = time.substring(4, 6);
//            String days = time.substring(6,8);
            Log.d("time", time);
            return year + "-" + month;
        }


        return msg;
    }
}
