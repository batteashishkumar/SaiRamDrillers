package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class CustomerDashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomerDo customerDo;
    TextView tv_customername,tv_place,tv_phonenumber,tv_datetime,tv_edit_totalamt,tv_totalamt,tv_pendingamt,tv_amountpaid;
    ImageView iv_editcustomer;
    EditText et_newpayment;
    Button btn_newpayment,btn_settlepending;
    String totalamount="";
    Vector<PaymentDo> vecPaymentsofCustomers=new Vector<PaymentDo>();
    int amountpaid=0;
    PaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        customerDo=(CustomerDo)getIntent().getSerializableExtra("customerDo");

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
        tv_phonenumber.setText(""+customerDo.number);
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


    }

    private void loaddata() {

        totalamount=new CustomerDA(this).getCustomertotalamount(customerDo.id);
        tv_totalamt.setText(totalamount);
        vecPaymentsofCustomers=new PaymentDA(this).getallpaymentsofcustomer(customerDo.id);
        amountpaid=0;
        for(int i=0;i<vecPaymentsofCustomers.size();i++){
            amountpaid=amountpaid+Integer.valueOf(vecPaymentsofCustomers.get(i).paymentAmount);
        }
        tv_amountpaid.setText(String.valueOf(amountpaid));
        tv_pendingamt.setText(String.valueOf(Integer.valueOf(totalamount)-Integer.valueOf(amountpaid)));


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
            holder.tv_amt_cell.setText(vecPaymentsofCustomers.get(position).paymentAmount);
            holder.tv_type_cell.setText(vecPaymentsofCustomers.get(position).paymentType);
            holder.tv_datetime.setText(vecPaymentsofCustomers.get(position).payment_datetime);


        }
        @Override
        public int getItemCount() {
            return vecPaymentsofCustomers.size();
        }
    }

}
