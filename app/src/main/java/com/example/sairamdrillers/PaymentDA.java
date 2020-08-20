package com.example.sairamdrillers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

public class PaymentDA {

    SQLiteDatabase db;

    public PaymentDA(Context context){
        db=new Database(context).getinstance();
    }
    Boolean insertPayment(PaymentDo paymentDo){
        try {
            db.execSQL("insert into payment_table(payment_id,customer_id,dateandtime,payment_type,payment_amount) values ('" + paymentDo.paymentId + "','" + paymentDo.customerId + "','" + paymentDo.payment_datetime + "','" + paymentDo.paymentType + "','" + paymentDo.paymentAmount + "')");
            return true;
        }
        catch (Exception e){
            return false;
        }

    }


    public Vector<PaymentDo> getallpaymentsofcustomer(String customer_id) {
        Vector<PaymentDo> vecPaymentsofCustomers=new Vector<>();
        try {
            Cursor cursor=db.rawQuery("select payment_id,customer_id,dateandtime,payment_type,payment_amount from payment_table where customer_id like '"+customer_id+"' ORDER BY dateandtime DESC",null);
            if (cursor.moveToFirst()){
                do{
                    PaymentDo paymentDo=new PaymentDo();
                    paymentDo.paymentId=cursor.getString(0);
                    paymentDo.customerId=cursor.getString(1);
                    paymentDo.payment_datetime=cursor.getString(2);
                    paymentDo.paymentType=cursor.getString(3);
                    paymentDo.paymentAmount=cursor.getString(4);
                    vecPaymentsofCustomers.add(paymentDo);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }


        catch (Exception e){
            return vecPaymentsofCustomers;
        }
        return vecPaymentsofCustomers;
    }
    public String getsumofallpaymentsofcustomer(String customer_id) {
        String sum="";
        try {
            Cursor cursor=db.rawQuery("select sum(payment_amount) from payment_table where customer_id like '"+customer_id+"' ",null);
            if (cursor.moveToFirst()){
                do{
                    sum=cursor.getString(0);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch (Exception e){
            return sum;
        }
        return sum;
    }
    public String getsumofallpaymentsofcustomers() {
        String sum="";
        try {
            Cursor cursor=db.rawQuery("SELECT sum(pt.payment_amount) FROM payment_table pt inner join customer_table ct on ct.customer_id=pt.customer_id WHERE ifnull(ct.IsDeleted,0) like 0",null);
            if (cursor.moveToFirst()){
                do{
                    sum=cursor.getString(0);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch (Exception e){
            return sum;
        }
        return sum;
    }
}
