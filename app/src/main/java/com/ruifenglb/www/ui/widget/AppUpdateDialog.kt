package com.ruifenglb.www.ui.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.content.FileProvider
import com.ruifenglb.www.R
import com.ruifenglb.www.bean.AppUpdateBean
import com.ruifenglb.www.utils.AppUpdateUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.android.synthetic.main.dialog_app_update_tip.*
import java.io.File


class AppUpdateDialog(context: Context, val data: AppUpdateBean) : Dialog(context, R.style.DefaultDialogStyle) {
    init {
        setContentView(R.layout.dialog_app_update_tip)
    }

    private val mHandler = Handler()
    private var isDownLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window!!.attributes?.apply {
            gravity = Gravity.CENTER
            // width = (ScreenUtils.getScreenWidth() * 0.8).toInt()
            width = (ScreenUtils.getScreenWidth() * 0.98).toInt()
        }
        tvMsg.text = data.summary

        Log.d("更新方式", "onCreate: "+data.type)
        if (data.type == 2) {
            tvUpdate.visibility = View.VISIBLE
        }else{
            tvUpdate.visibility = View.GONE
            iv_tvUpdate.visibility = View.GONE
        }
        tvUpdate.setOnClickListener {
//            Intent(Intent.ACTION_VIEW, Uri.parse(data.url)).run {
//                ActivityUtils.startActivity(this)
//            }
            dismiss()
        }

        new_version.text = "版本：" + data.version

        tvAppUpdate.setOnClickListener {
            download_pb.visibility = View.VISIBLE
            if (isDownLoading && tvAppUpdate.text.toString() != "下载失败") {
                return@setOnClickListener
            } else if (!isDownLoading || tvAppUpdate.text.toString() == "下载失败") {
                AppUpdateUtils.startDownload2(context, data.url2)
                AppUpdateUtils.setOnDownloadingLisenter(object : AppUpdateUtils.DowloadingLisenter {
                    @SuppressLint("SetTextI18n")
                    override fun downloading(progress: String) {
                        Log.e("APK下载进度", progress)

                        isDownLoading = true
                        mHandler.post {
                            val num: String = progress //后台返回数据 即报错0.001
                            val numToDouble = num.toDouble() // 先转换成double类型
                            download_pb.progress = numToDouble.toInt()
                            tvAppUpdate.text = "$progress%"
                        }
                    }

                    override fun downloadSuc(path: File) {
                        Log.e("APK下载结果", "成功path=${path}")
                        val intent = Intent()
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.action = Intent.ACTION_VIEW
                        //判读版本是否在7.0以上
                        //判读版本是否在7.0以上
                        if (Build.VERSION.SDK_INT >= 24) {
                            //provider authorities
                            val apkUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", path)
                            //Granting Temporary Permissions to a URI
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                        } else {
                            intent.setDataAndType(Uri.fromFile(path), "application/vnd.android.package-archive")
                        }
                        context.startActivity(intent)

                    }

                    override fun downloadFail() {
                        isDownLoading = false
                        Log.e("APK下载结果", "失败")
                        tvAppUpdate.setText("下载失败")
                    }

                })
            }
        }
    }
}