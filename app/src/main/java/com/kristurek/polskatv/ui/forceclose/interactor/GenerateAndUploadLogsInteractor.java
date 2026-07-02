package com.kristurek.polskatv.ui.forceclose.interactor;

import android.content.Context;

import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.arch.SingleParamAbstractInteractor;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateAndUploadLogsInteractor extends SingleParamAbstractInteractor<Boolean, String> {

    private RemoteServerService remoteService;
    private LoggerService logService;
    private DiagnosticService diagService;
    private Context context;

    public GenerateAndUploadLogsInteractor(Context context, RemoteServerService remoteService,
                                           LoggerService logService,
                                           DiagnosticService diagService) {
        this.context = context;
        this.remoteService = remoteService;
        this.logService = logService;
        this.diagService = diagService;
    }

    @Override
    protected Boolean process(String param) {

        String diagnostickMsg = diagService.deviceInformation()
                .append("************ CAUSE OF ERROR ************\n")
                .append(param)
                .toString();

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getCacheDir();

        String infoFileName = dateTime + "_info_error.txt";
        File infoFile = logService.prepareInfoFile(cacheDir.getPath(), infoFileName, diagnostickMsg);
        remoteService.uploadFile(infoFile, "log");
        infoFile.delete();

        String logFileName = dateTime + "_log_error.txt";
        File logFile = logService.prepareLogFile(cacheDir.getPath(), logFileName);
        remoteService.uploadFile(logFile, "log");
        logFile.delete();

        String dumpFileName = dateTime + "_dump_error.txt";
        File dumpFile = logService.prepareDumpFile(cacheDir.getPath(), dumpFileName);
        remoteService.uploadFile(dumpFile, "log");
        dumpFile.delete();

        return true;
    }
}
