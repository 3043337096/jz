package com.ruifenglb.www.ui.share


import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.StormWyrm.wanandroid.base.exception.ResponseException
import com.github.StormWyrm.wanandroid.base.net.RequestManager
import com.github.StormWyrm.wanandroid.base.net.observer.BaseObserver
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ruifenglb.www.R
import com.ruifenglb.www.base.BaseActivity
import com.ruifenglb.www.base.observer.LoadingObserver
import com.ruifenglb.www.bean.LoginBean
import com.ruifenglb.www.bean.ShareBean
import com.ruifenglb.www.bean.ShareInfoBean
import com.ruifenglb.www.bean.VodBean
import com.ruifenglb.www.netservice.VodService
import com.ruifenglb.www.ui.login.LoginActivity
import com.ruifenglb.www.utils.AgainstCheatUtil
import com.ruifenglb.www.utils.Retrofit2Utils
import com.ruifenglb.www.utils.SimpleUtils
import com.ruifenglb.www.utils.UserUtils
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_share.*
import org.greenrobot.eventbus.EventBus
import java.io.File

class ShareActivity : BaseActivity() {
    private var shareInfo: ShareInfoBean? = null
    private var vom_name: String = ""
    private var vod_tag: String = ""
    private var vod_typeName: String = ""
    private var vod_year: String = ""
    private var vod_area: String = ""
    private var vod_class: String = ""
    private var vod_pic_slide: String = ""
    private var vod_blurd: String = ""


    override fun getLayoutResID(): Int {
        return R.layout.activity_share
    }


    override fun initData() {
        super.initData()
        getShareUrl()

    }

    override fun initListener() {
        super.initListener()
        ivBack.setOnClickListener {
            finish()
        }
        ivInviteFriend.setOnClickListener {
            PermissionUtils.permission(PermissionConstants.STORAGE)
                    .callback(object : PermissionUtils.SimpleCallback {
                        override fun onGranted() {
                            inviteFriend()
                        }

                        override fun onDenied() {
                            ToastUtils.showShort("需要开启读写权限后才能分享！")
                        }

                    })
                    .request()

        }
        ivCopyLink.setOnClickListener {
            copyLink()
        }

        vom_name = getIntent()?.getStringExtra("vom_name").toString();
        vod_tag = getIntent()?.getStringExtra("vod_class").toString();
        vod_typeName = getIntent()?.getStringExtra("vod_class_typeName").toString();
        vod_year = getIntent()?.getStringExtra("vod_class_year").toString();
        vod_area = getIntent()?.getStringExtra("vod_class_area").toString();
        vod_class = getIntent()?.getStringExtra("vod_class_class").toString();
        vod_pic_slide = getIntent()?.getStringExtra("vod_pic").toString();
        vod_blurd = getIntent()?.getStringExtra("vod_blurd").toString();
        VomName.setText(vom_name)
        vodTag.setText(vod_tag)
        item_svv_playtypeName.setText(vod_typeName)
        item_svv_playinfo.setText(vod_class)
        item_svv_playyear.setText(vod_year)
        item_svv_playarea.setText(vod_area)


        if (vod_class.length < 15 && vod_area.isNotEmpty()) {
            item_svv_playarea.visibility = View.VISIBLE
        } else {
            item_svv_playarea.visibility = View.GONE
            if (vod_class.length > 20) {
                item_svv_playyear.visibility = View.GONE
            } else {
                item_svv_playyear.visibility = View.VISIBLE
            }
        }

        if (vod_tag.equals("null")) {
            item_share_fl.visibility = View.VISIBLE
            vodTag.visibility = View.GONE
            vodBlurd.setText("      " + vod_blurd)
        } else {
            item_share_fl.visibility = View.GONE
            vodTag.visibility = View.VISIBLE
            vodBlurd.setText(vod_blurd)
        }

        if (vod_typeName.equals("电影")) {
            item_svv_playtypePic.setBackgroundResource(R.drawable.playinfo_dypic);
        } else if (vod_typeName.equals("剧集")) {
            item_svv_playtypePic.setBackgroundResource(R.drawable.playinfo_jjpic);
        } else if (vod_typeName.equals("综艺")) {
            item_svv_playtypePic.setBackgroundResource(R.drawable.playinfo_zypic);
        } else if (vod_typeName.equals("动漫")) {
            item_svv_playtypePic.setBackgroundResource(R.drawable.playinfo_dmpic);
        } else if (vod_typeName.equals("B站")) {
            item_svv_playtypePic.setBackgroundResource(R.drawable.playinfo_bilipic);
        } else {
            item_svv_playtypePic.setBackgroundResource(R.drawable.playinfo_jlppic);
        }

        val multi = MultiTransformation(CenterCrop(), RoundedCornersTransformation(9, 0, RoundedCornersTransformation.CornerType.TOP))
        Glide.with(this).load(vod_pic_slide).apply(RequestOptions.bitmapTransform(multi)).placeholder(R.drawable.ic_extension_share_top).into(vodPicSlide);
        // vodBlurd.setText(vod_blurd)


//        tvCopy.setOnClickListener {
//            copyShareCode()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQOUEST_SHARE) {
            shareScore()
        }
        if (requestCode == -1) {
            if (data != null) {
                vom_name = data.getStringExtra("vom_name")
                VomName.setText(vom_name)
            }


        }
    }

    private fun getShareUrl() {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(
                this,
                vodService.getShareInfo(),
                object : LoadingObserver<ShareInfoBean>(mActivity) {
                    override fun onSuccess(data: ShareInfoBean) {
                        shareInfo = data
                        data.run {
                            val bitmap = BarcodeEncoder().encodeBitmap(share_url, BarcodeFormat.QR_CODE,
                                    ConvertUtils.dp2px(125f), ConvertUtils.dp2px(125f))
                            ivQrcode.setImageBitmap(bitmap)
                            if (share_url.contains("=")) {
                                val shareCode = share_url.split("=")[1]
                                //wqddg
//                                if (shareCode.isNotEmpty())
//                                    tvSharecode.text = shareCode
                            }
                            if (vod_pic_slide != "www") {
                                share_logo = vod_pic_slide
                            }
                            if (!share_logo.isNullOrEmpty() && mActivity != null && !mActivity.isFinishing) {
                                val multi = MultiTransformation(
                                        BlurTransformation(100, 2))
                                Glide.with(mActivity)
                                        .load(share_logo)
                                        .apply(RequestOptions.bitmapTransform(multi))
                                        .into(object : CustomTarget<Drawable>() {
                                            override fun onLoadCleared(placeholder: Drawable?) {
                                            }

                                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                                rlRoot.setBackgroundDrawable(resource)
                                            }

                                        })


                            }
                        }
                    }

                    override fun onError(e: ResponseException) {
                    }

                }
        )
    }

    private fun inviteFriend() {
        val progressDialog = ProgressDialog.show(mActivity, "", StringUtils.getString(R.string.loading_msg))
        ThreadUtils.executeBySingle(object : ThreadUtils.Task<File>() {
            override fun doInBackground(): File {
                val bitmap = SimpleUtils.getCacheBitmapFromView(nrong)
                return SimpleUtils.saveBitmapToSdCard(mActivity, bitmap)

            }

            override fun onSuccess(file: File?) {
                progressDialog.dismiss()
                if (file == null) {
                    ToastUtils.showShort("分享失败，请重试")
                }
                file?.let {
                    shareSingleImage(it)
                }
            }

            override fun onFail(t: Throwable?) {
                progressDialog.dismiss()
                ToastUtils.showShort("分享失败，请重试")
            }

            override fun onCancel() {
                progressDialog.dismiss()
            }


        })


    }

    //分享单张图片
    fun shareSingleImage(file: File) {
        val imageUri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, file.absolutePath, file.name, null))
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "分享到"), REQOUEST_SHARE);
    }

    private fun copyLink() {
        shareInfo?.run {
            val clipData = ClipData.newPlainText("", share_url)
            val clipbrardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipbrardManager.setPrimaryClip(clipData)
            ToastUtils.showShort("已经复制到剪切板")
        }

    }

    private fun copyShareCode() {
        val clipData = ClipData.newPlainText("", "tvSharecode.text.toString()")
        val clipbrardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipbrardManager.setPrimaryClip(clipData)
        ToastUtils.showShort("已经复制到剪切板")
    }

    private fun shareScore() {
        var vodService = Retrofit2Utils.INSTANCE.createByGson(VodService::class.java)
        if (AgainstCheatUtil.showWarn(vodService)) {
            return;
        }
        RequestManager.execute(
                mActivity,
                vodService.shareScore(),
                object : BaseObserver<ShareBean>() {
                    override fun onSuccess(data: ShareBean) {
                        if (data.score != "0") {
                            ToastUtils.showShort("分享成功，获得${data.score}积分")
                        } else {
                            ToastUtils.showShort("分享成功")
                        }
                        EventBus.getDefault().post(LoginBean())
                    }

                    override fun onError(e: ResponseException) {
                    }
                }
        )
    }

    companion object {
        const val REQOUEST_SHARE = 1
        fun start(mVodBean: VodBean) {
            if (!UserUtils.isLogin()) {
                ActivityUtils.startActivity(LoginActivity::class.java)
            } else {
                var bynit = Bundle()
                bynit.putString("vom_name", mVodBean.vodName)
                ActivityUtils.startActivity(ShareActivity::class.java, bynit)
            }
        }
    }

}
