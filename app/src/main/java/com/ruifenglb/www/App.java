package com.ruifenglb.www;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.app.ad.biddingsdk.AdConstant;
import com.app.ad.biddingsdk.AdListener;
import com.app.ad.biddingsdk.AdUtils;
import com.dpuntu.downloader.DownloadManager;
import com.dpuntu.downloader.Downloader;

import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.AndroidMediaPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.orhanobut.hawk.Hawk;
import com.ruifenglb.www.utils.AppConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

import org.litepal.LitePal;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.ruifenglb.av.play.MyIjkPlayerFactory;
import com.ruifenglb.www.base.BaseApplication;
import com.ruifenglb.www.bean.AppConfigBean;
import com.ruifenglb.www.bean.PlayScoreBean;
import com.ruifenglb.www.bean.StartBean;
import com.ruifenglb.www.download.GetFileSharePreance;
import com.ruifenglb.www.entity.AdvEntity;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import jaygoo.library.m3u8downloader.M3U8Library;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class App extends BaseApplication {
    private static final String TAG = "App";
//    public static List<String> searchHot;
//    public static StartBean startBean;
//    public static AdvEntity advEntity ;
//    public static AppConfigBean playAd;
//    public static AppConfigBean tagConfig;

    private static WeakReference<App> weakReference;
    private static App vocApp;
    public static List<Downloader> downloaders = new ArrayList<>();

//    public static PlayScoreBean curPlayScoreBean;

    public static App getInstance() {
        return weakReference.get();
    }


    public static App getApplication() {
        return vocApp;
    }

    // 页面刷新代码 static 代码段可以防止内存泄露
    static {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
//            @NonNull
//            @Override
//            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
//                MaterialHeader materialHeader = new MaterialHeader(context);
//                materialHeader.setColorSchemeColors(0xFFFF6600);
//                return materialHeader;
//            }
//        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }


    @Override
    public void onCreate() {

        super.onCreate();

        try {
            setRxJavaErrorHandler();
        } catch (Exception e) {
        }
        LitePal.initialize(this);
        UMConfigure.init(this, "#", "222", UMConfigure.DEVICE_TYPE_PHONE, "");
        weakReference = new WeakReference<>(this);
        vocApp = this; //xUtils 初始化
        x.Ext.init(this);
        x.Ext.setDebug(true);//是否输出Debug日志

        Hawk.init(this).build();
        DownloadManager.initDownloader(vocApp);

        MMKV.initialize(this);

        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.i(getClass().getName().toString(), "initX5Environment onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.i(getClass().getName().toString(), "initX5Environment onViewInitFinished");
            }
        });



        //播放器配置，注意：此为全局配置，按需开启
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
              //  .setLogEnabled(BuildConfig.DEBUG)
              //.setPlayerFactory(ExoMediaPylayerFactory.create())
                .setPlayerFactory(MyIjkPlayerFactory.create())

              // .setPlayerFactory(AndroidMediaPlayerFactory.create())
                //.setAutoRotate(true)
               //.setEnableMediaCodec(true)
                //.setUsingSurfaceView(true)
                //.setEnableParallelPlay(true)
                //.setEnableAudioFocus(true)
                //.setScreenScale(VideoView.SCREEN_SCALE_MATCH_PARENT)
                .build());
//        //设置toast的颜色
//        ToastUtils.setBgColor(ContextCompat.getColor(this, R.color.colorAccent));

        M3U8Library.init(this);
        //广告初始化
        AdUtils.init(this, AdConstant.APP_ID,new AdListener() {
            @Override
            public void onShow() {
            }

            @Override
            public void onClose() {

            }
        });


    }

    public static int getSrceenWidth() {
        return ((WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }


    // 页面刷新代码 static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public synchronized static GetFileSharePreance getFileSharePreance(){
        return new GetFileSharePreance(vocApp);
    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "RxJavaPlugins throw test");
            }
        });
    }

}
