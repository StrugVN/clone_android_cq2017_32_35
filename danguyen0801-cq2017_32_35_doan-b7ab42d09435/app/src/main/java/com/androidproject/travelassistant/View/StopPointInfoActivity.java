package com.androidproject.travelassistant.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.FeedbackAdapter;
import com.androidproject.travelassistant.AppData.Feedbacks;
import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.Object;
import com.androidproject.travelassistant.AppData.ReviewPoint;
import com.androidproject.travelassistant.AppData.StopPoints;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.AddStopPointRequest;
import com.androidproject.travelassistant.Request.AddStopPointResponse;
import com.androidproject.travelassistant.Request.GetListFeedbackResponse;
import com.androidproject.travelassistant.Request.GetPointFeedbackResponse;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.Utility.showDateTimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Integer.valueOf;

public class StopPointInfoActivity extends AppCompatActivity {
    private EditText etName, etAddr, etMin, etMax, etArrive, etLeave;
    private Button bSave;
    private ImageButton ibArrive, ibLeave, ibEdit;
    private ImageView ivAvt;
    private Spinner sServiceType;

    private RatingBar rbScore;
    private TextView tvScore;

    private RatingBar rbSetStar;

    private ListView lvFeedback;
    private ArrayList<Feedbacks> feedbackAdapter_list;
    private FeedbackAdapter feedbackAdapter;

    private int tourId, id;
    private long arriveTime, leaveTime;
    private String name, addr, avt;
    private int min, max, serviceTypeId, serviceId;
    private double Lat, Lng;
    private Calendar arrive, leave;

    private static final int SendFeedbackCode = 1;

    boolean avtFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point_info);

        Toolbar tbSPInfo = (Toolbar)findViewById(R.id.toolbarStopPointInfo);
        tbSPInfo.setTitle("Stop point details");
        setSupportActionBar(tbSPInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sServiceType = findViewById(R.id.sType_StopPointInfo);
        String[] items = new String[]{"Restaurant", "Hotel", "Rest Station", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(StopPointInfoActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        sServiceType.setAdapter(adapter);

        etName = findViewById(R.id.StopPointInfo_Name);
        etAddr = findViewById(R.id.etAddress_StopPointInfo);
        etMin = findViewById(R.id.etMinCost_StopPointInfo);
        etMax = findViewById(R.id.etMaxCost_StopPointInfo);
        etArrive = findViewById(R.id.etArrive_StopPointInfo);
        etArrive.setClickable(false);
        etArrive.setFocusable(false);
        etLeave = findViewById(R.id.etLeave_StopPointInfo);
        etLeave.setClickable(false);
        etLeave.setFocusable(false);
        ivAvt = findViewById(R.id.iv_StopPointInfoAvt);

        bSave = findViewById(R.id.bSaveUpdate_StopPointInfo);
        ibEdit = findViewById(R.id.StopPointInfo_EditStopPointButton);
        ibArrive = findViewById(R.id.btArrive_StopPointInfo);
        ibLeave = findViewById(R.id.btLeave_StopPointInfo);

        rbScore = findViewById(R.id.ratingBar_AvgReviewStopPointInfo);
        tvScore = findViewById(R.id.tv_AvgReviewStopPointInfo);

        rbSetStar = findViewById(R.id.ratingBar_StopPointInfo);

        lvFeedback = findViewById(R.id.lv_review_StopPointInfo);

        tourId = getIntent().getIntExtra("tourId", -1);
        if (tourId == -1){
            Toast.makeText(this, "Error: No tour id received - Closing", Toast.LENGTH_SHORT).show();
            finish();
        }

        Object source = (Object)getIntent().getSerializableExtra("stopPoint");
        id = source.getId();
        name = source.getName();
        addr = source.getAddress();
        arriveTime = source.getArrivalAt();
        leaveTime = source.getLeaveAt();
        min = source.getMinCost();
        max = source.getMaxCost();
        serviceId = source.getServiceId();
        serviceTypeId = source.getServiceTypeId();
        Lat = source.getLat();
        Lng = source.getLongg();
        avt = source.getAvatar();

        if(!TextUtils.isEmpty(source.getAvatar())){
            avtFlag = true;
            new DownloadImageTask(ivAvt).execute(source.getAvatar());
        }
        else{
            if (serviceTypeId == 1)
                ivAvt.setImageDrawable(StopPointInfoActivity.this.getDrawable(R.drawable.ic_sp_restaurant));
            if (serviceTypeId == 2)
                ivAvt.setImageDrawable(StopPointInfoActivity.this.getDrawable(R.drawable.ic_sp_hotel));
            if (serviceTypeId == 3)
                ivAvt.setImageDrawable(StopPointInfoActivity.this.getDrawable(R.drawable.ic_sp_rest));
        }

        if(TextUtils.isEmpty(name))
            etName.setText("None");
        else
            etName.setText(name);

        etAddr.setText(addr);
        etArrive.setText(Utility.MillisToDate(arriveTime));
        etLeave.setText(Utility.MillisToDate(leaveTime));
        etMin.setText(String.valueOf(min));
        etMax.setText(String.valueOf(max));
        sServiceType.setSelection(serviceTypeId - 1);

        editDisabled();

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEnabled();
            }
        });

        ibArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDateTimePicker(StopPointInfoActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        arrive = calendar;

                        etArrive.setText(Utility.MillisToDate(arrive.getTimeInMillis()));
                    }
                }).run();
            }
        });

        ibLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDateTimePicker(StopPointInfoActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        leave = calendar;

                        etLeave.setText(Utility.MillisToDate(leave.getTimeInMillis()));
                    }
                }).run();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStopPoint();
            }
        });

        rbSetStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent new_intent = new Intent(StopPointInfoActivity.this, ReviewTourActivity.class);
                new_intent.putExtra("Rated", rating);
                new_intent.putExtra("serviceId", serviceId);
                new_intent.putExtra("type", "feedback");
                startActivityForResult(new_intent, SendFeedbackCode);
            }
        });

        if(!getIntent().getBooleanExtra("isHost", false))
            ibEdit.setVisibility(View.INVISIBLE);

        loadFeedbackService();

        feedbackStat();
    }

    void updateStopPoint()
    {
        final AlertDialog update_stop_point_loading = new SpotsDialog.Builder()
                .setContext(StopPointInfoActivity.this)
                .setMessage(getString(R.string.update_stop_point_loading))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        update_stop_point_loading.show();

        boolean cancel = false;
        if(TextUtils.isEmpty(etName.getText().toString())){
            cancel = true;
            etName.setError(getString(R.string.create_tour_error_name_empty));
        }
        if (TextUtils.isEmpty(etAddr.getText().toString())){
            cancel = true;
            etAddr.setError(getString(R.string.add_stop_point_error_addr_empty));
        }
        if(cancel){
            update_stop_point_loading.dismiss();
            return;
        }
        else{
            String _name, _addr, _arrive, _leave;
            int _min, _max;
            final int _serviceTypeId;

            _name = etName.getText().toString();
            _addr = etAddr.getText().toString();
            _arrive = etArrive.getText().toString();
            _leave = etLeave.getText().toString();
            if (!TextUtils.isEmpty(etMin.getText().toString()))
                _min = valueOf(etMin.getText().toString());
            else
                _min = 0;

            if(!TextUtils.isEmpty(etMax.getText().toString()))
                _max = valueOf(etMax.getText().toString());
            else
                _max = 0;

            final AddStopPointRequest request = new AddStopPointRequest();
            request.setTourId(tourId);

            ArrayList<StopPoints> stopPoints = new ArrayList<>();
            StopPoints point = new StopPoints();

            point.setId(id);
            point.setName(_name);
            point.setMaxCost(_max);
            point.setMinCost(_min);
            point.setLat(Lat);
            point.setLongg(Lng);
            point.setArrivalAt(Utility.DateToMillis(_arrive));
            point.setLeaveAt(Utility.DateToMillis(_leave));
            _serviceTypeId = sServiceType.getSelectedItemPosition() + 1;
            point.setServiceTypeId(_serviceTypeId);
            point.setAddress(_addr);

            stopPoints.add(point);
            request.setStopPoints(stopPoints);

            Call<AddStopPointResponse> call = Global.userService.Add_Stop_Point(Global.userToken, request);
            call.enqueue(new Callback<AddStopPointResponse>() {
                @Override
                public void onResponse(Call<AddStopPointResponse> call, Response<AddStopPointResponse> response) {
                    if (response.code() != 200) {
                        try {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Toast.makeText(StopPointInfoActivity.this,
                                    "Error, please try again", Toast.LENGTH_LONG).show();
                            Log.d(StopPointInfoActivity.class.getSimpleName() + "update_stop_point_failed",
                                    "Code " + response.code() + ": " + error.getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        update_stop_point_loading.dismiss();
                        return;
                    }

                    update_stop_point_loading.dismiss();
                    if(!avtFlag) {
                        if (_serviceTypeId == 1)
                            ivAvt.setImageDrawable(StopPointInfoActivity.this.getDrawable(R.drawable.ic_sp_restaurant));
                        if (_serviceTypeId == 2)
                            ivAvt.setImageDrawable(StopPointInfoActivity.this.getDrawable(R.drawable.ic_sp_hotel));
                        if (_serviceTypeId == 3)
                            ivAvt.setImageDrawable(StopPointInfoActivity.this.getDrawable(R.drawable.ic_sp_rest));
                    }
                    editDisabled();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                }

                @Override
                public void onFailure(Call<AddStopPointResponse> call, Throwable t) {
                    Toast.makeText(StopPointInfoActivity.this,
                            "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TourInfoActivity.class.getSimpleName() + " updateStopPoint", t.getMessage());
                }
            });
        }
    }

    void feedbackStat() {
        Call <GetPointFeedbackResponse> call = Global.userService.Get_Point_Feedback(Global.userToken, serviceId);
        call.enqueue(new Callback<GetPointFeedbackResponse>() {
            @Override
            public void onResponse(Call<GetPointFeedbackResponse> call, Response<GetPointFeedbackResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(StopPointInfoActivity.this,
                                "Error loading feedback score, please try again", Toast.LENGTH_LONG).show();
                        Log.d(StopPointInfoActivity.class.getSimpleName() + " load_Feedback_Score_Failed",
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
                Toast.makeText(StopPointInfoActivity.this,
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
                        Toast.makeText(StopPointInfoActivity.this,
                                "Error loading feedback, please try again", Toast.LENGTH_LONG).show();
                        Log.d(StopPointInfoActivity.class.getSimpleName() + " load_Feedback_Failed",
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
                    feedbackAdapter = new FeedbackAdapter(feedbackAdapter_list, StopPointInfoActivity.this);

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

                            // Handle ListView touch events.
                            v.onTouchEvent(event);
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetListFeedbackResponse> call, Throwable t) {
                Toast.makeText(StopPointInfoActivity.this,
                        "Unexpected Error while loading feedback", Toast.LENGTH_LONG).show();
                Log.d(TourInfoActivity.class.getSimpleName() + " load_Feedback_Failed",
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

    void editEnabled() {
        etName.setClickable(true);
        etName.setFocusable(true);
        etName.setFocusableInTouchMode(true);
        etName.requestFocus();
        etAddr.setClickable(true);
        etAddr.setFocusable(true);
        etAddr.setFocusableInTouchMode(true);
        etMin.setClickable(true);
        etMin.setFocusable(true);
        etMin.setFocusableInTouchMode(true);
        etMax.setClickable(true);
        etMax.setFocusable(true);
        etMax.setFocusableInTouchMode(true);
        sServiceType.setEnabled(true);

        ibArrive.setVisibility(VISIBLE);
        ibLeave.setVisibility(VISIBLE);
        bSave.setVisibility(VISIBLE);
    }

    void editDisabled() {
        etName.setClickable(false);
        etName.setFocusable(false);
        etAddr.setClickable(false);
        etAddr.setFocusable(false);
        etMin.setClickable(false);
        etMin.setFocusable(false);
        etMax.setClickable(false);
        etMax.setFocusable(false);
        sServiceType.setEnabled(false);

        ibArrive.setVisibility(GONE);
        ibLeave.setVisibility(GONE);
        bSave.setVisibility(GONE);
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
