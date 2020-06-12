package com.example.subooksubook;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookViewAdapter extends BaseAdapter {
    private DatabaseReference rootRefer = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference conditionRef = rootRefer.child("mybookshelf");

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<BookViewItem> bookItemList = new ArrayList<BookViewItem>() ;

    // ListViewAdapter의 생성자
    public BookViewAdapter() {  }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return bookItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_custom, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView book_title = (TextView) convertView.findViewById(R.id.text_title);
        TextView book_author = (TextView) convertView.findViewById(R.id.text_author);
        TextView book_publisher = (TextView) convertView.findViewById(R.id.text_publish);
        ImageView book_image = (ImageView)convertView.findViewById(R.id.book_imageView);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        /* 데이터를 담는 그릇 정의 */
        BookViewItem bookViewItem = bookItemList.get(position);

        /* 해당 그릇에 담긴 정보들을 커스텀 리스트뷰 xml의 각 TextView에 뿌려줌 */
        // 아이템 내 각 위젯에 데이터 반영
        book_title.setText(bookViewItem.getTitle());
        book_author.setText(bookViewItem.getAuthor());
        book_publisher.setText(bookViewItem.getPublisher());
        book_image.setImageBitmap(bookViewItem.getBookImage());


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return bookItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String author, String publisher, Bitmap imagebook) {
        BookViewItem mItem = new BookViewItem();

        mItem.setTitle(title);
        mItem.setAuthor(author);
        mItem.setPublisher(publisher);
        mItem.setBookImage(imagebook);

        /* 데이터그릇 mItem에 담음 */
        bookItemList.add(mItem);

    }
    public String getTitle(int position)
    {
        return bookItemList.get(position).getTitle();
    }
    public String getAuthor(int position)
    {
        return bookItemList.get(position).getAuthor();
    }
    public String getPublisher(int position)
    {
        return bookItemList.get(position).getPublisher();
    }
    public Bitmap getBookImage(int position)
    {
        return bookItemList.get(position).getBookImage();
    }
}
