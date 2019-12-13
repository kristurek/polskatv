package com.kristurek.polskatv.ui.update;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.kristurek.polskatv.BuildConfig;
import com.kristurek.polskatv.PolskaTvApplication;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.util.CacheHelper;
import com.kristurek.polskatv.util.Tag;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class UpdateIntentService extends IntentService {

    @Inject
    public PreferencesService prefService;
    @Inject
    public RemoteServerService remoteService;

    public UpdateIntentService() {
        super("UpdateIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(Tag.UI, "UpdateIntentService.onCreate()");

        PolskaTvApplication.getComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[begin]");

        try {
            CacheHelper.deleteCache(getApplicationContext());

            List<String> files = remoteService.downloadListFileNames("/apk");
            if (!files.isEmpty()) {
                Collections.sort(files, (file1, file2) -> file1.replaceAll("\\D+", "").compareTo(file2.replaceAll("\\D+", "")));

                Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[" + files + "]");

                String file = files.get(files.size() - 1);

                int newVersionCode = Integer.valueOf(file.replaceAll("\\D+", ""));
                int currentVersionCode = BuildConfig.VERSION_CODE;

                Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[" + currentVersionCode + "," + newVersionCode + "]");

                if (newVersionCode > currentVersionCode) {
                    Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[Found new version]");
                    File downloadedApk = remoteService.downloadFile("/apk", getApplicationContext().getExternalCacheDir().getPath(), file);

                    if (downloadedApk != null) {
                        Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[Success downloaded file, start install proccess]");
                        Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[" + downloadedApk.getPath() + "]");

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[Android >= N]");

                            Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            installIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", downloadedApk), "application/vnd.android.package-archive");
                            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(installIntent);
                        } else {
                            Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[Android < N]");

                            Intent installIntent = new Intent(Intent.ACTION_VIEW);
                            installIntent.setDataAndType(Uri.fromFile(downloadedApk), "application/vnd.android.package-archive");
                            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(installIntent);
                        }
                    }
                }
            }
        } catch (RuntimeException ex) {
            Log.e(Tag.UI, ex.getMessage(), ex);
        }
        Log.d(Tag.UI, "UpdateIntentService.onHandleIntent()[end]");
    }
}