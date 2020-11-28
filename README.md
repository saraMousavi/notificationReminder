# notificationReminder
Reminder Library for Android:
If you want to set reminder in your android application only with invoking a function, use this library
# installation
For a working implementation, please have a look at app project
1.add dependency to Module gradle <br/>
``
dependencies {
    implementation 'com.github.saraMousavi:notificationReminder:v1.0'
}``
# usage
1.create an instance from ``AlarmReceiver`` class <br/>
``java
    AlarmReceiver alarmReceiver = new AlarmReceiver();`` <br/><br/>
2.Create Calender instance and set each time you want to remind <br/>
``java
    Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);``<br/><br/>
        
3.invoke ``setAlarm()`` function <br/>
``java
    alarmReceiver.setAlarm(getApplicationContext(), mCalendar, ID, reminder.getReminderTitle(),
                (repeatTypeSpinner.getSelectedItemPosition() == 0) ? 0 : 1,  intervalDuration);``
