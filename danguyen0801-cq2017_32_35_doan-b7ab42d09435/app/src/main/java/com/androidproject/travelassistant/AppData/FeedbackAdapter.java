package com.androidproject.travelassistant.AppData;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;

import java.util.ArrayList;

public class FeedbackAdapter extends BaseAdapter {
    private ArrayList<Feedbacks> fbList;
    private Context context;

    public FeedbackAdapter(ArrayList<Feedbacks> fbList, Context context) {
        this.fbList = fbList;
        this.context = context;
    }

    public ArrayList<Feedbacks> getFbList() {
        return fbList;
    }

    public void setFbList(ArrayList<Feedbacks> fbList) {
        this.fbList = fbList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return fbList.size();
    }

    @Override
    public Feedbacks getItem(int position) {
        return fbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.review_view, null);
        TextView tvName = v.findViewById(R.id.tv_nameRv);
        TextView tvRv = v.findViewById(R.id.tv_rv);
        ImageView ivAvt = v.findViewById(R.id.iv_avtRv);
        RatingBar rtB = v.findViewById(R.id.ratingBar_ReviewTour);
        TextView tvDate = v.findViewById(R.id.tv_dateRv);

        Utility.SpinImageView(ivAvt);

        tvName.setText(fbList.get(position).getName());
        tvRv.setText(fbList.get(position).getFeedback());

        if(!TextUtils.isEmpty(fbList.get(position).getAvatar()))
            new DownloadImageTask(ivAvt).execute(fbList.get(position).getAvatar());
        else
            Utility.setEmptyAvatar(fbList.get(position).getName(), fbList.get(position).getUserId(), ivAvt, v.getContext());

        rtB.setRating(fbList.get(position).getPoint());
        tvDate.setText(Utility.MillisToDate(fbList.get(position).getCreatedOn()));
        return v;
    }
}
