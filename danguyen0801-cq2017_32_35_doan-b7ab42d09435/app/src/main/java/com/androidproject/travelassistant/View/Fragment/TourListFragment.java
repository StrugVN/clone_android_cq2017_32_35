package com.androidproject.travelassistant.View.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.Tour;
import com.androidproject.travelassistant.AppData.TourAdapter;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetTourListResponse;
import com.androidproject.travelassistant.View.CreateTourActivity;
import com.androidproject.travelassistant.View.TourInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TourListFragment extends Fragment {
    private ArrayList<Tour> adapter_list = new ArrayList<Tour>();
    private ListView lvTour;
    private TourAdapter adapter;
    View mProgressBarFooter;

    private int pageNum = 0;

    private BottomNavigationView nav;

    boolean doubleBackToExitPressedOnce = false;

    boolean loadedAllPage = false;

    EditText etSearchTour;
    Button btSearch, btClearSearch;
    private ArrayList<Tour> searched_list = new ArrayList<Tour>();
    boolean searchMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tour_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        mProgressBarFooter = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.tour_view_footer, null, false);

        Toolbar tbTourView = v.findViewById(R.id.toolbar_Tour);
        tbTourView.setTitle("Tour List");

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateTourActivity.class));
                getActivity().overridePendingTransition(R.animator.slide_in_up, R.animator.slide_out_up);
            }
        });

        adapter = new TourAdapter(adapter_list, getActivity());

        lvTour = v.findViewById(R.id.lv_Tour);
        lvTour.addFooterView(mProgressBarFooter);
        lvTour.setAdapter(adapter);

        lvTour.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    loadTourPage();
                }
                else if (this.currentVisibleItemCount > 0
                            && this.currentScrollState == SCROLL_STATE_IDLE) {
                        View v = lvTour.getChildAt(0);
                        int offset = (v == null) ? 0 : v.getTop();
                        if (offset == 0 && !searchMode) {
                            // reached the top:
                            Toast.makeText(getActivity(), "Refeshed", Toast.LENGTH_SHORT).show();
                            refreshTourPage();
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
                .setCancelable(false)
                .build();

        get_tour_loading.show();

        loadTourPage();

        get_tour_loading.dismiss();

        adapter.notifyDataSetChanged();

        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), TourInfoActivity.class);
                Tour t = adapter_list.get(position);
                intent.putExtra("Tour Id", t.getId());

                startActivity(intent);
            }
        });

        etSearchTour = v.findViewById(R.id.etSearchTour);
        btSearch = v.findViewById(R.id.btSearchTour);
        btClearSearch = v.findViewById(R.id.btCancleSearch);

        btClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchTour.setText("");
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchKey = etSearchTour.getText().toString().toLowerCase();

                if(TextUtils.isEmpty(searchKey)) return;

                final AlertDialog searching_tour = new SpotsDialog.Builder()
                        .setContext(getActivity())
                        .setMessage("Searching...")
                        .setTheme(R.style.Custom_ProgressDialog)
                        .build();

                searching_tour.show();

                if (!loadedAllPage) {
                    Call<GetTourListResponse> call = Global.userService.get_tour(Global.userToken,
                            10000,
                            1,
                            "name",
                            true);

                    call.enqueue(new Callback<GetTourListResponse>() {
                        @Override
                        public void onResponse(Call<GetTourListResponse> call, Response<GetTourListResponse> response) {
                            if (response.code() != 200) {
                                if (response.code() == 500) {
                                    Toast.makeText(getContext(),
                                            getString(R.string.get_tour_failed), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(),
                                            "Unknown error, code: " + response.code(), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.d(TourListFragment.class.getSimpleName()
                                            + " unknown " + response.code(), response.errorBody().string());
                                } catch (IOException e) {
                                    Log.d(TourListFragment.class.getSimpleName(), e.getMessage());
                                }
                                return;
                            }

                            ArrayList<Tour> new_list = response.body().getTourList();

                            for (Tour x : new_list) {
                                adapter_list.add(x);
                            }


                            lvTour.removeFooterView(mProgressBarFooter);
                            loadedAllPage = true;

                            searched_list.clear();
                            searched_list = new ArrayList<>();

                            for (int i = 0; i < adapter_list.size(); i++) {
                                if (!TextUtils.isEmpty(adapter_list.get(i).getName())
                                        && adapter_list.get(i).getName().toLowerCase().contains(searchKey))
                                    searched_list.add(adapter_list.get(i));
                            }

                            Toast.makeText(getActivity(), "Found: " + searched_list.size(), Toast.LENGTH_SHORT).show();

                            adapter = new TourAdapter(searched_list, getActivity());
                            lvTour.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            searchMode = true;

                            lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), TourInfoActivity.class);
                                    Tour t = searched_list.get(position);
                                    intent.putExtra("Tour Id", t.getId());

                                    startActivity(intent);
                                }
                            });

                            searching_tour.dismiss();
                        }

                        @Override
                        public void onFailure(Call<GetTourListResponse> call, Throwable t) {
                            Toast.makeText(getContext(),
                                    "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TourListFragment.class.getSimpleName() + " loadTourPage", t.getMessage());
                        }
                    });
                }
                else{

                    searched_list.clear();
                    searched_list = new ArrayList<>();

                    for (int i = 0; i < adapter_list.size(); i++) {
                        if (!TextUtils.isEmpty(adapter_list.get(i).getName())
                                && adapter_list.get(i).getName().toLowerCase().contains(searchKey))
                            searched_list.add(adapter_list.get(i));
                    }

                    Toast.makeText(getActivity(), "Found: " + searched_list.size(), Toast.LENGTH_SHORT).show();

                    adapter = new TourAdapter(searched_list, getActivity());
                    lvTour.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    searchMode = true;

                    lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), TourInfoActivity.class);
                            Tour t = searched_list.get(position);
                            intent.putExtra("Tour Id", t.getId());

                            Toast.makeText(getActivity(), "Id: " + t.getId(), Toast.LENGTH_SHORT).show();

                            startActivity(intent);
                        }
                    });

                    searching_tour.dismiss();
                }
            }
        });



        etSearchTour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    adapter = new TourAdapter(adapter_list, getActivity());
                    lvTour.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    searchMode = false;

                    lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), TourInfoActivity.class);
                            Tour t = adapter_list.get(position);
                            intent.putExtra("Tour Id", t.getId());

                            startActivity(intent);
                        }
                    });

                    btClearSearch.setVisibility(View.INVISIBLE);
                }
                else{
                    btClearSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadTourPage(){
        if(loadedAllPage) return;
        pageNum++;
        Call<GetTourListResponse> call = Global.userService.get_tour(Global.userToken,
                Global.MAX_TOUR_PER_PAGE,
                pageNum,
                "name",
                true);

        call.enqueue(new Callback<GetTourListResponse>() {
            @Override
            public void onResponse(Call<GetTourListResponse> call, Response<GetTourListResponse> response) {
                if(response.code() != 200){
                    if (response.code() == 500){
                        Toast.makeText(getContext(),
                                getString(R.string.get_tour_failed), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(),
                                "Unknow" +
                                        "n error, code: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        Log.d(TourListFragment.class.getSimpleName()
                                + " unknown " + response.code(), response.errorBody().string());
                    }catch(IOException e){
                        Log.d(TourListFragment.class.getSimpleName(), e.getMessage());
                    }
                    pageNum--;
                    return;
                }

                ArrayList<Tour> new_list = response.body().getTourList();

                for ( Tour x : new_list) {
                    adapter_list.add(x);
                }

                if (adapter_list.size() == response.body().getTotal()) {
                    lvTour.removeFooterView(mProgressBarFooter);
                    loadedAllPage = true;
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetTourListResponse> call, Throwable t) {
                pageNum--;
                Toast.makeText(getContext(),
                        "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TourListFragment.class.getSimpleName() + " loadTourPage", t.getMessage());
            }
        });
    }

    private void refreshTourPage(){
        pageNum = 0;
        loadedAllPage = false;

        if(lvTour.getFooterViewsCount() == 0)
            lvTour.addFooterView(mProgressBarFooter);

        adapter_list.clear();
        adapter_list = new ArrayList<Tour>();
        adapter.setTourList(adapter_list);

        final AlertDialog get_tour_loading = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(getString(R.string.get_tour_loading))
                .setTheme(R.style.Custom_ProgressDialog_Small)
                .build();

        get_tour_loading.show();

        loadTourPage();

        get_tour_loading.dismiss();

        adapter.notifyDataSetChanged();
    }
}
