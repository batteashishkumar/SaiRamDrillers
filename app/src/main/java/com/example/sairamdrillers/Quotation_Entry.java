package com.example.sairamdrillers;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class Quotation_Entry extends Base {
RecyclerView rv_Quotation;
LinearLayout ll_quotationswipe,addquote;
Vector<QuotationDo> vecQuotationDo=new Vector<>();
    QuotationAdapter quotationAdapter=new QuotationAdapter();
    EditText ev_rangelower,ev_rangehigher, et_twoinchpipes, et_twoinchpipesamount,et_flushingamount,et_labourandtransportamount,et_remarks,et_TotalAmount,et_billType;
    EditText et_caseingOne,et_oneinchpipes,et_caseingOneFeets,et_caseingOnePrice,et_oneinchpipesamount;
    EditText et_caseingTwo,et_caseingTwoFeets,et_caseingTwoPrice;
    EditText et_caseingThree,et_Threeinchpipes,et_caseingThreeFeets,et_caseingThreePrice,et_Threeinchpipesamount;
    CustomerDo customerDo=new CustomerDo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation__entry);
        customerDo=(CustomerDo)getIntent().getSerializableExtra("customerDo");
        if (customerDo==null)
            customerDo= new CustomerDo();
        rv_Quotation=findViewById(R.id.rv_Quotation);
        ll_quotationswipe=findViewById(R.id.ll_quotationswipe);

        et_caseingOne = findViewById(R.id.et_caseingOne);
        et_oneinchpipes =findViewById(R.id.et_oneinchpipes);
        et_caseingOneFeets = findViewById(R.id.et_caseingOneFeets);
        et_caseingOnePrice = findViewById(R.id.et_caseingOnePrice);
        et_oneinchpipesamount =findViewById(R.id.et_oneinchpipesamount);

        et_caseingTwo = findViewById(R.id.et_caseingTwo);
        et_twoinchpipes =findViewById(R.id.et_twoinchpipes);
        et_caseingTwoFeets = findViewById(R.id.et_caseingTwoFeets);
        et_caseingTwoPrice = findViewById(R.id.et_caseingTwoPrice);
        et_twoinchpipesamount =findViewById(R.id.et_twoinchpipesamount);

        et_caseingThree = findViewById(R.id.et_caseingThree);
        et_Threeinchpipes=findViewById(R.id.et_Threeinchpipes);
        et_caseingThreeFeets = findViewById(R.id.et_caseingThreeFeets);
        et_caseingThreePrice = findViewById(R.id.et_caseingThreePrice);
        et_Threeinchpipesamount=findViewById(R.id.et_Threeinchpipesamount);
        et_billType=findViewById(R.id.et_billType);

        et_remarks=findViewById(R.id.et_remarks);
        et_flushingamount=findViewById(R.id.et_flushingamount);
        et_labourandtransportamount=findViewById(R.id.et_labourandtransportamount);


        et_TotalAmount=findViewById(R.id.et_TotalAmount);

        addquote=findViewById(R.id.addquote);
        rv_Quotation.setHasFixedSize(true);
        rv_Quotation.setLayoutManager(new LinearLayoutManager(this));
        buildquotationdo();
        rv_Quotation.setAdapter(quotationAdapter);

        ll_quotationswipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf("");
            }
        });
        addquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogaddQuoteToDo("");
            }
        });


    }

    private void showDialogaddQuoteToDo(String s) {
        try
        {
            Dialoginstance dialoginstance=new Dialoginstance(Quotation_Entry.this,90,30,"ALERT","");
            final Dialog dialog=dialoginstance.getdialoginstance();
            dialog.setContentView(R.layout.addquotationdailog);
            LinearLayout btnok=dialog.findViewById(R.id.addquoterange);
            ev_rangelower=dialog.findViewById(R.id.ev_rangelower);
            ev_rangehigher=dialog.findViewById(R.id.ev_rangehigher);
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addQuoteToDo();
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addQuoteToDo() {
        QuotationDo quotationDo=new QuotationDo();
        quotationDo.idsn=vecQuotationDo.size()+1+".";
        quotationDo.range=ev_rangelower.getText().toString()+"-"+ev_rangehigher.getText().toString();
        vecQuotationDo.add(quotationDo);
        quotationAdapter.notifyDataSetChanged();
    }

    private void buildquotationdo() {
        for (int i=1;i<=10;i++)
        {
            QuotationDo quotationDo=new QuotationDo();
            quotationDo.idsn=i+".";
            int upperindex=100*i;
            int lowerindex=upperindex-100;
            quotationDo.range=""+lowerindex+"-"+upperindex;
            quotationDo.feets="100";
            vecQuotationDo.add(quotationDo);
        }
    }

    public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.MyViewHolder> {

        public QuotationAdapter() {
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_range, tv_sno;
            EditText ev_feets, ev_rateperfeet, ev_rangeamount;

            public MyViewHolder(View v) {
                super(v);
                tv_sno = v.findViewById(R.id.tv_sno);
                tv_range = v.findViewById(R.id.tv_range);
                ev_feets = v.findViewById(R.id.ev_feets);
                ev_rateperfeet = v.findViewById(R.id.ev_rateperfeet);
                ev_rangeamount = v.findViewById(R.id.ev_rangeamount);

                ev_rangeamount.setClickable(false);
                ev_rangeamount.setEnabled(false);

                ev_feets.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try{
                            if(s.toString().equalsIgnoreCase(""))
                                vecQuotationDo.get(getAdapterPosition()).feets="0";
                                else
                            vecQuotationDo.get(getAdapterPosition()).feets=s.toString();
                            vecQuotationDo.get(getAdapterPosition()).rangeamount= (Double.valueOf(vecQuotationDo.get(getAdapterPosition()).feets)*Double.valueOf(vecQuotationDo.get(getAdapterPosition()).rateperfeet))+"";
                            ev_rangeamount.setText(vecQuotationDo.get(getAdapterPosition()).rangeamount);
                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                ev_rateperfeet.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().equalsIgnoreCase(""))
                            vecQuotationDo.get(getAdapterPosition()).rateperfeet="0";
                        else
                        vecQuotationDo.get(getAdapterPosition()).rateperfeet=s.toString();
                        vecQuotationDo.get(getAdapterPosition()).rangeamount= (Double.valueOf(vecQuotationDo.get(getAdapterPosition()).feets)*Double.valueOf(vecQuotationDo.get(getAdapterPosition()).rateperfeet))+"";
                        ev_rangeamount.setText(vecQuotationDo.get(getAdapterPosition()).rangeamount);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
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
            try{
                if(vecQuotationDo.get(position)!=null){
                    holder.tv_sno.setText(vecQuotationDo.get(position).idsn);
                    holder.tv_range.setText(vecQuotationDo.get(position).range);
                    holder.ev_feets.setText(vecQuotationDo.get(position).feets);
                }
            }catch (Exception e){

            }






        }
        @Override
        public int getItemCount() {
            return vecQuotationDo.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }












    private void createPdf(String sometext){

        PdfWriter pdfWriter = null;

        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/Pdffolder");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            String exampleString=getHtmlString();
            InputStream stream = new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
            InputStreamReader fis =  new InputStreamReader(stream);
            String fpath = "/sdcard/Pdffolder/" + "ash"+currentDateandTime+ ".pdf";
            File file = new File(fpath);
            Document document = new Document();

            pdfWriter=PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
            document.open();
            document.addAuthor("betterThanZero");
            document.addCreationDate();
            document.addProducer();

            document.setPageSize(PageSize.A4);

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, fis);
            document.close();
            pdfWriter.close();
            Intent i=new Intent(this,Pdfviewer.class);
            i.putExtra("pdfname","ash"+currentDateandTime+ ".pdf");
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
                "\t<h3 style=\"color:grey;\">OM SIDDI VINAYAKA <b style=\"color:red;\">ROBO</b> BOREWELLS</h3>\n" +
                "\t<p><font size=\"3\"  >Drilling of 6 1/2' Dia Hydraulic Rig</font></p>\n" +
                "<p><font size=\"3\"  >Office1:LB nagar Ring Road Hyderabad.</font></p>\n" +
                "<p><font size=\"3\"  >Office2:12-10-587/50/19,Warasiguda,Sec-bad.</font></p>\n" +
                "<p><font size=\"3\"  >Web:www.siddivinayakaborewells.com &nbsp;&nbsp; Mail:sathyambatte@gmail.com</font></p>\n" +
                "<p><font size=\"4\" style=\"letter-spacing: 2px; \" ><b>Cell:9866018726,9000906888.</b></font></p>\n" +
                "\t<h3 style=\"letter-spacing: 1px;padding: 4px; margin: 4px; \"><u>"+et_billType.getText().toString().toUpperCase()+"</u></h3>\n" +
                "\t\n" +
                "      </div>\n" +
                "\t  \n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Date: "+new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())+"</p>\n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Place: "+customerDo.place+"</p>\n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Name:"+customerDo.name+"</p>\n" +
                "      <div>\n" +
                "        <table>\n" +
                "          <tr>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">SNo.</th>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;\">PARTICULARS</th>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;\">FEETS</th>\n" +
                "\t\t\t<th style=\"border-right: 1px solid #dddddd;\">RATES</th>\n" +
                "            <th style=\"border-right: 1px solid #dddddd;\">AMOUNT</th>\n" +
                "          </tr>\n" +getQuotation()+
                "        </table>\n" +
                "      </div>\n" +
                "\t  <div style=\"margin-left: 15px;margin-top: 15px;\">\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">"+et_caseingOne.getText().toString()+"' " + et_oneinchpipes.getText().toString()+" Per Ft price("+et_caseingOnePrice.getText().toString()+") X Total ft("+et_caseingOneFeets.getText().toString()+")<span style=\"float:right;\"> : "+ et_oneinchpipesamount.getText().toString()+".00</span></p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">"+et_caseingTwo.getText().toString()+"' " + et_twoinchpipes.getText().toString()+" Per Ft price("+et_caseingTwoPrice.getText().toString()+") X Total ft("+et_caseingTwoFeets.getText().toString()+")<span style=\"float:right;\"> : "+ et_twoinchpipesamount.getText().toString()+".00</span></p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">"+et_caseingThree.getText().toString()+"' " + et_Threeinchpipes.getText().toString()+" Per Ft price("+et_caseingThreePrice.getText().toString()+") X Total ft("+et_caseingThreeFeets.getText().toString()+")<span style=\"float:right;\"> : "+ et_Threeinchpipesamount.getText().toString()+".00</span></p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\"> "+et_remarks.getText().toString()+"</p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">Flushing 6 1/2\" Borewells Per Feet<span style=\"float:right;\">Amount : "+et_flushingamount.getText().toString()+".00</span></p>\n" +
                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">Transport and Labour Charges<span style=\"float:right;\">Amount : "+et_labourandtransportamount.getText().toString()+".00</span></p>\n" +
                "\t  \n" +
                "\t  <p style=\"text-align:left;font-weight: bold; margin:0px;\"><span style=\"float:right;color:red;\">Total Amount : "+et_TotalAmount.getText().toString()+".00</span></p>\n" +
                "\t   <p style=\"text-align:left;font-weight: bold; margin-top:10px;\"><font size=\"6\" ><span style=\"float:right;color:red;\">from<b> Sai Ram Drillers</b></span></font></p>\n" +
                "\n" +
                "\t  </div>\n" +
                "</div>\n" +
                "  </body>\n" +
                "</html>\n";
        return  exampleString;
    }

    private String getQuotation() {
        String quotationhtmlinner="";
        for (int i=0;i<vecQuotationDo.size();i++)
        {
            if (vecQuotationDo.get(i).rateperfeet.equalsIgnoreCase("")||vecQuotationDo.get(i).rateperfeet.equalsIgnoreCase("0")||vecQuotationDo.get(i).rateperfeet==null)
            {

            }
            else {
                quotationhtmlinner=quotationhtmlinner+"          <tr>\n" +
                        "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">"+vecQuotationDo.get(i).idsn+"</td>\n" +
                        "            <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).range+" ft</td>\n" +
                        "            <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).feets+"</td>\n" +
                        "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).rateperfeet+".00</td>\n" +
                        "            <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).rangeamount+"</td>\n" +
                        "          </tr>\n";
            }

        }

        return quotationhtmlinner;
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
