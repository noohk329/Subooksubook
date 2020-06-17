package com.example.subooksubook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingFragment extends PreferenceFragmentCompat {
    SharedPreferences pref;
    private boolean isAlarm;  // 알람 버튼 true, false 받기
    private boolean islogin;   // login or out
    String iD_authen;

    SettingFragment(String id)
    {
        this.iD_authen = iD_authen;
        Log.d("SettingFragment", "id :"+ this.iD_authen);
    }

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
            else if (key.equals("login_onoff")){
                Toast.makeText(getActivity(), "로그인 설정 ", Toast.LENGTH_SHORT).show();

//                islogin = pref.getBoolean("setalarm", false);
            }
        }
    };

    @SuppressLint("WrongConstant")
    public void setAlarm(boolean set) {
        if(set == true){

        }
    }
}
