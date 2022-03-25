package com.androidproject.travelassistant.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.VerifyOTPPasswordRecoveryRequest;
import com.androidproject.travelassistant.Request.VerifyOTPPasswordRecoveryResponse;
import com.androidproject.travelassistant.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText etOTP, etPassword, etConfirm;
    private Button bReset_ResetPassword;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        etOTP = findViewById(R.id.etOTP);
        etPassword = findViewById(R.id.etPassword_PassReset);
        etConfirm = findViewById(R.id.etConfirm_PassReset);
        bReset_ResetPassword = findViewById(R.id.bReset_ResetPassword);

        TextView tvMess = findViewById(R.id.tvPassResetMess);
        userId = getIntent().getIntExtra("userId", -1);
        final String fullName = getIntent().getStringExtra("fullName");
        tvMess.append(" " + fullName);

        bReset_ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog passReset_loading = new SpotsDialog.Builder()
                        .setContext(PasswordResetActivity.this)
                        .setMessage("Requesting password reset, please wait...")
                        .setTheme(R.style.Custom_ProgressDialog)
                        .setCancelable(false)
                        .build();

                passReset_loading.show();

                boolean cancel = false;
                String otp = etOTP.getText().toString();
                String pass = etPassword.getText().toString();
                String passConf = etConfirm.getText().toString();

                if (!TextUtils.equals(pass, passConf)) {
                    etConfirm.setError(getString(R.string.error_password_not_matched));
                    cancel = true;
                }
                if(TextUtils.isEmpty(otp))
                {
                    etOTP.setError("Otp cannot be empty");
                    cancel = true;
                }
                if(cancel)
                {
                    passReset_loading.dismiss();
                    return;
                }
                else
                {
                    final VerifyOTPPasswordRecoveryRequest request = new VerifyOTPPasswordRecoveryRequest();
                    request.setUserId(userId);
                    request.setNewPassword(pass);
                    request.setVerifyCode(otp);
                    Call<VerifyOTPPasswordRecoveryResponse> call = Global.userService.Verify_OTP_Password(request);

                    call.enqueue(new Callback<VerifyOTPPasswordRecoveryResponse>() {
                        @Override
                        public void onResponse(Call<VerifyOTPPasswordRecoveryResponse> call, Response<VerifyOTPPasswordRecoveryResponse> response) {
                            if (response.code() != 200)
                            {
                                try {
                                    JSONObject error = new JSONObject(response.errorBody().string());
                                    Utility.showErrorDialog(PasswordResetActivity.this, "Error" + response.code(),
                                            error.getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                passReset_loading.dismiss();
                                return;
                            }

                            passReset_loading.dismiss();

                            AlertDialog dialog = new AlertDialog.Builder(PasswordResetActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Password successfully changed for user " + fullName)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }

                        @Override
                        public void onFailure(Call<VerifyOTPPasswordRecoveryResponse> call, Throwable t) {
                            passReset_loading.dismiss();
                            Utility.showErrorDialog(PasswordResetActivity.this, "Error", t.getMessage());
                        }
                    });

                }
            }
        });
    }
}
