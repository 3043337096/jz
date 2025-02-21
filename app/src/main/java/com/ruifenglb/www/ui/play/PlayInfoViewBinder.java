package com.ruifenglb.www.ui.play;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ruifenglb.www.R;
import com.ruifenglb.www.banner.Data;
import com.ruifenglb.www.bean.PlayFromBean;
import com.ruifenglb.www.bean.PlayerInfoBean;
import com.ruifenglb.www.bean.UrlBean;
import com.ruifenglb.www.bean.VodBean;
import me.drakeet.multitype.ItemViewBinder;

public class PlayInfoViewBinder extends ItemViewBinder<VodBean, PlayInfoViewBinder.ViewHolder> {

    private PlayInfoViewBinder.PlayerInfoClickListener playerInfoClickListener;

    public PlayInfoViewBinder setPlayerInfoClickListener(PlayerInfoClickListener playerInfoClickListener) {
        this.playerInfoClickListener = playerInfoClickListener;
        return this;

    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new PlayInfoViewBinder.ViewHolder(inflater.inflate(R.layout.item_playinfo, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull VodBean item) {
        holder.title.setText(item.getVod_name());

        holder.intro.setOnClickListener(v -> {
            if (playerInfoClickListener != null) {
                playerInfoClickListener.onSummary(v);
            }
        });
        holder.year.setText(item.getVod_year());

        holder.area.setText(item.getVod_area());


        holder.score.setText(item.getVod_score() + "分");
        holder.sortVodView.setText(item.getType().getType_name());

        holder.tvLastest.setText(item.getVodRemarks());//选集
        List<PlayFromBean> vod_play_list = item.getVod_play_list();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.rvLastest.setLayoutManager(linearLayoutManager);
        LastestAdapter lastestAdapter = new LastestAdapter();
        holder.rvLastest.setAdapter(lastestAdapter);

        if (vod_play_list != null && vod_play_list.size() != 0) {
            for (int i = 0; i < vod_play_list.size(); i++) {
                PlayFromBean playFromBean = vod_play_list.get(i);
                PlayerInfoBean player_info = playFromBean.getPlayer_info();
                List<UrlBean> urls = playFromBean.getUrls();
                String playSource = player_info.getShow();
                if (StringUtils.isEmpty(playSource)) {
                    playSource = "默认";
                }
                if (i == 0) {
                    lastestAdapter.addData(urls);
                }

                TabLayout.Tab tab = holder.tlPlaySource.newTab().setText(playSource);
                holder.tlPlaySource.addTab(tab);


















                holder.tlPlaySource.setOnClickListener(v -> lastestAdapter.setNewData(urls));
            }
        } else {
            TabLayout.Tab tab = holder.tlPlaySource.newTab();
            tab.setText("默认");
            holder.tlPlaySource.addTab(tab);
//            lastestAdapter.addData(item.getVod_urls());
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView intro;

        final TextView year;
        final TextView area;
        final TextView score;
        final TextView sortVodView;
        final TextView tvLastest;
        final TabLayout tlPlaySource;
        final RecyclerView rvLastest;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_tv_playinfo_title); //节目名
            intro = itemView.findViewById(R.id.item_tv_playinfo_intro); //简介
            area = itemView.findViewById(R.id.item_tv_playinfo_area); //播放次数
            year = itemView.findViewById(R.id.item_tv_playinfo_year); //播放次数
            score = itemView.findViewById(R.id.item_tv_playinfo_score);//评分
            tvLastest = itemView.findViewById(R.id.tvLastest);  //备注显示
            sortVodView = itemView.findViewById(R.id.item_svv_playinfo); //扩展分类
            tlPlaySource = itemView.findViewById(R.id.tlPlaySource); //播放源

            rvLastest = itemView.findViewById(R.id.rvLastest); //选集


        }
    }

    public interface PlayerInfoClickListener {
        void onSummary(View view);
    }

    public static class LastestAdapter extends BaseQuickAdapter<UrlBean, BaseViewHolder> {

        public LastestAdapter() {
            super(R.layout.item_video_source);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, UrlBean item) {
            String name = item.getName().replace("第", "").replace("集", "");
            helper.setText(R.id.tv, name);//过滤第和集
            helper.addOnClickListener(R.id.tv);
        }
    }
}



