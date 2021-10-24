package com.example.sairamdrillers;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class Billing_Entry extends Base {
    RecyclerView rv_Quotation;
    LinearLayout ll_quotationswipe,addquote,ll_Bill;
    Vector<QuotationDo> vecQuotationDo=new Vector<>();
    BillingAdapter billingAdapter =new BillingAdapter();
    EditText ev_rangelower,ev_rangehigher,et_seveninchpipes,et_teninchpipes,et_seveninchpipesamount,et_teninchpipesamount,et_flushingamount,et_labourandtransportamount,et_collapse,et_TotalInvoice;
    TextView tv_totalexgst,tv_cgst,tv_sgst,tv_Extras;
    CustomerDo customerDo=new CustomerDo();
    View previousView=null;
    Double totalCgst=0.0;
    Double totalsgst=0.0;
    Double TotalTax=0.0;
    Double TotalAmountQuoteAmount=0.0;
    Double TotalExtras=0.0;
    private boolean isSigned;
    private String customersign="";
    Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_entry);
//        customerDo=(CustomerDo)getIntent().getSerializableExtra("customerDo");
        if (customerDo==null)
            customerDo= new CustomerDo();
        rv_Quotation=findViewById(R.id.rv_Quotation);
        ll_quotationswipe=findViewById(R.id.ll_quotationswipe);
        et_seveninchpipes=findViewById(R.id.et_seveninchpipes);
        et_teninchpipes=findViewById(R.id.et_teninchpipes);
        et_seveninchpipesamount=findViewById(R.id.et_seveninchpipesamount);
        et_teninchpipesamount=findViewById(R.id.et_teninchpipesamount);
        et_flushingamount=findViewById(R.id.et_flushingamount);
        et_labourandtransportamount=findViewById(R.id.et_labourandtransportamount);
        et_collapse=findViewById(R.id.et_collapse);
        tv_Extras=findViewById(R.id.tv_Extras);
        tv_totalexgst=findViewById(R.id.tv_totalexgst);
        tv_cgst=findViewById(R.id.tv_cgst);
        tv_sgst=findViewById(R.id.tv_sgst);
        et_TotalInvoice=findViewById(R.id.et_TotalInvoice);
        addquote=findViewById(R.id.addquote);


        et_seveninchpipesamount.addTextChangedListener(textWatcher);
        et_teninchpipesamount.addTextChangedListener(textWatcher);
        et_flushingamount.addTextChangedListener(textWatcher);
        et_labourandtransportamount.addTextChangedListener(textWatcher);
        et_collapse.addTextChangedListener(textWatcher);




        rv_Quotation.setHasFixedSize(true);
        rv_Quotation.setLayoutManager(new LinearLayoutManager(this));
        buildquotationdo();
        billingAdapter.setHasStableIds(true);
        rv_Quotation.setAdapter(billingAdapter);

        ll_quotationswipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createPdf("");
                showSignatureDialog();
            }
        });
        addquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogaddQuoteToDo("");
            }
        });


    }

    Dialog dialog;
    private SignatureView customerSignature, presellerSignature;
    private void showSignatureDialog()
    {


        Dialoginstance dialoginstance=new Dialoginstance(Billing_Entry.this,90,60,"ALERT","");
        final Dialog dialog=dialoginstance.getdialoginstance();
        dialog.setContentView(R.layout.signaturedialog);
        final TextView tv_clear=dialog.findViewById(R.id.tv_clear);
        final TextView tv_cancel=dialog.findViewById(R.id.tv_cancel);
        final TextView tv_ok=dialog.findViewById(R.id.tv_ok);
        final LinearLayout ll_signature=dialog.findViewById(R.id.ll_signature);

        customerSignature  = new SignatureView(this);
        customerSignature.setDrawingCacheEnabled(true);
        customerSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT));
        customerSignature.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        ll_signature.addView(customerSignature);

        tv_clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(customerSignature != null)
                    customerSignature.resetSign();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                Bitmap bitmap 					= customerSignature.getDrawingCache(true);
                try {
                    customersign=BitMapToString(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadElementException e) {
                    e.printStackTrace();
                }
                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();
                createPdf("");
            }
        });

        dialog.show();
    }


    private void showDialogaddQuoteToDo(String s) {
        try
        {
            Dialoginstance dialoginstance=new Dialoginstance(Billing_Entry.this,90,30,"ALERT","");
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
        billingAdapter.notifyDataSetChanged();
    }

    private void buildquotationdo() {
       QuotationDo quotationDo=new QuotationDo();
       quotationDo.LowerLimit=0;
       quotationDo.HigherLimit=0;
       quotationDo.feets=0+"";
       vecQuotationDo.add(quotationDo);

         quotationDo=new QuotationDo();
        quotationDo.LowerLimit=0;
        quotationDo.HigherLimit=0;
        quotationDo.feets=0+"";
        vecQuotationDo.add(quotationDo);


    }

    public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.MyViewHolder> {

        public BillingAdapter() {
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            EditText ev_feets, ev_rateperfeet, ev_rangeamount,ev_H_limit;
            TextView tv_L_limit;

            public MyViewHolder(final View v) {
                super(v);
                ev_feets = v.findViewById(R.id.ev_feets);
                ev_rateperfeet = v.findViewById(R.id.ev_rateperfeet);
                ev_rangeamount = v.findViewById(R.id.ev_rangeamount);
                ev_H_limit = v.findViewById(R.id.ev_H_limit);
                tv_L_limit = v.findViewById(R.id.tv_L_limit);
                ev_rangeamount.setClickable(false);
                ev_rangeamount.setEnabled(false);

                ev_rangeamount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try{
                            if(!s.toString().equalsIgnoreCase("")) {
                                calculateTotalPrice();
                            }
                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


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
                ev_H_limit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            if (vecQuotationDo.size()==getAdapterPosition()+2)
                            {
                                QuotationDo quotationDo=new QuotationDo();
                                quotationDo.LowerLimit=0;
                                quotationDo.HigherLimit=0;
                                quotationDo.feets="0";
                                quotationDo.range="0";
                                quotationDo.rangeamount="0";
                                quotationDo.rateperfeet="0";
                                vecQuotationDo.add(quotationDo);
//                                billingAdapter.notifyItemChanged(getAdapterPosition());
                                billingAdapter.notifyDataSetChanged();
//                                billingAdapter.notifyItemRangeInserted(getAdapterPosition(),vecQuotationDo.size());
                            }
                        }

                    }
                });

                ev_H_limit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        View nextView=(View) v.getTag();
                        if (nextView==null){

                        }else {
                            TextView tv_nextLowerLimit=nextView.findViewById(R.id.tv_L_limit);
                            if (!s.toString().equalsIgnoreCase(""))
                            {
                                int nextLowerLimit=Integer.valueOf(s.toString())+1;
                                vecQuotationDo.get(getAdapterPosition()).HigherLimit=Integer.valueOf(s.toString());
                                vecQuotationDo.get(0).LowerLimit=1;
                                if(vecQuotationDo.get(getAdapterPosition()+1)!=null)
                                    vecQuotationDo.get(getAdapterPosition()+1).LowerLimit=Integer.valueOf(s.toString())+1;
                                int feets=vecQuotationDo.get(getAdapterPosition()).HigherLimit-(vecQuotationDo.get(getAdapterPosition()).LowerLimit-1);
                                tv_nextLowerLimit.setText(nextLowerLimit+"");
                                ev_feets.setText(""+feets);
                            }
                            else
                                tv_nextLowerLimit.setText("0");
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_bill, parent, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            if (previousView!=null)
            previousView.setTag(itemView);
            previousView=itemView;
            return vh;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try{
                if(vecQuotationDo.get(position)!=null){
                    if (vecQuotationDo.size()==position+1)
                    {
                        holder.ev_H_limit.setClickable(false);
                        holder.ev_H_limit.setEnabled(false);
                    }
                    else {
                        holder.ev_H_limit.setClickable(true);
                        holder.ev_H_limit.setEnabled(true);
                    }
//                    holder.ev_feets.setText(vecQuotationDo.get(position).feets);
//                    holder.ev_H_limit.setText(vecQuotationDo.get(position).HigherLimit);
//                    holder.tv_L_limit.setText(vecQuotationDo.get(position).LowerLimit);
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

    public synchronized void calculateTotalPrice() {
        totalCgst=0.0;
        totalsgst=0.0;
        TotalExtras=0.0;
        TotalAmountQuoteAmount=0.0;

        for (QuotationDo quotationDo:vecQuotationDo)
            TotalAmountQuoteAmount=TotalAmountQuoteAmount+Double.valueOf(quotationDo.rangeamount);

        Double seveninchpipesamount         =StringToInt(et_seveninchpipesamount        .getText().toString());
        Double teninchpipesamount           =StringToInt(et_teninchpipesamount          .getText().toString());
        Double flushingamount               =StringToInt(et_flushingamount              .getText().toString());
        Double labourandtransportamount     =StringToInt(et_labourandtransportamount    .getText().toString());
        Double collapse                     =StringToInt(et_collapse                    .getText().toString());

        TotalExtras=seveninchpipesamount+ teninchpipesamount+ flushingamount+ labourandtransportamount+ collapse;
        totalCgst=(TotalAmountQuoteAmount+TotalExtras)*0.09;
        totalsgst=(TotalAmountQuoteAmount+TotalExtras)*0.09;
        TotalTax=totalCgst+totalsgst;


        tv_totalexgst.setText("TOTAL : "+new DecimalFormat("0.00").format(TotalAmountQuoteAmount));
        tv_Extras.setText("Extras : "+new DecimalFormat("0.00").format(TotalExtras));
        tv_cgst.setText("CGST(9%): "+new DecimalFormat("0.00").format(totalCgst));
        tv_sgst.setText("SGST(9%): "+new DecimalFormat("0.00").format(totalsgst));
        et_TotalInvoice.setText(""+new DecimalFormat("0.00").format(TotalAmountQuoteAmount+TotalExtras+TotalTax));

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
            image.setAlignment(Element.ALIGN_RIGHT);
            image.scaleAbsolute(200, 100);
            int[] ia={0,0};
            image.setTransparency(ia);
            document.add(image);
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.RED);
            Chunk c = new Chunk("Sai Ram Drillers", f);
            Paragraph p1 = new Paragraph(c);
            p1.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(p1);
            document.close();
            pdfWriter.close();

            saveImagefromPdf(file);

            Intent i=new Intent(this,Pdfviewer.class);
            i.putExtra("pdfname","ash"+currentDateandTime+ ".pdf");
            startActivity(i);


        }
        catch (Exception e){
            e.printStackTrace();
        }











    }

    private void saveImagefromPdf(File file) throws IOException {
        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, 0x10000000);
        PdfRenderer renderer = new PdfRenderer(fileDescriptor);
        final int pageCount = renderer.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            PdfRenderer.Page page = renderer.openPage(i);
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();

            bitmapIsBlankOrWhite(bitmap);
            String root = Environment.getExternalStorageDirectory().toString();
            File file2 = new File(root + "/AShishImage" + ".png");

            if (file2.exists()) file2.delete();
            try {
                FileOutputStream out = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static boolean bitmapIsBlankOrWhite(Bitmap bitmap) {
        if (bitmap == null)
            return true;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        for (int i =  0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int pixel =  bitmap.getPixel(i, j);
                if (pixel != Color.WHITE) {
                    return false;
                }
            }
        }
        return true;
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
                ".header {\n" +
                "  display: inline-block; \n" +
                "  width: 100%;\n" +
                "  border: 1px solid red;\n" +
                "}\n" +
                ".playerOne {\n" +
                "  float: right;\n" +
                "}\n" +
                ".playerTwo {\n" +
                "  float: left;\n" +
                ""+
                "}"+
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
//                "\t<h3>SIDDI VINAYAKA BOREWELLS</h3>\n" +
                "\t<p><font size=\"3\"  >Drilling of 6 1/2' Dia Hydraulic Rig</font></p>\n" +
                "<p><font size=\"3\"  >Office1:LB nagar Ring Road Hyderabad.</font></p>\n" +
                "<p><font size=\"3\"  >Office2:12-10-587/50/19,Warasiguda,Sec-bad.</font></p>\n" +
                "<p><font size=\"3\"  >Web:www.siddivinayakaborewells.com &nbsp;&nbsp; Mail:sathyambatte@gmail.com</font></p>\n" +
                "<p><font size=\"4\" style=\"letter-spacing: 2px; \" ><b>Cell:9866018726,9000906888.</b></font></p>\n" +
                "<p><font size=\"4\" style=\"letter-spacing: 2px; \" ><b>GSTIN No. XXXXXXXXXXXXXXXXXX</b></font></p>\n" +
                "\t<h3 style=\"letter-spacing: 1px;padding: 4px; margin: 4px; \"><u>INVOICE</u></h3>\n" +
                "\t\n" +
                "      </div>\n" +
                "\t  \n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Bill to, </p>\n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Invoice Number : </p>\n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Invoice Date: "+new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())+"</p>\n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">Name:"+customerDo.name+"</p>\n" +
                "<p style=\"text-align:left;margin-left: 15px; margin-bottom: 5px; margin-right: 15px;\">GSTIN No.:XXXXXXXXXXXXXXXXX</p>\n" +
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


//                "        <table style=\"float:right\">\n" +
//                "  <tr>\n" +
//                "\t  <div style=\"margin-left: 15px;margin-top: 15px;\">\n" +
//                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">7\" "+et_seveninchpipes.getText().toString()+" Per Ft<span style=\"float:right;\"> : "+et_seveninchpipesamount.getText().toString()+".00</span></p>\n" +
//                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">10\" "+et_teninchpipes.getText().toString()+" Per Ft<span style=\"float:right;\"> : "+et_teninchpipesamount.getText().toString()+".00</span></p>\n" +
//                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">Flushing 6 1/2\" Borewells Per Ft<span style=\"float:right;\"> : "+et_flushingamount.getText().toString()+".00</span></p>\n" +
//                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">Transport and Labour Charges<span style=\"float:right;\"> : "+et_labourandtransportamount.getText().toString()+".00</span></p>\n" +
//                "\t  <p style=\"text-align:left;font-weight: bold;  margin-right: 15px;\">10' Bit collapse Drill Charge <span style=\"float:right;\"> : "+et_labourandtransportamount.getText().toString()+".00</span></p>\n" +
//                "\t  \n" +
//      "\n" +
//                "\t  </div>\n" +
//                "  </tr>\n" +
//                "  <tr>\n" +
//                "      <div style=\"text-align: right; margin-bottom: 30px; margin-top: 10px;\">\n" +
//                "\t\n" +
//                "        <h1 style=\"color:red;\">SAI RAM DRILLERS</h1>\n" +
////                "\t<h3>SIDDI VINAYAKA BOREWELLS</h3>\n" +
//                "\t<p><font size=\"3\"  >Drilling of 6 1/2' Dia Hydraulic Rig</font></p>\n" +
//                "<p><font size=\"3\"  >Office1:LB nagar Ring Road Hyderabad.</font></p>\n" +
//                "<p><font size=\"3\"  >Office2:12-10-587/50/19,Warasiguda,Sec-bad.</font></p>\n" +
//                "<p><font size=\"3\"  >Web:www.siddivinayakaborewells.com &nbsp;&nbsp; Mail:sathyambatte@gmail.com</font></p>\n" +
//                "<p><font size=\"4\" style=\"letter-spacing: 2px; \" ><b>Cell:9866018726,9000906888.</b></font></p>\n" +
//                "<p><font size=\"4\" style=\"letter-spacing: 2px; \" ><b>GSTIN No. XXXXXXXXXXXXXXXXXX</b></font></p>\n" +
//                "\t<h3 style=\"letter-spacing: 1px;padding: 4px; margin: 4px; \"><u>INVOICE</u></h3>\n" +
//                "\t\n" +
//                "      </div>\n" +
//                "  </tr>\n" +
//                "</table>\n" +


                "<div class=\"header\">\n" +
                "  <div class=\"playerOne\">\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\"><br></br><span style=\"float:right;\"></span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\">Total <span style=\"float:right;\"> : "+new DecimalFormat("0.00").format(TotalAmountQuoteAmount)+"</span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\">Extras <span style=\"float:right;\"> : "+new DecimalFormat("0.00").format(TotalExtras)+"</span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\">TAX(cgst+sgst)18% <span style=\"float:right;\"> : "+new DecimalFormat("0.00").format(TotalTax)+"</span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\">Total Amount <span style=\"float:right;\"> : "+new DecimalFormat("0.00").format(TotalAmountQuoteAmount+TotalExtras+TotalTax)+"</span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\"><span style=\"float:right;\"></span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\"><br></br><span style=\"float:right;\"></span></p>\n" +
                "  </div>\n" +
                "  <div class=\"playerTwo\">\n" +
                "\t  <p style=\"text-align:left; margin-right: 15px;\">7\" "+(et_seveninchpipes.getText().toString().equalsIgnoreCase("")?" Pipes ":et_seveninchpipes.getText().toString())+" Per Ft<span style=\"float:right;\"> : "+(et_seveninchpipesamount.getText().toString().equalsIgnoreCase("")?"0":et_seveninchpipesamount.getText().toString())+".00</span></p>\n" +
                "\t  <p style=\"text-align:left; margin-right: 15px;\">10\" "+(et_teninchpipes.getText().toString().equalsIgnoreCase("")?" Pipes ":et_teninchpipes.getText().toString())+" Per Ft<span style=\"float:right;\"> : "+(et_teninchpipesamount.getText().toString().equalsIgnoreCase("")?"0":et_teninchpipesamount.getText().toString())+".00</span></p>\n" +
                "\t  <p style=\"text-align:left; margin-right: 15px;\">Flushing 6 1/2\" Borewells Per Ft<span style=\"float:right;\"> : "+(et_flushingamount.getText().toString().equalsIgnoreCase("")?"0":et_flushingamount.getText().toString())+".00</span></p>\n" +
                "\t  <p style=\"text-align:left; margin-right: 15px;\">Transport and Labour Charges<span style=\"float:right;\"> : "+(et_labourandtransportamount.getText().toString().equalsIgnoreCase("")?"0":et_labourandtransportamount.getText().toString())+".00</span></p>\n" +
                "\t  <p style=\"text-align:left; margin-right: 15px;\">10' Bit collapse Drill Charge <span style=\"float:right;\"> : "+(et_collapse.getText().toString().equalsIgnoreCase("")?"0":et_collapse.getText().toString())+".00</span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\"><span style=\"float:right;\"></span></p>\n" +
                "\t  <p style=\"text-align:right;font-weight: bold;  margin-right: 15px;\"><span style=\"float:right;\"></span></p>\n" +
                "  </div>\n" +
                "  </div>\n" +
                "  </div>\n" +


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
                        "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\">"+(i+1)+"</td>\n" +
                        "            <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).LowerLimit+" ft -"+vecQuotationDo.get(i).HigherLimit+" ft</td>\n" +
                        "            <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).feets+"</td>\n" +
                        "\t\t\t <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).rateperfeet+".00</td>\n" +
                        "            <td style=\"border-right: 1px solid #dddddd;\">"+vecQuotationDo.get(i).rangeamount+"</td>\n" +
                        "          </tr>\n";
            }

        }
        quotationhtmlinner=quotationhtmlinner+"          <tr>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;border-left: 1px solid #dddddd;\"></td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\"></td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\"></td>\n" +
                "\t\t\t <td style=\"border-right: 1px solid #dddddd;\"><span style=\"font-weight:bold\">Total </span> </td>\n" +
                "            <td style=\"border-right: 1px solid #dddddd;\"><span style=\"font-weight:bold\">"+TotalAmountQuoteAmount+"</span></td>\n" +
                "          </tr>\n";

        return quotationhtmlinner;
    }

    private String getbitmap() throws IOException, BadElementException {
        Drawable myDrawable = getResources().getDrawable(R.drawable.icon);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        return BitMapToString(myLogo);
    }
    public String BitMapToString(Bitmap bitmap) throws IOException, BadElementException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();

        image = Image.getInstance(b);
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }



    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                if(!s.toString().equalsIgnoreCase("")) {
                    calculateTotalPrice();
                }
            }catch (Exception e){

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
