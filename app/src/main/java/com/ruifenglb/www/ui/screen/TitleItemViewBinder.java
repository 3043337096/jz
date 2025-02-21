package com.ruifenglb.www.ui.screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ruifenglb.www.banner.Data;
import org.greenrobot.eventbus.EventBus;
import com.blankj.utilcode.util.ColorUtils;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ruifenglb.www.R;
import com.ruifenglb.www.base.BaseItemClickListener2;
import com.ruifenglb.www.bean.RefreshEvent;
import me.drakeet.multitype.ItemViewBinder;

@SuppressWarnings("unused")
public class TitleItemViewBinder extends ItemViewBinder<Title, TitleItemViewBinder.ViewHolder> implements View.OnClickListener {

    private BaseItemClickListener2 baseItemClickListener;
    private Titles titles;

    public void setBaseItemClickListener(BaseItemClickListener2 baseItemClickListener) {
        this.baseItemClickListener = baseItemClickListener;
    }


    public void setData(Titles titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_tv, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Title item) {
        holder.itemView.setTag(R.id.itemData, item);
        holder.itemView.setTag(R.id.itemSelected, getPosition(holder));
        holder.itemView.setOnClickListener(this);

        TextView textView = (TextView) holder.itemView;
        textView.setText(item.getTitle());

        if (item.isSelected()) {
            textView.setBackgroundResource(R.drawable.bg_yellow);
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorAccent));
            if(Data.getQQ().equals("暗夜紫")) {
                textView.setBackgroundColor(ColorUtils.getColor(R.color.xkh2));
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.ls));
            }else if(Data.getQQ().equals("原始蓝")) {
                textView.setBackgroundResource(R.drawable.bg_yellow);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorAccent));
            }
        } else {
            textView.setBackground(null);
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.gray_999));
        }



    }


    @Override
    public void onClick(View view) {
        Object o = view.getTag(R.id.itemData);
        int pos = (int)view.getTag(R.id.itemSelected);
        if (titles != null) {
            titles.setCurTitle((Title) o);
        }
        EventBus.getDefault().postSticky(new RefreshEvent());

        if (o != null && baseItemClickListener != null) {
            baseItemClickListener.onClickItem(view, o,pos);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
