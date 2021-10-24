package com.example.sairamdrillers.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Customer {
    @PrimaryKey(autoGenerate = true)
    public int customer_id;
    @ColumnInfo(name = "customer_name")
 public String customer_name="";
    @ColumnInfo(name = "customer_place")
     public String customer_place="";
    @ColumnInfo(name = "customer_number")
     public String customer_number="";
    @ColumnInfo(name = "totalamount")
 public String totalamount="";
    @ColumnInfo(name = "dateandtime")
     public String dateandtime="";
    @ColumnInfo(name = "IsDeleted")
    public String IsDeleted="";

}

