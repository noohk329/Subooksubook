package com.example.subooksubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchNameSelected extends AppCompatActivity {

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference conditionRef = rootRefer.child("mybookshelf");

    int condition;
    String data_title, data_author, data_publisher;
    Bitmap imagebook;
    BookViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_name_selected);

        Intent intent = getIntent();
        final String iD_authen = intent.getStringExtra("id");
        final DatabaseReference conditionRef = rootRefer.child(iD_authen).child("mybookshelf");

        ImageView bookImg = (ImageView) findViewById(R.id.img_book);
        TextView title = (TextView) findViewById(R.id.text_title);
        TextView author = (TextView) findViewById(R.id.text_author);
        TextView publish = (TextView) findViewById(R.id.text_publish);
        TextView description = (TextView) findViewById(R.id.text_descript);

        imagebook = (Bitmap) intent.getParcelableExtra("image");
        data_title = intent.getStringExtra("title");
        data_author = intent.getStringExtra("author");
        data_publisher = intent.getStringExtra("publisher");

        title.setText(data_title);
        author.setText(data_author);
        publish.setText(data_publisher);
        description.setText(intent.getStringExtra("description"));
        bookImg.setImageBitmap(imagebook);

        condition = 0;
        Button addmybookshelf = (Button)findViewById(R.id.btn_add_bookshelf);
        addmybookshelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (postSnapshot.child("title").getValue(String.class).equals(data_title)) {
                                Toast.makeText(getApplicationContext(), "이미 책꽃이에 있는 책입니다.", Toast.LENGTH_LONG).show();
                                condition = -1;
                                break;
                            }
                        }
                        if (condition == 0) {
                            // 현재날짜 받아오기(시스템상 시간)
                            long now = System.currentTimeMillis();
                            Date mDate = new Date(now);
                            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  //받아오고싶은 형식 설정
                            String getTime = simpleDate.format(mDate);

                            conditionRef.child(getTime).child("title").setValue(data_title);
                            conditionRef.child(getTime).child("author").setValue(data_author);
                            conditionRef.child(getTime).child("imagebitmap").setValue(BitmapToString(imagebook));
                            conditionRef.child(getTime).child("publisher").setValue(data_publisher);
                            conditionRef.child(getTime).child("Time").setValue(getTime);
                            Toast.makeText(getApplicationContext(), "나만의 책꽃이 저장완료", Toast.LENGTH_LONG).show();
                            finish();
                            SearchName searchName = (SearchName) SearchName.searchName;
                            searchName.finish();
                        }
                        else {
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("SearchNameSelected", "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }
    /*
     * Bitmap을 String형으로 변환
     * */
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }
}
