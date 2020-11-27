package ir.sara.mousavi.notificationreminder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import ir.sara.mousavi.notificationreminder.db.holder.Reminder;

public class DataBaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "notification.db";

    //Project Table
    public static final String TABLE_REMINDER = "reminder";

    public static final String REMINDER_COLUMN_ID = "rmndrId";
    public static final String REMINDER_COLUMN_TITLE = "rmndrTitle";
    public static final String REMINDER_COLUMN_TIME = "rmndrTime";
    public static final String REMINDER_COLUMN_INTERVAL_TYPE = "rmndrIntervalType";
    public static final String REMINDER_COLUMN_INTERVAL_TIME = "rmndrIntervalTime";
    // Create table Project
    public static final String CREATE_TABLE_REMINDER =
            "CREATE TABLE " + TABLE_REMINDER + "("
                    + REMINDER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"//
                    + REMINDER_COLUMN_TITLE + " TEXT,"//
                    + REMINDER_COLUMN_TIME + " TEXT,"
                    + REMINDER_COLUMN_INTERVAL_TYPE + " TEXT,"
                    + REMINDER_COLUMN_INTERVAL_TIME + " INTEGER"
                    + ")";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
            onCreate(db);
        }
    }
    //------------------------Reminder-----------------
    public long insertReminder(Reminder reminder) {
        // get writable database as we want to write data


        ContentValues values = new ContentValues();
        values.put(REMINDER_COLUMN_TITLE, reminder.getReminderTitle());
        values.put(REMINDER_COLUMN_TIME, reminder.getReminderTime());
        values.put(REMINDER_COLUMN_INTERVAL_TIME, reminder.getReminderIntervalTime());
        values.put(REMINDER_COLUMN_INTERVAL_TYPE, reminder.getReminderIntervalType());
        // insert row
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_REMINDER, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Reminder getReminder(int reminderId) {
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDER + " where " + REMINDER_COLUMN_ID + " = '" + reminderId + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Reminder reminder = null;
        if (cursor.moveToNext()) {
            do {
                reminder = new Reminder();
                reminder.setReminderId(cursor.getInt(cursor.getColumnIndex(REMINDER_COLUMN_ID)));
                reminder.setReminderTitle(cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_TITLE)));
                reminder.setReminderTime(cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_TIME)));
                reminder.setReminderIntervalTime(cursor.getInt(cursor.getColumnIndex(REMINDER_COLUMN_INTERVAL_TIME)));
                reminder.setReminderIntervalType(cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_INTERVAL_TYPE)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reminder;
    }


    public ArrayList<Reminder> getAllReminders() {
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Reminder> reminders = new ArrayList<>();
        if (cursor.moveToNext()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setReminderId(cursor.getInt(cursor.getColumnIndex(REMINDER_COLUMN_ID)));
                reminder.setReminderTitle(cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_TITLE)));
                reminder.setReminderTime(cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_TIME)));
                reminder.setReminderIntervalTime(cursor.getInt(cursor.getColumnIndex(REMINDER_COLUMN_INTERVAL_TIME)));
                reminder.setReminderIntervalType(cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_INTERVAL_TYPE)));
                reminders.add(reminder);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reminders;
    }

    public int getReminderCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REMINDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REMINDER_COLUMN_ID, reminder.getReminderId());
        values.put(REMINDER_COLUMN_TITLE, reminder.getReminderTitle());

        // updating row
        return db.update(TABLE_REMINDER, values, REMINDER_COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getReminderId())});
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDER, REMINDER_COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getReminderId())});
        db.close();
    }
}
