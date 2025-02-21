package com.ruifenglb.www.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ruifenglb.www.R
import com.ruifenglb.www.base.BaseActivity

/**
 * 展示协议界面
 */
class AgreementActivity : BaseActivity() {

    companion object {
        /**
         * agreementType协议类型  1=用户使用协议  2=用户隐私政策
         */
        fun startAgreementActivity(context: Context, agreementType: Int) {
            var intent = Intent(context, AgreementActivity::class.java)
            intent.putExtra("agreementType", agreementType)
            context.startActivity(intent)
        }
    }

    override fun getLayoutResID() = R.layout.activity_agreement

    override fun initView() {
        var agreementType = intent.getIntExtra("agreementType", 1)

        findViewById<TextView>(R.id.tv_title).text = if (agreementType == 1) {
            "用户使用协议"
        } else {
            "用户隐私政策"
        }

        findViewById<TextView>(R.id.tvAgreement).text = if (agreementType == 1) {
            resources.getString(R.string.userAgreement).replace("用户","12345")
        } else {
            resources.getString(R.string.userYSAgreement)
        }
        findViewById<TextView>(R.id.tvAgreement).setMovementMethod(ScrollingMovementMethod.getInstance());

        findViewById<ImageView>(R.id.iv_av_back).setOnClickListener { finish() }
    }



}