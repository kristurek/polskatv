package com.kristurek.polskatv.ui.forceclose.interactor;

import android.content.Context;

import com.kristurek.polskatv.service.DiagnosticService;
import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.service.RemoteServerService;
import com.kristurek.polskatv.ui.arch.SingleParamAbstractInteractor;

import org.joda.time.LocalDateTime;

import java.io.File;

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

        String dateTime = LocalDateTime.now().toString("yyyyMMddhhmmss");

        String infoFileName = dateTime + "_info_error.txt";
        File infoFile = logService.prepareInfoFile(context.getExternalCacheDir().getPath(), infoFileName, diagnostickMsg);
        remoteService.uploadFile(infoFile, "log");
        infoFile.deleteOnExit();

        String logFileName = dateTime + "_log_error.txt";
        File logFile = logService.prepareLogFile(context.getExternalCacheDir().getPath(), logFileName);
        remoteService.uploadFile(logFile, "log");
        logFile.deleteOnExit();

        String dumpFileName = dateTime + "_dump_error.txt";
        File dumpFile = logService.prepareDumpFile(context.getExternalCacheDir().getPath(), dumpFileName);
        remoteService.uploadFile(dumpFile, "log");
        dumpFile.deleteOnExit();

        return true;
    }
}