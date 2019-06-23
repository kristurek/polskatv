package com.kristurek.polskatv.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class DrawableHelper {

    public static Drawable getIcon(Context context, String fileNamePath) {
        try {
            return Drawable.createFromStream(context.getAssets().open(fileNamePath), null);
        } catch (Exception ignore) {
            Log.e(Tag.UI, ignore.getMessage(), ignore);
        }

        return null;
    }
}
