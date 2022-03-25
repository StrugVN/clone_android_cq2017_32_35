package com.androidproject.travelassistant.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.ReviewAdapter;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.SendFeedbackRequest;
import com.androidproject.travelassistant.Request.SendFeedbackResponse;
import com.androidproject.travelassistant.Request.SendReviewRequest;
import com.androidproject.travelassistant.Request.SendReviewResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewTourActivity extends AppCompatActivity {
    EditText etReview;
    Button btSendReview;
    RatingBar ratingBar;

    float rating;
    int id, serviceId;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_tour);

        Toolbar tbTourInfo = findViewById(R.id.toolbarReviewTour);
        tbTourInfo.setTitle("Write what you think");

        etReview = findViewById(R.id.et_addReview);
        btSendReview = findViewById(R.id.bt_sendReview);
        ratingBar = findViewById(R.id.ratingBar_Review);

        rating = getIntent().getFloatExtra("Rated", -1);
        id = getIntent().getIntExtra("Id", -1);
        serviceId = getIntent().getIntExtra("serviceId", -1);
        type = getIntent().getStringExtra("type");

        ratingBar.setRating(rating);

        btSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(type, "review"))
                    sendReview();
                if (TextUtils.equals(type, "feedback"))
                    sendFeedback();
            }
        });
    }

    private void sendReview() {
        final SendReviewRequest request = new SendReviewRequest();
        request.setTourId(id);
        request.setPoint((int)rating);
        request.setReview(etReview.getText().toString());

        Call<SendReviewResponse> call = Global.userService.Send_Review(Global.userToken, request);

        call.enqueue(new Callback<SendReviewResponse>() {
            @Override
            public void onResponse(Call<SendReviewResponse> call, Response<SendReviewResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ReviewTourActivity.this,
                                "Error sending review, please try again", Toast.LENGTH_LONG).show();
                        Log.d(TourInfoActivity.class.getSimpleName() + " send_review_failed",
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<SendReviewResponse> call, Throwable t) {
                Toast.makeText(ReviewTourActivity.this,
                        "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(ReviewTourActivity.class.getSimpleName() + " send_review: ", t.getMessage());
            }
        });
    }

    void sendFeedback()
    {
        final SendFeedbackRequest request = new SendFeedbackRequest();
        request.setServiceId(serviceId);
        request.setPoint((int)rating);
        request.setFeedback(etReview.getText().toString());

        Call<SendFeedbackResponse> call = Global.userService.Send_Feedback(Global.userToken, request);
        call.enqueue(new Callback<SendFeedbackResponse>() {
            @Override
            public void onResponse(Call<SendFeedbackResponse> call, Response<SendFeedbackResponse> response) {
                if (response.code() != 200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ReviewTourActivity.this,
                                "Error sending feedback, please try again", Toast.LENGTH_LONG).show();
                        Log.d(TourInfoActivity.class.getSimpleName() + " send_feedback_failed",
                                "Code " + response.code() + ": " + error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<SendFeedbackResponse> call, Throwable t) {
                Toast.makeText(ReviewTourActivity.this,
                        "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(ReviewTourActivity.class.getSimpleName() + " send_feedback: ", t.getMessage());
            }
        });
    }
}
