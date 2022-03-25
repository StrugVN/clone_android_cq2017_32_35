package com.androidproject.travelassistant.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Comment;
import com.androidproject.travelassistant.AppData.CommentAdapter;
import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.MemberAdapter;
import com.androidproject.travelassistant.AppData.Members;
import com.androidproject.travelassistant.AppData.Object;
import com.androidproject.travelassistant.AppData.ReviewAdapter;
import com.androidproject.travelassistant.AppData.ReviewPoint;
import com.androidproject.travelassistant.AppData.Reviews;
import com.androidproject.travelassistant.AppData.StopPointAdapter;
import com.androidproject.travelassistant.AppData.StopPoints;
import com.androidproject.travelassistant.AppData.Tour;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.AddCommentRequest;
import com.androidproject.travelassistant.Request.AddCommentResponse;
import com.androidproject.travelassistant.Request.AddMemberRequest;
import com.androidproject.travelassistant.Request.CloneATourRequest;
import com.androidproject.travelassistant.Request.CloneATourResponse;
import com.androidproject.travelassistant.Request.GetListReviewResponse;
import com.androidproject.travelassistant.Request.GetPointReviewResponse;
import com.androidproject.travelassistant.Request.GetTourInfoResponse;
import com.androidproject.travelassistant.Request.UpdateTourRequest;
import com.androidproject.travelassistant.Request.UpdateTourResponse;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TourInfoActivity extends AppCompatActivity {
    private RatingBar mRatingBar, mReviewbar;
    private int id;
    TextView tvName, tvStartDate, tvEndDate, tvMaxCost, tvMinCost, tvAdults, tvChilds, tvStart, tvEnd, tvRvScore;
    CheckBox cbPrivate;

    private Button btAddCmt, btMemberCrtl, btReviewCrtl, btStopPointCrtl, btAddMember, btAddStopPoint, btClone;
    private ImageButton btEditTour;

    private EditText etCmt;

    private ArrayList<StopPoints> stopPointAdapter_list;
    private ListView lvStopPoint;
    private StopPointAdapter stopPointAdapter;
    boolean isStopPointListOpen = false;

    private ArrayList<Comment> cmtAdapter_list;
    private ListView lvComment;
    private CommentAdapter cmtAdapter;

    private ArrayList<Members> memberAdapter_list;
    private ListView lvMember;
    private MemberAdapter memAdapter;
    private boolean isMemberListOpen = false;

    private ArrayList<Reviews> reviewAdapter_list;
    private ListView lvReview;
    private ReviewAdapter reviewAdapter;
    private boolean isReviewListOpen = false;

    private static int SendReviewCode = 1;
    private static int AddStopPointCode = 2;
    private static int UpdateTourCode = 3;
    private static int StopPointInfoCode = 4;

    TextView tvHostName;
    ImageView ivHostAvatar;

    Members host = new Members();
    boolean hosting = false;
    Button TourInfo_Clone;

    Button btDelete, btFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_info);

        Toolbar tbTourInfo = (Toolbar) findViewById(R.id.toolbarTourInfo);
        tbTourInfo.setTitle("Tour Information");
        setSupportActionBar(tbTourInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        id = intent.getIntExtra("Tour Id", -1);
        hosting = intent.getBooleanExtra("isHost", false);

        btEditTour = findViewById(R.id.TourInfo_EditTourButton);
        btClone = findViewById(R.id.TourInfo_Clone);

        tvName = findViewById(R.id.TourInfo_NameView);
        tvStartDate = findViewById(R.id.TourInfo_StartDateView);
        tvEndDate = findViewById(R.id.TourInfo_EndDateView);
        tvMaxCost = findViewById(R.id.TourInfo_MaxCostView);
        tvMinCost = findViewById(R.id.TourInfo_MinCostView);
        tvAdults = findViewById(R.id.TourInfo_AdultView);
        tvChilds = findViewById(R.id.TourInfo_ChildView);
        cbPrivate = findViewById(R.id.TourInfo_PrivateCheckBox);

        /*tvStart = findViewById(R.id.TourInfo_StartPoint);
        tvEnd = findViewById(R.id.TourInfo_EndPoint);*/

        lvStopPoint = findViewById(R.id.lv_stopPoints_TourInfo);
        lvComment = findViewById(R.id.TourInfo_CmtList);
        lvMember = findViewById(R.id.lv_Member_TourInfo);
        lvReview = findViewById(R.id.lv_review_TourInfo);

        btAddCmt = findViewById(R.id.TourInfo_SendCmt);
        etCmt = findViewById(R.id.TourInfo_AddCmt);

        tvHostName = findViewById(R.id.TourInfo_HostName);
        ivHostAvatar = findViewById(R.id.iv_TourInfoHostAva);
        Utility.SpinImageView(ivHostAvatar);
        btMemberCrtl = findViewById(R.id.tourInfo_expandMember);
        btReviewCrtl = findViewById(R.id.tourInfo_expandReview);
        btStopPointCrtl = findViewById(R.id.tourInfo_expandStopPoints);

        mRatingBar = findViewById(R.id.ratingBar_AvgReviewTourInfo);
        mReviewbar = findViewById(R.id.ratingBar_TourInfo);

        tvRvScore = findViewById(R.id.tv_AvgReview);

        btAddMember = findViewById(R.id.tourInfo_addMember);
        btAddStopPoint = findViewById(R.id.TourInfo_addStopPoint);


        TourInfo_Clone = findViewById(R.id.TourInfo_Clone);
        TourInfo_Clone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CloneATourRequest request = new CloneATourRequest();
                Call <CloneATourResponse> call = Global.userService.Clone_A_Tour(Global.userToken, request);
                call.enqueue(new Callback<CloneATourResponse>() {
                    @Override
                    public void onResponse(Call<CloneATourResponse> call, Response<CloneATourResponse> response) {
                        if(response.code()!=200) {
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());
                                Toast.makeText(TourInfoActivity.this,
                                        "Error cloning, please try again", Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        Toast.makeText(TourInfoActivity.this,
                                "Successful ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<CloneATourResponse> call, Throwable t) {
                        Toast.makeText(TourInfoActivity.this, "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });


        btAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(TourInfoActivity.this, SearchUserActivity.class);
                newIntent.putExtra("Id", id);
                newIntent.putExtra("isPrivate", cbPrivate.isChecked());
                newIntent.putExtra("members", memberAdapter_list);
                startActivity(newIntent);
            }
        });

        btAddStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(TourInfoActivity.this, AddStopPointActivity.class);
                newIntent.putExtra("Id", id);
                startActivityForResult(newIntent, AddStopPointCode);
            }
        });

        mReviewbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent new_intent = new Intent(TourInfoActivity.this, ReviewTourActivity.class);
                new_intent.putExtra("Rated", rating);
                new_intent.putExtra("Id", id);
                new_intent.putExtra("type", "review");
                startActivityForResult(new_intent, SendReviewCode);
            }
        });

        etCmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()))
                    btAddCmt.setVisibility(VISIBLE);
                else
                    btAddCmt.setVisibility(GONE);
            }
        });

        btAddCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewComment();
                etCmt.setText("");
                etCmt.clearFocus();
            }
        });

        btDelete = findViewById(R.id.TourInfo_deleteTour);
        btFollow = findViewById(R.id.TourInfo_Follow);

        btFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourInfoActivity.this, TourProgressActivity.class);
                intent.putExtra("tourId", id);
                intent.putExtra("name", tvName.getText().toString());
                intent.putExtra("members", memberAdapter_list);
                intent.putExtra("stopPoints", stopPointAdapter_list);
                startActivity(intent);
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TourInfoActivity.this)
                        .setTitle("Delete tour")
                        .setMessage("Do you want to delete tour \"" + tvName.getText().toString() + "\"?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                UpdateTourRequest request = new UpdateTourRequest();

                                request.setId(String.valueOf(id));
                                request.setStatus(-1);


                                Call<UpdateTourResponse> call = Global.userService.Update_Tour(Global.userToken, request);
                                call.enqueue(new Callback<UpdateTourResponse>() {
                                    @Override
                                    public void onResponse(Call<UpdateTourResponse> call, Response<UpdateTourResponse> response) {
                                        if (response.code() != 200) {
                                            try {
                                                JSONObject error = new JSONObject(response.errorBody().string());
                                                Toast.makeText(TourInfoActivity.this,
                                                        "Error, please try again", Toast.LENGTH_LONG).show();
                                                Log.d(TourInfoActivity.class.getSimpleName() + "add_stop_point_failed",
                                                        "Code " + response.code() + ": " + error.getString("message"));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            dialog.dismiss();
                                            return;
                                        }

                                        dialog.dismiss();
                                        Toast.makeText(TourInfoActivity.this, "Tour deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateTourResponse> call, Throwable t) {
                                        Toast.makeText(TourInfoActivity.this,
                                                "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.d(TourInfoActivity.class.getSimpleName() + " update tour info", t.getMessage());
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });

        loadTourInfo();
    }

    void sendNewComment(){
        String cmt = etCmt.getText().toString();

        final AddCommentRequest request = new AddCommentRequest();

        request.setTourId(id);
        request.setComment(cmt);

        Call<AddCommentResponse> call = Global.userService.Add_Comment(Global.userToken, request);

        call.enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {
                if(response.code()!=200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Utility.showErrorDialog(TourInfoActivity.this, "Error" + response.code(),
                                error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                loadTourInfo();
            }

            @Override
            public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                Utility.showErrorDialog(TourInfoActivity.this, "Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadTourInfo(){
        Call<GetTourInfoResponse> call = Global.userService.Get_Tour_Info(Global.userToken, id);

        call.enqueue(new Callback<GetTourInfoResponse>() {
            @Override
            public void onResponse(Call<GetTourInfoResponse> call, Response<GetTourInfoResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(TourInfoActivity.this,
                                "Error, please try again", Toast.LENGTH_LONG).show();
                        Log.d(TourInfoActivity.class.getSimpleName() + " load_Tour_Failed",
                                "Code " + response.code() + ": " + error.getString("message"));
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }

                final String name = response.body().getName();
                final long StartDate = response.body().getStartDate();
                final long EndDate = response.body().getEndDate();
                final long minCost = response.body().getMinCost();
                final long maxCost = response.body().getMaxCost();
                final int adults = response.body().getAdults();
                final int childs = response.body().getChilds();
                final int stt = response.body().getStatus();
                final boolean is_private = response.body().isPrivate();
                final String avt = response.body().getAvatar();

                btEditTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent new_intent = new Intent(TourInfoActivity.this, UpdateTourActivity.class);
                        new_intent.putExtra("Id", id);
                        new_intent.putExtra("name", name);
                        new_intent.putExtra("startDate", StartDate);
                        new_intent.putExtra("endDate", EndDate);
                        new_intent.putExtra("min", minCost);
                        new_intent.putExtra("max", maxCost);
                        new_intent.putExtra("adult", adults);
                        new_intent.putExtra("child", childs);
                        new_intent.putExtra("isPrivate", is_private);
                        new_intent.putExtra("status", stt);
                        new_intent.putExtra("avt", avt);
                        startActivityForResult(new_intent, UpdateTourCode);
                    }
                });

                if (!TextUtils.isEmpty(name))
                    tvName.setText(name);
                else
                    tvName.setText(getString(R.string.none_given_info));

                tvStartDate.setText(Utility.MillisToDate(StartDate));
                tvEndDate.setText(Utility.MillisToDate(EndDate));

                tvMinCost.setText(String.valueOf(minCost));
                tvMaxCost.setText(String.valueOf(maxCost));

                tvAdults.setText(String.valueOf(adults));
                tvChilds.setText(String.valueOf(childs));


                if (is_private)
                    cbPrivate.setChecked(true);

                loadTourReview();

                // if (!TextUtils.isEmpty(response.body().getAvatar()))

                cmtAdapter_list = response.body().getComment();

                if (cmtAdapter_list != null && !cmtAdapter_list.isEmpty()) {
                    cmtAdapter = new CommentAdapter(cmtAdapter_list, TourInfoActivity.this);

                    lvComment.setAdapter(cmtAdapter);
                    cmtAdapter.notifyDataSetChanged();

                    lvComment.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action)
                            {
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

                    lvComment.setClickable(true);
                }

                stopPointAdapter_list = response.body().getStopPoints();
                if(stopPointAdapter_list != null && !stopPointAdapter_list.isEmpty()){
                    stopPointAdapter = new StopPointAdapter(stopPointAdapter_list, TourInfoActivity.this);

                    lvStopPoint.setAdapter(stopPointAdapter);
                    stopPointAdapter.notifyDataSetChanged();

                    lvStopPoint.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action)
                            {
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

                    lvStopPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long idList) {
                            Intent new_intent = new Intent(TourInfoActivity.this, StopPointInfoActivity.class);
                            new_intent.putExtra("tourId", id);
                            Object obj = new Object(stopPointAdapter_list.get(position).getId(),
                                    stopPointAdapter_list.get(position).getArrivalAt(),
                                    stopPointAdapter_list.get(position).getLeaveAt(),
                                    stopPointAdapter_list.get(position).getMaxCost(),
                                    stopPointAdapter_list.get(position).getMinCost(),
                                    stopPointAdapter_list.get(position).getLat(),
                                    stopPointAdapter_list.get(position).getLongg(),
                                    stopPointAdapter_list.get(position).getName(),
                                    stopPointAdapter_list.get(position).getAddress(),
                                    stopPointAdapter_list.get(position).getServiceId(),
                                    stopPointAdapter_list.get(position).getServiceTypeId(),
                                    stopPointAdapter_list.get(position).getAvatar());
                            new_intent.putExtra("stopPoint", obj);
                            new_intent.putExtra("isHost", host.getId() == Global.userId);
                            startActivityForResult(new_intent, StopPointInfoCode);
                        }
                    });
                }

                btStopPointCrtl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isStopPointListOpen){
                            setListViewHeight(lvStopPoint, 0);
                            btStopPointCrtl.setText("Show all " + stopPointAdapter_list.size() + " stop points");
                            lvStopPoint.setClickable(false);
                        }
                        else{
                            setListViewHeight(lvStopPoint, 800);
                            btStopPointCrtl.setText("Hide all stop points");
                            lvStopPoint.setClickable(true);
                        }
                        isStopPointListOpen = !isStopPointListOpen;
                    }
                });


                memberAdapter_list = response.body().getMembers();
                if (memberAdapter_list != null && !memberAdapter_list.isEmpty()){
                    for (int i=0; i< memberAdapter_list.size(); i++) {
                        if (memberAdapter_list.get(i).isHost()) {
                            host = memberAdapter_list.get(i);
                            //memberAdapter_list.remove(i);
                            break;
                        }
                    }

                    for (int i=0; i< memberAdapter_list.size(); i++) {
                        if (memberAdapter_list.get(i).getId() == Global.userId) {
                            btFollow.setVisibility(VISIBLE);
                            break;
                        }
                    }

                    if(host.getId() == Global.userId)
                        hosting = true;



                    if(hosting){
                        btEditTour.setVisibility(VISIBLE);
                        btAddMember.setVisibility(VISIBLE);
                        btAddStopPoint.setVisibility(VISIBLE);
                        btDelete.setVisibility(VISIBLE);
                        btClone.setVisibility(GONE);
                    }
                    else{
                        btEditTour.setVisibility(GONE);
                        btAddMember.setVisibility(GONE);
                        btAddStopPoint.setVisibility(GONE);
                        btClone.setVisibility(VISIBLE);
                        btDelete.setVisibility(GONE);
                        if(stopPointAdapter != null)
                            stopPointAdapter.setDeleteButton(false);
                    }

                    if(!TextUtils.isEmpty(host.getAvatar())) {
                        new DownloadImageTask(ivHostAvatar).execute(host.getAvatar());
                    }
                    else{
                        Utility.setEmptyAvatar(host.getName(), host.getId(), ivHostAvatar, TourInfoActivity.this);
                    }

                    tvHostName.setText(host.getName());

                    memAdapter = new MemberAdapter(memberAdapter_list, TourInfoActivity.this);

                    lvMember.setAdapter(memAdapter);

                    lvMember.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action)
                            {
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

                    btMemberCrtl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isMemberListOpen){
                                setListViewHeight(lvMember, 0);
                                btMemberCrtl.setText("Show all " + memberAdapter_list.size() + " members");
                                lvMember.setClickable(false);
                            }
                            else{
                                setListViewHeight(lvMember, 600);
                                btMemberCrtl.setText("Hide all members");
                                lvMember.setClickable(true);
                            }
                            isMemberListOpen = !isMemberListOpen;
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<GetTourInfoResponse> call, Throwable t) {
                Toast.makeText(TourInfoActivity.this,
                        "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TourInfoActivity.class.getSimpleName() + " loadTourPage", t.getMessage());
                finish();
            }
        });
    }

    private void loadTourReview() {
        Call<GetListReviewResponse> call = Global.userService.Get_Review(Global.userToken, id, 1, "10000");

        call.enqueue(new Callback<GetListReviewResponse>() {
            @Override
            public void onResponse(Call<GetListReviewResponse> call, Response<GetListReviewResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(TourInfoActivity.this,
                                "Error loading reviews, please try again", Toast.LENGTH_LONG).show();
                        Log.d(TourInfoActivity.class.getSimpleName() + " load_Review_Failed",
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }

                reviewAdapter_list = response.body().getReviews();

                if(reviewAdapter_list != null && reviewAdapter_list.size() > 0){
                    reviewAdapter = new ReviewAdapter(reviewAdapter_list, TourInfoActivity.this);

                    lvReview.setAdapter(reviewAdapter);
                    reviewAdapter.notifyDataSetChanged();

                    lvReview.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action)
                            {
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

                    btReviewCrtl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isReviewListOpen){
                                setListViewHeight(lvReview, 0);
                                btReviewCrtl.setText("Show all " + reviewAdapter_list.size() + " reviews");
                                lvReview.setClickable(false);
                            }
                            else{
                                setListViewHeight(lvReview, 600);
                                btReviewCrtl.setText("Hide all members");
                                lvReview.setClickable(true);
                            }
                            isReviewListOpen = !isReviewListOpen;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetListReviewResponse> call, Throwable t) {
                Toast.makeText(TourInfoActivity.this,
                        "Unexpected Error while loading reviews", Toast.LENGTH_LONG).show();
                Log.d(TourInfoActivity.class.getSimpleName() + " load_Review_Failed",
                        t.getMessage());
            }
        });

        Call<GetPointReviewResponse> call2 = Global.userService.Get_Point_Review(Global.userToken, id);

        call2.enqueue(new Callback<GetPointReviewResponse>() {
            @Override
            public void onResponse(Call<GetPointReviewResponse> call, Response<GetPointReviewResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(TourInfoActivity.this,
                                "Error loading reviews score, please try again", Toast.LENGTH_LONG).show();
                        Log.d(TourInfoActivity.class.getSimpleName() + " load_Review_Score_Failed",
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

                mRatingBar.setRating(score);
                tvRvScore.setText(String.valueOf(Math.round(score*10.0)/10.0));
            }

            @Override
            public void onFailure(Call<GetPointReviewResponse> call, Throwable t) {
                Toast.makeText(TourInfoActivity.this,
                        "Unexpected Error while loading reviews score", Toast.LENGTH_LONG).show();
                Log.d(TourInfoActivity.class.getSimpleName() + " load_Review_Score_Failed",
                        t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SendReviewCode) {
            if (resultCode == Activity.RESULT_OK) {
                loadTourReview();
            }
        }
        if(requestCode == AddStopPointCode){
            if (resultCode == Activity.RESULT_OK) {
                loadTourInfo();
            }
        }
        if(requestCode == UpdateTourCode){
            if (resultCode == Activity.RESULT_OK) {
                loadTourInfo();
            }
        }
        if(requestCode == StopPointInfoCode){
            if (resultCode == Activity.RESULT_OK) {
                loadTourInfo();
            }
        }
    }

    void setListViewHeight(ListView listView, int height){
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = height;

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /*
    void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + 500 + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

     void shrinkListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        view = listAdapter.getView(0, view, listView);
        view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

        view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
        totalHeight += view.getMeasuredHeight();

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + 500 + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    */

    /*
    void func(StopPoints stopPoints){
        stopPoints.getLat();
        stopPoints.getLong();
        LatLng point = new LatLng(stopPoints.getLat(), stopPoints.getLong());

        new ReverseGeocodingTask(getBaseContext(), tvEnd).execute(point);
    }


    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;
        TextView tv;

        public ReverseGeocodingTask(Context context, TextView tv){
            super();
            mContext = context;
            this.tv = tv;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].latitude;
            double longitude = params[0].longitude;

            List<Address> addresses = null;
            String addressText="";

            try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);

                addressText = address.getAddressLine(0);
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            tv.setText(tv.getText() + " - " + addressText);
        }
    }

     */
}
