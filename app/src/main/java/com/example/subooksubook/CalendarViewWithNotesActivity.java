package com.example.subooksubook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.calenderviewlib.CalendarView;
import com.example.subooksubook.data.Event;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarViewWithNotesActivity extends Fragment {
    private ViewGroup viewGroup;

    private String[] mShortMonths;
    private CalendarView mCalendarView;

    private List<Event> mEventList = new ArrayList<>();

    String iD_authen;

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef;
    public CalendarViewWithNotesActivity() {   }
    public CalendarViewWithNotesActivity(String iD_authen) {
        // Required empty public constructor
        this.iD_authen = iD_authen;
        Log.d("MyBookShelf", "id :"+ this.iD_authen);
        conditionRef = rootRefer.child(iD_authen).child("mybookshelf");
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, CalendarViewWithNotesActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (iD_authen == null) {
            Intent intent = getActivity().getIntent();
            iD_authen = intent.getStringExtra("id");
            Log.d("MyBookShelf", "id :" + this.iD_authen);
            conditionRef = rootRefer.child(iD_authen).child("mybookshelf");
        }

        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_calendar_view_with_notes, container, false);
        Toolbar toolbar = viewGroup.findViewById(R.id.toolbar);
        super.getActivity().getMenuInflater().inflate(R.menu.menu_toolbar_calendar_view, toolbar.getMenu());
        getActivity().setActionBar(toolbar);

        setHasOptionsMenu(true);



        initializeEvent();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeUI();
            }
        }, 200);


        return viewGroup;
    }


    private void initializeEvent(){

        mEventList.clear();

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String title = postSnapshot.child("title").getValue(String.class);
                    String color = postSnapshot.child("color").getValue(String.class);
                    String author = postSnapshot.child("author").getValue(String.class);

                    int count =0;
                    for (DataSnapshot recorddata: postSnapshot.child("record").getChildren()){

                        String id = recorddata.getKey();
                        Log.d("recordid", id);

                        String[] getDate = id.split(" ");
                        String[] calArr = getDate[0].split("-");

                        Calendar cal = Calendar.getInstance();
                        cal.set(Integer.parseInt(calArr[0]), Integer.parseInt(calArr[1])-1, Integer.parseInt(calArr[2]));

                        String checkDiary = recorddata.child("isdiary").getValue(String.class);
                        String readPage = recorddata.child("perPage").getValue(String.class);
                        String diaryText = "";

                        Boolean isDiary = null;

                        if(checkDiary.equals("0")){ // no diary
                            isDiary = false;
                            diaryText = "";
                        }else if(checkDiary.equals("1")){
                            isDiary = true;
                            diaryText = recorddata.child("diarytext").getValue(String.class);
                        }

                        int getcolor;
                        try{
                            getcolor = Integer.parseInt(color);
                        }catch (NumberFormatException e){
                            getcolor = 0;
                        }

                        Event mGetEvent = new Event(
                                id,
                                title,
                                cal,
                                getcolor,
                                isDiary,
                                diaryText,
                                readPage
                        );
                        mGetEvent.setmAuthor(author);
                        mEventList.add(mGetEvent);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initializeUI() {

        mShortMonths = new DateFormatSymbols().getShortMonths();

        mCalendarView = getActivity().findViewById(R.id.calendarView);
        mCalendarView.setOnMonthChangedListener((int month, int year) -> {
            if (getActivity().getActionBar() != null) {
                getActivity().getActionBar().setTitle(mShortMonths[month]);
                getActivity().getActionBar().setSubtitle(Integer.toString(year));
            }
        });

        // 날짜 클릭 시 dialog
        mCalendarView.setOnItemClickedListener((calendarObjects, previousDate, selectedDate) -> {
            if (calendarObjects.size() != 0) {

                Intent intent = new Intent(getActivity(), DiaryListActivity.class);

                String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(selectedDate.getTime());
                intent.putExtra("selectdate", date);
                intent.putExtra("id", iD_authen);

                startActivity(intent);

            }
        });

        for (Event e : mEventList) {
            mCalendarView.addCalendarObject(parseCalendarObject(e));
        }

        if (getActivity().getActionBar() != null) {
            int month = mCalendarView.getCurrentDate().get(Calendar.MONTH);
            int year = mCalendarView.getCurrentDate().get(Calendar.YEAR);
            getActivity().getActionBar().setTitle(mShortMonths[month]);
            getActivity().getActionBar().setSubtitle(Integer.toString(year));
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_calendar_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // today 클릭시 오늘 날짜로 이동.
        switch (item.getItemId()) {
            case R.id.action_today: {
                mCalendarView.setSelectedDate(Calendar.getInstance());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private static CalendarView.CalendarObject parseCalendarObject(Event event) {
        return new CalendarView.CalendarObject(
                event.getID(),
                event.getDate(),
                event.getColor(),
                event.getisDiary() ? Color.BLUE : Color.RED);
    }

}
