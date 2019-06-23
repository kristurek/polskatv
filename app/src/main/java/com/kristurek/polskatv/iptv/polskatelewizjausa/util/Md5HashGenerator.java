package com.kristurek.polskatv.iptv.polskatelewizjausa.util;

import android.util.Log;

import com.kristurek.polskatv.iptv.util.Tag;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5HashGenerator {

    public static final String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e(Tag.API, e.getMessage(), e);
            return null;
        }
    }

}
