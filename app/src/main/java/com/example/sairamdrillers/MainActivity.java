package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
Vector<CustomerDo> vecCustomers=new Vector<>();
TextView tv_totalamt,tv_pendingamt,tv_writequote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        tv_totalamt=findViewById(R.id.tv_totalamt);
        tv_pendingamt=findViewById(R.id.tv_pendingamt);
        tv_writequote=findViewById(R.id.tv_writequote);




        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView tv_create=findViewById(R.id.tv_create);
        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateCustomer.class);
                startActivity(i);
            }
        });

        tv_writequote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    createPdf("");
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String totalamt=new CustomerDA(this).getallCustomertotalamount();
        String paid=new PaymentDA(this).getsumofallpaymentsofcustomers();
        if (totalamt==null)
            totalamt="0";
        if (paid==null)
            paid="0";
        int pending=Integer.parseInt(totalamt)-Integer.parseInt(paid);
        tv_totalamt.setText(totalamt);
        tv_pendingamt.setText(String.valueOf(pending));
        vecCustomers=new CustomerDA(this).getallCustomers();
        recyclerView.setAdapter(new MyAdapter(vecCustomers));
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private Vector<CustomerDo> vecCustomers;

        public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_custname,tv_total,tv_datetime,tv_status,tv_pending,tv_paid,tv_pending_color;
            public MyViewHolder(View v) {
                super(v);
                tv_pending_color=v.findViewById(R.id.tv_pending_color);
                tv_custname=v.findViewById(R.id.tv_custname);
                tv_total=v.findViewById(R.id.tv_total);
                tv_datetime=v.findViewById(R.id.tv_datetime);
                tv_status=v.findViewById(R.id.tv_status);
                tv_paid=v.findViewById(R.id.tv_paid);
                tv_pending=v.findViewById(R.id.tv_pending);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(),CustomerDashboard.class);
                        i.putExtra("customerDo",vecCustomers.get(getAdapterPosition()));
                        startActivity(i);
                    }
                });




            }
        }
        public MyAdapter(Vector<CustomerDo> vecCustomers) {

            this.vecCustomers = vecCustomers;
        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell, parent, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            return vh;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_custname.setText(vecCustomers.get(position).name);
            holder.tv_datetime.setText(vecCustomers.get(position).dateandtime);
            String totalamount=vecCustomers.get(position).amount;
            String paid=new PaymentDA(getApplicationContext()).getsumofallpaymentsofcustomer(vecCustomers.get(position).id);
            if (totalamount==null)
                totalamount="0";
            if (paid==null)
                paid="0";
            int pending=Integer.parseInt(totalamount)-Integer.parseInt(paid);
            String pendingstr=String.valueOf(pending);
            holder.tv_total.setText(totalamount);
            holder.tv_paid.setText(paid);
            holder.tv_pending.setText(pendingstr);
            if (pending<=0)
            {
                holder.tv_status.setText("Payment Completed");
                holder.tv_status.setTextColor(getResources().getColor(R.color.green));
                holder.tv_pending.setTextColor(getResources().getColor(R.color.green));
                holder.tv_pending_color.setTextColor(getResources().getColor(R.color.green));

            }


            else
            {
                holder.tv_status.setText("Payment Pending");
                holder.tv_status.setTextColor(getResources().getColor(R.color.red));
                holder.tv_pending.setTextColor(getResources().getColor(R.color.red));
                holder.tv_pending_color.setTextColor(getResources().getColor(R.color.red));
            }



        }
        @Override
        public int getItemCount() {
            return vecCustomers.size();
        }
    }






    private void createPdf(String sometext){

        PdfWriter pdfWriter = null;

        try {

            String exampleString=getHtmlString();
            InputStream stream = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
            InputStreamReader fis =  new InputStreamReader(stream);
            String fpath = "/sdcard/" + "ash" + ".pdf";
            File file = new File(fpath);
            Document document = new Document();

            pdfWriter=PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
            document.open();
            document.addAuthor("betterThanZero");
            document.addCreationDate();
            document.addProducer();
//            document.addCreator("MySampleCode.com");
//            document.addTitle("Demo for iText XMLWorker");
            document.setPageSize(PageSize.A4);
//            document.add(new Paragraph("Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!Sigueme en Twitter!"));

//            document.add(new Paragraph("@DavidHackro"));

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, fis);
            document.close();
            pdfWriter.close();
            Intent i=new Intent(this,Pdfviewer.class);
            startActivity(i);


        }
        catch (Exception e){
            e.printStackTrace();
        }











    }

    private String getHtmlString() {
        String exampleString="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "html,body{\n" +
                "    width: 100%;\n" +
                "    height: 100%;\n" +
                "    margin: 0; /* Space from this element (entire page) and others*/\n" +
                "    padding: 0; /*space from content and border*/\n" +
                "    border: solid black;\n" +
                "    border-width: thin;\n" +
                "    overflow:hidden;\n" +
                "    display:block;\n" +
                "    box-sizing: border-box;\n" +
                "}\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h2>HTML Table</h2>\n" +
                "\n" +
                "<table>\n" +
                "    <tr>\n" +
                "        <th>Company</th>\n" +
                "        <th>Contact</th>\n" +
                "        <th>Country LABEL</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>Alfreds Futterkiste</td>\n" +
                "        <td>Maria Anders</td>\n" +
                "        <td>Germany</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>Centro comercial Moctezuma</td>\n" +
                "        <td>Francisco Chang</td>\n" +
                "        <td>Mexico</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>Ernst Handel</td>\n" +
                "        <td>Roland Mendel</td>\n" +
                "        <td>Austria</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>Island Trading</td>\n" +
                "        <td>Helen Bennett</td>\n" +
                "        <td>UK</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>Laughing Bacchus Winecellars</td>\n" +
                "        <td>Yoshi Tannamuri</td>\n" +
                "        <td>Canada</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>Magazzini Alimentari Riuniti</td>\n" +
                "        <td>Giovanni Rovelli</td>\n" +
                "        <td>Italy</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        return  exampleString;
    }


}
