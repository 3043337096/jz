package com.app.ad.biddingsdk;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kwad.sdk.api.KsContentPage;
import com.superad.ad_lib.ADManage;
import com.superad.ad_lib.SuperBannerAD;
import com.superad.ad_lib.SuperFullUnifiedInterstitialAD;
import com.superad.ad_lib.SuperHalfUnifiedInterstitialAD;
import com.superad.ad_lib.SuperKsContentPageAD;
import com.superad.ad_lib.SuperNativeExpressAD;
import com.superad.ad_lib.SuperRewardVideoAD;
import com.superad.ad_lib.SuperSplashAD;
import com.superad.ad_lib.listener.ADInitListener;
import com.superad.ad_lib.listener.AdError;
import com.superad.ad_lib.listener.SuperFullUnifiedInterstitialADListener;
import com.superad.ad_lib.listener.SuperHalfUnifiedInterstitialADListener;
import com.superad.ad_lib.listener.SuperKsContentPageADListener;
import com.superad.ad_lib.listener.SuperNativeADListener;
import com.superad.ad_lib.listener.SuperRewardVideoADListener;
import com.superad.ad_lib.listener.SuperSplashADListener;
import com.superad.ad_lib.listener.SuperUnifiedBannerADListener;

import java.util.Map;

public class AdUtils {

    private static final String TAG= "AdUtils";


    //初始化
    public static void init(Context context,String id,AdListener listener){

        new ADManage().initSDK(context, id, new ADInitListener() {
            @Override
            public void onSuccess() {
                Log.e("~~~~~","init成功");
                listener.onShow();
            }
            @Override
            public void onError(int code, String message) {
                Log.e("~~~~~","init失败");
            }
        });

    }

    //开屏广告
    public static void splashAd(Context mContext, ViewGroup container, Long adId, AdListener listener) {
        SuperSplashAD ad = new SuperSplashAD(mContext,container, adId, new SuperSplashADListener() {

            @Override
            public void onError(AdError adError) {
                listener.onClose();
                Log.e("~~~~~","onError splashAd "+adError);
            }

            @Override
            public void onAdLoad() {
                Log.e("~~~~~","onAdLoad");
            }

            @Override
            public void onADShow() {
                listener.onShow();
                Log.e("~~~~~","onADShow");
            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADDismissed() {
                container.removeAllViews();
                container.setVisibility(View.GONE);
                listener.onClose();
                Log.e("~~~~~","onADDismissed");
            }

            @Override
            public void onAdTypeNotSupport() {
                Log.e("~~~~~","onAdTypeNotSupport");
            }

        });


    }

    //激励视频
    public static void rewardVideo(Activity activity,Long adId, AdListener listener){
        SuperRewardVideoAD mRewardVideoAD = new SuperRewardVideoAD(activity, adId,new SuperRewardVideoADListener() {
            @Override
            public void onError(AdError adError) {
                Log.e("~~~~~", "onError rewardVideo");
            }

            @Override
            public void onADLoad() {
                Log.e("~~~~~", "onADLoad ");
            }

            @Override
            public void onADShow() {
                Log.e("~~~~~", "onADShow");
            }


            @Override
            public void onReward(Map<String, Object> var1) {
                listener.onShow();
                Log.e("~~~~~", "onReward");
            }

            @Override
            public void onADClick() {
                Log.e("~~~~~", "onADClick");
            }

            @Override
            public void onVideoComplete() {
                Log.e("~~~~~", "onVideoComplete");
            }

            @Override
            public void onADClose() {
                listener.onClose();
            }

            @Override
            public void onAdTypeNotSupport() {

            }
        });
    }


    //信息流
    public static void nativeExpressAd(Activity activity,Long adId, ViewGroup container){
        SuperNativeExpressAD ad = new SuperNativeExpressAD(activity, container, adId, new SuperNativeADListener() {

            @Override
            public void onError(AdError adError) {
                Log.e("~~~~~", "onError nativeExpressAd");
            }

            @Override
            public void onADLoad() {
                Log.e("~~~~~", "onADLoad");
            }

            @Override
            public void onADShow() {
                Log.e("~~~~~", "onADShow");
            }

            @Override
            public void onADClick() {
                Log.e("~~~~~", "onADClick");
            }

            @Override
            public void onADClose() {
                Log.e("~~~~~", "onADClose");
            }

            @Override
            public void onRenderFail() {
                Log.e("~~~~~", "onRenderFail");
            }

            @Override
            public void onRenderSuccess() {
                Log.e("~~~~~", "onRenderSuccess");
            }

            @Override
            public void onAdTypeNotSupport() {

            }
        });
    }

    //插屏
    public static void interstitialAd(Activity activity, Long adId, AdListener adListener) {
        SuperHalfUnifiedInterstitialAD ad = new SuperHalfUnifiedInterstitialAD(activity, adId, new SuperHalfUnifiedInterstitialADListener() {
            @Override
            public void onError(AdError adError) {
                Log.e("~~~~~", "onError interstitialAd");
            }

            @Override
            public void onAdLoad() {
                Log.e("~~~~~", "onAdLoad");
            }

            @Override
            public void onAdClicked() {
                Log.e("~~~~~", "onAdClicked");
            }

            @Override
            public void onAdShow() {
                Log.e("~~~~~", "onAdShow");
            }

            @Override
            public void onADClosed() {
                Log.e("~~~~~", "onADClosed");
            }

            @Override
            public void onRenderSuccess() {
                Log.e("~~~~~", "onRenderSuccess");
            }

            @Override
            public void onRenderFail() {
                Log.e("~~~~~", "onRenderFail");
            }

            @Override
            public void onAdTypeNotSupport() {

            }
        });
    }

    //全屏
    public static void fullScreenAd(Activity activity,Long adId, AdListener adListener) {
        SuperFullUnifiedInterstitialAD ad = new SuperFullUnifiedInterstitialAD(activity, adId,new SuperFullUnifiedInterstitialADListener() {

            @Override
            public void onError(AdError adError) {
                Log.e("~~~~", "onError fullScreenAd");
            }

            @Override
            public void onAdLoad() {
                Log.e("~~~~", "onAdLoad");
            }

            @Override
            public void onAdClicked() {
                Log.e("~~~~", "onAdClicked");
            }

            @Override
            public void onAdShow() {
                Log.e("~~~~", "onAdShow");
            }

            @Override
            public void onADClosed() {
                Log.e("~~~~", "onADClosed");

            }

            @Override
            public void onRenderSuccess() {
                Log.e("~~~~", "onRenderSuccess");

            }

            @Override
            public void onRenderFail() {
                Log.e("~~~~", "onRenderFail");
            }

            @Override
            public void onAdTypeNotSupport() {
                Log.e("~~~~", "onAdTypeNotSupport");
            }
        });
    }

    //Banner
    public static void bannerAd(Activity activity,Long adId, ViewGroup container) {
        SuperBannerAD ad = new SuperBannerAD(activity, container, adId, new SuperUnifiedBannerADListener() {

            @Override
            public void onError(AdError adError) {
                Log.e("~~~~", "onError bannerAd");
            }

            @Override
            public void onADLoad() {
                Log.e("~~~~", "onADLoad");
            }

            @Override
            public void onADShow() {
                Log.e("~~~~", "onADShow");
            }

            @Override
            public void onADClick() {
                Log.e("~~~~", "onADClick");
            }

            @Override
            public void onADClose() {
                Log.e("~~~~", "onADClose");
                container.removeAllViews();
            }

            @Override
            public void onRenderFail() {
                Log.e("~~~~", "onRenderFail");
            }

            @Override
            public void onRenderSuccess() {
                Log.e("~~~~", "onRenderSuccess");
            }

            @Override
            public void onAdTypeNotSupport() {
                Log.e("~~~~", "onAdTypeNotSupport");
            }
        });
    }

    public static SuperKsContentPageAD GetsContentPageAD(Context context, Long adId) {
        SuperKsContentPageAD ksContentPageAD = new SuperKsContentPageAD(context, adId, new SuperKsContentPageADListener() {

            /**
             * 进入页面回调
             * @param contentItem 视频/广告对象
             */
            @Override
            public void onPageEnter(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onPageEnter: " );
            }

            /**
             * ⻚⾯可⻅回调
             * @param contentItem 视频/广告对象
             */
            @Override
            public void onPageResume(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onPageResume: " );
            }

            /**
             * ⻚⾯不可⻅回调
             * @param contentItem 视频/广告对象
             */
            @Override
            public void onPagePause(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onPagePause: " );
            }

            /**
             * ⻚⾯离开回调
             * @param contentItem
             */
            @Override
            public void onPageLeave(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onPageLeave: " );
            }

            /**
             * 页面创建成功，拿到视频fragment对象
             * @param fragment
             */
            @Override
            public void onComplete(Fragment fragment) {
                //方式1,如果不需要下面监听的话用此方法
               /* getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .commitNowAllowingStateLoss();*/

            }

            @Override
            public void onAdTypeNotSupport() {

            }

        });

        /*
         * 监听私信好友的视频分享按钮点击
         */
        ksContentPageAD.addShareListener(new KsContentPage.KsShareListener() {
            /**
             * 私信好友点击的回调
             * @param s ⼀个json string，包含三个字段：
             * 1. "coverUrl"字段：封⾯图⽚URL
             * 2. "videoDesc"字段：视频描述
             * 3. "mediaShareUrl"字段：⽤于跳转刷新的协议字符串
             */
            @Override
            public void onClickShareButton(String s) {
                Log.e(TAG, "onClickShareButton: " );
            }
        });
        /*
         * 播放回调
         */
        ksContentPageAD.addVideoListener(new KsContentPage.VideoListener() {

            /**
             * 播放开始
             */
            @Override
            public void onVideoPlayStart(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onVideoPlayStart: " );
            }

            /**
             * 播放暂停
             */
            @Override
            public void onVideoPlayPaused(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onVideoPlayPaused: " );
            }

            /**
             * 恢复播放
             */
            @Override
            public void onVideoPlayResume(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onVideoPlayResume: " );
            }

            /**
             * 播放完成
             */
            @Override
            public void onVideoPlayCompleted(KsContentPage.ContentItem contentItem) {
                Log.e(TAG, "onVideoPlayCompleted: " );
            }

            /**
             * 播放出错
             */
            @Override
            public void onVideoPlayError(KsContentPage.ContentItem contentItem, int i, int i1) {
                Log.e(TAG, "onVideoPlayError: " );
            }
        });

        /*
         * 媒体离开快⼿⻚⾯事件处理
         * @return true表示拦截。
         *
         */

        boolean intercept = ksContentPageAD.addPageLeaveClickListener(() -> Log.e(TAG, "onPageLeaveClick: " ));
        //尝试刷新，建议在第二次点击tab时候用,参考KsContentAdActivity showFragment2()
        // ksContentPageAD.tryToRefresh();

        //ksContentPageAD.getFragment()
        return ksContentPageAD;


    }
}
