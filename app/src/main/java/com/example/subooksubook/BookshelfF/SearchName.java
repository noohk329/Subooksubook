package com.example.subooksubook.BookshelfF;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.subooksubook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SearchName extends AppCompatActivity {
    final String clientId = "VrRub9EgH_WUlJTdizBP";//애플리케이션 클라이언트 아이디값";
    final String clientSecret = "Cly7aO4MIz";//애플리케이션 클라이언트 시크릿값";

    private String iD_authen;

    public static Activity searchName;
    private static final String TAG = "SearchName";
    private ListView mListView;

    StringBuilder searchResult;
    BufferedReader br;
    String[] title, author, publisher, description, img_url;
    Bitmap[] imagebook;
    ResearchViewAdaptor mMyAdapter;

    int itemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybookshelf_searchbyname);

        Intent intent = getIntent();
        iD_authen = intent.getStringExtra("id");
        Log.d("SearchName", "id :"+ iD_authen);

        Button btn = (Button) findViewById(R.id.btn_serach);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searchText = (TextView)findViewById(R.id.et_search);
                String keyword = searchText.getText().toString();
                searchNaverAPI(keyword);
            }
        });
        mListView=(ListView)findViewById(R.id.resultlist);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SearchNameSelected.class);
                intent.putExtra("title", mMyAdapter.getItem(position).getTitle());
                intent.putExtra("author", mMyAdapter.getItem(position).getAuthor());
                intent.putExtra("publisher", mMyAdapter.getItem(position).getPublisher());
                intent.putExtra("description", mMyAdapter.getItem(position).getDescription());
                intent.putExtra("image", mMyAdapter.getItem(position).getBookImage());

                searchName = SearchName.this;

                intent.putExtra("id", iD_authen);
                Log.d("SearchName", "id :"+ iD_authen);
                startActivity(intent);
            }
        });
    }

    public void searchNaverAPI(final String keyword) {
        final int display = 10; // 보여지는 검색결과의 수

        // 네트워크 연결은 Thread 생성 필요
        new Thread() {

            @Override
            public void run() {
                try {
                    String text = URLEncoder.encode(keyword, "UTF-8");
                    Log.d(TAG, "keyword : " + text);
                    String apiURL = "https://openapi.naver.com/v1/search/book.json?query=" + text + "&display=" + display + "&"; // json 결과

                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    con.connect();

                    int responseCode = con.getResponseCode();
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
                    }
                    br.close();
                    con.disconnect();

                    String data = searchResult.toString();
                    String[] array = data.split("\"");
                    title = new String[display];
                    author = new String[display];
                    description = new String[display];
                    publisher = new String[display];
                    img_url = new String[display];
                    imagebook = new Bitmap[display];

                    itemCount = 0;
                    for (int i = 0; i < array.length; i++) {
//                        Log.d(TAG, "array: " + array[i]);
                        if (array[i].equals("title"))
                            title[itemCount] = removeTag(array[i + 2]);
                        if (array[i].equals("author"))
                            author[itemCount] = array[i + 2];
                        if (array[i].equals("description")) {
                            description[itemCount] = removeTag(array[i + 2]);
                            itemCount++;  }
                        if (array[i].equals("publisher"))
                            publisher[itemCount] = array[i + 2];
                        if (array[i].equals("image")) {
                            // imageView Link connect
                            // 웹에서 이미지를 가지고 온 후 이미지 뷰에 지정할 Bitmap 생성
                            URL imgurl = new URL(array[i + 2]);
                            HttpURLConnection conn = (HttpURLConnection)imgurl.openConnection();
                            conn.setDoInput(true);

                            InputStream is = conn.getInputStream();
                            imagebook[itemCount]  = BitmapFactory.decodeStream(is);
                        }
                    }
                    // 결과를 성공적으로 불러오면, UiThread에서 listView에 데이터를 추가
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listViewDataAdd();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "error : " + e);
                }
            }
        }.start();
    }
    public void listViewDataAdd() {
        mMyAdapter = new ResearchViewAdaptor();
        for (int i = 0; i < itemCount; i++) {
            mMyAdapter.addItem((title[i]).toString(),
                    (author[i]).toString(),
                    (publisher[i]).toString(),
                    (description[i]).toString(),
                    (imagebook[i]));
        }
        // set adapter on listView
        mListView.setAdapter(mMyAdapter);
    }

    public String removeTag(String html) throws Exception {
        return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }
}




