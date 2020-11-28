package ir.sara.mousavi.notificationreminder.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.ArrayList;
import java.util.Calendar;

import ir.sara.mousavi.notificationreminder.db.DataBaseHelper;
import ir.sara.mousavi.notificationreminder.db.holder.Reminder;
import ir.sara.mousavi.notificationreminder.utils.Init;


public class AutoStartServicesReceiver extends BroadcastReceiver {
    private String mTitle;
    private long mIntervalDuration;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay, mReceivedID;
    private long mRepeatDuration;
    private int mIsRepeat = 0;

    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            DataBaseHelper db = new DataBaseHelper(context);
            ArrayList<Reminder> reminders = db.getAllReminders();
            for (Reminder reminder : reminders) {
                mReceivedID = reminder.getReminderId();
                mTitle = reminder.getReminderTitle();
                //once reminder
                if (reminder.getReminderIntervalTime() == 0) {
                    if (Init.getDifferentBetweenToCalenderInMilliSecond(mCalendar) >= 0) {
                        String mDate = reminder.getReminderTime();
                        mDateSplit = (mDate.split(",")[0]).split("/");
                        mTimeSplit = (mDate.split(",")[1]).split(":");

                        mDay = Integer.parseInt(mDateSplit[2]);
                        mMonth = Integer.parseInt(mDateSplit[1]);
                        mYear = Integer.parseInt(mDateSplit[0]);
                        mHour = Integer.parseInt(mTimeSplit[0]);
                        mMinute = Integer.parseInt(mTimeSplit[1]);

                        mCalendar.set(Calendar.MONTH, --mMonth);
                        mCalendar.set(Calendar.YEAR, mYear);
                        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                        mCalendar.set(Calendar.MINUTE, mMinute);
                        mCalendar.set(Calendar.SECOND, 0);
                    }
                } else {
                    //repeated reminder
                    mIsRepeat = 1;
                    String mDate = reminder.getReminderTime();
                    mDateSplit = (mDate.split(",")[0]).split("/");
                    mTimeSplit = (mDate.split(",")[1]).split(":");

                    mDay = Integer.parseInt(mDateSplit[2]);
                    mMonth = Integer.parseInt(mDateSplit[1]);
                    mYear = Integer.parseInt(mDateSplit[0]);
                    mHour = Integer.parseInt(mTimeSplit[0]);
                    mMinute = Integer.parseInt(mTimeSplit[1]);

                    mCalendar.set(Calendar.MONTH, --mMonth);
                    mCalendar.set(Calendar.YEAR, mYear);
                    mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                    mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                    mCalendar.set(Calendar.MINUTE, mMinute);
                    mCalendar.set(Calendar.SECOND, 0);
                    switch (reminder.getReminderIntervalType()){
                        case "Hour":
                        case "hourly":
                            mIntervalDuration = AlarmManager.INTERVAL_HOUR * reminder.getReminderIntervalTime();
                            break;
                        case "Day":
                        case "daily":
                            mIntervalDuration = AlarmManager.INTERVAL_DAY * reminder.getReminderIntervalTime();
                            break;
                        case "Week":
                        case "weekly":
                            mIntervalDuration = AlarmManager.INTERVAL_DAY * 7 * reminder.getReminderIntervalTime();
                            break;
                        case "Month":
                        case "monthly":
                            mIntervalDuration = AlarmManager.INTERVAL_DAY * 30 * reminder.getReminderIntervalTime();
                            break;
                        case "Year":
                        case "yearly":
                            mIntervalDuration = AlarmManager.INTERVAL_DAY * 365 * reminder.getReminderIntervalTime();
                            break;
                    }
                    //if created time had passed
                    if (Init.getDifferentBetweenToCalenderInMilliSecond(mCalendar) < 0) {
                        switch (reminder.getReminderIntervalType()){
                            case "Hour":
                            case "hourly":
                                mCalendar.add(Calendar.MILLISECOND, (int) (milHour + Init.getDifferentBetweenToCalenderInMilliSecond(mCalendar)));
                                break;
                            case "Day":
                            case "daily":
                                mCalendar.add(Calendar.MILLISECOND, (int) (milDay + Init.getDifferentBetweenToCalenderInMilliSecond(mCalendar)));
                                break;
                            case "Week":
                            case "weekly":
                                mCalendar.add(Calendar.MILLISECOND, (int) (milWeek+ Init.getDifferentBetweenToCalenderInMilliSecond(mCalendar)));
                                break;
                            case "Month":
                            case "monthly":
                                mCalendar.add(Calendar.MILLISECOND, (int) (milMonth + Init.getDifferentBetweenToCalenderInMilliSecond(mCalendar)));
                                break;
                            case "Year":
                            case "yearly":
                                mCalendar.add(Calendar.MILLISECOND, (int) ((milDay * 365) + Init.getDifferentBetweenToCalenderInMilliSecond(mCalendar)));
                                break;
                        }
                    }
                }
                // Create a new notification
                mAlarmReceiver.setAlarm(context, mCalendar, mReceivedID, mTitle,
                        mIsRepeat, mIntervalDuration);
            }
        }
    }
}
