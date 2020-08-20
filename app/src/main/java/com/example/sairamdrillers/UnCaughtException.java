package com.example.sairamdrillers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class UnCaughtException implements Thread.UncaughtExceptionHandler {
    public UnCaughtException(Context mainActivity, Context mainActivity1) {
        Log.e("uncaught","unagughterror");
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "SRDcrashLog");
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, "CrashReport.txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            StringBuilder report = new StringBuilder();
            Date curDate = new Date();
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            report.append(sb);
            fis.close();
            isr.close();
            bufferedReader.close();

            report.append("Error Report collected on : ")
                    .append(curDate.toString()).append('\n').append('\n');
            report.append("Stack:\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            report.append(result.toString());
            printWriter.close();
            report.append('\n');
            report.append("****  End of current Report ***");
            report.append("\n\n");
            report.append("**************************************************");
            report.append("\n\n");

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter writer_out = new OutputStreamWriter(fileOutputStream);
            writer_out.append(report);
            writer_out.close();
            fileOutputStream.close();
            new MainActivity().closeapp();

        }
        catch (Exception ef)
        {
            e.printStackTrace();
        }

    }

}
