package com.zzh.openairchina;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final String APP_PACKAGE_NAME = "com.rytong.airchina";
    private static final String TAG = "ZZHTAG";

    private Dialog mTipsDialog;

    private final static int MSG_FIRSET = 1;
    private final static int MSG_SECOND = 2;
    private final static int MSG_THIRD = 3;

    private static long DELAY_TIME_FIRST = 5000L;
    private static long DELAY_TIME_SECOND = 2000L;
    private static long DELAY_TIME_THIRD = 5000L;

    private int i = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FIRSET:
                    Intent intent = new Intent("auto.click");
                    intent.putExtra("flag", 1);
                    intent.putExtra("id", "ll_home_me");

                    sendBroadcast(intent);
                    mHandler.sendEmptyMessageDelayed(MSG_SECOND, DELAY_TIME_SECOND);
                    break;
                case MSG_SECOND:
                    Intent secondIntent = new Intent("auto.click");
                    secondIntent.putExtra("flag", 1);
                    secondIntent.putExtra("id", "fl_sign_up");

                    sendBroadcast(secondIntent);
                    mHandler.sendEmptyMessageDelayed(MSG_THIRD, DELAY_TIME_THIRD);
                    break;
                case MSG_THIRD:
                    Intent thirdIntent = new Intent("auto.click");
                    thirdIntent.putExtra("flag", 1);
                    thirdIntent.putExtra("id", "rl_sign_up_sign");

                    sendBroadcast(thirdIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ClickService.isRunning()) {
            if (mTipsDialog != null) {
                mTipsDialog.dismiss();
            }
            mHandler.sendEmptyMessageDelayed(MSG_FIRSET, DELAY_TIME_FIRST);
            launchApp(this);
        } else {
            showOpenAccessibilityServiceDialog();
        }
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

    /**
     * 显示未开启辅助服务的对话框
     */
    private void showOpenAccessibilityServiceDialog() {
        if (mTipsDialog != null && mTipsDialog.isShowing()) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.dialog_tips_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilityServiceSettings();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("需要开启辅助服务正常使用");
        builder.setView(view);
        builder.setPositiveButton("打开辅助服务", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibilityServiceSettings();
            }
        });
        mTipsDialog = builder.show();
    }

    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "找[openairchina],然后开启服务", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
