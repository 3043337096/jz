package com.ruifenglb.www.ui.expand

import android.app.Activity
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.MyExpand
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.DateUtil
import com.ruifenglb.www.utils.DensityUtils
import com.ruifenglb.www.utils.Retrofit2Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.ruifenglb.www.base.observer.LoadingObserver
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_my_expand.*

class MyExpandActivity : BaseActivity(), View.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    private var mPage = 1;
    private var isRefresh = true
    private var mDataList = ArrayList<MyExpand.ListBean>()

    private val msgAdapter by lazy {
        ExpandAdapter(this@MyExpandActivity)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_my_expand
    }

    override fun initView() {
        super.initView()





        if (Data.getQQ() == "暗夜紫") {
            uun.setBackgroundResource(R.color.xkh)
            uum.setBackgroundResource(R.color.xkh)
        }
        if (Data.getQQ() == "原始蓝") {
            uun.setBackgroundResource(R.color.ls)
            uum.setBackgroundResource(R.color.ls)
        }


















































        refreshLayout.setOnLoadMoreListener(this)
        refreshLayout.setOnRefreshListener(this)
        rvExpand.layoutManager = LinearLayoutManager(mActivity)
        rvExpand.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val paddingLeft = DensityUtils.dp2px(application, 5f)
                outRect.set(paddingLeft, 0, paddingLeft, 0)
            }
        })
        rvExpand.adapter = msgAdapter


        rlBack.setOnClickListener(this)
    }

    override fun initData() {
        super.initData()
        getExpandList()
    }

    private fun getExpandList() {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return
        }
        RequestManager.execute(mActivity, vodService.myExpand(mPage.toString(), "20"),
                object : LoadingObserver<MyExpand>(mActivity) {
                    override fun onSuccess(data: MyExpand) {
                        refreshLayout.finishLoadMore()
                        refreshLayout.finishRefresh()
                        val list = data.list
                        if (list.size < 20) {
                            refreshLayout.setNoMoreData(true)
                        } else {
                            refreshLayout.setNoMoreData(false)
                        }
                        if (isRefresh) {
                            mDataList.clear()
                        }
                        mDataList.addAll(list)
                        msgAdapter.setNewData(mDataList)
                        tv_total.text = data.total.toString() + "人"
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
        }
    }

    class ExpandAdapter(var activity: Activity) : BaseQuickAdapter<MyExpand.ListBean, BaseViewHolder>(R.layout.item_my_expand) {
        override fun convert(helper: BaseViewHolder, item: MyExpand.ListBean?) {
            item?.run {
                helper.setText(R.id.tv_name, this.user_nick_name)
                helper.setText(R.id.tv_id, this.user_id.toString())
                helper.setText(R.id.tv_time, DateUtil.getyyyyMMddHHmm(this.user_reg_time * 1000.toLong()))
            }
        }

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isRefresh = false
        mPage++
        getExpandList()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        isRefresh = true
        mPage = 1
        getExpandList()
    }


}