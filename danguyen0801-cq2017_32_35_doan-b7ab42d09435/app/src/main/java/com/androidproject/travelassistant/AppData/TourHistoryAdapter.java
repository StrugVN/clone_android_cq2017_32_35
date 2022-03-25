package com.androidproject.travelassistant.AppData;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Utility.DownloadImageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TourHistoryAdapter extends BaseAdapter {
    private ArrayList<Tours> tourList;
    private Context context;

    public TourHistoryAdapter(ArrayList<Tours> tourList, Context context) {
        this.tourList = tourList;
        this.context = context;
    }

    public ArrayList<Tours> getTourList() {
        return tourList;
    }

    public void setTourList(ArrayList<Tours> tourList) {
        this.tourList = tourList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return  tourList.size();
    }

    @Override
    public Tours getItem(int position) {
        return tourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.tour_view, null);
        TextView tvName = v.findViewById(R.id.Tour_NameView);
        TextView tvStartDate = v.findViewById(R.id.Tour_StartDateView);
        TextView tvEndDate = v.findViewById(R.id.Tour_EndDateView);
        TextView tvMinCost = v.findViewById(R.id.Tour_MinCostView);
        TextView tvMaxCost = v.findViewById(R.id.Tour_MaxCostView);
        ImageView ivAvatar = v.findViewById(R.id.iv_Tour);
        ImageView ivHost = v.findViewById(R.id.iv_isHosted);
        LinearLayout layout = v.findViewById(R.id.tour_view_frame);

        if(tourList.get(position).getStatus() == -1){
            v.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            return v;
        }

        tvName.setText(tourList.get(position).getName());

        tvStartDate.setText(
                new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US)
                        .format(new Date(tourList.get(position).getStartDate())));
        tvEndDate.setText(
                new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US)
                        .format(new Date(tourList.get(position).getEndDate())));

        tvMinCost.setText(String.valueOf(tourList.get(position).getMinCost()));
        tvMaxCost.setText(String.valueOf(tourList.get(position).getMaxCost()));


        if (!TextUtils.isEmpty(tourList.get(position).getAvatar()))
            new DownloadImageTask(ivAvatar).execute(tourList.get(position).getAvatar());
        if(tourList.get(position).isHost())
            ivHost.setVisibility(View.VISIBLE);
        return v;
    }
}
