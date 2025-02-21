package com.ruifenglb.www.horizontal;

import android.content.Context;
import android.util.Log;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.blankj.utilcode.util.ColorUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.ruifenglb.www.R;
import com.ruifenglb.www.banner.Data;
import com.ruifenglb.www.base.BaseRecyclerAdapter;
import com.ruifenglb.www.bean.VodBean;
import com.ruifenglb.www.ui.widget.RoundImgView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author : yjz
 * @date : 2020/10/29 17:50
 * @des :
 */
public class HorizontalAdapter extends BaseRecyclerAdapter<VodBean> {

    private Context context;
    public HorizontalAdapter(@NonNull Context context, List<VodBean> data) {
        super(context, R.layout.item_horizontal, data, true);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, VodBean item, int position) {

        AppCompatImageView imageView = (AppCompatImageView) holder.getView(R.id.item_iv_card_child_icon);
        String img = item.getVod_pic_thumb();

        MultiTransformation multiTransformation = new MultiTransformation(new CenterCrop(),
                new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL));

        Glide.with(context)

                .load(img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(1.0f)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
        TextView textView1 = holder.itemView.findViewById(R.id.item_tv_card_child_vod_blurb);
        TextView textView2 = holder.itemView.findViewById(R.id.item_tv_card_child_title);
        holder.setText(R.id.item_tv_card_child_title, item.getVodName())
        .setText(R.id.item_tv_card_child_vod_blurb, item.getVodBlurb());
if(Data.getQQ().equals("暗夜紫")) {
            textView2.setTextColor(ColorUtils.getColor(R.color.white));
            textView1.setTextColor(ColorUtils.getColor(R.color.white));

        }else if(Data.getQQ().equals("原版蓝")) {
            textView2.setTextColor(ColorUtils.getColor(R.color.black));
            textView1.setTextColor(ColorUtils.getColor(R.color.ls));
        }
    }
}
