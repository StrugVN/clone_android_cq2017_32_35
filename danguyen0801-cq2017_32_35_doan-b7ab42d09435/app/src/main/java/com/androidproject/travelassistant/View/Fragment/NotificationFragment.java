package com.androidproject.travelassistant.View.Fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.InvitationAdapter;
import com.androidproject.travelassistant.AppData.Tours_Invitation;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetInvitationUsersResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {
    ListView lvInv;
    ArrayList<Tours_Invitation> adapter_list = new ArrayList<>();
    InvitationAdapter adapter;

    Timer selfRefesh = new Timer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        lvInv = v.findViewById(R.id.lv_Noti);

        selfRefesh.scheduleAtFixedRate(
                new TimerTask()
                {
                    public void run()
                    {
                        loadInvite();
                    }
                },
                0,
                10000);
    }

    private void loadInvite(){
        adapter_list.clear();

        Call<GetInvitationUsersResponse> call = Global.userService.Get_Invitation(Global.userToken, 1, "10000");

        call.enqueue(new Callback<GetInvitationUsersResponse>() {
            @Override
            public void onResponse(Call<GetInvitationUsersResponse> call, Response<GetInvitationUsersResponse> response) {
                if(response.code()!=200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity(),
                                "Error " + response.code() + ", retrying", Toast.LENGTH_LONG).show();
                        Log.d(getActivity().getClass().getSimpleName(),
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter_list = response.body().getTours();

                if(adapter_list != null && adapter_list.size() != 0){
                    adapter = new InvitationAdapter(adapter_list, getActivity());

                    lvInv.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<GetInvitationUsersResponse> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Error " + t.getMessage() + ", retrying", Toast.LENGTH_LONG).show();
                Log.d(getActivity().getClass().getSimpleName(),
                        "Error " + t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInvite();
    }
}
