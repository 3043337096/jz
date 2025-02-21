package com.ruifenglb.www.ui.start;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.content.SharedPreferences;
import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.app.ad.biddingsdk.AdConstant;
import com.app.ad.biddingsdk.AdListener;
import com.app.ad.biddingsdk.AdUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.StormWyrm.wanandroid.base.exception.Code;
import com.github.StormWyrm.wanandroid.base.exception.ResponseException;
import com.github.StormWyrm.wanandroid.base.net.RequestManager;
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import com.ruifenglb.av.ad.Datata;
import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.App;
import com.ruifenglb.www.MainActivity;
import com.ruifenglb.www.R;
import com.ruifenglb.www.ad.AdClickListener;
import com.ruifenglb.www.ad.AdWebView;
import com.ruifenglb.www.banner.Data;
import com.ruifenglb.www.banner.HtmlService;
import com.ruifenglb.www.base.BaseActivity;
import com.ruifenglb.www.bean.AppConfigBean;
import com.ruifenglb.www.bean.BaseResult;
import com.ruifenglb.www.bean.CloseSplashEvent;
import com.ruifenglb.www.bean.PageResult;
import com.ruifenglb.www.bean.SpecialtTopicBean;
import com.ruifenglb.www.bean.StartBean;
import com.ruifenglb.www.download.SPKey;
import com.ruifenglb.www.entity.AdvEntity;
import com.ruifenglb.www.netservice.StartService;
import com.ruifenglb.www.netservice.TopicService;
import com.ruifenglb.www.netservice.VodService;
import com.ruifenglb.www.network.RetryWhen;
import com.ruifenglb.www.ui.account.Chcary;
import com.ruifenglb.www.ui.account.chly;
import com.ruifenglb.www.utils.AgainstCheatUtil;
import com.ruifenglb.www.utils.AppConfig;
import com.ruifenglb.www.utils.MMkvUtils;
import com.ruifenglb.www.utils.Retrofit2Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartActivity extends BaseActivity {

    public static final String KEY_START_BEAN = "KEY_START_BEAN";
    private static final String TAG = "start";

    private String ad_str;
    private Disposable disposable;
    private StartService startService;
    private Disposable disposable1;

    @BindView(R.id.awv_start)
    AdWebView webView;
    @BindView(R.id.tv_start)
    TextView textView;
    @BindView(R.id.iv_image)
    ImageView imageView;
    @BindView(R.id.tv_load)
    TextView loadTv;

    @BindView(R.id.adView)
    FrameLayout adView;

    private boolean isInit = false;
    private static final int MAX_TIME = 8;
    private int start_time = MAX_TIME;
    private Handler handler = new Handler();
    private boolean isClosed = false;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            setTime(start_time);
            start_time -= 1;
            if (start_time >= 0 && !isClosed) {
                handler.postDelayed(runnable, 1000);
            } else {
                gotoMain();
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_start;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setTheme(R.style.AppTheme_Launcher);
        //清空启动背景
        //this.getWindow().getDecorView().setBackground(null);
        super.onCreate(savedInstanceState);

       if(com.ruifenglb.www.download.SPUtils.getString(imageView.getContext(),"why").equals("暗夜紫")) {
           Data.setQQ("暗夜紫");
           Data.setB("1");
       }else if(com.ruifenglb.www.download.SPUtils.getString(imageView.getContext(),"why").equals("原始蓝")) {
           Data.setQQ("原始蓝");
           Data.setB("1");
       }else {
           Data.setQQ("原始蓝");
           Data.setB("1");

       }

        //     ToastUtils.showShort(SPUtils.getInstance().getString("why"));
//Data.setJ(0xff223344);
        //ToastUtils.showShort("正在选择加速通道，请稍后...");

        EventBus.getDefault().register(this);

      //  getAppConfig();
        new Thread(() -> ReadUrl()).start();

        BarUtils.setStatusBarVisibility(this, false);
        BarUtils.setNavBarVisibility(this, false);

       // final SharedPreferences sp = getSharedPreferences("mrsoft", MODE_PRIVATE);
      //  final SharedPreferences.Editor editor = sp.edit();
      // sp.getInt()
    }


    @Override
    protected void initData() {
        super.initData();

        getStartData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("xxxxxxx", "startbean========001onResume");

      //  if (!isInit) getStartData();


        // getTopicData();

    }


    private void showAd(){
        AdUtils.splashAd(this, adView, AdConstant.OPEN_SCREEN_AD,new AdListener() {
            @Override
            public void onShow() {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onClose() {
                gotoMain();
            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getStartData() {
        Log.d("2initData容器webView是什么", "onADDismissed: >>>>"+(webView== null));
        Log.d("2initData容器webView是什么", "onADDismissed: >>>>"+webView);
        Log.d("initData容器textView是什么", "onADDismissed: >>>>"+(textView== null));
        Log.d("initData容器textView是什么", "onADDismissed: >>>>"+textView);
        Log.d("initData容器imageView是什么", "onADDismissed: >>>>"+(imageView== null));
        Log.d("initData容器imageView是什么", "onADDismissed: >>>>"+imageView);
        Log.d("initData容器adView是什么", "onADDismissed: >>>>"+(adView== null));
        Log.d("initData容器adView是什么", "onADDismissed: >>>>"+adView);
        Log.i("xxxxxxx", "startbean========001");
        if (startService == null) {
            startService = Retrofit2Utils.INSTANCE.createByGson(StartService.class);
        }
        if (AgainstCheatUtil.showWarn(startService)) {
            return;
        }
        startService.getStartBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .retryWhen(new RetryWhen(3, 30))
                .subscribe(new Observer<BaseResult<StartBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("xxxxxxx", "disposable========haha111");
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                            disposable = null;
                        }
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResult<StartBean> result) {
                        Log.i("xxxxxxx", "startbean========haha111");

                        if (result != null) {
                            Log.i("xxxxxxx", "startbean========111");
                            if (result.isSuccessful()) {
                                Log.i("xxxxxxx", "startbean========222");
                                if (result.getData() != null) {
                                    Log.i("xxxxxxx", "startbean========333");
                                    StartBean startBean = result.getData();
                                    CacheDiskStaticUtils.put(KEY_START_BEAN, startBean);
                                    if (startBean != null) {
                                        StartBean.Ads ads = startBean.getAds();
                                        VodService vodService2 = Retrofit2Utils.INSTANCE.createByGson(VodService.class);
                                        RequestManager.execute(vodService2.getAdv(),
                                                new BaseObserver<AdvEntity>() {
                                                    @Override
                                                    public void onSuccess(AdvEntity data) {
                                                        MMkvUtils.Companion.Builds().saveAdvEntity(data);
                                                    }

                                                    @Override
                                                    public void onError(@NotNull ResponseException e) {

                                                    }
                                                }
                                        );


                                        StartBean.Ads newAds = startBean.getAds();
                                        StartBean.Ads oldAds = new StartBean.Ads();
                                        Gson gson = new Gson();


                                        String indexGet = SPUtils.getInstance().getString(SPKey.AD_INDEX);
                                        StartBean.Ad oldIndex = gson.fromJson(indexGet, StartBean.Ad.class);
                                        String cartoonGet = SPUtils.getInstance().getString(SPKey.AD_CARTOON);
                                        StartBean.Ad oldCartoon = gson.fromJson(cartoonGet, StartBean.Ad.class);
                                        String sitcomGet = SPUtils.getInstance().getString(SPKey.AD_SITCOM);
                                        StartBean.Ad oldSitcom = gson.fromJson(sitcomGet, StartBean.Ad.class);
                                        String vodGet = SPUtils.getInstance().getString(SPKey.AD_VOD);
                                        StartBean.Ad oldVod = gson.fromJson(vodGet, StartBean.Ad.class);
                                        String searcherGet = SPUtils.getInstance().getString(SPKey.AD_SEARCHER);
                                        StartBean.Ad oldSearcher = gson.fromJson(searcherGet, StartBean.Ad.class);
                                        String varietyGet = SPUtils.getInstance().getString(SPKey.AD_VARIETY);
                                        StartBean.Ad oldVariety = gson.fromJson(varietyGet, StartBean.Ad.class);

                                        oldAds.setIndex(oldIndex);
                                        oldAds.setCartoon(oldCartoon);
                                        oldAds.setSitcom(oldSitcom);
                                        oldAds.setVod(oldVod);
                                        oldAds.setSearcher(oldSearcher);
                                        oldAds.setVariety(oldVariety);

                                        MMkvUtils.Companion.Builds().saveStartBean(startBean);

                                        if (newAds != null) {
                                            StartBean.Ad index = newAds.getIndex();

                                            if (newAds.getIndex().getDescription() == null || newAds.getIndex().getDescription().isEmpty()) {
                                                if (oldAds.getIndex() != null && oldAds.getIndex().getDescription() != null && !oldAds.getIndex().getDescription().isEmpty()) {
                                                    index.setDescription(oldAds.getIndex().getDescription());
                                                }
                                            }
                                            String indexStr = gson.toJson(newAds.getIndex(), StartBean.Ad.class);
                                            SPUtils.getInstance().put(SPKey.AD_INDEX, indexStr);


                                            StartBean.Ad cartoon = newAds.getCartoon();
                                            if (newAds.getCartoon().getDescription() == null || newAds.getCartoon().getDescription().isEmpty()) {
                                                if (oldAds.getCartoon() != null && oldAds.getCartoon().getDescription() != null && !oldAds.getCartoon().getDescription().isEmpty()) {
                                                    cartoon.setDescription(oldAds.getCartoon().getDescription());
                                                }
                                            }
                                            String cartoonStr = gson.toJson(newAds.getCartoon(), StartBean.Ad.class);
                                            SPUtils.getInstance().put(SPKey.AD_CARTOON, cartoonStr);


                                            StartBean.Ad sitcom = newAds.getSitcom();
                                            if (newAds.getSitcom().getDescription() == null || newAds.getSitcom().getDescription().isEmpty()) {
                                                if (oldAds.getSitcom() != null && oldAds.getSitcom().getDescription() != null && !oldAds.getSitcom().getDescription().isEmpty()) {
                                                    sitcom.setDescription(oldAds.getSitcom().getDescription());
                                                }
                                            }
                                            String sitcomStr = gson.toJson(newAds.getSitcom(), StartBean.Ad.class);
                                            SPUtils.getInstance().put(SPKey.AD_SITCOM, sitcomStr);


                                            StartBean.Ad vod = newAds.getVod();
                                            if (newAds.getVod().getDescription() == null || newAds.getVod().getDescription().isEmpty()) {
                                                if (oldAds.getVod() != null && oldAds.getVod().getDescription() != null && !oldAds.getVod().getDescription().isEmpty()) {
                                                    vod.setDescription(oldAds.getVod().getDescription());
                                                }
                                            }
                                            String vodStr = gson.toJson(newAds.getVod(), StartBean.Ad.class);
                                            SPUtils.getInstance().put(SPKey.AD_VOD, vodStr);


                                            StartBean.Ad searcher = newAds.getSearcher();
                                            if (newAds.getSearcher().getDescription() == null || newAds.getSearcher().getDescription().isEmpty()) {
                                                if (oldAds.getSearcher() != null && oldAds.getSearcher().getDescription() != null && !oldAds.getSearcher().getDescription().isEmpty()) {
                                                    searcher.setDescription(oldAds.getSearcher().getDescription());
                                                }
                                            }
                                            String searcherStr = gson.toJson(newAds.getSearcher(), StartBean.Ad.class);
                                            SPUtils.getInstance().put(SPKey.AD_SEARCHER, searcherStr);


                                            StartBean.Ad variety = newAds.getVariety();
                                            if (newAds.getVariety().getDescription() == null || newAds.getVariety().getDescription().isEmpty()) {
                                                if (oldAds.getVariety() != null && oldAds.getVariety().getDescription() != null && !oldAds.getVariety().getDescription().isEmpty()) {
                                                    variety.setDescription(oldAds.getVariety().getDescription());
                                                }
                                            }
                                            String varietyStr = gson.toJson(newAds.getVariety(), StartBean.Ad.class);
                                            SPUtils.getInstance().put(SPKey.AD_VARIETY, varietyStr);

                                            if (MMkvUtils.Companion.Builds().loadStartBean("") != null) {
                                                StartBean startBean1 = MMkvUtils.Companion.Builds().loadStartBean("");

                                                startBean1.setAds(newAds);
                                                MMkvUtils.Companion.Builds().saveStartBean(startBean1);
                                            }
                                            MMkvUtils.Companion.Builds().saveSearchHot(startBean.getSearch_hot());
                                            if (ads != null) {
                                                StartBean.Ad ad = ads.getStartup_adv();
                                                if (ad != null) {
                                                    ad_str = ad.getDescription();

                                                    if (ad.getStatus() == 1 ){
                                                        // gotoMain();
                                                        showAd();
                                                    }else{
                                                        gotoMain();

                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i("xxxxxxx", "startbean========555" + e.getMessage());
                        init();
                    }

                    @Override
                    public void onComplete() {
                        init();
                    }
                });

    }

    private void stopGet() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable.dispose();
        }
    }

    private void init() {
        if (StringUtils.isEmpty(ad_str)) return;
        webView.setVisibility(View.VISIBLE);
        LogUtils.e(ad_str);

        isInit = true;
        webView.isforceFullScreen(true);
        webView.addAdClickListener(new AdClickListener() {
            @Override
            public void onAdClick(String url) {
                isClosed = true;
                handler.removeCallbacks(runnable);
            }
        });
        webView.loadHtmlBody(ad_str);
    }

    private void cancleImage() {
      /*  ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
        anim.setDuration(500);
        anim.setRepeatCount(0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (imageView != null) {
                    imageView.setAlpha((Float) animation.getAnimatedValue());
                    loadTv.setAlpha((Float) animation.getAnimatedValue());
                }
                System.out.println("onAnimationUpdate " + (Float) animation.getAnimatedValue());
            }
        });
        anim.start();*/
    }

    private void setTime(int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textView != null) {
                    textView.setText(StringUtils.getString(R.string.skip, i));
                }
            }
        });
    }
    private boolean GenuineVerification(){

        if (MMkvUtils.Companion.Builds().loadStartBean("") == null){
            Toast.makeText(mActivity, "网络链接错误0。。", Toast.LENGTH_LONG).show();
            Log.e(TAG, "checkversion: 网络链接错误0..");
            return true;
        }
        StartBean.Comment com  = Objects.requireNonNull(MMkvUtils.Companion.Builds().loadStartBean("")).getComment();


        if (com != null){
            if (!com.getSg().contains("o/")){
                Toast.makeText(mActivity, "网络链接错误1。。", Toast.LENGTH_LONG).show();
                Log.e(TAG, "checkversion: 网络链接错误1..");
                return true;
            }

            if (!com.getSg().contains("a/")){
                Toast.makeText(mActivity, "网络链接错误2。。", Toast.LENGTH_LONG).show();
                Log.e(TAG, "checkversion: 网络链接错误2..");
                return true;
            }

            if (com.getSg() == null || com.getSg().equals("") ){
                Toast.makeText(mActivity, "网络链接错误3。。", Toast.LENGTH_LONG).show();
                Log.e(TAG, "checkversion: 网络链接错误3..");
                return true;
            }


            byte[] sgb = Base64.decode(com.getSg().replaceAll("(o/)|(a/)|(c/)|/", "").getBytes(), Base64.DEFAULT);
            String sg = new String(sgb);
            if (!sg.equals(ApiConfig.MOGAI_BASE_URL)) {
                Toast.makeText(mActivity, "网络链接错误4。。", Toast.LENGTH_LONG).show();
                Log.e(TAG, "checkversion: 网络链接错误4..");
                return true;
            }

        }else {
            return true;
        }
        return false;
    }
    private void gotoMain() {


        isClosed = true;
        handler.removeCallbacks(runnable);
        stopGet();
        finish();
        startActivity(new Intent(this,MainActivity.class));
     //   ActivityUtils.startActivity(MainActivity.class);

    }


    @OnClick(R.id.tv_start)
    void missAd() {

        isClosed = true;
        handler.removeCallbacks(runnable);
        stopGet();
        finish();
        startActivity(new Intent(this,MainActivity.class));
       // ActivityUtils.startActivity(MainActivity.class);

    }



    public void getAppConfig() {
        VodService vodService = Retrofit2Utils.INSTANCE.createByGson(VodService.class);
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(
                this, vodService.getPlayAd(2 + ""),
                new BaseObserver<AppConfigBean>() {
                    @Override
                    public void onSuccess(AppConfigBean data) {
                        MMkvUtils.Companion.Builds().saveAppConfigAd(data);
                    }

                    @Override
                    public void onError(@NotNull ResponseException e) {

                    }
                }
        );

        //获取标签状态
        RequestManager.execute(
                this, vodService.getPlayAd(1 + ""),
                new BaseObserver<AppConfigBean>() {
                    @Override
                    public void onSuccess(AppConfigBean data) {
                        MMkvUtils.Companion.Builds().saveAppConfig(data);
                    }

                    @Override
                    public void onError(@NotNull ResponseException e) {

                    }
                }
        );

    }

    private void getTopicData() {
        TopicService cardService = Retrofit2Utils.INSTANCE.createByGson(TopicService.class);
        if (AgainstCheatUtil.showWarn(cardService)) {
            return;
        }
        cardService.getTopicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .retryWhen(new RetryWhen(3, 3))
                .subscribe(new Observer<PageResult<SpecialtTopicBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (disposable1 != null && !disposable1.isDisposed()) {
                            disposable1.dispose();
                            disposable1 = null;
                        }
                        disposable1 = d;
                    }

                    @Override
                    public void onNext(PageResult<SpecialtTopicBean> result) {
                        if (result != null) {
                            if (result.isSuccessful()) {
                                List<SpecialtTopicBean> list = result.getData().getList();
//                                topicEntities.clear();
//                                topicEntities.addAll(list);
//                                activityLevelAdpter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onCloseEvent(CloseSplashEvent event) {
        start_time = MAX_TIME;
        setTime(start_time);
        handler.postDelayed(runnable, 1000);
        cancleImage();
    }

    private void ReadUrl(){
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(ApiConfig.HTTP_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader( new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            ApiConfig.MOGAI_BASE_URL = response.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            runOnUiThread(() -> getAppConfig());

        }

    }

}
