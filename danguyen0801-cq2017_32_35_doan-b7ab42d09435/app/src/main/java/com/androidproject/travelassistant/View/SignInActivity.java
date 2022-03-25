package com.androidproject.travelassistant.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.MyApplication;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Request.ThirdPartyLoginRequest;
import com.androidproject.travelassistant.Request.ThirdPartyLoginResponse;
import com.androidproject.travelassistant.Request.LoginRequest;
import com.androidproject.travelassistant.Request.LoginResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private Button bLogin, bReg;
    private ImageButton bGoogleLogin, bFacebookLogin;
    private EditText edID, edPass;
    private GoogleSignInOptions gso;
    private GoogleSignInClient client;
    private CallbackManager callbackManager;    // fb
    private String accessToken; // Google and Fb login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if(getIntent().getBooleanExtra("EXIT_APP", false)){
            finish();
        }

        bLogin = findViewById(R.id.bSignIn);
        edID = findViewById(R.id.etEmailPhone);
        edPass = findViewById(R.id.etPassword);
        bGoogleLogin = findViewById(R.id.ibGoogle);
        bFacebookLogin = findViewById(R.id.ibFacebook);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // Code provided by developers.google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);

        bGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "Logging in with google", Toast.LENGTH_SHORT).show();
                attemptGoogleLogin();
            }
        });

        callbackManager = CallbackManager.Factory.create();

        bFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "Logging in with facebook", Toast.LENGTH_SHORT).show();
                attemptFacebookLogin();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        accessToken = AccessToken.getCurrentAccessToken().getToken();

                        attemptApiLogin_Facebook();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(SignInActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        Toast.makeText(SignInActivity.this, "Error occur when logging in with facebook\n"
                                + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        edID.setText(Global.lastId);
        edPass.setText(Global.lastPass);

        autoLogin();
    }

    private void autoLogin() {
        if(Global.lastLoginType == Global.LAST_LOGIN_BY_GG)
            attemptGoogleLogin();
        else if (Global.lastLoginType == Global.LAST_LOGIN_BY_FB)
            attemptFacebookLogin();
        else if(Global.lastLoginType == Global.LAST_LOGIN_BY_EMAIL)
            attemptLogin();

    }

    //Clickable "Don't have an account?"
    public void onClicktvSignUp(View v) {
        //perform your action here
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    //Clickable "Forgot password?"
    public void onClicktvPassword(View v) {
        //perform your action here
        startActivity(new Intent(SignInActivity.this, PasswordResetRequestActivity.class));
    }

    private void attemptGoogleLogin(){
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    private void googleSignOut() {
        client.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // st
            }
        });
    }

    private void attemptFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data); // FB
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            accessToken = account.getServerAuthCode();

            attemptApiLogin_Google();


            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(SignInActivity.class.getSimpleName(), "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void attemptApiLogin_Google(){
        final AlertDialog login_loading = new SpotsDialog.Builder()
                .setContext(SignInActivity.this)
                .setMessage(getString(R.string.login_processing))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        login_loading.show();

        final ThirdPartyLoginRequest request = new ThirdPartyLoginRequest();
        request.setAccessToken(accessToken);

        final Call<ThirdPartyLoginResponse> call = Global.userService.google_login(request);

        login_loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                call.cancel();
            }
        });

        call.enqueue(new Callback<ThirdPartyLoginResponse>() {
            @Override
            public void onResponse(Call<ThirdPartyLoginResponse> call, Response<ThirdPartyLoginResponse> response) {
                if(response.code() != 200) {
                    switch (response.code()) {
                        case 400:
                            // Will make separate cases later
                            Toast.makeText(SignInActivity.this, getString(R.string.login_3rd_pt_failed), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            // Will change/improve later
                            Toast.makeText(SignInActivity.this, getString(R.string.login_3rd_pt_server_error), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            // Will change/improve later
                            Toast.makeText(SignInActivity.this, "Unknown error, code: " + response.code(), Toast.LENGTH_SHORT).show();
                            try {
                                Toast.makeText(SignInActivity.this, "Message: "
                                        + response.errorBody().string(), Toast.LENGTH_LONG).show();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                    }
                    login_loading.dismiss();
                    return;
                }


                MyApplication app = (MyApplication)SignInActivity.this.getApplication();
                app.setAccessToken(response.body().getToken(), response.body().getId(), Global.LAST_LOGIN_BY_GG);

                login_loading.dismiss();

                openMainScreen();
                //openTourView();
                finish();
            }

            @Override
            public void onFailure(Call<ThirdPartyLoginResponse> call, Throwable t) {
                login_loading.dismiss();
                Toast.makeText(SignInActivity.this,
                        "Unexpected error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(SignInActivity.class.getSimpleName() + " attemptLogin_Google", t.getMessage());
            }
        });
    }

    private void attemptApiLogin_Facebook(){
        final AlertDialog login_loading = new SpotsDialog.Builder()
                .setContext(SignInActivity.this)
                .setMessage(getString(R.string.login_processing))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        login_loading.show();

        final ThirdPartyLoginRequest request = new ThirdPartyLoginRequest();
        request.setAccessToken(accessToken);

        final Call<ThirdPartyLoginResponse> call = Global.userService.facebook_login(request);

        login_loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                call.cancel();
            }
        });

        call.enqueue(new Callback<ThirdPartyLoginResponse>() {
            @Override
            public void onResponse(Call<ThirdPartyLoginResponse> call, Response<ThirdPartyLoginResponse> response) {
                if(response.code() != 200) {
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(SignInActivity.this, getString(R.string.login_3rd_pt_failed), Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            // Will change/improve later
                            Toast.makeText(SignInActivity.this, getString(R.string.login_3rd_pt_server_error), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            // Will change/improve later
                            Toast.makeText(SignInActivity.this, "Unknown error, code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                    login_loading.dismiss();
                    return;
                }
                login_loading.dismiss();
                MyApplication app = (MyApplication)SignInActivity.this.getApplication();
                app.setAccessToken(response.body().getToken(), response.body().getId(), Global.LAST_LOGIN_BY_FB);

                //openTourView();
                openMainScreen();
                finish();
            }

            @Override
            public void onFailure(Call<ThirdPartyLoginResponse> call, Throwable t) {
                login_loading.dismiss();
                Toast.makeText(SignInActivity.this,
                        "Unexpected error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(SignInActivity.class.getSimpleName() + " attemptLogin_Facebook", t.getMessage());
            }
        });
    }

    private void attemptLogin() {
        // Loading Dialog
        final AlertDialog login_loading = new SpotsDialog.Builder()
                .setContext(SignInActivity.this)
                .setMessage(getString(R.string.login_processing))
                .setTheme(R.style.Custom_ProgressDialog)
                .build();

        login_loading.show();

        edID.setError(null);
        edPass.setError(null);

        final String id = edID.getText().toString();
        final String pass = edPass.getText().toString();

        boolean cancel = false;
        View focusView = null;



        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !Global.isPasswordValid(pass)) {
            edPass.setError(getString(R.string.error_password_invalid));
            focusView = edPass;
            cancel = true;
        }

        if (TextUtils.isEmpty(id)) {
            edID.setError(getString(R.string.error_email_empty));
            focusView = edID;
            cancel = true;
        } else if (!Global.isNumberString(id)) {
            if (!Global.isEmailValid(id)) {
                edID.setError(getString(R.string.error_email_invalid));
                focusView = edID;
                cancel = true;
            }
        }

        if(cancel){
            login_loading.dismiss();
            return;
        }
        else{
            final LoginRequest request = new LoginRequest();
            request.setEmailPhone(id);
            request.setPassword(pass);

            final Call<LoginResponse> call = Global.userService.login(request);

            login_loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    call.cancel();
                }
            });

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.code() != 200){
                        switch (response.code()){
                            case 400:
                                edID.setError(getString(R.string.error_login_bad_request));
                                edPass.setError(getString(R.string.error_login_bad_request));
                                break;
                            case 404:
                                edID.setError(getString(R.string.error_login_not_found));
                                edPass.setError(getString(R.string.error_login_not_found));
                                break;
                            default:
                                edID.setError(String.format(Locale.US, getString(R.string.error_login_unknown), response.code()));
                                edPass.setError(String.format(Locale.US, getString(R.string.error_login_unknown), response.code()));


                        }

                        login_loading.dismiss();
                        return;
                    }

                    MyApplication app = (MyApplication)SignInActivity.this.getApplication();
                    app.setAccessToken(response.body().getToken(), response.body().getUserId(),
                            Global.LAST_LOGIN_BY_EMAIL, id, pass);

                    login_loading.dismiss();

                    //openTourView();
                    openMainScreen();
                    finish();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    login_loading.dismiss();
                    edID.setError(getString(R.string.login_failed) + " " + t.getMessage());
                    Log.d(SignInActivity.class.getSimpleName() + "attemptLogin", t.getMessage());
                }


            });

        }
    }

    void openMainScreen(){
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }
}
