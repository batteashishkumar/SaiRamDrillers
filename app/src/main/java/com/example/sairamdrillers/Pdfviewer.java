package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class Pdfviewer extends AppCompatActivity {
PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        pdfView= (PDFView)findViewById(R.id.pdfView);

        final File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/ash.pdf");
        pdfView.fromFile(file).defaultPage(1).load();


        findViewById(R.id.btnshare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(Pdfviewer.this, Pdfviewer.this.getPackageName() + ".provider", file);
                try {
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.setPackage("com.whatsapp");
                    startActivity(share);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
