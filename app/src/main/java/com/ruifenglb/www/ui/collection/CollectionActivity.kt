package com.ruifenglb.www.ui.collection

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.CollectionBean
import com.ruifenglb.www.bean.Page
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.ui.login.LoginActivity
import com.ruifenglb.www.ui.play.PlayActivity
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.Retrofit2Utils
import com.ruifenglb.www.utils.UserUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.fragment_user.*

class CollectionActivity : BaseActivity() {
    private var curCollectionPage = 1
    private var isEditMode = false
    private var isAllSelect : Boolean = false

    private val collectionAdapter: CollectionAdapter by lazy {
        CollectionAdapter().apply {
            setOnItemClickListener { adapter, _, position ->
                val item = adapter.getItem(position) as CollectionBean
                if (isEditMode) {
                    item.isSelect = !item.isSelect
                    if(isAllSelect && !item.isSelect){
                        isAllSelect = false
                        tvSelect.text =  "全选"
                    }
                    adapter.data[position] = item
                    notifyItemChanged(position)
                    changeDeleteNum()
                }else{
                    PlayActivity.startByCollection(item.data.id)
                }
            }
        }
    }



    override fun getLayoutResID(): Int {
        return R.layout.activity_collection
    }

    override fun initView() {
        super.initView()

        refreshLayout.setEnableRefresh(false)
        refreshLayout.setRefreshFooter(ClassicsFooter(mActivity))

        rvCollection.layoutManager = LinearLayoutManager(mActivity)
        rvCollection.adapter = collectionAdapter
    }

    override fun initListener() {
        super.initListener()
        tvSelect.setOnClickListener {
            if(isAllSelect){
                collectionAdapter.data.map {
                    it.isSelect = false
                }
                isAllSelect = false
                tvSelect.text =  "全选"
            }else{
                collectionAdapter.data.map {
                    it.isSelect = true
                }
                isAllSelect = true
                tvSelect.text =  "取消全选"
            }
            collectionAdapter.notifyDataSetChanged()
            changeDeleteNum()

        }
        tvSelectCount.setOnClickListener {
            val ids = getSelectCollection()
            if (ids.isNullOrEmpty()) {
                ToastUtils.showShort("未选择任何数据")
            } else {
                deleteCollection(ids)
            }
        }
        ///////////////////////////////////////




        //  ToastUtils.showShort("签到成功");

        if (Data.getQQ() == "暗夜紫") {
            uy.setBackgroundResource(R.color.xkh)
            bj7.setBackgroundResource(R.color.xkh)

        }
        if (Data.getQQ() == "原始蓝") {
            uy.setBackgroundResource(R.color.ls)
            bj7.setBackgroundResource(R.color.white)
        }


































        ////////////////////////////////////
        tvEdit.setOnClickListener {
            isEditMode = !isEditMode
            changeEditMode()
        }

        rlBack.setOnClickListener {
            finish()
        }

        refreshLayout.setOnLoadMoreListener {
            curCollectionPage++
            getCollectionList()
        }
    }

    override fun initData() {
        super.initData()
        getCollectionList()
    }

    private fun getCollectionList(isFresh: Boolean = false) {
        var vodService= Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(this,
                vodService.getCollectList(curCollectionPage.toString(), 10.toString(), 2.toString()),
                object : BaseObserver<Page<CollectionBean>>() {
                    override fun onSuccess(data: Page<CollectionBean>) {
                        if (curCollectionPage == 1) {
                            if (isFresh)
                                collectionAdapter.setNewData(data.list)
                            else
                                collectionAdapter.addData(data.list)
                        }

                        if (curCollectionPage > 1) {
                            collectionAdapter.addData(data.list)
                            if (data.list.isEmpty()) {
                                refreshLayout.finishLoadMoreWithNoMoreData()
                            } else {
                                refreshLayout.finishLoadMore(true)
                            }
                        }
                    }

                    override fun onError(e: ResponseException) {
                        if (curCollectionPage > 1) {
                            refreshLayout.finishLoadMore(false)
                        }
                    }

                })
    }

    private fun deleteCollection(ids: String) {
        var vodService=Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(this,
                vodService.deleteCollect(ids, 2.toString()),
                object : BaseObserver<String>() {
                    override fun onSuccess(data: String) {
                        ToastUtils.showShort("取消成功")
                        isEditMode = false
                        changeEditMode()
                        curCollectionPage = 1
                        getCollectionList(true)
                    }

                    override fun onError(e: ResponseException) {
                    }

                }
        )
    }

    private fun changeEditMode() {
        if (isEditMode) {
            tvEdit.text = "取消"
            breakLine.visibility = View.VISIBLE
            rlEdit.visibility = View.VISIBLE
            collectionAdapter.data.map {
                it.isSelect = false
            }
            isAllSelect = false
            tvSelect.text =  "全选"
            collectionAdapter.notifyDataSetChanged()
        } else {
            tvEdit.text = "编辑"
            breakLine.visibility = View.GONE
            rlEdit.visibility = View.GONE
        }
        collectionAdapter.changeEditMode(isEditMode)
    }

    private fun getSelectCount(): Int {
        var count = 0
        collectionAdapter.data.map {
            if (it.isSelect) {
                count++
            }
        }
        return count
    }


    private fun changeDeleteNum(){
        tvSelectCount.text = "删除(${getSelectCount()})"
    }

    private fun getSelectCollection(): String {
        var ids = ""
        collectionAdapter.data.map {
            if (it.isSelect) {
                ids += "${it.data.id},"
            }
        }
        if (ids.isNotEmpty()) {
            return ids.substring(0, ids.length - 1)
        }
        return ids
    }

    class CollectionAdapter(var isEditMode: Boolean = false) : BaseQuickAdapter<CollectionBean, BaseViewHolder>(R.layout.item_collection) {

        override fun convert(helper: BaseViewHolder, item: CollectionBean?) {
            item?.let {
                helper.setText(R.id.tvName, it.data.name)
                helper.setText(R.id.tvType, it.data.type.type_name)

                val ivAvator = helper.getView<ImageView>(R.id.ivImg)
                if (isEditMode) {
                    helper.setChecked(R.id.cb, item.isSelect)
                }
//                Glide.with(helper.itemView.context)
//                        .load( it.data.pic)
//                        .apply(RequestOptions.bitmapTransform(RoundedCorners(ConvertUtils.dp2px(5f))))
//                        .into(ivAvator)

                //        Glide.with(holder.itemView.getContext())
//                .load(img)
//                .thumbnail(0.1f)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.icon);
                val mation: MultiTransformation<Bitmap> = MultiTransformation(CenterCrop(), RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL))
                Glide.with(helper.itemView.context)
                        .load(it.data.pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(RequestOptions.bitmapTransform(mation))
                        .into(ivAvator)
            }

            if (isEditMode) {
                helper.setGone(R.id.cb, true)
            } else {
                helper.setGone(R.id.cb, false)
            }
        }

        fun changeEditMode(isEditMode: Boolean) {
            this.isEditMode = isEditMode
            notifyDataSetChanged()
        }
    }

    companion object {
        fun start() {
            if (UserUtils.isLogin()) {
                ActivityUtils.startActivity(CollectionActivity::class.java)
            } else {
                ActivityUtils.startActivity(LoginActivity::class.java)
            }
        }
    }
}
