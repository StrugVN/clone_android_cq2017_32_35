package com.androidproject.travelassistant.View.Fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.GetUserInfoResponse;
import com.androidproject.travelassistant.Request.UpdatePasswordRequest;
import com.androidproject.travelassistant.Request.UpdatePasswordResponse;
import com.androidproject.travelassistant.Request.UpdateUserAvatarRequest;
import com.androidproject.travelassistant.Request.UpdateUserAvatarResponse;
import com.androidproject.travelassistant.Request.UpdateUserInfoRequest;
import com.androidproject.travelassistant.Request.UpdateUserInfoResponse;
import com.androidproject.travelassistant.Utility.DownloadImageTask;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.Utility.showDatePicker;
import com.androidproject.travelassistant.Utility.showDateTimePicker;
import com.androidproject.travelassistant.View.CreateTourActivity;
import com.androidproject.travelassistant.View.MainScreenActivity;
import com.androidproject.travelassistant.View.SignInActivity;
import com.androidproject.travelassistant.View.TourInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment {
    private boolean loaded_user_info = false;
    private EditText etName, etEmail, etPhone, etAddr, etDob;
    private Spinner sGender;
    private TextView tvLogout, tvSave;
    private ImageButton btEdit, btAvatar;
    private ImageView ivAvatar;
    private Bitmap bitmap;

    private LinearLayout ll_pw;
    private Button bSubmitPw, bExpandPw;
    private EditText etCurPw, etNewPw, etConfNewPw;
    private boolean pwFlag = false;

    private static final int GetImageRequestCode = 0;

    private String imagePath, avatarBase64;
    private String name, email, phone, addr, dob;
    private ImageButton ibDob;

    private AlertDialog updating_user;

    private GetUserInfoResponse user;
    private Calendar newDob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_profile, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updating_user = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage("Updating user info\nPlease wait...")
                .setTheme(R.style.Custom_ProgressDialog_Small)
                .setCancelable(false)
                .build();

        View v = getView();

        Toolbar tbSignUp = v.findViewById(R.id.toolbar_User);
        tbSignUp.setTitle("Profile");

        sGender = v.findViewById(R.id.sGender_UserInfo);
        String[] items = new String[]{"Female", "Male"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        sGender.setAdapter(adapter);

        etName = v.findViewById(R.id.etName_UserInfo);
        etEmail = v.findViewById(R.id.etEmail_UserInfo);
        etPhone = v.findViewById(R.id.etPhone_UserInfo);
        etAddr = v.findViewById(R.id.etAddr_UserInfo);
        etDob = v.findViewById(R.id.etDOB_UserInfo);

        etCurPw = v.findViewById(R.id.et_currentPassword);
        etNewPw = v.findViewById(R.id.et_newPassword);
        etConfNewPw = v.findViewById(R.id.et_confirmNewPassword);
        ll_pw = v.findViewById(R.id.ll_managePassword);
        bExpandPw = v.findViewById(R.id.bt_expandPassword);
        bSubmitPw = v.findViewById(R.id.btSubmitPassword);

        tvLogout = v.findViewById(R.id.tvAskSignOut);
        tvSave = v.findViewById(R.id.tvSaveChanges);

        btEdit = v.findViewById(R.id.iB_editInfo);
        btAvatar = v.findViewById(R.id.iB_editAvt);

        ivAvatar = v.findViewById(R.id.iv_avatar);
        Utility.SpinImageView(ivAvatar);

        ibDob = v.findViewById(R.id.ibDob_UserInfo);

        disableEdit();

        if (!loaded_user_info)
            loadUserInfo();

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Global.lastLoginType = Global.LAST_LOGIN_NONE;
                startActivity(intent);
                getActivity().finish();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEdit();
                updating_user.show();
                tvSave.setVisibility(View.INVISIBLE);
                ibDob.setVisibility(View.INVISIBLE);
                update_user_info();
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdit();
                tvSave.setVisibility(View.VISIBLE);
                ibDob.setVisibility(View.VISIBLE);

                if(TextUtils.equals(getString(R.string.none_given_info), etName.getText().toString()))
                    etName.setText("");

                if(TextUtils.equals(getString(R.string.none_given_info), etEmail.getText().toString()))
                    etEmail.setText("");

                if(TextUtils.equals(getString(R.string.none_given_info), etPhone.getText().toString()))
                    etPhone.setText("");

                if(TextUtils.equals(getString(R.string.none_given_info), etAddr.getText().toString()))
                    etAddr.setText("");

                if(TextUtils.equals(getString(R.string.none_given_info), etDob.getText().toString()))
                    etDob.setText("");
            }
        });

        btAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, GetImageRequestCode);
            }
        });

        ibDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDatePicker(getActivity(), new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        newDob = calendar;
                        etDob.setText(Utility.MillisToDate(newDob.getTimeInMillis(), "yyyy-MM-dd"));
                    }
                }).run();
            }
        });

        bExpandPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bExpandPw.setVisibility(View.GONE);
                ll_pw.setVisibility(View.VISIBLE);
            }
        });

        bSubmitPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    public void changePassword() {
        final AlertDialog password_loading = new SpotsDialog.Builder()
                .setContext(getActivity().getApplicationContext())
                .setMessage(getString(R.string.pw_mng))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        boolean cancel = false;
        if(TextUtils.isEmpty(etCurPw.getText())) {
            etCurPw.setError(getString(R.string.pw_error));
            cancel = true;
        }
        if(TextUtils.isEmpty(etNewPw.getText())) {
            etNewPw.setError(getString(R.string.pw_error));
            cancel = true;
        }
        if(TextUtils.isEmpty(etConfNewPw.getText())) {
            etConfNewPw.setError(getString(R.string.pw_error));
            cancel = true;
        }
        if(!TextUtils.equals(etNewPw.getText(), etConfNewPw.getText())) {
            Toast.makeText(getContext(), "Password confirmation failed", Toast.LENGTH_LONG).show();
            etConfNewPw.setError(getString(R.string.pw_conf_error));
            cancel = true;
        }
        if(cancel){
            password_loading.dismiss();
            return;
        }
        else {
            UpdatePasswordRequest request = new UpdatePasswordRequest();
            String _cur = etCurPw.getText().toString(), _new = etNewPw.getText().toString();

            request.setUserId(user.getId());
            request.setCurrentPassword(_cur);
            request.setNewPassword(_new);

            Call<UpdatePasswordResponse> call = Global.userService.update_password(Global.userToken, request);
            call.enqueue(new Callback<UpdatePasswordResponse>() {
                @Override
                public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
                    if (response.code() == 400) {
                        etCurPw.setError(getString(R.string.cur_pw_error));
                        try {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Log.d(UserProfileFragment.class.getSimpleName() + "update_password_failed",
                                    "Code " + response.code() + ": " + error.getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (response.code() != 200) {
                        try {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(),
                                    "Error: " + error.getString("message"), Toast.LENGTH_LONG).show();
                            Log.d(UserProfileFragment.class.getSimpleName() + "add_stop_point_failed",
                                    "Code " + response.code() + ": " + error.getString("message"));
                            bExpandPw.setVisibility(View.VISIBLE);
                            ll_pw.setVisibility(View.GONE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    etCurPw.setText("");
                    etNewPw.setText("");
                    etConfNewPw.setText("");

                    bExpandPw.setVisibility(View.VISIBLE);
                    ll_pw.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
                    Toast.makeText(getContext(),
                            "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(UserProfileFragment.class.getSimpleName() + " update password", t.getMessage());
                    bExpandPw.setVisibility(View.VISIBLE);
                    ll_pw.setVisibility(View.GONE);
                }
            });
            password_loading.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GetImageRequestCode && resultCode == RESULT_OK && null != data) {
            updating_user.show();
            Uri selectedImage = data.getData();

            ivAvatar.setVisibility(View.VISIBLE);

            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getActivity().managedQuery(selectedImage, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            imagePath = cursor.getString(column_index);

            try {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), selectedImage);
                bitmap = ImageDecoder.decodeBitmap(source);

                new ImageToBase64Task(bitmap).execute();
            } catch (IOException e) {
                updating_user.dismiss();
                e.printStackTrace();
            }

        }
    }

    void upLoadAvatar(){
        final UpdateUserAvatarRequest request = new UpdateUserAvatarRequest();
        request.setFile(avatarBase64);

        Call<UpdateUserAvatarResponse> call = Global.userService.update_avatar(Global.userToken, request);

        call.enqueue(new Callback<UpdateUserAvatarResponse>() {
            @Override
            public void onResponse(Call<UpdateUserAvatarResponse> call, Response<UpdateUserAvatarResponse> response) {
                if(response.code() != 200){
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Utility.showErrorDialog(getActivity(), "Error on updating user avatar: " + response.code(),
                                error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updating_user.dismiss();
                    return;
                }

                Toast.makeText(getActivity(), "User avatar updated", Toast.LENGTH_SHORT).show();
                updating_user.dismiss();
                loadUserInfo();
            }

            @Override
            public void onFailure(Call<UpdateUserAvatarResponse> call, Throwable t) {
                updating_user.dismiss();
                Utility.showErrorDialog(getActivity(), "Unexpected Error",
                        t.getMessage());
            }
        });

    }

    void disableEdit(){
        etName.setFocusable(false);
        sGender.setEnabled(false);
    }

    void enableEdit(){
        etName.setFocusable(true);
        etName.setFocusableInTouchMode(true);
        sGender.setEnabled(true);
        etName.requestFocus();
    }

    void loadUserInfo(){
        final AlertDialog load_user_loading = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage("Retrieving User info\nPlease wait...")
                .setTheme(R.style.Custom_ProgressDialog_Small)
                .setCancelable(false)
                .build();

        load_user_loading.show();

        Call<GetUserInfoResponse> call = Global.userService.get_user(Global.userToken);

        call.enqueue(new Callback<GetUserInfoResponse>() {
            @Override
            public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                if(response.code() != 200){
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Utility.showErrorDialog(getActivity(), "Error on loading user info " + response.code(),
                                error.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    load_user_loading.dismiss();
                    return;
                }

                user = response.body();

                name = user.getFull_name();
                email = user.getEmail();
                phone = user.getPhone();
                addr = user.getAddress();
                dob = user.getDob();

                if(!TextUtils.isEmpty(name))
                    etName.setText(name);
                else
                    etName.setText(getString(R.string.none_given_info));

                if(!TextUtils.isEmpty(email))
                    etEmail.setText(email);
                else
                    etEmail.setText(getString(R.string.none_given_info));

                if(!TextUtils.isEmpty(phone))
                    etPhone.setText(phone);
                else
                    etPhone.setText(getString(R.string.none_given_info));

                if(!TextUtils.isEmpty(addr))
                    etAddr.setText(addr);
                else
                    etAddr.setText(getString(R.string.none_given_info));

                if(!TextUtils.isEmpty(dob))
                    etDob.setText(dob.substring(0,10));
                else
                    etDob.setText(getString(R.string.none_given_info));

                if(!TextUtils.isEmpty(response.body().getAvatar())) {
                    new DownloadImageTask(ivAvatar)
                            .execute(response.body().getAvatar());
                }
                else{
                    Utility.StopSpinImageView(ivAvatar);
                    if(response.body().getGender() == 1) {
                        ivAvatar.setImageDrawable(getActivity().getDrawable(R.drawable.ic_blank_user_avatar_male));
                    }
                    else {
                        ivAvatar.setImageDrawable(getActivity().getDrawable(R.drawable.ic_blank_user_avatar_female));
                    }
                }

                if(response.body().getGender() == 1) {
                    sGender.setSelection(1);
                }
                else {
                    sGender.setSelection(0);
                }



                loaded_user_info = true;
                load_user_loading.dismiss();
            }

            @Override
            public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                load_user_loading.dismiss();
                Utility.showErrorDialog(getActivity(), "Unexpected Error",
                        t.getMessage());
            }
        });
    }

    void update_user_info(){
        String new_name, new_dob;


        new_name = etName.getText().toString();
        new_dob = etDob.getText().toString();

        final UpdateUserInfoRequest request = new UpdateUserInfoRequest();

        if (!TextUtils.isEmpty(new_name))
            request.setFullName(new_name);

        if (!TextUtils.isEmpty(new_dob))
            request.setDob(new_dob);

        if (sGender.getSelectedItemId() == 0)
            request.setGender(0);
        else
            request.setGender(1);

        Call<UpdateUserInfoResponse> call = Global.userService.update_UserInfo(Global.userToken, request);

        call.enqueue(new Callback<UpdateUserInfoResponse>() {
            @Override
            public void onResponse(Call<UpdateUserInfoResponse> call, Response<UpdateUserInfoResponse> response) {
                if(response.code() != 200){
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());

                        Log.d(getActivity().getClass().getSimpleName() + " update user failed",
                                "Code " + response.code() + ": " + error.getString("message"));

                        Utility.showErrorDialog(getActivity(), "Error " + response.code(), error.getString("message"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    updating_user.dismiss();
                    return;
                }

                updating_user.dismiss();
                loadUserInfo();
            }

            @Override
            public void onFailure(Call<UpdateUserInfoResponse> call, Throwable t) {
                Utility.showErrorDialog(getActivity(), "Unexpected Error", t.getMessage());
            }
        });
    }

    private class ImageToBase64Task extends AsyncTask<Void, Void, String> {
        Bitmap bitmap;
        public ImageToBase64Task(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

        @Override
        protected void onPostExecute(String s) {
            avatarBase64 = s;
            upLoadAvatar();
        }
    }
}
