package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class CustomerDashboard extends Base {
    RecyclerView recyclerView;
    CustomerDo customerDo;
    TextView tv_customername,tv_place,tv_phonenumber,tv_datetime,tv_edit_totalamt,tv_totalamt,tv_pendingamt,tv_amountpaid,tv_sendquote;
    ImageView iv_editcustomer;
    EditText et_newpayment;
    Button btn_newpayment,btn_settlepending;
    String totalamount="";
    Vector<PaymentDo> vecPaymentsofCustomers=new Vector<PaymentDo>();
    int amountpaid=0,pendingamt=0;
    PaymentAdapter paymentAdapter;
    String Totalamtnew="";
    EditText et_totalamt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        customerDo=(CustomerDo)getIntent().getSerializableExtra("customerDo");
        tv_sendquote=findViewById(R.id.tv_sendquote);
        tv_customername=findViewById(R.id.tv_customername);
        tv_place=findViewById(R.id.tv_place);
        tv_phonenumber=findViewById(R.id.tv_phonenumber);
        tv_datetime=findViewById(R.id.tv_datetime);
        tv_edit_totalamt=findViewById(R.id.tv_edit_totalamt);
        tv_totalamt=findViewById(R.id.tv_totalamt);
        tv_pendingamt=findViewById(R.id.tv_pendingamt);
        tv_amountpaid=findViewById(R.id.tv_amountpaid);

        iv_editcustomer=findViewById(R.id.iv_editcustomer);

        btn_newpayment=findViewById(R.id.btn_newpayment);
        btn_settlepending=findViewById(R.id.btn_settlepending);

        et_newpayment=findViewById(R.id.et_newpayment);

        recyclerView = (RecyclerView) findViewById(R.id.rv_payment_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tv_customername.setText(""+customerDo.name);
        tv_place.setText(""+customerDo.place);
        tv_phonenumber.setText("+91-"+customerDo.number);
        tv_datetime.setText(""+customerDo.dateandtime);


        loaddata();

        paymentAdapter=new PaymentAdapter();

        recyclerView.setAdapter(paymentAdapter);
        btn_newpayment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PaymentDo paymentDo=new PaymentDo();
        if(et_newpayment.getText().toString().equalsIgnoreCase("")||et_newpayment.getText().toString().equalsIgnoreCase("0")){
            Toast.makeText(getApplicationContext(),"Enter Amount Greater Than Zero",Toast.LENGTH_SHORT).show();
        }
        else{
            String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd/HH:mm", Locale.getDefault()).format(new Date());
            String paymentuuid=new Uniqueid(getApplicationContext()).getPaymentUniqueId();
            paymentDo.paymentId=paymentuuid;
            paymentDo.customerId=customerDo.id;
            paymentDo.payment_datetime=currentDateandTime;
            paymentDo.paymentType="Cash";
            paymentDo.paymentAmount=et_newpayment.getText().toString();
            Boolean Status=new PaymentDA(getApplicationContext()).insertPayment(paymentDo);
            if (Status==true){
                Toast.makeText(getApplicationContext(),"Payment saved",Toast.LENGTH_SHORT).show();
                loaddata();
                paymentAdapter.notifyDataSetChanged();
                et_newpayment.setText("");
            }
            else {Toast.makeText(getApplicationContext(),"Payment Not Saved",Toast.LENGTH_SHORT).show();  }


        }
    }
});


        tv_phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CustomerDashboard.this, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED)
                {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", customerDo.number, null)));
                }
            }
        });


        tv_edit_totalamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialoginstance dialoginstance=new Dialoginstance(CustomerDashboard.this,80,25,"Total Amount","Current Total Amount:"+totalamount+".00");//dont use getapplicationcontext()
                final Dialog dialog=dialoginstance.getdialoginstance();
                Button btn_ok=dialoginstance.getBtn_ok();
                et_totalamt=dialoginstance.getEt_newpayment();
                et_totalamt.setVisibility(View.VISIBLE);
                btn_ok.setText("Save");
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Totalamtnew=et_totalamt.getText().toString();
                       if(Totalamtnew.equalsIgnoreCase(""))
                       {
                          Toast.makeText(CustomerDashboard.this,"Enter Amount greater than zero",Toast.LENGTH_SHORT).show();
                       }
                       else {
                           if (0==Integer.parseInt(Totalamtnew)){
                               Toast.makeText(CustomerDashboard.this,"Enter Amount greater than zero",Toast.LENGTH_SHORT).show();
                           }
                           else if(Integer.parseInt(Totalamtnew)<amountpaid){
                               Toast.makeText(CustomerDashboard.this,"Customer Already paid :"+amountpaid+".00",Toast.LENGTH_SHORT).show();
                           }
                           else {
                            Boolean status=new CustomerDA(getApplicationContext()).setnewTotalamt(Totalamtnew,customerDo.id);
                            if(status==true)
                            {
                                Toast.makeText(CustomerDashboard.this,"Success Updated",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                loaddata();
                            }
                            else {
                                Toast.makeText(CustomerDashboard.this,"Failed to update",Toast.LENGTH_SHORT).show();

                            }
                           }

                       }



                    }
                });
                dialog.show();
            }
        });
        tv_sendquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Quotation_Entry.class);
                intent.putExtra("customerDo",customerDo);
                startActivity(intent);
            }
        });
    }

    private void loaddata() {

        totalamount=new CustomerDA(this).getCustomertotalamount(customerDo.id);
        if(totalamount.equalsIgnoreCase(""))
            totalamount="0";
        tv_totalamt.setText(totalamount+".0");
        vecPaymentsofCustomers=new PaymentDA(this).getallpaymentsofcustomer(customerDo.id);
        amountpaid=0;
        for(int i=0;i<vecPaymentsofCustomers.size();i++){
            amountpaid=amountpaid+Integer.valueOf(vecPaymentsofCustomers.get(i).paymentAmount);
        }
        pendingamt=Integer.valueOf(totalamount)-Integer.valueOf(amountpaid);
        tv_amountpaid.setText(String.valueOf(amountpaid)+".0");
        tv_pendingamt.setText(String.valueOf(pendingamt)+".0");

        et_newpayment.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String enteredtext=s.toString();
                if(enteredtext.equalsIgnoreCase(""))
                    enteredtext="0";
                if(pendingamt<Integer.parseInt(enteredtext))
                {
                    et_newpayment.getText().clear();
                    Toast.makeText(getApplicationContext(),"Enter Amount Less Than Pending Amount",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_amt_cell,tv_type_cell,tv_datetime;
            public MyViewHolder(View v) {
                super(v);
                tv_amt_cell=v.findViewById(R.id.tv_amt_cell);
                tv_type_cell=v.findViewById(R.id.tv_type_cell);
                tv_datetime=v.findViewById(R.id.tv_datetime);
            }
        }
        @Override
        public PaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_payment_history, parent, false);
            MyViewHolder vh = new MyViewHolder(itemView);
            return vh;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_amt_cell.setText(vecPaymentsofCustomers.get(position).paymentAmount+".0");
            holder.tv_type_cell.setText(vecPaymentsofCustomers.get(position).paymentType);
            holder.tv_datetime.setText(vecPaymentsofCustomers.get(position).payment_datetime);


        }
        @Override
        public int getItemCount() {
            return vecPaymentsofCustomers.size();
        }
    }

}
