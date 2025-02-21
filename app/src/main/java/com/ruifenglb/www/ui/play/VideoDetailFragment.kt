package com.ruifenglb.www.ui.play

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import com.ruifenglb.www.utils.UserUtils.isLogin
import android.widget.Toast
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ad.biddingsdk.AdConstant
import com.app.ad.biddingsdk.AdListener
import com.app.ad.biddingsdk.AdUtils
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dueeeke.videoplayer.player.VideoViewManager
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.fragment.BaseFragment
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.ruifenglb.av.play.AvVideoView
import com.ruifenglb.av.play.VideoViewImpt
import com.ruifenglb.www.ApiConfig
import com.ruifenglb.www.R
import com.ruifenglb.www.ad.AdWebView
import com.ruifenglb.www.utils.MMkvUtils.Companion.Builds
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.bean.*
import com.ruifenglb.www.jiexi.BackListener
import com.ruifenglb.www.jiexi.JieXiUtils2
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.ui.down.AllDownloadActivity
import com.ruifenglb.www.ui.down.cache.Square
import com.ruifenglb.www.ui.down.cache.SquareViewBinder
import com.ruifenglb.www.ui.feedback.FeedbackActivity
import com.ruifenglb.www.ui.home.MyDividerItemDecoration
import com.ruifenglb.www.ui.login.LoginActivity
import com.ruifenglb.www.ui.pay.PayActivity
import com.ruifenglb.www.ui.share.ShareActivity
import com.ruifenglb.www.ui.widget.HitDialog
import com.ruifenglb.www.ui.widget.HitDialog.OnHitDialogClickListener
import com.ruifenglb.www.utils.*
import com.ruifenglb.www.utils.DensityUtils.dp2px
import com.ruifenglb.www.utils.DensityUtils.getScreenWidth
import com.ruifenglb.www.utils.decoration.GridItemDecoration
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import jaygoo.library.m3u8downloader.control.DownloadPresenter
import jaygoo.library.m3u8downloader.db.table.M3u8DoneInfo
import jaygoo.library.m3u8downloader.db.table.M3u8DownloadingInfo
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_pay.*
import kotlinx.android.synthetic.main.activity_new_play.*
import kotlinx.android.synthetic.main.fragment_play_detail.*
import kotlinx.android.synthetic.main.fragment_play_detail.refreshLayout
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.item_playinfo.*
import kotlinx.android.synthetic.main.item_recommend.*
import me.drakeet.multitype.MultiTypeAdapter
import org.litepal.LitePalApplication
import java.util.*

class VideoDetailFragment : BaseFragment() {

    private lateinit var mVodBean: VodBean
    private var isParse = false
    private var isCollected: Boolean = false
    private var urlIndex: Int = 0 //播放集
    private var playSourceIndex: Int = 0//播放源
    private var curCommentPage = 1
    private lateinit var vod_play_list: List<PlayFromBean>//播放视频列表
    private var curType = 0//推荐 默认是相似推荐
    private var curSameTypePage = 1

    private lateinit var videoView: AvVideoView
    private var curSameActorPage = 1
    private var curParseIndex = 0//记录上一次解析到的位置，如果出现解析到是视频不能播放的话 自动解析下一条
    private var curFailIndex = -1

    private val recommendAdapter: RecommendAdapter by lazy {

        RecommendAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val vodBean = adapter.getItem(position) as VodBean

                playActivity.showNewVideo(vodBean)


            }
        }
    }

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter().apply {

            setHeaderAndEmpty(true)
        }
    }
    private val selectionAdapter: SelectionAdapter by lazy {
        SelectionAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                if (urlIndex != position) {
                    urlIndex = position
                    playActivity.changeSelection(urlIndex, false)
                    notifyDataSetChanged()

                }
            }
        }
    }

    private val headerView: View by lazy {
        View.inflate(mActivity, R.layout.layout_video_detail, null)

    }

    private lateinit var rvLastest: RecyclerView
    private lateinit var tlPlaySource: TabLayout
    private lateinit var playActivity: NewPlayActivity

    public var isOrder = true//播放顺序，默认为顺序播放

    override fun getLayoutId(): Int {

        return R.layout.fragment_play_detail

    }

    override fun initView() {

        super.initView()

        playActivity = mActivity as NewPlayActivity

        arguments?.run {
            mVodBean = getParcelable(VOD_BEAN) ?: null as VodBean
            urlIndex = getInt(URL_INDEX)
            playSourceIndex = getInt(PLAY_SOURCE_INDEX)
        }

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setRefreshFooter(ClassicsFooter(mActivity))

        rvPlayDetail.layoutManager = LinearLayoutManager(mActivity)
        rvPlayDetail.adapter = commentAdapter
        initHeaderMsg()
        commentAdapter.addHeaderView(headerView)

        getCommentList()
        getSameTypeData()

    }

    override fun onResume() {
        super.onResume()

        getCollectionState()
    }

    fun changeCurIndex(urlIndex: Int) {
        this.urlIndex = urlIndex

        selectionAdapter.notifyDataSetChanged()

        scrollCurIndex(rvLastest)
    }
    var videoViewImpt: VideoViewImpt = VideoViewManager.instance()["pip"] as AvVideoView
    open fun sendDanmaku_wqddg(str: String) {
        if (videoViewImpt == null) return
        if (!StringUtils.isEmpty(str)) {
            videoViewImpt.showDanmaku()
            videoViewImpt.addDanmaku(str, str, true)

        } else {
            ToastUtils.showShort("请输入弹幕！")
        }
    }
    @SuppressLint("SetTextI18n")
    private fun initHeaderMsg() {
        val danmakubj = headerView.findViewById<LinearLayout>(R.id.tv_av_danmakubj)
        val bj8 = headerView.findViewById<LinearLayout>(R.id.bj8)
        val bj11 = headerView.findViewById<LinearLayout>(R.id.bj11)
        val bj3 = headerView.findViewById<LinearLayout>(R.id.bj3)
        val changeSourceTipLayout = headerView.findViewById<LinearLayout>(R.id.changeSourceTipLayout)
        val text4 = headerView.findViewById<TextView>(R.id.text4)
        val text5 = headerView.findViewById<TextView>(R.id.text5)
        val danmaku2 = headerView.findViewById<TextView>(R.id.tv_av_danmaku2)
        val title = headerView.findViewById<TextView>(R.id.item_tv_playinfo_title)
        val intro = headerView.findViewById<TextView>(R.id.item_tv_playinfo_intro)
        val year = headerView.findViewById<TextView>(R.id.item_tv_playinfo_year)
        val area = headerView.findViewById<TextView>(R.id.item_tv_playinfo_area)
        val type = headerView.findViewById<TextView>(R.id.item_tv_playinfo_type)
        val score = headerView.findViewById<TextView>(R.id.item_tv_playinfo_score)
        val tvLastest = headerView.findViewById<TextView>(R.id.tvLastest)
        val ivLastest = headerView.findViewById<ImageView>(R.id.iv_lastest)
        val sortVodTypePic = headerView.findViewById<TextView>(R.id.item_svv_playtypePic)
        val sortVodView = headerView.findViewById<TextView>(R.id.item_svv_playinfo)
        val checkOrder = headerView.findViewById<CheckBox>(R.id.checkOrder)
        tlPlaySource = headerView.findViewById(R.id.tlPlaySource)
        rvLastest = headerView.findViewById(R.id.rvLastest)
        val ivGovip = headerView.findViewById<LinearLayout>(R.id.iv_go_vip)
        val awvPlayerDown = headerView.findViewById<AdWebView>(R.id.awvPlayerDown)
        val ad = MMkvUtils.Companion.Builds().loadStartBean("")?.ads?.player_down
        awvPlayerDown.visibility = View.GONE
        if (ad == null || ad.status == 0 || ad.description.isNullOrEmpty()) {
            // awvPlayerDown.visibility = View.GONE
        } else {
            /* awvPlayerDown.visibility = View.VISIBLE
             awvPlayerDown.loadHtmlBody(ad.description)*/
            if(com.ruifenglb.www.utils.MatchUtil.checkInteractionAd(context!!,ad.description)){
                AdUtils.interstitialAd(activity, AdConstant.INTERSTITIAL_SCREEN_AD, object :AdListener{
                    override fun onShow() {

                    }

                    override fun onClose() {

                    }
                })
            }

        }
        val bannerLayout = headerView.findViewById<FrameLayout>(R.id.bannerLayout)
        // App.getApplication().adUtils.showBannerAd(activity,bannerLayout,object :AdListener(){})
        AdUtils.nativeExpressAd(activity, AdConstant.INFO_FLOW_AD, bannerLayout);
  //7242484
        var isVip = false
        if (isLogin() && UserUtils.userInfo?.group_id == 3) {
            isVip = true
        }
        val startBean = Objects.requireNonNull(Builds().loadStartBean(""))
        if (startBean != null && startBean.ads != null) {
            val ads = startBean.ads
            if (isVip || ads.player_down_isvip == null || ads.player_down_isvip.status != 1 || ads.player_down_isvip.description.isNullOrEmpty()) {
                ivGovip.visibility = View.GONE
            } else {
                if (ads.player_down_isvip.description.contains("||")) {
                    val descriptions = ads.player_down_isvip.description.split("||")
                    headerView.findViewById<TextView>(R.id.isvip_1).text = descriptions[0]
                    headerView.findViewById<TextView>(R.id.isvip_2).text = descriptions[1]
                }
                ivGovip.visibility = View.VISIBLE
            }

            if (ads.play_message == null || ads.play_message.status != 1 || ads.play_message.description.isNullOrEmpty()) {
                headerView.findViewById<LinearLayout>(R.id.iv_play_message).visibility = View.GONE
            } else {
                headerView.findViewById<LinearLayout>(R.id.iv_play_message).visibility = View.VISIBLE
                headerView.findViewById<TextView>(R.id.play_message).text = ads.play_message.description
            }


            if (startBean.app_play_recommend == null || startBean.app_play_recommend != "1") {
                headerView.findViewById<LinearLayout>(R.id.item_tuijian).visibility = View.GONE
            } else {
                headerView.findViewById<LinearLayout>(R.id.item_tuijian).visibility = View.VISIBLE
            }
        } else {
//            awvPlayerDown.visibility = View.GONE
            headerView.findViewById<LinearLayout>(R.id.item_tuijian).visibility = View.GONE
            headerView.findViewById<LinearLayout>(R.id.iv_play_message).visibility = View.GONE
        }
//7242484
        headerView.findViewById<TextView>(R.id.item_tv_playinfo_grade).setOnClickListener {
            score()

        }



        headerView.findViewById<TextView>(R.id.item_tv_playinfo_collect)
                .setOnClickListener {
                    if (UserUtils.isLogin()) {
                        if (isCollected) {
                            uncollection()
                        } else {
                            collection()
                        }
                    } else {
                        ActivityUtils.startActivity(LoginActivity::class.java)
                    }

                }
        //下载
        headerView.findViewById<TextView>(R.id.item_tv_playinfo_download)
                .setOnClickListener {
                    if (MMkvUtils.Builds().loadStartBean("")?.ads?.download?.status != 0) {
                        if (!LoginUtils.checkLogin2(activity)) {
                            HitDialog(context!!).setTitle("提示").setMessage("需登录后开通VIP才可下载，确定登录。").setOnHitDialogClickListener(object : OnHitDialogClickListener() {
                                override fun onCancelClick(dialog: HitDialog) {
                                    super.onCancelClick(dialog)
                                }

                                override fun onOkClick(dialog: HitDialog) {
                                    super.onOkClick(dialog)
                                    ActivityUtils.startActivity(LoginActivity::class.java)
                                }
                            }).show()
                        } else {
                            if (LoginUtils.checkVIP(activity, "下载需要开通vip是否去开通")) {
                                startCache()
                            }
                        }
                    } else {
                        startCache()
                    }


                }

        headerView.findViewById<TextView>(R.id.item_tv_playinfo_feedback)

                .setOnClickListener {
                   // ActivityUtils.startActivity(FeedbackActivity::class.java)
                    //val message = "视频《${mVodBean.vod_name}》播放失败,播放源:${playActivity.playFrom.player_info.show},地址${playActivity.playList!![urlIndex].url}"
                    val message = "视频《${mVodBean.vod_name}》播放失败\n播放源：${playActivity.playFrom.player_info.show}\n视频序列：${playActivity.playList!![urlIndex].name}\n请及时修复！"
                    FeedbackActivity.start(mActivity, message)
                //                    EventBus.getDefault().post("画中画")
                }
        headerView.findViewById<TextView>(R.id.item_tv_playinfo_share)
                .setOnClickListener {
                    if (!UserUtils.isLogin()) {
                        ActivityUtils.startActivity(LoginActivity::class.java)
                    } else {
                        var intn = Intent(this.mActivity, ShareActivity::class.java)
                        intn.putExtra("vom_name", mVodBean.vodName)
                        intn.putExtra("vod_pic", mVodBean.vodPic) //图片
                        Log.e("wqddg", mVodBean.toString());
                        intn.putExtra("vod_class_typeName", mVodBean.type.typeName)
                        intn.putExtra("vod_class_year", mVodBean.vod_year) //标签
                        intn.putExtra("vod_class_area", mVodBean.vod_area)
                        intn.putExtra("vod_class_class", mVodBean.vod_class.replace(",", " / "))
                        intn.putExtra("vod_blurd", mVodBean.vodBlurb) //注释
                        startActivity(intn)
                    }
                }

        headerView.findViewById<TextView>(R.id.tv_av_danmaku2)
                .setOnClickListener {
                    if (UserUtils.isLogin()) {
                        val avVideoView = VideoViewManager.instance().get("pip") as AvVideoView
                        CommentDialog(mActivity, "弹幕") //目前调用的是评论的对话框，对话框提示信息是评论相关的，看能不能判断下从这边拉起的对话框改变下提示文字
                                .setOnCommentSubmitClickListener(object : CommentDialog.OnCommentSubmitClickListener {
                                    override fun onCommentSubmit(comment: String) {
                                        sendDanmaku_wqddg(comment)
                                        sendDanmu(comment, avVideoView.currentPosition.toString()) //发送弹幕
//                                        sendDanmu(comment,System.currentTimeMillis().toString()) //发送弹幕
                                    }
                                })
                                .show()
                    } else {
                        LoginActivity.start()
                    }
//                    val message = "请更新视频：${mVodBean.vod_name}\n视频播放源：${playActivity.playFrom.player_info.show}\n当前剧集数：${playActivity.playList!![urlIndex].name}"
//                    FeedbackActivity.start(mActivity, message)
                }

        tvLastest.setOnClickListener {
            playActivity.showPlayList()
        }
        headerView.findViewById<LinearLayout>(R.id.iv_go_vipm)
                .setOnClickListener {
                    if (!isLogin()) {
                        LoginActivity.start()
                    } else {
                        val intent = Intent(activity, PayActivity::class.java)
                        intent.putExtra("type", 1)
                        ActivityUtils.startActivity(intent)
                    }
                }
        ivLastest.setOnClickListener {
            playActivity.showPlayList()
        }


        title.text = mVodBean.vod_name



        ///////////////////////QQ7242484///////////////////////////////////////
        if (Data.getQQ() == "暗夜紫") {
        bj8.setBackgroundColor(ColorUtils.getColor(R.color.xkh))

        bj3.setBackgroundColor(ColorUtils.getColor(R.color.xkh))
        title.setTextColor(ColorUtils.getColor(R.color.white))
        text4.setTextColor(ColorUtils.getColor(R.color.white))
        text5.setTextColor(ColorUtils.getColor(R.color.white))
        danmaku2.setTextColor(ColorUtils.getColor(R.color.white))
        danmakubj.setBackgroundResource(R.drawable.bg_skip3)
        changeSourceTipLayout.setBackgroundResource(R.drawable.shape_bg_gray_radius_10dpanhei)

        }
        if (Data.getQQ() == "原始蓝") {
        bj8.setBackgroundColor(ColorUtils.getColor(R.color.white))

        bj3.setBackgroundColor(ColorUtils.getColor(R.color.white))
        title.setTextColor(ColorUtils.getColor(R.color.textColor))
        text4.setTextColor(ColorUtils.getColor(R.color.textColor))
        text5.setTextColor(ColorUtils.getColor(R.color.textColor))
        danmaku2.setTextColor(ColorUtils.getColor(R.color.textColor))
        danmakubj.setBackgroundResource(R.drawable.bg_skip4)
        changeSourceTipLayout.setBackgroundResource(R.drawable.shape_bg_gray_radius_10dp)

        }
        //////////////////////QQ7242484///////////////////////////////////////
        year.text = mVodBean.vod_year
        area.text = mVodBean.vod_area
        type.text = mVodBean.type.typeName
        score.text = mVodBean.vod_score + "分"

        sortVodView.text = mVodBean.vod_class.replace(",", " / ")
        intro.setOnClickListener {

            playActivity.showSummary()
        }

        if (mVodBean.vodRemarks.isNotEmpty()) {
            tvLastest.text = mVodBean.vodRemarks//选集
            ivLastest.visibility = View.VISIBLE
        } else {
            ivLastest.visibility = View.GONE
        }
        if (mVodBean.type.typeName.equals("电影")) {
            sortVodTypePic.setBackgroundResource(R.drawable.playinfo_dypic);
        } else if (mVodBean.type.typeName.equals("剧集")) {
            sortVodTypePic.setBackgroundResource(R.drawable.playinfo_jjpic);
        } else if (mVodBean.type.typeName.equals("综艺")) {
            sortVodTypePic.setBackgroundResource(R.drawable.playinfo_zypic);
        } else if (mVodBean.type.typeName.equals("动漫")) {
            sortVodTypePic.setBackgroundResource(R.drawable.playinfo_dmpic);
        } else if (mVodBean.type.typeName.equals("B站")) {
            sortVodTypePic.setBackgroundResource(R.drawable.playinfo_bilipic);
        } else {
            sortVodTypePic.setBackgroundResource(R.drawable.playinfo_jlppic);
        }
//        vod_play_list.cl
        vod_play_list = mVodBean.vod_play_list
        rvLastest.layoutManager = LinearLayoutManager(mActivity).apply {
            orientation = LinearLayoutManager.HORIZONTAL

        }
        rvLastest.adapter = selectionAdapter


        if (vod_play_list.isNotEmpty()) {
            for (i in vod_play_list.indices) {
                val playFromBean = vod_play_list[i]
                val playerInfo = playFromBean.player_info
                val urls = playFromBean.urls

                var playSource = playerInfo.show
                if (StringUtils.isEmpty(playSource)) {
                    playSource = "默认"
                }
                if (i == playSourceIndex) {
                    selectionAdapter.addData(urls)
                }

                val tab = tlPlaySource.newTab().setText(playSource)
                tlPlaySource.addTab(tab)
            }
        }

        tlPlaySource.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                Log.d("", "")
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                Log.d("", "")
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

                val playFromBean = vod_play_list[tlPlaySource.selectedTabPosition]

                selectionAdapter.setNewData(playFromBean.urls)

                playActivity.changePlaySource(playFromBean, tlPlaySource.selectedTabPosition)
            }

        })

        tlPlaySource.getTabAt(playSourceIndex)?.select()
        scrollCurIndex(rvLastest)


        val rvRecommand = headerView.findViewById<RecyclerView>(R.id.rvRecommand)
        val tvChange = headerView.findViewById<TextView>(R.id.tvChange)
        val tvSameType = headerView.findViewById<TextView>(R.id.tvSameType)
        val item_tuijian = headerView.findViewById<LinearLayout>(R.id.item_tuijian)
        val tvSameActor = headerView.findViewById<TextView>(R.id.tvSameActor)
        val dividerItemDecoration = MyDividerItemDecoration(mActivity, RecyclerView.HORIZONTAL, false)
        dividerItemDecoration.setDrawable(mActivity.resources.getDrawable(R.drawable.divider_image))
        rvRecommand.addItemDecoration(dividerItemDecoration)
        rvRecommand.layoutManager = GridLayoutManager(mActivity, 3, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return 6
                }
            }
        }
        ///////////////////////QQ7242484///////////////////////////////////////
        if (Data.getQQ() == "暗夜紫") {
            item_tuijian.setBackgroundColor(ColorUtils.getColor(R.color.xkh))
            bj4.setBackgroundColor(ColorUtils.getColor(R.color.xkh2))


        }
        if (Data.getQQ() == "原始蓝") {
            item_tuijian.setBackgroundColor(ColorUtils.getColor(R.color.white))
            bj4.setBackgroundColor(ColorUtils.getColor(R.color.white))

        }
        //////////////////////QQ7242484///////////////////////////////////////

        rvRecommand.adapter = recommendAdapter
        tvChange.setOnClickListener {
            when (curType) {
                0 -> getSameTypeData()
                1 -> getSameActorData()
            }
        }
        tvSameType.setOnClickListener {
            if (curType != 0) {
                curType = 0
                tvSameType.setTextColor(ColorUtils.getColor(R.color.colorPrimary))
                tvSameActor.setTextColor(ColorUtils.getColor(R.color.gray_999))

                getSameTypeData()

            }
        }

        checkOrder.setOnCheckedChangeListener { buttonView, isChecked ->

            val data = selectionAdapter.data
            mVodBean.vod_play_list.forEachIndexed { index, playFromBean ->
                    playFromBean.urls.reverse()
            }
            //vod_play_list = vod_play_list.reversed()

//            if (data.size == 1){
//                checkOrder.isChecked =!isChecked
//                return@setOnCheckedChangeListener
//            }

            //data.reverse()

            selectionAdapter.setNewData(mVodBean.vod_play_list[playActivity.playSourceIndex].urls)
            isOrder = !isChecked
            playActivity.changeSelection(urlIndex, true)

        }

        tvSameActor.setOnClickListener {
            if (curType != 1) {
                curType = 1
                tvSameType.setTextColor(ColorUtils.getColor(R.color.gray_999))
                tvSameActor.setTextColor(ColorUtils.getColor(R.color.colorPrimary))

                getSameActorData()
            }
        }

        rlComment.setOnClickListener {
            if (UserUtils.isLogin()) {

                CommentDialog(mActivity, "讨论")
                        .setOnCommentSubmitClickListener(object : CommentDialog.OnCommentSubmitClickListener {
                            override fun onCommentSubmit(comment: String) {
                                commitComment(comment)
                            }
                        })
                        .show()
            } else {
                LoginActivity.start()
            }

        }
        Log.d("hhhh1", "嘿嘿${selectionAdapter.data.size}")
    }

    private fun sendDanmu(content: String, str: String) { //从com.ruifenglb.www.ui.play.NewPlayActivity复制过来的
        if (content.isEmpty()) { //com.ruifenglb.av.play.AvVideoController下有个showDanmaku()方法可以拉起原来的弹幕输入框
            ToastUtils.showShort("请输入弹幕！")
            return
        }
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        //上传弹幕所需参数？
        Log.d("弹幕内容", "onSuccess: $content")
        Log.d("视频ID", "onSuccess: " + mVodBean.vod_id.toString())
        Log.d("这个是啥", "onSuccess: " + System.currentTimeMillis().toString())

        RequestManager.execute(  //这个函数可能有问题
                mActivity,
                vodService.sendDanmu(content, mVodBean.vod_id.toString(), str,str),
                object : BaseObserver<GetScoreBean>() {
                    override fun onSuccess(data: GetScoreBean) {
                        if (data.score != "0") {
                            Utils.runOnUiThread {
                                ToastUtils.showShort("发送弹幕成功，获得${data.score}积分")
                            }
                        }
                    }

                    override fun onError(e: ResponseException) {
                        Utils.runOnUiThread {
                            ToastUtils.showShort(e.getErrorMessage())
                        }
                    }
                })
    }

    private fun scrollCurIndex(rvLastest: RecyclerView) {

        rvLastest.smoothScrollToPosition(urlIndex)
        val mLayoutManager = rvLastest.layoutManager as LinearLayoutManager
        mLayoutManager.scrollToPositionWithOffset(urlIndex, 0)


    }

    override fun initListener() {
        super.initListener()

        refreshLayout.setOnLoadMoreListener {
            curCommentPage++
            getCommentList()

        }
    }

    private fun collection() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }

        RequestManager.execute(this,
                vodService.collect(1.toString(), mVodBean.vod_id.toString(), 2.toString()),
                object : BaseObserver<String>() {
                    override fun onSuccess(data: String) {
                        ToastUtils.showShort("已收藏")
                        val drawable = mActivity.getDrawable(R.drawable.ic_collected2)
                        isCollected = true
                        drawable?.setBounds(0, 0, drawable.minimumWidth,
                                drawable.minimumHeight)
                        headerView.findViewById<TextView>(R.id.item_tv_playinfo_collect)
                                .apply {
                                    setCompoundDrawables(null, drawable, null, null)
                                    text = "已收藏"
                                }
                    }

                    override fun onError(e: ResponseException) {

                    }

                })
    }

    private fun uncollection() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(this,
                vodService.deleteCollect(mVodBean.vod_id.toString(), 2.toString()),
                object : BaseObserver<String>() {
                    override fun onSuccess(data: String) {
                        ToastUtils.showShort("取消成功")
                        isCollected = false
                        val drawable = mActivity.getDrawable(R.drawable.ic_collection2)
                        drawable?.setBounds(0, 0, drawable.minimumWidth,
                                drawable.minimumHeight)
                        headerView.findViewById<TextView>(R.id.item_tv_playinfo_collect)
                                .apply {
                                    setCompoundDrawables(null, drawable, null, null)
                                    text = "收藏"
                                }
                    }

                    override fun onError(e: ResponseException) {
                    }

                }
        )
    }

    private fun getCollectionState() {
        if (UserUtils.isLogin()) {
            val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
            if (AgainstCheatUtil.showWarn(vodService)) {
                return
            }
            RequestManager.execute(this,
                    vodService.getCollectList(1.toString(), 100.toString(), 2.toString()),
                    object : BaseObserver<Page<CollectionBean>>() {
                        override fun onSuccess(data: Page<CollectionBean>) {
                            for (bean in data.list) {
                                if (bean.data.id == mVodBean.vod_id) {
                                    isCollected = true
                                    break
                                }
                            }
                            if (isCollected) {
                                val drawable = mActivity.getDrawable(R.drawable.ic_collected2)
                                drawable?.setBounds(0, 0, drawable.minimumWidth,
                                        drawable.minimumHeight)
                                headerView.findViewById<TextView>(R.id.item_tv_playinfo_collect)
                                        .apply {
                                            setCompoundDrawables(null, drawable, null, null)
                                            text = "已收藏"
                                        }
                            } else {
                                val drawable = mActivity.getDrawable(R.drawable.ic_collection2)
                                drawable?.setBounds(0, 0, drawable.minimumWidth,
                                        drawable.minimumHeight)
                                headerView.findViewById<TextView>(R.id.item_tv_playinfo_collect)
                                        .apply {
                                            setCompoundDrawables(null, drawable, null, null)
                                            text = "收藏"
                                        }
                            }

                        }

                        override fun onError(e: ResponseException) {

                        }

                    })
        }
    }

    private fun score() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        ScoreDialog(mActivity)
                .setOnScoreSubmitClickListener(object : ScoreDialog.OnScoreSubmitClickListener {
                    override fun onScoreSubmit(scoreDialog: ScoreDialog, score: Float) {
                        if (score == 0f) {
                            ToastUtils.showShort("评分不能为空!")
                        } else {
                            scoreDialog.dismiss()
                            val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
                            if (AgainstCheatUtil.showWarn(vodService)) {
                                return
                            }
                            RequestManager.execute(
                                    this@VideoDetailFragment,
                                    vodService.score(mVodBean.vod_id.toString(), score.toString()),
                                    object : BaseObserver<GetScoreBean>() {
                                        override fun onSuccess(data: GetScoreBean) {
                                            if (data.score != "0") {
                                                ToastUtils.showShort("评分成功，获得${data.score}积分")
                                            }
                                        }

                                        override fun onError(e: ResponseException) {
                                        }
                                    }
                            )
                        }
                    }
                })
                .show()
    }

    private fun commitComment(commentContent: String) {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }

        RequestManager.execute(this,
                vodService.comment(commentContent, 1.toString(), mVodBean.vod_id.toString()),
                object : BaseObserver<GetScoreBean>() {
                    override fun onSuccess(data: GetScoreBean) {
                        if (data.score == "0") {
                            ToastUtils.showShort("评论成功")
                        } else {
                            ToastUtils.showShort("评论成功,获得${data.score}积分")
                        }
                        curCommentPage = 1
                        getCommentList(true)
                    }

                    override fun onError(e: ResponseException) {

                    }

                })
    }

    private fun replayComment(commentContent: String, commentId: String, commentPid: String) {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(this,
                vodService.replayComment(commentContent, 1.toString(), mVodBean.vod_id.toString(), commentId, commentPid),
                object : BaseObserver<String>() {
                    override fun onSuccess(data: String) {

                    }

                    override fun onError(e: ResponseException) {

                    }

                })
    }

    private fun getCommentList(isFresh: Boolean = false) {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(this,
                vodService.getCommentList(mVodBean.vod_id, 1.toString(), curCommentPage, 10),
                object : BaseObserver<Page<CommentBean>>() {
                    override fun onSuccess(data: Page<CommentBean>) {

                        if (curCommentPage == 1) {
                            if (isFresh)
                                commentAdapter.setNewData(data.list)
                            else
                                commentAdapter.addData(data.list)
                        }

                        if (curCommentPage > 1) {
                            commentAdapter.addData(data.list)
                            if (refreshLayout != null) {
                                if (data.list.isEmpty()) {
                                    refreshLayout.finishLoadMoreWithNoMoreData()
                                } else {
                                    refreshLayout.finishLoadMore(true)
                                }
                            }
                        }
                    }

                    override fun onError(e: ResponseException) {
                        if (curCommentPage > 1 && refreshLayout != null) {
                            refreshLayout.finishLoadMore(false)
                        }
                    }

                })
    }

    private fun getSameTypeData() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }

        RequestManager.execute(this,
                vodService.getSameTypeList(mVodBean.type_id, mVodBean.vod_class, curSameTypePage, 3),
                object : BaseObserver<Page<VodBean>>() {
                    override fun onSuccess(data: Page<VodBean>) {
                        if (data.list.isNotEmpty()) {
                            curSameTypePage++
                            recommendAdapter.setNewData(data.list)
                        }
                    }

                    override fun onError(e: ResponseException) {
                    }

                })
    }

    private fun getSameActorData() {
        val vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(this,
                vodService.getSameActorList(mVodBean.type_id, mVodBean.vod_actor, curSameActorPage, 3),
                object : BaseObserver<Page<VodBean>>() {
                    override fun onSuccess(data: Page<VodBean>) {
                        if (data.list.isNotEmpty()) {
                            recommendAdapter.setNewData(data.list)
                            curSameActorPage++
                        }
                    }

                    override fun onError(e: ResponseException) {

                    }

                })
    }

    fun changePlaysource(playSourceIndex: Int) {
        tlPlaySource.getTabAt(playSourceIndex)?.select()

    }

    private class CommentAdapter : BaseQuickAdapter<CommentBean, BaseViewHolder>(R.layout.item_hot_comment) {
        override fun convert(helper: BaseViewHolder, item: CommentBean?) {

            helper.let {
                item?.run {
                    ///////////////////////QQ7242484///////////////////////////////////////
                    if (Data.getQQ() == "暗夜紫") {
                        it.setTextColor(R.id.tvTime,ColorUtils.getColor(R.color.gray_999))
                        it.setTextColor(R.id.tvUser,ColorUtils.getColor(R.color.white))
                        it.setTextColor(R.id.tvComment,ColorUtils.getColor(R.color.white))

                    }
                    if (Data.getQQ() == "原始蓝") {
                        it.setTextColor(R.id.tvTime,ColorUtils.getColor(R.color.gray_999))
                        it.setTextColor(R.id.tvUser,ColorUtils.getColor(R.color.textColor))
                        it.setTextColor(R.id.tvComment,ColorUtils.getColor(R.color.textColor))
                    }
                    //////////////////////QQ7242484///////////////////////////////////////
                    it.setText(R.id.tvUser, comment_name)
                    it.setText(R.id.tvTime, TimeUtils.millis2String(comment_time * 1000))
                    it.setText(R.id.tvComment, comment_content)

                    val ivAvatar = it.getView<ImageView>(R.id.ivAvatar)
                    if (user_portrait.isNotEmpty()) {
                        Glide.with(helper.convertView)
                                .load(ApiConfig.MOGAI_BASE_URL + "/" + user_portrait)
                                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                                .into(ivAvatar)
                    } else {
                        Glide.with(helper.convertView)
                                .load(R.drawable.ic_default_avator)
                                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                                .into(ivAvatar)
                    }
                }
            }
        }


    }

    private class RecommendAdapter : BaseQuickAdapter<VodBean, BaseViewHolder>(R.layout.item_card_child) {

        override fun convert(helper: BaseViewHolder, item: VodBean) {
            helper.setVisible(R.id.item_tv_card_child_tip, false)
            helper.setText(R.id.item_tv_card_child_title, item.vodName)
            helper.setText(R.id.item_tv_card_child_up_title, item.vodRemarks)
            val img = item.vod_pic
            ///////////////////////QQ7242484///////////////////////////////////////
            ///////////////////////QQ7242484///////////////////////////////////////
            if (Data.getQQ() == "暗夜紫") {
                helper.setTextColor(R.id.item_tv_card_child_title, ColorUtils.getColor(R.color.white))

            }
            if (Data.getQQ() == "原始蓝") {
                helper.setTextColor(R.id.item_tv_card_child_title, ColorUtils.getColor(R.color.textColor))

            }

            //////////////////////QQ7242484///////////////////////////////////////
            //////////////////////QQ7242484///////////////////////////////////////

            val icon = helper.getView<ImageView>(R.id.item_iv_card_child_icon)
            val lp = icon.layoutParams
            val perWidth = (getScreenWidth(LitePalApplication.getContext()) - dp2px(LitePalApplication.getContext(), 4f)) / 3
            lp.height = (perWidth * 1.4f).toInt()
            icon.layoutParams = lp

            val multiTransformation = MultiTransformation(CenterInside(), RoundedCornersTransformation(15, 8, RoundedCornersTransformation.CornerType.ALL))

            Glide.with(helper.itemView.context)
                    .load(img)
                    .thumbnail(1.0f)
                    .apply(RequestOptions.bitmapTransform(multiTransformation))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(icon)
        }
    }

    inner class SelectionAdapter : BaseQuickAdapter<UrlBean, BaseViewHolder>(R.layout.item_video_source) {

        override fun convert(helper: BaseViewHolder, item: UrlBean) {

            if (mVodBean.type_id == 3) {
                helper.itemView.layoutParams = helper.itemView.layoutParams.apply {
                    width = ViewGroup.MarginLayoutParams.WRAP_CONTENT //ConvertUtils.dp2px(130f)
                    height = ConvertUtils.dp2px(50f)
                }
            } else {
                helper.itemView.layoutParams = helper.itemView.layoutParams.apply {
                    width = ViewGroup.MarginLayoutParams.WRAP_CONTENT//ConvertUtils.dp2px(50f)
                    height = ConvertUtils.dp2px(50f)
                }
            }

            val position = helper.layoutPosition
//            if (isOrder) {

            if (position == urlIndex) {
                helper.setTextColor(R.id.tv, ColorUtils.getColor(R.color.userTopBg))
                ///////////////////////QQ7242484///////////////////////////////////////
                if (Data.getQQ() == "暗夜紫") {
                    helper.setBackgroundRes(R.id.tv, R.drawable.bg_video_sourceahz)

                }
                if (Data.getQQ() == "原始蓝") {
                    helper.setBackgroundRes(R.id.tv, R.drawable.bg_video_source)

                }
                //////////////////////QQ7242484///////////////////////////////////////
            } else {
                helper.setTextColor(R.id.tv, ColorUtils.getColor(R.color.gray_999))
                ///////////////////////QQ7242484///////////////////////////////////////
                if (Data.getQQ() == "暗夜紫") {
                    helper.setBackgroundRes(R.id.tv, R.drawable.bg_video_sourceahz)

                }
                if (Data.getQQ() == "原始蓝") {
                    helper.setBackgroundRes(R.id.tv, R.drawable.bg_video_source)

                }
                //////////////////////QQ7242484///////////////////////////////////////
            }

//            } else {
//                var indexPostion = 0
//                if (urlIndex == 0) {
//                    indexPostion = urlIndex
//                } else {
//                    indexPostion = selectionAdapter.data.size - 1 - urlIndex
//                }
//                if (position == indexPostion) {
//                    helper.setTextColor(R.id.tv, ColorUtils.getColor(R.color.userTopBg))
//                } else {
//                    helper.setTextColor(R.id.tv, ColorUtils.getColor(R.color.gray_999))
//                }
//            }
            val name = item.name.replace("第", "").replace("集", "")
            helper.setText(R.id.tv, name)

        }
    }

    companion object {
        const val VOD_BEAN = "vodBean"

        const val URL_INDEX = "urlIndex"

        const val PLAY_SOURCE_INDEX = "playInfoIndex"

        fun newInstance(vodBean: VodBean, urlIndex: Int, playSourceIndex: Int): VideoDetailFragment = VideoDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(VOD_BEAN, vodBean)
                putInt(URL_INDEX, urlIndex)
                putInt(PLAY_SOURCE_INDEX, playSourceIndex)
            }
        }
    }


    private fun startCache() {
        val bottomSheetDialog = activity?.let {

            BottomSheetDialog(it)
        }
        val view: View = LayoutInflater.from(activity).inflate(R.layout.cache_all_list_layout, null)
        bottomSheetDialog?.setContentView(view)
        bottomSheetDialog?.window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        if (vod_play_list.isEmpty()) {
//            Toast.makeText(activity, "正在请求数据，请稍后", Toast.LENGTH_SHORT).show()
//            return
//        }

        val cacheItem: ArrayList<Square> = ArrayList()
        if (vod_play_list.isNotEmpty()) {
            val playInfoBean = playActivity.playFrom

            val urlS = playInfoBean.urls
            if (urlS.isNotEmpty()) {
                for (index in urlS.indices) {
                    val urlBean = urlS[index]
                    val square = Square(urlBean.name) {
                        val downloadTitle = "${mVodBean.vodName}\t${urlBean.name}"
                        Log.e("TAG", "" + urlBean.url)
                        if (urlBean.url.contains(".m3u8?") ||urlBean.url.endsWith(".m3u8") ) {
                            val imageView = it.findViewById<ImageView>(R.id.status_tag)
                            imageView.setVisibility(View.VISIBLE)
                            imageView.setImageResource(R.drawable.ic_cache_down)
                            Toast.makeText(activity, "开始缓存第${urlBean.name}集", Toast.LENGTH_SHORT).show()
                            // 三个参数 下载地址 标题  封面图片
                            DownloadPresenter.addM3u8Task(activity, urlBean.url, downloadTitle, mVodBean.vod_pic)
                        } else {
                            var iscache = false;
                            if (!isParse) {
                                it.isSelected = true
                                val imageView = it.findViewById<ImageView>(R.id.status_tag)
                                imageView.setVisibility(View.VISIBLE)
                                imageView.setImageResource(R.drawable.ic_cache_down)
                                isParse = true

                                // 链接转换
                                val parse = playInfoBean.player_info.parse2
                                JieXiUtils2.INSTANCE.getPlayUrl(parse, urlBean.url, curParseIndex, object : BackListener {
                                    override fun onSuccess(url: String, curParseIndex: Int) {
                                        isParse = false
                                        LogUtils.eTag("TAG", "onSuccess: curParseIndex =  $curParseIndex url=${url}")
                                        url.let {
                                            if (url.endsWith(".m3u8") || url.contains(".m3u8?")) {
                                                if (!iscache) {
                                                    iscache = true
                                                    Toast.makeText(activity, "开始缓存${urlBean.name}", Toast.LENGTH_SHORT).show()
                                                    // 三个参数 下载地址 标题  封面图片
                                                    DownloadPresenter.addM3u8Task(activity, it, downloadTitle, mVodBean.vod_pic)
                                                }

                                            } else {
                                                ToastUtils.showLong("当前线路不支持缓存...")
                                            }
                                        }

                                    }

                                    override fun onError() {
                                        isParse = false
                                        ToastUtils.showLong("解析失败，请尝试切换线路缓存")
                                    }

                                    override fun onProgressUpdate(msg: String?) {

                                    }
                                }, curFailIndex)
                            } else {
                                ToastUtils.showLong("请等待上一个解析完在缓存")
                            }
                        }
                    }

                    square.isSelected = false
                    square.finished = false
                    val info: List<M3u8DownloadingInfo> = DownloadPresenter.getM3u8DownLoading(urlS[index].url)
                    if (info.isNotEmpty()) {
                        //正在下载中
                        square.isSelected = true
                    }
                    val doneInfos: List<M3u8DoneInfo> = DownloadPresenter.getM3u8Done(urlS[index].url)
                    if (doneInfos.isNotEmpty()) {
                        //已下载完成
                        square.isSelected = false
                        square.finished = true
                    }
                    cacheItem.add(square)
                }
                val selectedSet = TreeSet<Int>()
                val multiTypeAdapter = MultiTypeAdapter()
                multiTypeAdapter.register(Square::class.java, SquareViewBinder(selectedSet))
                val cacheItems = ArrayList<Any?>()
                cacheItems.addAll(cacheItem)
                multiTypeAdapter.items = cacheItems
                val allList: RecyclerView = view.findViewById(R.id.all_list)
                val title = view.findViewById<TextView>(R.id.title)
                // 查看下载
                val downCenter = view.findViewById<TextView>(R.id.down_center)
                downCenter.setOnClickListener {
                    //进入下载界面
                    activity?.let { it1 -> AllDownloadActivity.start(it1) }
                    bottomSheetDialog?.dismiss()
                }
                title.text = "缓存剧集"
                val close = view.findViewById<ImageView>(R.id.close)
                val gridLayoutManager = GridLayoutManager(activity, 3)
                gridLayoutManager.orientation = GridLayoutManager.VERTICAL
                allList.addItemDecoration(GridItemDecoration(activity, R.drawable.grid_item_decor))
                allList.layoutManager = gridLayoutManager
                allList.adapter = multiTypeAdapter
                bottomSheetDialog?.show()
                close.setOnClickListener {
                    bottomSheetDialog?.dismiss()
                }
            }
        }
    }

}
