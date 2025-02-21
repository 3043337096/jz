package com.ruifenglb.www.ui.user

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle

import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ad.biddingsdk.AdConstant
import com.app.ad.biddingsdk.AdListener
import com.app.ad.biddingsdk.AdUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.fragment.BaseFragment
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver
import com.google.gson.Gson
import com.ruifenglb.www.ApiConfig
import com.ruifenglb.www.App
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.observer.LoadingObserver
import com.ruifenglb.www.bean.*
import com.ruifenglb.www.download.SPUtils
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.ui.account.AccountSettingActivity
import com.ruifenglb.www.ui.collection.CollectionActivity
import com.ruifenglb.www.ui.down.AllDownloadActivity
import com.ruifenglb.www.ui.expand.ExpandCenterActivity
import com.ruifenglb.www.ui.feedback.FeedbackActivity
import com.ruifenglb.www.ui.login.ForgetPswActivity
import com.ruifenglb.www.ui.login.LoginActivity
import com.ruifenglb.www.ui.notice.MessageCenterActivity
import com.ruifenglb.www.ui.pay.PayActivity
import com.ruifenglb.www.ui.play.PlayActivity
import com.ruifenglb.www.ui.score.PlayScoreActivity
import com.ruifenglb.www.ui.seek.SeekActivity
import com.ruifenglb.www.ui.share.ShareActivity
import com.ruifenglb.www.ui.task.TaskActivity2
import com.ruifenglb.www.ui.withdraw.GoldWithdrawActivity
import com.ruifenglb.www.ui.withdraw.GoldWithdrawActivitySkin
import com.ruifenglb.www.utils.*
import com.ruifenglb.www.utils.MMkvUtils.Companion.Builds
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_user.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal


class UserFragment : BaseFragment() {
    private val playScoreAdapter: PlayScoreAdapter by lazy {
        PlayScoreAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.getItem(position) as PlayScoreBean
                if (!UserUtils.isLogin()) {
                    LoginActivity.start()
                } else {
                    Builds().savePlayScore(item)
                    PlayActivity.startByPlayScoreResult(this@UserFragment, item.vodId);
                }

            }
        }
    }
    override var isUseEventBus: Boolean = true
    var isInit: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_user
    }

    override  fun onStart(){






        super.onStart()

        if (Data.getQQ() == "暗夜紫"&& Data.getB()=="2") {


            SPUtils.setString(context,"why","暗夜紫")



            uup.setBackgroundResource(R.drawable.shape_bg_orange_to_bschj4)

        }
        if (Data.getQQ() == "原始蓝"&& Data.getB()=="2") {


            SPUtils.setString(context,"why","原始蓝")


            uup.setBackgroundResource(R.drawable.shape_bg_orange_to_bschj5)


        }

    }


    @JvmField
    public var userFragment = this@UserFragment
    public var playVideoReceiver: PlayVideoReceiver = PlayVideoReceiver()
    private fun adGroup() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(this, vodService.addGroup(), object : BaseObserver<CardBuyBean>() {
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
    override fun initView() {
        super.initView()



        if (Data.getQQ() == "暗夜紫") {

            SPUtils.setString(context,"why","暗夜紫")

            uup.setBackgroundResource(R.drawable.userbj)


        }
        if (Data.getQQ() == "原始蓝") {


            SPUtils.setString(context,"why","原始蓝")


            uup.setBackgroundResource(R.drawable.userbj)


        }



        tv_user_task.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv_user_task.setSelected(true);
        tv_user_task.setFocusable(true);
        tv_user_task.setFocusableInTouchMode(true);
        //  mActivity = (context as? AppCompatActivity)!!

        val filter = IntentFilter()
        //给意图过滤器添加action，就是要监听的广播对应的action
        filter.addAction("android.intent.action.AddPlayScore")

        // playVideoReceiver = PlayVideoReceiver()
        mActivity.registerReceiver(playVideoReceiver, filter)

        val userTip = MMkvUtils.Companion.Builds().loadStartBean("")?.document?.notice?.content
                ?: ""
        if (userTip.isNotEmpty()) {
            tv_user_task.text = userTip
        }
        rvPlayScore.layoutManager = LinearLayoutManager(activity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        rvPlayScore.adapter = playScoreAdapter
        val ad = Builds().loadStartBean("")?.ads?.user_center
        if (ad == null || ad.status == 0 || ad.description.isNullOrEmpty()) {
            awvUser.visibility = View.GONE
        } else {
            awvUser.visibility = View.VISIBLE
            awvUser.setOnClickListener {
                if (!UserUtils.isLogin()) {
                    LoginActivity.start()
                } else {
                    val intent = Intent(activity, ExpandCenterActivity::class.java)
                    ActivityUtils.startActivity(intent)
                }
            }
            Glide.with(mContext).load(ad.description).into(awvUser)
        }

//        userRefreshLayout.setDisableContentWhenRefresh(false) //是否在刷新的时候禁止列表的操作
//        userRefreshLayout.setDisableContentWhenLoading(false) //是否在加载的时候禁止列表的操作
//        userRefreshLayout.setEnableLoadMore(false) //是否启用上拉加载功能
//        userRefreshLayout.setEnableRefresh(true) //是否启用上拉加载功能;
//        userRefreshLayout.setEnableAutoLoadMore(false)
//        userRefreshLayout.setOnRefreshListener(OnRefreshListener {
//            updateUserInfo()
//            userRefreshLayout.finishRefresh()
//        })
//        userRefreshLayout.setOnLoadMoreListener(OnLoadMoreListener {
//        })
//        if(!isInit) {
//            isInit = true
//            updateUserInfo()
//        }

        getGroupChatList()
    }

    override fun initListener() {
        super.initListener()
        tvLogin.setOnClickListener {
            LoginActivity.start()
        }

        iv_user_title.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                ActivityUtils.startActivity(AccountSettingActivity::class.java)
            }
        }

        iv_user_title2.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                ActivityUtils.startActivity(AccountSettingActivity::class.java)
            }
        }

        iv_user_fenxiao.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                val intent = Intent(activity, ExpandCenterActivity::class.java)
                ActivityUtils.startActivity(intent)
            }
        }

        iv_user_pic.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                ActivityUtils.startActivity(AccountSettingActivity::class.java)
            }
        }

        tv_user_task.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                TaskActivity2.start()
            }
        }
        tv_user_t2.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                val intent = Intent(activity, PayActivity::class.java)
                intent.putExtra("type", 1)
                ActivityUtils.startActivity(intent)
            }

        }
        tv_user_t5.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                TaskActivity2.start()
            }
        }

        tv_user_share.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                start()
            }
        }

        tv_user_share2.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                start()
            }
        }

        item_tv_playinfo_feedback.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                ActivityUtils.startActivity(FeedbackActivity::class.java)
            }
        }

        item_tv_playinfo_feedback2.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                ActivityUtils.startActivity(FeedbackActivity::class.java)
            }
        }

        tv_qqun.setOnClickListener {
            val startBean = Builds().loadStartBean("")
            if (startBean != null && startBean.ads != null && startBean.ads.service_qqqun != null && startBean.ads.service_qqqun.description != null) {
                joinQQGroup(startBean.ads.service_qqqun.description)
            } else {
                ToastUtils.showShort("暂无官方QQ群")
            }
        }

        tv_user_service.setOnClickListener {
             var description: String = ""
              val startBean = MMkvUtils.Builds().loadStartBean("")
            if (startBean != null && startBean.ads != null && startBean.ads.service_qq != null && startBean.ads.service_qq.description != null) {
               description = startBean.ads.service_qq.description
            }
            //获取QQ
           if (description.contains("uin=")) {
               description = description.split("uin=")[1]
           }
           if (description.contains("&site")) {
               description.split("&site")[0]
           }
           val link = "mqq://im/chat?chat_type=wpa&uin=${description}&version=1&src_type=web"

           Intent(Intent.ACTION_VIEW, Uri.parse(link)).let {
               if (it.resolveActivity(mActivity.packageManager) != null) {
                   ActivityUtils.startActivity(it)
              } else {
                 ToastUtils.showShort("未安装QQ!!")
               }
           }
           }

        tv_home_seek.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                ActivityUtils.startActivity(SeekActivity::class.java)
            }
        }
        tv_coin_withdraw.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                ActivityUtils.startActivity(GoldWithdrawActivity::class.java)
            }
        }


        llCollect.setOnClickListener {
         if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
            ActivityUtils.startActivity(CollectionActivity::class.java)
        }
        }


        tv_user_sign.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                sign()
            }

        }






        llPlayScore.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                //  ActivityUtils.startActivity(PlayScoreActivity::class.java)
                val intent = Intent(activity, PlayScoreActivity::class.java)
                startActivityForResult(intent, 2)
            }
        }
        findss.setOnClickListener {
            activity?.let { it1 -> ForgetPswActivity.start(it1) }
        }
        llClear.setOnClickListener {
            LitePal.deleteAll(PlayScoreBean::class.java)
            ToastUtils.showShort("已清除缓存")
            getPlayScore()
        }
        llNotice.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                val intent = Intent(activity, MessageCenterActivity::class.java)
                startActivity(intent)
            }
        }

        skin.setOnClickListener {



            ActivityUtils.startActivity(GoldWithdrawActivitySkin::class.java)


        }


        tv_user_ccdhy.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                val intent = Intent(activity, PayActivity::class.java)
                intent.putExtra("type", 1)
                ActivityUtils.startActivity(intent)
            }
        }

        tv_user_ccdhy2.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                val intent = Intent(activity, PayActivity::class.java)
                intent.putExtra("type", 1)
                ActivityUtils.startActivity(intent)
            }
        }


        llCache.setOnClickListener {
            if (!UserUtils.isLogin()) {
                LoginActivity.start()
            } else {
                if (LoginUtils.checkLogin(activity)) {
                    activity?.let { it1 -> AllDownloadActivity.start(it1) }
                }
            }
        }


    }

    fun getUserInfo() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(this, vodService.userInfo(), object : BaseObserver<UserInfoBean>() {
            override fun onSuccess(data: UserInfoBean) {
                updateUserInfo(data)
                UserUtils.userInfo = data
                getPlayScore()
                EventBus.getDefault().post(data)//通知改变信息
                when (data.user_level) {
                    "1" -> {
                        tv_user_vip.setBackgroundResource(R.drawable.ic_vip1)
                    }
                    "2" -> {
                        tv_user_vip.setBackgroundResource(R.drawable.ic_vip2)
                    }
                    "3" -> {
                        tv_user_vip.setBackgroundResource(R.drawable.ic_vip3)
                    }
                    "4" -> {
                        tv_user_vip.setBackgroundResource(R.drawable.ic_vip4)
                    }
                    "5" -> {
                        tv_user_vip.setBackgroundResource(R.drawable.ic_vip5)
                    }
                }
            }

            override fun onError(e: ResponseException) {
            }
        })
    }

    private fun getGlodTip() {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(this, vodService.goldTip(),
                object : LoadingObserver<GoldTipBean>(mActivity) {
                    override fun onSuccess(data: GoldTipBean) {
                        tv_user_money.text = data.can_money
                    }

                    override fun onError(e: ResponseException) {

                    }

                })
    }



    @Subscribe
    fun onLoginSucces(data: LoginBean? = null) {
        getUserInfo()
        getPlayScore()
        getGlodTip()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            UserUtils.userInfo = null
            updateUserInfo()
            getPlayScore()

            if (UserUtils.isLogin()) {
                getUserInfo()
                getGlodTip()
                onLoginSucces()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUserInfo()
        getPlayScore()

        if (UserUtils.isLogin()) {
            onLoginSucces()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //从我的页面点击视频播放界面和视频播放记录页面
        if (requestCode == 1 || requestCode == 2) {
            getPlayScore()
        }

    }

    class PlayVideoReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i(javaClass.name.toString(), "onReceive playscore")
            //   UserFragment.newInstance().getPlayScore();
            // UserFragment.newInstance().mHandler.sendEmptyMessage(1)


        }

    }

    public var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    getPlayScore()
                }
            }
        }
    }


//    internal val playVideoReceiver: BroadcastReceiver by lazy {
//        BroadcastReceiver.apply {
//              fun onReceive(context: Context, intent: Intent) {
//         Log.i(javaClass.name.toString(), "onReceive playscore")
//                  userFragment.getPlayScore()
//
//       }
//
//        }
//    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
//        getPlayScore()
//        if (UserUtils.isLogin()) {
//            onLoginSucces()
//        }
    }

    @Subscribe
    fun onLogout(data: LogoutBean? = null) {
        UserUtils.userInfo = null
        updateUserInfo()
    }


    private fun sign() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(this, vodService.sign(), object : BaseObserver<GetScoreBean>() {
            override fun onSuccess(data: GetScoreBean) {
                if (data.score == "0") {
                    ToastUtils.showShort(R.string.sign_success)
                } else {
                    ToastUtils.showShort("签到成功，获得${data.score}积分")
                    // AdUtils.interstitialAd(activity)
                    AdUtils.fullScreenAd(mActivity, AdConstant.FULL_SCREEN_AD ,object :AdListener{
                        override fun onShow() {
                        }

                        override fun onClose() {
                        }
                    })
                }
                onLoginSucces()
            }

            override fun onError(e: ResponseException) {
                ToastUtils.showShort(e.getErrorMessage())
                /* AdUtils.rewardVideo(mActivity,ADConfig.AdReward_SignIN,object :AdListener{
                     override fun onShow() {
                         adGroup()
                     }

                     override fun onClose() {

                     }

                 })*/
            }
        })
    }

    private fun start() {
        //    Toast.makeText(activity, "分享", Toast.LENGTH_SHORT).show()

        var intn = Intent(this.mActivity, ShareActivity::class.java)
        intn.putExtra("vom_name", "有奖推广活动")
        intn.putExtra("vod_pic", "www") //图片
        intn.putExtra("vod_class", "分享给未安装过的用户、对方注册并打开应用算分享成功") //标签
        intn.putExtra(
                "vod_blurd",
                "推广可获得积分奖励\n推广可获得观影次数"
        ) //注释
        startActivity(intn)
    }
    private fun updateUserInfo(data: UserInfoBean? = null) {
        if (UserUtils.isLogin()) {
            tvLogin.visibility = View.GONE
            //   tv_user_name.visibility = View.VISIBLE

            tv_user_phone.visibility = View.VISIBLE
            tv_user_vip.visibility = View.VISIBLE

            tv_user_jifen.visibility = View.VISIBLE



        } else {
            tvLogin.visibility = View.VISIBLE
            //    tv_user_name.visibility = View.INVISIBLE
            tv_user_phone.visibility = View.INVISIBLE
            tv_user_vip.visibility = View.INVISIBLE

            tv_user_jifen.visibility = View.INVISIBLE


        //    tv_user_jinbi.text = "剩余金币 0"
            tv_user_jifen.text = ""
            tv_user_money.text = ""
            tv_user_video.text = "登录查看"
            time2.text = "免费观影"
            buy.text = "购买SVIP"
            tv_user_sign.text = "签到"

            buy2.text = "特惠购会员"
            time.text = "SVIP时间"
            tv_user_time.text = "登录查看"


        }
        data?.let {
            val isVIp = it.group.group_name.contains("VIP")
            SPUtils.setBoolean(activity, "isVip", isVIp)
            if (!SPUtils.getBoolean(activity, "isVip")) {
                tv_user_time.text = "已过期"
                time.text = "SVIP时间"
                time2.text = "免费观影"
                buy.text = "购买SVIP"
                buy2.text = "特惠购会员"
                tv_user_sign.text = "签到"

                tv_user_phone.setTextColor(ColorUtils.getColor(R.color.textColor));
                tv_user_vip.setBackgroundResource(R.drawable.ic_vipff);
            } else {
               
                tv_user_phone.setTextColor(ColorUtils.getColor(R.color.goldenrod));
            }

            buy.text = "购买SVIP"
            buy2.text = "特惠购会员"
            time.text = "SVIP时间"
            time2.text = "免费观影"
            tv_user_sign.text = "签到"

            tv_user_time.text = "到期时间:  ${DateUtil.getyyyyMMddHHmm(it.user_end_time*1000)}"
            if (!SPUtils.getBoolean(activity,"isVip")){
                tv_user_time.text = "已过期"
                time.text = "SVIP时间"
                time2.text = "免费观影"
                buy.text = "购买SVIP"
                buy2.text = "特惠购会员"
                tv_user_sign.text = "签到"


            }
            tv_user_phone.text = data.user_nick_name
           // tv_user_jinbi.text = it.user_gold
            tv_user_jifen.text = "积分：${it.user_points}"
            tv_user_video.text = "${it.leave_times}次"

            if (it.user_portrait.isNotEmpty()) {
                Glide.with(mActivity)
                        .load(ApiConfig.MOGAI_BASE_URL + "/" + it.user_portrait)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(iv_user_pic)
            } else {
                Glide.with(mActivity)
                        .load(R.drawable.ic_default_avator)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(iv_user_pic)
            }
        }
    }


    private fun getPlayScore() {
//        LitePal.order("id desc")
//                .findAsync(PlayScoreBean::class.java)
//                .listen {
//                    when {
//                        it.size > 10 -> {
//                            rvPlayScore.visibility = View.VISIBLE
//                            playScoreAdapter.setNewData(it.subList(0, 10))
//                        }
//                        it.size == 0 -> rvPlayScore.visibility = View.GONE
//                        else -> {
//                            rvPlayScore.visibility = View.VISIBLE
//                            playScoreAdapter.setNewData(it)
//                        }
//                    }
//                }


        var playScoreBeans = ArrayList<PlayScoreBean>()
        if (UserUtils.isLogin()) {
            val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
            if (AgainstCheatUtil.showWarn(vodService)) {
                return;
            }
            RequestManager.execute(this, vodService.getPlayLogList("1", "12"),
                    object : LoadingObserver<Page<PlayLogBean>>(this@UserFragment.mActivity) {
                        override fun onSuccess(data: Page<PlayLogBean>) {
                            val playLogBeans = data.list
                            playLogBeans.forEach {
                                val playScoreBean: PlayScoreBean = PlayScoreBean()
                                playScoreBean.vodName = it.vod_name
                                playScoreBean.vodImgUrl = it.vod_pic
                                if (it.percent.equals("NaN")) {
                                    playScoreBean.percentage = 0.0f
                                } else {
                                    try {
                                        playScoreBean.percentage = it.percent.toFloat()
                                    } catch (ex: Exception) {
                                    }
                                }
                                playScoreBean.typeId = it.type_id;
                                playScoreBean.vodId = it.vod_id.toInt();
                                playScoreBean.isSelect = false
                                playScoreBean.vodSelectedWorks = it.nid.toString()

                                playScoreBean.urlIndex = it.urlIndex
                                playScoreBean.curProgress = it.curProgress
                                playScoreBean.playSourceIndex = it.playSourceIndex


                                var gson: Gson = Gson()
                                var playScoreBeanStr = gson.toJson(playScoreBean).toString();
                                Log.i("playlog", "playScoreBean${playScoreBeanStr}")
                                playScoreBeans.add(playScoreBean)

                                if (playScoreBeans.size > 10) {
                                    playScoreAdapter.setNewData(playScoreBeans.subList(0, 10))
                                } else {
                                    playScoreAdapter.setNewData(playScoreBeans)
                                }

                            }

                            if (data.list.size==0){
                                playScoreAdapter.setNewData(playScoreBeans)
                            }


                            Log.i("playlog", "getPlayLogList11${data}");
                        }

                        override fun onError(e: ResponseException) {
                            Log.i("playlog", "getPlayLogList222")

                        }
                    })


        }
    }

    private class PlayScoreAdapter :
            BaseQuickAdapter<PlayScoreBean, BaseViewHolder>(R.layout.item_play_score_horizontal) {
        override fun convert(helper: BaseViewHolder, item: PlayScoreBean?) {
            item?.run {
                val name = if (item.typeId == 3) {
                    "$vodName $vodSelectedWorks"
                } else if (item.typeId == 1) {
                    "$vodName"
                } else {
                    "$vodName ${vodSelectedWorks}"
                }
                helper.setText(R.id.tvName, name)
                helper.setText(R.id.tvPlayProgress, "${(percentage * 100).toInt()}%")
                val mation: MultiTransformation<Bitmap> = MultiTransformation(
                        CenterCrop(),
                        RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL)
                )
                Glide.with(helper.itemView.context)
                        .load(vodImgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(RequestOptions.bitmapTransform(mation))
                        .into(helper.getView<View>(R.id.ivImg) as ImageView)
            }
        }

    }


    companion object {
        @JvmStatic
        fun newInstance(): UserFragment {
            val args = Bundle()
            val fragment = UserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onOpenShareEvent(event: OpenShareEvent) {
        if (!UserUtils.isLogin()) {
            LoginActivity.start()
        } else {
            ActivityUtils.startActivity(ShareActivity::class.java)
        }
    }

    fun gotoWeb(url: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val uri = Uri.parse(url)
        intent.data = uri
        if (intent.resolveActivity(App.getApplication().packageManager) != null) {
            startActivity(intent)
        } else {
            //要调起的应用不存在时的处理
        }
    }

    //获取群聊列表
    private fun getGroupChatList() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(
                this,
                vodService.groupChat(),
                object : BaseObserver<GroupChatBean>() {
                    override fun onSuccess(data: GroupChatBean) {
                        val list = data.list
                        for (i in list.indices) {
                            if (i == 0) {
                                llPotato.visibility = View.VISIBLE

                                tv_potato.text = list[0].title
                                llPotato.setOnClickListener {
                                    gotoWeb(list[0].url)
                                }
                            } else if (i == 1) {
                                llPlane.visibility = View.VISIBLE

                                tv_plane.text = list[1].title
                                llPlane.setOnClickListener {
                                    gotoWeb(list[1].url)
                                }
                            }
                        }
                    }

            override fun onError(e: ResponseException) {

            }
        })
    }


}
