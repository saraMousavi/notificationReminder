package ir.sara.mousavi.notificationreminder.utils.enums;

public enum ReminderType {
    HOURLY(0),

    DAILY(1),

    WEEKLY(2),

    MONTHLY(3),

    YEARLY(4);

    private int value;

    ReminderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
