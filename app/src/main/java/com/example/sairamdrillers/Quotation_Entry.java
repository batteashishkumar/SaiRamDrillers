package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Quotation_Entry extends AppCompatActivity {
RecyclerView rv_Quotation;
LinearLayout ll_quotationswipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation__entry);
        rv_Quotation=findViewById(R.id.rv_Quotation);
        ll_quotationswipe=findViewById(R.id.ll_quotationswipe);
        rv_Quotation.setHasFixedSize(true);
        rv_Quotation.setLayoutManager(new LinearLayoutManager(this));
        rv_Quotation.setAdapter(new QuotationAdapter());

        ll_quotationswipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf("");
            }
        });


    }
    public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_amt_cell,tv_type_cell,tv_datetime;
            public MyViewHolder(View v) {
                super(v);
//                tv_amt_cell=v.findViewById(R.id.tv_amt_cell);
//                tv_type_cell=v.findViewById(R.id.tv_type_cell);
//                tv_datetime=v.findViewById(R.id.tv_datetime);
            }
        }
        @Override
        public QuotationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_quotation, parent, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            return vh;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.tv_amt_cell.setText(vecPaymentsofCustomers.get(position).paymentAmount);
//            holder.tv_type_cell.setText(vecPaymentsofCustomers.get(position).paymentType);
//            holder.tv_datetime.setText(vecPaymentsofCustomers.get(position).payment_datetime);


        }
        @Override
        public int getItemCount() {
            return 30;
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
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>Document</title>\n" +
                "\n" +
                "    <style>\n" +
                "\tp {\n" +
                "  display: block;\n" +
                "  margin-top: 0;\n" +
                "  letter-spacing: 1px;\n" +
                "  margin-bottom: 0;\n" +
                "  margin-left: 0;\n" +
                "  margin-right: 0;\n" +
                "}\n" +
                "h1 {\n" +
                "  letter-spacing: 3px;\n" +
                "   padding: 0px;\n" +
                "        margin: 1px;\n" +
                "}\n" +
                "    table {\n" +
                "        font-family: arial, sans-serif;\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\n" +
                "      }\n" +
                "\n" +
                "      td,\n" +
                "      th {\n" +
                "        text-align: left;\n" +
                "        padding: 8px;\n" +
                "        border-top: 1px solid #dddddd;\n" +
                "        border-bottom: 1px solid #dddddd;\n" +
                "      }\n" +
                "\n" +
                "      tr:nth-child(even) {\n" +
                "        /* background-color: #dddddd; */\n" +
                "      }\n" +
                "\t  h3,\n" +
                "      h4,\n" +
                "      h2 {\n" +
                "        padding: 0px;\n" +
                "        margin: 0px;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body style=\"margin-top: 50px;\">\n" +
                "    <div style=\"border: 1px solid black;\">\n" +
                "      <div style=\"text-align: center; margin-bottom: 30px; margin-top: 10px;\">\n" +
                "\t\n" +
                "        <h1 style=\"color:red;\">SAI RAM DRILLERS</h1>\n" +
                "\t<h3>SIDDI VINAYAKA BOREWELLS</h3>\n" +
                "\t<p><font size=\"3\"  >Drilling of 6 1/2' Dia Hydraulic Rig</font></p>\n" +
                "<p><font size=\"3\"  >Office1:LB nagar Ring Road Hyderabad.</font></p>\n" +
                "<p><font size=\"3\"  >Office2:12-10-587/50/19,Warasiguda,Sec-bad.</font></p>\n" +
                "<p><font size=\"3\"  >Web:www.siddivinayakaborewells.com &nbsp;&nbsp; Mail:sathyambatte@gmail.com</font></p>\n" +
                "<p><font size=\"4\" style=\"letter-spacing: 2px; \" ><b>Cell:9866018726,9000906888.</b></font></p>\n" +
                "\t<h3 style=\"letter-spacing: 1px;padding: 4px; margin: 4px; \"><u>QUOTATION</u></h3>\n" +
                "\t\n" +
                "      </div>\n" +
                "\t  \n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Name:Batte Ashish Kumar<span style=\"float:right;\">Date: 1st March 2020</span></p>\n" +
                "      <div>\n" +
                "        <table>\n" +
                "          <tr>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">SNo.</th>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;\">PARTICULARS</th>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;\">FEETS</th>\n" +
                "\t\t\t<th style=\"border-right: 1px solid #dddddd;\">RATES</th>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;\">AMOUNT</th>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "\t\t  <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">1</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">001 - 100 ft</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">100</td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">30.00</td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\">3000.00</td>\n" +
                "          </tr>\n" +
                "        </table>\n" +
                "      </div>\n" +
                "\t  <div style=\"margin-left: 15px;margin-top: 15px;\">\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">7\" Sudhakar pipes Per Feet<span style=\"float:right;\">Amount : 300.00</span></p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">10\" Sudhakar pipes Per Feet<span style=\"float:right;\">Amount : 500.00</span></p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">Flushing 6 1/2\" Borewells Per Feet<span style=\"float:right;\">Amount : 0.00</span></p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">Transport and Labour Charges<span style=\"float:right;\">Amount : 0.00</span></p>\n" +
                "\t  \n" +
                "\t  <p style=\"text-align:left;font-weight: bold; margin-top: 10px; margin-right: 15px;\">&nbsp;&nbsp;<span style=\"float:right;\">Total Amount : 800.00</span></p>\n" +
                "\t   <p style=\"text-align:left;font-weight: bold; margin-top: 20px; margin-right: 15px;\"><font size=\"5\" >&nbsp;&nbsp;<span style=\"float:right;color:red;\">from<b> Sai Ram Drillers</b></span></font></p>\n" +
                "\n" +
                "\t  </div>\n" +
                "</div>\n" +
                "  </body>\n" +
                "</html>\n";
        return  exampleString;
    }

    private String getbitmap() {
        Drawable myDrawable = getResources().getDrawable(R.drawable.icon);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        return BitMapToString(myLogo);
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


}
