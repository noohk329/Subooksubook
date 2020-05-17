package com.example.subooksubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    MyBookShelf mybookshelf;  // fragment 1
    DiaryCal diaryCal;  //fragment 2
    StatisticForm statisticForm;    //fragment 3
    SettingForm settingForm;    //fragment 4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // create fragment
        mybookshelf = new MyBookShelf();
        diaryCal = new DiaryCal();
        statisticForm = new StatisticForm();
        settingForm = new SettingForm();

        //제일 처음 띄워줄 뷰를 세팅해줍니다. commit();까지 해줘야 합니다.
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,mybookshelf).commitAllowingStateLoss();

        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.home:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,mybookshelf).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.diary:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,diaryCal).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.statistics:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,statisticForm).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.setting:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,settingForm).commitAllowingStateLoss();
                        return true;
                    }
                    default: return false;
                }
            }
        });
    }
    // 책추가 버튼 클릭시
    public void onClickHandler(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("책 추가 방법을 선택하세요.");    // main title
        // array string_add_newBook은 string에 존재
        builder.setItems(R.array.string_add_newBook, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] items = getResources().getStringArray(R.array.string_add_newBook);
                // which -> 0~2
                if (which == 0){
                    Intent intent = new Intent(getApplicationContext(), SearchCode.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_LONG).show();
                }
                else if (which == 1) {
                    Intent intent = new Intent(getApplicationContext(), SearchName.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_LONG).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
