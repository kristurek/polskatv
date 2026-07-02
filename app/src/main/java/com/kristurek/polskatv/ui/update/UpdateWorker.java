package com.kristurek.polskatv.ui.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.kristurek.polskatv.BuildConfig;
import com.kristurek.polskatv.PolskaTvApplication;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.util.Tag;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class UpdateWorker extends Worker {

    @Inject
    public PreferencesService prefService;
    @Inject
    public RemoteServerService remoteService;

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        PolskaTvApplication.getComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(Tag.UI, "UpdateWorker.doWork()[begin]");

        try {
            List<String> files = remoteService.downloadListFileNames("/apk");
            if (!files.isEmpty()) {

                Collections.sort(files, (file1, file2) -> {
                    int version1 = Integer.parseInt(file1.replaceAll("\\D+", ""));
                    int version2 = Integer.parseInt(file2.replaceAll("\\D+", ""));
                    return version1 - version2;
                });

                Log.d(Tag.UI, "UpdateWorker.doWork()[" + files + "]");

                String file = files.get(files.size() - 1);

                int newVersionCode = Integer.parseInt(file.replaceAll("\\D+", ""));
                int currentVersionCode = BuildConfig.VERSION_CODE;

                Log.d(Tag.UI, "UpdateWorker.doWork()[" + currentVersionCode + "," + newVersionCode + "]");

                if (newVersionCode > currentVersionCode) {
                    Log.d(Tag.UI, "UpdateWorker.doWork()[Found new version]");

                    File cacheDir = getApplicationContext().getExternalCacheDir();
                    if (cacheDir == null)
                        cacheDir = getApplicationContext().getCacheDir();

                    File downloadedApk = remoteService.downloadFile("/apk", cacheDir.getPath(), file);

                    if (downloadedApk != null) {
                        Log.d(Tag.UI, "UpdateWorker.doWork()[Success downloaded file, start install process]");
                        installApk(downloadedApk);
                    }
                }
            }
            return Result.success();
        } catch (Exception ex) {
            Log.e(Tag.UI, ex.getMessage(), ex);
            return Result.failure();
        } finally {
            Log.d(Tag.UI, "UpdateWorker.doWork()[end]");
        }
    }

    private void installApk(File downloadedApk) {
        Context context = getApplicationContext();
        Intent installIntent;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            installIntent.setDataAndType(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", downloadedApk), "application/vnd.android.package-archive");
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.fromFile(downloadedApk), "application/vnd.android.package-archive");
        }
        
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(installIntent);
    }
}
