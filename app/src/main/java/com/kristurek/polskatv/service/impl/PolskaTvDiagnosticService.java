package com.kristurek.polskatv.service.impl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Debug;

import com.kristurek.polskatv.BuildConfig;
import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.PreferencesService;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import static android.content.Context.WIFI_SERVICE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_LANGUAGE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_MEDIA_SERVERS;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_MEDIA_SERVER_ID;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_PARENTAL_PASSWORD;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_PASSWORD;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_REST_OF_DAY;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_SUBSCRIPTION;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_TIME_SHIFT;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.ACCOUNT_TIME_ZONE;
import static com.kristurek.polskatv.service.PreferencesService.KEYS.API_PROVIDER_ID;

public class PolskaTvDiagnosticService implements DiagnosticService {

    private final static String LINE_SEPARATOR = "\n";

    private Context context;
    private PreferencesService preferencesService;

    public PolskaTvDiagnosticService(Context context, PreferencesService preferencesService) {
        this.context = context;
        this.preferencesService = preferencesService;
    }

    public StringBuilder deviceInformation() {

        StringBuilder report = new StringBuilder();

        report.append("************ DEVICE INFORMATION ***********\n");

        report.append("PolskaTv version name: ");
        report.append(BuildConfig.VERSION_NAME);
        report.append(LINE_SEPARATOR);
        report.append("PolskaTv version code: ");
        report.append(BuildConfig.VERSION_CODE);
        report.append(LINE_SEPARATOR);
        report.append("Brand: ");
        report.append(Build.BRAND);
        report.append(LINE_SEPARATOR);
        report.append("Device: ");
        report.append(Build.DEVICE);
        report.append(LINE_SEPARATOR);
        report.append("Model: ");
        report.append(Build.MODEL);
        report.append(LINE_SEPARATOR);
        report.append("Id: ");
        report.append(Build.ID);
        report.append(LINE_SEPARATOR);
        report.append("Product: ");
        report.append(Build.PRODUCT);
        report.append(LINE_SEPARATOR);
        report.append("IP: ");
        report.append(wifiIpAddress(context.getApplicationContext()));
        report.append(LINE_SEPARATOR);
        report.append("\n************ FIRMWARE ************\n");
        report.append("SDK: ");
        report.append(Build.VERSION.SDK_INT);
        report.append(LINE_SEPARATOR);
        report.append("Release: ");
        report.append(Build.VERSION.RELEASE);
        report.append(LINE_SEPARATOR);
        report.append("Incremental: ");
        report.append(Build.VERSION.INCREMENTAL);
        report.append(LINE_SEPARATOR);
        report.append(LINE_SEPARATOR);

        report.append("************ CONFIGURATION INFORMATION ***********\n");
        report.append("Subscription: ");
        report.append(preferencesService.get(ACCOUNT_SUBSCRIPTION, "None"));
        report.append(LINE_SEPARATOR);
        report.append("Password: ");
        report.append(preferencesService.get(ACCOUNT_PASSWORD, "None"));
        report.append(LINE_SEPARATOR);
        report.append("Api provider ID: ");
        report.append(preferencesService.get(API_PROVIDER_ID, 0));
        report.append(LINE_SEPARATOR);
        report.append("Parental pass: ");
        report.append(preferencesService.get(ACCOUNT_PARENTAL_PASSWORD, "None"));
        report.append(LINE_SEPARATOR);
        report.append("Language: ");
        report.append(preferencesService.get(ACCOUNT_LANGUAGE, "None"));
        report.append(LINE_SEPARATOR);
        report.append("Stream servers: ");
        report.append(preferencesService.get(ACCOUNT_MEDIA_SERVERS, "None"));
        report.append(LINE_SEPARATOR);
        report.append("Stream server ID: ");
        report.append(preferencesService.get(ACCOUNT_MEDIA_SERVER_ID, 0));
        report.append(LINE_SEPARATOR);
        report.append("Rest of day: ");
        report.append(preferencesService.get(ACCOUNT_REST_OF_DAY, 0));
        report.append(LINE_SEPARATOR);
        report.append("Time shift : ");
        report.append(preferencesService.get(ACCOUNT_TIME_SHIFT, 0));
        report.append(LINE_SEPARATOR);
        report.append("Time Zone: ");
        report.append(preferencesService.get(ACCOUNT_TIME_ZONE, 0));
        report.append(LINE_SEPARATOR);

        report.append(LINE_SEPARATOR);

        report.append(memoryUsage(context));

        report.append(LINE_SEPARATOR);

        return report;
    }

    private static StringBuilder memoryUsage(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        StringBuilder report = new StringBuilder();

        report.append("************ MEMORY INFORMATION ***********\n");

        report.append(LINE_SEPARATOR);
        report.append("************ ActivityManager.MemoryInfo ***********\n");
        report.append("memoryInfo.totalMem " + memoryInfo.totalMem / 0x1024L + "KB, " + memoryInfo.totalMem / 0x1048576L + "MB");
        report.append(LINE_SEPARATOR);
        report.append("memoryInfo.usedMem " + (memoryInfo.totalMem - memoryInfo.availMem) / 0x1024L + "KB, " + (memoryInfo.totalMem - memoryInfo.availMem) / 0x1048576L + " MB");
        report.append(LINE_SEPARATOR);
        report.append("memoryInfo.availMem " + memoryInfo.availMem / 0x1024L + "KB, " + memoryInfo.availMem / 0x1048576L + "MB");
        report.append(LINE_SEPARATOR);
        report.append("memoryInfo.availMem " + (int) (memoryInfo.availMem / (double) memoryInfo.totalMem * 100.0) + "%");
        report.append(LINE_SEPARATOR);
        report.append("memoryInfo.lowMemory " + memoryInfo.lowMemory);
        report.append(LINE_SEPARATOR);
        report.append("memoryInfo.threshold " + memoryInfo.threshold / 0x1024L + "KB, " + memoryInfo.threshold / 0x1048576L + "MB");
        report.append(LINE_SEPARATOR);
        report.append(LINE_SEPARATOR);

        report.append("************ Runtime.getRuntime() ***********\n");
        report.append("VM Heap Size - totalMemory() " + Runtime.getRuntime().totalMemory() / 0x1024L + "KB, " + Runtime.getRuntime().totalMemory() / 0x1048576L + "MB");
        report.append(LINE_SEPARATOR);
        report.append("Get Allocated VM - usedMemory() " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 0x1024L + "KB, " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 0x1048576L + " MB");
        report.append(LINE_SEPARATOR);
        report.append("VM Heap Size - freeMemory() " + Runtime.getRuntime().freeMemory() / 0x1024L + "KB, " + Runtime.getRuntime().freeMemory() / 0x1048576L + "MB");
        report.append(LINE_SEPARATOR);
        report.append("VM Heap Size - freeMemory() " + (int) (Runtime.getRuntime().freeMemory() / (double) Runtime.getRuntime().totalMemory() * 100.0) + "%");
        report.append(LINE_SEPARATOR);
        report.append("VM Heap Size - maxMemory() " + Runtime.getRuntime().maxMemory() / 0x1024L + "KB, " + Runtime.getRuntime().maxMemory() / 0x1048576L + "MB");
        report.append(LINE_SEPARATOR);
        report.append("Native Allocated Memory " + Debug.getNativeHeapAllocatedSize() / 0x1024L + "KB, " + Debug.getNativeHeapAllocatedSize() / 0x1048576L + "MB");
        report.append(LINE_SEPARATOR);

        report.append(LINE_SEPARATOR);

        return report;
    }

    public static boolean isSupportTouchScreen(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.touchscreen");
    }

    private static String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException e) {
            ipAddressString = null;
        }

        return ipAddressString;
    }
}
