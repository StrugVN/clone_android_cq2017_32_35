package com.androidproject.travelassistant.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.SignUpRequest;
import com.androidproject.travelassistant.Request.SignUpResponse;
import com.androidproject.travelassistant.Utility.DateInputMask;
import com.androidproject.travelassistant.Utility.Utility;
import com.androidproject.travelassistant.Utility.showDatePicker;
import com.androidproject.travelassistant.Utility.showDateTimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private TextView tv_Email;
    private TextView tv_Phone;
    private TextView tv_Password;
    private TextView tv_Confirm;
    private RadioButton rb_M;
    private RadioButton rb_F;

    private EditText etName, etMail, etPhone, etAddress, etDob, etPass, etConfPass;
    private Button btSignup;

    private ImageButton ibDob;

    Calendar dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar tbSignUp = (Toolbar)findViewById(R.id.toolbarSignUp);
        tbSignUp.setTitle("Sign Up");
        setSupportActionBar(tbSignUp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //(*)
        SpannableString ss;

        tv_Email = findViewById(R.id.tvEmail_SignUp);
        ss = new SpannableString(getString(R.string.su_email));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_Email.setText(ss);

        tv_Phone = findViewById(R.id.tvPhone_SignUp);
        ss = new SpannableString(getString(R.string.su_phone));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_Phone.setText(ss);

        tv_Password = findViewById(R.id.tvPassword_SignUp);
        ss = new SpannableString(getString(R.string.su_password));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 8, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_Password.setText(ss);

        tv_Confirm = findViewById(R.id.tvConfirm_SignUp);
        ss = new SpannableString(getString(R.string.su_confirm));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 16, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_Confirm.setText(ss);
        // --

        etName = findViewById(R.id.etName_SignUp);
        etMail = findViewById(R.id.etEmail_SignUp);
        etPhone = findViewById(R.id.etPhone_SignUp);
        etAddress = findViewById(R.id.etAddress_SignUp);
        etDob = findViewById(R.id.etDOB_SignUp);
        etPass = findViewById(R.id.etPassword_PassReset);
        etConfPass = findViewById(R.id.etConfirm_PassReset);

        rb_F = findViewById(R.id.rbGenderF_SignUp);
        rb_M = findViewById(R.id.rbGenderM_SignUp);

        btSignup = findViewById(R.id.bSignUp);

        btSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                attempSignUp();
            }
        });

        ibDob = findViewById(R.id.ibDob_SignUp);
        ibDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showDatePicker(SignUpActivity.this, new showDateTimePicker.TaskListener() {
                    @Override
                    public void onFinished(Calendar calendar) {
                        dob = calendar;
                        etDob.setText(Utility.MillisToDate(dob.getTimeInMillis(), "dd/MM/yyyy"));
                    }
                }).run();
            }
        });
    }

    private void attempSignUp(){
        // Loading Dialog
        final AlertDialog signup_loading = new SpotsDialog.Builder()
                .setContext(SignUpActivity.this)
                .setMessage(getString(R.string.sign_up_processing))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        signup_loading.show();

        String name = etName.getText().toString(),
                mail = etMail.getText().toString(),
                phone = etPhone.getText().toString(),
                addr = etAddress.getText().toString(),
                dob = etDob.getText().toString(),
                pass = etPass.getText().toString(),
                conf_pass = etConfPass.getText().toString();
        int gender = -1;    // No gender input
        if(rb_F.isChecked()) gender = 0;
        else if(rb_M.isChecked()) gender = 1;

        boolean cancel = false;

        if (TextUtils.isEmpty(mail)) {
            etMail.setError(getString(R.string.error_email_empty));
            cancel = true;
        } else if (!Global.isEmailValid(mail)) {
            etMail.setError(getString(R.string.error_email_invalid));
            cancel = true;
        }

        if(TextUtils.isEmpty(phone)){
            etPhone.setError(getString(R.string.error_phone_empty));
            cancel = true;
        }
        else if (!Global.isNumberString(phone)){
            etPhone.setError(getString(R.string.error_phone_invalid));
            cancel = true;
        }

        if(!Global.isPasswordValid(pass)){
            etPass.setError(getString(R.string.error_password_invalid));
            cancel = true;
        }

        if(!pass.equals(conf_pass)){
            etConfPass.setError(getString(R.string.error_password_not_matched));
            cancel = true;
        }

        if(cancel){
            signup_loading.hide();
            return;
        }
        else{
            final SignUpRequest request = new SignUpRequest();
            if(!TextUtils.isEmpty(name))
                request.setFullName(name);
            request.setEmail(mail);
            request.setPassword(pass);
            request.setPhone(phone);
            if(!TextUtils.isEmpty(dob)) {
                String day = dob.substring(0,2);
                String month = dob.substring(3,5);
                String year = dob.substring(6);
                request.setDob(year + "-" + month + "-" + day);
            }

            if(gender!=-1)
                request.setGender(gender);
            if(!TextUtils.isEmpty(addr))
                request.setAddress(addr);

            Call<SignUpResponse> call = Global.userService.sign_up(request);

            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    if(response.code() != 200) {
                        switch (response.code()) {
                            case 400:
                                try {
                                    JSONObject error = new JSONObject(response.errorBody().string());
                                    JSONObject message = error.getJSONArray("message").getJSONObject(0);

                                    if(message.getString("param").equals("email"))
                                        etMail.setError(getString(R.string.error_sign_up_dub_email));
                                    if(message.getString("param").equals("phone"))
                                        etPhone.setError(getString(R.string.error_sign_up_dub_phone));
                                } catch(JSONException e){
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(SignUpActivity.class.getSimpleName(), e.getMessage());
                                }
                                catch(IOException t){
                                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(SignUpActivity.class.getSimpleName(), t.getMessage());
                                }
                                break;
                            case 503:
                                // Will change/improve later
                                Toast.makeText(SignUpActivity.this, getString(R.string.error_sign_up_unavailable), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                // Will change/improve later
                                Toast.makeText(SignUpActivity.this, "Unknown error, code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                        signup_loading.hide();
                        return;
                    }

                    // Will change/improve later
                    Toast.makeText(SignUpActivity.this, getString(R.string.sign_up_succeeded), Toast.LENGTH_SHORT).show();

                    signup_loading.hide();
                    finish();
                    return;
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    signup_loading.hide();
                    Toast.makeText(SignUpActivity.this, getString(R.string.sign_up_failed), Toast.LENGTH_SHORT).show();
                    Log.d(SignUpActivity.class.getSimpleName(), t.getMessage());
                }
            });

        }
    }

    //Toolbar back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
