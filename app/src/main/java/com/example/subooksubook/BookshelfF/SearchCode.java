package com.example.subooksubook.BookshelfF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.subooksubook.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class SearchCode extends AppCompatActivity {

    private static String CLIENT_ID = "VrRub9EgH_WUlJTdizBP";
    private static String CLIENT_PASSWORD = "Cly7aO4MIz";
    String bookName, bookPrice, bookPubDate, authorName, publisherName;
    private String iD_authen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybookshelf_searchcode);

        Intent intent = getIntent();
        iD_authen = intent.getStringExtra("id");
        Log.d("SearchName", "id :"+ iD_authen);

        /**********권한 요청************/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * 사용자 단말기의 권한 중 "카메라" 권한이 허용되어 있는지 확인한다.
             *  Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다.
             */
            int permissionResult = checkSelfPermission(Manifest.permission.CAMERA);


            /** * 패키지는 안드로이드 어플리케이션의 아이디이다. *
             *  현재 어플리케이션이 카메라에 대해 거부되어있는지 확인한다. */
            if (permissionResult == PackageManager.PERMISSION_DENIED) {

                /** * 사용자가 CALL_PHONE 권한을 거부한 적이 있는지 확인한다. *
                 * 거부한적이 있으면 True를 리턴하고 *
                 * 거부한적이 없으면 False를 리턴한다. */
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SearchCode.this);
                    dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 단말기의 \"카메라\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    /** * 새로운 인스턴스(onClickListener)를 생성했기 때문에 *
                                     * 버전체크를 다시 해준다. */
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        // CALL_PHONE 권한을 Android OS에 요청한다.
                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SearchCode.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();
                    new IntentIntegrator(this).initiateScan();
                }
                // 최초로 권한을 요청할 때
                else {
                    // CALL_PHONE 권한을 Android OS에 요청한다.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                }
            }
            // CALL_PHONE의 권한이 있을 때
            else {
                new IntentIntegrator(this).initiateScan();
            }
        }
        /************권한요청 끝**************/

    }

    /** * 권한 요청에 대한 응답을 이곳에서 가져온다. * *
     *  @param requestCode 요청코드 *
     *  @param permissions 사용자가 요청한 권한들 *
     *  @param grantResults 권한에 대한 응답들(인덱스별로 매칭) */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            // 요청한 권한을 사용자가 "허용" 했다면...
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 이곳에 허용했을때 실행할 코드를 넣는다

            } else {
                // 거부했을때 띄워줄
                Toast.makeText(SearchCode.this, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // QR코드/바코드를 스캔한 결과 값을 가져옵니다.
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // 결과값 출력
        Toast.makeText(this, "출력 ISBN:" + result.getContents(), Toast.LENGTH_SHORT).show();

    }
}


