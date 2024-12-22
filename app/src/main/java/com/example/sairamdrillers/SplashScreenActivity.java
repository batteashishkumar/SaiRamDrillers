package com.example.sairamdrillers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {
    private  static  int SPLASH_SCREEN =1500;

    ImageView imageView;
    TextView textView1, textView2;
    Animation top, bottom;
    String[] permissions;
    boolean allpermissionsgranted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(
                SplashScreenActivity.this, this));
        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        imageView.setAnimation(top);
        textView1.setAnimation(bottom);
        textView2.setAnimation(bottom);




    }
    @Override
    protected void onResume() {
        super.onResume();
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions = info.requestedPermissions;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                permissionrequest();
            }
        },SPLASH_SCREEN);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        allpermissionsgranted=false;
        if(requestCode==163)
        {
            for (int i=0;i<grantResults.length;i++)
            {
                if (grantResults[i]==PackageManager.PERMISSION_GRANTED)
                {
                    allpermissionsgranted=true;
                }
                else
                {
                    allpermissionsgranted=false;
                    break;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
            if (permissions.length == 1){
                if (permissions[0].equalsIgnoreCase("android.permission.MANAGE_EXTERNAL_STORAGE") ){
                    allpermissionsgranted=true;
                }
            }
            if (allpermissionsgranted)
            {
                Intent intent = new Intent(SplashScreenActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
            else
            {
//               permissionrequest();

            }
        }


    }

    private void permissionrequest()
    {
        boolean allperforfirsttime=false;
        HashMap<String, Boolean> hmpermissionsvalidate=new HashMap<>();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            for (int i=0;i<permissions.length;i++)
            {
                if (permissions[i].equalsIgnoreCase("android.permission.SYSTEM_ALERT_WINDOW"))
                    continue;
                if (!hmpermissionsvalidate.containsKey(permissions[i]))
                hmpermissionsvalidate.put(permissions[i],false);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                    hmpermissionsvalidate.put(permissions[i],true);
                } else {
                    hmpermissionsvalidate.put(permissions[i],false);
                    ActivityCompat.requestPermissions(SplashScreenActivity.this,new String[]{permissions[i]}, 163);
                }
            }
        }
        for (int i=0;i<permissions.length;i++)
        {
            if (permissions[i].equalsIgnoreCase("android.permission.SYSTEM_ALERT_WINDOW"))
                continue;
            if (hmpermissionsvalidate.containsKey(permissions[i])&&hmpermissionsvalidate.get(permissions[i])){
                allperforfirsttime=true;
            }
            else
            {
                allperforfirsttime=false;
                break;
            }
        }
        if (allperforfirsttime)
        {
            Intent intent = new Intent(SplashScreenActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        }


    }

}
