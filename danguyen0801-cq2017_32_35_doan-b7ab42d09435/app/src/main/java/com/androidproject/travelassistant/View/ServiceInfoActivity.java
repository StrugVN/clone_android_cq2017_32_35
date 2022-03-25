package com.androidproject.travelassistant.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.FeedbackAdapter;
import com.androidproject.travelassistant.AppData.Feedbacks;
import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.ReviewPoint;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetListFeedbackResponse;
import com.androidproject.travelassistant.Request.GetPointFeedbackResponse;
import com.androidproject.travelassistant.Request.GetServiceDetailRequest;
import com.androidproject.travelassistant.Request.GetServiceDetailResponse;
import com.androidproject.travelassistant.Utility.DownloadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceInfoActivity extends AppCompatActivity {
    private EditText etName, etAddr, etMin, etMax, etContact;
    private ImageView ivAvt;
    private Spinner sServiceType;

    private TextView tvScore;
    private RatingBar rbScore, rbSetScore;

    private ListView lvFeedback;
    private ArrayList<Feedbacks> feedbackAdapter_list;
    private FeedbackAdapter feedbackAdapter;

    private int serviceId;
    private static final int SendFeedbackCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info);

        Toolbar tbSPInfo = (Toolbar)findViewById(R.id.toolbarServiceInfo);
        tbSPInfo.setTitle("Service details");
        setSupportActionBar(tbSPInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etName = findViewById(R.id.ServiceInfo_Name);
        etAddr = findViewById(R.id.etAddress_ServiceInfo);
        etMin = findViewById(R.id.etMinCost_ServiceInfo);
        etMax = findViewById(R.id.etMaxCost_ServiceInfo);
        sServiceType = findViewById(R.id.sType_ServiceInfo);
        String[] items = new String[]{"Restaurant", "Hotel", "Rest Station", "Other"};
        ArrayAdapter<String> _adapter = new ArrayAdapter<>(ServiceInfoActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        sServiceType.setAdapter(_adapter);
        sServiceType.setEnabled(false);
        ivAvt = findViewById(R.id.iv_ServiceInfoAvt);

        tvScore = findViewById(R.id.tv_AvgReviewServiceInfo);
        rbScore = findViewById(R.id.ratingBar_AvgReviewServiceInfo);

        rbSetScore = findViewById(R.id.ratingBar_ServiceInfo);

        lvFeedback = findViewById(R.id.lv_review_ServiceInfo);

        serviceId = getIntent().getIntExtra("serviceId", -1);
        if (serviceId == -1){
            Toast.makeText(this, "Error: No service id received - Closing", Toast.LENGTH_SHORT).show();
            finish();
        }
        Toast.makeText(this, String.valueOf(serviceId), Toast.LENGTH_LONG).show();

        loadServiceDetails();

        rbSetScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent new_intent = new Intent(ServiceInfoActivity.this, ReviewTourActivity.class);
                new_intent.putExtra("Rated", rating);
                new_intent.putExtra("serviceId", serviceId);
                new_intent.putExtra("type", "feedback");
                startActivityForResult(new_intent, SendFeedbackCode);
            }
        });

        loadFeedbackService();
        feedbackStat();
    }

    public void loadServiceDetails() {
        Call<GetServiceDetailResponse> call = Global.userService.Get_Service_Detail(Global.userToken, serviceId);
        call.enqueue(new Callback<GetServiceDetailResponse>() {
            @Override
            public void onResponse(Call<GetServiceDetailResponse> call, Response<GetServiceDetailResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ServiceInfoActivity.this,
                                "Error, please try again", Toast.LENGTH_LONG).show();
                        Log.d(ServiceInfoActivity.class.getSimpleName() + "get_service_failed",
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    return;
                }
                if(!TextUtils.isEmpty(response.body().getName()))
                    etName.setText(response.body().getName());
                else
                    etName.setText("None");
                if(!TextUtils.isEmpty(response.body().getAddress()))
                    etAddr.setText(response.body().getAddress());
                else
                    etAddr.setText("None");
                etMin.setText(String.valueOf(response.body().getMinCost()));
                etMax.setText(String.valueOf(response.body().getMaxCost()));
                sServiceType.setSelection(response.body().getServiceTypeId() - 1);
                if(!TextUtils.isEmpty(response.body().getAvatar())) {
                    new DownloadImageTask(ivAvt).execute(response.body().getAvatar());
                }
                else {
                    if (response.body().getServiceTypeId() == 1)
                        ivAvt.setImageDrawable(ServiceInfoActivity.this.getDrawable(R.drawable.ic_sp_restaurant));
                    if (response.body().getServiceTypeId() == 2)
                        ivAvt.setImageDrawable(ServiceInfoActivity.this.getDrawable(R.drawable.ic_sp_hotel));
                    if (response.body().getServiceTypeId() == 3)
                        ivAvt.setImageDrawable(ServiceInfoActivity.this.getDrawable(R.drawable.ic_sp_rest));
                }
            }

            @Override
            public void onFailure(Call<GetServiceDetailResponse> call, Throwable t) {
                Toast.makeText(ServiceInfoActivity.this,
                    "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TourInfoActivity.class.getSimpleName() + " getService", t.getMessage());
                finish();
            }
        });
    }

    void feedbackStat() {
        Call <GetPointFeedbackResponse> call = Global.userService.Get_Point_Feedback(Global.userToken, serviceId);
        call.enqueue(new Callback<GetPointFeedbackResponse>() {
            @Override
            public void onResponse(Call<GetPointFeedbackResponse> call, Response<GetPointFeedbackResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ServiceInfoActivity.this,
                                "Error loading feedback score, please try again", Toast.LENGTH_LONG).show();
                        Log.d(ServiceInfoActivity.class.getSimpleName() + " load_Feedback_Score_Failed",
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                float total = 0;
                float point = 0;
                for (ReviewPoint stat : response.body().getPointStat()){
                    total += stat.getTotal();
                    point += stat.getPoint()*stat.getTotal();
                }

                float score = point/total;

                rbScore.setRating(score);
                tvScore.setText(String.valueOf(Math.round(score*10.0)/10.0));
            }

            @Override
            public void onFailure(Call<GetPointFeedbackResponse> call, Throwable t) {
                Toast.makeText(ServiceInfoActivity.this,
                        "Unexpected Error while loading feedback score", Toast.LENGTH_LONG).show();
                Log.d(TourInfoActivity.class.getSimpleName() + " load_Feedback_Score_Failed",
                        t.getMessage());
            }
        });
    }

    void loadFeedbackService() {
        Call<GetListFeedbackResponse> call = Global.userService.Get_Feedbacks(Global.userToken, serviceId, 1, "10000");
        call.enqueue(new Callback<GetListFeedbackResponse>() {
            @Override
            public void onResponse(Call<GetListFeedbackResponse> call, Response<GetListFeedbackResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ServiceInfoActivity.this,
                                "Error loading feedback, please try again", Toast.LENGTH_LONG).show();
                        Log.d(ServiceInfoActivity.class.getSimpleName() + " load_Feedback_Failed",
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                feedbackAdapter_list = response.body().getFeedbacks();

                if(feedbackAdapter_list != null && feedbackAdapter_list.size() > 0) {
                    feedbackAdapter = new FeedbackAdapter(feedbackAdapter_list, ServiceInfoActivity.this);

                    lvFeedback.setAdapter(feedbackAdapter);
                    feedbackAdapter.notifyDataSetChanged();

                    lvFeedback.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    // Disallow ScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    break;

                                case MotionEvent.ACTION_UP:
                                    // Allow ScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                    break;
                            }
                            v.onTouchEvent(event);
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetListFeedbackResponse> call, Throwable t) {
                Toast.makeText(ServiceInfoActivity.this,
                        "Unexpected Error while loading feedback", Toast.LENGTH_LONG).show();
                Log.d(ServiceInfoActivity.class.getSimpleName() + " load_Feedback_Failed",
                        t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SendFeedbackCode) {
            if (resultCode == Activity.RESULT_OK) {
                loadFeedbackService();
                feedbackStat();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.slide_in_down, R.animator.slide_out_down);
    }
}
