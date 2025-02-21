package com.ruifenglb.www.ui.play;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruifenglb.av.widget.view.SortVodView;

import com.ruifenglb.www.R;
import com.ruifenglb.www.bean.PlayList;
import me.drakeet.multitype.ItemViewBinder;

public class PlayListViewBinder extends ItemViewBinder<PlayList, PlayListViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new PlayListViewBinder.ViewHolder(inflater.inflate(R.layout.item_playlist, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PlayList item) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView year;
        final TextView score;
        final SortVodView sortVodView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_tv_playinfo_title);
            year = itemView.findViewById(R.id.item_tv_playinfo_year);
            score = itemView.findViewById(R.id.item_tv_playinfo_score);
            sortVodView = itemView.findViewById(R.id.item_svv_playinfo);
        }
    }
}
