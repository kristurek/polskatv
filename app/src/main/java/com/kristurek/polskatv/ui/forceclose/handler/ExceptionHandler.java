package com.kristurek.polskatv.ui.forceclose.handler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.kristurek.polskatv.ui.forceclose.ForceCloseActivity;
import com.kristurek.polskatv.util.Tag;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {

    private final Activity context;

    public ExceptionHandler(Activity context) {
        this.context = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Log.e(Tag.UI, exception.getMessage(), exception);

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        Intent intent = new Intent(context, ForceCloseActivity.class);
        intent.putExtra("error", stackTrace.toString());
        context.startActivity(intent);

        context.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}