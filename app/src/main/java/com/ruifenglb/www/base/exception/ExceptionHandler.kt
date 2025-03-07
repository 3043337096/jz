package com.github.StormWyrm.wanandroid.base.exception

import android.net.ParseException
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.BAD_GATEWAY
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.FORBIDDEN
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.GATEWAY_TIMEOUT
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.HTTP_ERROR
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.INTERNAL_SERVER_ERROR
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.NET_ERROR
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.NORMAL
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.NOT_FOUND
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.PARSE_ERROR
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.REQUEST_TIMEOUT
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.SERVICE_UNAVAILABLE
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.SSL_ERROR
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.TIMEOUT_ERROR
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.UNAUTHORIZED
import com.github.StormWyrm.wanandroid.base.exception.Code.Companion.UNKNOWN_ERROR
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

class ExceptionHandler {
    companion object {
        fun handle(e: Throwable): ResponseException {
            val responseException: ResponseException
            if (e is ApiException) {
                responseException = ResponseException(e, Integer.valueOf(e.errorCode), e.message)
            } else if (e is HttpException) {
                var msg = ""
                if (e.code() == 400) {
                    val result = e.response()?.errorBody()?.string()
                    val jsonObject = JSONObject(result)
                    val errorCode = jsonObject.optInt("code")
                    if (errorCode == 403) {
                        SPUtils.getInstance("user_cookie").clear(true)
                    }
                    if(errorCode == -10000){
                        ToastUtils.showShort("播放次数已用完")
                    }
                    msg = jsonObject.optString("msg")
                }
                if (msg.isNullOrEmpty()) {
                    msg = "请求失败，请稍后重试"
                }
                responseException = when (e.code()) {
                    NORMAL, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> ResponseException(
                            e,
                            "$HTTP_ERROR:${e.code()}",
                            msg
                    )
                    else -> ResponseException(e, "$HTTP_ERROR:${e.code()}", "网络连接错误")
                }
            } else if (e is JsonParseException
                    || e is JSONException
                    || e is ParseException
            ) {
                responseException = ResponseException(e, PARSE_ERROR, "解析错误")
            } else if (e is ConnectException || e is UnknownHostException) {
                responseException = ResponseException(e, NET_ERROR, "连接失败")
            } else if (e is ConnectTimeoutException || e is java.net.SocketTimeoutException) {
                responseException = ResponseException(e, TIMEOUT_ERROR, "网络连接超时")
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                responseException = ResponseException(e, SSL_ERROR, "证书验证失败")
            } else {
                responseException = ResponseException(e, UNKNOWN_ERROR, "")
            }
            return responseException
        }
    }
}