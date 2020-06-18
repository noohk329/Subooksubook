package com.example.subooksubook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.example.subooksubook.Login.Profile;


public class SettingFragment extends PreferenceFragmentCompat {
    SharedPreferences pref;
    private boolean isAlarm;  // 알람 버튼 true, false 받기
    private boolean islogin;   // login or out
    String iD_authen, time_label;

    private PreferenceScreen screan;

    SettingFragment(String id)
    {
        this.iD_authen = iD_authen;
        Log.d("SettingFragment", "id :"+ this.iD_authen);
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference instanceof EditTextPreference) {
                preference.setSummary(time_label);
            }
            return true;
        }

    };

    public void setOnPreferenceChange(Preference mPreference)
    {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        onPreferenceChangeListener.onPreferenceChange(mPreference,
                PreferenceManager.getDefaultSharedPreferences(mPreference.getContext()).getString(mPreference.getKey(), ""));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);
        setOnPreferenceChange(findPreference("setalarm_time"));
//        setOnPreferenceChange(findPreference("userNameOpen"));
//        setOnPreferenceChange(findPreference("autoUpdate_ringtone"));

        screan = getPreferenceScreen();

        if (time_label == null)  {}
        else
            time_label= getArguments().getString("time_lap");

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pref.registerOnSharedPreferenceChangeListener(pre_listener);


    }

    SharedPreferences.OnSharedPreferenceChangeListener pre_listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("setalarm")){
                isAlarm = pref.getBoolean("setalarm", false);
                Toast.makeText(getActivity(), "알람 설정: " + isAlarm, Toast.LENGTH_SHORT).show();
            }
            else if (key.equals("login_onoff")){
                islogin = pref.getBoolean("login_onoff", false);
                Toast.makeText(getActivity(), "로그인 설정 " + islogin, Toast.LENGTH_SHORT).show();
                setLogin(islogin);
            }
            else if (key.equals("setalarm_time")){
                Toast.makeText(getActivity(), "시간 설정 ", Toast.LENGTH_SHORT).show();
                setAlarm(isAlarm);
            }
        }
    };

    @SuppressLint("WrongConstant")
    public void setAlarm(boolean set) {
        if(set == true){
            Intent intent = new Intent(getActivity().getApplicationContext(), Timepicker.class);
            intent.putExtra("id", iD_authen);
            startActivity(intent);
            getActivity().finish();
        }
    }
    @SuppressLint("WrongConstant")
    public void setLogin(boolean set) {

        Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
        intent.putExtra("id", iD_authen);
        startActivity(intent);
        getActivity().finish();

    }
}
