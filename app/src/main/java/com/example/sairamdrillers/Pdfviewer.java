package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class Pdfviewer extends Base {
PDFView pdfView;
String pdfname="";
boolean isfromstorage=false;
    boolean isSaved=false;
     File file=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        pdfView= (PDFView)findViewById(R.id.pdfView);
        pdfname=(String) getIntent().getStringExtra("pdfname");
        isfromstorage=(boolean) getIntent().getBooleanExtra("isfromstorage",false);
        file= new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Pdffolder/"+pdfname);
        pdfView.fromFile(file).defaultPage(1).load();


        findViewById(R.id.btnshare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(Pdfviewer.this, "Pdf Saved Successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                isSaved=true;
                Uri uri = FileProvider.getUriForFile(Pdfviewer.this, Pdfviewer.this.getPackageName() + ".provider", file);
                try {
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_STREAM, uri);
//                    share.setPackage("com.whatsapp");
                    startActivity(share);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        if (!isSaved)
        file.delete();
        super.onBackPressed();

    }
}
