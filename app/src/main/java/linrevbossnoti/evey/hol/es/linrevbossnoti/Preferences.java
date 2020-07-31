
package linrevbossnoti.evey.hol.es.linrevbossnoti;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private static Preferences instance;
    private static SharedPreferences preferences;

    private static final String KEY_PLAYER_LEVEL = "key_player_level";
    private static final String KEY_NOTI_TYPE_NIGHT_ALARM_ONOFF = "key_noti_type_night_alarm_onoff";

    private static Context mContext;

    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context.getApplicationContext());
        }
        mContext = context;
        return instance;
    }

    private Preferences(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public void setKeyPlayerLevel(int num) {
        preferences.edit().putInt(KEY_PLAYER_LEVEL, num).commit();
    }

    public int getKeyPlayerLevel() {
        return preferences.getInt(KEY_PLAYER_LEVEL, 1);
    }


    public void setKeyNotiTypeNightAlarmOnoff(boolean is) {
        preferences.edit().putBoolean(KEY_NOTI_TYPE_NIGHT_ALARM_ONOFF, is).commit();
    }

    public boolean getKeyNotiTypeNightAlarmOnoff() {
        return preferences.getBoolean(KEY_NOTI_TYPE_NIGHT_ALARM_ONOFF, true);
    }
}
