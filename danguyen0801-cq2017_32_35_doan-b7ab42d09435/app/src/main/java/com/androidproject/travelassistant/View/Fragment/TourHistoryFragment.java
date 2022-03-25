package com.androidproject.travelassistant.View.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.CommentAdapter;
import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.Tour;
import com.androidproject.travelassistant.AppData.TourAdapter;
import com.androidproject.travelassistant.AppData.TourHistoryAdapter;
import com.androidproject.travelassistant.AppData.Tours;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetHistoryTourRequest;
import com.androidproject.travelassistant.Request.GetHistoryTourResponse;
import com.androidproject.travelassistant.Request.GetTourListResponse;
import com.androidproject.travelassistant.View.TourInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourHistoryFragment extends Fragment {
    private ListView lvTourHistory;
    private ArrayList<Tours> adapter_list = new ArrayList<Tours>();
    private TourHistoryAdapter adapter;
    View mProgressBarFooter;

    private int pageNum = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tour_history, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        mProgressBarFooter = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.tour_view_footer, null, false);

        Toolbar tbTourView = (Toolbar)v.findViewById(R.id.toolbarTourHistory);
        tbTourView.setTitle("History");

        adapter = new TourHistoryAdapter(adapter_list, getActivity());

        lvTourHistory = v.findViewById(R.id.lv_TourHistory);
        lvTourHistory.addFooterView(mProgressBarFooter);
        lvTourHistory.setAdapter(adapter);


        lvTourHistory.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    /** To do code here*/
                    loadTourHistoryPage();
                }
                else if (this.currentVisibleItemCount > 0
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    View v = lvTourHistory.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the top:
                        Toast.makeText(getActivity(), "Refeshed", Toast.LENGTH_SHORT).show();
                        refreshTourHistoryPage();
                        return;
                    }
                    //Do your work

                }
            }
        });

        final AlertDialog get_tour_loading = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(getString(R.string.get_tour_loading))
                .setTheme(R.style.Custom_ProgressDialog_Small)
                .build();

        get_tour_loading.show();

        loadTourHistoryPage();

        get_tour_loading.dismiss();

        adapter.notifyDataSetChanged();

        lvTourHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), TourInfoActivity.class);
                Tours t = adapter_list.get(position);
                intent.putExtra("Tour Id", t.getId());
                intent.putExtra("isHost", t.isHost());

                Toast.makeText(getActivity(), "Id: " + t.getId(), Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
    }

    private void refreshTourHistoryPage() {
        pageNum = 0;
        adapter_list.clear();
        adapter_list = new ArrayList<Tours>();
        adapter.setTourList(adapter_list);

        final AlertDialog get_tour_loading = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(getString(R.string.get_tour_loading))
                .setTheme(R.style.Custom_ProgressDialog_Small)
                .build();

        get_tour_loading.show();

        if (lvTourHistory.getFooterViewsCount() == 0)
            lvTourHistory.addFooterView(mProgressBarFooter);

        loadTourHistoryPage();

        get_tour_loading.dismiss();

        adapter.notifyDataSetChanged();
    }

    private void loadTourHistoryPage() {
        pageNum++;
        Call<GetHistoryTourResponse> call = Global.userService.Get_History(Global.userToken, pageNum, Global.MAX_TOUR_PER_PAGE);
        call.enqueue(new Callback<GetHistoryTourResponse>() {
            @Override
            public void onResponse(Call<GetHistoryTourResponse> call, Response<GetHistoryTourResponse> response) {
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

                    pageNum--;
                    return;
                }



                for(Tours tour : response.body().getTours())
                    adapter_list.add(tour);
                adapter.notifyDataSetChanged();

                if (adapter_list.size() == response.body().getTotal())
                    lvTourHistory.removeFooterView(mProgressBarFooter);
            }

            @Override
            public void onFailure(Call<GetHistoryTourResponse> call, Throwable t) {
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
        if (lvTourHistory.getFooterViewsCount() == 0) {
            refreshTourHistoryPage();
        }
    }
}
