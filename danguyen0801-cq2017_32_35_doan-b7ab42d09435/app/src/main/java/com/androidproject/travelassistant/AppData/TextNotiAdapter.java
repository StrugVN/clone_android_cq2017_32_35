package com.androidproject.travelassistant.AppData;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;

import java.util.ArrayList;

public class TextNotiAdapter extends BaseAdapter {
    private ArrayList<TextNoti> tnList;
    Context context;

    public TextNotiAdapter(ArrayList<TextNoti> tnList, Context context) {
        this.tnList = tnList;
        this.context = context;
    }

    public ArrayList<TextNoti> getTnList() {
        return tnList;
    }

    public void setTnList(ArrayList<TextNoti> tnList) {
        this.tnList = tnList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return tnList.size();
    }

    @Override
    public TextNoti getItem(int position) {
        return tnList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.comment_view, null);
        TextView tvName = v.findViewById(R.id.tv_nameCmt);
        TextView tvCmt = v.findViewById(R.id.tv_cmt);
        ImageView ivAvt = v.findViewById(R.id.iv_avtCmt);

        Utility.SpinImageView(ivAvt);

        TextNoti tn = tnList.get(position);

        tvName.setText(tn.getName());
        tvCmt.setText(tn.getNotification());

        if (!TextUtils.isEmpty(tn.getAvatar()))
            new DownloadImageTask(ivAvt).execute(tn.getAvatar());
        else
            Utility.setEmptyAvatar(tn.getName(), tn.getUserId(), ivAvt, v.getContext());

        return v;
    }


}
