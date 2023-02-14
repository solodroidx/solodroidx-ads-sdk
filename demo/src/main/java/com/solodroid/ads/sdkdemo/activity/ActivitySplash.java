package com.solodroid.ads.sdkdemo.activity;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.solodroid.ads.sdk.format.AdNetwork;
import com.solodroid.ads.sdkdemo.BuildConfig;
import com.solodroid.ads.sdkdemo.callback.CallbackConfig;
import com.solodroid.ads.sdkdemo.data.Constant;
import com.solodroid.ads.sdkdemo.application.MyApplication;
import com.solodroid.ads.sdkdemo.R;
import com.solodroid.ads.sdkdemo.database.SharedPref;
import com.solodroid.ads.sdkdemo.rest.RestAdapter;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {
    private static final String TAG = "ActivitySplash";
    Call<CallbackConfig> callbackConfigCall = null;
    public static int DELAY_PROGRESS = 1500;
    Application application;
    AdNetwork.Initialize adNetwork;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPref = new SharedPref(this);
        initAds();

        application = getApplication();

        new Handler().postDelayed(() -> {
            switch (Constant.AD_NETWORK) {
                case ADMOB:
                case GOOGLE_AD_MANAGER:
                case APPLOVIN:
                case APPLOVIN_MAX:
                    ((MyApplication) application).showAdIfAvailable(ActivitySplash.this, this::requestConfig);
                    break;
                default:
                    requestConfig();
                    break;
            }
        }, DELAY_PROGRESS);

    }

    private void requestConfig() {
        requestAPI("https://raw.githubusercontent.com/solodroidev/content/uploads/json/android.json");
    }

    private void requestAPI(@SuppressWarnings("SameParameterValue") String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            if (url.contains("https://drive.google.com")) {
                String driveUrl = url.replace("https://", "").replace("http://", "");
                List<String> data = Arrays.asList(driveUrl.split("/"));
                String googleDriveFileId = data.get(3);
                callbackConfigCall = RestAdapter.createApi().getDriveJsonFileId(googleDriveFileId);
            } else {
                callbackConfigCall = RestAdapter.createApi().getJsonUrl(url);
            }
        } else {
            callbackConfigCall = RestAdapter.createApi().getDriveJsonFileId(url);
        }
        callbackConfigCall.enqueue(new Callback<CallbackConfig>() {
            public void onResponse(@NonNull Call<CallbackConfig> call, @NonNull Response<CallbackConfig> response) {
                CallbackConfig resp = response.body();
                if (resp != null) {
                    sharedPref.savePostList(resp.android);
                    startMainActivity();
                    Log.d(TAG, "responses success");
                } else {
                    startMainActivity();
                    Log.d(TAG, "responses null");
                }
            }

            public void onFailure(@NonNull Call<CallbackConfig> call, @NonNull Throwable th) {
                Log.d(TAG, "responses failed: " + th.getMessage());
                startMainActivity();
            }
        });
    }

    private void initAds() {
        adNetwork = new AdNetwork.Initialize(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setAdMobAppId(null)
                .setStartappAppId(Constant.STARTAPP_APP_ID)
                .setUnityGameId(Constant.UNITY_GAME_ID)
                .setAppLovinSdkKey(getResources().getString(R.string.applovin_sdk_key))
                .setIronSourceAppKey(Constant.IRONSOURCE_APP_KEY)
                .setDebug(BuildConfig.DEBUG)
                .build();
    }

    public void startMainActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, DELAY_PROGRESS);
    }

}
