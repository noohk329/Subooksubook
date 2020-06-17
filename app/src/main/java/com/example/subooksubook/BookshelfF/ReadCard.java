package com.example.subooksubook.BookshelfF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.subooksubook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadCard extends AppCompatActivity {

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef;
    String iD_authen;
    String get_title;
    int totalpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcard);

        Intent intent = getIntent();
        iD_authen = intent.getStringExtra("id");
        get_title = intent.getStringExtra("title");
        conditionRef = rootRefer.child(iD_authen).child("mybookshelf");
        Log.d("ReadCard", get_title);

        final TextView title = findViewById(R.id.tx_title);
        final TextView author = findViewById(R.id.tx_author);
        final TextView publisher = findViewById(R.id.tx_publisher);

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("ReadCard_title search", postSnapshot.child("title").getValue(String.class));
                    if (postSnapshot.child("title").getValue(String.class).equals(get_title)) {
                        Log.d("ReadCard_into", "1");
                        title.setText((postSnapshot.child("title").getValue(String.class)));
                        author.setText((postSnapshot.child("author").getValue(String.class)));
                        publisher.setText((postSnapshot.child("publisher").getValue(String.class)));
                        totalpage = (postSnapshot.child("totalpage").getValue(Integer.class));
                        Log.d("ReadCard", title.getText().toString());
                        Log.d("ReadCard", author.getText().toString());
                        Log.d("ReadCard", publisher.getText().toString());
                        Log.d("ReadCard", String.valueOf(totalpage));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ReadCard", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
