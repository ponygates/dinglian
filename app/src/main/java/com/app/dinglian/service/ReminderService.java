package com.app.dinglian.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.app.dinglian.R;
import com.app.dinglian.activity.MainActivity;
import com.app.dinglian.database.DatabaseHelper;
import com.app.dinglian.model.Customer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderService extends Service {
    private static final String CHANNEL_ID = "reminder_channel";
    private static final int NOTIFICATION_ID = 1001;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkAndShowNotification();
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 检查需要联系的客户并显示通知
     */
    private void checkAndShowNotification() {
        List<Customer> allCustomers = databaseHelper.getAllCustomers();
        int needContactCount = 0;

        for (Customer customer : allCustomers) {
            if (isNeedContact(customer)) {
                needContactCount++;
            }
        }

        if (needContactCount > 0) {
            showNotification(needContactCount);
        }
    }

    /**
     * 检查是否需要联系
     */
    private boolean isNeedContact(Customer customer) {
        String followDateStr = customer.getFollowDate();
        if (followDateStr == null || followDateStr.isEmpty()) {
            return true;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date followDate = sdf.parse(followDateStr);
            Calendar followCalendar = Calendar.getInstance();
            followCalendar.setTime(followDate);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long diffInMillis = today.getTimeInMillis() - followCalendar.getTimeInMillis();
            long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);

            int weeks = customer.getContactIntervalWeeks();
            long days = weeks * 7;

            return diffInDays >= days;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 创建通知渠道
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "客户联系提醒";
            String description = "提醒您有客户需要联系";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 显示通知
     */
    private void showNotification(int count) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle("客户联系提醒")
                .setContentText("有 " + count + " 位客户需要今天联系")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
