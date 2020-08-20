package com.example.sairamdrillers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.Vector;

public class PdfList extends Base {
RecyclerView rv_pdflist;
TextView tv_noitems,tv_addpdf;
    private Vector<PdfDo> vecpdflist=new Vector<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);
        rv_pdflist=findViewById(R.id.rv_pdflist);
        tv_noitems=findViewById(R.id.tv_noitems);
        tv_addpdf=findViewById(R.id.tv_addpdf);
        rv_pdflist.setHasFixedSize(true);
        rv_pdflist.setLayoutManager(new LinearLayoutManager(this));
        loaddata();
tv_addpdf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),Quotation_Entry.class);
        startActivity(intent);
    }
});
    }
    @Override
    protected void onResume() {
        super.onResume();
        loaddata();
    }
    public void loaddata(){
        vecpdflist=new Vector<>();
        String path = Environment.getExternalStorageDirectory().toString()+"/Pdffolder";
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files!=null&&files.length>0){
            rv_pdflist.setVisibility(View.VISIBLE);
            tv_noitems.setVisibility(View.GONE);
            for (int i = 0; i < files.length; i++)
            {
                PdfDo pdfDo=new PdfDo();
                pdfDo.pdfname=files[i].getName();
                pdfDo.pdfdatetime=Long.toString(files[i].lastModified());
                vecpdflist.add(pdfDo);
            }
            rv_pdflist.setAdapter(new PdfListAdapter());
        }
        else
        {
            rv_pdflist.setVisibility(View.GONE);
            tv_noitems.setVisibility(View.VISIBLE);
        }

    }


    public class PdfListAdapter extends RecyclerView.Adapter<PdfListAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView pdfname,pdfdatetime;
            public MyViewHolder(View v) {
                super(v);
                pdfname=v.findViewById(R.id.pdfname);
                pdfdatetime=v.findViewById(R.id.pdfdatetime);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(),Pdfviewer.class);
                        i.putExtra("isfromstorage",true);
                        i.putExtra("pdfname",vecpdflist.get(getAdapterPosition()).pdfname);
                        startActivity(i);
                    }
                });




            }
        }
        public PdfListAdapter() {
        }
        @Override
        public PdfListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_pdflist, parent, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            return vh;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                holder.pdfname.setText(vecpdflist.get(position).pdfname);
                holder.pdfdatetime.setText(vecpdflist.get(position).pdfdatetime);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public int getItemCount() {
            return vecpdflist.size();
        }
    }
}
