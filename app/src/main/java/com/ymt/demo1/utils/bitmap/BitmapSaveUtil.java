package com.ymt.demo1.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by DonMoses on 2015/10/20
 */
public class BitmapSaveUtil {
    private static BitmapCache bitmapCache;

    static {
        bitmapCache = BitmapCache.getInstance();
    }

    public static Bitmap getBitmap(String url) {
        Bitmap ramMap = bitmapCache.getBitmap(url);
        if (ramMap == null) {   //从内存取
            new BitmapGetTask().execute(url);
            return null;
        } else {
            return ramMap;
        }
    }

    public static void saveBitmap(String url, Bitmap bitmap) {
        new BitmapSaveTask().execute(url, bitmap);
    }

    private static String dir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();

    public static class BitmapGetTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String pathString = dir + "/" + params[0];
            Bitmap bitmap = null;
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
                Log.e("TAG", ".>>get pathString>>>>>>" + pathString);
            }

            return bitmap;
        }

    }

    public static class BitmapSaveTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            try {
                File file = new File(dir + "/" + params[0].toString());
                FileOutputStream out = new FileOutputStream(file);
                Bitmap bitmap = (Bitmap) params[1];
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                Log.e("TAG", ".>>save>>>>>>" + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
