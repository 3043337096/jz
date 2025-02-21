package com.ruifenglb.www.ui.withdraw

import com.ruifenglb.www.MainActivity
import com.ruifenglb.www.R
import com.ruifenglb.www.banner.Data
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.download.SPUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import jaygoo.library.m3u8downloader.M3U8Library.context
import kotlinx.android.synthetic.main.activity_coin_withdraw_skin.*

class GoldWithdrawActivitySkin : BaseActivity() {

    override fun getLayoutResID(): Int {
        return R.layout.activity_coin_withdraw_skin
    }

    override fun initView() {
        super.initView()

        xingkong.setBackground(getResources().getDrawable(R.drawable.xingkonghui))
        dyl.setBackground(getResources().getDrawable(R.drawable.lanbai))

        if(Data.getQQ().equals("暗夜紫")) {
            xingkong.setBackground(getResources().getDrawable(R.drawable.xingkonghui))
            b11g.setBackgroundColor(ColorUtils.getColor(R.color.xkh))
            t88.setBackground(getResources().getDrawable(R.drawable.xingkonghui1))
        }  else if(Data.getQQ().equals("原始蓝")) {
            dyl.setBackground(getResources().getDrawable(R.drawable.lanbai))
            b11g.setBackgroundColor(ColorUtils.getColor(R.color.ls))
            t100.setBackground(getResources().getDrawable(R.drawable.lanbai1))
        }








    }



    override fun initListener() {
        super.initListener()
        iv_login_back.setOnClickListener {
            finish()}


        mmm7.setOnClickListener {
            Data.setB("2")
            Data.setYS("2")
            SPUtils.setString(context,"why","暗夜紫")
            Data.setQQ("暗夜紫")

            xingkong.setBackground(getResources().getDrawable(R.drawable.xingkonghui))
            dyl.setBackground(getResources().getDrawable(R.drawable.lanbai))

            b11g.setBackgroundColor(ColorUtils.getColor(R.color.xkh))

            t88.setBackground(getResources().getDrawable(R.drawable.xingkonghui1))
            t100.setBackground(getResources().getDrawable(R.drawable.uiguan))
        }

        mmm8.setOnClickListener {
            Data.setB("2")
            Data.setYS("2")
            SPUtils.setString(context,"why","原始蓝")
            Data.setQQ("原始蓝")

            xingkong.setBackground(getResources().getDrawable(R.drawable.xingkonghui))
            dyl.setBackground(getResources().getDrawable(R.drawable.lanbai))

            b11g.setBackgroundColor(ColorUtils.getColor(R.color.ls))

            t88.setBackground(getResources().getDrawable(R.drawable.uiguan))
            t100.setBackground(getResources().getDrawable(R.drawable.lanbai1))
        }
        qd.setOnClickListener{
            if(Data.getQQ().equals("原始蓝")) {
                SPUtils.setString(context,"why","原始蓝")


                ActivityUtils.startActivity(MainActivity::class.java)

            }
        }

    }


}




