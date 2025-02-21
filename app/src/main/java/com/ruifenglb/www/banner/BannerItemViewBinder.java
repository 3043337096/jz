package com.ruifenglb.www.banner;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;

import com.ruifenglb.www.R;
import com.ruifenglb.www.ad.AdWebView;
import com.ruifenglb.www.bean.BannerBean;
import com.ruifenglb.www.bean.StartBean;
import com.ruifenglb.www.bean.VodBean;
import com.ruifenglb.www.utils.MMkvUtils;
import me.drakeet.multitype.ItemViewBinder;


@SuppressWarnings("unused")
//@SuppressWarnings("unused")
public class BannerItemViewBinder extends ItemViewBinder<BannerBean, BannerItemViewBinder.ViewHolder> implements BlurBanner.onBannerActionListener {

    private BlurBanner.onBannerActionListener onActionListener;
    private StartBean.Ad ad;

    public BannerItemViewBinder setOnActionListener(BlurBanner.onBannerActionListener onActionListener, int index) {
        this.onActionListener = onActionListener;
        if (index == -1) {
            return this;
        }
        StartBean startBean = MMkvUtils.Companion.Builds().loadStartBean("");
        if (startBean != null) {
            if (index == 0) {
                if (startBean != null && startBean.getAds() != null && startBean.getAds().getIndex() != null) {
                    ad = startBean.getAds().getIndex();
                }
            } else if (index == 1) {
                if (startBean != null && startBean.getAds() != null && startBean.getAds().getVod() != null) {
                    ad = startBean.getAds().getVod();
                }
            } else if (index == 2) {
                if (startBean != null && startBean.getAds() != null && startBean.getAds().getSitcom() != null) {
                    ad = startBean.getAds().getSitcom();
                }
            } else if (index == 3) {
                if (startBean != null && startBean.getAds() != null && startBean.getAds().getVariety() != null) {
                    ad = startBean.getAds().getVariety();
                }
            } else if (index == 4) {
                if (startBean != null && startBean.getAds() != null && startBean.getAds().getCartoon() != null) {
                    ad = startBean.getAds().getCartoon();
                }
            }
        }
        return this;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_banner, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BannerBean item) {
        holder.myBanner.setOnBannerActionListener(this);
        holder.myBanner.setDataList(item.getBannerList());
        holder.myBanner.start();

        if (ad != null && !StringUtils.isEmpty(ad.getDescription()) && ad.getStatus() == 1) {
            holder.adWebView.setVisibility(View.VISIBLE);
            holder.adWebView.loadHtmlBody(ad.getDescription());
        } else {
            holder.adWebView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageChange(int position, Bitmap bitmap) {
        if (onActionListener != null) onActionListener.onPageChange(position, bitmap);
    }

    @Override
    public void onBannerClick(int position, Object o) {
        if (onActionListener != null) onActionListener.onBannerClick(position, o);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private @NonNull
        final BlurBanner<VodBean> myBanner;

        private @NonNull
        final AdWebView adWebView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            myBanner = itemView.findViewById(R.id.item_banner);
            adWebView = itemView.findViewById(R.id.adWebView);
        }

    }

}

