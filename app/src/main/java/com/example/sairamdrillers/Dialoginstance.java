package com.example.sairamdrillers;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Dialoginstance extends Activity {
    Dialog dialog;
    TextView tv_body,tv_title;
    EditText et_newpayment;
    Context context;
    int width,height;
    String title,body;
    Button btn_ok,btn_yes;
    public Dialoginstance(Context context,float width_percentage,float height_percentage,String title,String body){
        this.context=context;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        btn_ok=dialog.findViewById(R.id.btn_ok);
        btn_yes=dialog.findViewById(R.id.btn_yes);
        tv_body=dialog.findViewById(R.id.tv_body);
        tv_title=dialog.findViewById(R.id.tv_title);
        et_newpayment=dialog.findViewById(R.id.et_newpayment);
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
    Button getBtn_yes()
    {
        return btn_yes;
    }

    public TextView getTv_body() {
        return tv_body;
    }

    public void setTv_body(TextView tv_body) {
        this.tv_body = tv_body;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public EditText getEt_newpayment() {
        return et_newpayment;
    }

    public void setEt_newpayment(EditText et_newpayment) {
        this.et_newpayment = et_newpayment;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setBtn_ok(Button btn_ok) {
        this.btn_ok = btn_ok;
    }
}
