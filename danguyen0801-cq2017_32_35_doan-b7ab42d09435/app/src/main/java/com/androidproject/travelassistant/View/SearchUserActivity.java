package com.androidproject.travelassistant.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.Members;
import com.androidproject.travelassistant.AppData.ReviewAdapter;
import com.androidproject.travelassistant.AppData.User;
import com.androidproject.travelassistant.AppData.UserAdapter;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.SearchUserResponse;
import com.androidproject.travelassistant.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity extends AppCompatActivity {
    ListView lvUser;
    ArrayList<User> adapter_list = new ArrayList<>();
    ArrayList<User> adapter_searched_list = new ArrayList<>();
    UserAdapter adapter;
    ArrayList<Members> member_list;

    int tourId;
    boolean isPrivate;

    EditText etSearch;
    Button btSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        tourId = getIntent().getIntExtra("Id", -1);
        isPrivate = getIntent().getBooleanExtra("isPrivate", false);
        member_list = (ArrayList<Members>)getIntent().getSerializableExtra("members");

        if (tourId == -1){
            Toast.makeText(this, "Error: No tour id received - Closing", Toast.LENGTH_SHORT).show();
            finish();
        }

        adapter = new UserAdapter(adapter_list, SearchUserActivity.this, tourId, isPrivate, member_list);

        lvUser = findViewById(R.id.lvUserList);

        etSearch = findViewById(R.id.etSearchUser);

        lvUser.setAdapter(adapter);

        btSearch = findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchUserActivity.this, "Search for \""  + etSearch.getText().toString() + "\""  , Toast.LENGTH_SHORT).show();
                Call<SearchUserResponse> call = Global.userService.get_search_user(etSearch.getText().toString(),1, "10000");
                call.enqueue(new Callback<SearchUserResponse>() {
                    @Override
                    public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {

                        if (response.code() != 200) {
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());
                                Toast.makeText(SearchUserActivity.this,
                                        "Error searching, please try again", Toast.LENGTH_LONG).show();
                                Log.d(TourInfoActivity.class.getSimpleName() + " load_User_Failed",
                                        "Code " + response.code() + ": " + error.getString("message"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return;
                        }
                        adapter_list.clear();
                        adapter_list = response.body().getUsers();
                        adapter = new UserAdapter(adapter_list, SearchUserActivity.this, tourId, isPrivate, member_list);
                        lvUser.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                        Utility.showErrorDialog(SearchUserActivity.this, "Unexpected Error", t.getMessage());
                    }
                });
            }
        });

        /*
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Chưa biết đây làm gì nữa
                User u = adapter_list.get(position);
                Toast.makeText(SearchUserActivity.this,
                        "User name: \"" + u.getFullName() + "\", id: " + u.getId(), Toast.LENGTH_SHORT).show();
            }
        });

         */
    }
}
