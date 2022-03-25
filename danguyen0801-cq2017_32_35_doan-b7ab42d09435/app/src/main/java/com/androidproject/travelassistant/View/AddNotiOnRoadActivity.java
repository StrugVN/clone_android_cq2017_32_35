package com.androidproject.travelassistant.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.AddNotiOnRoadRequest;
import com.androidproject.travelassistant.Request.AddNotiOnRoadRespone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNotiOnRoadActivity extends AppCompatActivity {
    double lat, lng;
    int id;

    Spinner sType;

    LinearLayout Speed;

    Button btSend;

    EditText etNote, etSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_noti_on_road);

        lat = getIntent().getDoubleExtra("lat", -1);
        lng = getIntent().getDoubleExtra("long", -1);
        id = getIntent().getIntExtra("tourId", -1);

        sType = findViewById(R.id.sType_AddOnRoadNoti);
        String[] items = new String[]{"Police Position", "Problem on road", "Speed limit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNotiOnRoadActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        sType.setAdapter(adapter);

        Speed = findViewById(R.id.layout_Speed);

        sType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2)
                    Speed.setVisibility(View.VISIBLE);
                else
                    Speed.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etNote = findViewById(R.id.etNote_AddNotiOnRoad);
        etSpeed = findViewById(R.id.etSpeed_AddNotiOnRoad);
        btSend = findViewById(R.id.bt_sendNoti);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddNotiOnRoadRequest request = new AddNotiOnRoadRequest();

                request.setLat(lat);
                request.setLng(lng);
                request.setTourId(id);
                request.setUserId(Global.userId);
                request.setNotiType(sType.getSelectedItemPosition() + 1);
                request.setNote(etNote.getText().toString());
                if (request.getNotiType() == 3 && !TextUtils.isEmpty(etSpeed.getText().toString()))
                    request.setSpeed(Integer.parseInt(etSpeed.getText().toString()));

                Call<AddNotiOnRoadRespone> call = Global.userService.Add_Noti_On_Road(Global.userToken, request);

                call.enqueue(new Callback<AddNotiOnRoadRespone>() {
                    @Override
                    public void onResponse(Call<AddNotiOnRoadRespone> call, Response<AddNotiOnRoadRespone> response) {
                        if(response.code()!=200) {
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());
                                Toast.makeText(AddNotiOnRoadActivity.this,
                                        "Error: " + error.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return;
                        }

                        Toast.makeText(AddNotiOnRoadActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();

                        setResult(RESULT_OK, intent);

                        finish();
                    }

                    @Override
                    public void onFailure(Call<AddNotiOnRoadRespone> call, Throwable t) {
                        Toast.makeText(AddNotiOnRoadActivity.this,
                                "Unexpected error: " + t.toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TourProgressActivity.class.getSimpleName(), t.getMessage());
                    }
                });
            }
        });
    }
}
