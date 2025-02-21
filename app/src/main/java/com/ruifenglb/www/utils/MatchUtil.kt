package com.ruifenglb.www.utils

import android.content.Context
import android.util.Log

import com.ruifenglb.www.download.SPUtils
import com.ruifenglb.www.ui.widget.NoticeDialog2
import java.util.*

import java.util.regex.Pattern

class MatchUtil {

    companion object {
        private const val NoticeKey = "NoticeKey"
        @JvmStatic
        fun checkNotice(context: Context, str: String){
            val time = SPUtils.getLong(context, NoticeKey, 0)
            val pattern = Pattern.compile("(^#(\\d+)#)([\\s\\S]*)")
            val matcher = pattern.matcher(str.trim())
            //间隔时间小时
            var intervalHour = 0
            var showStr= str
            if(matcher.matches()){
                intervalHour = matcher.group(2).toIntOrNull()?:0
                showStr = matcher.group(3)
            }
            if(Calendar.getInstance().timeInMillis-time>0){
                NoticeDialog2(context, showStr.trim()).show()
                SPUtils.setLong(context, NoticeKey, Calendar.getInstance().timeInMillis+intervalHour*60*60*1000)
            }else{
                Log.e("test","下次显示时间 ${time}")
            }

        }
        private const val InteractionCountKey = "InteractionCountKey"
        //判断插屏广告
        @JvmStatic
        fun checkInteractionAd(context: Context, str: String): Boolean {
            val time = SPUtils.getInt(context, InteractionCountKey, 0)
            val count= str.trim().toIntOrNull()?:0
            if(time<count){
                SPUtils.setInt(context, InteractionCountKey, time+1)
                return false
            }else{
                SPUtils.setInt(context, InteractionCountKey, 0)
                return true
            }

        }
    }

}