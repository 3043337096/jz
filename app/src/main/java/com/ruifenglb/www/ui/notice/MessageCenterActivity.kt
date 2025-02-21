package com.ruifenglb.www.ui.notice

import android.app.Activity
import android.graphics.Rect
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.MessageBean
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.DensityUtils
import com.ruifenglb.www.utils.Retrofit2Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.ruifenglb.www.base.observer.LoadingObserver
import com.blankj.utilcode.util.ColorUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_message_center.*
import kotlinx.android.synthetic.main.fragment_user.*

class MessageCenterActivity : BaseActivity(), View.OnClickListener {

    private val msgAdapter by lazy {
        MsgAdapter(this@MessageCenterActivity)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_message_center
    }

    override fun initView() {
        super.initView()

        rvMsg.layoutManager = LinearLayoutManager(mActivity)
        rvMsg.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val paddingTop = DensityUtils.dp2px(application, 5f)
                val paddingLeft = DensityUtils.dp2px(application, 15f)
                outRect.set(paddingLeft, paddingTop, paddingLeft, paddingTop)
            }
        })
        rvMsg.adapter = msgAdapter

///////////////////////////////////////////////////////////////////////////////////////







        if (Data.getQQ() == "°µÒ¹×Ï") {
            gonggaobj.setBackgroundColor(ColorUtils.getColor(R.color.xkh));
            rvMsg.setBackgroundColor(ColorUtils.getColor(R.color.xkh));

        }
        if (Data.getQQ() == "Ô­Ê¼À¶") {
            gonggaobj.setBackgroundResource(R.drawable.shape_bg_orange_to_light_top)
            rvMsg.setBackgroundColor(ColorUtils.getColor(R.color.white));
        }



































        //////////////////////////////////////////////////////////////////////////////////////////////////
        rlBack.setOnClickListener(this)
        rl_msg_center.setOnClickListener(this)
        //rl_system_notice.setOnClickListener(this)
    }

    override fun initData() {
        super.initData()
        getMsgList()
    }

    private fun getMsgList() {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(mActivity, vodService.getMsgList(),
                object : LoadingObserver<MessageBean>(mActivity) {
                    override fun onSuccess(data: MessageBean) {
                        val list = data.list
                        msgAdapter.setNewData(list)
                    }

                    override fun onError(e: ResponseException) {
                    }

                })
    }


    override fun onClick(v: View?) {
        when (v) {
            rlBack -> {
                finish()
            }
            rl_msg_center -> {

            }

        }
    }

    class MsgAdapter(var activity: Activity) : BaseQuickAdapter<MessageBean.ListBean, BaseViewHolder>(R.layout.item_msg_list) {
        override fun convert(helper: BaseViewHolder, item: MessageBean.ListBean?) {
            item?.run {
                helper.setText(R.id.tv_title,this.title )
                helper.setText(R.id.tv_desc, this.content)
                helper.setText(R.id.tv_time, this.create_date)

                helper.getView<ConstraintLayout>(R.id.total_view).setOnClickListener {
                    MessageDetailActivity.start(activity,this.id.toString())
                }
            }
        }

    }


}