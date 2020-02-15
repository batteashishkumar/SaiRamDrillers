package com.example.sairamdrillers;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

public class Dialoginstance extends Activity {
    Dialog dialog;
    TextView tv_body,tv_title;
    Context context;
    int width,height;
    String title,body;
    Button btn_ok;
    public Dialoginstance(Context context,float width_percentage,float height_percentage,String title,String body){
        this.context=context;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        btn_ok=dialog.findViewById(R.id.btn_ok);
        tv_body=dialog.findViewById(R.id.tv_body);
        tv_title=dialog.findViewById(R.id.tv_title);
        tv_body.setMovementMethod(new ScrollingMovementMethod());
        width_percentage=width_percentage/100;
        height_percentage=height_percentage/100;
        width = (int)(context.getResources().getDisplayMetrics().widthPixels*width_percentage);
        height = (int)(context.getResources().getDisplayMetrics().heightPixels*height_percentage);
        dialog.getWindow().setLayout(width,height);
        this.title=title;
        this.body=body;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    Dialog getdialoginstance(){
        tv_body.setText(body+"");
        tv_title.setText(title+"");
        return dialog;
    }
    Button getBtn_ok()
    {
        return btn_ok;
    }
}
