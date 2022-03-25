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

public class MemberAdapter extends BaseAdapter {
    private ArrayList<Members> memList;
    private Context context;

    public MemberAdapter(ArrayList<Members> memList, Context context) {
        this.memList = memList;
        this.context = context;
    }

    public ArrayList<Members> getMemList() {
        return memList;
    }

    public void setMemList(ArrayList<Members> memList) {
        this.memList = memList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return memList.size();
    }

    @Override
    public Members getItem(int position) {
        return memList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.member_view, null);
        TextView tvName = v.findViewById(R.id.tv_nameMember);
        TextView tvPhone = v.findViewById(R.id.tv_phoneMember);
        ImageView ivAvt = v.findViewById(R.id.iv_avtMember);

        tvName.setText(memList.get(position).getName());
        tvPhone.setText(memList.get(position).getPhone());

        Utility.SpinImageView(ivAvt);

        if(!TextUtils.isEmpty(memList.get(position).getAvatar()))
            new DownloadImageTask(ivAvt).execute(memList.get(position).getAvatar());
        else
            Utility.setEmptyAvatar(memList.get(position).getName(), memList.get(position).getId(), ivAvt, v.getContext());

        return v;
    }
}
