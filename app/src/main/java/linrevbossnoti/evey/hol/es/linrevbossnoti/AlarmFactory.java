package linrevbossnoti.evey.hol.es.linrevbossnoti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by evey on 17. 4. 2.
 */

public class AlarmFactory {
    private Context context;
    AlarmManager am;

    public static final int NOTI_TYPE_FIELD_BOSS_TIME_2 = 1;
    public static final int NOTI_TYPE_FIELD_BOSS_TIME_6 = 2;
    public static final int NOTI_TYPE_FIELD_BOSS_TIME_10 = 3;
    public static final int NOTI_TYPE_FIELD_BOSS_TIME_14 = 4;
    public static final int NOTI_TYPE_FIELD_BOSS_TIME_18 = 5;
    public static final int NOTI_TYPE_FIELD_BOSS_TIME_22 = 6;
    public static final int NOTI_TYPE_WORLD_BOSS_TIME = 7;
    public static final int NOTI_TYPE_TODAY_CORE_MOB = 8;
    public static final int NOTI_TYPE_TODAY_ELITE_MOB = 9;

    public static final int REPEAT_DAY = 1;

    public AlarmFactory(Context context) {
        this.context = context;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarmOnReboot() {
        setAlarm(NOTI_TYPE_FIELD_BOSS_TIME_2);
        setAlarm(NOTI_TYPE_FIELD_BOSS_TIME_6);
        setAlarm(NOTI_TYPE_FIELD_BOSS_TIME_10);
        setAlarm(NOTI_TYPE_FIELD_BOSS_TIME_14);
        setAlarm(NOTI_TYPE_FIELD_BOSS_TIME_18);
        setAlarm(NOTI_TYPE_FIELD_BOSS_TIME_22);
        setAlarm(NOTI_TYPE_WORLD_BOSS_TIME);
        setAlarm(NOTI_TYPE_TODAY_CORE_MOB);
        setAlarm(NOTI_TYPE_TODAY_ELITE_MOB);
    }

    public void setAlarm(int type) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_SHOW_NOTIFICATION);
        intent.putExtra("index", Integer.toString(type));
        PendingIntent sender = PendingIntent.getBroadcast(context, type, intent, 0);
        Calendar calendar = Calendar.getInstance();
        switch (type) {
            case NOTI_TYPE_FIELD_BOSS_TIME_2:
                setEveryDayAlarm(sender, calendar, 1, 50, false);
                break;
            case NOTI_TYPE_FIELD_BOSS_TIME_6:
                setEveryDayAlarm(sender, calendar, 5, 50, false);
                break;
            case NOTI_TYPE_FIELD_BOSS_TIME_10:
                setEveryDayAlarm(sender, calendar, 9, 50, false);
                break;
            case NOTI_TYPE_FIELD_BOSS_TIME_14:
                setEveryDayAlarm(sender, calendar, 13, 50, false);
                break;
            case NOTI_TYPE_FIELD_BOSS_TIME_18:
                setEveryDayAlarm(sender, calendar, 17, 50, false);
                break;
            case NOTI_TYPE_FIELD_BOSS_TIME_22:
                setEveryDayAlarm(sender, calendar, 21, 50, false);
                break;
            case NOTI_TYPE_WORLD_BOSS_TIME:
                setEveryDayAlarm(sender, calendar, 22, 50, false);
                break;
            case NOTI_TYPE_TODAY_CORE_MOB:
                setEveryDayAlarm(sender, calendar, 7, 0, true);
                break;
            case NOTI_TYPE_TODAY_ELITE_MOB:
                setEveryDayAlarm(sender, calendar, 7, 0, true);
                break;
        }
    }

    private void setEveryDayAlarm(PendingIntent sender, Calendar calendar, int hour, int minute, boolean isShowTodayNoti) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, minute, 0);
        if (!isAfterFromNow(hour, minute) && !isShowTodayNoti) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.v("lbn", "lbn setEveryDayAlarm " + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DATE) + " " + hour + " " + minute);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, sender);
    }

    private void setOnceAlarm(PendingIntent sender, Calendar calendar) {
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    private boolean isAfterFromNow(int hour, int min) {
        Calendar nowCal = Calendar.getInstance();
        int nowHour = nowCal.get(Calendar.HOUR_OF_DAY);
        int nowMin = nowCal.get(Calendar.MINUTE);
        if (hour == nowHour) {
            return min > nowMin;
        } else {
            return hour > nowHour;
        }
    }

    public void cancelAlarm(int index, int repeatType) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_SHOW_NOTIFICATION);
        switch (repeatType) {
            case REPEAT_DAY:
                cancelRepeat(intent, index);
                break;
        }
    }

    private void cancelRepeat(Intent intent, int alarmIndex) {
        PendingIntent senderCancel = PendingIntent.getBroadcast(context, alarmIndex, intent, 0);
        am.cancel(senderCancel);
    }

}