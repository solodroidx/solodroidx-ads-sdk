package com.solodroid.ads.sdkdemo.application;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.solodroid.ads.sdk.format.AppOpenAdAppLovin;
import com.solodroid.ads.sdk.format.AppOpenAdManager;
import com.solodroid.ads.sdk.format.AppOpenAdMob;
import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;
import com.solodroid.ads.sdkdemo.data.Constant;

public class MyApplication extends Application implements ActivityLifecycleCallbacks {

    private AppOpenAdMob appOpenAdMob;
    private AppOpenAdManager appOpenAdManager;
    private AppOpenAdAppLovin appOpenAdAppLovin;
    Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);
        appOpenAdMob = new AppOpenAdMob();
        appOpenAdManager = new AppOpenAdManager();
        appOpenAdAppLovin = new AppOpenAdAppLovin();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    LifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onStart(@NonNull LifecycleOwner owner) {
            if (Constant.isAppOpen) {
                switch (Constant.AD_NETWORK) {
                    case ADMOB:
                        appOpenAdMob.showAdIfAvailable(currentActivity, Constant.ADMOB_APP_OPEN_AD_ID);
                        break;
                    case GOOGLE_AD_MANAGER:
                        appOpenAdManager.showAdIfAvailable(currentActivity, Constant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID);
                        break;
                    case APPLOVIN:
                    case APPLOVIN_MAX:
                        appOpenAdAppLovin.showAdIfAvailable(currentActivity, Constant.APPLOVIN_APP_OPEN_AP_ID);
                        break;
                }
            }
            DefaultLifecycleObserver.super.onStart(owner);
        }
    };

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        switch (Constant.AD_NETWORK) {
            case ADMOB:
                if (!appOpenAdMob.isShowingAd) {
                    currentActivity = activity;
                }
                break;
            case GOOGLE_AD_MANAGER:
                if (!appOpenAdManager.isShowingAd) {
                    currentActivity = activity;
                }
                break;
            case APPLOVIN:
            case APPLOVIN_MAX:
                if (!appOpenAdAppLovin.isShowingAd) {
                    currentActivity = activity;
                }
                break;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        switch (Constant.AD_NETWORK) {
            case ADMOB:
                appOpenAdMob.showAdIfAvailable(activity, Constant.ADMOB_APP_OPEN_AD_ID, onShowAdCompleteListener);
                Constant.isAppOpen = true;
                break;
            case GOOGLE_AD_MANAGER:
                appOpenAdManager.showAdIfAvailable(activity, Constant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID, onShowAdCompleteListener);
                Constant.isAppOpen = true;
                break;
            case APPLOVIN:
            case APPLOVIN_MAX:
                appOpenAdAppLovin.showAdIfAvailable(activity, Constant.APPLOVIN_APP_OPEN_AP_ID, onShowAdCompleteListener);
                Constant.isAppOpen = true;
                break;
        }
    }

}
