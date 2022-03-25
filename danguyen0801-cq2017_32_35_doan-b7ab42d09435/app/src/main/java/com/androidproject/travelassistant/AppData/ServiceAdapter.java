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

import java.util.ArrayList;

public class ServiceAdapter extends BaseAdapter {
    private ArrayList<Service> svList;
    Context context;

    public ServiceAdapter(ArrayList<Service> svList, Context context) {
        this.svList = svList;
        this.context = context;
    }

    public ArrayList<Service> getSvList() {
        return svList;
    }

    public void setSvList(ArrayList<Service> svList) {
        this.svList = svList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return svList.size();
    }

    @Override
    public Service getItem(int position) {
        return svList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.service_view, null);
        TextView tvName = v.findViewById(R.id.tv_nameService);
        TextView tvAddr = v.findViewById(R.id.tv_addrService);
        TextView tvCost = v.findViewById(R.id.tv_cost_Service);

        ImageView ivAvt = v.findViewById(R.id.iv_ServiceAvatar);

        tvName.setText(svList.get(position).getName());
        tvAddr.setText(svList.get(position).getAddress());

        tvCost.setText("Cost: ");
        tvCost.append(String.valueOf(svList.get(position).getMinCost()));
        tvCost.append(" - ");
        tvCost.append(String.valueOf(svList.get(position).getMaxCost()));

        if(!TextUtils.isEmpty(svList.get(position).getAvatar())){
            new DownloadImageTask(ivAvt).execute(svList.get(position).getAvatar());
        }
        else{
            if (svList.get(position).getServiceTypeId() == 1)
                ivAvt.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_sp_restaurant));
            if (svList.get(position).getServiceTypeId() == 2)
                ivAvt.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_sp_hotel));
            if (svList.get(position).getServiceTypeId() == 3)
                ivAvt.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_sp_rest));
        }
        return v;
    }
}
