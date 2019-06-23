package com.kristurek.polskatv.service;

import java.io.File;

public interface LoggerService {

    File prepareInfoFile(String localPath, String fileName, String msg);


    File prepareLogFile(String localPath, String fileName);


    File prepareDumpFile(String localPath, String fileName);
}
