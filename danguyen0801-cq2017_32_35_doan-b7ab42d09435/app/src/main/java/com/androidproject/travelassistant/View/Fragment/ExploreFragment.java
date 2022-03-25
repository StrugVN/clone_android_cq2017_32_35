package com.androidproject.travelassistant.View.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.FeedbackAdapter;
import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.Service;
import com.androidproject.travelassistant.AppData.ServiceAdapter;
import com.androidproject.travelassistant.AppData.StopPoints;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetSuggestedDestinationRequest;
import com.androidproject.travelassistant.Request.GetSuggestedDestinationResponse;
import com.androidproject.travelassistant.Request.SearchServiceResponse;
import com.androidproject.travelassistant.View.ServiceInfoActivity;
import com.androidproject.travelassistant.View.TourProgressActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment {
    private ListView lvService;
    private ArrayList<Service>  serviceAdapter_list;
    private ServiceAdapter serviceAdapter;

    private Button btSearch, btCancel;
    private EditText etSeach;

    private ArrayList<Service>  serviceSearched_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        Toolbar tbTourView = (Toolbar)v.findViewById(R.id.toolbarExplorer);
        tbTourView.setTitle("Explore");

        lvService = v.findViewById(R.id.lv_Explorer);

        GetSuggestedDestinationRequest request = new GetSuggestedDestinationRequest();
        request.setHasOneCoordinate(false);
        request.addToCoordList(23.980056, 85.577677, 23.588665, 163.065945);
        request.addToCoordList(-12.609835, 163.707522, -13.928084, 75.526301);

        Call<GetSuggestedDestinationResponse> call = Global.userService.get_suggested_destination(Global.userToken, request);
        call.enqueue(new Callback<GetSuggestedDestinationResponse>() {
            @Override
            public void onResponse(Call<GetSuggestedDestinationResponse> call, Response<GetSuggestedDestinationResponse> response) {
                if (response.code() != 200) {
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
                    return;
                }
                ArrayList<StopPoints> spList = response.body().getStopPoints();
                if (spList != null && spList.size() > 0) {
                    serviceAdapter_list = new ArrayList<>();

                    for (StopPoints sp : spList)
                    {
                        Service item = new Service(sp.getId(),
                                sp.getName(),
                                sp.getAddress(),
                                sp.getProvinceId(),
                                sp.getLat(),
                                sp.getLongg(),
                                sp.getMinCost(),
                                sp.getMaxCost(),
                                sp.getServiceTypeId(),
                                sp.getAvatar());
                        serviceAdapter_list.add(item);
                    }

                    serviceAdapter = new ServiceAdapter(serviceAdapter_list, getActivity());
                    lvService.setAdapter(serviceAdapter);
                    serviceAdapter.notifyDataSetChanged();

                    lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long idList) {
                            Intent new_intent = new Intent(getActivity().getApplicationContext(), ServiceInfoActivity.class);
                            new_intent.putExtra("serviceId", serviceAdapter_list.get(position).getId());
                            /*new_intent.putExtra("avatar", serviceAdapter_list.get(position).getAvatar());
                            new_intent.putExtra("name", serviceAdapter_list.get(position).getName());
                            new_intent.putExtra("addr", serviceAdapter_list.get(position).getAddress());
                            new_intent.putExtra("min", serviceAdapter_list.get(position).getMinCost());
                            new_intent.putExtra("max", serviceAdapter_list.get(position).getMaxCost());
                            new_intent.putExtra("serviceTypeId", serviceAdapter_list.get(position).getServiceTypeId());*/
                            startActivity(new_intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetSuggestedDestinationResponse> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Unexpected Error " + t.getMessage() + ", retrying", Toast.LENGTH_LONG).show();
                Log.d(getActivity().getClass().getSimpleName(),
                        "Unexpected Error " + t.getMessage());
            }
        });

        btSearch = v.findViewById(R.id.btSearchService);
        btCancel = v.findViewById(R.id.btCancleSearchService);

        etSeach = v.findViewById(R.id.etSearchService);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKey = etSeach.getText().toString();

                if(TextUtils.isEmpty(searchKey))
                    return;

                Call<SearchServiceResponse> call = Global.userService.Get_Search_Service(Global.userToken, searchKey, 1, 10000);

                call.enqueue(new Callback<SearchServiceResponse>() {
                    @Override
                    public void onResponse(Call<SearchServiceResponse> call, Response<SearchServiceResponse> response) {
                        if(response.code()!=200) {
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());
                                Toast.makeText(getActivity(),
                                        "Error: " + error.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        serviceSearched_list = response.body().getStopPoints();

                        if(serviceSearched_list != null){
                            serviceAdapter = new ServiceAdapter(serviceSearched_list, getActivity());
                            lvService.setAdapter(serviceAdapter);

                            lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent new_intent = new Intent(getActivity().getApplicationContext(), ServiceInfoActivity.class);
                                    new_intent.putExtra("serviceId", serviceSearched_list.get(position).getId());

                                    startActivity(new_intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchServiceResponse> call, Throwable t) {
                        Toast.makeText(getActivity(),
                                "Unexpected error: " + t.toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TourProgressActivity.class.getSimpleName(), t.getMessage());
                    }
                });
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSeach.setText("");
            }
        });

        etSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s))
                    btCancel.setVisibility(View.VISIBLE);
                else {
                    serviceAdapter = new ServiceAdapter(serviceAdapter_list, getActivity());
                    lvService.setAdapter(serviceAdapter);

                    lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent new_intent = new Intent(getActivity().getApplicationContext(), ServiceInfoActivity.class);
                            new_intent.putExtra("serviceId", serviceAdapter_list.get(position).getId());

                            startActivity(new_intent);
                        }
                    });

                    btCancel.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
