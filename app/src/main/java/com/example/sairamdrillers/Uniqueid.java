package com.example.sairamdrillers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;
import java.util.Vector;

public class Uniqueid {
    SQLiteDatabase db;

    public Uniqueid(Context context){
        db=new Database(context).getinstance();
    }
    String getCustomerUniqueId(){
        Vector<String> vec_uuids=new Vector<String>();
        String uuid="";
        boolean flag=true;
        try {
            while(flag){
                flag=false;
                uuid=UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                Cursor cursor=db.rawQuery("select customer_id from customer_table",null);
                if (cursor.moveToFirst()){
                    do{
                        if (cursor.getString(0).equalsIgnoreCase(uuid)) {
                            flag=true;
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }

        }
        catch (Exception e){
            return "";
        }
        return uuid;

    }

    public String getPaymentUniqueId() {
        Vector<String> vec_uuids=new Vector<String>();
        String uuid="";
        boolean flag=true;
        try {
            while(flag){
                flag=false;
                uuid=UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                Cursor cursor=db.rawQuery("select payment_id from payment_table",null);
                if (cursor.moveToFirst()){
                    do{
                        if (cursor.getString(0).equalsIgnoreCase(uuid)) {
                            flag=true;
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }

        }
        catch (Exception e){
            return "";
        }
        return uuid;
    }
}
