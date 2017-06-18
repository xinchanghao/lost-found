package edu.fjnu.cse.lostandfound.activity;

import android.app.Application;
import android.preference.PreferenceManager;

import com.iflytek.cloud.SpeechUtility;

import edu.fjnu.cse.lostandfound.entities.LostItem;

/**
 * 全局Application
 * Created by zspmh on 2017-6-10.
 */

public class AppContext extends Application {
    private boolean isLogined = false;
    private String SID;
    private String Name;
    private LostItem currentItem;
    //public static String mDomain = "192.168.198.190";
    public static String mDomain = "l.city";

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this, "appid=59227e71" );
        isLogined = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isLogined", false);
        SID = PreferenceManager.getDefaultSharedPreferences(this).getString("SID", "");
        Name = PreferenceManager.getDefaultSharedPreferences(this).getString("Name", "");
    }

    public LostItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(LostItem currentItem) {
        this.currentItem = currentItem;
    }

    public void setName(String name) {
        Name = name;
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Name", name).apply();
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isLogined", logined).apply();
    }

    public void setSID(String SID) {
        this.SID = SID;
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("SID", SID).apply();
    }


    public String getSID() {
        return SID;
    }

    public String getName() {
        return Name;
    }

    public boolean isLogined() {
        return isLogined;
    }
}
