package com.ruifenglb.www.utils

import com.ruifenglb.www.bean.AppConfigBean
import com.ruifenglb.www.bean.PlayScoreBean
import com.ruifenglb.www.bean.StartBean
import com.ruifenglb.www.entity.AdvEntity
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

/**
 * Times: 2020/11/17 16:33
 * Author: sj
 * Description:腾讯MMKV框架，用来替代SP的，详情见https://github.com/Tencent/MMKV
 */
class MMkvUtils {


    private var mmkv: MMKV = MMKV.defaultMMKV()

    constructor()

    companion object {
        private val SEARC_HOT = "searchHotMsg"
        private val START_BEAN = "startBeanMsg"
        private val ADV_ENTITY = "advEntityMsg"
        private val APP_CONFIG = "appConfigBeanMsg"
        private val APP_CONFIG_AD = "appConfigBeanAdMsg"
        private val PLAY_SCORE = "playScoreBeanMsg"
        private val PLAY_TZ = "playTiaoZhuan"
        private val PLAY_TZ2 = "playTiaoZhuan2"
        private val USER = "account"

        private var mmkvUtils: MMkvUtils? = null

        fun Builds(): MMkvUtils {
            if (mmkvUtils == null) {
                mmkvUtils = MMkvUtils()
            }
            return mmkvUtils!!
        }

    }

    /**
     * 存储SearchHot数据
     */
    fun saveSearchHot(value: List<String>) {
        val toJson = GsonUtils.toJson(value)
        mmkv.encode(SEARC_HOT, toJson)
    }

    fun loadSearchHot(defaultValue: String): List<String> {
        val msg = mmkv.decodeString(SEARC_HOT, defaultValue)
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val toMutableList = GsonUtils.fromJson<List<String>>(msg, type)
        return toMutableList
    }

    /**
     * 存储StartBean数据
     */
    fun saveStartBean(value: StartBean?) {
        if (value == null) {
            mmkv.encode(START_BEAN, "")
        } else {
            val msg = GsonUtils.toJson(value)
            mmkv.encode(START_BEAN, msg)
        }
    }

    fun loadStartBean(defaultValue: String): StartBean? {
        val msg = mmkv.decodeString(START_BEAN, defaultValue)
        if (msg.isNullOrEmpty()){
            return null
        }else{
            val fromJson = GsonUtils.fromJson(msg, StartBean::class.java)
            return fromJson
        }
    }

    /**
     * 储存advEntity数据
     */
    fun saveAdvEntity(value: AdvEntity?) {
        if (value == null) {
            mmkv.encode(ADV_ENTITY, "")
        } else {
            val msg = GsonUtils.toJson(value)
            mmkv.encode(ADV_ENTITY, msg)
        }
    }

    fun loadAdvEntity(defaultValue: String): AdvEntity? {
        val msg = mmkv.decodeString(ADV_ENTITY, defaultValue)
        if (msg.isNullOrEmpty()) {
            return null
        } else {
            val fromJson = GsonUtils.fromJson(msg, AdvEntity::class.java)
            return fromJson
        }
    }


    /**
     * 储存AppConfig数据
     */
    fun saveAppConfig(value: AppConfigBean?) {
        if (value == null) {
            mmkv.encode(APP_CONFIG, "")
        } else {
            val msg = GsonUtils.toJson(value)
            mmkv.encode(APP_CONFIG, msg)
        }
    }

    fun loadAppConfig(defaultValue: String): AppConfigBean? {
        val msg = mmkv.decodeString(APP_CONFIG, defaultValue)
        if (msg.isNullOrEmpty()) {
            return null
        } else {
            val fromJson = GsonUtils.fromJson(msg, AppConfigBean::class.java)
            return fromJson
        }
    }

    /**
     * 储存AppConfig数据
     */
    fun saveAppConfigAd(value: AppConfigBean?) {
        if (value == null) {
            mmkv.encode(APP_CONFIG_AD, "")
        } else {
            val msg = GsonUtils.toJson(value)
            mmkv.encode(APP_CONFIG_AD, msg)
        }
    }

    fun loadAppConfigAd(defaultValue: String): AppConfigBean? {
        val msg = mmkv.decodeString(APP_CONFIG_AD, defaultValue)
        if (msg.isNullOrEmpty()) {
            return null
        } else {
            val fromJson = GsonUtils.fromJson(msg, AppConfigBean::class.java)
            return fromJson
        }
    }

    fun savePlayTiaoZhuan(formatTime:String,vod_id:Int){
        mmkv.encode(vod_id.toString()+ PLAY_TZ,formatTime)
    }
    fun loadPlayTiaoZhuan(vod_id: Int):String{
        return mmkv.decodeString(vod_id.toString()+ PLAY_TZ)?:"00:00:00"
    }

    fun savePlayTiaoZhuan2(formatTime:String,vod_id:Int){
        mmkv.encode(vod_id.toString()+ PLAY_TZ2,formatTime)
    }
    fun loadPlayTiaoZhuan2(vod_id:Int):String{ //片尾时间
        return mmkv.decodeString(vod_id.toString()+PLAY_TZ2)?:"00:00:00"
    }
    //存储账号
    fun saveAccount(text: String){
        mmkv.encode(USER,text)
    }
    fun loadAccount():String{
        return mmkv.decodeString(USER)?:""
    }

    /**
     * 存储playScore数据
     */
    fun savePlayScore(value: PlayScoreBean?) {
        if (value == null) {
            mmkv.encode(PLAY_SCORE, "")
        } else {
            val msg = GsonUtils.toJson(value)
            mmkv.encode(PLAY_SCORE, msg)
        }
    }

    fun loadPlayScore(defaultValue: String): PlayScoreBean? {
        val msg = mmkv.decodeString(PLAY_SCORE, defaultValue)
        if (msg.isNullOrEmpty()) {
            return null
        } else {
            val fromJson = GsonUtils.fromJson(msg, PlayScoreBean::class.java)
            return fromJson
        }
    }


    fun saveString(key: String, value: String) = mmkv.encode(key, value)
    fun loadString(key: String, defaultValue: String) = mmkv.decodeString(key, defaultValue)

    fun saveBoolean(key: String, value: String) = mmkv.encode(key, value)
    fun loadBoolean(key: String, defaultValue: Boolean) = mmkv.decodeBool(key, defaultValue)

}