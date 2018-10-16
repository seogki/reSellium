package com.dev.skh.resellium.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v7.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    //SDF to generate a unique name for the compressed file.
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());

    public static File getCompressed(Context context, String path, String AdditionalName) throws IOException {
        Bitmap bitmap;
        if (context == null)
            throw new NullPointerException("Context must not be null.");
        //getting device external cache directory, might not be available on some devices,
        // so our code fall back to internal storage cache directory, which is always available but in smaller quantity
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            //fall back
            cacheDir = context.getCacheDir();

        String rootDir = cacheDir.getAbsolutePath() + "/ImageCompressor";
        File root = new File(rootDir);

//Create ImageCompressor folder if it doesnt already exists.
        if (!root.exists())
            root.mkdirs();


//decode and resize the original bitmap from @param path.
        bitmap = decodeImageFromFiles(path, /* your desired width*/640, /*your desired height*/ 480);


        ExifInterface exif = new ExifInterface(path);
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);
        Matrix matrix = new Matrix();
        if (rotation != 0f) {
            matrix.preRotate(rotationInDegrees);
        }
        Bitmap bitmaps = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//create placeholder for the compressed image file
        File compressed = new File(root, AdditionalName + "_" + SDF.format(new Date()) + ".jpg" /*Your desired format*/);

//convert the decoded bitmap to stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

/*compress bitmap into byteArrayOutputStream
 Bitmap.compress(Format, Quality, OutputStream)

Where Quality ranges from 1–100.
 */
        bitmaps.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

/*
 Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
 java.io.FileOutputStream can help us do just That!

*/
        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();

        fileOutputStream.close();

//File written, return to the caller. Done!
        return compressed;
    }

    private static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }


        // decode with the sample size
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }

    private static int exifToDegrees(int exifOrientation) {

        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static File bitmapToFile(Context context, String filename, Bitmap myBitmap) {
        //create a file to write bitmap data
        try {
            File f = new File(context.getCacheDir(), filename);
            f.createNewFile();


            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            return f;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showAlertDialog(@NonNull Context c, @NonNull String msg) {
        new AlertDialog.Builder(c)
                .setMessage(msg)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(null, null)
                .show();

    }

    public static String currencyFormat(String amount) {
        if(amount != null) {
            if (amount.isEmpty()) {
                return "";
            }

            DecimalFormat formatter = new DecimalFormat("###,###,###");
            return formatter.format(Double.parseDouble(amount));
        } else {
            return "";
        }


    }
}
