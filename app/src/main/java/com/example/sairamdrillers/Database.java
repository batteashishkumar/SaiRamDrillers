package com.example.sairamdrillers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper
{
 public static final String database_name="sqlite.db";



    public Database(Context context) {
        super(context, database_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL("create table customer_table (customer_id text primary key,customer_name text,dateandtime text,customer_place text,customer_number text,totalamount text,Status text) ");
        db.execSQL("create table payment_table (payment_id text primary key,customer_id text,dateandtime text,payment_type text,payment_amount text) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists customer_table");
        db.execSQL("drop table if exists payment_table");
        onCreate(db);

    }
//    public boolean insertdata(String name_pass,String phone_pass)
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        try {
//
//
//            db.execSQL("insert into contact_table(name,phone) values ('" + name_pass + "','" + phone_pass + "')");
//            return true;
//        }
//        catch (Exception e)
//        {
//            return false;
//        }
//
//    }
//
//    public Cursor getalldata()
//    {
//        SQLiteDatabase db=this.getWritableDatabase();
//        Cursor res=db.rawQuery("select * from contact_table",null);
//        return res;
//    }


    public SQLiteDatabase getinstance()
    {
        return this.getWritableDatabase();
    }



}
