package com.androidproject.travelassistant.AppData;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.RemoveStopPointResponse;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.View.AddStopPointActivity;
import com.androidproject.travelassistant.View.TourInfoActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StopPointAdapter extends BaseAdapter {
    private ArrayList<StopPoints> spList;
    Context context;
    boolean showDelete;

    public StopPointAdapter(ArrayList<StopPoints> spList, Context context) {
        this.spList = spList;
        this.context = context;
        showDelete = true;
    }

    public ArrayList<StopPoints> getSpList() {
        return spList;
    }

    public void setSpList(ArrayList<StopPoints> spList) {
        this.spList = spList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return spList.size();
    }

    @Override
    public StopPoints getItem(int position) {
        return spList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDeleteButton(boolean show){
        showDelete = show;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.stop_point_view, null);
        TextView tvName = v.findViewById(R.id.tv_nameStopPoints);
        Button ibDel = v.findViewById(R.id.ib_delStopPoints);
        ImageView iv = v.findViewById(R.id.iv_StopPointAvatar);
        TextView tvAddr = v.findViewById(R.id.tv_addrStopPoint);
        TextView tvTime = v.findViewById(R.id.tv_arrive_leave_StopPoint);
        TextView tvCost = v.findViewById(R.id.tv_cost_StopPoint);

        final StopPoints p = spList.get(position);

        tvName.setText(p.getName());
        if(!TextUtils.isEmpty(p.getAddress()))
            tvAddr.setText(p.getAddress());
        else{
            new ReverseGeocodingTask(v.getContext(), tvAddr).execute(new LatLng(p.getLat(), p.getLong()));
        }

        tvTime.setText(Utility.MillisToDate(p.getArrivalAt()));
        tvTime.append(" - ");
        tvTime.append(Utility.MillisToDate(p.getLeaveAt()));

        tvCost.setText("Cost: ");
        tvCost.append(String.valueOf(p.getMinCost()));
        tvCost.append(" - ");
        tvCost.append(String.valueOf(p.getMaxCost()));

        if(!TextUtils.isEmpty(p.getAvatar())){
            new DownloadImageTask(iv).execute(p.getAvatar());
        }
        else{
            if (p.getServiceTypeId() == 1)
                iv.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_sp_restaurant));
            if (p.getServiceTypeId() == 2)
                iv.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_sp_hotel));
            if (p.getServiceTypeId() == 3)
                iv.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_sp_rest));
        }

        final int pos = position;

        if(showDelete) {
            ibDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete stop point \"" + p.getName() + "\"?")
                            .setMessage("Permanently delete this stop point from the tour?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<RemoveStopPointResponse> call = Global.userService.Get_Remove_Stop_Point(Global.userToken,
                                            String.valueOf(spList.get(pos).getId()));

                                    call.enqueue(new Callback<RemoveStopPointResponse>() {
                                        @Override
                                        public void onResponse(Call<RemoveStopPointResponse> call, Response<RemoveStopPointResponse> response) {
                                            if (response.code() != 200) {
                                                try {
                                                    JSONObject error = new JSONObject(response.errorBody().string());
                                                    Utility.showErrorDialog(getContext(), "Error" + response.code(),
                                                            error.getString("message"));
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                return;
                                            }

                                            spList.remove(pos);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Call<RemoveStopPointResponse> call, Throwable t) {
                                            Toast.makeText(getContext(),
                                                    "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d(TourInfoActivity.class.getSimpleName() + " delStopPoint", t.getMessage());
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();

                    dialog.show();
                }
            });
        }
        else
            ibDel.setVisibility(View.INVISIBLE);

        return v;
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;
        TextView addr;

        public ReverseGeocodingTask(Context context, TextView addr){
            super();
            mContext = context;
            this.addr = addr;
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
            addr.setText(addressText);
        }
    }
}
