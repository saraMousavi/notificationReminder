package ir.sara.mousavi.notificationreminder.utils;

import java.util.Calendar;

import ir.sara.mousavi.notificationreminder.utils.calender.PersianCalendar;

public class Init {
    /**
     * calculate different between selected time from now in milli second
     * @param selectedCalender
     * @return
     */
    public static long getDifferentBetweenToCalenderInMilliSecond(Calendar selectedCalender) {
        //current
        Calendar calendar = Calendar.getInstance();
        int cHour = calendar.get(Calendar.HOUR_OF_DAY);
        int cMinute = calendar.get(Calendar.MINUTE);
        int cSecond = calendar.get(Calendar.SECOND);
        int cMillisecond = calendar.get(Calendar.MILLISECOND);
        PersianCalendar persianCalendar = new PersianCalendar();
        int cMonth = persianCalendar.getPersianMonth() + 1;
        int cYear = persianCalendar.getPersianYear();
        int cDay = persianCalendar.getPersianDay();
        //selected
        int sHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
        int sMinute = selectedCalender.get(Calendar.MINUTE);
        int sSecond = selectedCalender.get(Calendar.SECOND);
        int sMillisecond = selectedCalender.get(Calendar.MILLISECOND);
        int sMonth = selectedCalender.get(Calendar.MONTH) + 1;
        int sDay = selectedCalender.get(Calendar.DATE);
        int sYear = selectedCalender.get(Calendar.YEAR);
        long diffTime = 0;
        if (cYear == sYear) {
            if (cMonth == sMonth) {
                if (cDay == sDay) {
                    if (cHour == sHour) {
                        //selected time is for next less than an hour
                        if (cMinute < sMinute) {
                            diffTime = (sMinute - cMinute) * 60 * 1000 - cSecond * 1000 - cMillisecond;
                        }
                    } else {
                        //selected time is for next less than an day
                        if (cHour < sHour) {
                            int diffHour = sHour - cHour;
                            int diffMinute = sMinute - cMinute;
                            if (diffMinute < 0) {
                                diffHour = diffHour - 1;
                                diffMinute = (60 - cMinute) + sMinute;
                            }
                            diffTime = diffHour * 60 * 60 * 1000 + (diffMinute) * 60 * 1000 - cSecond * 1000 - cMillisecond;
                        }
                    }
                } else {
                    //selected time is for next less than an month
                    if (cDay < sDay) {
                        int diffDay = sDay - cDay;
                        int diffHour = sHour - cHour;
                        if (diffHour < 0) {
                            diffDay = diffDay - 1;
                            diffHour = (24 - cHour) + sHour;
                        }
                        int diffMinute = sMinute - cMinute;
                        if (diffMinute < 0) {
                            diffHour = diffHour - 1;
                            diffMinute = (60 - cMinute) + sMinute;
                        }
                        diffTime = diffDay * 24 * 60 * 60 * 1000 + diffHour * 60 * 60 * 1000 + (diffMinute) * 60 * 1000 - cSecond * 1000 - cMillisecond;
                    }
                }
            } else {
                //selected time is for next less than an year
                if (cMonth < sMonth) {
                    int diffMonth = sMonth - cMonth;
                    int diffDay = sDay - cDay;
                    if (diffDay < 0) {
                        diffMonth = diffMonth - 1;
                        diffDay = (30 - cDay) + sDay;
                    }
                    int diffHour = sHour - cHour;
                    if (diffHour < 0) {
                        diffDay = diffDay - 1;
                        diffHour = (24 - cHour) + sHour;
                    }
                    int diffMinute = sMinute - cMinute;
                    if (diffMinute < 0) {
                        diffHour = diffHour - 1;
                        diffMinute = (60 - cMinute) + sMinute;
                    }
                    diffTime = diffMonth * 30 * 24 * 60 * 60 * 1000 + diffDay * 24 * 60 * 60 * 1000
                            + diffHour * 60 * 60 * 1000 +
                            (diffMinute) * 60 * 1000 - cSecond * 1000 - cMillisecond;
                }
            }
        } else {
            //selected time is for next more than an year
            if (cYear < sYear) {
                int diffYear = sYear - cYear;
                int diffMonth = sMonth - cMonth;
                if (diffMonth < 0) {
                    diffYear = diffYear - 1;
                    diffMonth = (12 - cMonth) + sMonth;
                }
                int diffDay = sDay - cDay;
                if (diffDay < 0) {
                    diffMonth = diffMonth - 1;
                    diffDay = (30 - cDay) + sDay;
                }
                int diffHour = sHour - cHour;
                if (diffHour < 0) {
                    diffDay = diffDay - 1;
                    diffHour = (24 - cHour) + sHour;
                }
                int diffMinute = sMinute - cMinute;
                if (diffMinute < 0) {
                    diffHour = diffHour - 1;
                    diffMinute = (60 - cMinute) + sMinute;
                }
                diffTime = diffMonth * 365 * 24 * 60 * 60 * 1000 + diffMonth * 30 * 24 * 60 * 60 * 1000 + diffDay * 24 * 60 * 60 * 1000
                        + diffHour * 60 * 60 * 1000 +
                        (diffMinute) * 60 * 1000 - cSecond * 1000 - cMillisecond;
            }
        }
        return diffTime;
    }

}
