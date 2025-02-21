package com.ruifenglb.www.ui.task

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.OpenRecommendEvent
import com.ruifenglb.www.bean.OpenShareEvent
import com.ruifenglb.www.bean.TaskBean
import com.ruifenglb.www.bean.TaskItemBean
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.DensityUtils
import com.ruifenglb.www.utils.Retrofit2Utils
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.ruifenglb.www.base.observer.LoadingObserver
import com.blankj.utilcode.util.ColorUtils
import kotlinx.android.synthetic.main.activity_expand_center.*
import kotlinx.android.synthetic.main.activity_task2.*
import kotlinx.android.synthetic.main.activity_task2.ivBack
import org.greenrobot.eventbus.EventBus

class TaskActivity2 : BaseActivity(), View.OnClickListener {
    private val taskAdapter by lazy {
        TaskAdapter(this@TaskActivity2)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_task2
    }

    override fun initView() {
        super.initView()
        rvTask.layoutManager = LinearLayoutManager(mActivity)
        rvTask.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val paddingTop = DensityUtils.dp2px(application, 5f)
                val paddingLeft = DensityUtils.dp2px(application, 15f)
                outRect.set(paddingLeft, paddingTop, paddingLeft, paddingTop)
            }
        })
        rvTask.adapter = taskAdapter






        /////////////////////////////////////



        if (Data.getQQ() == "暗夜紫") {
            uuvv.setBackgroundColor(ColorUtils.getColor(R.color.xkh));
            uuv.setBackgroundColor(ColorUtils.getColor(R.color.xkh));
            rvTask.setBackgroundColor(ColorUtils.getColor(R.color.xkh));

        }
        if (Data.getQQ() == "原始蓝") {
            uuvv.setBackgroundColor(ColorUtils.getColor(R.color.ls));
            uuv.setBackgroundColor(ColorUtils.getColor(R.color.ls));
            rvTask.setBackgroundColor(ColorUtils.getColor(R.color.ivory));
        }








































        ////////////////////////////////////////////////////////////
        rl_day_task.setOnClickListener(this)
        rl_more_task.setOnClickListener(this)
    }

    override fun initListener() {
        super.initListener()
        ivBack.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        super.initData()
        getTaskList()
    }

    private fun getTaskList() {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(mActivity, vodService.getTaskList(),
                object : LoadingObserver<TaskBean>(mActivity) {
                    override fun onSuccess(data: TaskBean) {
                        val taskItems = ArrayList<TaskItemBean>()
                        taskItems.add(TaskItemBean(data.sign))
                        taskItems.add(TaskItemBean(data.mark))
                        taskItems.add(TaskItemBean(data.danmu))
                        taskItems.add(TaskItemBean(data.comment))
                        taskItems.add(TaskItemBean(data.view30m))
                        taskItems.add(TaskItemBean(data.share))
                        taskAdapter.setNewData(taskItems)
                    }

                    override fun onError(e: ResponseException) {
                    }

                })
    }


    class TaskAdapter(var activity: Activity) : BaseQuickAdapter<TaskItemBean, BaseViewHolder>(R.layout.item_task2) {
        override fun convert(helper: BaseViewHolder, item: TaskItemBean?) {
            item?.run {
                helper.setText(R.id.tv_name, item.title)
                helper.setText(R.id.tv_num, "+${item.points}")
                helper.getView<ImageView>(R.id.iv_image).setImageResource(item.image)
                val rl_do = (helper.getView(R.id.rl_status) as RelativeLayout)

                if (item.finish == 1) {
                    helper.getView<TextView>(R.id.tv_gotodo).visibility = View.GONE
                    helper.getView<TextView>(R.id.tv_done).visibility = View.VISIBLE

                    rl_do.setOnClickListener(null)
                } else {
                    helper.getView<TextView>(R.id.tv_gotodo).visibility = View.VISIBLE
                    helper.getView<TextView>(R.id.tv_done).visibility = View.GONE

                    rl_do.setTag(R.id.rl_status, item.id)
                    rl_do.setOnClickListener {
                        when (it.getTag(R.id.rl_status)) {
                            1 -> {
                                activity.finish()
                            }
                            2 -> {
                                activity.finish()
                                EventBus.getDefault().postSticky(OpenRecommendEvent())
                            }
                            3 -> {
                                activity.finish()
                                EventBus.getDefault().postSticky(OpenRecommendEvent())
                            }
                            4 -> {
                                activity.finish()
                                EventBus.getDefault().postSticky(OpenRecommendEvent())
                            }
                            5 -> {
                                activity.finish()
                                EventBus.getDefault().postSticky(OpenRecommendEvent())
                            }
                            6 -> {
                                activity.finish()

                            }
                        }
                    }
                }
            }
        }

    }

    companion object {
        fun start() {
            ActivityUtils.startActivity(TaskActivity2::class.java, R.anim.slide_in_right, R.anim.no_anim)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            rl_day_task -> {
            }
            rl_more_task -> {
//                finish()
//                EventBus.getDefault().postSticky(OpenShareEvent())
                Toast.makeText(this, "没有福利任务", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
