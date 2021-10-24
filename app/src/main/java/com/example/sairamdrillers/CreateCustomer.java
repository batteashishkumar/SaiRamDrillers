package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sairamdrillers.room.Appdatabase;
import com.example.sairamdrillers.room.Customer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.Vector;

public class CreateCustomer extends Base {
    CustomerDo customerDo;
    EditText et_CustName,et_place,et_number,et_amount;
    boolean status=false;
    LinearLayout ll_scroll;
    Vector<ContactDo> veccontactDo=new Vector<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);
        activity=2;
        et_CustName=findViewById(R.id.et_CustName);
        et_place=findViewById(R.id.et_place);
        et_number=findViewById(R.id.et_number);
        et_amount=findViewById(R.id.et_amount);


        //equal size horizontal Scroll View
        /*ll_scroll=findViewById(R.id.ll_scroll);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        for (int i=0;i<4;i++){
            LinearLayout ll0= (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.cellcategory,null);
            TextView tv_cat=ll0.findViewById(R.id.tv_cat);
            tv_cat.setText("Category "+i);
            tv_cat.setTag("Object "+i);
            ll0.setLayoutParams(new LinearLayout.LayoutParams(
                    width/2,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            ll_scroll.addView(ll0);
            tv_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), (String) v.getTag(),Toast.LENGTH_SHORT).show();
                }
            });
        }*/



        Button btn_save= findViewById(R.id.btn_save);
        customerDo=new CustomerDo();

        et_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_CustName.getText().toString()==null||et_CustName.getText().toString().equalsIgnoreCase("")||et_amount.getText().toString()==null||et_amount.getText().toString().equalsIgnoreCase(""))
                {
                    String alert_message="";
                    if (et_CustName.getText().toString()==null||et_CustName.getText().toString().equalsIgnoreCase(""))
                    {
                        alert_message="-Enter Customer Name\n";
                    }
                     if(et_amount.getText().toString()==null||et_amount.getText().toString().equalsIgnoreCase("")) {
                        alert_message=alert_message+"-Enter Amount \n";
                    }
                    try
                    {
                        Dialoginstance dialoginstance=new Dialoginstance(CreateCustomer.this,80,30,"ALERT",""+alert_message);
                        final Dialog dialog=dialoginstance.getdialoginstance();
                        Button btn_ok=dialoginstance.getBtn_ok();
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else{
                    String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd/HH:mm", Locale.getDefault()).format(new Date());
                    String uuid=new Uniqueid(getApplicationContext()).getCustomerUniqueId();
                    customerDo.id= uuid;
                    customerDo.name=et_CustName.getText().toString();
                    customerDo.place=et_place.getText().toString();
                    customerDo.number=et_number.getText().toString();
                    customerDo.amount=et_amount.getText().toString();
                    customerDo.dateandtime=currentDateandTime;

                    //Room
                    Customer customer=new Customer();
                    customer.customer_name=customerDo.name;
                    customer.customer_place=customerDo.place;
                    customer.customer_number=customerDo.number;
                    customer.totalamount=customerDo.amount;
                    customer.dateandtime=customerDo.dateandtime;
                    customer.IsDeleted="0";




                    if (!customerDo.id.equalsIgnoreCase("")) {
                       status =new CustomerDA(getApplicationContext()).insertCustomer(customerDo);
                        //ROOM
                        Appdatabase appdatabase=Appdatabase.getdbINSTANCE(getApplicationContext());
                        appdatabase.customerDao().insertCUST(customer);
                            status=true;
                        if (status){
                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Caught Some exception while creating unique",Toast.LENGTH_LONG).show();
                    }


                }


            }
        });
    }


}
