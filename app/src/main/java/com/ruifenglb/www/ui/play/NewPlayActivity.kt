package com.ruifenglb.www.ui.play

import android.app.Activity
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.ad.biddingsdk.AdConstant
import com.app.ad.biddingsdk.AdListener
import com.app.ad.biddingsdk.AdUtils
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.StringUtils
import com.dueeeke.videocontroller.component.GestureView
import com.dueeeke.videoplayer.player.VideoView
import com.dueeeke.videoplayer.player.VideoViewManager
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver
import com.github.StormWyrm.wanandroid.base.net.observer.PlayLoadingObserver
import com.liuwei.android.upnpcast.NLUpnpCastManager
import com.liuwei.android.upnpcast.device.CastDevice
import com.liuwei.android.upnpcast.util.CastUtils
import com.lxj.xpopup.XPopup
import com.ruifenglb.av.CheckVodTrySeeBean
import com.ruifenglb.av.MyPlayerManager
import com.ruifenglb.av.play.AvVideoController
import com.ruifenglb.av.play.*
import com.ruifenglb.av.play.AvVideoController.RECEIVER_TYPE_REPLAY
import com.ruifenglb.av.play.AvVideoController.RECEIVER_TYPE_TIMER
import com.ruifenglb.av.play.AvVideoView
import com.ruifenglb.av.play.ControllerClickListener
import com.ruifenglb.av.play.ControllerPlayIngLisenter
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.*
import com.ruifenglb.www.entity.AdvEntity
import com.ruifenglb.www.jiexi.BackListener
import com.ruifenglb.www.jiexi.JieXiUtils2
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.pip.PIPManager
import com.ruifenglb.www.ui.dlan.DlanListPop
import com.ruifenglb.www.ui.feedback.FeedbackActivity
import com.ruifenglb.www.ui.login.LoginActivity
import com.ruifenglb.www.ui.pay.PayActivity
import com.ruifenglb.www.ui.widget.AdControlView
import com.ruifenglb.www.ui.widget.HitDialog
import com.ruifenglb.www.utils.*
import com.ruifenglb.www.utils.MMkvUtils.Companion.Builds
import kotlinx.android.synthetic.main.activity_new_play.*
import kotlinx.android.synthetic.main.layout_html_ad_view.*
import java.text.DecimalFormat
import com.ruifenglb.www.utils.UserUtils.isLogin
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList


open class NewPlayActivity : BaseActivity(), OnSpeedItemClickListener {
    private val TAG = "NewPlayActivity"
    private lateinit var controller: AvVideoController
    private lateinit var mVodBean: VodBean
    private var isShowPlayProgress = false
    private var isCanPlayAd2 = true
    open var curPlayUrl: String = ""
    private var isAllowCastScreen: Boolean = false//根据视频的类型，来判断是否可以投屏
    private var curParseIndex = 0//记录上一次解析到的位置，如果出现解析到是视频不能播放的话 自动解析下一条
    private var curFailIndex = -1
    private var isPlay = false//是否正在播放
    open var playSourceIndex = 0//播放源位置
    private var urlIndex = 0//当前播放的到哪一集
    private lateinit var playFormList: List<PlayFromBean>
    open lateinit var playFrom: PlayFromBean //当前播放播放源信息
    open var playList: List<UrlBean>? = null//当前播放列表
    private var playScoreInfo: PlayScoreBean? = null
    private var isParseSuccess = false
    private var isSeekToHistory: Boolean = false
    private var curProgressHistory: Long = 0
    private var vodDuration: Long = 0
    private var videoNetProgress: Long = 0L
    private var pipManager: PIPManager? = null
    private var mIsShowing: Boolean? = false
    private var IsSwitchkernel: Boolean = false

    private var gg =0;

    private val onJiexiResultListener = object : BackListener {

        override fun onSuccess(url: String?, curParseIndex: Int) {
            //正式编译禁止输出
            //println("===Jiexi onSuccess 坐标：$curParseIndex url=${url}")
            //println("---play----onSuccess" + url)
            if (isSuccess) {
                Log.d(TAG, "====ParseonFail  " + "\n url=" + url)
                return
            } else {
                Log.d(TAG, "====ParseonSuccess  " + "\n url=" + url)
            }
            curFailIndex = curParseIndex
            //正式编译禁止输出
            // println("---play----onSuccess=false curFailIndex=" + curFailIndex + " 当前url" + url)
            if (url == null || url.isEmpty()) {
                println("===修复onSuccess")
                runOnUiThread {
                    chengeNextLine()
                }
                return
            }
            //正式编译禁止输出
            // println("---play----isPlay=" + isPlay)
            url.let {
                if (!isPlay) {
                    Log.d(TAG, "====ParseonSuccess  play（）" + " url=" + it)
                    play(it)
                    curPlayUrl = it
                    isPlay = true
                }
            }


        }

        override fun onError() {
            Log.e("解析错误", "1111111111111111")
            controller.updateJiexiProgess("获取资源失败,请换来源或者联系客服解决！")
            controller.showPlayErrorView()

            // 自动切换到下一个播放源
            if (playSourceIndex + 1 > playFormList.count() - 1) {
                Toast.makeText(this@NewPlayActivity, "没有其他播放源了.", Toast.LENGTH_SHORT).show()
                return
            }

            val playFromBean = playFormList[playSourceIndex + 1]
            changePlaySource(playFromBean)
        }

        override fun onProgressUpdate(msg: String?) {
            controller.updateJiexiProgess(msg)
        }
    }
    private var videoDetailFragment: VideoDetailFragment? = null
    private var summaryFragment: SummaryFragment? = null
    private var playListFragment: PlayListFragment? = null
    private var isParsed: Boolean = false
    private var isLandscape = false//当前是否为横屏
    private var timerCount: MyCount? = null
    private lateinit var videoView: AvVideoView
    override fun getLayoutResID(): Int {
        return R.layout.activity_new_play
    }

    override fun initView() {
        super.initView()
        // Builds().loadStartBean("")?.ads?.player_pause?.status // 0关闭 1,开启
//        Builds().loadStartBean("")?.ads?.player_pause?.description // 广告代码

//        EventBus.getDefault().register(this)
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.player_status_color))

        videoView = VideoViewManager.instance().get("pip") as AvVideoView
        controller = AvVideoController(videoView, this)

        //添加手势
        val gestureControlView = GestureView(this)
        controller.addControlComponent(gestureControlView)
        pipManager = PIPManager.getInstance()
        videoView.setVideoController(controller)
        if (pipManager?.isStartFloatWindow!!) {
            mIsShowing = true
            pipManager?.stopFloatWindow()
            controller.setPlayerState(videoView.currentPlayerState)
            controller.setPlayState(videoView.currentPlayState)
            mVodBean = pipManager?.vodBean as VodBean
            isShowPlayProgress = pipManager?.getShouldProgress!!

        } else {
            mIsShowing = false
            mVodBean = intent.getParcelableExtra(PlayActivity.KEY_VOD) as VodBean
            isShowPlayProgress = intent.getBooleanExtra(PlayActivity.KEY_SHOW_PROGRESS, false)
            pipManager?.actClass = NewPlayActivity::class.java
        }

        controller.type_id=mVodBean.type_id;

        frameLayout.addView(videoView)

        registerReceiver()
        //  videoView=MyPlayerManager.checktFactory("0",videoView)

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun startHua(msg:String){
//        if (msg=="画中画"){
//            requePer {
//                pipManager?.startFloatWindow();
//                pipManager?.resume();
//                finish()
//            }
//        }
//    }
    private fun adGroup() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(this@NewPlayActivity, vodService.addGroup(), object : BaseObserver<CardBuyBean>() {
            override fun onSuccess(data: CardBuyBean) {
                Log.e("test", "onSuccess " + data.msg)
                ToastUtils.showLong(data.msg)
                EventBus.getDefault().post(LoginBean())
            }

            override fun onError(e: ResponseException) {
                Log.e("test", "onError " + e.getErrorMessage())
            }
        })
    }

    override fun initListener() {
        super.initListener()

        imgCloseStopAd.setOnClickListener {
            stopdH.visibility = View.GONE
            imgCloseStopAd.visibility = View.GONE
            controller.isPlayStopAd(false)
        }
        ad_timeV.setOnClickListener {
            updateVip()
        }
        /* goAdVideo.setOnClickListener{
            if(!isLogin()){
                ToastUtils.showShort("请先登录")
                LoginActivity.start()
            }else{
                App.getApplication().adUtils.showRewardAd(this@NewPlayActivity,object: AdListener() {
                    override fun onComplete() {
                        adGroup()
                    }

                    override fun onError() {
                        goAdVideo.visibility= View.GONE
                        goAdGame.visibility = View.GONE
                    }
                })

            }
        }
        goAdGame.setOnClickListener{
            App.getApplication().adUtils.showGameActivity(this@NewPlayActivity)
        }*/

        controller.setControllerPlayIngLisenter(object : ControllerPlayIngLisenter {
            override fun onPlayIng(position: Int, total: Int) {
                val wei = CastUtils.getIntTime(Builds().loadPlayTiaoZhuan2(mVodBean.vod_id))
                val tou = CastUtils.getIntTime(Builds().loadPlayTiaoZhuan(mVodBean.vod_id))
//                if (total *1000 > wei && wei != 0L && total*1000-tou > wei){
//                    if (position *1000 >= wei){
//                        Toast.makeText(this@NewPlayActivity, "自动跳过片尾。", Toast.LENGTH_SHORT).show()
//                        playNext()
//                    }
//                }
                if (total == position && total != 0) {
                    playNext()
                }
                if (wei < total * 1000 - wei && total != 0 && wei != 0L && wei + tou < total * 1000) {
                    if ((total - position) * 1000 <= wei) {
                        Toast.makeText(this@NewPlayActivity, "自动跳过片尾。", Toast.LENGTH_SHORT).show()
                        playNext()
                    }
                }


            }

            override fun playPrepared() {
                // prepared()
            }

        })

        controller.setControllerClickListener(
                object : ControllerClickListener {
                    override fun onClick(it: View) {
                        when (it.id) {
                            //嗅探错误提交错误信息
                            R.id.btError -> {
                                val message = "视频《${mVodBean.vod_name}》播放失败\n播放源：${playFrom.player_info.show}\n视频序列：${playList!![urlIndex].name}"
                                FeedbackActivity.start(this@NewPlayActivity, message)


                            }
                            R.id.btReplay -> {
                                // parseData()
                                if (playSourceIndex + 1 > playFormList.count() - 1) {
                                    Toast.makeText(this@NewPlayActivity, "没有其他播放源了.", Toast.LENGTH_SHORT).show()
                                    return
                                }


                                val playFromBean = playFormList[playSourceIndex + 1]
                                changePlaySource(playFromBean)
                            }

                            R.id.tv_av_hd ->
                                chengeNextLine()
                            R.id.iv_av_back, R.id.iv_av_back1, R.id.iv_av_back2 -> {
                                Log.i("bds", "back===========")
                                finish();
                                Builds().savePlayScore(null)
                                playScoreInfo = null
                                savePlayRecord(true)
                                setResult(3)
                            }
                            R.id.imgTz, R.id.imgTzH_land,R.id.imgTzH_port -> {
                                HitDialog(mActivity!!).setTitle("跳过片头/尾").setInputView(mVodBean.vod_id).setOnHitDialogClickListener(object : HitDialog.OnHitDialogClickListener() {
                                    override fun onCancelClick(dialog: HitDialog) {
                                        super.onCancelClick(dialog)
                                    }

                                    override fun onTz(dialog: HitDialog, tou: String, wei: String) {
                                        super.onTz(dialog, tou, wei)
                                        val patt = "\\d\\d:\\d\\d:\\d\\d".toRegex()
                                        if (patt.matches(tou)) {
                                            Builds().savePlayTiaoZhuan(tou, mVodBean.vod_id)
                                        } else {
                                            Toast.makeText(this@NewPlayActivity, "请输入正确时间格式：00:00:00", Toast.LENGTH_LONG).show()
                                            return
                                        }
                                        if (patt.matches(wei)) {
                                            Builds().savePlayTiaoZhuan2(wei, mVodBean.vod_id)
                                        } else {
                                            Toast.makeText(this@NewPlayActivity, "请输入正确时间格式：00:00:00", Toast.LENGTH_LONG).show()
                                            return
                                        }
                                        dialog.dismiss()
                                        // videoView.seekTo(speed)
                                    }
                                }).show()
                            }
                            //画中画
                            R.id.iv_av_pip -> {


                                if (MMkvUtils.Builds().loadStartBean("")?.ads?.pictureinpicture?.status != 0) {

                                    if (isPlay) {
                                        if (!LoginUtils.checkLogin2(mActivity)) {
                                            HitDialog(mActivity).setTitle("提示").setMessage("需登录后开通VIP才可使用画中画，确定登录。").setOnHitDialogClickListener(object : HitDialog.OnHitDialogClickListener() {
                                                override fun onCancelClick(dialog: HitDialog) {
                                                    super.onCancelClick(dialog)
                                                }

                                                override fun onOkClick(dialog: HitDialog) {
                                                    super.onOkClick(dialog)
                                                    ActivityUtils.startActivity(LoginActivity::class.java)
                                                }
                                            }).show()
                                        } else {
                                            if (LoginUtils.checkVIP(this@NewPlayActivity, "画中画需要开通vip,是否去开通")) {
                                                requePer {
                                                    Log.e("画中画", "开启")
                                                    controller.huaTest()
                                                    pipManager?.vodBean = mVodBean
                                                    pipManager?.setShouldProgress(isShowPlayProgress)
                                                    pipManager?.startFloatWindow(getPipMsgBean())
                                                    pipManager?.resume()
                                                    finish()
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(mActivity, "等待播放地址获取完成在尝试。", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    if (isPlay) {
                                        requePer {
                                            Log.e("画中画", "开启")
                                            controller.huaTest()
                                            pipManager?.vodBean = mVodBean
                                            pipManager?.setShouldProgress(isShowPlayProgress)
                                            pipManager?.startFloatWindow(getPipMsgBean());
                                            pipManager?.resume();
                                            finish()
                                        }
                                    } else {
                                        Toast.makeText(mActivity, "等待播放地址获取完成在尝试。", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            R.id.iv_av_next ->
                                playNext()
                            R.id.tv_av_speed ->
                                showSpeedListDialog(it.tag as Int)
                            R.id.tv_av_selected ->
                                showPlayListDialog()
                            R.id.tvPlaySource_land, R.id.tvPlaySource_port ->
                                showPlaySourceDialog()
                            //投屏
                            R.id.iv_av_miracast -> {
                                if (MMkvUtils.Builds().loadStartBean("")?.ads?.projection?.status != 0) {
                                    if (!LoginUtils.checkLogin2(mActivity)) {
                                        HitDialog(mActivity!!).setTitle("提示").setMessage("需登录后开通VIP才可投屏，确定登录。").setOnHitDialogClickListener(object : HitDialog.OnHitDialogClickListener() {
                                            override fun onCancelClick(dialog: HitDialog) {
                                                super.onCancelClick(dialog)
                                            }

                                            override fun onOkClick(dialog: HitDialog) {
                                                super.onOkClick(dialog)
                                                ActivityUtils.startActivity(LoginActivity::class.java)
                                            }
                                        }).show()
                                    } else {
                                        if (LoginUtils.checkVIP(this@NewPlayActivity, "投屏需要开通vip,是否去开通")) {
                                            showCastScreenDialog()
                                        }
                                    }
                                } else {
                                    showCastScreenDialog()
                                }
                            }


                            R.id.tvPayButton, R.id.tvEndPayButton -> {
                                payPlay()
                            }
                            R.id.tvUpdateButton, R.id.tvEndUpdateButton -> {
                                updateVip()
                            }
                            /*  R.id.btn_pop_danmaku -> {
                                val s = it.tag as String
                                sendDanmu(s)
                            }*/
                        }
                    }

                    override fun onPlayProgress(duration: Int, position: Int) {
                        //当播放进度距离最后一条danmu小于10s，开始获取后面的弹幕
                        if (!loadingDanmu && (lastTime_getdanmu - position < 1000 * 10) && videoView.isDanmuEnable) {
                            loadingDanmu = true;
                            if (lastTime_getdanmu > 0) {
                                lastTime_getdanmu = Math.max(position.toLong(), lastTime_getdanmu);
                            }
                            var attime = if (lastTime_getdanmu > 0) lastTime_getdanmu + 1 else 0
                            getDanmu(attime);
                        }
                    }

                    override fun onSendDanmu(content: String, textcolor: String) {
                        sendDanmu(content, textcolor);
                    }

                    override fun onLongPress() {
                        videoView.setVideoSpeed(3.0f)
                    }

                    override fun onSingleTapUp() {
                        videoView.setVideoSpeed(videoView.GetSpeed(SPUtils.getInstance().getInt(AvVideoController.KEY_SPEED_INDEX, 3)))
                        //  videoView.setVideoSpeed()
                    }

                    override fun isLogin(): Boolean {
                        if (!UserUtils.isLogin()) {
                            HitDialog(this@NewPlayActivity)
                                    .setTitle("提示")
                                    .setOkTitle("登录")
                                    .setMessage("发送弹幕需要登录，是否去登录")
                                    .setOnHitDialogClickListener(object : HitDialog.OnHitDialogClickListener() {
                                        override fun onCancelClick(dialog: HitDialog) {
                                            super.onCancelClick(dialog)
                                        }

                                        override fun onOkClick(dialog: HitDialog) {
                                            super.onOkClick(dialog)
                                            LoginActivity.start();
                                        }
                                    }).show()
                        }
                        return UserUtils.isLogin()
                    }

                }
        )

        videoView.setOnStateChangeListener(object : VideoView.OnStateChangeListener {
            override fun onPlayStateChanged(playState: Int) {
                Log.e("测试", "当前播放状态=${playState}")
                if (playState == VideoView.STATE_PLAYBACK_COMPLETED) {
                    Log.e("测试", "STATE_PLAYBACK_COMPLETED")
                    val percentage = getPercentage(curProgressHistory, vodDuration)
                    println("进度9：=" + controller.percentage + "  2:" + playScoreInfo?.curProgress + " 3=" + curProgressHistory + " 4=" + percentage)
                    if (percentage <= 0.01f || percentage >= 0.99f) {
                        println("进度5：==")
                        playNext()
                    } else {
                        println("进度1：==" + curProgressHistory)
                        controller.setReplayByCurProgress(true)
                    }
                } else if (playState == VideoView.STATE_PREPARED) {
                    prepared()
                } else if (playState == VideoView.STATE_PAUSED) {
                    if (controller.clickivPlay) {
                        //显示暂停广告
                        if (isLandscape) {
                            var stopAd = controller.startPlayStopHAd(Builds().loadStartBean("")?.ads?.player_pause?.description)
                            AdUtils.nativeExpressAd(this@NewPlayActivity, AdConstant.INFO_FLOW_AD, stopAd)
                        } else {

                            //竖屏显示广告
                            stopdH.visibility = View.VISIBLE
                            imgCloseStopAd.visibility = View.VISIBLE
                            //  stopdH.loadDataWithBaseURL(null, Builds().loadStartBean("")?.ads?.player_pause?.description, "text/html", "utf-8", null)
                            AdUtils.nativeExpressAd(this@NewPlayActivity,AdConstant.INFO_FLOW_AD, stopdH)
                            controller.isPlayStopAd(true)
                        }
                        /*if (LoginUtils.checkLogin2(mActivity)) {
                               if (com.ruifenglb.www.download.SPUtils.getBoolean(mActivity, "isVip")) {

                               } else {
                                   //显示暂停广告
                                   if (isLandscape) {
                                      var stopAd =   controller.startPlayStopHAd(Builds().loadStartBean("")?.ads?.player_pause?.description)
                                       App.getApplication().adUtils.showExpressAd(this@NewPlayActivity,stopAd,object: AdListener() {
                                       })
                                   } else {

                                       //竖屏显示广告
                                       stopdH.visibility = View.VISIBLE
                                       imgCloseStopAd.visibility = View.VISIBLE
                                     //  stopdH.loadDataWithBaseURL(null, Builds().loadStartBean("")?.ads?.player_pause?.description, "text/html", "utf-8", null)
                                       App.getApplication().adUtils.showExpressAd(this@NewPlayActivity,stopdH,object: AdListener() {
                                       })
                                       controller.isPlayStopAd(true)
                                   }
                               }
                           } else {
                               //显示暂停广告
                               if (isLandscape) {
                                  var stopAd=  controller.startPlayStopHAd(Builds().loadStartBean("")?.ads?.player_pause?.description)
                                   App.getApplication().adUtils.showExpressAd(this@NewPlayActivity,stopAd,object: AdListener() {
                                   })
                               } else {
                                   //竖屏显示广告
                                   stopdH.visibility = View.VISIBLE
                                   imgCloseStopAd.visibility = View.VISIBLE
                                 //  stopdH.loadDataWithBaseURL(null, Builds().loadStartBean("")?.ads?.player_pause?.description, "text/html", "utf-8", null)
                                   App.getApplication().adUtils.showExpressAd(this@NewPlayActivity,stopdH,object: AdListener() {
                                       })
                                   controller.isPlayStopAd(true)
                               }
                           }*/

                    }

                } else if (playState == VideoView.STATE_PLAYING) {
                    //隐藏暂停广告
                    stopdH.visibility = View.GONE
                    imgCloseStopAd.visibility = View.GONE
                    controller.stopPlayStopHAd()
                } else if (playState == VideoView.STATE_ERROR) {

                    LogUtils.d("=====问题 video OnError")
                    controller.setReplayByCurProgress(true)
                    isSeekToHistory = true
                    curParseIndex++
                    isCanPlayAd2 = false
                    parseData()
                }
            }

            override fun onPlayerStateChanged(playerState: Int) {
                val pausead = Builds().loadStartBean("")?.ads?.player_pause
                //播放暂停广告

                if (playerState == VideoView.PLAYER_NORMAL) {
                    isLandscape = false
                    if (controller.isPlayStopAd()) {
                        stopdH.visibility = View.VISIBLE
                        imgCloseStopAd.visibility = View.VISIBLE
                        // stopdH.loadDataWithBaseURL(null, pausead?.description, "text/html", "utf-8", null)
                        AdUtils.nativeExpressAd(this@NewPlayActivity, AdConstant.INFO_FLOW_AD, stopdH)
                        controller.stopPlayStopHAd()
                        controller.isPlayStopAd(true)
                    }
                } else if (playerState == VideoView.PLAYER_FULL_SCREEN) {
                    isLandscape = true
                    if (controller.isPlayStopAd()) {
                        stopdH.visibility = View.GONE
                        imgCloseStopAd.visibility = View.GONE
                        val stopAd = controller.startPlayStopHAd(pausead?.description)
                        AdUtils.nativeExpressAd(this@NewPlayActivity, AdConstant.INFO_FLOW_AD, stopAd)
                    }
                }
                //播放暂停广告代码结束

            }
        })
    }

    var advData: AdvEntity? = Builds().loadAdvEntity("")
    var advControl: AdControlView? = null
    override fun initData() {

//        RequestManager.execute(this,
//                Retrofit2Utils.INSTANCE.createByGson(VodService::class.java).getAdv(),
//                object : BaseObserver<AdvEntity>(false) {
//                    override fun onSuccess(data: AdvEntity) {
//                        advData = data
//                    }
//                    override fun onError(e: ResponseException) {
//
//                    }
//
//
//                }
//        )
        super.initData()
        getVideoDetail()

    }
    //上次获取弹幕最后一条弹幕时间
    private var lastTime_getdanmu: Long = 0;
    //是否正在获取弹幕
    var loadingDanmu: Boolean = false;
    private fun getDanmu(atTime: Long) {
        val vodService =
                Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            loadingDanmu = false;
            return;
        }
        RequestManager.execute(this,
                vodService.getDanMuList(
                        mVodBean.vod_id,
                        100,
                        0,
                        atTime
                ),
                object : BaseObserver<DanMuBean>(false) {
                    override fun onSuccess(data: DanMuBean) {
                        loadingDanmu = false;

                        if (data != null && data.list != null && !data.list.isEmpty()) {
                            for (danmuBean in data.list) {
                                videoView.addDanmaku(
                                        danmuBean.content,
                                        danmuBean.color,
                                        danmuBean.at_time - videoView.currentPosition
                                )
                            }
                            lastTime_getdanmu = data.list.get(data.list.size - 1).at_time;
                        } else {
                            //没有弹幕就延时30秒获取
                            lastTime_getdanmu += 30000;
                        }
                    }

                    override fun onError(e: ResponseException) {
                        Log.e("test", "onError " + e.message)
                        loadingDanmu = false;
                    }
                })


    }

    /**
     * 视频准备完成
     */
    private fun prepared() {
        isParseSuccess = true
        vodDuration = controller.duration
        if (isShowPlayProgress) {
//                        Log.i("dsd", "iko===${App.curPlayScoreBean?.curProgress ?: 0}")
            if (vodDuration > playScoreInfo?.curProgress ?: 0) {
                videoView.seekTo(playScoreInfo?.curProgress ?: 0)

            }
            println("进度3：==" + playScoreInfo?.curProgress)
            //从播放记录中点击播放的时候，需要重新插入输入库
//                        playScoreInfo?.let {
//                            LitePal.deleteAll(PlayScoreBean::class.java, "vodId = ?", "${it.vodId}")
//                            playScoreInfo = null
//                        }
            isShowPlayProgress = false
        } else {
            if (isSeekToHistory) {
                videoView.seekTo(curProgressHistory)
                println("进度2：==" + curProgressHistory)
//                            isSeekToHistory = false
            } else {
                //跳过30秒片头
                val wei = CastUtils.getIntTime(Builds().loadPlayTiaoZhuan2(mVodBean.vod_id))
                val tou = CastUtils.getIntTime(Builds().loadPlayTiaoZhuan(mVodBean.vod_id))
                if (videoNetProgress == 0L) {
                    if (vodDuration > tou && tou != 0L && tou + wei < vodDuration) {
                        Toast.makeText(this@NewPlayActivity, "自动跳过片头。", Toast.LENGTH_SHORT).show()
                        videoView.seekTo(tou)
                    }

                } else {
                    if (vodDuration > videoNetProgress) {
                        videoView.seekTo(videoNetProgress)
                    }

                }
                println("进度4：== videoNetProgress=" + videoNetProgress)
            }
        }


        println("进度12：==" + vodDuration)
        when (SPUtils.getInstance().getInt(AvVideoController.KEY_SPEED_INDEX, 3)) {
            0 -> {
                videoView.setSpeed(2f)
                controller.setSpeed("2.00")
            }
            1 -> {
                videoView.setSpeed(1.5f)
                controller.setSpeed("1.50")
            }
            2 -> {
                videoView.setSpeed(1.25f)
                controller.setSpeed("1.25")
            }
            3 -> {
                videoView.setSpeed(1f)
                controller.setSpeed("1.00")
            }
            4 -> {
                videoView.setSpeed(0.75f)
                controller.setSpeed("0.75")
            }
            5 -> {
                videoView.setSpeed(0.5f)
                controller.setSpeed("0.50")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        videoView.resume()
        if (isParsed) {
            checkVodTrySee()
        }
    }

    override fun onResume() {
        super.onResume()
        checkVodTrySee()
        pipManager?.resume()
    }

    override fun onPause() {
        pipManager?.pause()
        super.onPause()

    }

//    override fun onStop() {
//        super.onStop()
//        videoView.pause()
//    }

    override fun onDestroy() {
        super.onDestroy()
        JieXiUtils2.INSTANCE.stopGet()
        if (pipManager != null && !pipManager?.isStartFloatWindow!!) {
            videoView.clearOnStateChangeListeners()
            videoView.release()
        }
        pipManager?.reset()
        controller.onDestroy()
        timerCount?.cancel()
        lbm?.unregisterReceiver(localReceiver)
        cancelTimer()


        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (AvVideoController.pendingIntent != null) alarmManager.cancel(AvVideoController.pendingIntent)
    }

    override fun onBackPressedSupport() {
        if (pipManager != null && pipManager?.onBackPress()!!) {
            return
        }
        if (!videoView.onBackPressed()) {
            try {
                recordPlay()//播放记录可能为空
            } catch (e: Exception) {
            } finally {
                super.onBackPressedSupport()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            controller.clickivPlay = false
            if (controller.videoState) {

                Builds().savePlayScore(null)
                playScoreInfo = null
                savePlayRecord(true)
                setResult(3)
                controller.huaTest()
                finish()
            } else {
                controller.huaTest()
            }
            false
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    fun getPipMsgBean(): PipMsgBean {
        val voidid = mVodBean.vod_id.toString()
        val vodSelectedWorks = this@NewPlayActivity.playList!![urlIndex].name
        var playSource = ""
        if (mVodBean.vod_play_list != null) {
            val playFromBean = mVodBean.vod_play_list!!.get(playSourceIndex)
            val playerInfo = playFromBean.player_info
            val urls = playFromBean.urls
            playSource = playerInfo.show
        }
        if (StringUtils.isEmpty(playSource)) {
            playSource = "默认"
        }
        var percentage = controller.percentage.toString()
        if (percentage == "NaN") {
            percentage = "0.0"
        }
        val urlIndex = this@NewPlayActivity.urlIndex

        var pipMsgBean = PipMsgBean()
        pipMsgBean.voidid = voidid
        pipMsgBean.vodSelectedWorks = vodSelectedWorks
        pipMsgBean.playSource = playSource
        pipMsgBean.percentage = percentage
        pipMsgBean.urlIndex = urlIndex
        pipMsgBean.curPregress = curProgressHistory
        pipMsgBean.playSourceIndex = playSourceIndex
        return pipMsgBean
    }

    fun showSummary() {
        if (summaryFragment == null) {
            summaryFragment = SummaryFragment.newInstance(mVodBean)
            supportFragmentManager.beginTransaction()
                    .add(R.id.flContainer, summaryFragment!!)
                    .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                    .show(summaryFragment!!)
                    .commitAllowingStateLoss()
        }
    }

    fun hideSummary() {
        supportFragmentManager.beginTransaction()
                .hide(summaryFragment!!)
                .commitAllowingStateLoss()
    }

    fun showVideoDetail() {
        if (videoDetailFragment == null) {
            videoDetailFragment = VideoDetailFragment.newInstance(mVodBean, urlIndex, playSourceIndex)
            supportFragmentManager.beginTransaction()
                    .add(R.id.flContainer, videoDetailFragment!!)
                    .commitNowAllowingStateLoss()
        } else {
            videoDetailFragment?.changeCurIndex(urlIndex)
            supportFragmentManager.beginTransaction()
                    .show(videoDetailFragment!!)
                    .commitNowAllowingStateLoss()
        }
    }

    fun showPlayList() {
        if (playListFragment == null) {
            val spanCount = if (mVodBean.type_id == 3) {
                2
            } else {
                5
            }
            playListFragment = PlayListFragment.newInstance(spanCount).apply {
                if (playList != null) {
                    showPlayList(playList!!, urlIndex)
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.flContainer, playListFragment!!)
                    .commitAllowingStateLoss()
        } else {
            playListFragment?.run {
                if (playList != null) {
                    showPlayList(playList!!, urlIndex)
                }
            }
            supportFragmentManager.beginTransaction()
                    .show(playListFragment!!)
                    .commitAllowingStateLoss()
        }

    }


    fun hidePlayList() {
        supportFragmentManager.beginTransaction()
                .hide(playListFragment!!)
                .commitAllowingStateLoss()
    }

    fun showNewVideo(vodBean: VodBean) {
        savePlayRecord(false)
        curProgressHistory = 0
        videoNetProgress = 0
//        isSeekToHistory = false
        recordPlay()//缓存上一个视频的进度
        Builds().savePlayScore(null)
        playScoreInfo = null
        mVodBean = vodBean
        supportFragmentManager.beginTransaction()
                .remove(videoDetailFragment!!)
                .commitNowAllowingStateLoss()
        videoDetailFragment = null

        if (summaryFragment != null) {
            supportFragmentManager.beginTransaction()
                    .remove(summaryFragment!!)
                    .commitNowAllowingStateLoss()
            summaryFragment = null

        }

        if (playListFragment != null) {
            supportFragmentManager.beginTransaction()
                    .remove(playListFragment!!)
                    .commitNowAllowingStateLoss()
            playListFragment = null
        }

        videoView.release()
        controller.setTitle(mVodBean.vodName)
        getVideoDetail()
    }

    fun changeSelection(position: Int, isNeedOrder: Boolean) {
        urlIndex = position//更改当前选集
        curProgressHistory = 0
        videoNetProgress = 0
        isSeekToHistory = false
        curFailIndex = -1
        this.curParseIndex = 0
//        Collections.reverse(playList)
//        if (isNeedOrder)
//            playList= playList?.reversed()as List<UrlBean>



        LogUtils.d("=====问题 changeSelection")
        parseData()

        val ad = MMkvUtils.Companion.Builds().loadStartBean("")?.ads?.player_down

        if (ad == null || ad.status == 0 || ad.description.isNullOrEmpty()) {
            // awvPlayerDown.visibility = View.GONE
        } else {
            /* awvPlayerDown.visibility = View.VISIBLE
             awvPlayerDown.loadHtmlBody(ad.description)*/
            if (MatchUtil.checkInteractionAd(this, ad.description)) {
                AdUtils.interstitialAd(mActivity, AdConstant.INTERSTITIAL_SCREEN_AD,object :AdListener{
                    override fun onShow() {

                    }

                    override fun onClose() {

                    }

                })
            }

        }
    }

    fun changeVideoUrlIndex(position: Int = -1) {

        videoDetailFragment?.changeCurIndex(urlIndex)
        this.curParseIndex = 0
        curFailIndex = -1
    }

    fun changePlaySource(playFromBean: PlayFromBean, playSourceIndex: Int) {

        this.playFrom = playFromBean
        this.playList = playFrom.urls


        if (urlIndex > playFrom.urls.count() - 1) {
            urlIndex = playFrom.urls.count() - 1
            changeVideoUrlIndex(playFrom.urls.count())
        }
        this.playSourceIndex = playSourceIndex
        this.curParseIndex = 0
        curFailIndex = -1
        LogUtils.d("=====问题 changePlaySource")
        parseData()
        videoDetailFragment?.changePlaysource(playSourceIndex)
    }

    fun changePlaySource(playFromBean: PlayFromBean) {

        this.playFrom = playFromBean
        this.playList = playFrom.urls

        this.playSourceIndex++
        if (this.playSourceIndex > playFormList.count() - 1) {
            Toast.makeText(this, "没有其他播放源了.", Toast.LENGTH_SHORT).show()
            return
        }
        if (urlIndex > playFrom.urls.count() - 1) {
            Toast.makeText(this, "该源没有当前集数，自动切换下一个播放源.", Toast.LENGTH_SHORT).show()
            changePlaySource(playFrom)
            return
        }
        this.curParseIndex = 0
        curFailIndex = -1
        LogUtils.d("=====问题 changePlaySource")
        parseData()
        videoDetailFragment?.changePlaysource(playSourceIndex)
    }

    fun castScreen(device: CastDevice) {
        if (isParseSuccess && curPlayUrl.isNotEmpty()) {
            val vodUrl = if (curPlayUrl.startsWith("//")) {
                "https:$curPlayUrl"
            } else {
                curPlayUrl
            }
            NLUpnpCastManager.getInstance().connect(device);
            Intent(this, CastScreenActivity2::class.java).apply {
                putExtra("vod", mVodBean)
                putExtra("playSourceIndex", playSourceIndex)
                putExtra("urlIndex", urlIndex)
                putExtra("vodurl", vodUrl)
                putExtra("vodLong", controller.duration)
//                putExtra("device", device )
                println("vodurl+=${vodUrl}")
                val newPlayFromList = ArrayList<PlayFromBean>()
                playFormList.map {
                    newPlayFromList.add(it)
                }
                putParcelableArrayListExtra("playFormList", newPlayFromList)
                ActivityUtils.startActivity(this)
            }
        } else {
            runOnUiThread {
                ToastUtils.showShort("正在解析中...")
            }
        }
    }

    private fun changeTitle() {
        var title = mVodBean.vod_name
        if (mVodBean.type_id == 2) {
            if (playList != null) {
                title += " ${playList!![urlIndex].name}"
            }
        }
        controller.setTitle(title)
    }

    private fun chengeNextLine() {

        curParseIndex++
        parseData()
    }

    private fun chengeNextLineFromHead() {
        isCanPlayAd2 = false
        curParseIndex = 0
        curFailIndex = -1
        LogUtils.d("=====问题 chengeNextLineFromHead")
        parseData()
    }

    private fun showSpeedListDialog(pos: Int) {
        SpeedListDialog(mActivity, this, pos).show()
    }

    private fun showPlayListDialog() {
        if (playList != null) {
            PlayListDialog(mActivity, urlIndex, playList!!)
                    .show()
        }
    }

    private fun showPlaySourceDialog() {
        PlaySourceDialog(mActivity, playSourceIndex, playFormList)
                .show()
    }
//投屏提示
    private fun showCastScreenDialog() {

//            val dialogHeight = if (isLandscape) {
//                -1
//            } else {
//                videoView.height
//            }
//            CastScreenDialog(mActivity, dialogHeight)
//                    .show()
//            val url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"
            val danListPop = DlanListPop(this@NewPlayActivity) {

                castScreen(it)
            }
            XPopup.Builder(this@NewPlayActivity)
                    .asCustom(danListPop)
                    .show()

    }

    private fun play_img_url() {


        val play_gg = Builds().loadStartBean("")?.ads?.play_gg
        if (play_gg != null) {
            gg=play_gg.status
        }
        controller.set_v_av_top_bg(gg);

        val logoimg = Builds().loadStartBean("")?.ads?.play_logo
        val reward = Builds().loadStartBean("")?.ads?.number_of_awards?.description
        var csjvideoadv = Builds().loadStartBean("")?.ads?.csj_video_adv?.status
        if (csjvideoadv == null) {
            csjvideoadv = 0
        }
        if (logoimg == null || logoimg.status == 0 || logoimg.description.isNullOrEmpty()) {
            controller.visibility_wqddg(null, reward, csjvideoadv)
        } else {
            Log.d("播放器LOGO", "initView: " + logoimg.description) //能获取到图片地址
            controller.visibility_wqddg(logoimg.description, reward, csjvideoadv)
        }
    }

    private fun getVideoDetail() {
        play_img_url()
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)

        if (AgainstCheatUtil.showWarn(vodService)) {


        }



        RequestManager.execute(this, vodService.getVideoDetail(mVodBean.vod_id, 10),
                object : PlayLoadingObserver<VodBean>(mActivity) {
                    override fun onSuccess(data: VodBean) {
                        mVodBean = data
                        curParseIndex = 0
                        curFailIndex = -1
                        // playScoreInfo = LitePal.where("vodId = ?", data.vod_id.toString()).findFirst(PlayScoreBean::class.java)
                        playScoreInfo = Builds().loadPlayScore("")

                        urlIndex = playScoreInfo?.urlIndex ?: 0//获取播放记录中的选集

                        playSourceIndex = playScoreInfo?.playSourceIndex ?: 0//获取播放源

                        val playInfo = data.playInfo
                        pipManager = PIPManager.getInstance()
                        if (mIsShowing!!) {

//                            if (playInfo != null) {
//                                playSourceIndex = pipManager?.pipMsg?.playSourceIndex ?: 0//获取播放源
//                                urlIndex = pipManager?.pipMsg?.urlIndex ?: 0//获取播放记录中的选集
//                                videoNetProgress = playInfo.curProgress
//                                playInfo.nid = pipManager?.pipMsg?.vodSelectedWorks ?: ""
////                                playInfo.curProgress = pipManager?.pipMsg?.curPregress ?: 0
//                            } else {
                            urlIndex = pipManager?.pipMsg?.urlIndex ?: 0//获取播放记录中的选集
                            playSourceIndex = pipManager?.pipMsg?.playSourceIndex ?: 0//获取播放源
                            videoNetProgress = pipManager?.pipMsg?.curPregress ?: 0
                            // }
                            curProgressHistory = videoNetProgress
                        } else {
                            if (playInfo != null) {
                                playSourceIndex = playInfo.playSourceIndex
                                urlIndex = playInfo.urlIndex
                                videoNetProgress = playInfo.curProgress
                                curProgressHistory = videoNetProgress
                            }
                        }


//                        runOnUiThread {
//                        ToastUtils.showShort("进度：playInfo=" + playInfo + "  videoNetProgress=" + videoNetProgress)
//                        }

                        playFormList = data.vod_play_list
                        if (playSourceIndex + 1 > playFormList.count()) {
                            playSourceIndex = 0
                        }
                        if (data.vod_play_list.isNullOrEmpty() || data.vod_play_url.isEmpty() || data.getVod_play_from().equals("no")) {

                            HitDialog(this@NewPlayActivity)
                                    .setTitle(StringUtils.getString(R.string.tip))
                                    .setMessage("无播放地址，联系客服添加")
                                    .setOnHitDialogClickListener(object : HitDialog.OnHitDialogClickListener() {
                                        override fun onCancelClick(dialog: HitDialog) {
                                            super.onCancelClick(dialog)
                                            finish()
                                        }

                                        override fun onOkClick(dialog: HitDialog) {
                                            super.onOkClick(dialog)
                                            finish()
                                        }
                                    })
                                    .show()
                            return
                        }
                        if (data.vod_play_list != null) {
                            playFrom = data.vod_play_list!![playSourceIndex]
                        }
                        playList = playFrom.urls
                        LogUtils.d("=====问题 getVideoDetail")
                        if (!mIsShowing!!) {
                            mIsShowing = false
                            parseData()
                        } else {
                            isParseSuccess = true
                            isPlay = true
                        }
                        showVideoDetail()
                    }

                    override fun onError(e: ResponseException) {
                        finish()
                    }

                })

    }

    //开始播放广告https://tb-video.bdstatic.com/tieba-smallvideo-transcode-crf/3025116_d787eccb7230052923f1b28e948b5792_0.mp4?vt=0&pt=3&cr=2&cd=0&sid=&ft=8&tbau=2020-12-24_5fe5ef20e481c5cedbcd00b246f98dcd496c89ae0260f790467e258f0088dd2a
    private fun getAdv(url: String) {
        if (timerCount != null) {
            timerCount?.cancel()
            if (controller.videoState) {
                findViewById<ConstraintLayout>(R.id.layoutAdv).visibility = View.INVISIBLE
            } else {
                controller.stopPayHAdv()
            }
            timerCount = null
        }

        releaseAdVideo()

        //如果是会员直接播放视频,或者是不需要播放广告(已经播放过了)
        if (!isCanPlayAd2) {
            play2(url)
            return
        }
        if (LoginUtils.checkLogin2(mActivity)) {
            if (com.ruifenglb.www.download.SPUtils.getBoolean(mActivity, "isVip")) {
                //开始播放视频
                play2(url)
                return
            }
        }
        val code = advData?.html?.code




        if (advData?.ad_select.isNullOrEmpty() || advData?.ad_select.isNullOrBlank()) {
            play2(url)
            return
        }

        //暂时写死为视频广告
        //App.mogai_cfg.ad_select = "video"

        if (advData?.ad_select == "html") {

            val timeout = (advData?.html?.timeout)
            if (timeout?.equals(0)!!) {
                play2(url)
                return
            }

            if (controller.videoState) {
                layoutAdv.visibility = View.VISIBLE
                //webviewV.loadDataWithBaseURL(null, code, "text/html", "utf-8", null)
                AdUtils.nativeExpressAd(this@NewPlayActivity, AdConstant.INFO_FLOW_AD, webviewV)
            } else {
                controller.startPlayHAdv(code)
            }

            timerCount = MyCount(mActivity, (timeout * 1000).toLong(), 1000L, object : MyCount.timerFinsihLisenter {
                override fun onFinish() {
//                            倒计时结束，开始播放视频
                    if (controller.videoState) {
                        findViewById<ConstraintLayout>(R.id.layoutAdv).visibility = View.INVISIBLE
                    } else {
                        controller.stopPayHAdv()
                    }
                    play2(url)
                }

                override fun onTicks(mill: Long) {
                    if (controller.videoState) {
                        ad_timeV.text = "${mill / 1000}s | 免费升级vip关闭此广告"
                    } else {
                        controller.setAdvTime(mill)
                    }

                }
            })
            timerCount?.start()
        } else if (advData?.ad_select == "video") {
            val click_urls = advData?.video?.click_url?.split("|")?.toTypedArray()
            val vodurls = advData?.video?.url?.split("|")?.toTypedArray()
            if (click_urls?.size == 0 || vodurls?.size == 0 || !vodurls?.size?.equals(click_urls?.size)!!) {
                play2(url)
                return
            }

            val ra = (0..(click_urls?.size!! - 1 ?: 0)).random()

            advControl = AdControlView(mActivity)
            advControl?.setListener(object : AdControlView.AdControlListener {
                override fun onAdClick() {
                    var intent = Intent()
                    intent.setAction(Intent.ACTION_VIEW)
                    //跳转到外部链接的测试地址
                    //intent.setData(Uri.parse("https://www.baidu.com/"))
                    intent.setData(Uri.parse(click_urls?.get(ra) ?: ""))
                    if (intent.resolveActivity(packageManager) != null) {
                        var componentName = intent.resolveActivity(packageManager)
                        Log.e("componentName=", "=================" + componentName)
                        startActivity(Intent.createChooser(intent, "请选择浏览器"))
                    } else {
                        ToastUtils.showShort("未安装浏览器")
                    }
                }

                override fun onSkipAd() {
                    //点击跳过
                }

            })
            runOnUiThread {
                //正式数据替换这个视频广告链接
                val testVideoUrl = vodurls?.get(ra)
                        ?: ""//"https://tb-video.bdstatic.com/tieba-smallvideo-transcode-crf/3025116_d787eccb7230052923f1b28e948b5792_0.mp4?vt=0&pt=3&cr=2&cd=0&sid=&ft=8&tbau=2020-12-24_5fe5ef20e481c5cedbcd00b246f98dcd496c89ae0260f790467e258f0088dd2a";
                videoView.setUrl(testVideoUrl)
                if (isSuccess) {
                    videoView.setOnStateChangeListener(object : VideoView.OnStateChangeListener {
                        override fun onPlayStateChanged(playState: Int) {
                            if (playState == VideoView.STATE_PLAYBACK_COMPLETED) {
                                Log.e("播放广告", "结束了")
//                                controller.removeControlComponent(advControl)
//                                videoView.release()
//                                controller.setAllView(true)
                                releaseAdVideo()
                                play2(url)
                            } else if (playState == VideoView.STATE_ERROR) {
//                                controller.removeControlComponent(advControl)
//                                videoView.release()
//                                controller.setAllView(true)
                                releaseAdVideo()
                                play2(url)
                            }

                        }

                        override fun onPlayerStateChanged(playerState: Int) {
                        }

                    })
                    controller.startPlayVideoAdv()
                    controller.setAllView(false)
                    controller.addControlComponent(advControl)
                    videoView.setVideoController(controller)

                    VideoViewManager.instance().setPlayOnMobileNetwork(true)

                    if (!controller.isShowHint) {
                        videoView.start()
                        controller.startPlay()
                    }
                }
            }
        }


    }

    /**
     * 释放视频广告
     */
    private fun releaseAdVideo() {
        if (advData?.ad_select == "video" && advControl != null) {
            controller.removeControlComponent(advControl)
            videoView.release()
            controller.setAllView(true)
        }
    }

    private fun play2(url: String) {
        isCanPlayAd2 = true
        loadingDanmu = false
        lastTime_getdanmu = 0
        val head = playFrom.player_info.headers ?: ""
        val issetheadstr = playFrom.player_info.issethead ?: ""

        val issetheadlist = issetheadstr.split("||")
        val heads = head.split("||")
        val headers: MutableMap<String, String>? = HashMap()
        var isSetHead = false
        issetheadlist.forEach {
            if (url.matches(it.toRegex())) {
                isSetHead = true
                return@forEach
            }

        }
        if (isSetHead) {
            heads.forEach {
                val kv = it.split("=>")
                if (kv.size == 2) {
                    println(kv.get(0) + "1234567321" + kv.get(1))
                    headers?.put(kv.get(0), kv.get(1))
                }

            }
        }
//        headers?.put("referer"," https://www.mgtv.com/")
//        headers?.put("Accept-Encoding"," gzip, deflate")
//        headers?.put("Accept"," */*")
//        headers?.put("Connection"," keep-alive")
//        headers?.put("user-agent"," Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36")
        var url = url
        println(url + "1234567321")
        if (url.startsWith("//")) {
            url = "https:$url"
        }

        if (playFrom.player_info.kernel ?: "0" != "0") {
            videoView = MyPlayerManager.checktFactory(playFrom.player_info.kernel ?: "1", videoView)
        }

        videoView.setUrl(url, headers)
        videoView.clearDisappearingChildren();

        controller.setTitle(mVodBean.vodName + "   " + (playList?.get(urlIndex)?.name ?: ""))

        VideoViewManager.instance().setPlayOnMobileNetwork(true)


        if (!controller.isShowHint) {
                videoView.start()
                controller.startPlay()

        }


    }


    private fun checkVodTrySee() {
        if (playList == null) {
            return
        }
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(
                mActivity,
                vodService.checkVodTrySee(mVodBean.vod_id.toString(), 1.toString(), playList!![urlIndex].nid.toString()),

                object : BaseObserver<CheckVodTrySeeBean>() {
                    override fun onSuccess(data: CheckVodTrySeeBean) {

                        var isVip = false
                        if (UserUtils.isLogin() && UserUtils.userInfo?.group_id == 3) {
                            isVip = true
                        }
                        val status = data.status
                        isAllowCastScreen = status == 0 || (isVip && status == 1)
                        controller.CheckVodTrySeeBean(data.user_video, data, isVip, mVodBean.vod_points_play)
                    }

                    override fun onError(e: ResponseException) {
                        isAllowCastScreen = false
                    }
                })
    }

    private fun parseData() {


        IsSwitchkernel = false
        checekPlayVip()

        LogUtils.d("=====问题 parseData")
        if (isPlay) {
            videoView.release()
        }
        isParseSuccess = false
        isPlay = false
        // showPlayerAd()
        showAnnouncement()
        // 开始解析地址
        val features = playFrom.player_info.features?.split(",")
        val parse = playFrom.player_info.parse2
        var url: String = ""
        if (playList != null) {
            url = playList!![urlIndex].url
        }
        LogUtils.d("", "====Parse start url=" + url + "  parse=" + parse)
        checkVodTrySee()
        changeTitle()
        var isStraightChain = false
        features?.forEach {

            if (url.matches(it.toRegex())) {
                isStraightChain = true
                return@forEach
            }
        }
//        if (url.endsWith(".mp4") || url.endsWith(".m3u8") || url.contains(".aliyuncs.com") || url.contains("4kyasvip-oss-alivideo")) {
////            getSameActorData(url)
//            isPlay=true
//            curPlayUrl = url
//            play(url)
//
//        } else {
        if (isStraightChain) {
            isPlay = true
            curPlayUrl = url
            play(url)
        } else {
            isSuccess = false
            controller.showJiexi()
            LogUtils.d("", "====Parse start url=" + "正常进这？？")
            JieXiUtils2.INSTANCE.getPlayUrl(parse, url, curParseIndex, onJiexiResultListener, curFailIndex)
        }

    }

    private fun checekPlayVip(): Boolean {
        if (mVodBean.type.type_name.contains(regex = "[Vv][Ii][Pp]".toRegex())) {

            if (com.ruifenglb.www.download.SPUtils.getBoolean(this@NewPlayActivity, "isVip")) {
                controller.isShowVipHint(false)
                return true
            } else {
                controller.isShowVipHint(true)
                return false
            }
        }
        return false
    }

    private fun showPlayerAd() {
        MMkvUtils.Builds().loadAppConfigAd("")?.let {
            if (it.img.isNotEmpty()) {
                controller.showAd(it.img, it.url)
            }
        }
    }

    private fun showAnnouncement() {
        runOnUiThread {
            MMkvUtils.Builds().loadStartBean("")?.document?.roll_notice?.let {
                if (it.content.isNotEmpty() && it.status == "1") {
                    controller.showAnnouncement(it.content)
                }
            }
        }
    }

    private var isSuccess = false
    private fun play(url: String) {

//        var url = url

//        val url="https://vip.fwwmy1.cn/api/data/youku/62ba0f501791adb51d22b699566bda7b.m3u8"
//        val url="https://vip.fwwmy1.cn/zhilian.php?url=https://vip.fwwmy1.cn/api/data/youku/62ba0f501791adb51d22b699566bda7b.m3u8"
        //错误
//        https://v5.monidai.com/20200905/ZWb6ezWP/index.m3u8
//        https://v5.szjal.cn/20200905/ZWb6ezWP/index.m3u8

        LogUtils.d("---play----测试11111111111111")
        if (isPlay) {
            videoView.release()
        }
        isSuccess = true
        startTimer()
        controller.hideJiexi()
        LogUtils.d("", "====Parse play url=" + url)
        println("====Parse play url=" + url)
        // getSameActorData(url)
        videoView.post {
            getAdv(url)
            //------------------
//            videoView.start()
//            VideoViewManager.instance().setPlayOnMobileNetwork(true)
//
//            controller.startPlay()
            //-----------------

//            controller.setCurProgress(playScoreInfo?.percentage ?: 0f)
        }
    }


    //将当前记录缓存下来
    private fun recordPlay() {
        if (playScoreInfo == null) {
            playScoreInfo = PlayScoreBean().apply {
                vodId = mVodBean.vod_id
                typeId = mVodBean.type_id
                vodName = mVodBean.vod_name
                vodImgUrl = mVodBean.vod_pic
                percentage = controller.percentage
                curProgress = controller.curProgress
                playSourceIndex = this@NewPlayActivity.playSourceIndex
                if (playList != null) {
                    urlIndex = this@NewPlayActivity.urlIndex
                    vodSelectedWorks = this@NewPlayActivity.playList!![urlIndex].name
                }
                save()
            }
        } else {
            playScoreInfo?.run {
                percentage = controller.percentage
                curProgress = controller.curProgress
                playSourceIndex = this@NewPlayActivity.playSourceIndex
                if (playList != null) {
                    urlIndex = this@NewPlayActivity.urlIndex
                    vodSelectedWorks = this@NewPlayActivity.playList!![urlIndex].name
                }
                saveOrUpdate("vodId = ?", mVodBean.vod_id.toString())
            }
        }
    }


    private fun savePlayRecord(isClose: Boolean) {


        // var percentage = controller.percentage
        val curProgress = controller.curProgress
        if (curProgress == 0L) {
            if (isClose) {
                finish()
            }
            return
        }
//        runOnUiThread {
//        ToastUtils.showShort("保存进度：" + curProgress)
//        }
        if (curProgress != 0L) {
            curProgressHistory = curProgress
        }

        val voidid = mVodBean.vod_id.toString()
        Log.e(TAG, "======voidid===$voidid")

        var percentage = controller.percentage.toString()
        if (percentage == "NaN") {
            percentage = "0.0"
        }

        println("进度 ---savePlayRecord---  curProgress=" + curProgress)

        if (this@NewPlayActivity.playList.isNullOrEmpty() && isClose) {
            finish()
            return
        }

        val urlIndex = this@NewPlayActivity.urlIndex
        val vodSelectedWorks = this@NewPlayActivity.playList!![urlIndex].name


        var playSource = ""
        if (mVodBean.vod_play_list != null) {
            val playFromBean = mVodBean.vod_play_list!!.get(playSourceIndex)
            val playerInfo = playFromBean.player_info
            val urls = playFromBean.urls
            playSource = playerInfo.show
        }
        if (StringUtils.isEmpty(playSource)) {
            playSource = "默认"
        }


        if (UserUtils.isLogin()) {
            val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
            if (AgainstCheatUtil.showWarn(vodService)) {
                return;
            }
            Log.d(TAG, "voidid=${voidid}  vodSelectedWorks=${vodSelectedWorks}  playSource=${playSource}  percentage=${percentage} curProgress=${curProgress}")
            var urlIndex_temp = urlIndex
            if (!videoDetailFragment?.isOrder!!) {
                urlIndex_temp = playList!!.size - 1 - urlIndex
            }
            RequestManager.execute(this, vodService.addPlayLog(voidid, vodSelectedWorks, playSource.toString(), percentage, urlIndex_temp.toString(), curProgress.toString(), playSourceIndex.toString()),
                    object : BaseObserver<UserVideo>() {
                        override fun onSuccess(data: UserVideo) {
                            Log.i("play", "addPlayLogsucess")
                            val intent = Intent("android.intent.action.AddPlayScore")
                            sendBroadcast(intent)
                            if (isClose) {
                                finish()
                            }
                        }

                        override fun onError(e: ResponseException) {
                            Log.i("play", "addPlayfaied")
                            if (isClose) {
                                finish()
                            }
                        }
                    })
            println("watchVideoLong==$watchVideoLong")
            if (watchVideoLong != 0) {
                RequestManager.execute(this, vodService.addWatchTime(watchVideoLong),
                        object : BaseObserver<GetScoreBean>() {
                            override fun onSuccess(data: GetScoreBean) {
                                if (data.score != "0") {
                                    runOnUiThread {
                                        ToastUtils.showShort("已观影30分钟，获得${data.score}积分")
                                    }
                                }
                            }

                            override fun onError(e: ResponseException) {
                                println("watchVideoLong==  onError")
                            }
                        })
            }
        } else {
            if (isClose) {
                finish()
            }
        }


//        RequestManager.execute(
//                mActivity,
//                vodService.shareScore(),
//                object : BaseObserver<ShareBean>() {
//                    override fun onSuccess(data: ShareBean) {
//        runOnUiThread {
//                        ToastUtils.showShort(data.info)
//        }
//                        EventBus.getDefault().post(LoginBean())
//                    }
//
//                    override fun onError(e: ResponseException) {
//                    }
//                }
//        )

    }

    //播放下一集
    private fun playNext() {
        curProgressHistory = 0
        isSeekToHistory = false
        videoNetProgress = 0
        if (++urlIndex >= playFrom.urls.size) {
            urlIndex = 0
        }
//        if (videoDetailFragment?.isOrder!!){
//            Collections.reverse(playList)
//        }
//        //顺序
//        if (videoDetailFragment?.isOrder!!) {
//            if (++urlIndex >= playFrom.urls.size) {
//                urlIndex = 0
//            }
//            //倒序播放
//        } else {
//            if (urlIndex == 0) {
//                urlIndex = playFrom.urls.size - 1
//            } else {
//                --urlIndex
//            }
//        }

        changeVideoUrlIndex()
        parseData()


    }

    //升级vip
    private fun updateVip() {
        /*if (!UserUtils.isLogin()) {
            LoginActivity.start()
        } else {
            if (UserUtils.userInfo?.group_id != 3) {
                val intent = Intent(mActivity, PayActivity::class.java)
                intent.putExtra("type", 1)
                ActivityUtils.startActivity(intent)
            } else {
                checkVodTrySee()
            }
        }*/
        if(!isLogin()){
            ToastUtils.showShort("请先登录")
            LoginActivity.start()
        }else{
            AdUtils.rewardVideo(this@NewPlayActivity, AdConstant.INCENTIVE_AD,object :AdListener{
                override fun onShow() {
                    adGroup()
                }

                override fun onClose() {
                    if (UserUtils.userInfo?.group_id != 3) {
                        val intent = Intent(mActivity, PayActivity::class.java)
                        intent.putExtra("type", 1)
                        ActivityUtils.startActivity(intent)
                    } else {
                        checkVodTrySee()
                    }
                }
            })

        }
    }

    //付费观看
    private fun payPlay() {
        if (!UserUtils.isLogin()) {
            LoginActivity.start()
        } else {
            if (playList == null) {
                return
            }
            val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
            if (AgainstCheatUtil.showWarn(vodService)) {
                return;
            }
            RequestManager.execute(
                    mActivity,
                    vodService.buyVideo(4.toString(), mVodBean.vod_id.toString(), playFrom.sid.toString(), playList!![urlIndex].nid.toString(), 1.toString(), 1.toString()),
                    object : BaseObserver<String>() {
                        override fun onSuccess(data: String) {

                            runOnUiThread {
                                ToastUtils.showShort("购买成功！")
                            }
                            checkVodTrySee()
                        }

                        override fun onError(e: ResponseException) {

                        }

                    }
            )
        }
    }

    private var lbm: LocalBroadcastManager? = null
    private val localReceiver = LocalReceiver(this@NewPlayActivity)


    private fun registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(this@NewPlayActivity)
        lbm!!.registerReceiver(localReceiver, IntentFilter("cn.whiner.newAv.AvVideoController"))//av
    }

    class LocalReceiver(act: NewPlayActivity) : BroadcastReceiver() {

        private var act = act

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == "cn.whiner.newAv.AvVideoController") {//av
                when (intent.getIntExtra("type", RECEIVER_TYPE_TIMER)) {
                    RECEIVER_TYPE_REPLAY -> {
                        act.isSeekToHistory = true
                    }
                    RECEIVER_TYPE_TIMER -> {
                        val isFromHead = intent.getBooleanExtra("isFromHead", false)
                        if (isFromHead) {
                            act.chengeNextLineFromHead()
                        } else {
                            act.chengeNextLine()
                        }
                    }
                }
            }
        }
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var watchVideoLong = 0
    private var index = 0

    private fun startTimer() {
        if (timer == null && timerTask == null) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    if (index == 0) {
                        index++
                    } else {
                        savePlayRecord(false)
                        watchVideoLong += 60
                    }
                }
            }
            timer!!.schedule(timerTask, 0, 1000 * 30)
        }
    }

    private fun cancelTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        if (timerTask != null) {
            timerTask!!.cancel()
            timerTask = null
        }
    }

    override fun onSpeedItemClick(speed: String) {
        controller.setSpeedSelect(speed)
    }

    private fun sendDanmu(content: String, textcolor: String) {
        if (content.isEmpty()) {
            return
        }
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(
                mActivity,
                vodService.sendDanmu(
                        content,
                        mVodBean.vod_id.toString(),
                        textcolor,
                        videoView.currentPosition.toString()
                ),
                object : BaseObserver<GetScoreBean>() {
                    override fun onSuccess(data: GetScoreBean) {
                        if (data.score != "0") {
                            runOnUiThread {
                                ToastUtils.showShort("发送弹幕成功，获得${data.score}积分")
                            }
                        }
                    }

                    override fun onError(e: ResponseException) {
                        runOnUiThread {
                            ToastUtils.showShort(e.getErrorMessage())
                        }
                    }
                })
    }

    fun getPercentage(curPosition: Long, duration: Long): Float {
        val percentage: Float = curPosition / (duration * 1.0f)
        val df = DecimalFormat("#.00")
        val dfs = DecimalFormatSymbols()
        dfs.decimalSeparator = '.'
        df.decimalFormatSymbols = dfs
        return java.lang.Float.valueOf(df.format(percentage.toDouble()))
    }


    /**
     * CountDownTimer 实现倒计时
     */
    internal class MyCount : CountDownTimer {

        private var mView: View
        private var context: Activity
        private var lisenter: timerFinsihLisenter

        constructor(context: Activity, millisInFuture: Long, countDownInterval: Long, lisenter: timerFinsihLisenter) : super(millisInFuture, countDownInterval) {
            this.mView = context.window.decorView
            this.context = context
            this.lisenter = lisenter
        }

        override fun onFinish() {
//            context.findViewById<TextView>(R.id.ad_time).visibility = View.GONE
            context.runOnUiThread {
                lisenter.onFinish()
            }
        }

        override fun onTick(millisUntilFinished: Long) {
            context.runOnUiThread {
                lisenter.onTicks(millisUntilFinished)
            }
        }

        interface timerFinsihLisenter {
            fun onFinish()
            fun onTicks(mill: Long)
        }
    }

/*    fun getSameActorData(url: String): String {
        var p1: String = ""
        OkHttpUtils.getInstance().getDataAsynFromNet(url, object : OkHttpUtils.MyNetCall {
            override fun success(call: Call?, response: Response?) {
                val playUrl: String? = response?.body?.string()
                Log.d("222ssss", playUrl + "_______")
                if (playUrl?.contains("http")!! && playUrl.contains("m3u8")) {
                    val rgex = "http(.*?)m3u8"
                    p1 = com.ruifenglb.www.utils.StringUtils.getSubUtil(playUrl, rgex)[0]
                    getSameActorData(p1)
                } else {
                    videoView.post {

                        if (url.startsWith("//")) {
                            videoView.setUrl("https:$url")
                        } else {
                            videoView.setUrl(url)
                        }
                        videoView.start()
//            controller.setCurProgress(playScoreInfo?.percentage ?: 0f)
                    }
                }
            }

            override fun failed(call: Call?, e: IOException?) {
                mActivity.runOnUiThread {
                    Toast.makeText(this@NewPlayActivity, "该影片暂时无法播放，请稍后再试", Toast.LENGTH_SHORT).show()
                }
            }
        })
        return p1
    }*/

}

