package com.androidproject.travelassistant.AppData;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.ResponseInvitationRequest;
import com.androidproject.travelassistant.Request.ResponseInvitationResponse;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationAdapter extends BaseAdapter {
    private ArrayList<Tours_Invitation> invList;
    private Context context;

    public InvitationAdapter(ArrayList<Tours_Invitation> invList, Context context) {
        this.invList = invList;
        this.context = context;
    }

    public ArrayList<Tours_Invitation> getInvList() {
        return invList;
    }

    public void setInvList(ArrayList<Tours_Invitation> invList) {
        this.invList = invList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return invList.size();
    }

    @Override
    public Tours_Invitation getItem(int position) {
        return invList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.invitation_view, null);

        TextView tvTourName = v.findViewById(R.id.tvTourName_Inv);
        TextView tvHostName = v.findViewById(R.id.tvHostName_Inv);
        TextView tvMinCost = v.findViewById(R.id.tvMinCost_Inv);
        TextView tvMaxCost = v.findViewById(R.id.tvMaxCost_Inv);
        TextView tvEmail = v.findViewById(R.id.tv_emailHost_Inv);
        TextView tvPhone = v.findViewById(R.id.tv_phoneHost_Inv);

        ImageView ivHostAva = v.findViewById(R.id.ivHostAva_Inv);
        ImageView ivTourAva = v.findViewById(R.id.iv_TourAva_Inv);

        Button btYes = v.findViewById(R.id.bt_Accept);
        Button btNo = v.findViewById(R.id.bt_Deny);

        final Tours_Invitation ti = invList.get(position);

        tvTourName.setText(ti.getName());
        tvHostName.setText(ti.getHostName());
        tvMinCost.setText(String.valueOf(ti.getMinCost()));
        tvMaxCost.setText(String.valueOf(ti.getMaxCost()));
        tvEmail.setText(ti.getHostEmail());
        tvPhone.setText(ti.getHostPhone());

        if(!TextUtils.isEmpty(ti.getAvatar()))
            new DownloadImageTask(ivTourAva).execute(ti.getAvatar());

        if(!TextUtils.isEmpty(ti.getHostAvatar()))
            new DownloadImageTask(ivHostAva).execute(ti.getHostAvatar());
        else
            Utility.setEmptyAvatar(ti.getHostName(), ti.getHostId(), ivHostAva, v.getContext());

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseToInvite(false, ti);
            }
        });

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseToInvite(true, ti);
            }
        });

        return v;
    }

    private void responseToInvite(final boolean answer, final Tours_Invitation ti){
        final ResponseInvitationRequest request = new ResponseInvitationRequest();

        request.setTourId(ti.getId());
        request.setAccepted(answer);

        Call<ResponseInvitationResponse> call = Global.userService.Response_Invitation(Global.userToken, request);

        call.enqueue(new Callback<ResponseInvitationResponse>() {
            @Override
            public void onResponse(Call<ResponseInvitationResponse> call, Response<ResponseInvitationResponse> response) {
                if(response.code()!=200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(context,
                                "Error " + response.code() + ", retrying", Toast.LENGTH_LONG).show();
                        Log.d(context.getClass().getSimpleName(),
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                invList.remove(ti);
                notifyDataSetChanged();
                if(answer == true)
                    Toast.makeText(context, "Joined " + ti.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseInvitationResponse> call, Throwable t) {
                Toast.makeText(context,
                        "Error " + t.getMessage() + ", retrying", Toast.LENGTH_LONG).show();
                Log.d(context.getClass().getSimpleName(),
                        "Error " + t.getMessage());
            }
        });
    }
}
