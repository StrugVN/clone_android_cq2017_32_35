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

public class ReviewAdapter extends BaseAdapter {
    private ArrayList<Reviews> rvList;
    private Context context;

    public ReviewAdapter(ArrayList<Reviews> rvList, Context context) {
        this.rvList = rvList;
        this.context = context;
    }

    public ArrayList<Reviews> getRvList() {
        return rvList;
    }

    public void setRvList(ArrayList<Reviews> rvList) {
        this.rvList = rvList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return rvList.size();
    }

    @Override
    public Reviews getItem(int position) {
        return rvList.get(position);
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

        tvName.setText(rvList.get(position).getName());
        tvRv.setText(rvList.get(position).getReview());

        if(!TextUtils.isEmpty(rvList.get(position).getAvatar()))
            new DownloadImageTask(ivAvt).execute(rvList.get(position).getAvatar());
        else
            Utility.setEmptyAvatar(rvList.get(position).getName(), rvList.get(position).getId(), ivAvt, v.getContext());

        rtB.setRating(rvList.get(position).getPoint());

        tvDate.setText(Utility.MillisToDate(rvList.get(position).getCreateOn()));

        return v;
    }
}
