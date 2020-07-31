package linrevbossnoti.evey.hol.es.linrevbossnoti;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by evey on 17. 3. 30.
 */

public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_SHOW_NOTIFICATION = "linrevbossnoti.evey.hol.es.linrevbossnoti.notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_SHOW_NOTIFICATION)) {
                boolean isNotiAvailable = false;
                String indexStr = intent.getExtras().getString("index", "");
                int index = Integer.parseInt(indexStr);
                String sound = intent.getExtras().getString("sound", "y");
                String title = "레볼도우미";
                String ticker = "";
                String content = "";

                switch (index) {
                    case AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_2:
                    case AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_6:
                    case AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_10:
                    case AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_14:
                    case AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_18:
                    case AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_22:
                        isNotiAvailable = true;
                        content = "필드 보스 출현 10분 전입니다";
                        break;
                    case AlarmFactory.NOTI_TYPE_WORLD_BOSS_TIME:
                        isNotiAvailable = true;
                        content = "기요틴 출현 10분 전입니다";
                        break;
                    case AlarmFactory.NOTI_TYPE_TODAY_CORE_MOB:
                        isNotiAvailable = true;
                        content = "오늘 사냥하실 필드 코어 몹은 " + getFieldMonsterStr(context) + " 입니다";
                        break;
                    case AlarmFactory.NOTI_TYPE_TODAY_ELITE_MOB:
                        isNotiAvailable = true;
                        content = "오늘 사냥하실 정던 코어 몹은 " + getEliteMonsterStr(context) + " 입니다";
                        break;
                    default:
                        isNotiAvailable = false;
                        break;
                }
                if (isNotiAvailable) {
                    ticker = content;
                    showNotification(context, index, sound, title, ticker, content);
                }

            } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

                new AlarmFactory(context).setAlarmOnReboot();

            }
        }
    }

    public String getFieldMonsterStr(Context context) {
        int userLevel = Preferences.getInstance(context).getKeyPlayerLevel();
        //몹과 3렙차까지
        for (int i = 0; i < Const.fieldCoreMonster.length; i++) {
            int monsterLevel = Integer.parseInt(Const.fieldCoreMonster[i][0]);
            if (userLevel - 3 <= monsterLevel) {
                return Const.fieldCoreMonster[i][1];
            }
        }

        //플레이어레벨이 몹레벨 이상인 경우
//        for (int i = 0; i < Const.fieldCoreMonster.length; i++) {
//            int monsterLevel = Integer.parseInt(Const.fieldCoreMonster[i][0]);
//            if (i > 0 && userLevel <= monsterLevel) {
//                return Const.fieldCoreMonster[i - 1][1];
//            }
//        }
        return Const.fieldCoreMonster[0][1];
    }

    public String getEliteMonsterStr(Context context) {
        int userLevel = Preferences.getInstance(context).getKeyPlayerLevel();
        for (int i = 0; i < Const.eliteCoreMonster.length; i++) {
            int monsterLevel = Integer.parseInt(Const.eliteCoreMonster[i][0]);
            if (userLevel - 3 <= monsterLevel) {
                return Const.eliteCoreMonster[i][1];
            }
        }
        return Const.eliteCoreMonster[0][1];
    }

    private void showNotification(Context context, int index, String sound, String title, String ticker, String content) {
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setTicker(ticker).setWhen(System.currentTimeMillis())
                .setContentTitle(title).setContentText(content)
                .setContentIntent(pendingIntent).setAutoCancel(true).setPriority(Notification.PRIORITY_HIGH);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder);
        style.setBigContentTitle(title);
        style.bigText(content);
        builder.setStyle(style);
        if (index == AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_2
                || index == AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_6
                || index == AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_10
                || index == AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_14
                || index == AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_18
                || index == AlarmFactory.NOTI_TYPE_FIELD_BOSS_TIME_22
                ) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000});
        } else {
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        }
        notificationmanager.notify(index, builder.build());

    }

}