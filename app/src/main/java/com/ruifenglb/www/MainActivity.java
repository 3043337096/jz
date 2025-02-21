package com.ruifenglb.www;

import android.Manifest;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Bundle;


import android.util.Log;
import android.view.KeyEvent;

import android.view.MenuItem;



import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.StormWyrm.wanandroid.base.exception.ResponseException;
import com.github.StormWyrm.wanandroid.base.net.RequestManager;
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver;
import com.github.StormWyrm.wanandroid.base.sheduler.IoMainScheduler;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;




import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import com.ruifenglb.www.ad.ADFragment;
import com.ruifenglb.www.banner.Data;
import com.ruifenglb.www.base.BaseActivity;
import com.ruifenglb.www.base.BaseMainFragment;
import com.ruifenglb.www.bean.AppUpdateBean;
import com.ruifenglb.www.bean.BaseResult;
import com.ruifenglb.www.bean.OpenRecommendEvent;
import com.ruifenglb.www.bean.PipMsgBean;
import com.ruifenglb.www.bean.StartBean;
import com.ruifenglb.www.bean.TitleEvent;
import com.ruifenglb.www.bean.UserVideo;

import com.ruifenglb.www.netservice.VodService;
import com.ruifenglb.www.pip.PIPManager;
import com.ruifenglb.www.receiver.ScreenBroadCastReceiver;
import com.ruifenglb.www.ui.home.HomeFragment;
import com.ruifenglb.www.ui.live.LiveFragment;

import com.ruifenglb.www.ui.rank.RankFragment;

import com.ruifenglb.www.ui.specialtopic.SpecialtTopicFragment;
import com.ruifenglb.www.ui.user.UserFragment;
import com.ruifenglb.www.ui.widget.AppUpdateDialog;
import com.ruifenglb.www.ui.widget.NoticeDialog2;
import com.ruifenglb.www.utils.AgainstCheatUtil;
import com.ruifenglb.www.utils.MMkvUtils;
import com.ruifenglb.www.utils.Retrofit2Utils;

import com.ruifenglb.www.utils.UserUtils;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnBackToFirstListener {


    private static final String TAG = "";
    private SharedPreferences mSpf;


    @BindView(R.id.bnv_main)
    BottomNavigationView bnv_main;

    public static final int HOME = 0;
    //public static final int SHARE = 3;
    public static final int TOPIC = 1;
    public static final int Video = 2;
    public static final int RANK = 3;
    //    public static final int GAME = 3;

    public static final int USER = 4;
    private SupportFragment[] mFragments = new SupportFragment[6];
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;
    private ScreenBroadCastReceiver screenBroadCastReceiver;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }
    @Override
    public void onStart()  {

        super.onStart();

        if (Data.getQQ() == "暗夜紫" && Data.getYS()=="2")  {



            bnv_main.setBackgroundColor(ColorUtils.getColor(R.color.xkh));

            bnv_main.setItemTextColor(getResources().getColorStateList(R.color.color_state_menu_navi));
            Data.setYS("1");
        }
        if (Data.getQQ() == "原始蓝" && Data.getYS()=="2")  {
            bnv_main.setBackgroundColor(ColorUtils.getColor(R.color.white));

            bnv_main.setItemTextColor(getResources().getColorStateList(R.color.color_state_menu_navi));
            Data.setYS("1");

        }






    }












    @Override
    public void onPause() {
        super.onPause();





    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        EventBus.getDefault().register(this);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);

        bnv_main.setBackgroundColor(Color.parseColor("#ffffff"));


          //  String htmlContent = HtmlService.getHtml(path);
          //  ToastUtils.showShort( HtmlService.getHtml(path)+"jj");



        if (Data.getQQ().equals("暗夜紫")) {
            bnv_main.setBackgroundColor(ColorUtils.getColor(R.color.xkh));
            bnv_main.setItemTextColor(getResources().getColorStateList(R.color.color_state_menu_navi));
            bnv_main.inflateMenu(R.menu.bottom_nav_menu);
        }
        if (Data.getQQ().equals("原始蓝")) {
            bnv_main.setBackgroundColor(ColorUtils.getColor(R.color.white));
            bnv_main.setItemTextColor(getResources().getColorStateList(R.color.color_state_menu_navi));
            bnv_main.inflateMenu(R.menu.bottom_nav_menu);
        }




























        screenBroadCastReceiver = new ScreenBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(screenBroadCastReceiver, filter);

        //初始化
        SupportFragment firstFragment = findFragment(HomeFragment.class);
        if (firstFragment == null) {
            mFragments[HOME] = HomeFragment.newInstance();
            // mFragments[TOPIC] = ShareFragment.newInstance();
        //    mFragments[Video] = LiveFragment.newInstance();
            mFragments[Video] = ADFragment.newInstance();
            mFragments[RANK] = RankFragment.newInstance();
            mFragments[USER] = UserFragment.newInstance();
            mFragments[TOPIC] = SpecialtTopicFragment.newInstance();
//            mFragments[GAME] = GameFragment2.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            showFragment(R.id.fl_main_container, mFragments[USER], transaction);
            showFragment(R.id.fl_main_container, mFragments[TOPIC], transaction);
            showFragment(R.id.fl_main_container, mFragments[RANK], transaction);
            showFragment(R.id.fl_main_container, mFragments[Video], transaction);
//            showFragment(R.id.fl_main_container, mFragments[TOPIC], transaction);
            showFragment(R.id.fl_main_container, mFragments[HOME], transaction);
            transaction.commit();

//            loadMultipleRootFragment(R.id.fl_main_container, HOME, mFragments[HOME], mFragments[LIVE], mFragments[USER], mFragments[TOPIC], mFragments[GAME]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
//            mFragments[TOPIC] = findFragment(ShareFragment.class);
            mFragments[HOME] = firstFragment;
            mFragments[TOPIC] = findFragment(SpecialtTopicFragment.class);
//            mFragments[GAME] = findFragment(GameFragment2.class);
            mFragments[Video] = findFragment(LiveFragment.class);
            mFragments[RANK] = findFragment(RankFragment.class);
            mFragments[USER] = findFragment(UserFragment.class);
        }
        initPermission();
        initView();
        showNotice();
        checkVersion();
        getTabThreeName();
//        getTabFourInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(screenBroadCastReceiver);
    }

    private void initPermission() {
        mPermissionList.clear();
        /**
         * 判断哪些权限未授予
         */
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                break;
            default:
                break;
        }
    }

    private void addAllFragment(SupportFragment[] mFragments, FragmentTransaction transaction, int layoutId) {
        for (SupportFragment fragment : mFragments) {
            if (fragment != null) {
                transaction.add(layoutId, fragment);
            }
        }
    }

    private void showFragment(int layoutId, SupportFragment fragment, FragmentTransaction transaction) {
        if (fragment != null) {
            transaction.add(layoutId, fragment);
        }
    }

    private void hideAllFragment(SupportFragment[] mFragments, FragmentTransaction transaction) {
        for (SupportFragment fragment : mFragments) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
    }

    @Override
    protected void initView() {
        bnv_main.setItemIconTintList(null);
        bnv_main.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackToFirstFragment() {
        bnv_main.setSelectedItemId(R.id.navigation_main_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_main_home:
                showHideFragment(mFragments[HOME]);
                return true;

            case R.id.navigation_main_topic:
                EventBus.getDefault().post("隐藏播放历史");
                showHideFragment(mFragments[TOPIC]);
                return true;

            case R.id.navigation_main_rank:
                EventBus.getDefault().post("隐藏播放历史");
                showHideFragment(mFragments[RANK]);
                return true;

         /*   case R.id.navigation_main_live:
                EventBus.getDefault().post("隐藏播放历史");
                showHideFragment(mFragments[LIVE]);
                return true;*/


//            case R.id.navigation_main_game:
//                showHideFragment(mFragments[GAME]);
//                mFragments[GAME].setUserVisibleHint(true);
//                return true;
           /* case R.id.navigation_main_share:
                if (!UserUtils.isLogin()) {
                    LoginActivity.Companion.start();
                    return false;
                } else {
                    showHideFragment(mFragments[TOPIC]);
                    return true;
                }*/
            case R.id.navigation_main_user:
                EventBus.getDefault().post("隐藏播放历史");
                showHideFragment(mFragments[USER]);
                mFragments[USER].setUserVisibleHint(true);
                return true;
            case R.id.navigation_main_video:
                EventBus.getDefault().post("隐藏播放历史");
                showHideFragment(mFragments[Video]);
                ((ADFragment)mFragments[Video]).Show();
                return true;
            default:
                return false;
        }
    }

    private void showNotice() {
        StartBean startBean = MMkvUtils.Companion.Builds().loadStartBean("");
        if (startBean != null) {
            StartBean.Document document = startBean.getDocument();
            if (document != null) {
                StartBean.Register registerd = document.getRegisterd();
                if (registerd != null && registerd.getStatus().equals("1")) {
                    new NoticeDialog2(mActivity, registerd.getContent())
                            .show();
                }
            }
        }
//        new NoticeDialog(mActivity,"1. 修改了首页 http://www.baidu.com \n2.添加了游戏功能 \n3.添加了专题功能 \n4.修改了bug1.\n1 修改了首页 \n2.添加了游戏功能 \n3.添加了专题功能 \n4.修改了bug\n1 修改了首页 \n2.添加了游戏功能 \n3.添加了专题功能 \n4.修改了bug")
//                .show();
    }

    private void checkVersion() {
//        AppUpdateBean baen = new AppUpdateBean();
//        baen.setSummary("1. 修改了首页 \n2.添加了游戏功能 \n3.添加了专题功能 \n4.修改了bug1.\n1 修改了首页 \n2.添加了游戏功能 \n3.添加了专题功能 \n4.修改了bug\n1 修改了首页 \n2.添加了游戏功能 \n3.添加了专题功能 \n4.修改了bug");
//       new AppUpdateDialog(mActivity, baen).show();
//
//        return;
        VodService vodService = Retrofit2Utils.INSTANCE.createByGson(VodService.class);
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        vodService
                .checkVersion("v" + AppUtils.getAppVersionName(), 1 + "")
                .compose(new IoMainScheduler<>())
                .subscribe(new BaseObserver<BaseResult<AppUpdateBean>>(true) {

                    @Override
                    public void onError(@NotNull ResponseException e) {

                    }

                    @Override
                    public void onSuccess(BaseResult<AppUpdateBean> data) {
                        if (data.getData() != null) {
                            new AppUpdateDialog(mActivity, data.getData())
                                    .show();
                        }
                    }
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onOpenRecommendEvent(OpenRecommendEvent event) {
        showHideFragment(mFragments[HOME]);
        bnv_main.setSelectedItemId(R.id.navigation_main_home);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void SCREEN_OFF(String msg) {
        if (msg == "锁屏") {
            Log.e("哈哈哈哈", "接收到锁屏信息");
            PipMsgBean pipMsg = PIPManager.getInstance().getPipMsg();
            if (pipMsg != null) {
                PIPManager.getInstance().stopPlay();
            }
        } else if (msg == "解锁") {
            PipMsgBean pipMsg = PIPManager.getInstance().getPipMsg();
            if (pipMsg != null) {
                PIPManager.getInstance().startPlay();
//                PIPManager.getInstance().resume();
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void pipRecode(PipMsgBean pipMsgBean) {
        if (UserUtils.isLogin()) {
            VodService vodService = Retrofit2Utils.INSTANCE.createByGson(VodService.class);
            if (AgainstCheatUtil.showWarn(vodService)) {
                return;
            }
            Log.d("画中画记录", "voidid=${voidid}  vodSelectedWorks=${vodSelectedWorks}  playSource=${playSource}  percentage=${percentage} curProgress=${curProgress}");
            BaseActivity activity;
            RequestManager.execute(MainActivity.this, vodService.addPlayLog(pipMsgBean.getVoidid(), pipMsgBean.getVodSelectedWorks(), pipMsgBean.getPlaySource(), pipMsgBean.getPercentage(), pipMsgBean.getUrlIndex() + "", pipMsgBean.getCurPregress() + "", pipMsgBean.getPlaySourceIndex() + ""),
                    new BaseObserver<UserVideo>() {
                        @Override
                        public void onError(@NotNull ResponseException e) {
                            PIPManager.getInstance().stopFloatWindow();
                            PIPManager.getInstance().reset();
                        }

                        @Override
                        public void onSuccess(UserVideo data) {
                            PIPManager.getInstance().stopFloatWindow();
                            PIPManager.getInstance().reset();
                        }
                    });
        } else {
            PIPManager.getInstance().stopFloatWindow();
            PIPManager.getInstance().reset();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (System.currentTimeMillis() - startTime <= 5000) {
                super.onBackPressedSupport();
            } else {
                startTime = System.currentTimeMillis();
                ToastUtils.showShort("再按一次退出程序");
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private Long startTime = 0L;

    private void getTabThreeName() {
        VodService vodService = Retrofit2Utils.INSTANCE.createByGson(VodService.class);
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        vodService.getTabThreeName()
                .compose(new IoMainScheduler<>())
                .subscribe(new BaseObserver<BaseResult<String>>(true) {

                    @Override
                    public void onError(@NotNull ResponseException e) {

                    }

                    @Override
                    public void onSuccess(BaseResult<String> data) {
                        String ui_faxian;
                        String ui_zhuanji;
                        if (data.getDatas() != null) {
                            Log.d("底部TAB", "onSuccess: " + data.getDatas() + "======");
                            boolean status = data.getDatas().contains("|");
                            if (status) { //插件数据
                                String str = data.getDatas();
                                String[] strArr = str.split("\\|");
                                ui_faxian = strArr[1]; //发现开关
                                ui_zhuanji = strArr[2]; //专辑开关
                            } else {  //开源后台赋值
                                ui_faxian = "1"; //打开
                                ui_zhuanji = "0"; //关闭
                            }
                        } else {
                            ui_faxian = "1";
                            ui_zhuanji = "0";
                        }

                        if (ui_faxian.equals("0")) { //关闭发现
                            ((BottomNavigationView) findViewById(R.id.bnv_main)).getMenu().removeItem(R.id.navigation_main_video);
                        }

                        if (ui_zhuanji.equals("0")) { //关闭专辑
                            ((BottomNavigationView) findViewById(R.id.bnv_main)).getMenu().removeItem(R.id.navigation_main_topic);
                        }








                    }


                });

    }



}
