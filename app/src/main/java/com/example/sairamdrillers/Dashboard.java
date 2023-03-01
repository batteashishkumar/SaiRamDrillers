package com.example.sairamdrillers;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sairamdrillers.databinding.ActivityMainBinding;
import com.example.sairamdrillers.room.Appdatabase;
import com.example.sairamdrillers.room.Customer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class Dashboard extends Base {
RecyclerView recyclerView;
Vector<CustomerDo> vecCustomers=new Vector<>();
    List<Customer> vecCustomersRoom=null;
//    private StorageReference mStorageRef;
TextView tv_totalamt,tv_pendingamt,tv_writequote,tv_Bill;
    private boolean swipeBack = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setDashboardviewmodel(new DashboardViewModel());
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        tv_totalamt=findViewById(R.id.tv_totalamt);
        tv_pendingamt=findViewById(R.id.tv_pendingamt);
        tv_writequote=findViewById(R.id.tv_writequote);
        tv_Bill=findViewById(R.id.tv_Bill);
//        mStorageRef = FirebaseStorage.getInstance().getReference();
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(
                Dashboard.this, this));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        TextView tv_create=findViewById(R.id.tv_create);
//        tv_create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), CreateCustomer.class);
//                startActivity(i);
//
//            }
//        });

        tv_writequote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent=new Intent(Dashboard.this,PdfList.class);
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(Dashboard.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });
        tv_Bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Bill.setClickable(false);
                tv_Bill.setClickable(false);

                Intent intent=new Intent(getApplicationContext(),Billing_Entry.class);
                startActivity(intent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_Bill.setClickable(true);
                        tv_Bill.setClickable(true);
                    }
                },1000);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                if (swipeBack) {
                    swipeBack = false;
                    return 0;
                }
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                Dialoginstance dialoginstance=new Dialoginstance(Dashboard.this,80,25,"ALERT!!","Are you sure want to delete this customer?");//dont use getapplicationcontext()
                final Dialog dialog=dialoginstance.getdialoginstance();
                Button btn_no=dialoginstance.getBtn_ok();
                btn_no.setText("NO");
                EditText  Et_newpayment=dialoginstance.getEt_newpayment();
                Button btn_yes=dialoginstance.getBtn_yes();
                btn_yes.setVisibility(View.VISIBLE);
                Et_newpayment.setVisibility(View.GONE);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        loaddata();
                    }
                });
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean status=new CustomerDA(getApplicationContext()).deletecustomer(vecCustomers.get(viewHolder.getAdapterPosition()));
                        if (status==true)
                        {
                            Toast.makeText(getApplicationContext(),"Customer Deleted Successfully",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loaddata();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Customer Not Deleted",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loaddata();
                        }

                    }
                });
                dialog.show();

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view

                super.onChildDraw(c, recyclerView, viewHolder, dX/2, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

//        Uri uri = Uri.fromFile(new File(getDatabasePath("sqlite.db").getPath()));
//        StorageReference riversRef = mStorageRef.child("database/sqlite.db");
//        riversRef.putFile(uri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
//                    }
//                });




    }

    private void setTouchListener(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loaddata();
    }
    public void loaddata(){
        String totalamt=new CustomerDA(this).getallCustomertotalamount();
        String paid=new PaymentDA(this).getsumofallpaymentsofcustomers();
        if (totalamt==null||totalamt.equalsIgnoreCase(""))
            totalamt="0";
        if (paid==null||paid.equalsIgnoreCase(""))
            paid="0";
        int pending=Integer.parseInt(totalamt)-Integer.parseInt(paid);
        tv_totalamt.setText(totalamt+".0");
        tv_pendingamt.setText(pending+".0");
        vecCustomers=new CustomerDA(this).getallCustomers();
        try {
            vecCustomersRoom= Appdatabase.getdbINSTANCE(getApplicationContext()).customerDao().getallCustomersRoom();
        }catch (Exception e){

        }

        recyclerView.setAdapter(new MyAdapter(vecCustomers));

    }

    public void closeapp() {
        Toast.makeText(Dashboard.this,"UncaughtException:Check report",Toast.LENGTH_LONG).show();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private Vector<CustomerDo> vecCustomers;

        public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_custname,tv_total,tv_datetime,tv_status,tv_pending,tv_paid,tv_pending_color,tv_customerid;
            public MyViewHolder(View v) {
                super(v);
//                tv_pending_color=v.findViewById(R.id.tv_pending_color);
                tv_customerid=v.findViewById(R.id.tv_customerid);
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
            try {
                holder.tv_customerid.setText("CustomerId:"+vecCustomers.get(position).id);
                holder.tv_custname.setText(vecCustomers.get(position).name);
                holder.tv_datetime.setText(vecCustomers.get(position).dateandtime);
                String totalamount=vecCustomers.get(position).amount;
                String paid=new PaymentDA(getApplicationContext()).getsumofallpaymentsofcustomer(vecCustomers.get(position).id);
                if (totalamount==null)
                    totalamount="0";
                if (paid==null)
                    paid="0";
                if(totalamount.equalsIgnoreCase(""))
                    totalamount="0";
                if(paid.equalsIgnoreCase(""))
                    paid="0";
                int pending=Integer.parseInt(totalamount)-Integer.parseInt(paid);
                String pendingstr=String.valueOf(pending);
                holder.tv_total.setText(totalamount+".0");
                holder.tv_paid.setText(paid+".0");
                holder.tv_pending.setText(pendingstr+".0");
                if (pending<=0)
                {
                    holder.tv_status.setText("Completed");
                    holder.tv_status.setBackground(ContextCompat.getDrawable(Dashboard.this,R.drawable.textview_sty_green));
                    holder.tv_pending.setTextColor(getResources().getColor(R.color.green));

                }


                else
                {
                    holder.tv_status.setText("Pending");
                    holder.tv_status.setBackground(ContextCompat.getDrawable(Dashboard.this,R.drawable.textview_sty_red));
                    holder.tv_pending.setTextColor(getResources().getColor(R.color.red));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public int getItemCount() {
            return vecCustomers.size();
        }
    }











}
