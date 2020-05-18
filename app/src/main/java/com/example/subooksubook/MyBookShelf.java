package com.example.subooksubook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MyBookShelf extends Fragment {

    ViewGroup viewGroup;
    ListView listView;  // 뷰 객체
    List<BookViewItem>bookList = new ArrayList<>(); // 정보 담을 객체

    public static Context context_mybookshelf; //context 변수선언
    public BookViewAdapter adapter;


    //데이터베이스
    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference conditionRef = rootRefer.child("mybookshelf");

    public MyBookShelf() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.mybookshelf, container, false);
        adapter = new BookViewAdapter();
        context_mybookshelf = getActivity();     //oncreate에서 this 할당
        listView = (ListView)viewGroup.findViewById(R.id.myBookList);
        listView.setAdapter(adapter);

        conditionRef.addValueEventListener(new ValueEventListener() {
            // 실시간 연동 시스템, 한번 추가되면 계속 추가되도록 시스템 설계가 되어있음
            // 그래서 조건을 달아야 중복 생성 방지
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 부모가 User 인데 부모 그대로 가져오면 User 각각의 데이터 이니까 자식으로 가져와서 담아줌
                int same = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    for(int i=0; i<adapter.getCount(); i++) {
                        Log.d("MyBookShelf", "gettitle :"+ adapter.getTitle(i));
                        if (adapter.getTitle(i) == postSnapshot.child("title").getValue(String.class)) {
                            same = 1;   // 만약 DB 아이템이 이미 존재하는 아이템이라면 1로 표기
                            break;
                        }
                    }
                    if(same ==0)    // 리스트뷰에 찾았을때 똑같은 게 없다면 추가하자 same =0
                    {
                        String saveDate = postSnapshot.getKey();
                        Log.i("id", postSnapshot.getKey());
                        // System.out.println(saveDate);
                        BookViewItem singleItem = postSnapshot.getValue(BookViewItem.class);

                        singleItem.setTitle(postSnapshot.child("title").getValue(String.class));
                        singleItem.setAuthor(postSnapshot.child("author").getValue(String.class));
                        singleItem.setPublisher(postSnapshot.child("publisher").getValue(String.class));
                        String img_notbmp = postSnapshot.child("image").getValue(String.class);
                        singleItem.setBookImage(StringToBitmap(img_notbmp));

                        /* 데이터그릇 bookItemList에 담음 */
                        bookList.add(singleItem);
                        adapter.addItem(singleItem.getTitle(), singleItem.getAuthor(), singleItem.getPublisher(), singleItem.getBookImage());
                    }
                    same = 0;
                }
                //어뎁터한테 데이터 넣어줬다고 알려줌 (안하면 화면에 안나온다)
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("MyBookShelf", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // 책추가 버튼 클릭시
        Button addBook = (Button)viewGroup.findViewById(R.id.btn_addbook);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("책 추가 방법을 선택하세요.");    // main title
                // array string_add_newBook은 string에 존재
                builder.setItems(R.array.string_add_newBook, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] items = getResources().getStringArray(R.array.string_add_newBook);
                        // which -> 0~2
                        if (which == 0){
                            Intent intent = new Intent(getActivity().getApplicationContext(), SearchCode.class);
                            startActivity(intent);
                            Toast.makeText(getActivity().getApplicationContext(), items[which], Toast.LENGTH_LONG).show();
                        }
                        else if (which == 1) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), SearchName.class);
                            startActivity(intent);
                            Toast.makeText(getActivity().getApplicationContext(), items[which], Toast.LENGTH_LONG).show();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return viewGroup;
    }
    /*
     * String형을 BitMap으로 변환시켜주는 함수
     * */
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}

