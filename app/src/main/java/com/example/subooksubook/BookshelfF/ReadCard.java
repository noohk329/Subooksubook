package com.example.subooksubook.BookshelfF;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.subooksubook.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReadCard extends AppCompatActivity {

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    String iD_authen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcard);
    }
}
