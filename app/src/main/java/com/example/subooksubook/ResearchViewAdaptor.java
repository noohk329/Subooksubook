package com.example.subooksubook;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class ResearchViewAdaptor extends BaseAdapter{

    /* 데이터 그릇들의 집합을 정의 */
    private ArrayList<ResearchViewItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ResearchViewItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        // 커스텀 리스트뷰의 xml을 inflate
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemsearch_custom, parent, false);
        }

        /* 커스텀 리스트뷰 xml에 있는 속성값들을 정의 */
        TextView book_title = (TextView) convertView.findViewById(R.id.text_title);
        TextView book_author = (TextView) convertView.findViewById(R.id.text_author);
        TextView book_publisher = (TextView) convertView.findViewById(R.id.text_publish);
        TextView book_description = (TextView) convertView.findViewById(R.id.text_descript);
        ImageView book_image = (ImageView)convertView.findViewById(R.id.book_imageView);


        /* 데이터를 담는 그릇 정의 */
        ResearchViewItem researchViewItem = getItem(position);

        /* 해당 그릇에 담긴 정보들을 커스텀 리스트뷰 xml의 각 TextView에 뿌려줌 */
        book_title.setText(researchViewItem.getTitle());
        book_author.setText(researchViewItem.getAuthor());
        book_publisher.setText(researchViewItem.getPublisher());
//        book_description.setText(researchViewItem.getDescription());
        book_image.setImageBitmap(researchViewItem.getBookImage());

        return convertView;
    }

    /* 네이버 검색 중, 제목, 저자, 이미지, 출판사, 설명 등을 담음 */
    public void addItem(String title, String author, String publisher, String description, Bitmap imagebook) {

        ResearchViewItem mItem = new ResearchViewItem();

        mItem.setTitle(title);
        mItem.setAuthor(author);
        mItem.setPublisher(publisher);
        mItem.setBookImage(imagebook);
        mItem.setDescription(description);

        /* 데이터그릇 mItem에 담음 */
        mItems.add(mItem);

    }
}
