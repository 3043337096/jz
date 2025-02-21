package com.ruifenglb.www.ui.account

import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.bean.LoginBean
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.Retrofit2Utils
import com.blankj.utilcode.util.ToastUtils
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.ruifenglb.www.base.observer.LoadingObserver
import kotlinx.android.synthetic.main.activity_change_nickname.*
import org.greenrobot.eventbus.EventBus

class ChangeNicknameActivity : BaseActivity() {

    override fun getLayoutResID(): Int {
        return R.layout.activity_change_nickname
    }

    override fun initListener() {




        //  ToastUtils.showShort("签到成功");

        if (Data.getQQ() == "暗夜紫") {
            ttrree.setBackgroundResource(R.color.xkh)
        }
        if (Data.getQQ() == "原始蓝") {
            ttrree.setBackgroundResource(R.color.ls)
        }

        super.initListener()
        rlBack.setOnClickListener {
            finish()
        }
        tvFinish.setOnClickListener {
            changeNickname()
        }
    }

    private fun changeNickname() {
        val newNickName = etNickname.text.trim().toString()
        if(newNickName.isEmpty()){
            ToastUtils.showShort(R.string.new_nickname_empty)
            return
        }
        var vodService= Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(this, vodService.changeNickname(newNickName), object : LoadingObserver<String>(mActivity) {
            override fun onSuccess(data: String) {
                ToastUtils.showShort(R.string.change_nickname_success)
                EventBus.getDefault().post(LoginBean())
                finish()
            }

            override fun onError(e: ResponseException) {
            }

        })
    }

}
