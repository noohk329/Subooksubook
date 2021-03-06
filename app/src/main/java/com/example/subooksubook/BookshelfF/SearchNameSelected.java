package com.example.subooksubook.BookshelfF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subooksubook.BuildConfig;
import com.example.subooksubook.MainActivity;
import com.example.subooksubook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class SearchNameSelected extends AppCompatActivity {

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();

    private int condition;
    private String data_title, data_author, data_publisher, img_url;
    private Bitmap imagebook;
    private int totalpageinput;
    private Button addmybookshelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_name_selected);

        Intent intent = getIntent();
        final String iD_authen = intent.getStringExtra("id");
        final DatabaseReference conditionRef = rootRefer.child(iD_authen).child("mybookshelf");

        final ImageView bookImg = (ImageView) findViewById(R.id.img_book);
        TextView title = (TextView) findViewById(R.id.text_title);
        TextView author = (TextView) findViewById(R.id.text_author);
        TextView publish = (TextView) findViewById(R.id.text_publish);
        TextView description = (TextView) findViewById(R.id.text_descript);

        imagebook = (Bitmap)intent.getParcelableExtra("image");
        data_title = intent.getStringExtra("title");
        data_author = intent.getStringExtra("author");
        data_publisher = intent.getStringExtra("publisher");
        img_url=intent.getStringExtra("image_url");

        title.setText(data_title);
        author.setText(data_author);
        publish.setText(data_publisher);
        description.setText(intent.getStringExtra("description"));
        bookImg.setImageBitmap(imagebook);

        addmybookshelf = (Button)findViewById(R.id.btn_add_bookshelf);
        addmybookshelf.setEnabled(false);

        findViewById(R.id.btn_pageinput).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //여기에 이벤트를 적어주세요
                        pageDialog();
                    }
                }
        );

        condition = 0;
        addmybookshelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (postSnapshot.getKey().equals(data_title)) {
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
                            String zero = "0";

                            //이미지 다운로드
                            String imgUrl = img_url;
                            conditionRef.child(data_title).child("title").setValue(data_title);
                            conditionRef.child(data_title).child("author").setValue(data_author);
                            conditionRef.child(data_title).child("image").setValue(img_url);
                            conditionRef.child(data_title).child("publisher").setValue(data_publisher);
                            conditionRef.child(data_title).child("Time").setValue(getTime);
                            conditionRef.child(data_title).child("totalpage").setValue(totalpageinput);
                            conditionRef.child(data_title).child("progress").setValue(zero);
                            conditionRef.child(data_title).child("lastestPage").setValue(zero);
                            Toast.makeText(getApplicationContext(), "나만의 책꽃이 저장완료", Toast.LENGTH_LONG).show();
                            finish();
                            SearchName searchName = (SearchName) SearchName.searchName;
                            searchName.finish();

                            //fragment 보내기
                            Intent intentF = (new Intent(getApplicationContext(), MainActivity.class));
                            intentF.putExtra("id", iD_authen);
                            Log.d("SearchName", "id :"+ iD_authen);
                            startActivity(intentF);
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

    public void pageDialog()
    {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button submitButton = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button cancelButton = (Button) dialogView.findViewById(R.id.buttonCancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                addmybookshelf.setEnabled(false);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString()==null || Integer.parseInt(editText.getText().toString()) <0){
                    Toast.makeText(getApplicationContext(), "잘못된 페이지 수 입력입니다. 다시 입력하세요", Toast.LENGTH_LONG).show();
                    dialogBuilder.dismiss();
                    addmybookshelf.setEnabled(false);
                }
                else {
                    totalpageinput = Integer.parseInt(editText.getText().toString());
                    dialogBuilder.dismiss();
                    addmybookshelf.setEnabled(true);
                    addmybookshelf.setBackground((Drawable)getResources().getDrawable(R.drawable.button_input_custom));
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}


