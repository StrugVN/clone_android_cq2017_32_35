package com.androidproject.travelassistant.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.Members;
import com.androidproject.travelassistant.AppData.OnRoadNoti;
import com.androidproject.travelassistant.AppData.StopPoints;
import com.androidproject.travelassistant.AppData.TextNoti;
import com.androidproject.travelassistant.AppData.TextNotiAdapter;
import com.androidproject.travelassistant.AppData.UserCoord;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetCoordinateUsersRequest;
import com.androidproject.travelassistant.Request.GetNotificationListResponse;
import com.androidproject.travelassistant.Request.GetNotificationOnRoadResponse;
import com.androidproject.travelassistant.Request.SendTextNotiResponse;
import com.androidproject.travelassistant.Request.SendTextNotificationRequest;
import com.androidproject.travelassistant.Utility.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.androidproject.travelassistant.Utility.WorkAroundMapFragment;

public class TourProgressActivity extends FragmentActivity implements OnMapReadyCallback {
    float zoomLevel = 16.0f;
    private GoogleMap mMap;
    private LatLng point_location = null;
    private Marker mark = null;
    private MarkerOptions markerOptions;

    FusedLocationProviderClient fusedLocationProviderClient;

    private ArrayList<Marker> marker_arr = new ArrayList<>();
    private ArrayList<OnRoadNoti> onRoadNoti_list = new ArrayList<>();

    private int id;

    private static final int LOCATION_REQUEST_CODE = 1;

    private LatLng selectedLocation;
    private Marker selectedLocationMark = null;
    private MarkerOptions selectedLocationmarkerOptions;

    private Button btAddNotiOnRoad;
    private ScrollView mScrollView;

    private ArrayList<TextNoti> adapter_list;
    private TextNotiAdapter adapter;
    private ListView lvTextNoti;

    private EditText etText;
    private Button btSend;

    private boolean movedOnce = false;

    private ArrayList<Marker> userMarker_arr = null;

    private ArrayList<Members> tourMembers;
    private ArrayList<StopPoints> tourStopPoints;
    private MarkerOptions stopPointMarkerOption = new MarkerOptions();

    Timer selfRefesh = new Timer();

    final private int CreateOnRoadNotiCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_progress);

        id = getIntent().getIntExtra("tourId", -1);

        if(id == -1){
            Toast.makeText(this, "Error, no tour id received", Toast.LENGTH_SHORT).show();
            finish();
        }

        tourMembers = (ArrayList<Members>)getIntent().getSerializableExtra("members");
        tourStopPoints = (ArrayList<StopPoints>)getIntent().getSerializableExtra("stopPoints");

        Toolbar tb = findViewById(R.id.tbGoogleMapTP);
        String title = "Tour: " + getIntent().getStringExtra("name");
        tb.setTitle(title);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTP);
        mapFragment.getMapAsync(this);

        markerOptions = new MarkerOptions();
        selectedLocationmarkerOptions = new MarkerOptions();

        btAddNotiOnRoad = findViewById(R.id.bAddNoti_TP);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        btAddNotiOnRoad = findViewById(R.id.bAddNoti_TP);

        btAddNotiOnRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourProgressActivity.this, AddNotiOnRoadActivity.class);

                intent.putExtra("lat", selectedLocation.latitude);
                intent.putExtra("long", selectedLocation.longitude);
                intent.putExtra("tourId", id);

                startActivityForResult(intent, CreateOnRoadNotiCode);
            }
        });

        lvTextNoti = findViewById(R.id.lvTextNoti);

        etText = findViewById(R.id.TourProgress_AddTextNoti);
        btSend = findViewById(R.id.TourProgress_SendTextNoti);

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s))
                    btSend.setVisibility(View.VISIBLE);
                else
                    btSend.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SendTextNotificationRequest request = new SendTextNotificationRequest();

                request.setTourId(id);
                request.setNoti(etText.getText().toString());

                Call<SendTextNotiResponse> call = Global.userService.Send_Text_Noti(Global.userToken, request);

                call.enqueue(new Callback<SendTextNotiResponse>() {
                    @Override
                    public void onResponse(Call<SendTextNotiResponse> call, Response<SendTextNotiResponse> response) {
                        if(response.code()!=200) {
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());
                                Toast.makeText(TourProgressActivity.this,
                                        "Error: " + error.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        etText.setText("");
                        loadTextNoti();
                    }

                    @Override
                    public void onFailure(Call<SendTextNotiResponse> call, Throwable t) {
                        Toast.makeText(TourProgressActivity.this,
                                "Unexpected error: " + t.toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TourProgressActivity.class.getSimpleName(), t.getMessage());
                    }
                });
            }
        });

        // -------------------------------------------------------------------------------------------------------------
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.762910, 106.682099), zoomLevel));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        if(tourStopPoints != null){
            for(StopPoints p : tourStopPoints){
                stopPointMarkerOption.position(new LatLng(p.getLat(), p.getLong()));

                String title = "";

                if(p.getServiceTypeId() == 1){
                    title = "Restaurant " + p.getName();
                    stopPointMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_restaurant));
                }
                else if(p.getServiceTypeId() == 2){
                    title = "Hotel " + p.getName();
                    stopPointMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_hotel));
                }
                else if(p.getServiceTypeId() == 3){
                    title = "Rest station " + p.getName();
                    stopPointMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_rest));
                }
                else {
                    title = p.getName();
                    stopPointMarkerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_other));
                }

                stopPointMarkerOption.title(title);
                stopPointMarkerOption.snippet(p.getAddress());
                mMap.addMarker(stopPointMarkerOption);
            }
        }

        mScrollView = findViewById(R.id.sv_MapTP); //parent scrollview in xml, give your scrollview id value

        ((WorkAroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTP))
                .setListener(new WorkAroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch()
                    {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (selectedLocationMark!=null)
                    if(marker != selectedLocationMark) {
                       btAddNotiOnRoad.setVisibility(View.INVISIBLE);
                        selectedLocationMark.remove();
                    }

                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                btAddNotiOnRoad.setVisibility(View.VISIBLE);
                if(selectedLocationMark!=null) {
                    selectedLocationMark.remove();
                    selectedLocationMark = null;
                }

                selectedLocation = latLng;
                selectedLocationmarkerOptions.position(selectedLocation);

                Geocoder geocoder = new Geocoder(TourProgressActivity.this);
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

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

                selectedLocationmarkerOptions.title("Selected point");
                selectedLocationmarkerOptions.snippet(addressText);
                selectedLocationmarkerOptions.position(latLng);
                selectedLocationMark = mMap.addMarker(selectedLocationmarkerOptions);
                selectedLocationMark.showInfoWindow();
            }
        });

        selfRefesh.scheduleAtFixedRate(
                new TimerTask()
                {
                    public void run()
                    {
                        fetchLocation();
                        loadNotiOnRoad();
                        loadTextNoti();
                    }
                },
                10000,      // delay 1st run 10s
                10000); // 10s repeat timer

        loadNotiOnRoad();
        loadTextNoti();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    point_location = new LatLng(location.getLatitude(), location.getLongitude());
                    if (mMap != null) {
                        if(mark != null){
                            mark.remove();
                            mark = null;
                        }

                        if(!movedOnce) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point_location, zoomLevel));
                            movedOnce = true;
                        }
                        markerOptions.position(point_location);
                        Geocoder geocoder = new Geocoder(TourProgressActivity.this);

                        List<Address> addresses = null;
                        String addressText="";

                        try {
                            addresses = geocoder.getFromLocation(point_location.latitude, point_location.longitude,1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(addresses != null && addresses.size() > 0 ){
                            Address address = addresses.get(0);

                            addressText = address.getAddressLine(0);
                        }

                        markerOptions.title("Your location");
                        markerOptions.snippet(addressText);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        markerOptions.position(point_location);
                        mark = mMap.addMarker(markerOptions);
                    }


                    final GetCoordinateUsersRequest request = new GetCoordinateUsersRequest();

                    request.setLat(point_location.latitude);
                    request.setLongg(point_location.longitude);
                    request.setTourId(id);
                    request.setUserId(Global.userId);

                    Call<ArrayList<UserCoord>> call = Global.userService.Get_Coordinate_Users(Global.userToken, request);

                    call.enqueue(new Callback<ArrayList<UserCoord>>() {
                        @Override
                        public void onResponse(Call<ArrayList<UserCoord>> call, Response<ArrayList<UserCoord>> response) {
                            if(response.code()!=200) {
                                try {
                                    JSONObject error = new JSONObject(response.errorBody().string());
                                    Toast.makeText(TourProgressActivity.this,
                                            "Error: " + error.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }

                            ArrayList<UserCoord> userCoords = response.body();

                            if(userMarker_arr != null) {
                                for (Marker mr : userMarker_arr) {
                                    mr.remove();
                                }

                                userMarker_arr.clear();
                            }
                            userMarker_arr = new ArrayList<>();

                            for(UserCoord uc : userCoords){
                                if(uc.getId() == Global.userId)
                                    continue;
                                point_location = new LatLng(uc.getLat(), uc.getLng());

                                markerOptions.position(point_location);
                                Geocoder geocoder = new Geocoder(TourProgressActivity.this);

                                List<Address> addresses = null;
                                String addressText="";

                                try {
                                    addresses = geocoder.getFromLocation(point_location.latitude, point_location.longitude,1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if(addresses != null && addresses.size() > 0 ){
                                    Address address = addresses.get(0);

                                    addressText = address.getAddressLine(0);
                                }

                                String name = "";
                                for(Members m : tourMembers)
                                    if(m.getId() == uc.getId()) {
                                        name = m.getName();
                                        break;
                                    }

                                markerOptions.title(name);
                                markerOptions.snippet(addressText);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                markerOptions.position(point_location);
                                userMarker_arr.add(mMap.addMarker(markerOptions));
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<UserCoord>> call, Throwable t) {

                        }
                    });
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }



    private void loadNotiOnRoad(){
        Call<GetNotificationOnRoadResponse> call
                = Global.userService.Get_Notication_OnRoad_TourId(Global.userToken, id, 1, "10000");

        call.enqueue(new Callback<GetNotificationOnRoadResponse>() {
            @Override
            public void onResponse(Call<GetNotificationOnRoadResponse> call, Response<GetNotificationOnRoadResponse> response) {
                if(response.code()!=200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(TourProgressActivity.this,
                                "Error: " + error.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                if(response.body().getNotiList() != null && response.body().getNotiList().size() > 0){
                    if(onRoadNoti_list!=null)
                        onRoadNoti_list.clear();
                    onRoadNoti_list = response.body().getNotiList();

                    for(Marker m : marker_arr)
                        m.remove();
                    marker_arr.clear();

                    for(OnRoadNoti noti : onRoadNoti_list){
                        if(noti.getNotificationType() == 1)
                            addType1(new LatLng(noti.getLat(), noti.getLng()), noti.getNote());
                        else if (noti.getNotificationType() == 2)
                            addType2(new LatLng(noti.getLat(), noti.getLng()), noti.getNote());
                        else if (noti.getNotificationType() == 3)
                            addType3(new LatLng(noti.getLat(), noti.getLng()), noti.getNote(), noti.getSpeed());
                    }
                }

            }

            @Override
            public void onFailure(Call<GetNotificationOnRoadResponse> call, Throwable t) {
                Toast.makeText(TourProgressActivity.this,
                        "Unexpected error: " + t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TourProgressActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }

    void loadTextNoti(){
        Call<GetNotificationListResponse> call =
                Global.userService.Get_Notification_List(Global.userToken, id, 1, "10000");

        call.enqueue(new Callback<GetNotificationListResponse>() {
            @Override
            public void onResponse(Call<GetNotificationListResponse> call, Response<GetNotificationListResponse> response) {
                if(response.code()!=200) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(TourProgressActivity.this,
                                "Error: " + error.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                if(adapter_list!=null)
                    adapter_list.clear();

                adapter_list = response.body().getNotiList();

                if(adapter_list!=null && adapter_list.size() > 0){
                    adapter = new TextNotiAdapter(adapter_list, TourProgressActivity.this);

                    lvTextNoti.setAdapter(adapter);

                    lvTextNoti.setOnTouchListener(new View.OnTouchListener() {
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
                }
            }

            @Override
            public void onFailure(Call<GetNotificationListResponse> call, Throwable t) {
                Toast.makeText(TourProgressActivity.this,
                        "Unexpected error: " + t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TourProgressActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }

    private void addType1(LatLng location, String note){
        markerOptions.title("Police position");
        markerOptions.snippet(note);
        markerOptions.position(location);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.police));
        marker_arr.add(mMap.addMarker(markerOptions));
    }

    private void addType2(LatLng location, String note){
        markerOptions.title("Problem on road");
        markerOptions.snippet(note);
        markerOptions.position(location);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
        marker_arr.add(mMap.addMarker(markerOptions));
    }

    private void addType3(LatLng location, String note, int speed){
        markerOptions.title("Speed limit: " + speed);
        markerOptions.snippet(note);
        markerOptions.position(location);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.speed_limit));
        marker_arr.add(mMap.addMarker(markerOptions));
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        public MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.map_info_view, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tvType, tvNote;
            tvType = myContentsView.findViewById(R.id.map_info_type);
            tvNote = myContentsView.findViewById(R.id.map_info_note);

            tvType.setText(marker.getTitle());

            tvNote.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CreateOnRoadNotiCode)
            if(resultCode == Activity.RESULT_OK) {
                loadNotiOnRoad();
                selectedLocationMark.remove();
                selectedLocationMark = null;
            }
    }
}
