package com.example.subooksubook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SearchName extends AppCompatActivity {
    ViewGroup viewGroup;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybookshelf_searchbyname);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = getIntent();
                    final String keyword = intent.getStringExtra("keyword");
                    str = getNaverSearch(keyword);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView searchResult = (TextView) findViewById(R.id.searchResult);
                            if(keyword==null);
                            else
                                searchResult.setText(str);
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });thread.start();

        Button btn = (Button) findViewById(R.id.btn_serach);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searchText = (TextView) findViewById(R.id.et_search);
                String keyword = searchText.getText().toString();

                Intent intent = getIntent();
                //Intent intent = new Intent(getApplicationContext(), SearchName.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });
    }

    public String getNaverSearch(String keyword) {

        String clientID = "VrRub9EgH_WUlJTdizBP";
        String clientSecret = "Cly7aO4MIz";
        StringBuffer sb = new StringBuffer();

        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book.xml?query=" + text + "&display=10" + "&start=1";

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            String tag;
            //inputStream으로부터 xml값 받기
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("item")) ; //첫번째 검색 결과
                        else if (tag.equals("title")) {
                            sb.append("제목 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("author")) {
                            sb.append("저자 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("publisher")) {
                            sb.append("출판사 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("image")) {
                            sb.append("이미지 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("description")) {
                            sb.append("정보 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n\n\n\n");
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }
}




