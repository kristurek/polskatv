package com.kristurek.polskatv.service.impl;

import android.util.Log;

import com.kristurek.polskatv.service.LoggerService;
import com.kristurek.polskatv.util.Tag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class PolskaTvLoggerService implements LoggerService {

    public File prepareInfoFile(String localPath, String fileName, String msg) {
        try {
            File tempFile = new File(localPath + File.separator + fileName);
            tempFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(tempFile);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(msg);
            osw.flush();
            osw.close();

            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Cannot prepare information file, check logs for details", e);
        }
    }

    public File prepareLogFile(String localPath, String fileName) {
        try {
            File tempFile = new File(localPath + File.separator + fileName);
            tempFile.createNewFile();

            StringBuilder cmd = new StringBuilder();
            cmd.append("logcat -d -v threadtime ");
            cmd.append(com.kristurek.polskatv.iptv.util.Tag.API + ":D ");
            cmd.append(Tag.UI + ":D ");
            cmd.append(Tag.DATA + ":D ");
            cmd.append(Tag.SERVICE + ":D ");
            cmd.append(Tag.EVENT + ":D ");
            cmd.append(Tag.MASSIVE + ":D ");
            cmd.append("*:S");

            Log.d(Tag.SERVICE, cmd.toString());

            Process process = Runtime.getRuntime().exec(cmd.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append(System.getProperty("line.separator"));
            }

            FileOutputStream fOut = new FileOutputStream(tempFile);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(log.toString());
            osw.flush();
            osw.close();

            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Cannot prepare log file, check logs for details", e);
        }
    }

    public File prepareDumpFile(String localPath, String fileName) {
        try {
            File tempFile = new File(localPath + File.separator + fileName);
            tempFile.createNewFile();

            Process process = Runtime.getRuntime().exec("logcat -d -v threadtime");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append(System.getProperty("line.separator"));
            }

            FileOutputStream fOut = new FileOutputStream(tempFile);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(log.toString());
            osw.flush();
            osw.close();

            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Cannot prepare dump file, check logs for details", e);
        }
    }
}
