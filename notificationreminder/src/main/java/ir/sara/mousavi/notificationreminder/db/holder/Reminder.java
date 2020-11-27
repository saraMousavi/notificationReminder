package ir.sara.mousavi.notificationreminder.db.holder;

public class Reminder {
    private int reminderId;
    private String reminderTitle;
    private String reminderTime;
    private String reminderIntervalType;
    private int reminderIntervalTime;

    public Reminder() {
    }

    public Reminder(String reminderTitle, String reminderTime, String reminderIntervalType, int reminderIntervalTime) {
        this.reminderTitle = reminderTitle;
        this.reminderTime = reminderTime;
        this.reminderIntervalType = reminderIntervalType;
        this.reminderIntervalTime = reminderIntervalTime;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderIntervalType() {
        return reminderIntervalType;
    }

    public void setReminderIntervalType(String reminderIntervalType) {
        this.reminderIntervalType = reminderIntervalType;
    }

    public int getReminderIntervalTime() {
        return reminderIntervalTime;
    }

    public void setReminderIntervalTime(int reminderIntervalTime) {
        this.reminderIntervalTime = reminderIntervalTime;
    }
}
