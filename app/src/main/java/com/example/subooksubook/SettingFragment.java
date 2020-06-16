package com.example.subooksubook;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


public class SettingFragment extends PreferenceFragmentCompat {
    SharedPreferences pref;
    private boolean isAlarm;  // 알람 버튼 true, false 받기

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pref.registerOnSharedPreferenceChangeListener(pre_listener);


    }

    SharedPreferences.OnSharedPreferenceChangeListener pre_listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("setalarm")){
                isAlarm = pref.getBoolean("setalarm", false);
                Toast.makeText(getActivity(), "알람 설정: " + isAlarm, Toast.LENGTH_SHORT).show();
                setAlarm(isAlarm);
            }
        }
    };

    @SuppressLint("WrongConstant")
    public void setAlarm(boolean set) {
        if(set == true){

        }
    }
}
