package com.kristurek.polskatv.util;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class CacheHelper {

    public static void deleteCache(Context context) {
        deleteCache(context.getExternalCacheDir());
        deleteCache(context.getCacheDir());
    }

    private static void deleteCache(File dir) {
        try {
            deleteDir(dir);
        } catch (Exception e) {
            Log.e(Tag.UI, e.getMessage(), e);
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
