package com.ruifenglb.www.horizontal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.ruifenglb.www.R;
import com.ruifenglb.www.ad.AdWebView;
import com.ruifenglb.www.banner.Data;
import com.ruifenglb.www.base.BaseItemClickListener;
import com.ruifenglb.www.base.BaseRecyclerAdapter;
import com.ruifenglb.www.bean.PageResult;
import com.ruifenglb.www.bean.RecommendBean2;
import com.ruifenglb.www.bean.SpecialtTopicBean;
import com.ruifenglb.www.bean.StartBean;
import com.ruifenglb.www.bean.VodBean;
import com.ruifenglb.www.bean.NewRecommendBean2;
import com.ruifenglb.www.netservice.TopicService;
import com.ruifenglb.www.network.RetryWhen;
import com.ruifenglb.www.utils.AgainstCheatUtil;
import com.ruifenglb.www.utils.MMkvUtils;
import com.ruifenglb.www.utils.Retrofit2Utils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author : yjz
 * @date : 2020/10/29 17:41
 * @des :
 */
public class HorizontalScrollViewBinder extends ItemViewBinder<NewRecommendBean2.DataBean, HorizontalScrollViewBinder.ViewHolder> {

    private BaseItemClickListener baseItemClickListener;
    private StartBean.Ad ad;
    Disposable  disposable1;
    public HorizontalScrollViewBinder setBaseItemClickListener(BaseItemClickListener baseItemClickListener) {
        this.baseItemClickListener = baseItemClickListener;
        StartBean startBean = MMkvUtils.Companion.Builds().loadStartBean("");
        if (startBean != null) {
            if (startBean != null && startBean.getAds() != null && startBean.getAds().getIndex() != null) {
                ad = startBean.getAds().getIndex();
            }
        }
        return this;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_scroller, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NewRecommendBean2.DataBean item) {
        if (ad != null && !StringUtils.isEmpty(ad.getDescription()) && ad.getStatus() == 1) {
            holder.adWebView.setVisibility(View.VISIBLE);
            holder.adWebView.loadHtmlBody(ad.getDescription());
        } else {
            holder.adWebView.setVisibility(View.GONE);
        }
        RecommendBean2 recommendBean2 = item.getZhui().get(0);
        holder.textView.setText(recommendBean2.getVod_type_name());

        holder.setZhuiBeanList(recommendBean2.getVod_list());
        Glide.with(holder.itemView.getContext()).load(recommendBean2.getVod_type_img()).into(holder.typeIcon);
        holder.horizontalAdapter.setOnRecyclerViewItemClickListen(new BaseRecyclerAdapter.OnRecyclerViewItemClickListen() {
            @Override
            public void onItemClickListen(View view, int position) {
                baseItemClickListener.onClickItem(view, holder.horizontalAdapter.getItem(position));
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;
        private List<VodBean> zhuiBeanList = new ArrayList<>();
        private HorizontalAdapter horizontalAdapter;
        private TextView textView;
        private ImageView typeIcon;
        private @NonNull
        final AdWebView adWebView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adWebView  = itemView.findViewById(R.id.adWebView);
            recyclerView = itemView.findViewById(R.id.recycle_view);
            textView = itemView.findViewById(R.id.item_tv_top_title);
            typeIcon = itemView.findViewById(R.id.type_icon);


            LinearLayoutManager linearLayoutManager = new GridLayoutManager(itemView.getContext(), 2, RecyclerView.VERTICAL, false);

            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new HorizontalItemDecoration(0, itemView.getContext()));

            horizontalAdapter = new HorizontalAdapter(itemView.getContext(), zhuiBeanList);
            recyclerView.setAdapter(horizontalAdapter);
        }

        public void setZhuiBeanList(List<VodBean> zhuiBeanList) {
            horizontalAdapter.clear();
            horizontalAdapter.addAll(zhuiBeanList);
        }
    }
}
