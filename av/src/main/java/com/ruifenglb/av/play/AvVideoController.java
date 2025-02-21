package com.ruifenglb.av.play;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dueeeke.videoplayer.controller.GestureVideoController;
import com.dueeeke.videoplayer.controller.MediaPlayerControl;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;

import com.ruifenglb.av.CheckVodTrySeeBean;

import com.ruifenglb.av.WakeupReceiver;
import com.xiaweizi.marquee.MarqueeTextView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.ruifenglb.av.R;
import com.ruifenglb.av.ad.AdWebView;
import pl.droidsonroids.gif.GifImageView;

import java.util.TimerTask;

import static com.dueeeke.videoplayer.util.PlayerUtils.stringForTime;

public class AvVideoController extends GestureVideoController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private int isNeedVip;
    private Boolean isClickivPlay = false;
    private int trySeeTime;
    protected ControllerClickListener controllerClickListener;
    protected ControllerPlayIngLisenter playingLisenter;
    private RelativeLayout rl_fullscreen;
    public static final String KEY_SPEED_INDEX = "KEY_SPEED_INDEX";
    public static final int RECEIVER_TYPE_REPLAY = 1;
    public static final int RECEIVER_TYPE_TIMER = 2;
    private Boolean mReplayByCurProgress = false;
    private ImageView loge;
    private ImageView play_img;
    private GifImageView gifIMageView;

    //    @SuppressWarnings("unused")
    public void setControllerClickListener(ControllerClickListener controllerClickListener) {
        this.controllerClickListener = controllerClickListener;
    }

    public void setControllerPlayIngLisenter(ControllerPlayIngLisenter lisenter) {
        this.playingLisenter = lisenter;
    }

    private static final String KEY_IS_OPEN_DANMAKU = "KEY_IS_OPEN_DANMAKU";

    private static final String TAG = "AvVideoController";
    private Animation mShowAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_av_alpha_in);
    private Animation mHideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_av_alpha_out);

    protected ImageView iv_bg;
    //顶部
    protected View v_all_bg;
    protected View v_top_bg;
    protected View v_bottom_bg;
    protected ImageView iv_back;
    protected MarqueeTextView tv_title;
    protected TextClock tc_localtime;
    protected ImageView iv_miracast;
    protected ImageView iv_download;
    //中部
    protected ImageView iv_lock;
    protected ImageView iv_pip;
    protected ImageView iv_scale;
    //底部
    protected ImageView iv_play;
    protected TextView tv_curr_time, tv_total_time, tv_playtime;
    protected SeekBar progress, sb_1, sb_2;//使用两个来切换
    protected ImageView iv_fullscreen;
    protected ImageView iv_next;
    protected ImageView iv_av_danmaku_land,iv_av_danmaku_port;
    protected TextView tv_danmaku_land,tv_danmaku_port;
    protected TextView tvPlaySource_land,tvPlaySource_port;
    protected TextView tv_speed;
    public static TextView tv_timer_close;
    protected TextView tv_hd;
    protected TextView tv_selected;
    private LinearLayout llPay;
    private LinearLayout llUpdate;
    private FrameLayout rlEndUpdate;
    private FrameLayout rlEndPay;
    private TextView tvUpdateTitle;
    private TextView tvPayTitle;
    private TextView tvEndUpdateTitle;
    private TextView tvEndPayTitle;
    private View v_av_top_bg,v_av_top_bg0;

    private FrameLayout llSkip;
    private ImageView awvPlayer;
    private ImageView imgTz, imgTzH_land,imgTzH_port;
    private TextView tvSkip;
    private TextView tvAvAnnouncement;

    private Button btError, btReplay;

    ///全局View
    protected ImageView iv_replay;
    protected TextView tv_replay;
    //加载进度条
    protected LinearLayout clpb_loading;
    //加载进度条
    protected ConstraintLayout clpb_jiexi;
    protected TextView tvJiexiMsg;
    //是否在滑动进度条
    private boolean mIsDragging;
    //是否显示vip hint
    public boolean isShowHint;
    private Animation animation;
    private VideoViewImpt videoViewImpt;
    private WeakReference<AppCompatActivity> curActivity;

    private boolean isNormal = true;//是否是竖屏
    private boolean isPlayVideoAdv = false;//是否播放视频广告中
    private boolean isPlayStopAd = false;//是否播放暂停广告中
    private TextView speedAdd;

    public int type_id;

    private View layout_land, layout_port;

    public AvVideoController(VideoViewImpt impt, @NonNull Context context) {
        this(context);
        curActivity = new WeakReference<>((AppCompatActivity) context);
        this.videoViewImpt = impt;
    }

    public AvVideoController(@NonNull Context context) {
        this(context, null);
    }

    public AvVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_av_controller;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return super.onScroll(e1, e2, distanceX, distanceY);
    }
   @Override
    public void onLongPress(MotionEvent e) {
        if (controllerClickListener != null){
            speedAdd.setVisibility(View.VISIBLE);
            controllerClickListener.onLongPress();
        }
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (controllerClickListener != null){
            speedAdd.setVisibility(View.GONE);
            controllerClickListener.onSingleTapUp();
        }
        return  super.onSingleTapUp(e);
    } //长按倍速

    @Override
    protected void initView() {
        super.initView();
        Log.e(TAG, "initView");

        setEnableInNormal(true);
        play_img = findViewById(R.id.play_img);
        btError = findViewById(R.id.btError);
        btReplay = findViewById(R.id.btReplay);
        loge=findViewById(R.id.loge);
        gifIMageView=findViewById(R.id.progress);
        tvAvAnnouncement = findViewById(R.id.tv_av_announcement);
        v_av_top_bg = findViewById(R.id.v_av_top_bg);v_av_top_bg0= findViewById(R.id.v_av_top_bg0);        tvUpdateTitle = findViewById(R.id.tvUpdateTitle);
        tvJiexiMsg = findViewById(R.id.tvJiexiMsg);
        tvPayTitle = findViewById(R.id.tvPayTitle);
        tvEndPayTitle = findViewById(R.id.tvEndPayTitle);
        tvEndUpdateTitle = findViewById(R.id.tvEndUpdateTitle);
        rlEndUpdate = findViewById(R.id.rlEndUpdate);
        rlEndPay = findViewById(R.id.rlEndPay);
        llPay = findViewById(R.id.llPay);
        llUpdate = findViewById(R.id.llUpdateVip);
        iv_bg = findViewById(R.id.iv_av_bg);
        llSkip = findViewById(R.id.llSkip);
        tvSkip = findViewById(R.id.tvSkip);
        awvPlayer = findViewById(R.id.awvPlayer);
        imgTz = findViewById(R.id.imgTz);
        imgTzH_land = findViewById(R.id.imgTzH_land);
        imgTzH_port = findViewById(R.id.imgTzH_port);
        imgTz.setOnClickListener(this);
        imgTzH_land.setOnClickListener(this);
        imgTzH_port.setOnClickListener(this);
        speedAdd =  findViewById(R.id.speedAdd);
        findViewById(R.id.tvPayButton).setOnClickListener(this);
        findViewById(R.id.tvUpdateButton).setOnClickListener(this);
        findViewById(R.id.tvEndPayButton).setOnClickListener(this);
        findViewById(R.id.tvEndUpdateButton).setOnClickListener(this);
        //全部背景
        v_all_bg = findViewById(R.id.v_av_all_bg);
        //顶部背景
        v_top_bg = findViewById(R.id.v_av_top_bg);
        //底部背景
        v_bottom_bg = findViewById(R.id.v_av_bottom_bg);
        //-------------顶部
        //返回键
        iv_back = findViewById(R.id.iv_av_back);
        iv_back.setOnClickListener(this);
        findViewById(R.id.iv_av_back1).setOnClickListener(this);
        findViewById(R.id.iv_av_back2).setOnClickListener(this);
        //标题
        tv_title = findViewById(R.id.tv_av_title);
        //系统时间
        tc_localtime = findViewById(R.id.tc_av_localtime);
        //投屏
        iv_miracast = findViewById(R.id.iv_av_miracast);
        iv_miracast.setOnClickListener(this);
        //下载
        iv_download = findViewById(R.id.iv_av_download);
        iv_download.setOnClickListener(this);
        //--------------中部
        //锁定
        iv_lock = findViewById(R.id.iv_av_lock);
        iv_lock.setOnClickListener(this);
        //画中画
        iv_pip = findViewById(R.id.iv_av_pip);
        iv_pip.setOnClickListener(this);
        //视频比例
        iv_scale = findViewById(R.id.iv_av_scale);
        iv_scale.setOnClickListener(this);
        //---------------底部
        //播放
        iv_play = findViewById(R.id.iv_av_play);
        iv_play.setOnClickListener(this);
        //当前播放时间
        tv_curr_time = findViewById(R.id.tv_av_curr_time);
        //总播放时间
        tv_total_time = findViewById(R.id.tv_av_total_time);
        //播放时间合集
        tv_playtime = findViewById(R.id.tv_av_playtime);
        //播放进度条
        sb_1 = findViewById(R.id.sb_av_1);
        sb_1.setOnSeekBarChangeListener(null);
        sb_2 = findViewById(R.id.sb_av_2);
        sb_2.setOnSeekBarChangeListener(null);
        progress = sb_1;
        progress.setOnSeekBarChangeListener(this);
        //全屏
        iv_fullscreen = findViewById(R.id.iv_av_fullscreen);
        rl_fullscreen = findViewById(R.id.rl_av_fullscreen);
        rl_fullscreen.setOnClickListener(this);
        //下一集
        iv_next = findViewById(R.id.iv_av_next);
        iv_next.setOnClickListener(this);
        //弹幕开关
        iv_av_danmaku_land = findViewById(R.id.iv_av_danmaku_land);
        iv_av_danmaku_land.setOnClickListener(this);
        iv_av_danmaku_port = findViewById(R.id.iv_av_danmaku_port);
        iv_av_danmaku_port.setOnClickListener(this);

        //弹幕发送按钮
        tvPlaySource_land = findViewById(R.id.tvPlaySource_land);
        tvPlaySource_land.setOnClickListener(this);
        tvPlaySource_port = findViewById(R.id.tvPlaySource_port);
        tvPlaySource_port.setOnClickListener(this);

        //弹幕发送按钮
        tv_danmaku_land = findViewById(R.id.tv_av_danmaku_land);
        tv_danmaku_land.setOnClickListener(this);
        tv_danmaku_port = findViewById(R.id.tv_av_danmaku_port);
        tv_danmaku_port.setOnClickListener(this);

        //初始化弹幕状态
        boolean b = SPUtils.getInstance().getBoolean(KEY_IS_OPEN_DANMAKU, true);
        iv_av_danmaku_land.setSelected(b);
        iv_av_danmaku_port.setSelected(b);
        //定时关闭
        tv_timer_close= findViewById(R.id.tv_timer_close);
        tv_timer_close.setOnClickListener(this);
        activity=(Activity)tv_timer_close.getContext();
        //播放倍数
        tv_speed = findViewById(R.id.tv_av_speed);
        tv_speed.setOnClickListener(this);
        //清晰度
        tv_hd = findViewById(R.id.tv_av_hd);
        tv_hd.setOnClickListener(this);
        //选集
        tv_selected = findViewById(R.id.tv_av_selected);
        tv_selected.setOnClickListener(this);
        //加载进度条
        clpb_loading = findViewById(R.id.clpb_av_loading);
        clpb_loading.setVisibility(View.GONE);
        //加载进度条
        clpb_jiexi = findViewById(R.id.clpb_av_jiexi);
        //重新播放
        iv_replay = findViewById(R.id.iv_av_replay);
        iv_replay.setOnClickListener(this);
        tv_replay = findViewById(R.id.tv_av_replay);


        layout_land = findViewById(R.id.layout_land);
        layout_port = findViewById(R.id.layout_port);

        findViewById(R.id.imgCloseStopHAd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayStopHAd();
            }
        });
    }

    void checkScreen(){
        if(mControlWrapper.isFullScreen()){
            int[] size =  mControlWrapper.getVideoSize();
            if(size[0]>size[1]){
                findViewById(R.id.layout_land).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_port).setVisibility(View.GONE);
            }else{
                findViewById(R.id.layout_land).setVisibility(View.GONE);
                findViewById(R.id.layout_port).setVisibility(View.VISIBLE);
            }
        }
    }

    int bufferTime = 0;
    TimerTask bufferTask;

    private void startBufferTimer() {
        if (bufferTimer != null) {
            bufferTimer.cancel();
        }
        if (bufferTask != null) {
            bufferTask.cancel();
        }
        bufferTimer = new Timer();
        bufferTask = new TimerTask() {
            @Override
            public void run() {
                if (bufferTime >= 8) {
                    bufferTime = 0;
                    bufferTimer.cancel();
                    bufferTimer = null;
                    bufferTask = null;
                    Intent intent = new Intent("cn.whiner.av.AvVideoController");
                    intent.putExtra("type", RECEIVER_TYPE_TIMER);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                    System.out.println("缓存结束====");
                } else {
                    bufferTime++;
                }
            }
        };
        bufferTimer.schedule(bufferTask, 0, 1000);
    }

    /**
     * 拦截手势的双击事件
     * @param e
     * @return
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (!isLocked() && isInPlaybackState()){
            if (!isPlayStopAd){
                togglePlay();
            }
        }
        return false;
    }

    //窗口切换
    @Override
    public void setPlayerState(int playerState) {
        super.setPlayerState(playerState);
        if (isLocked()) return;
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                isNormal = true;
                Log.e(TAG, "正常窗口");


                //系统时间
                tc_localtime.setVisibility(GONE);
                //下载
                iv_download.setVisibility(GONE);
                //锁定
                iv_lock.setVisibility(GONE);

                //视频比列
                iv_scale.setVisibility(GONE);
                //如果不是视频广告播放中
                if (!isPlayVideoAdv) {
                    //画中画
                    iv_pip.setVisibility(VISIBLE);
                    if (animation != null && tvAvAnnouncement.getVisibility() == VISIBLE) {
                        animation.reset();
                        tvAvAnnouncement.startAnimation(animation);
                    }
                    setGestureEnabled(true);

                    iv_back.setVisibility(VISIBLE);
                    //当前播放时间
                    tv_curr_time.setVisibility(VISIBLE);
                    //总时间
                    tv_total_time.setVisibility(VISIBLE);
                    //全屏
                    iv_fullscreen.setVisibility(VISIBLE);
                    rl_fullscreen.setVisibility(VISIBLE);
                    //seekBar
                    sb_1.setVisibility(VISIBLE);
                    sb_1.setOnSeekBarChangeListener(this);
                    sb_2.setVisibility(GONE);
                    sb_2.setOnSeekBarChangeListener(null);

                    imgTz.setVisibility(VISIBLE);
                    imgTzH_land.setVisibility(GONE);
                    imgTzH_port.setVisibility(GONE);
                }

                progress = sb_1;
                ///下一集
                iv_next.setVisibility(GONE);
                //时间集合
                tv_playtime.setVisibility(GONE);
                //弹幕
                iv_av_danmaku_land.setVisibility(GONE);
                iv_av_danmaku_port.setVisibility(GONE);
                tv_danmaku_land.setVisibility(GONE);
                tv_danmaku_port.setVisibility(GONE);
                tvPlaySource_land.setVisibility(GONE);
                tvPlaySource_port.setVisibility(GONE);
                //定时关闭
                tv_timer_close.setVisibility(GONE);
                //播放倍数
                tv_speed.setVisibility(GONE);
                //播放清晰度
                tv_hd.setVisibility(GONE);
                //选集
                tv_selected.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:

                if(type_id!=20)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                isNormal = false;
                Log.e(TAG, "全屏窗口");

                //系统时间
                tc_localtime.setVisibility(VISIBLE);
//                //下载
//                iv_download.setVisibility(VISIBLE);
                //锁定
                iv_lock.setVisibility(VISIBLE);

                //视频比列
                iv_scale.setVisibility(VISIBLE);
                //当前播放时间
                tv_curr_time.setVisibility(GONE);
                //总时间
                tv_total_time.setVisibility(GONE);
                //全屏
                iv_fullscreen.setVisibility(GONE);
                rl_fullscreen.setVisibility(GONE);
                //seekBar
                sb_1.setVisibility(GONE);
                sb_1.setOnSeekBarChangeListener(null);
                //如果不是视频广告播放中
                if (!isPlayVideoAdv) {
                    if (animation != null && tvAvAnnouncement.getVisibility() == VISIBLE) {
                        animation.reset();
                        tvAvAnnouncement.startAnimation(animation);
                    }
                    setGestureEnabled(true);

                    //                //画中画
                    iv_pip.setVisibility(VISIBLE);

                    //seekBar
                    sb_2.setVisibility(VISIBLE);
                    sb_2.setOnSeekBarChangeListener(this);
                    progress = sb_2;
                    ///下一集
                    iv_next.setVisibility(VISIBLE);
                    //时间集合
                    tv_playtime.setVisibility(VISIBLE);
                    //弹幕
                    iv_av_danmaku_land.setVisibility(VISIBLE);
                    iv_av_danmaku_port.setVisibility(VISIBLE);
                    imgTz.setVisibility(View.GONE);
                    imgTzH_land.setVisibility(View.VISIBLE);
                    imgTzH_port.setVisibility(View.VISIBLE);
                }
                if (iv_av_danmaku_land.isSelected()) {
                    tv_danmaku_land.setVisibility(VISIBLE);
                    tvPlaySource_land.setVisibility(VISIBLE);
                } else {
                    tv_danmaku_land.setVisibility(GONE);
                    tvPlaySource_land.setVisibility(GONE);
                }
                if (iv_av_danmaku_port.isSelected()) {
                    tv_danmaku_port.setVisibility(GONE);
                    tvPlaySource_port.setVisibility(GONE);
                } else {
                    tv_danmaku_port.setVisibility(VISIBLE);
                    tvPlaySource_port.setVisibility(VISIBLE);
                }
                //定时关闭
                if(type_id==20)
                    tv_timer_close.setVisibility(View.GONE);
                else
                    tv_timer_close.setVisibility(VISIBLE);
                //播放倍数
                tv_speed.setVisibility(VISIBLE);
                //播放清晰度
                tv_hd.setVisibility(VISIBLE);
                //选集
                tv_selected.setVisibility(VISIBLE);
                break;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v_av_top_bg.getLayoutParams();
        if (curActivity.get() != null && playerState == VideoView.PLAYER_NORMAL) {
            layoutParams.topMargin = BarUtils.getStatusBarHeight();
            layoutParams.height = ConvertUtils.dp2px(45);
        //    BarUtils.setStatusBarVisibility(curActivity.get(), true);
        } else {
            layoutParams.height = ConvertUtils.dp2px(45);
            layoutParams.topMargin = ConvertUtils.dp2px(0);
         //   BarUtils.setStatusBarVisibility(curActivity.get(), false);
        }
        v_av_top_bg.setLayoutParams(layoutParams);

    }

    public void startPlay() {

        if (mControlWrapper != null) {
            if (isShowHint) {
                mControlWrapper.pause();
            } else {
                mControlWrapper.start();
            }
        }
    }

    //播放状态
    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);
//        hideStatusView();
        switch (playState) {
            case VideoView.STATE_IDLE:
                Log.e(TAG, "初始状态");
                isHdChange = true;
                tv_hd.setText(R.string.av_hd);
                iv_bg.setVisibility(VISIBLE);
                clpb_loading.setVisibility(View.GONE);
                sb_1.setProgress(0);
                sb_1.setSecondaryProgress(0);
                sb_2.setProgress(0);
                sb_2.setSecondaryProgress(0);
                setLocked(false);
                iv_lock.setSelected(false);
                iv_play.setSelected(false);
                //重新播放
                v_all_bg.setVisibility(GONE);
                iv_replay.setVisibility(GONE);
                tv_replay.setVisibility(GONE);
                show();
                break;
            case VideoView.STATE_PLAYING:
                Log.e(TAG, "播放中");
                iv_bg.setVisibility(GONE);
                iv_play.setSelected(true);
                hideJiexi();
                clpb_loading.setVisibility(View.GONE);
                post(mShowProgress);
                break;
            case VideoView.STATE_PAUSED:
                Log.e(TAG, "暂停");
                iv_bg.setVisibility(GONE);
                iv_play.setSelected(false);
                break;
            case VideoView.STATE_PREPARING:
                Log.e(TAG, "准备中");
                startBufferTimer();
                onDanmakuChanged(true);
                iv_bg.setVisibility(GONE);
                clpb_loading.setVisibility(View.VISIBLE);
                //重新播放
                v_all_bg.setVisibility(GONE);
                iv_replay.setVisibility(GONE);
                tv_replay.setVisibility(GONE);
                show();
                break;
            case VideoView.STATE_PREPARED:
                stopBufferTime();
//                hide();
                postDelayed(() -> {
                            mShowing = true;
                            hide();
                        },
                        8000L
                );

                Log.e(TAG, "准备好了");
                //hideJiexi();
                iv_bg.setVisibility(GONE);
                if (playingLisenter != null && !isPlayVideoAdv)
                    playingLisenter.playPrepared();
                break;
            case VideoView.STATE_ERROR:
                Log.e(TAG, "错误");
                iv_bg.setVisibility(GONE);
                clpb_loading.setVisibility(View.GONE);
                //重新播放
                v_all_bg.setVisibility(VISIBLE);
                iv_replay.setVisibility(VISIBLE);
                tv_replay.setVisibility(VISIBLE);
                tv_replay.setText(R.string.av_error);
                show();
                break;
            case VideoView.STATE_BUFFERING:
                Log.e(TAG, "加载中");
                iv_bg.setVisibility(GONE);
                clpb_loading.setVisibility(View.VISIBLE);
                iv_play.setSelected(mControlWrapper.isPlaying());
                break;
            case VideoView.STATE_BUFFERED:
                Log.e(TAG, "加载结束");
                iv_bg.setVisibility(GONE);
                clpb_loading.setVisibility(View.GONE);
                iv_play.setSelected(mControlWrapper.isPlaying());
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                Log.e(TAG, "播放完毕");
                if (isPlayVideoAdv) {
                    isPlayVideoAdv = false;
                } else {
                    iv_bg.setVisibility(VISIBLE);
                    clpb_loading.setVisibility(View.GONE);
                    setLocked(false);
//                mControlWrapper.setLock(false);
                    iv_lock.setSelected(false);
                    iv_play.setSelected(false);
                    //重新播放
                    v_all_bg.setVisibility(VISIBLE);
                    iv_replay.setVisibility(VISIBLE);
                    tv_replay.setVisibility(VISIBLE);
                    tv_replay.setText(R.string.av_replay);
                    hide();
                }
                break;
            default:
                Log.e(TAG, "未知状态---" + playState);
                break;
        }
    }

    //播放横屏广告
    public void startPlayHAdv(String htmlCode) {
        this.isPlayVideoAdv = true;
        findViewById(R.id.layoutHAdv).setVisibility(View.VISIBLE);
        WebView webview = findViewById(R.id.webviewH);
        webview.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", null);
    }

    //播放暂停广告
    public FrameLayout startPlayStopHAd(String htmlCode) {
        isPlayStopAd(true);
        //横屏暂停广告
        FrameLayout stopAd = findViewById(R.id.stopAdH);
        stopAd.setVisibility(View.VISIBLE);
        findViewById(R.id.imgCloseStopHAd).setVisibility(View.VISIBLE);
        // stopAd.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", null);
        return stopAd;
    }

    public void stopPlayStopHAd() {
        isPlayStopAd(false);
        findViewById(R.id.stopAdH).setVisibility(View.GONE);
        findViewById(R.id.imgCloseStopHAd).setVisibility(View.GONE);
    }

    public void isPlayStopAd(Boolean is){
        isPlayStopAd = is;
    }

    public Boolean isPlayStopAd() {
        return isPlayStopAd;
    }

    public void stopPayHAdv() {
        this.isPlayVideoAdv = false;
        findViewById(R.id.layoutHAdv).setVisibility(View.INVISIBLE);
    }

    public void setAdvTime(Long mill) {
        TextView textView = findViewById(R.id.ad_timeH);
        textView.setText((mill / 1000) + "s | 免费升级vip关闭此广告");
    }

    //开始视频广告播放
    public void startPlayVideoAdv() {
        this.isPlayVideoAdv = true;
    }

    public boolean getIsPlayVideoAdv() {
        return isPlayVideoAdv;
    }

    public void setIsPlayVideoAdv(boolean msg) {
        this.isPlayVideoAdv = msg;
    }

    //获取视频播放器横竖屏状态
    public boolean getVideoState() {
        return isNormal;
    }

    @Override
    public void show() {
        if (!isPlayVideoAdv)
            show(mDefaultTimeout);
    }

    @Override
    public void hide() {
        if (mShowing) {
            iv_lock.setVisibility(GONE);
            if (!isLocked()) {
                setAllView(false);
            }
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v_av_top_bg.getLayoutParams();
            if (curActivity.get() != null && !mControlWrapper.isFullScreen()) {
                layoutParams.topMargin = BarUtils.getStatusBarHeight();
                layoutParams.height = ConvertUtils.dp2px(45);
             //   BarUtils.setStatusBarVisibility(curActivity.get(), false);
            } else {
                layoutParams.height = ConvertUtils.dp2px(45);
                layoutParams.topMargin = ConvertUtils.dp2px(0);
            }
            v_av_top_bg.setLayoutParams(layoutParams);

            //隐藏播放倍数
            if (speedPop != null && speedPop.isShowing()) {
                speedPop.dismiss();
            }
            //隐藏清晰度
            if (hdPop != null && hdPop.isShowing()) {
                hdPop.dismiss();
            }
            mShowing = false;
        }
    }

    public long getDuration() {
        return mControlWrapper.getDuration();
    }

    public float getPercentage() {
//        if (mControlWrapper.getDuration() == 0) {
//            return 100f;
//        }
        float percentage = mControlWrapper.getCurrentPosition() / (mControlWrapper.getDuration() * 1.0f);
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        System.out.println("进度11：curr=" + mControlWrapper.getCurrentPosition() + " duration=" + mControlWrapper.getDuration() + " percentage=" + percentage + "   >" + df.format(percentage));
        float progress = 0.0F;
        //解决播放点击返回有几率闪退的bug
        try {
            progress = Float.valueOf(df.format(percentage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progress;
    }

    public long getCurProgress() {
        return mControlWrapper.getCurrentPosition();
    }

    public void CheckVodTrySeeBean(int freeCount, CheckVodTrySeeBean bean, boolean isVip, int vod_point_pay) {
        rlEndPay.setVisibility(View.GONE);
        int status = bean.getStatus();
        if (isVip) {
            rlEndUpdate.setVisibility(View.GONE);
            if (isShowHint) {
                isShowHint = false;
                startPlay();
            }
        }

        if (status == 0) {
            isNeedVip = 0;
            trySeeTime = bean.getTrysee();
            llPay.setVisibility(View.GONE);
            llUpdate.setVisibility(View.GONE);
            return;
        }
        if (freeCount > 0) {
            isNeedVip = 0;
            trySeeTime = bean.getTrysee();
            llPay.setVisibility(View.GONE);
            llUpdate.setVisibility(View.GONE);
            return;
        }

        isNeedVip = status;
        trySeeTime = bean.getTrysee();

        if (status == 1) {
            if (isVip) {
                isNeedVip = 0;
                llPay.setVisibility(View.GONE);
                llUpdate.setVisibility(View.GONE);
            } else {
                llPay.setVisibility(View.GONE);
                llUpdate.setVisibility(View.VISIBLE);
                tvUpdateTitle.setText("可试看" + trySeeTime + "分钟，观看完整版请 ");
            }
        } else if (status == 2) {
            llPay.setVisibility(View.VISIBLE);
            llUpdate.setVisibility(View.GONE);
            tvPayTitle.setText("可试看" + trySeeTime + "分钟 ，观看完整版请支付" + vod_point_pay + "积分 ");
            tvEndPayTitle.setText("可试看" + trySeeTime + "分钟 ，观看完整版请支付\n" + vod_point_pay + "积分");
        }
    }

    Timer timer = null;
    Timer bufferTimer = null;

    public void showAd(String gifUrl, String url) {
        llSkip.setVisibility(View.VISIBLE);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (!TextUtils.isEmpty(url))
            llSkip.setOnClickListener(v -> ActivityUtils.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))));
        if (gifUrl.endsWith(".gif"))
            Glide.with(this).asGif().load(gifUrl).centerCrop().into(awvPlayer);
        else
            Glide.with(this).asBitmap().load(gifUrl).centerCrop().into(awvPlayer);

        final int[] time = {6};
        tvSkip.setText(time[0] + "s");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                post(() -> {
                    if (--time[0] <= 0) {
                        llSkip.setVisibility(View.GONE);
                        timer.cancel();
                        timer = null;
                    } else {
                        tvSkip.setText(time[0] + "s");
                    }
                });
            }
        }, 1000, 1000);
    }

    public void showAnnouncement(String announcement) {
        tvAvAnnouncement.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvAvAnnouncement.getLayoutParams();
        layoutParams.width = ConvertUtils.dp2px(1100);
        tvAvAnnouncement.setLayoutParams(layoutParams);
        tvAvAnnouncement.setText(announcement);
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.recomment_in_left);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvAvAnnouncement.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        tvAvAnnouncement.setAnimation(animation);
    }

    private void show(int timeout) {
        if (!mShowing) {
            if (mControlWrapper.isFullScreen()) {
                iv_lock.setVisibility(VISIBLE);
            }
            if (!isLocked()) {
                setAllView(true);
            }

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v_av_top_bg.getLayoutParams();
            if (curActivity.get() != null && !mControlWrapper.isFullScreen()) {
                layoutParams.topMargin = BarUtils.getStatusBarHeight();
                layoutParams.height = ConvertUtils.dp2px(45);
              //  BarUtils.setStatusBarVisibility(curActivity.get(), true);
            } else {
                layoutParams.height = ConvertUtils.dp2px(45);
                layoutParams.topMargin = ConvertUtils.dp2px(0);
            }
            v_av_top_bg.setLayoutParams(layoutParams);
            mShowing = true;
        }
        removeCallbacks(mFadeOut);
        if (timeout != 0) {
            postDelayed(mFadeOut, timeout);
        }
    }

    public void set_v_av_top_bg(int status) {
        switch (status){
            case 0:
                v_av_top_bg0.setVisibility(View.GONE);
                break;
            case 1:
                v_av_top_bg0.setVisibility(View.VISIBLE);
                break;
        }
    }

    //设置所有view
    public void setAllView(boolean isShow) {
        autoSetVisibility(v_top_bg, isShow);
        autoSetVisibility(v_bottom_bg, isShow);
        autoSetVisibility(iv_back, isShow);
        autoSetVisibility(tv_title, isShow);
        autoSetVisibility(tc_localtime, isShow);
        autoSetVisibility(iv_miracast, isShow);
        //暂时隐藏下载功能
//        autoSetVisibility(iv_download, isShow);
        autoSetVisibility(iv_lock, isShow);
        autoSetVisibility(iv_pip, isShow);
        autoSetVisibility(iv_scale, isShow);
        autoSetVisibility(iv_play, isShow);
        autoSetVisibility(tv_curr_time, isShow);
        autoSetVisibility(tv_total_time, isShow);
        autoSetVisibility(tv_playtime, isShow);
        autoSetVisibility(sb_1, isShow);
        autoSetVisibility(sb_2, isShow);
        autoSetVisibility(iv_fullscreen, isShow);
        autoSetVisibility(rl_fullscreen, isShow);
        autoSetVisibility(iv_next, isShow);
        autoSetVisibility(iv_av_danmaku_land, isShow);
        autoSetVisibility(iv_av_danmaku_port, isShow);
        autoSetVisibility(imgTz, isShow);
        autoSetVisibility(imgTzH_land, isShow);
        autoSetVisibility(imgTzH_port, isShow);
        autoSetVisibility(tv_danmaku_land, isShow);
        autoSetVisibility(tv_danmaku_port, isShow);
        autoSetVisibility(tvPlaySource_land, isShow);
        autoSetVisibility(tvPlaySource_port, isShow);
        autoSetVisibility(tv_timer_close, isShow);
        autoSetVisibility(tv_speed, isShow);
        autoSetVisibility(tv_hd, isShow);
        autoSetVisibility(tv_selected, isShow);
    }

    //如果view不是被GONE了，显示或隐藏View
    private void autoSetVisibility(View view, boolean isShow) {
        if (view.getVisibility() == GONE) return;
        if (isShow) {
            view.setVisibility(VISIBLE);
            view.startAnimation(mShowAnim);
        } else {
            view.setVisibility(INVISIBLE);
            view.startAnimation(mHideAnim);
        }
    }

    //显示隐藏会员提示
    public void isShowVipHint(boolean isShowHint) {
        if (rlEndUpdate == null) {
            return;
        }

        this.isShowHint = isShowHint;
        tvEndUpdateTitle.setText("该视频需开通VIP才可观看");
        rlEndUpdate.setVisibility(isShowHint ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void setProgress(int duration, int position) {
        if (controllerClickListener != null) {
            controllerClickListener.onPlayProgress(duration, position);
        }
        if (mControlWrapper == null || mIsDragging) {
            return;
        }
        if (isNeedVip != 0) {
            if (position / 1000f > trySeeTime * 60) {
                if (isNeedVip == 1) {
                    tvEndUpdateTitle.setText("激励广告2小时内只需观看一次");
                    rlEndUpdate.setVisibility(View.VISIBLE);
                    rlEndPay.setVisibility(View.GONE);
                } else {
                    rlEndPay.setVisibility(View.VISIBLE);
                    rlEndUpdate.setVisibility(View.GONE);
                }
                mControlWrapper.pause();
            }
        }
        if (progress != null) {
            if (duration > 0) {
                progress.setEnabled(true);
                int pos = (int) (position * 1.0 / duration * progress.getMax());
                progress.setProgress(pos);
            } else {
                progress.setEnabled(false);
            }
            int percent = mControlWrapper.getBufferedPercentage();
            if (percent >= 95) { //解决缓冲进度不能100%问题
                progress.setSecondaryProgress(progress.getMax());
            } else {
                progress.setSecondaryProgress(percent * 10);
            }
        }

        if (tv_total_time != null)
            tv_total_time.setText(stringForTime((int) duration));
        if (tv_curr_time != null)
            tv_curr_time.setText(stringForTime((int) position));
        if (tv_playtime != null) {
            String text = stringForTime((int) position) + "/" + stringForTime((int) duration);
            tv_playtime.setText(text);
        }

        if (playingLisenter != null && !isPlayVideoAdv) {
            int total = duration / 1000;
            int cu = position / 1000;
            playingLisenter.onPlayIng(cu, total);
        }
    }

//    public void setProgress(SeekBar progress) {
//        if (mControlWrapper == null || mIsDragging) {
//            this.progress = progress;
//            return;
//        }
//
//        //   clpb_loading.setVisibility(View.GONE);
//        long position = mControlWrapper.getCurrentPosition();
//        long duration = mControlWrapper.getDuration();
//        if (isNeedVip != 0) {
//            if (position / 1000f > trySeeTime * 60) {
//                if (isNeedVip == 1) {
//                    rlEndUpdate.setVisibility(View.VISIBLE);
//                    rlEndPay.setVisibility(View.GONE);
//                } else {
//                    rlEndPay.setVisibility(View.VISIBLE);
//                    rlEndUpdate.setVisibility(View.GONE);
//                }
//                mControlWrapper.pause();
//            }
//        }
//        if (progress != null) {
//            if (duration > 0) {
//                progress.setEnabled(true);
//                int pos = (int) (position * 1.0 / duration * progress.getMax());
//                progress.setProgress(pos);
//            } else {
//                progress.setEnabled(false);
//            }
//            int percent = mControlWrapper.getBufferedPercentage();
//            if (percent >= 95) { //解决缓冲进度不能100%问题
//                progress.setSecondaryProgress(progress.getMax());
//            } else {
//                progress.setSecondaryProgress(percent * 10);
//            }
//        }
//
//        if (tv_total_time != null)
//            tv_total_time.setText(stringForTime((int) duration));
//        if (tv_curr_time != null)
//            tv_curr_time.setText(stringForTime((int) position));
//        if (tv_playtime != null) {
//            String text = stringForTime((int) position) + "/" + stringForTime((int) duration);
//            tv_playtime.setText(text);
//        }
//        this.progress = progress;
//    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (!b) {
            return;
        }
        long duration = mControlWrapper.getDuration();

        long newPosition = (duration * i) / progress.getMax();
        if (tv_curr_time != null)
            tv_curr_time.setText(stringForTime((int) newPosition));
        if (tv_playtime != null) {
            String text = stringForTime((int) newPosition) + "/" + stringForTime((int) duration);
            tv_playtime.setText(text);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mIsDragging = true;
        removeCallbacks(mShowProgress);
        removeCallbacks(mFadeOut);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = mControlWrapper.getDuration();
        long newPosition = (duration * seekBar.getProgress()) / progress.getMax();
        mControlWrapper.seekTo((int) newPosition);
        mIsDragging = false;
        post(mShowProgress);
        show();
    }


    @Override
    public void setMediaPlayer(MediaPlayerControl mediaPlayer) {
        super.setMediaPlayer(mediaPlayer);
    }

    //返回键
    @Override
    public boolean onBackPressed() {
        if (isLocked()) {
            show();
            ToastUtils.showShort(R.string.av_lock_tip);
            return true;
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity == null) return super.onBackPressed();
        if (mControlWrapper.isFullScreen()) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            mControlWrapper.stopFullScreen();
            return true;
        }
        return super.onBackPressed();
    }

    public static Activity activity;
    public static PendingIntent pendingIntent;

    @Override
    public void onClick(View view) {
        show();
        int i = view.getId();
        if (view.getId() == R.id.tvEndUpdateButton || view.getId() == R.id.tvUpdateButton) {
            if (mControlWrapper.isFullScreen()) {
                toggleFullScreen();
                checkScreen();
            }
            if (controllerClickListener != null) controllerClickListener.onClick(view);

        } else if (view.getId() == R.id.tvEndPayButton || view.getId() == R.id.tvPayButton) {
            if (mControlWrapper.isFullScreen()) {
                toggleFullScreen();
                checkScreen();
            }
            if (controllerClickListener != null) controllerClickListener.onClick(view);

        } else if (view.getId() == R.id.iv_av_lock) {//先处理锁屏
            doLockUnlock();
        } else if (i == R.id.iv_av_back) {//返回键
            if (mControlWrapper.isFullScreen()) {
                toggleFullScreen();
                checkScreen();
            } else {
                //需要加接口
                if (controllerClickListener != null) controllerClickListener.onClick(view);
            }
        } else if (i == R.id.iv_av_back1) {//返回键
            if (mControlWrapper.isFullScreen()) {
                toggleFullScreen();
                checkScreen();
            } else {
                //需要加接口
                if (controllerClickListener != null) controllerClickListener.onClick(view);
            }
        } else if (i == R.id.iv_av_back2) {//返回键
            if (mControlWrapper.isFullScreen()) {
                toggleFullScreen();
                checkScreen();
            } else {
                //需要加接口
                if (controllerClickListener != null) controllerClickListener.onClick(view);
            }
        } else if (i == R.id.iv_av_miracast) {//投屏
            if (controllerClickListener != null) {
                hide();
                controllerClickListener.onClick(view);
            }
        } else if (i == R.id.iv_av_download) {//下载
            if (controllerClickListener != null) controllerClickListener.onClick(view);
        } else if (i == R.id.iv_av_pip) {//画中画
            if (controllerClickListener != null) controllerClickListener.onClick(view);
        } else if (i == R.id.iv_av_scale) {//视频比例
            doScale();
        } else if (i == R.id.iv_av_play) {//播放
            setClickivPlay(true);
            // if (!isPlayStopAd)
            togglePlay();
        } else if (i == R.id.rl_av_fullscreen) {//全屏
            toggleFullScreen();
            checkScreen();
        } else if (i == R.id.iv_av_next) {//下集
            if (controllerClickListener != null) controllerClickListener.onClick(view);
        } else if (i == R.id.iv_av_danmaku_land ||i == R.id.iv_av_danmaku_port) {//弹幕开关
            onDanmakuChanged(false);
        } else if (i == R.id.tv_av_danmaku_land||i == R.id.tv_av_danmaku_port) {//显示弹幕发送框
            showDanmaku();
        } else if (i == R.id.tv_timer_close) {//定时关闭

            //获取当前系统时间
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            //弹出闹钟框
            TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    // 获取AlarmManager对象
                    AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(activity, WakeupReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                  //设置闹钟触发时间为每天上午8点
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        // 允许在空闲时发送广播，Android6.0之后新增的方法
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                    else
                    {
                        // 设置一次性闹钟，延迟若干秒后，携带延迟意图发送闹钟广播(但Android6.0之后，set方法在暗屏时不保证发送广播，必须调用setAndAllowWhileIdle方法)
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }

                    Log.i("美国",System.currentTimeMillis()+"");
                    Log.i("美国",calendar.getTimeInMillis()+"");


                    String hour=hourOfDay<10?("0"+hourOfDay):(""+hourOfDay);
                    String minut=minute<10?("0"+minute):(""+minute);
                    tv_timer_close.setText(hour + ":" + minut);

                }
            },hour,minute,true);
            timePickerDialog.show();


        }
        else if (i == R.id.tv_av_speed) {//视频播放速度
            if (controllerClickListener != null) {
                hide();
                view.setTag(curSpeedSelect);
                controllerClickListener.onClick(view);
            }
        } else if (i == R.id.tv_av_hd) {//视频清晰度
            if (controllerClickListener != null) controllerClickListener.onClick(view);
        } else if (i == R.id.tv_av_selected) {//选集
            if (controllerClickListener != null) {
                hide();
                controllerClickListener.onClick(view);
            }
        } else if (i == R.id.iv_av_replay) {//重新播放
            if (mReplayByCurProgress) {
                mControlWrapper.replay(true);
                Intent intent = new Intent("cn.whiner.av.AvVideoController");
                intent.putExtra("type", RECEIVER_TYPE_REPLAY);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                mReplayByCurProgress = false;
                System.out.println("进度6：==");
            } else {
                mControlWrapper.replay(true);
                System.out.println("进度7：==");
            }
        } else if (i == R.id.tvPlaySource_land||i == R.id.tvPlaySource_port) {//选择播放源
            if (controllerClickListener != null) {
                hide();
                controllerClickListener.onClick(view);
            }
        } else if (i == R.id.imgTz || i == R.id.imgTzH_land||i == R.id.imgTzH_port) {//跳转播放
            if (controllerClickListener != null) {
                controllerClickListener.onClick(view);
            }
        } else {
            ToastUtils.showShort("未知View" + view.getId());
        }
    }

    //锁定处理
    protected void doLockUnlock() {
        if (isLocked()) {
            setLocked(false);
            setGestureEnabled(true);
            mShowing = false;
            show();
            ToastUtils.showShort(R.string.av_unlocked);
        } else {
            hide();
            setLocked(true);
            setGestureEnabled(false);
            ToastUtils.showShort(R.string.av_locked);
        }
        iv_lock.setSelected(isLocked());
//        setLocked(false);
    }

    //视频比例模式
    private int scale_val = 0;

    protected void doScale() {
        scale_val++;
        if (scale_val >= 6) scale_val = 0;
        switch (scale_val) {
            case 0:
                ToastUtils.showShort("默认");
                mControlWrapper.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
                break;
            case 1:
                ToastUtils.showShort("16:9");
                mControlWrapper.setScreenScaleType(VideoView.SCREEN_SCALE_16_9);
                break;
            case 2:
                ToastUtils.showShort("4:3");
                mControlWrapper.setScreenScaleType(VideoView.SCREEN_SCALE_4_3);
                break;
            case 3:
                ToastUtils.showShort("填充");
                mControlWrapper.setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT);
                break;
            case 4:
                ToastUtils.showShort("原始大小");
                mControlWrapper.setScreenScaleType(VideoView.SCREEN_SCALE_ORIGINAL);
                break;
            case 5:
                ToastUtils.showShort("居中裁剪");
                mControlWrapper.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
                break;
            default:
                break;
        }
    }

    public void huaTest() {
        if (!isNormal) {
            toggleFullScreen();
            checkScreen();
        }
    }

    //弹幕开关
    public void onDanmakuChanged(boolean isFirst) {
        if (videoViewImpt == null) return;
        if (iv_av_danmaku_land == null || tv_danmaku_land == null) return;
        if (iv_av_danmaku_port == null || tv_danmaku_port == null) return;
        if (isFirst) {
            boolean b = SPUtils.getInstance().getBoolean(KEY_IS_OPEN_DANMAKU, true);
            Log.e(TAG, b + "2");
            //设置相反的状态
            iv_av_danmaku_land.setSelected(b);
            iv_av_danmaku_port.setSelected(b);
            if (b) {
                videoViewImpt.showDanmaku();
            } else {
                videoViewImpt.hideDanmaku();
            }
        } else {
            if (!iv_av_danmaku_land.isSelected()||!iv_av_danmaku_port.isSelected()) {
                //ToastUtils.showShort("弹幕开");
                iv_av_danmaku_land.setSelected(true);
                iv_av_danmaku_port.setSelected(true);
                videoViewImpt.showDanmaku();
                SPUtils.getInstance().put(KEY_IS_OPEN_DANMAKU, true);
                if (iv_av_danmaku_land.getVisibility() == VISIBLE) {
                    tv_danmaku_land.setVisibility(VISIBLE);
                    tvPlaySource_land.setVisibility(VISIBLE);
                }
                if (iv_av_danmaku_port.getVisibility() == VISIBLE) {
                    tv_danmaku_port.setVisibility(VISIBLE);
                    tvPlaySource_port.setVisibility(VISIBLE);
                }
            } else {
                //ToastUtils.showShort("弹幕关");
                iv_av_danmaku_land.setSelected(false);
                iv_av_danmaku_port.setSelected(false);
                videoViewImpt.hideDanmaku();
                SPUtils.getInstance().put(KEY_IS_OPEN_DANMAKU, false);
                tv_danmaku_land.setVisibility(GONE);
                tv_danmaku_port.setVisibility(GONE);
                tvPlaySource_land.setVisibility(GONE);
                tvPlaySource_port.setVisibility(GONE);
            }
        }
    }

    //显示弹幕输入框
    private DanmuInputDialog popupWindow;
    protected void showDanmaku() {
        if (controllerClickListener != null && !controllerClickListener.isLogin()) {
            return;
        }
        if (popupWindow == null) {
            popupWindow = new DanmuInputDialog(getContext());
            popupWindow.setOnSendClickListener(new DanmuInputDialog.OnSendClickListener() {
                @Override
                public void onSend(String value, String textcolor) {
                    if (videoViewImpt == null) return;
                    if (!StringUtils.isEmpty(value)) {
                        videoViewImpt.addDanmaku(value, textcolor, true);
                        if (controllerClickListener != null) {
                            controllerClickListener.onSendDanmu(value, textcolor);
                        }
                    } else {
                        ToastUtils.showShort("请输入弹幕！");
                    }
                }
            });
        }
        popupWindow.show();
    }

 /*   //发送弹幕---还需要上传到服务器
    private void sendDanmaku() {
        if (videoViewImpt == null) return;
        if (et_danmaku != null) {addDanmaku
            String str = et_danmaku.getText().toString();
            if (!StringUtils.isEmpty(str)) {
                videoViewImpt.addDanmaku(str, true);
            } else {
                ToastUtils.showShort("请输入弹幕！");
            }
        }
    }*/

    public void setReplayByCurProgress(Boolean replayByCurProgress) {
        mReplayByCurProgress = replayByCurProgress;
    }

    public void setSpeed(String speed) {
        tv_speed.setText(speed);
    }

    private int curSpeedSelect = SPUtils.getInstance().getInt(KEY_SPEED_INDEX, 3);

    public void setSpeedSelect(String speed) {
        switch (speed) {
            case "0.5X":
                videoViewImpt.setVideoSpeed(0.50f);
                tv_speed.setText(getResources().getText(R.string.av_speed_1));
                curSpeedSelect = 5;
                break;
            case "0.75X":
                videoViewImpt.setVideoSpeed(0.75f);
                tv_speed.setText(getResources().getText(R.string.av_speed_2));
                curSpeedSelect = 4;
                break;
            case "1.0X":
                videoViewImpt.setVideoSpeed(1.00f);
                tv_speed.setText(getResources().getText(R.string.av_speed_3));
                curSpeedSelect = 3;
                break;
            case "1.25X":
                videoViewImpt.setVideoSpeed(1.25f);
                tv_speed.setText(getResources().getText(R.string.av_speed_4));
                curSpeedSelect = 2;
                break;
            case "1.5X":
                videoViewImpt.setVideoSpeed(1.50f);
                tv_speed.setText(getResources().getText(R.string.av_speed_5));
                curSpeedSelect = 1;
                break;
            case "2.0X":
                videoViewImpt.setVideoSpeed(2.00f);
                tv_speed.setText(getResources().getText(R.string.av_speed_6));
                curSpeedSelect = 0;
                break;
            default:
                break;
        }
        SPUtils.getInstance().put(KEY_SPEED_INDEX, curSpeedSelect);
    }


    //speed播放速度
    private PopupWindow speedPop;
    private LinearLayout speedPopLayout;
    private TextView tv_speed_1, tv_speed_2, tv_speed_3, tv_speed_4, tv_speed_5, tv_speed_6;
    private TextView oldSpeedTv;
    private OnClickListener speedOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoViewImpt == null) return;
            int i = v.getId();
            if (i == R.id.tv_pop_speed_1) {
                videoViewImpt.setVideoSpeed(0.50f);
                tv_speed_1.setTextColor(getResources().getColor(R.color.player_theme_color));
                tv_speed.setText(getResources().getText(R.string.av_speed_1));
                if (oldSpeedTv != null) oldSpeedTv.setTextColor(Color.WHITE);
                oldSpeedTv = tv_speed_1;
            } else if (i == R.id.tv_pop_speed_2) {
                videoViewImpt.setVideoSpeed(0.75f);
                tv_speed_2.setTextColor(getResources().getColor(R.color.player_theme_color));
                tv_speed.setText(getResources().getText(R.string.av_speed_2));
                if (oldSpeedTv != null) oldSpeedTv.setTextColor(Color.WHITE);
                oldSpeedTv = tv_speed_2;
            } else if (i == R.id.tv_pop_speed_3) {
                videoViewImpt.setVideoSpeed(1.00f);
                tv_speed_3.setTextColor(getResources().getColor(R.color.player_theme_color));
                tv_speed.setText(getResources().getText(R.string.av_speed_3));
                if (oldSpeedTv != null) oldSpeedTv.setTextColor(Color.WHITE);
                oldSpeedTv = tv_speed_3;
            } else if (i == R.id.tv_pop_speed_4) {
                videoViewImpt.setVideoSpeed(1.25f);
                tv_speed_4.setTextColor(getResources().getColor(R.color.player_theme_color));
                tv_speed.setText(getResources().getText(R.string.av_speed_4));
                if (oldSpeedTv != null) oldSpeedTv.setTextColor(Color.WHITE);
                oldSpeedTv = tv_speed_4;
            } else if (i == R.id.tv_pop_speed_5) {
                videoViewImpt.setVideoSpeed(1.50f);
                tv_speed_5.setTextColor(getResources().getColor(R.color.player_theme_color));
                tv_speed.setText(getResources().getText(R.string.av_speed_5));
                if (oldSpeedTv != null) oldSpeedTv.setTextColor(Color.WHITE);
                oldSpeedTv = tv_speed_5;
            } else if (i == R.id.tv_pop_speed_6) {
                videoViewImpt.setVideoSpeed(2.00f);
                tv_speed_6.setTextColor(getResources().getColor(R.color.player_theme_color));
                tv_speed.setText(getResources().getText(R.string.av_speed_6));
                if (oldSpeedTv != null) oldSpeedTv.setTextColor(Color.WHITE);
                oldSpeedTv = tv_speed_6;
            }
            speedPop.dismiss();
        }
    };

    private void selectSpeeed() {
        if (speedPop == null || speedPopLayout == null) {
            speedPopLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.pop_layout_speed, this, false);
            tv_speed_1 = speedPopLayout.findViewById(R.id.tv_pop_speed_1);
            tv_speed_1.setOnClickListener(speedOnClickListener);
            tv_speed_2 = speedPopLayout.findViewById(R.id.tv_pop_speed_2);
            tv_speed_2.setOnClickListener(speedOnClickListener);
            tv_speed_3 = speedPopLayout.findViewById(R.id.tv_pop_speed_3);
            tv_speed_3.setTextColor(getResources().getColor(R.color.player_theme_color));
            oldSpeedTv = tv_speed_3;
            tv_speed_3.setOnClickListener(speedOnClickListener);
            tv_speed_4 = speedPopLayout.findViewById(R.id.tv_pop_speed_4);
            tv_speed_4.setOnClickListener(speedOnClickListener);
            tv_speed_5 = speedPopLayout.findViewById(R.id.tv_pop_speed_5);
            tv_speed_5.setOnClickListener(speedOnClickListener);
            tv_speed_6 = speedPopLayout.findViewById(R.id.tv_pop_speed_6);
            tv_speed_6.setOnClickListener(speedOnClickListener);
            speedPop = new PopupWindow(speedPopLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            speedPop.setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent)));
            speedPop.setOutsideTouchable(true);
            speedPop.setClippingEnabled(false);
        }
        speedPopLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        speedPop.showAsDropDown(tv_speed, -((speedPopLayout.getMeasuredWidth() - tv_speed.getMeasuredWidth()) / 2),
                -(speedPopLayout.getMeasuredHeight() + tv_speed.getMeasuredHeight()));
    }


    //清晰度
    private PopupWindow hdPop;
    private LinearLayout hdPopLayout;
    private boolean isHdChange = true;
    private int currentIndex = 0;
    private List<String> hdList;
    private OnClickListener rateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoViewImpt == null) return;
            int index = (int) v.getTag();
            if (currentIndex == index) return;
            ((TextView) hdPopLayout.getChildAt(currentIndex)).setTextColor(Color.BLACK);
            ((TextView) hdPopLayout.getChildAt(index)).setTextColor(getResources().getColor(R.color.player_theme_color));
            tv_hd.setText(hdList.get(index));
            videoViewImpt.switchHd(hdList.get(index));
            hdPop.dismiss();
            currentIndex = index;
        }
    };

    private void selectHd() {
        if (videoViewImpt == null) return;
        if (hdPop == null || hdPopLayout == null || isHdChange) {
            isHdChange = false;
            hdPopLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.pop_layout_hd, this, false);
            hdPop = new PopupWindow(hdPopLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            hdPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
            hdPop.setOutsideTouchable(true);
            hdPop.setClippingEnabled(false);
            hdList = new ArrayList<>();
            LinkedHashMap<String, String> definitionData = videoViewImpt.getHdData();
            if (definitionData == null) return;
            int i = 0;
            for (Map.Entry<String, String> entry : definitionData.entrySet()) {
                LogUtils.d("key:" + entry.getKey() + "   value:" + entry.getValue());
                hdList.add(entry.getKey());
                TextView rateItem = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.pop_item_layout_hd, hdPopLayout, false);
                rateItem.setText(entry.getKey());
                rateItem.setTag(i);
                if (i == currentIndex) {
                    rateItem.setTextColor(getResources().getColor(R.color.player_theme_color));
                }
                rateItem.setOnClickListener(rateOnClickListener);
                hdPopLayout.addView(rateItem);
                i++;
            }
        }
        hdPopLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        hdPop.showAsDropDown(tv_hd, -((hdPopLayout.getMeasuredWidth() - tv_hd.getMeasuredWidth()) / 2),
                -(hdPopLayout.getMeasuredHeight() + tv_hd.getMeasuredHeight()));
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (title == null) {
            return;
        }
        tv_title.post(() -> {
            tv_title.setText(title);
            if (title.length() > 10)
                tv_title.startScroll();
        });
    }


    public void showJiexi() {
        play_imgs();
        tvJiexiMsg.setText("正在获取资源、请耐心等待");
        clpb_jiexi.post(() -> clpb_jiexi.setVisibility(View.VISIBLE));
    }

    public void updateJiexiProgess(String progress) {
        post(() -> tvJiexiMsg.setText(progress));
    }

    public void showPlayErrorView() {
        loge.setVisibility(GONE);
        gifIMageView.setVisibility(GONE);
        btError.setVisibility(View.VISIBLE);
        btReplay.setVisibility(View.VISIBLE);

        btError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideJiexi();
                if (controllerClickListener != null) controllerClickListener.onClick(v);
            }
        });

        btReplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideJiexi();
                if (controllerClickListener != null) controllerClickListener.onClick(v);
            }
        });

    }
    //播放器加载特效远程图片地址
    public static String iv_replay_logo = null;
    public static String Number_of_awards_descriptions = null;
    public static int csj_video_advs = 0;

    public void visibility_wqddg(String description, String Number_of_awards_description, int csj_video_adv) {
        iv_replay_logo = description;
        csj_video_advs = csj_video_adv;
        Number_of_awards_descriptions = Number_of_awards_description;
    }

    public void play_imgs() {
        Log.d("播放器图片地址1", "iv_replay_logo: " + iv_replay_logo);
        if (iv_replay_logo != null) { //加载网络图片
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.rf_logo)  //加载错误时显示该图
                    .placeholder(R.drawable.rf_logo) //加载时显示该图
                    .override(Target.SIZE_ORIGINAL) //指定大小  Target.SIZE_ORIGINAL代表原始大小
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .skipMemoryCache(true);//禁用内存缓存
            Glide.with(mActivity)
                    .load(iv_replay_logo)
                    .apply(requestOptions)
                    .into(play_img);
            Glide.with(mActivity)
                    .load(iv_replay_logo)
                    .apply(requestOptions)
                    .into(loge);
        } else {
            play_img.setBackgroundResource(R.drawable.rf_logo);
        }
    }
    public void hideJiexi() {
        clpb_jiexi.post(new Runnable() {
            @Override
            public void run() {
                clpb_jiexi.setVisibility(View.GONE);
                loge.setVisibility(VISIBLE);
                gifIMageView.setVisibility(VISIBLE);
                btError.setVisibility(View.GONE);
                btReplay.setVisibility(View.GONE);
            }
        });
    }

    public void setClickivPlay(Boolean clickivPlay) {
        isClickivPlay = clickivPlay;
    }

    public Boolean getClickivPlay() {
        return isClickivPlay;
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        videoViewImpt = null;
        stopBufferTime();
    }

    private void stopBufferTime() {
        if (bufferTimer != null) {
            bufferTimer.cancel();
            bufferTimer = null;
        }
        if (bufferTask != null) {
            bufferTask.cancel();
            bufferTask = null;
        }
    }
}
