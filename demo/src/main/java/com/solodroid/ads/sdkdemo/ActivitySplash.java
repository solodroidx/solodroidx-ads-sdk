package com.solodroid.ads.sdkdemo;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.solodroid.ads.sdk.format.AdNetwork;

public class ActivitySplash extends AppCompatActivity {

    public static int DELAY_PROGRESS = 1500;
    Application application;
    AdNetwork.Initialize adNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initAds();

        application = getApplication();

        new Handler().postDelayed(() -> {
            switch (Constant.AD_NETWORK) {
                case ADMOB:
                case GOOGLE_AD_MANAGER:
                case APPLOVIN:
                case APPLOVIN_MAX:
                    ((MyApplication) application).showAdIfAvailable(ActivitySplash.this, this::startMainActivity);
                    break;
                default:
                    startMainActivity();
                    break;
            }
        }, DELAY_PROGRESS);

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
