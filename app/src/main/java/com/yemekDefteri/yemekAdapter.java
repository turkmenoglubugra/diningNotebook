package com.yemekDefteri;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yemekDefteri.R;

import java.util.List;


public class yemekAdapter extends BaseAdapter {
    private LayoutInflater courseInflater;
    private List<Yemek> yemekList;

    public yemekAdapter(Activity activity, List<Yemek> courseList) {
        courseInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.yemekList = courseList;
    }

    @Override
    public int getCount() {
        return yemekList.size();
    }

    @Override
    public Object getItem(int i) {
        return yemekList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View lineView;
        lineView = courseInflater.inflate(R.layout.yemeklistview, null);
        TextView textViewCourseName = (TextView) lineView.findViewById(R.id.textViewYemekName);
        ImageView imageViewCoursePicture = (ImageView) lineView.findViewById(R.id.imageViewYemekPicture);

        Yemek yemek = yemekList.get(i);
        textViewCourseName.setText(yemek.getYemekAdi());
        imageViewCoursePicture.setImageResource(R.mipmap.s_round);

        return lineView;
    }
}