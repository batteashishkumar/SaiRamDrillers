package com.example.sairamdrillers.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Customer.class},version = 1)
public abstract class Appdatabase extends RoomDatabase {

    public  abstract  CustomerDao customerDao();
    private static  Appdatabase INSTANCE;
    public static Appdatabase getdbINSTANCE(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),Appdatabase.class,"ROOM_DATABASE").allowMainThreadQueries().build();
        }
    return INSTANCE;
    }
}
