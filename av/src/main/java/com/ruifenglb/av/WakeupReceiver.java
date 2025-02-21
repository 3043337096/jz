package com.ruifenglb.av;

import static com.ruifenglb.av.play.AvVideoController.activity;
import static com.ruifenglb.av.play.AvVideoController.pendingIntent;
import static com.ruifenglb.av.play.AvVideoController.tv_timer_close;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import java.util.Calendar;

public class WakeupReceiver extends BroadcastReceiver {
    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void onReceive(Context context, Intent intent) {

        AlertDialog.Builder builder;
        Handler handler=new Handler();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(pendingIntent!=null)
                    alarmManager.cancel(pendingIntent);
                if(activity!=null)
                    activity.finishAffinity();

            }
        };
        handler.postDelayed(runnable,10000);

        if(activity!=null){
            builder=new AlertDialog.Builder(activity)
                .setTitle("10秒后自动退出本app")
                .setNegativeButton("取消退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(tv_timer_close!=null)
                            tv_timer_close.setText("定时关闭");
                        if(pendingIntent!=null)
                            alarmManager.cancel(pendingIntent);
                        handler.removeCallbacks(runnable);
                    }
                });

            AlertDialog dialog = builder.create();      //创建AlertDialog对象
            //对话框显示的监听事件
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    // 获取对话框
                    AlertDialog alertDialog = (AlertDialog) dialog;
                    // 设置背景颜色
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0000")));
                }
            });
            //对话框消失的监听事件
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
            dialog.show();                              //显示对话框
        }

    }
}