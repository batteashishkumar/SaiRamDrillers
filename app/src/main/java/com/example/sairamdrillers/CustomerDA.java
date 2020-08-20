package com.example.sairamdrillers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;
import java.util.Vector;

class CustomerDA {
    SQLiteDatabase db;

    public CustomerDA(Context context){
        db=new Database(context).getinstance();
    }
    Boolean insertCustomer(CustomerDo customerDo){
        try {
            db.execSQL("insert into customer_table(customer_id,customer_name,dateandtime,customer_place,customer_number,totalamount) values ('" + customerDo.id + "','" + customerDo.name + "','" + customerDo.dateandtime + "','" + customerDo.place + "','" + customerDo.number + "','" + customerDo.amount + "')");
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    public Vector<CustomerDo> getallCustomers() {

        Vector<CustomerDo> vecCustomers=new Vector<>();
        try {
                Cursor cursor=db.rawQuery("select customer_id,customer_name,customer_place,customer_number,totalamount,dateandtime from customer_table where  ifnull(IsDeleted,0) like 0 order by dateandtime desc",null);
                if (cursor.moveToFirst()){
                    do{
                       CustomerDo customerDo=new CustomerDo();
                       customerDo.id=cursor.getString(0);
                        customerDo.name=cursor.getString(1);
                        customerDo.place=cursor.getString(2);
                        customerDo.number=cursor.getString(3);
                        customerDo.amount=cursor.getString(4);
                        customerDo.dateandtime=cursor.getString(5);
                        vecCustomers.add(customerDo);
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }


        catch (Exception e){
            return vecCustomers;
        }
        return vecCustomers;

    }

    public String getCustomertotalamount(String id) {
        String totalamount="";
        try {
            Cursor cursor=db.rawQuery("select totalamount from customer_table where customer_id like '"+id+"' and ifnull(IsDeleted,0) like 0",null);
            if (cursor.moveToFirst()){
                do{
                    totalamount=cursor.getString(0);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }


        catch (Exception e){
            return "";
        }
        return totalamount;
    }
    public String getallCustomertotalamount() {
        String totalamount="";
        try {
            Cursor cursor=db.rawQuery("select sum(totalamount) from customer_table where ifnull(IsDeleted,0) like 0",null);
            if (cursor.moveToFirst()){
                do{
                    totalamount=cursor.getString(0);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }


        catch (Exception e){
            return "";
        }
        return totalamount;
    }
    public Boolean setnewTotalamt(String Totalamtnew,String customerid) {
        String totalamount="";
        try {
            db.execSQL("UPDATE customer_table SET totalamount='"+Totalamtnew+"' WHERE customer_id='"+customerid+"'");
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public Boolean deletecustomer(CustomerDo customerDo) {
        try{
          db.execSQL("UPDATE customer_table SET IsDeleted='1' WHERE customer_id='"+customerDo.id+"'");
        }
        catch (Exception e){
            return false;
        }



        return  true;
    }
}
