package com.ruifenglb.www.ui.feedback

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.FeedbackBean
import com.ruifenglb.www.bean.Page
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.ui.login.LoginActivity
import com.ruifenglb.www.ui.seek.SeekActivity
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.Retrofit2Utils
import com.ruifenglb.www.utils.UserUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver

import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.fragment_user.*

class FeedbackActivity : BaseActivity() {
    private var curFeedbackPage = 1

    private var commitMsg = ""
    private var isHave = false

    companion object {
        fun start(context: Context, message: String) {
            val intent = Intent(context, FeedbackActivity::class.java)
            intent.putExtra("msg", message)
            intent.putExtra("isHave", true)
            context.startActivity(intent)

        }
    }

    private val feedbackAdapter: FeedbackAdapter by lazy {
        FeedbackAdapter()
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_feedback
    }

    override fun initView() {
        super.initView()
        isHave = intent.getBooleanExtra("isHave", false)
        if (isHave) {
            commitMsg = intent.getStringExtra("msg")
            commitMsg?.let {
                etComment.setText(commitMsg)
            }
        }else if(Data.getYS().equals("2")){
            etComment.setText("小编你好，请添加影片《"+Data.getWhy()+"》谢谢")

            tv_task_title.setText("当前无此影片，请提交求片信息，谢谢")


        }

        //留言求片
        qiupian.setOnClickListener{
            etComment.setText("【求片】")
            qiupian.setBackgroundResource(R.drawable.shape_bg_transparent_rect3)
            jianyi.setBackgroundResource(R.drawable.shape_bg_blue_radius_25dp)
            gegnxing.setBackgroundResource(R.drawable.shape_bg_blue_radius_25dp)

        }
        //功能建议
        jianyi.setOnClickListener{
            etComment.setText("【功能建议】")
            jianyi.setBackgroundResource(R.drawable.shape_bg_transparent_rect3)
            qiupian.setBackgroundResource(R.drawable.shape_bg_blue_radius_25dp)
            gegnxing.setBackgroundResource(R.drawable.shape_bg_blue_radius_25dp)
        }
        //提醒更新
        gegnxing.setOnClickListener{
            etComment.setText("【更新提醒】")
            gegnxing.setBackgroundResource(R.drawable.shape_bg_transparent_rect3)
            qiupian.setBackgroundResource(R.drawable.shape_bg_blue_radius_25dp)
            jianyi.setBackgroundResource(R.drawable.shape_bg_blue_radius_25dp)
        }


        if (Data.getQQ() == "暗夜紫") {
            uuyytt.setBackgroundColor(ColorUtils.getColor(R.color.xkh))
            bj.setBackgroundColor(ColorUtils.getColor(R.color.xkh2))
            leixing.setTextColor(ColorUtils.getColor(R.color.white))
            miaoshu.setTextColor(ColorUtils.getColor(R.color.white))
            shuoshenme.setTextColor(ColorUtils.getColor(R.color.white))

            tvCount.setTextColor(ColorUtils.getColor(R.color.gray_999))
        }
        if (Data.getQQ() == "原始蓝") {
            uuyytt.setBackgroundColor(ColorUtils.getColor(R.color.ls))
            bj.setBackgroundColor(ColorUtils.getColor(R.color.white))
            leixing.setTextColor(ColorUtils.getColor(R.color.textColor))
            miaoshu.setTextColor(ColorUtils.getColor(R.color.textColor))
            shuoshenme.setTextColor(ColorUtils.getColor(R.color.textColor))
            tvCount.setTextColor(ColorUtils.getColor(R.color.gray_999))
        }


        tvSubmit.setOnClickListener {
            val comment = etComment.text.trim().toString()
            if (comment.isEmpty()) {
                ToastUtils.showShort("反馈内容不能为空")
            } else {
                if (UserUtils.isLogin()) {
                    feedback(comment)
                } else {
                    ActivityUtils.startActivity(LoginActivity::class.java)
                }
            }
        }

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setRefreshFooter(ClassicsFooter(mActivity))

        rvFeedback.layoutManager = LinearLayoutManager(mActivity)
        rvFeedback.adapter = feedbackAdapter
    }

    override fun initData() {
        super.initData()
        getFeedbackList()
    }

    override fun initListener() {
        super.initListener()
        refreshLayout.setOnLoadMoreListener {
            curFeedbackPage++
            getFeedbackList()
        }
        rlBack.setOnClickListener {
            finish()
        }
    }

    private fun feedback(commentContent: String) {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(this,
                vodService.feedbak(commentContent),
                object : BaseObserver<String>() {
                    override fun onSuccess(data: String) {

                        if(Data.getYS().equals("2")){
                            ToastUtils.showShort("求片成功，请耐心等待")

                        }else{ ToastUtils.showShort("反馈成功")}
                        curFeedbackPage = 1
                        getFeedbackList(true)
                    }

                    override fun onError(e: ResponseException) {

                    }

                })
    }


    private fun getFeedbackList(isFresh: Boolean = false) {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(this,
                vodService.getFeedbackList(curFeedbackPage.toString(), 10.toString()),
                object : BaseObserver<Page<FeedbackBean>>() {
                    override fun onSuccess(data: Page<FeedbackBean>) {
                        if (curFeedbackPage == 1) {
                            if (isFresh)
                                feedbackAdapter.setNewData(data.list)
                            else
                                feedbackAdapter.addData(data.list)
                        }

                        if (curFeedbackPage > 1) {
                            feedbackAdapter.addData(data.list)
                            if (data.list.isEmpty()) {
                                refreshLayout.finishLoadMoreWithNoMoreData()
                            } else {
                                refreshLayout.finishLoadMore(true)
                            }
                        }
                    }

                    override fun onError(e: ResponseException) {
                        if (curFeedbackPage > 1) {
                            refreshLayout.finishLoadMore(false)
                        }
                    }

                })
    }

    private class FeedbackAdapter : BaseQuickAdapter<FeedbackBean, BaseViewHolder>(R.layout.item_feedback) {
        override fun convert(helper: BaseViewHolder, item: FeedbackBean?) {
            helper.let {
                item?.run {
                    it.setText(R.id.tvUser, gbook_name)
                    it.setText(R.id.tvTime, TimeUtils.millis2String(gbook_time * 1000))
                    it.setText(R.id.tvComment, gbook_content)
                    if (gbook_reply.isNotEmpty()) {
                        it.setGone(R.id.llReplay, true)
                        it.setText(R.id.tvReplay, gbook_reply)
                    } else {
                        it.setGone(R.id.llReplay, false)
                    }
                }
            }
        }


    }

}
