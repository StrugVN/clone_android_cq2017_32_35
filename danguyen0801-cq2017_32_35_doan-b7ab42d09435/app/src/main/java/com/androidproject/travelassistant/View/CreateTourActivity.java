package com.androidproject.travelassistant.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.CreateTourRequest;
import com.androidproject.travelassistant.Request.CreateTourResponse;
import com.androidproject.travelassistant.Utility.DateInputMask;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.Utility.showDateTimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTourActivity extends AppCompatActivity {
    private String TAG = CreateTourActivity.class.getSimpleName();
    private EditText etName, etStartDate,etEndDate,etAdult, etChildren, etMinCost,etMaxCost;
    private CheckBox checkBox;
    private Button bCreate, bGetStartPoint, bGetEndPoint, bSelectImage;
    private TextView etStartPoint, etEndPoint;
    private ImageView ivTour;
    String imagePath;

    private double startLat, startLng, endLat, endLng;

    private String avatarBase64;

    private ImageButton btStartDate, btEndDate;
    Calendar startDate, endDate;

    private static final int GetStartPointRequestCode = 1;
    private static final int GetEndPointRequestCode = 2;
    private static final int GetImageRequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        setTextAtStartUp();

        etName=findViewById(R.id.etName_CreateTour);
        etStartDate = findViewById(R.id.etStartDate_CreateTour);
        etEndDate = findViewById(R.id.etEndDate_CreateTour);
        etAdult = findViewById(R.id.etAdult_CreateTour);
        etChildren =findViewById(R.id.etChild_CreateTour);
        etMinCost = findViewById(R.id.etMinCost_CreateTour);
        etMaxCost = findViewById(R.id.etMaxCost_CreateTour);
        checkBox = findViewById(R.id.checkBox_CreateTour);
        bCreate = findViewById(R.id.bCreateTour);

        bGetStartPoint = findViewById(R.id.b_StartPoint_CreateTour);
        bGetEndPoint = findViewById(R.id.b_EndPoint_CreateTour);

        etStartPoint = findViewById(R.id.et_StartPoint_CreateTour);
        etStartPoint.setSelected(true);
        etEndPoint = findViewById(R.id.et_EndPoint_CreateTour);
        etEndPoint.setSelected(true);

        ivTour = findViewById(R.id.ivImage_CreateTour);
        bSelectImage = findViewById(R.id.bImage_CreateTour);

        bSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, GetImageRequestCode);
            }
        });

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTour();
            }
        });

        bGetStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTourActivity.this, GoogleMapsActivity.class);
                intent.putExtra("Title","Choose Start point location:");
                startActivityForResult(intent, GetStartPointRequestCode);
            }
        });

        bGetEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTourActivity.this, GoogleMapsActivity.class);
                intent.putExtra("Title","Choose End point location:");
                startActivityForResult(intent, GetEndPointRequestCode);
            }
        });

        btStartDate = findViewById(R.id.btStartDate_CreateTour);
        btEndDate = findViewById(R.id.btEndDate_CreateTour);

        btStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDateTimePicker(CreateTourActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        startDate = calendar;
                        etStartDate.setText(Utility.MillisToDate(startDate.getTimeInMillis()));
                    }
                }).run();
            }
        });

        btEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDateTimePicker(CreateTourActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        endDate = calendar;

                        etEndDate.setText(Utility.MillisToDate(endDate.getTimeInMillis()));
                    }
                }).run();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GetStartPointRequestCode){
            if(resultCode == Activity.RESULT_OK){
                startLat = data.getDoubleExtra("lat", 0);
                startLng = data.getDoubleExtra("long", 0);
                String text = "";
                if (data.getStringExtra("name") == null)
                    text = data.getStringExtra("addr");
                else
                    text = data.getStringExtra("name") + ": " + data.getStringExtra("addr");
                etStartPoint.setText(text);
            }
        }
        else if(requestCode == GetEndPointRequestCode){
            if(resultCode == Activity.RESULT_OK){
                endLat = data.getDoubleExtra("lat", 0);
                endLng = data.getDoubleExtra("long", 0);

                String text = "";
                if (data.getStringExtra("name") == null)
                    text = data.getStringExtra("addr");
                else
                    text = data.getStringExtra("name") + ": " + data.getStringExtra("addr");

                etEndPoint.setText(text);
            }
        }
        if (requestCode == GetImageRequestCode && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            ivTour.setVisibility(View.VISIBLE);

            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = managedQuery(selectedImage, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            imagePath = cursor.getString(column_index);

            Toast.makeText(this,
                    imagePath, Toast.LENGTH_LONG).show();

            try {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), selectedImage);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);

                ivTour.setImageBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                avatarBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Log.d(CreateTourActivity.class.getSimpleName() + " Avatar Base64: ", avatarBase64);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void createTour() {
        final AlertDialog create_tour_loading = new SpotsDialog.Builder()
                .setContext(CreateTourActivity.this)
                .setMessage(getString(R.string.create_tour_loading))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        create_tour_loading.show();

        boolean cancel = false;
        if(TextUtils.isEmpty(etName.getText().toString())){
            cancel = true;
            etName.setError(getString(R.string.create_tour_error_name_empty));
        }
        if (TextUtils.isEmpty(etStartDate.getText().toString())){
            cancel = true;
            etStartDate.setError(getString(R.string.create_tour_error_start_date_empty));
        }
        if (TextUtils.isEmpty(etEndDate.getText().toString())){
            cancel =true;
            etEndDate.setError(getString(R.string.create_tour_error_end_date_empty));
        }

        if(cancel){
            create_tour_loading.dismiss();
            return;
        }
        else{
            String name;
            int adults, children, min, max;
            boolean isPrivate;

            name = etName.getText().toString();
            if (!TextUtils.isEmpty(etAdult.getText().toString()))
                adults = Integer.valueOf(etAdult.getText().toString());
            else
                adults = 0;
            if (!TextUtils.isEmpty(etChildren.getText().toString()))
                children = Integer.valueOf(etChildren.getText().toString());
            else
                children = 0;
            if (!TextUtils.isEmpty(etMinCost.getText().toString()))
                min = Integer.valueOf(etMinCost.getText().toString());
            else
                min = 0;

            if(!TextUtils.isEmpty(etMaxCost.getText().toString()))
                max = Integer.valueOf(etMaxCost.getText().toString());
            else
                max = 0;

            isPrivate = checkBox.isChecked();

            CreateTourRequest request = new CreateTourRequest();
            request.setName(name);
            request.setStartDate(startDate.getTimeInMillis());
            request.setEndDate(endDate.getTimeInMillis());
            request.setAdults(adults);
            request.setChilds(children);
            request.setMinCost(min);
            request.setMaxCost(max);
            request.setPrivate(isPrivate);

            if (!TextUtils.isEmpty(avatarBase64))
                request.setAvatar(avatarBase64);

            Call<CreateTourResponse> call = Global.userService.create_tour(Global.userToken, request);

            call.enqueue(new Callback<CreateTourResponse>() {
                @Override
                public void onResponse(Call<CreateTourResponse> call, Response<CreateTourResponse> response) {
                    if(response.code() != 200){
                        switch (response.code()){
                            case 500:
                                Toast.makeText(CreateTourActivity.this, getString(R.string.create_tour_error_server_error), Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                try {
                                    JSONObject error = new JSONObject(response.errorBody().string());
                                    JSONArray message = error.getJSONArray("message");

                                    StringBuilder error_message = new StringBuilder();

                                    for(int i=0; i<message.length(); i++){
                                        JSONObject temp = message.getJSONObject(i);
                                        error_message.append(temp.getString("param") + ": " + temp.getString("msg") +"\n");
                                    }

                                    Utility.showErrorDialog(CreateTourActivity.this, "Error 404:", error_message.toString());
                                    Log.d(CreateTourActivity.class.getSimpleName(), error_message.toString());
                                }
                                catch(JSONException e){
                                    Toast.makeText(CreateTourActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(CreateTourActivity.class.getSimpleName(), e.getMessage());
                                }
                                catch(IOException t){
                                    Toast.makeText(CreateTourActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(CreateTourActivity.class.getSimpleName(), t.getMessage());
                                }
                                break;
                            default:
                                Toast.makeText(CreateTourActivity.this, "Unknown error: " + response.code(), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.d(CreateTourActivity.class.getSimpleName(), response.errorBody().string());
                                }
                                catch(IOException t){
                                    Log.d(CreateTourActivity.class.getSimpleName(), t.getMessage());
                                }
                        }

                        create_tour_loading.dismiss();
                        return;
                    }

                    Intent intent = new Intent(CreateTourActivity.this, TourInfoActivity.class);
                    intent.putExtra("Tour Id", response.body().getId());
                    startActivity(intent);

                    finish();
                }

                @Override
                public void onFailure(Call<CreateTourResponse> call, Throwable t) {
                    Toast.makeText(CreateTourActivity.this, "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(CreateTourActivity.class.getSimpleName() + " create_tour ", t.getMessage());
                }
            });
        }

    }

    void setTextAtStartUp(){
        //Set title and back button
        Toolbar tbCreateTour = (Toolbar)findViewById(R.id.toolbarCreateTour);
        tbCreateTour.setTitle("Create a tour");
        setSupportActionBar(tbCreateTour);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //(*)
        SpannableString ss;
        TextView tv;

        tv = findViewById(R.id.tvName_CreateTour);
        ss = new SpannableString(getString(R.string.ct_name));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);

        tv = findViewById(R.id.tvStartDate_CreateTour);
        ss = new SpannableString(getString(R.string.ct_start_date));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 10, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);

        tv = findViewById(R.id.tvEndDate_CreateTour);
        ss = new SpannableString(getString(R.string.ct_end_date));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 8, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);

        tv = findViewById(R.id.tv_StartPoint_CreateTour);
        ss = new SpannableString(getString(R.string.ct_startp));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 21, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);

        tv = findViewById(R.id.tv_EndPoint_CreateTour);
        ss = new SpannableString(getString(R.string.ct_endp));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 19, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }

    //Toolbar back button
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
