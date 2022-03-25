package com.androidproject.travelassistant.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.OTPPasswordRecoveryRequest;
import com.androidproject.travelassistant.Request.OTPPasswordRecoveryResponse;
import com.androidproject.travelassistant.Request.SearchUserResponse;
import com.androidproject.travelassistant.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

public class PasswordResetRequestActivity extends AppCompatActivity implements View.OnClickListener{
    private RadioGroup radiog_option;
    private RadioButton radiob_SMS;
    private RadioButton radiob_Email;
    private EditText etSubmit;
    private Button btSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_request);

        this.etSubmit = findViewById(R.id.etSubmit);

        Toolbar tbPassword = (Toolbar) findViewById(R.id.toolbarPassword);
        tbPassword.setTitle("Forgot Password");
        setSupportActionBar(tbPassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        radiob_SMS = findViewById(R.id.rb_SMS);
        radiob_Email = findViewById(R.id.rb_Email);

        btSubmit = findViewById(R.id.bSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog passReset_loading = new SpotsDialog.Builder()
                        .setContext(PasswordResetRequestActivity.this)
                        .setMessage("Requesting, please wait...")
                        .setTheme(R.style.Custom_ProgressDialog)
                        .setCancelable(false)
                        .build();

                passReset_loading.show();

                final OTPPasswordRecoveryRequest request = new OTPPasswordRecoveryRequest();

                if(radiob_SMS.isChecked())
                    request.setType("phone");
                else if(radiob_Email.isChecked())
                    request.setType("email");
                request.setValue(etSubmit.getText().toString());

                Call<OTPPasswordRecoveryResponse> call = Global.userService.OTP_Password(Global.userToken,request);
                call.enqueue(new Callback<OTPPasswordRecoveryResponse>() {
                    @Override
                    public void onResponse(Call<OTPPasswordRecoveryResponse> call, Response<OTPPasswordRecoveryResponse> response) {
                        if (response.code()!=200){
                            if(response.code() == 404)
                            {
                                etSubmit.setError("Wrong OTP or your OTP expired");
                            }
                            try {
                                JSONObject error = new JSONObject(response.errorBody().string());

                                Utility.showErrorDialog(PasswordResetRequestActivity.this,
                                        "Error " + response.code(),
                                        error.getString("message"));

                            } catch (IOException e){

                            } catch (JSONException e){

                            }

                            passReset_loading.dismiss();
                            return;
                        }
                        final Intent intent = new Intent(getBaseContext(), PasswordResetActivity.class);

                        final Call<SearchUserResponse> call1 = Global.userService.get_search_user(request.getValue(), 1, "10");
                        call1.enqueue(new Callback<SearchUserResponse>() {
                            @Override
                            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                                if(response.code()!=200) {
                                    try {
                                        JSONObject error = new JSONObject(response.errorBody().string());
                                        Utility.showErrorDialog(PasswordResetRequestActivity.this, "Error" + response.code(),
                                                error.getString("message"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    passReset_loading.dismiss();
                                    return;
                                }

                                intent.putExtra("userId", response.body().getUsers().get(0).getId());
                                intent.putExtra("fullName",response.body().getUsers().get(0).getFullName());
                                passReset_loading.dismiss();
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                                passReset_loading.dismiss();
                                Utility.showErrorDialog(PasswordResetRequestActivity.this, "Unexpected Error: Cannot find user" , call1.request().url().toString() + "\n" + t.getMessage());
                                Log.d(PasswordResetRequestActivity.class.getSimpleName() + " get_search_user ", call1.request().url().toString() + " | " + t.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<OTPPasswordRecoveryResponse> call, Throwable t) {
                        passReset_loading.dismiss();
                        Utility.showErrorDialog(PasswordResetRequestActivity.this, "Unexpected Error: Cannot send request" , t.getMessage());
                        Log.d(PasswordResetRequestActivity.class.getSimpleName() + " OTP_Password ", t.getMessage());
                    }
                });


            }
        });
    }

    @Override
    public void onClick(View v) {
        boolean check = ((RadioButton) v).isChecked();
        switch(v.getId()) {
            case R.id.rb_SMS:
                if (check) {
                    etSubmit.setHint("Enter your phone number");
                    etSubmit.setInputType(TYPE_CLASS_PHONE);
                }
                break;
            case R.id.rb_Email:
                if (check) {
                    etSubmit.setHint("Enter your email address");
                    etSubmit.setInputType(TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                break;
        }
    }

    //Toolbar back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
