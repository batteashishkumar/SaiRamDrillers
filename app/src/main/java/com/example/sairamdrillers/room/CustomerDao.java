package com.example.sairamdrillers.room;

import android.graphics.drawable.VectorDrawable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Vector;

@Dao
public interface CustomerDao {
    @Query("select customer_id,customer_name,customer_place,customer_number,totalamount,dateandtime from customer where  ifnull(IsDeleted,0) like 0 order by dateandtime desc")
    List<Customer> getallCustomersRoom();

    @Insert
    void insertCUST(Customer... customers);

    @Delete
    void delete(Customer customer);

}
