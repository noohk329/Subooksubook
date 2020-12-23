package com.example.subooksubook.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subooksubook.MainActivity;
import com.example.subooksubook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLogin extends AppCompatActivity {

    private String TAG = "*************MainLogin*************";
    private TextView signup, findpassword;
    private Button btn_login;
    private EditText email, password;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlogin);

        initialize();

        /// TODO: 2020-12-23 이메일 비번 설정
        email.setText("minseonpub@gmail.com");
        password.setText("000000");

        // 로그인
        btn_login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String id = email.getText().toString();
                 String pw = password.getText().toString();

                 if (id.isEmpty()) {
                     // 아이디, 비밀번호 둘중 어느곳 하나 빈 곳이 있다면
                     Log.d(TAG, "로그인 실패 : 아이디가 입력되있지 않습니다.");
                     Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG);
                 } else if (pw.isEmpty()) {
                     // 아이디, 비밀번호 둘중 어느곳 하나 빈 곳이 있다면
                     Log.d(TAG, "로그인 실패 : 비밀번호 가 입력되있지 않습니다.");
                     Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_LONG);
                 } else {
                     progressDialog = new ProgressDialog(MainLogin.this,R.style.ProgressDialogStyle);
                     progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                     progressDialog.setMessage("로그인중입니다. \n잠시 기다려 주세요...");
                     progressDialog.show();

                     //logging in the user
                     firebaseAuth.signInWithEmailAndPassword(id, pw)
                             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()) {
//                                         findUserInfo();
                                         FirebaseUser user = firebaseAuth.getCurrentUser();
                                         //textViewUserEmail의 내용을 변경해 준다.
                                         String user_id = user.getUid();
                                         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                         intent.putExtra("user_id", user_id);
                                         intent.putExtra("user", user);
                                         startActivity(intent);
                                         progressDialog.dismiss();
                                     }
                                 }
                                 // 현경: 로그인 에러 exception 오류 출력
                             }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             if (e.getMessage().contains("The email address is badly formatted.")) {
                                 Log.e("LOGIN FAILURE", String.valueOf(e));
                                 Toast.makeText(getApplicationContext(), "올바른 이메일 형식이 아닙니다. 다시 확인해주세요. ", Toast.LENGTH_LONG).show();
                                 email.setText(null);
                             } else if (e.getMessage().contains("There is no user record corresponding to this identifier.")) {
                                 Log.e("LOGIN FAILURE", String.valueOf(e));
                                 Toast.makeText(getApplicationContext(), "등록되지 않은 아이디입니다. 아이디를 확인해 주세요. ", Toast.LENGTH_LONG).show();
                             } else if (e.getMessage().contains("The password is invalid or the user does not have a password")) {
                                 Log.e("LOGIN FAILURE", String.valueOf(e));
                                 Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.  ", Toast.LENGTH_LONG).show();
                             } else if (e.getMessage().contains("The given sign-in provider is disabled for this Firebase project. Enable it in the Firebase console, under the sign-in method tab of the Auth section.")) {
                                 Log.e("LOGIN FAILURE", String.valueOf(e));
                                 Toast.makeText(getApplicationContext(), "서비스 점검중입니다.....  ", Toast.LENGTH_LONG).show();
                             } else if (e.getMessage().contains("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                                 Log.e("LOGIN FAILURE", String.valueOf(e));
                                 Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주세요..", Toast.LENGTH_LONG).show();
                             } else {
                                 Log.e("LOGIN FAILURE", String.valueOf(e));
                                 Toast.makeText(getApplicationContext(), "로그인 실패. 다시 시도해주세요. ", Toast.LENGTH_LONG).show();
                             }
                             progressDialog.dismiss();
                         }
                     });
                 }
             }
        });



        // 비밀번호 찾기
        findpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainLogin.this, FindPassword.class);
                startActivity(intent);
                finish();
            }
        });

        // 회원가입
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainLogin.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initialize(){
        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        signup = findViewById(R.id.text_signup);
        findpassword = findViewById(R.id.text_findpassword);
        btn_login = findViewById(R.id.button_login);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextNumberPassword);
    }
}