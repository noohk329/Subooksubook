package com.example.subooksubook.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subooksubook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    private String TAG = "**********Signup***********";

    //define view objects
    private EditText editTextEmail;
    private EditText editTextPassword, editTextPasswordRE;
    private Button buttonSignup;
    private TextView textviewSignin, textviewMessage;
    private ProgressDialog progressDialog;

    //define firebase object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        initialize();
    }

    private void initialize(){
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextNumberPassword);
        editTextPasswordRE = (EditText)findViewById(R.id.editTextNumberPasswordRE);
        textviewSignin = (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.button_signup);
        progressDialog = new ProgressDialog(Signup.this, R.style.ProgressDialogStyle);

        //button click event
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        textviewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, MainLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Firebse creating a new user
    private void registerUser(){
        String email ,password, password_re;

        if(editTextEmail.getText()==null || editTextPassword.getText()==null ||editTextPasswordRE.getText()==null){
            Toast.makeText(this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        //email과 password가 비었는지 아닌지를 체크 한다.
        else if(TextUtils.isEmpty(editTextEmail.getText())){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(editTextPassword.getText())){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(editTextPasswordRE.getText())){
            Toast.makeText(this, "Password 확인란을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }else {
            //사용자가 입력하는 email, password를 가져온다.
            email = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            password_re = editTextPasswordRE.getText().toString().trim();
            if (!password.equals(password_re)) {
                Toast.makeText(this, "Password가 일치하지 않습니다.\n 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!(email.contains("@") && email.contains("."))) {
                Toast.makeText(this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog = new ProgressDialog(Signup.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.signOut();
                            startActivity(new Intent(getApplicationContext(), MainLogin.class));
                            finish();
                        }
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(Signup.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "에러발생: "+e);
                progressDialog.dismiss();
            }
        });
    }
}
