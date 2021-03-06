package com.example.subooksubook;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.subooksubook.BookshelfF.MyBookShelf;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    MyBookShelf mybookshelf;  // fragment 1
    CalendarViewWithNotesActivity calenderview;  //fragment 2
    StatisticForm statisticForm;    //fragment 3
    SettingFragment settingFragment;    //fragment 4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String iD_authen = intent.getStringExtra("id");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // create fragment
        Log.d("MainActivity", "id :"+ iD_authen);
        mybookshelf = new MyBookShelf(iD_authen);
        calenderview = new CalendarViewWithNotesActivity();
        statisticForm = new StatisticForm();
        settingFragment = new SettingFragment(iD_authen);

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, calenderview).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.statistics: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, statisticForm).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.setting: {
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


