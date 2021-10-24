package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;

public class Base extends AppCompatActivity {
    int activity=0;

    public double StringToInt(String i){
        try {
            double x=Double.parseDouble(i);
            return x;
        }catch (Exception e){
            return 0;
        }

    }
}
