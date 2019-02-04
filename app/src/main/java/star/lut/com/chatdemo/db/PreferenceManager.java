package star.lut.com.chatdemo.db;

import android.content.Context;
import android.content.SharedPreferences;

import star.lut.com.chatdemo.constants.PreferenceConstants;

public class PreferenceManager {
    private Context mContext;

    public PreferenceManager(Context context) {
        this.mContext = context;
    }

    //get shared pref
    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void clearPref(){
        getPreferences().edit().clear().apply();
    }

    public void setUserLoggedIn(boolean isLoggedIn){
        getPreferences().edit().putBoolean(
                PreferenceConstants.USER_LOGGED_IN, isLoggedIn
        ).apply();
    }

    public boolean getUserLoggedIn(){
        return getPreferences().getBoolean(
                PreferenceConstants.USER_LOGGED_IN, false
        );
    }

    public void setUserID(String userID){
        getPreferences().edit().putString(
                PreferenceConstants.USER_ID, userID
        ).apply();
    }

    public String getUSerID(){
        return getPreferences().getString(
                PreferenceConstants.USER_ID, "-1"
        );
    }

    public void setPassword(String password){
        getPreferences().edit().putString(
                PreferenceConstants.USER_PASSWORD, password
        ).apply();
    }

    public String getPassword(){
        return getPreferences().getString(
                PreferenceConstants.USER_PASSWORD, "-1"
        );
    }

    public void setUserPic(String pic){
        getPreferences().edit().putString(
                PreferenceConstants.USER_PIC, pic
        ).apply();
    }

    public String getUserPic(){
        return getPreferences().getString(
                PreferenceConstants.USER_PIC, "-1"
        );
    }

    public void setPosition(String position){
        getPreferences().edit().putString(
                PreferenceConstants.USER_POSITION, position
        ).apply();
    }

    public String getUserPosition(){
        return getPreferences().getString(
                PreferenceConstants.USER_POSITION, "-1"
        );
    }

    public void setUserName(String name){
        getPreferences().edit().putString(
                PreferenceConstants.USER_NAME, name
        ).apply();
    }

    public String getUserName(){
        return getPreferences().getString(
                PreferenceConstants.USER_NAME, "-1"
        );
    }

    public void setFullName(String name){
        getPreferences().edit().putString(
                PreferenceConstants.USER_FULL_NAME, name
        ).apply();
    }

    public String getFullName(){
        return getPreferences().getString(
                PreferenceConstants.USER_FULL_NAME, "-1"
        );
    }

    public void setToken(String token){
        getPreferences().edit().putString(
                PreferenceConstants.FIREBASE_TOKEN, token
        ).apply();
    }

    public String getToken(){
        return getPreferences().getString(
                PreferenceConstants.FIREBASE_TOKEN, "-1"
        );
    }

    public void setNotificationSettings(int time){
        getPreferences().edit().putInt(
                PreferenceConstants.NOTIFICATION_SETTINGS, time
        ).apply();
    }

    public int getNotificationSettings(){
        return getPreferences().getInt(
                PreferenceConstants.NOTIFICATION_SETTINGS, 0
        );
    }

    public void setNotificationTimeFrom(String time){
        getPreferences().edit().putString(
                PreferenceConstants.NOTIFICATION_TIME_FROM, time
        ).apply();
    }

    public String getNotificationTimeFrom(){
        return getPreferences().getString(
                PreferenceConstants.NOTIFICATION_TIME_FROM, "-1"
        );
    }

    public void setNotificationTimeTo(String time){
        getPreferences().edit().putString(
                PreferenceConstants.NOTIFICATION_TIME_TO, time
        ).apply();
    }

    public String getNotificationTimeTo(){
        return getPreferences().getString(
                PreferenceConstants.NOTIFICATION_TIME_TO, "-1"
        );
    }

    public void setThreadID(String threadID){
        getPreferences().edit().putString(
                PreferenceConstants.THREAD_ID, threadID
        ).apply();
    }

    public String getThreadID(){
        return getPreferences().getString(
                PreferenceConstants.THREAD_ID, "-1"
        );
    }
}