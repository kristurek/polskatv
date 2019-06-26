package com.kristurek.polskatv.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.kristurek.polskatv.BuildConfig;
import com.kristurek.polskatv.service.RemoteServerService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PolskaTvRemoteServerService implements RemoteServerService {

    private DbxClientV2 client;

    public PolskaTvRemoteServerService() {
        client = login();
    }

    private DbxClientV2 login() {
        DbxRequestConfig config = new DbxRequestConfig("polskatv.firetv");
        return new DbxClientV2(config, BuildConfig.DROPBOX_TOKEN);
    }

    @Override
    public File downloadFile(String remotePath, String localPath, String fileName) {
        try {
            ListFolderResult result = client.files().listFolder(remotePath);
            List<Metadata> files = result.getEntries();

            Metadata fileMetadata = Iterables.tryFind(files, metadata -> fileName.equals(metadata.getName())).orNull();

            if (fileMetadata != null) {
                File downloadFile = new File(localPath + File.separator + fileMetadata.getName());

                OutputStream outputStream = new FileOutputStream(downloadFile);
                client.files().download(fileMetadata.getPathLower(), null).download(outputStream);

                return downloadFile;
            }
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Cannot download update, check logs for details", e);
        }
        return null;
    }

    @Override
    public List<String> downloadListFileNames(String locationPath) {
        try {
            ListFolderResult result = client.files().listFolder(locationPath);
            List<Metadata> files = result.getEntries();

            return new ArrayList<>(Lists.transform(files, s -> s.getName()));
        } catch (DbxException e) {
            throw new RuntimeException("Cannot download list file names, check logs for details", e);
        }
    }

    @Override
    public void uploadFile(File localFile, String destinationPath) {
        String destinationFilePath = File.separator + destinationPath + File.separator + localFile.getName();
        try (InputStream in = new FileInputStream(localFile)) {
            client.files().uploadBuilder(destinationFilePath)
                    .withMode(WriteMode.OVERWRITE)
                    .withClientModified(new Date(localFile.lastModified()))
                    .uploadAndFinish(in);
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Cannot upload file, check logs for details", e);
        }
    }

}
