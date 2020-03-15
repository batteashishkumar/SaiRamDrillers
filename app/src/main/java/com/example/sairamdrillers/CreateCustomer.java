package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateCustomer extends AppCompatActivity {
    CustomerDo customerDo;
    EditText et_CustName,et_place,et_number,et_amount;
    boolean status=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);
        et_CustName=findViewById(R.id.et_CustName);
        et_place=findViewById(R.id.et_place);
        et_number=findViewById(R.id.et_number);
        et_amount=findViewById(R.id.et_amount);
        Button btn_save= findViewById(R.id.btn_save);
        customerDo=new CustomerDo();
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
                    if (!customerDo.id.equalsIgnoreCase("")) {
                       status =new CustomerDA(getApplicationContext()).insertCustomer(customerDo);
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
