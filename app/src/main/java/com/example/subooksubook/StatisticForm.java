package com.example.subooksubook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.RecoverySystem;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subooksubook.BookshelfF.ReadCard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticForm extends Fragment {
    ViewGroup viewGroup;

    String iD_authen;

    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef;
    public StatisticForm() {   }
    public StatisticForm(String iD_authen) {
        // Required empty public constructor
        this.iD_authen = iD_authen;
        Log.d("MyBookShelf", "id :"+ this.iD_authen);
        conditionRef = rootRefer.child(iD_authen).child("mybookshelf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.statistic_main, container, false);

        TextView getAimbtn = (TextView)viewGroup.findViewById(R.id.getAim_btn);
        ProgressBar progressBar = (ProgressBar)viewGroup.findViewById(R.id.progressBar3);


//        getAimbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.custom_dialog_getaim, null);
//
//                final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
//                Button submitButton = (Button) dialogView.findViewById(R.id.buttonSubmit);
//                Button cancelButton = (Button) dialogView.findViewById(R.id.buttonCancel);
//
//                conditionRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        TextView aimAmount = (TextView)viewGroup.findViewById(R.id.show_amountText);
//                        String amount = dataSnapshot.child("aim").getValue(String.class);
//                        aimAmount.setText(amount +" 권");
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                cancelButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialogBuilder.dismiss();
//                    }
//                });
//                submitButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        conditionRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                conditionRef.child("aim").setValue(editText.getText());
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Log.w("ReadCard_pageInput", "loadPost:onCancelled", databaseError.toException());
//                            }
//                        });
//
//                    }
//                });
//
//                dialogBuilder.setView(dialogView);
//                dialogBuilder.show();
//            }
//        });

        return viewGroup;
    }

    public void aimOnClick(View v){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_getaim, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button submitButton = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button cancelButton = (Button) dialogView.findViewById(R.id.buttonCancel);

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView aimAmount = (TextView)viewGroup.findViewById(R.id.show_amountText);
                String amount = dataSnapshot.child("aim").getValue(String.class);
                aimAmount.setText(amount +" 권");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conditionRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        conditionRef.child("aim").setValue(editText.getText());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("ReadCard_pageInput", "loadPost:onCancelled", databaseError.toException());
                    }
                });

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

}

