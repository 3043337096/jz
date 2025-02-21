package com.ruifenglb.www.base

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import com.ruifenglb.www.R
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import com.ruifenglb.www.banner.Data
import com.umeng.analytics.MobclickAgent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_playinfo.*
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity() : SupportActivity() {

    private var unbinder: Unbinder? = null
    lateinit var mActivity: BaseActivity

    //画中画权限
    val PICTURE_PERMISSION = arrayOf(
            Manifest.permission.SYSTEM_ALERT_WINDOW
    )

    private val mDisposablePool: CompositeDisposable by lazy {
        CompositeDisposable()
    }
//    protected val vodService : VodService by lazy {
//        Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        setContentView(getLayoutResID())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#E8E8E8")
        }
        mActivity = this
        BaseApplication.setContextRef(this)
        unbinder = ButterKnife.bind(this)
        if (this.javaClass.isAnnotationPresent(BindEventBus::class.java) || isUseEventBus()) {
            EventBus.getDefault().register(this)
        }

        initView()
        initListener()
        initData()
    }


    /**
     * 请求画中画权限
     */
    public open fun requePer(getPermissionSuccess: () -> Unit) {
        //hide the title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (XXPermissions.isHasPermission(this, PICTURE_PERMISSION)) {
            getPermissionSuccess()
            // phoneDevice = AppUtils.getDevice(this);
        } else {
            XXPermissions.with(this) // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                    .constantRequest()
                    .permission(PICTURE_PERMISSION)
                    .request(object : OnPermission {
                        override fun hasPermission(granted: List<String>, isAll: Boolean) {
                            getPermissionSuccess()
                        }

                        override fun noPermission(denied: List<String>, quick: Boolean) {
                            if (quick) {
                                Toast.makeText(this@BaseActivity, "被永久拒绝授权，请手动授予权限", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@BaseActivity, "获取权限失败", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
        }
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    protected abstract fun getLayoutResID(): Int

    protected open fun initView() {

    }

    protected open fun initListener() {

    }

    protected open fun initData() {

    }

    protected open fun isUseEventBus(): Boolean {
        return false
    }


    fun addDisposable(disposable: Disposable) {
        if (mDisposablePool.isDisposed)
            mDisposablePool.add(disposable)
    }

    fun removeDisposable(disposable: Disposable) {
        if (mDisposablePool.isDisposed)
            mDisposablePool.remove(disposable)
    }

    fun detach() {
        if (!mDisposablePool.isDisposed)
            mDisposablePool.clear()
    }


    override fun onDestroy() {
        LogUtils.e("onDestroy:--$this")
        super.onDestroy()
        detach()
        if (unbinder != null) unbinder!!.unbind()
        if (this.javaClass.isAnnotationPresent(BindEventBus::class.java) || isUseEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(0, R.anim.slide_out_right)
    }



}
