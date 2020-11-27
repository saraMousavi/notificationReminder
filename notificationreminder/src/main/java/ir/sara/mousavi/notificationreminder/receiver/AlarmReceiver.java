package ir.sara.mousavi.notificationreminder.receiver;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

import ir.sara.mousavi.notificationreminder.R;
import ir.sara.mousavi.notificationreminder.activity.NotificationMessage;

public class AlarmReceiver  extends BroadcastReceiver {
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras().getInt("isRepeat") == 1){
            new AlarmReceiver().setAlarm(context, null, intent.getExtras().getInt("id"), 20000, intent.getExtras().getString("title"));
        }
        Intent notificationIntent = new Intent(context, NotificationMessage.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("message", "text");
        PendingIntent mClick = PendingIntent.getActivity(context, intent.getExtras().getInt("id"), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Create Notification
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)  //R.mipmap.ic_launcher
                    .setContentTitle(intent.getExtras().getString("title"))
                    .setSmallIcon(R.drawable.ic_black_alarm)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);


            nManager.notify(intent.getExtras().getInt("id"), mBuilder.build());
            return;
        }

        // API_service 26 notification - NotificationChannel
        String channelId = "channel-id";
        String channelName = "ALARM";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            nManager.createNotificationChannel(mChannel);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)  //R.mipmap.ic_launcher
                    .setContentTitle(intent.getExtras().getString("title"))
                    .setContentIntent(mClick)
                    .setContentText(intent.getExtras().getString("title"))
                    .setSound(alarmSound)
                    .setPriority(NotificationManagerCompat.IMPORTANCE_MAX)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(context, R.color.green));
            vibrator.vibrate(300);

            //notify
            nManager.notify(1, mBuilder.build());
        }

    }

    public void setAlarm(Context context, Calendar calendar, int ID, long diffTime, String title) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("isRepeat", 1);
        intent.putExtra("title", title);
        intent.putExtra("id", ID);
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification time
//        Calendar c = Calendar.getInstance();
//        long currentTime = c.getTimeInMillis();
//        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using notification time
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + diffTime,
                    mPendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + diffTime,
                    mPendingIntent);
        } else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 5000,
                    mPendingIntent);
        }

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, AutoStartServicesReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, int ID, long RepeatTime) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("id", Integer.toString(ID));
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification timein
//        Calendar c = Calendar.getInstance();
//        long currentTime = c.getTimeInMillis();
//        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using initial notification time and repeat interval time
        System.out.println("RepeatTime = " + RepeatTime);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10000,
                RepeatTime , mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, AutoStartServicesReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel Alarm using Reminder ID
        mPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
        mAlarmManager.cancel(mPendingIntent);

        // Disable alarm
        ComponentName receiver = new ComponentName(context, AutoStartServicesReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


}
