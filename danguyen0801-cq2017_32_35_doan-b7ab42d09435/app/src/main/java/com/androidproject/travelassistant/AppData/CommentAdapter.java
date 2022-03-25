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

public class CommentAdapter extends BaseAdapter {
    private ArrayList<Comment> cmtList;
    private Context context;

    public CommentAdapter(ArrayList<Comment> cmtList, Context context) {
        this.cmtList = cmtList;
        this.context = context;
    }

    public ArrayList<Comment> getCmtList() {
        return cmtList;
    }

    public void setCmtList(ArrayList<Comment> cmtList) {
        this.cmtList = cmtList;
    }

    @Override
    public int getCount() {
        return cmtList.size();
    }

    @Override
    public Comment getItem(int position) {
        return cmtList.get(position);
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

        tvName.setText(cmtList.get(position).getName());
        tvCmt.setText(cmtList.get(position).getComment());

        if (!TextUtils.isEmpty(cmtList.get(position).getAvatar()))
            new DownloadImageTask(ivAvt).execute(cmtList.get(position).getAvatar());
        else
            Utility.setEmptyAvatar(cmtList.get(position).getName(), cmtList.get(position).getId(), ivAvt, v.getContext());

        return v;
    }
}
