package com.example.subooksubook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */

public class DiaryCal extends Fragment {

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference conditionRef = rootRefer.child("diary");
    // private DatabaseReference diaryDBRef = FirebaseDatabase.getInstance().getReference("diary");

    private ViewGroup viewGroup;
    private CalendarView calendarView;

    private String selectDay;

    private EditText diaryText;
    private Button saveButton;
    private TextView savedDiary;

    public DiaryCal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.diary_main, container, false);

        calendarView=(CalendarView) viewGroup.findViewById(R.id.calender);
        saveButton = (Button) viewGroup.findViewById(R.id.saveDiary);
        diaryText = (EditText)viewGroup.findViewById(R.id.diaryText);
        savedDiary = (TextView)viewGroup.findViewById(R.id.diaryData);

        Calendar calendar = Calendar.getInstance();
        selectDay = Integer.toString(calendar.get(Calendar.YEAR))+"-"+Integer.toString(calendar.get(Calendar.MONTH)+1)+"-"+Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        // 선택한  날짜 변수에 받아오기
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                selectDay=year+"-"+(month+1)+"-"+dayOfMonth;
                // 선택한 날짜에 데이터가 저장되어 있으면 저장된 text 출력
                // firebase 검색

                conditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            // diary child 내에 있는 data 탐색
                            String saveDate = postSnapshot.getKey();
                            // System.out.println(saveDate);

                            if(saveDate.equals(selectDay)){
                                String saveDiaryText = postSnapshot.child("text").getValue(String.class);
                                // System.out.println(saveDiaryText);
                                savedDiary.setText(saveDiaryText);
                                break;

                            }else{
                                savedDiary.setText("해당 날짜에 메모가 없습니다.");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // 없으면 입력 칸이 뜨도록... 그러면 fragment 를 사용해야 할까?
                // 추후 개발 파트: 책을 추가한 날짜에 받아와서 띄워야 함.. 구조를 나중에 추가하는 걸로
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // diary - date "2020-03-21" - diaryText
                conditionRef.child(selectDay).child("text").setValue(diaryText.getText().toString());
                diaryText.setText("");

                conditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            // diary child 내에 있는 data 탐색
                            String saveDate = postSnapshot.getKey();
                            // System.out.println(saveDate);

                            if (saveDate.equals(selectDay)) {
                                String saveDiaryText = postSnapshot.child("text").getValue(String.class);
                                // System.out.println(saveDiaryText);
                                savedDiary.setText(saveDiaryText);
                                break;

                            } else {
                                savedDiary.setText("해당 날짜에 메모가 없습니다.");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }
        });
        return viewGroup;
    }

}

// class 만들기 (diary - 날짜_key / text 저장하는,.. 또 뭐가 필요할랑가)