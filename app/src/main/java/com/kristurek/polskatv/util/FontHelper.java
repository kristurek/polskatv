package com.kristurek.polskatv.util;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.kristurek.polskatv.R;

import java.util.Arrays;

public class FontHelper {

    public enum SIZES {
        SMALL,
        STANDARD,
        LARGE,
        XL,
        XXL;

        public static String[] names() {
            return Arrays.toString(SIZES.values()).replaceAll("^.|.$", "").split(", ");
        }
    }

    public enum Header {
        H_1,
        H_2,
        H_3;
    }

    public static final String DEFAULT_FONT_SIZE = SIZES.STANDARD.name();

    private static String fontSize;

    public static void setFontSize(String fontSize) {
        Log.d(Tag.UI, "FontHelper.setFontSize()[" + fontSize + "]");
        FontHelper.fontSize = fontSize;
    }

    public static void setFont(Context context, TextView view, Header header) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(getDimension(header)));
    }

    public static float getFontSize(Context context, Header header) {
        return context.getResources().getDimension(getDimension(header));
    }

    private static int getDimension(Header header) {
        switch (header) {
            case H_1:
                if (fontSize.equals(SIZES.SMALL.name())) {
                    return R.dimen.small_header_1;
                } else if (fontSize.equals(SIZES.STANDARD.name())) {
                    return R.dimen.normal_header_1;
                } else if (fontSize.equals(SIZES.LARGE.name())) {
                    return R.dimen.large_header_1;
                } else if (fontSize.equals(SIZES.XL.name())) {
                    return R.dimen.xl_header_1;
                } else if (fontSize.equals(SIZES.XXL.name())) {
                    return R.dimen.xxl_header_1;
                } else
                    return R.dimen.normal_header_1;
            case H_2:
                if (fontSize.equals(SIZES.SMALL.name())) {
                    return R.dimen.small_header_2;
                } else if (fontSize.equals(SIZES.STANDARD.name())) {
                    return R.dimen.normal_header_2;
                } else if (fontSize.equals(SIZES.LARGE.name())) {
                    return R.dimen.large_header_2;
                } else if (fontSize.equals(SIZES.XL.name())) {
                    return R.dimen.xl_header_2;
                } else if (fontSize.equals(SIZES.XXL.name())) {
                    return R.dimen.xxl_header_2;
                } else
                    return R.dimen.normal_header_2;
            case H_3:
                if (fontSize.equals(SIZES.SMALL.name())) {
                    return R.dimen.small_header_3;
                } else if (fontSize.equals(SIZES.STANDARD.name())) {
                    return R.dimen.normal_header_3;
                } else if (fontSize.equals(SIZES.LARGE.name())) {
                    return R.dimen.large_header_3;
                } else if (fontSize.equals(SIZES.XL.name())) {
                    return R.dimen.xl_header_3;
                } else if (fontSize.equals(SIZES.XXL.name())) {
                    return R.dimen.xxl_header_3;
                } else
                    return R.dimen.normal_header_3;
            default:
                return 0;
        }
    }
}
