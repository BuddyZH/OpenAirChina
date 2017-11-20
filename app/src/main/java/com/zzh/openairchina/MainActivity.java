package com.zzh.openairchina;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PACKAGE_NAME = "com.rytong.airchina";
    private static final String TAG = "ZZHTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onOpenAirChina(View view) {
        launchApp(getApplicationContext());
    }

    public static void launchApp(Context context) {
        if (isAppInstalled(context, APP_PACKAGE_NAME)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE_NAME));
        } else {
            Toast.makeText(context, "The App is not installed", Toast.LENGTH_SHORT);
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
