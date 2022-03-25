package com.androidproject.travelassistant.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.StopPoints;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetSuggestedDestinationFromPointRequest;
import com.androidproject.travelassistant.Request.GetSuggestedDestinationResponse;
import com.androidproject.travelassistant.Utility.StringCursorAdapter;
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
import com.google.android.libraries.places.api.Places;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    float zoomLevel = 16.0f;
    private GoogleMap mMap = null;
    private Toolbar toolbar;
    private SearchView search;
    private LatLng point_location = null;
    private Marker mark = null;
    private MarkerOptions markerOptions;

    private Button bOk;
    private SearchView actSearch;
    private ImageView ivPlaceHolder;
    private ImageButton ibMyLocation;

    private boolean moved, markerCliked;

    private boolean suggestPoint;

    private Marker choseSuggestedPoint;

    private ArrayList<SuggestedPoint> suggestedPoints = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        moved = false;
        markerCliked = false;

        suggestPoint = getIntent().getBooleanExtra("needSuggestion", false);


        ivPlaceHolder = findViewById(R.id.iv_placeHolder);
        ibMyLocation = findViewById(R.id.ib_GPS_Location);
        bOk = findViewById(R.id.bOk_GoogleMap);

        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(!suggestPoint || choseSuggestedPoint == null) {
                    intent.putExtra("lat", point_location.latitude);
                    intent.putExtra("long", point_location.longitude);
                    intent.putExtra("addr", markerOptions.getTitle());
                }
                else if(suggestPoint){
                    StopPoints chosen = null;
                    for(SuggestedPoint p : suggestedPoints)
                        if(p.getMarker().equals(choseSuggestedPoint)) {
                            chosen = p.getPoint();
                            break;
                        }

                    if(chosen == null){
                        Toast.makeText(GoogleMapsActivity.this,
                                "Error when reading chosen stop point, please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    intent.putExtra("isSuggested", true);
                    intent.putExtra("lat", chosen.getLat());
                    intent.putExtra("long", chosen.getLong());
                    intent.putExtra("addr", chosen.getAddress());
                    intent.putExtra("name", chosen.getName());
                    intent.putExtra("provinceId", Utility.findProvinceIdByAddress(chosen.getAddress()));
                    intent.putExtra("serviceId", chosen.getId());
                    intent.putExtra("serviceTypeId", chosen.getServiceTypeId());
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        ibMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        markerOptions = new MarkerOptions();

        actSearch = findViewById(R.id.svGoogleMap);

        actSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Geocoder geo = new Geocoder(getBaseContext());
                try {
                    List<Address> r_add = geo.getFromLocationName(query, 1);
                    if (r_add.size() < 0){
                        Toast.makeText(GoogleMapsActivity.this, "Cannot find places", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        String title = getIntent().getStringExtra("Title");
        if(TextUtils.isEmpty(title))
            title = "Choose a location";
        toolbar = findViewById(R.id.tbGoogleMap);
        toolbar.setTitle(title);

        search = findViewById(R.id.svGoogleMap);
        search.setSuggestionsAdapter(new StringCursorAdapter
                (GoogleMapsActivity.this, new MatrixCursor(new String[] { "_id", "addr"}), search));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Geocoder geocoder = new Geocoder(getBaseContext());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(query, 1);
                    if (addresses.size() <= 0)
                        return false;

                    if(mark != null) mark.remove();

                    Address address = addresses.get(0);
                    point_location = new LatLng(address.getLatitude(), address.getLongitude());
                    mark = mMap.addMarker(new MarkerOptions().position(point_location).title("Selected position"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point_location, zoomLevel));
                    // tvPlace.setText(address.getAddressLine(0));
                    search.getSuggestionsAdapter().changeCursor(null);
                    search.clearFocus();

                }
                catch (IOException e){
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new SuggestLocation(getBaseContext()).execute(newText);
                return false;
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.762910, 106.682099), zoomLevel));

        if (point_location != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point_location, zoomLevel));
            markerOptions.position(point_location);
            new ReverseGeocodingTask(getBaseContext()).execute(point_location);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(suggestPoint)
                    choseSuggestedPoint = marker;

                markerCliked = true;
                moved = false;
                return false;
            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                bOk.setVisibility(View.GONE);
                ivPlaceHolder.setVisibility(View.VISIBLE);
                ivPlaceHolder.bringToFront();
                if (markerCliked) {
                    markerCliked = false;
                    return;
                }
                moved = true;
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                bOk.setVisibility(View.VISIBLE);
                ivPlaceHolder.setVisibility(View.INVISIBLE);
                if(moved) {
                    point_location = mMap.getCameraPosition().target;
                    if(suggestPoint)
                        choseSuggestedPoint = null;
                    if (mark != null) {
                        mark.remove();
                    }
                    markerOptions.position(point_location);
                    new ReverseGeocodingTask(getBaseContext()).execute(point_location);
                    moved = false;

                    if(suggestPoint){
                        for(SuggestedPoint p : suggestedPoints)
                            p.getMarker().remove();
                        suggestedPoints.clear();
                        suggestedPoints = new ArrayList<>();

                        final GetSuggestedDestinationFromPointRequest request = new GetSuggestedDestinationFromPointRequest();
                        request.setHasOneCoordinate(true);
                        request.setCoordList(point_location.latitude, point_location.longitude);

                        Call<GetSuggestedDestinationResponse> call = Global.userService.get_suggested_destination_from_point(Global.userToken, request);

                        call.enqueue(new Callback<GetSuggestedDestinationResponse>() {
                            @Override
                            public void onResponse(Call<GetSuggestedDestinationResponse> call, Response<GetSuggestedDestinationResponse> response) {
                                if (response.code() != 200) {
                                    try {
                                        JSONObject error = new JSONObject(response.errorBody().string());
                                        Toast.makeText(GoogleMapsActivity.this,
                                                "Error on loading suggested points", Toast.LENGTH_LONG).show();
                                        Log.d(TourInfoActivity.class.getSimpleName() + " load_suggested_Failed",
                                                "Code " + response.code() + ": " + error.getString("message"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    return;
                                }

                                ArrayList<StopPoints> stopPoints = response.body().getStopPoints();

                                MarkerOptions newMark = new MarkerOptions();

                                for (StopPoints point : stopPoints){
                                    newMark.position(new LatLng(point.getLat(), point.getLong()));
                                    newMark.title(point.getName());

                                    if(point.getServiceTypeId() == 1){

                                        newMark.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_restaurant));
                                    }
                                    else if(point.getServiceTypeId() == 2){

                                        newMark.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_hotel));
                                    }
                                    else if(point.getServiceTypeId() == 3){

                                        newMark.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_rest));
                                    }
                                    else {

                                        newMark.icon(BitmapDescriptorFactory.fromResource(R.drawable.sp_other));
                                    }

                                    Marker marker;
                                    marker = mMap.addMarker(newMark);

                                    SuggestedPoint newPoint = new SuggestedPoint();
                                    newPoint.setMarker(marker);
                                    newPoint.setPoint(point);
                                    suggestedPoints.add(newPoint);
                                }

                            }

                            @Override
                            public void onFailure(Call<GetSuggestedDestinationResponse> call, Throwable t) {
                                Toast.makeText(GoogleMapsActivity.this,
                                        "Unexpected error on loading suggested points", Toast.LENGTH_LONG).show();
                                Log.d(TourInfoActivity.class.getSimpleName() + " load_suggested_Failed",
                                        t.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
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
            if (mark != null) {
                mark.remove();
            }

            markerOptions.title(addressText);
            mark = mMap.addMarker(markerOptions);
            mark.showInfoWindow();
        }
    }

    private class SuggestLocation extends AsyncTask<String, Void, Cursor> {
        Context mContext;

        public SuggestLocation(Context context){
            super();
            mContext = context;
        }

        @Override
        protected Cursor doInBackground(String... params) {
            String str = params[0];
            Geocoder geocoder = new Geocoder(mContext);
            MatrixCursor cursor = new MatrixCursor(new String[] { "_id", "addr"});

            try{
                List<Address> add_list = geocoder.getFromLocationName(str, 5);

                for(int i = 0; i < add_list.size(); i++){
                    Address t = add_list.get(i);
                    if(!TextUtils.equals(t.getAddressLine(0), str))
                        cursor.addRow(new Object[]{ i, t.getAddressLine(0) } );
                }

            } catch(IOException e){
                e.printStackTrace();
            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            search.getSuggestionsAdapter().changeCursor(result);
        }

        @Override
        protected void onPreExecute() {

        }
    }

    private class SuggestedPoint{
        Marker marker;
        StopPoints point;

        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }

        public StopPoints getPoint() {
            return point;
        }

        public void setPoint(StopPoints point) {
            this.point = point;
        }
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

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point_location, zoomLevel));
                        markerOptions.position(point_location);
                        new ReverseGeocodingTask(getBaseContext()).execute(point_location);
                    }
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
}
