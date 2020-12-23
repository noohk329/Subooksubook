package com.example.subooksubook;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.subooksubook.BookshelfF.MyBookShelf;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private String TAG = "*************MainActivity*************";

    private BottomNavigationView bottomNavigationView;
    private MyBookShelf mybookshelf;  // fragment 1
    private CalendarViewWithNotesActivity calenderview;  //fragment 2
    private StatisticForm statisticForm;    //fragment 3
    private SettingFragment settingFragment;    //fragment 4

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String iD_authen = intent.getStringExtra("user_id");
        user = (FirebaseUser)intent.getParcelableExtra("user");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // create fragment
        Log.d("MainActivity", "id :"+ iD_authen);
        mybookshelf = new MyBookShelf(iD_authen);


        //제일 처음 띄워줄 뷰를 세팅해줍니다. commit();까지 해줘야 합니다.
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, mybookshelf).commitAllowingStateLoss();

        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, mybookshelf).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.diary: {
                        calenderview = new CalendarViewWithNotesActivity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, calenderview).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.statistics: {
                        statisticForm = new StatisticForm();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, statisticForm).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.setting: {
                        settingFragment = new SettingFragment(iD_authen);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, settingFragment).commitAllowingStateLoss();
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }
}


