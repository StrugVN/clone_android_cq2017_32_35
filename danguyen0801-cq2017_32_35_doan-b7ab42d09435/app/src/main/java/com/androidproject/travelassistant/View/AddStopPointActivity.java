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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.StopPoints;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.AddStopPointRequest;
import com.androidproject.travelassistant.Request.AddStopPointResponse;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.Utility.showDateTimePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStopPointActivity extends AppCompatActivity {
    private TextView etAddr;
    private EditText etName, etMaxCost, etMinCost, etLeave, etArrive;
    private Button btAddr, btCreate, btImage;
    private ImageButton ibArrive, ibLeave;
    private ImageView ivStopPoint;
    String imagePath;
    private double Lat, Lng;
    private int serviceType, serviceId, province;
    Calendar arrive, leave;

    private String avatarBase64;

    private String address;

    private int tourId;

    private Spinner sServiceType;

    private static final int GetImageRequestCode = 0;
    private static final int GetAddrRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop_point);

        Toolbar tbAddStopPoint = (Toolbar)findViewById(R.id.toolbarCreateStopPoint);
        tbAddStopPoint.setTitle("Add a stop point");
        setSupportActionBar(tbAddStopPoint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sServiceType = findViewById(R.id.sType_CreateStopPoint);
        String[] items = new String[]{"Restaurant", "Hotel", "Rest Station", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddStopPointActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        sServiceType.setAdapter(adapter);

        etName = findViewById(R.id.etName_CreateStopPoint);
        etAddr = findViewById(R.id.et_Addr_StopPoint);
        etMaxCost = findViewById(R.id.etMaxCost_CreateStopPoint);
        etMinCost = findViewById(R.id.etMinCost_CreateStopPoint);
        etLeave = findViewById(R.id.etLeave_CreateStopPoint);
        etArrive = findViewById(R.id.etArrival_CreateStopPoint);
        btAddr = findViewById(R.id.b_Point_CreateStopPoint);
        btCreate = findViewById(R.id.bCreateStopPoint);
        btImage = findViewById(R.id.bImage_CreateStopPoint);
        ibArrive = findViewById(R.id.btArrival_CreateStopPoint);
        ibLeave = findViewById(R.id.btLeave_CreateStopPoint);

        tourId = getIntent().getIntExtra("Id", -1);

        serviceType = -1;
        serviceId = -1;
        province = -1;


        if (tourId == -1){
            Toast.makeText(this, "Error: No tour id received - Closing", Toast.LENGTH_SHORT).show();
            finish();
        }

        btAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sServiceType.setEnabled(true);
                serviceType = -1;
                serviceId = -1;
                province = -1;

                etName.setFocusable(true);
                etName.setFocusableInTouchMode(true);

                Intent intent = new Intent(AddStopPointActivity.this, GoogleMapsActivity.class);
                intent.putExtra("Title","Choose stop point location:");
                intent.putExtra("needSuggestion", true);
                startActivityForResult(intent, GetAddrRequestCode);
            }
        });

        ibArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDateTimePicker(AddStopPointActivity.this, new showDateTimePicker.TaskListener() {
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
                new showDateTimePicker(AddStopPointActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        leave = calendar;

                        etLeave.setText(Utility.MillisToDate(leave.getTimeInMillis()));
                    }
                }).run();
            }
        });

        btImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, GetImageRequestCode);
            }
        });

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStopPoint();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GetAddrRequestCode){
            if(resultCode == Activity.RESULT_OK){
                Lat = data.getDoubleExtra("lat", 0);
                Lng = data.getDoubleExtra("long", 0);
                address = data.getStringExtra("addr");

                if(data.getBooleanExtra("isSuggested", false)){
                    province = data.getIntExtra("provinceId", -1);
                    serviceId = data.getIntExtra("serviceId", -1);
                    serviceType = data.getIntExtra("serviceTypeId", -1);

                    if(serviceType!=-1) {
                        sServiceType.setSelection(serviceType - 1);
                        sServiceType.setEnabled(false);
                    }

                    etName.setText(data.getStringExtra("name"));
                    etName.setFocusable(false);
                }
                else{
                    province = Utility.findProvinceIdByAddress(data.getStringExtra("addr"));
                }

                String text = "";
                if (data.getStringExtra("name") == null)
                    text = data.getStringExtra("addr");
                else
                    text = data.getStringExtra("name") + ": " + data.getStringExtra("addr");
                etAddr.setText(text);
            }
        }

        if (requestCode == GetImageRequestCode && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            ivStopPoint.setVisibility(View.VISIBLE);

            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = managedQuery(selectedImage, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            imagePath = cursor.getString(column_index);

            try {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), selectedImage);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);

                ivStopPoint.setImageBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                avatarBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addStopPoint(){
        final AlertDialog add_stop_point_loading = new SpotsDialog.Builder()
                .setContext(AddStopPointActivity.this)
                .setMessage(getString(R.string.add_stop_point_loading))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        add_stop_point_loading.show();

        boolean cancel = false;
        if(TextUtils.isEmpty(etName.getText().toString())){
            cancel = true;
            etName.setError(getString(R.string.add_stop_point_error_name_empty));
        }
        if (TextUtils.isEmpty(etArrive.getText().toString())){
            cancel = true;
            etArrive.setError(getString(R.string.add_stop_point_error_arrive_leave_empty));
        }
        if (TextUtils.isEmpty(etLeave.getText().toString())){
            cancel = true;
            etLeave.setError(getString(R.string.add_stop_point_error_arrive_leave_empty));
        }
        if (TextUtils.isEmpty(etAddr.getText().toString())){
            cancel = true;
            etAddr.setError(getString(R.string.add_stop_point_error_addr_empty));
        }


        if(cancel){
            add_stop_point_loading.dismiss();
            return;
        }
        else {
            String name;
            int min, max;

            name = etName.getText().toString();
            if (!TextUtils.isEmpty(etMinCost.getText().toString()))
                min = Integer.valueOf(etMinCost.getText().toString());
            else
                min = 0;

            if(!TextUtils.isEmpty(etMaxCost.getText().toString()))
                max = Integer.valueOf(etMaxCost.getText().toString());
            else
                max = 0;

            final AddStopPointRequest request = new AddStopPointRequest();
            request.setTourId(tourId);

            ArrayList<StopPoints> stopPoints = new ArrayList<>();

            StopPoints point = new StopPoints();
            point.setName(name);
            if(serviceId != -1)
                point.setServiceId(serviceId);
            point.setProvinceId(province);
            point.setMaxCost(max);
            point.setMinCost(min);
            point.setLat(Lat);
            point.setLongg(Lng);
            point.setArrivalAt(arrive.getTimeInMillis());
            point.setLeaveAt(leave.getTimeInMillis());
            serviceType = sServiceType.getSelectedItemPosition() + 1;
            point.setServiceTypeId(serviceType);
            point.setAddress(address);

            if (!TextUtils.isEmpty(avatarBase64))
                point.setAvatar(avatarBase64);

            stopPoints.add(point);

            request.setStopPoints(stopPoints);

            Call<AddStopPointResponse> call = Global.userService.Add_Stop_Point(Global.userToken, request);
            call.enqueue(new Callback<AddStopPointResponse>() {
                @Override
                public void onResponse(Call<AddStopPointResponse> call, Response<AddStopPointResponse> response) {
                    if (response.code() != 200) {
                        try {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Toast.makeText(AddStopPointActivity.this,
                                    "Error, please try again", Toast.LENGTH_LONG).show();
                            Log.d(TourInfoActivity.class.getSimpleName() + "add_stop_point_failed",
                                    "Code " + response.code() + ": " + error.getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        add_stop_point_loading.dismiss();
                        return;
                    }

                    add_stop_point_loading.dismiss();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(Call<AddStopPointResponse> call, Throwable t) {
                    Toast.makeText(AddStopPointActivity.this,
                            "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TourInfoActivity.class.getSimpleName() + " addStopPoint", t.getMessage());
                }
            });
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
