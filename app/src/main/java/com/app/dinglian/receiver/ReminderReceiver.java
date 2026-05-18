package com.app.dinglian.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.dinglian.service.ReminderService;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 启动服务检查需要联系的客户
        Intent serviceIntent = new Intent(context, ReminderService.class);
        context.startService(serviceIntent);
    }
}
