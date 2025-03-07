package com.ruifenglb.www.ui.task

import androidx.recyclerview.widget.LinearLayoutManager
import com.ruifenglb.www.R
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.TaskBean
import com.ruifenglb.www.bean.TaskItemBean
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.Retrofit2Utils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.ruifenglb.www.base.observer.LoadingObserver
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : BaseActivity() {
    private val taskAdapter by lazy {
        TaskAdapter()
    }

    override fun getLayoutResID(): Int {
        BarUtils.setStatusBarLightMode(this, true)
        return R.layout.activity_task
    }

    override fun initView() {
        super.initView()
        rvTask.layoutManager = LinearLayoutManager(mActivity)
        rvTask.adapter = taskAdapter
    }



    override fun initData() {
        super.initData()
        getTaskList()
    }

    private fun getTaskList() {
        var vodService= Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(mActivity, vodService.getTaskList(),
                object : LoadingObserver<TaskBean>(mActivity) {
                    override fun onSuccess(data: TaskBean) {
                        val taskItems = ArrayList<TaskItemBean>()
                        taskItems.add(TaskItemBean(data.sign))
                        taskItems.add(TaskItemBean(data.share))
                        taskItems.add(TaskItemBean(data.comment))
                        taskItems.add(TaskItemBean(data.mark))
                        taskItems.add(TaskItemBean(data.danmu))
                        taskAdapter.setNewData(taskItems)
                    }

                    override fun onError(e: ResponseException) {
                    }

                })
    }

    class TaskAdapter : BaseQuickAdapter<TaskItemBean, BaseViewHolder>(R.layout.item_task) {
        override fun convert(helper: BaseViewHolder, item: TaskItemBean?) {
            item?.run {
                helper.setText(R.id.item_tv_task_t1, item.title)
                helper.setText(R.id.item_tv_task_t2, item.info)
                helper.setText(R.id.item_tv_task_t3, "+${item.points}分")

            }
        }

    }

    companion object {
        fun start() {
            ActivityUtils.startActivity(TaskActivity::class.java, R.anim.slide_in_right, R.anim.no_anim)
        }
    }
}
