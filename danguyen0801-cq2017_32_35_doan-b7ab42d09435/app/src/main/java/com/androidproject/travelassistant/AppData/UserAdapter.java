package com.androidproject.travelassistant.AppData;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.AddCommentRequest;
import com.androidproject.travelassistant.Request.AddMemberRequest;
import com.androidproject.travelassistant.Request.AddMemberResponse;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.View.TourInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends BaseAdapter {
    private ArrayList<User> userList;
    private Context context;
    private int tourId;
    private boolean isPrivate;
    ArrayList<Members> member_list;

    public UserAdapter(ArrayList<User> userList, Context context, int tourId, boolean isPrivate, ArrayList<Members> members) {
        this.userList = userList;
        this.context = context;
        this.tourId = tourId;
        this.isPrivate = isPrivate;
        member_list = members;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.user_view, null);

        TextView tvName = v.findViewById(R.id.tv_nameUser);
        TextView tvEmail = v.findViewById(R.id.tv_emailUser);
        TextView tvPhone = v.findViewById(R.id.tv_phoneUser);
        TextView tvDob = v.findViewById(R.id.tv_dobUser);
        TextView tvGender = v.findViewById(R.id.tv_genderUser);
        ImageView ivAvatar = v.findViewById(R.id.iv_avtUser);

        final TextView tvAddMember = v.findViewById(R.id.tv_addMember);

        final User user = userList.get(position);

        tvName.setText(user.getFullName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        if (!TextUtils.isEmpty(user.getDob()))
            tvDob.setText(user.getDob().substring(0,10));
        if(user.getGender() == 1)
            tvGender.setText("Male");
        else
            tvGender.setText("Female");

        if (!TextUtils.isEmpty(user.getAvatar()))
            new DownloadImageTask(ivAvatar).execute(user.getAvatar());
        else {
            if (user.getGender() == 1)
                ivAvatar.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_blank_user_avatar_male));
            else
                ivAvatar.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_blank_user_avatar_female));
        }


        tvAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện call thêm thành viên ở đây (xóa cái toast)
                final AddMemberRequest request = new AddMemberRequest();

                request.setTourId(tourId);
                request.setInvited(true);
                request.setInviteUserId(user.getId());

                Call<AddMemberResponse> call = Global.userService.Add_Member(Global.userToken, request);
                call.enqueue(new Callback<AddMemberResponse>() {
                    @Override
                    public void onResponse(Call<AddMemberResponse> call, Response<AddMemberResponse> response) {
                        if(response.code()!=200) {
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());
                                Utility.showErrorDialog(context, "Error" + response.code(),
                                        error.getString("message"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        tvAddMember.setText("Invited");
                        tvAddMember.setTextColor(Color.parseColor("#FF0000"));
                        tvAddMember.setClickable(false);

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<AddMemberResponse> call, Throwable t) {
                        Utility.showErrorDialog(context, "Error", t.getMessage());
                    }
                });

            }
        });

        for(int i=0; i<member_list.size(); i++){
            if(user.getId() == member_list.get(i).getId()){
                tvAddMember.setText("Already a member");
                tvAddMember.setTextColor(Color.parseColor("#FF0000"));
                tvAddMember.setClickable(false);
                tvAddMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

        return v;
    }
}
