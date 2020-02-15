package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
Vector<CustomerDo> vecCustomers=new Vector<>();
TextView tv_totalamt,tv_pendingamt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        tv_totalamt=findViewById(R.id.tv_totalamt);
        tv_pendingamt=findViewById(R.id.tv_pendingamt);





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


























}
