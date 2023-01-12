package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.FAN;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_ADMOB;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.NONE;
import static com.solodroid.ads.sdk.util.Constant.STARTAPP;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAppOpenAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.util.Constant;
import com.solodroid.ads.sdk.util.NativeTemplateStyle;
import com.solodroid.ads.sdk.util.Tools;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppOpenAdMax {

    private static final String LOG_TAG = "AppOpenAd";
    public final MaxAppOpenAd appOpenAd;
    public final Context context;
    public boolean isShowingAd = false;

    public AppOpenAdMax(final Context context, String maxAppOpenAdUnitId) {
        this.context = context;
        appOpenAd = new MaxAppOpenAd(maxAppOpenAdUnitId, context);
        appOpenAd.loadAd();
    }

    public void showAdIfAvailable() {

        if (isShowingAd) {
            Log.d(LOG_TAG, "The max app open ad is already showing.");
            return;
        }

        if (appOpenAd == null || !AppLovinSdk.getInstance(context).isInitialized()) return;

        if (appOpenAd.isReady()) {
            isShowingAd = true;
            appOpenAd.showAd();
        } else {
            appOpenAd.loadAd();
        }

        appOpenAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(final MaxAd ad) {
            }

            @Override
            public void onAdLoadFailed(final String adUnitId, final MaxError error) {
                isShowingAd = false;
            }

            @Override
            public void onAdDisplayed(final MaxAd ad) {
            }

            @Override
            public void onAdClicked(final MaxAd ad) {
            }

            @Override
            public void onAdHidden(final MaxAd ad) {
                isShowingAd = false;
                appOpenAd.loadAd();
            }

            @Override
            public void onAdDisplayFailed(final MaxAd ad, final MaxError error) {
                isShowingAd = false;
                appOpenAd.loadAd();
            }
        });
    }

}
