package com.kristurek.polskatv.service;

import java.io.File;
import java.util.List;

public interface RemoteServerService {

    List<String> downloadListFileNames(String locationPath);

    File downloadFile(String remotePath, String localPath, String fileName);

    void uploadFile(File localFile, String destinationPath);

}
