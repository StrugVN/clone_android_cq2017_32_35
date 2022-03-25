package com.androidproject.travelassistant.Utility;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.User;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.CreateTourRequest;
import com.androidproject.travelassistant.Request.SearchUserResponse;
import com.androidproject.travelassistant.View.PasswordResetRequestActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utility {
    public static final HashMap<Integer, String> provinceIds = new HashMap<>();

    static public void initProvinceIds(){
        int i = 0;
        provinceIds.put(++i, "Hồ Chí Minh");
        provinceIds.put(++i, "Hà Nội");
        provinceIds.put(++i, "Đà Nẵng");
        provinceIds.put(++i, "Bình Dương");
        provinceIds.put(++i, "Đồng Nai");
        provinceIds.put(++i, "Khánh Hòa");
        provinceIds.put(++i, "Hải Phòng");
        provinceIds.put(++i, "Long An");
        provinceIds.put(++i, "Quảng Nam");
        provinceIds.put(++i, "Bà Rịa Vũng Tàu");
        provinceIds.put(++i, "Đắk Lắk");
        provinceIds.put(++i, "Cần Thơ");
        provinceIds.put(++i, "Bình Thuận");
        provinceIds.put(++i, "Lâm Đồng");
        provinceIds.put(++i, "Thừa Thiên Huế");
        provinceIds.put(++i, "Kiên Giang");
        provinceIds.put(++i, "Bắc Ninh");
        provinceIds.put(++i, "Quảng Ninh");
        provinceIds.put(++i, "Thanh Hóa");
        provinceIds.put(++i, "Nghệ An");
        provinceIds.put(++i, "Hải Dương");
        provinceIds.put(++i, "Gia Lai");
        provinceIds.put(++i, "Bình Phước");
        provinceIds.put(++i, "Hưng Yên");
        provinceIds.put(++i, "Bình Định");
        provinceIds.put(++i, "Tiền Giang");
        provinceIds.put(++i, "Thái Bình");
        provinceIds.put(++i, "Bắc Giang");
        provinceIds.put(++i, "Hòa Bình");
        provinceIds.put(++i, "An Giang");
        provinceIds.put(++i, "Vĩnh Phúc");
        provinceIds.put(++i, "Tây Ninh");
        provinceIds.put(++i, "Thái Nguyên");
        provinceIds.put(++i, "Lào Cai");
        provinceIds.put(++i, "Nam Định");
        provinceIds.put(++i, "Quảng Ngãi");
        provinceIds.put(++i, "Bến Tre");
        provinceIds.put(++i, "Đắk Nông");
        provinceIds.put(++i, "Cà Mau");
        provinceIds.put(++i, "Vĩnh Long");
        provinceIds.put(++i, "Ninh Bình");
        provinceIds.put(++i, "Phú Thọ");
        provinceIds.put(++i, "Ninh Thuận");
        provinceIds.put(++i, "Phú Yên");
        provinceIds.put(++i, "Hà Nam");
        provinceIds.put(++i, "Hà Tĩnh");
        provinceIds.put(++i, "Đồng Tháp");
        provinceIds.put(++i, "Sóc Trăng");
        provinceIds.put(++i, "Kon Tum");
        provinceIds.put(++i, "Quảng Bình");
        provinceIds.put(++i, "Quảng Trị");
        provinceIds.put(++i, "Trà Vinh");
        provinceIds.put(++i, "Hậu Giang");
        provinceIds.put(++i, "Sơn La");
        provinceIds.put(++i, "Bạc Liêu");
        provinceIds.put(++i, "Yên Bái");
        provinceIds.put(++i, "Tuyên Quang");
        provinceIds.put(++i, "Điện Biên");
        provinceIds.put(++i, "Lai Châu");
        provinceIds.put(++i, "Lạng Sơn");
        provinceIds.put(++i, "Hà Giang");
        provinceIds.put(++i, "Bắc Kạn");
        provinceIds.put(++i, "Cao Bằng");
    }

    static public int findProvinceIdByAddress(String Address){
        for(int i=1; i<=63; i++){
            if (Address.contains(provinceIds.get(i)))
                return i;
        }
        return 0;
    }

    static public void SpinImageView(ImageView iv){
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(750);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        iv.startAnimation(rotateAnimation);
    }

    static public void StopSpinImageView(ImageView iv){
        iv.clearAnimation();
    }

    static public void showErrorDialog(Context context, String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(R.layout.error_dialog);
        dialog.setTitle(title);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate( R.layout.error_dialog, null );


        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dl = dialog.create();
        dl.show();

        TextView tv = dl.findViewById(R.id.tvDialogMessage);
        tv.setText(message);
    }


    static public long DateToMillis(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date d = sdf.parse(date);
            return d.getTime();
        }
        catch(ParseException e){
            Log.d(CreateTourRequest.class.getSimpleName(), e.getMessage());

        }
        return -1;
    }

    static public String MillisToDate(long millis){
        DateFormat simple = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm", Locale.US);

        Date result = new Date(millis);

        return simple.format(result);
    }


    static public String MillisToDate(long millis, String format){
        DateFormat simple = new SimpleDateFormat(format, Locale.US);

        Date result = new Date(millis);

        return simple.format(result);
    }

    static public void setEmptyAvatar(String key, final int id, final ImageView iv, final Context context){
        final Call<SearchUserResponse> call1 = Global.userService.get_search_user(key, 1, "10");

        call1.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                if(response.code()!=200) {
                    Log.d("Utility: Find a user", "Server response: Unsuccessful");
                    return;
                }

                for (User u : response.body().getUsers())
                    if(u.getId() == id) {
                        if (u.getGender() == 1){
                            iv.setImageDrawable(context.getDrawable(R.drawable.ic_blank_user_avatar_male));
                        }
                        else{
                            iv.setImageDrawable(context.getDrawable(R.drawable.ic_blank_user_avatar_female));
                        }
                        StopSpinImageView(iv);
                        break;
                    }
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                Log.d("Utility: Find a user", "Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
