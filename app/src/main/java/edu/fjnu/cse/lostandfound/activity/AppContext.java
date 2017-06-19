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
    private boolean nfcEnabled;
    private boolean isNFC_support = false;
    //public static String mDomain = "192.168.198.190";
    public static String mDomain = "l.city";
    public String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public boolean isNfcEnabled() {
        return nfcEnabled;
    }

    public void setNfcEnabled(boolean nfcEnabled) {
        this.nfcEnabled = nfcEnabled;
    }

    public boolean isNFC_support() {
        return isNFC_support;
    }

    public void setNFC_support(boolean NFC_support) {
        isNFC_support = NFC_support;
    }

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
