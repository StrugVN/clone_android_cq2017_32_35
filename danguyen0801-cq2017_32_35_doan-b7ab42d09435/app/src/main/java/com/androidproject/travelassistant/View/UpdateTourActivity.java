package com.androidproject.travelassistant.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.UpdateTourRequest;
import com.androidproject.travelassistant.Request.UpdateTourResponse;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.Utility.showDateTimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTourActivity extends AppCompatActivity {
    private EditText etName, etStartDate,etEndDate,etAdult, etChildren, etMinCost,etMaxCost;
    private ImageButton ibStartDate, ibEndDate;
    private CheckBox checkBox;
    private Button bUpdate, bImage;
    private ImageView ivAvt;
    private Spinner sStatus;

    private int tourId;
    private String name, avt;
    private int adult, child, min, max, stt;
    private long startDate, endDate;
    private boolean isPrivate;

    String imagePath;
    private String avatarBase64;
    Calendar start, end;

    private static final int TourAvatarRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tour);

        Toolbar tbUpdateTour = (Toolbar)findViewById(R.id.toolbarUpdateTour);
        tbUpdateTour.setTitle("Update a tour");
        setSupportActionBar(tbUpdateTour);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sStatus = findViewById(R.id.sStatus_UpdateTour);
        String[] items = new String[]{"Canceled", "Open", "Started", "Closed"}; //-1, 0, 1, 2
        ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateTourActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        sStatus.setAdapter(adapter);

        etName = findViewById(R.id.etName_UpdateTour);
        etStartDate = findViewById(R.id.etStartDate_UpdateTour);
        etEndDate = findViewById(R.id.etEndDate_UpdateTour);
        etAdult = findViewById(R.id.etAdult_UpdateTour);
        etChildren =findViewById(R.id.etChild_UpdateTour);
        etMinCost = findViewById(R.id.etMinCost_UpdateTour);
        etMaxCost = findViewById(R.id.etMaxCost_UpdateTour);
        checkBox = findViewById(R.id.checkBox_UpdateTour);
        bUpdate = findViewById(R.id.bUpdateTour);
        bImage = findViewById(R.id.bPickAvt_UpdateTour);
        ivAvt = findViewById(R.id.ivTourAvt_UpdateTour);
        ibStartDate = findViewById(R.id.btStartDate_UpdateTour);
        ibEndDate = findViewById(R.id.btEndDate_UpdateTour);

        tourId = getIntent().getIntExtra("Id", -1);
        name = getIntent().getStringExtra("name");
        startDate = getIntent().getLongExtra("startDate", 0);
        endDate = getIntent().getLongExtra("endDate", 0);
        min = getIntent().getIntExtra("min", 0);
        max = getIntent().getIntExtra("max", 0);
        adult = getIntent().getIntExtra("adult", 0);
        child = getIntent().getIntExtra("child", 0);
        stt = getIntent().getIntExtra("status", 0);
        avt = getIntent().getStringExtra("avt");
        isPrivate = getIntent().getBooleanExtra("isPrivate", false);

        if(TextUtils.isEmpty(name))
            etName.setText("None");
        else
            etName.setText(name);
        etMinCost.setText(String.valueOf(min));
        etMaxCost.setText(String.valueOf(max));
        etAdult.setText(String.valueOf(adult));
        etChildren.setText(String.valueOf(child));
        etStartDate.setText(Utility.MillisToDate(startDate));
        etEndDate.setText(Utility.MillisToDate(endDate));
        checkBox.setChecked(isPrivate);
        sStatus.setSelection(stt + 1);

        ibStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDateTimePicker(UpdateTourActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        start = calendar;
                        etStartDate.setText(Utility.MillisToDate(start.getTimeInMillis()));
                    }
                }).run();
            }
        });

        ibEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDateTimePicker(UpdateTourActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        end = calendar;
                        etEndDate.setText(Utility.MillisToDate(end.getTimeInMillis()));
                    }
                }).run();
            }
        });

        bImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, TourAvatarRequestCode);
            }
        });

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTour();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TourAvatarRequestCode && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            ivAvt.setVisibility(View.VISIBLE);

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

                ivAvt.setImageBitmap(bitmap);

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

    private void updateTour(){
        final AlertDialog update_tour_loading = new SpotsDialog.Builder()
                .setContext(UpdateTourActivity.this)
                .setMessage(getString(R.string.update_tour_loading))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        update_tour_loading.show();

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
            update_tour_loading.dismiss();
            return;
        }
        else{
            UpdateTourRequest request = new UpdateTourRequest();

            request.setId(String.valueOf(tourId));
            request.setName(etName.getText().toString());
            if(!TextUtils.isEmpty(etMinCost.getText().toString()))
                min = Integer.valueOf(etMinCost.getText().toString());
            else
                min = 0;
            request.setMinCost(min);
            if(!TextUtils.isEmpty(etMaxCost.getText().toString()))
                max = Integer.valueOf(etMaxCost.getText().toString());
            else
                max = 0;
            request.setMaxCost(max);
            if(!TextUtils.isEmpty(etAdult.getText().toString()))
                adult = Integer.valueOf(etAdult.getText().toString());
            else
                adult = 0;
            request.setAdults(adult);
            if(!TextUtils.isEmpty(etChildren.getText().toString()))
                child = Integer.valueOf(etChildren.getText().toString());
            else
                child = 0;
            request.setChilds(child);
            request.setStartDate(Utility.DateToMillis(etStartDate.getText().toString()));
            request.setEndDate(Utility.DateToMillis(etEndDate.getText().toString()));
            request.setPrivate(checkBox.isChecked());
            request.setStatus(Math.toIntExact(sStatus.getSelectedItemId() - 1));
            if (!TextUtils.isEmpty(avatarBase64))
                request.setAvatar(avatarBase64);

            Call<UpdateTourResponse> call = Global.userService.Update_Tour(Global.userToken, request);
            call.enqueue(new Callback<UpdateTourResponse>() {
                @Override
                public void onResponse(Call<UpdateTourResponse> call, Response<UpdateTourResponse> response) {
                    if (response.code() != 200) {
                        try {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Toast.makeText(UpdateTourActivity.this,
                                    "Error, please try again", Toast.LENGTH_LONG).show();
                            Log.d(TourInfoActivity.class.getSimpleName() + "add_stop_point_failed",
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
                public void onFailure(Call<UpdateTourResponse> call, Throwable t) {
                    Toast.makeText(UpdateTourActivity.this,
                            "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TourInfoActivity.class.getSimpleName() + " update tour info", t.getMessage());
                }
            });
            update_tour_loading.dismiss();
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
