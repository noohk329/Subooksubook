package com.example.subooksubook.BookshelfF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subooksubook.MainActivity;
import com.example.subooksubook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadCard extends AppCompatActivity {

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference conditionRef;
    private String iD_authen, get_title, totalPage_DB;
    private int checkerForTotalPageInput, submit_check, last_page;
    private String dialog_page, getTime;
    private boolean addDatabase;

    TextView title, author, publisher, totalPage, readFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcard);

        addDatabase = false;
        Intent intent = getIntent();
        iD_authen = intent.getStringExtra("id");
        get_title = intent.getStringExtra("title");
        conditionRef = rootRefer.child(iD_authen).child("mybookshelf");
        Log.d("ReadCard", get_title);

        checkerForTotalPageInput =0; last_page=0;
        submit_check=0;

        title = findViewById(R.id.tx_title);
        author = findViewById(R.id.tx_author);
        publisher = findViewById(R.id.tx_publisher);
        totalPage = findViewById(R.id.tx_totalpage);
        readFinal = findViewById(R.id.tx_readfinal);

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkerForTotalPageInput =0;last_page=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("ReadCard_title search", postSnapshot.child("title").getValue(String.class));
                    if (postSnapshot.getKey().equals(get_title)) {
                        Log.d("ReadCard_into", "1");
                        title.setText((postSnapshot.child("title").getValue(String.class)));
                        author.setText((postSnapshot.child("author").getValue(String.class)));
                        publisher.setText((postSnapshot.child("publisher").getValue(String.class)));
                        totalPage.setText("총 "+ postSnapshot.child("totalpage").getValue(Integer.class).toString()+"쪽");
                        totalPage_DB = postSnapshot.child("totalpage").getValue(Integer.class).toString();
                        Log.d("ReadCard", title.getText().toString());
                        Log.d("ReadCard", author.getText().toString());
                        Log.d("ReadCard", publisher.getText().toString());

                        for(DataSnapshot postSnapshot_d : postSnapshot.child("record").getChildren()) {
                            if (postSnapshot_d.child("addTime").getValue(String.class) == null || postSnapshot_d.child("perPage").getValue(String.class) == null
                                    || postSnapshot_d.child("perPercent").getValue(String.class) == null) {
                                break;
                            } else {
                                if (addDatabase == false) {
                                    Log.d("ReadCard_into : " + checkerForTotalPageInput, postSnapshot_d.child("addTime").getValue(String.class));
                                    Log.d("ReadCard_into : " + checkerForTotalPageInput, postSnapshot_d.child("perPage").getValue(String.class));
                                    Log.d("ReadCard_into : " + checkerForTotalPageInput, postSnapshot_d.child("perPercent").getValue(String.class));
                                    tableAllocate(postSnapshot_d.child("addTime").getValue(String.class), postSnapshot_d.child("perPage").getValue(String.class),
                                            totalPage_DB);

                                    if (last_page < Integer.parseInt(postSnapshot_d.child("perPage").getValue(String.class)))
                                        last_page = Integer.parseInt(postSnapshot_d.child("perPage").getValue(String.class));
                                    Log.d("last Page : " + last_page, postSnapshot_d.child("perPage").getValue(String.class));
                                }
                            }
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ReadCard", "loadPost:onCancelled", databaseError.toException());
            }
        });
        //뒤로가기
        Button back_space = findViewById(R.id.btn_backspace);
        back_space.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentF = (new Intent(getApplicationContext(), MainActivity.class));
                intentF.putExtra("id", iD_authen);
                Log.d("ReadCard", "id :"+ iD_authen);
                addDatabase=false;
                startActivity(intentF);
            }
        });
        //페이지 추가하기
        Button addpage_daily = findViewById(R.id.btn_dailypage_input);
        addpage_daily.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ReadCard_into", "버튼이 눌렸습니다.");
                final AlertDialog dialogBuilder = new AlertDialog.Builder(ReadCard.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog_page, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
                Button submitButton = (Button) dialogView.findViewById(R.id.buttonSubmit);
                Button cancelButton = (Button) dialogView.findViewById(R.id.buttonCancel);
                Log.d("ReadCard_into", "버튼이 눌렸습니다.");
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText.getText().toString()==null || Integer.parseInt(editText.getText().toString()) <0||
                                Integer.parseInt(editText.getText().toString()) > Integer.parseInt(totalPage_DB)
                                || last_page> Integer.parseInt(editText.getText().toString()) || Integer.parseInt(editText.getText().toString())==last_page){
                            Toast.makeText(getApplicationContext(), "잘못된 페이지 수 입력입니다. 다시 입력하세요", Toast.LENGTH_LONG).show();
                            dialogBuilder.dismiss();
                        }
                        else {
                            // 한번 다 읽으면 1독 표시
                            if(Integer.parseInt(editText.getText().toString()) == Integer.parseInt(totalPage_DB))
                                readFinal.setText("1 독");
                            submit_check = 1;
                            dialog_page=editText.getText().toString();
                            Log.d("ReadCard_into", "다이얼로그 완료");
                            dialogBuilder.dismiss();

                            Log.d("ReadCard_into", "dialog_input  " );
                            //현재 시간 추가
                            long now = System.currentTimeMillis();
                            Date mDate = new Date(now);
                            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");  //받아오고싶은 형식 설정
                            getTime = simpleDate.format(mDate);

                            Date mDate_title = new Date(now);
                            SimpleDateFormat simpleDate_title = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  //받아오고싶은 형식 설정
                            final String getTime_title = simpleDate_title.format(mDate_title);
                            Log.d("ReadCard_into", "sumbit  " + submit_check );
                            if(submit_check ==1) {
                                Log.d("ReadCard_into", "책페이지 추가등록 시작");
                                conditionRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            if (postSnapshot.getKey().equals(get_title)) {
                                                Log.d("ReadCard_into", "1");
                                                addDatabase = true;
                                                conditionRef.child(get_title).child("record").child(getTime_title).child("isdiary").setValue("0");
                                                conditionRef.child(get_title).child("record").child(getTime_title).child("perPage").setValue(dialog_page);
                                                conditionRef.child(get_title).child("record").child(getTime_title).child("addTime").setValue(getTime);

                                                String a = String.valueOf((int)(((Double.parseDouble(dialog_page) / Double.parseDouble(totalPage_DB)))*(int)(100)));
                                                conditionRef.child(get_title).child("record").child(getTime_title).child("perPercent").setValue(a);
                                                conditionRef.child(get_title).child("progress").setValue(a);

                                                Log.d("ReadCard_into", "책페이지 추가등록 완료");
                                                break;
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.w("ReadCard_pageInput", "loadPost:onCancelled", databaseError.toException());
                                    }
                                });

                                Log.d("ReadCard_into", "책페이지 추가등록 동적할당 시작");
                                Log.d("ReadCard_into", "checker = " + checkerForTotalPageInput);

                                // Table 동적 할당 늘리기
                                tableAllocate(getTime, dialog_page, totalPage_DB);
                            }
                        }
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        });
    }

    public void tableAllocate(String time, String page, String total)
    {
        TableLayout table_adding = (TableLayout) findViewById(R.id.tablelayout_page);
        TableRow row = new TableRow(ReadCard.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp);

        TextView date = new TextView(ReadCard.this);
        TextView dPage = new TextView(ReadCard.this);
        TextView percent = new TextView(ReadCard.this);
        date.setText(time);
        dPage.setText("~ p." + page + "  ");
        percent.setText((int)(((Double.parseDouble(page) / Double.parseDouble(total)))*(int)(100)) + "%");

        date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        dPage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        percent.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        row.addView(date);
        row.addView(dPage);
        row.addView(percent);

        table_adding.addView(row);
        Log.d("ReadCard_into", "책페이지 추가등록 동적할당 끝");
    }
}
