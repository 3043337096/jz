package com.ruifenglb.www.card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.blankj.utilcode.util.ColorUtils;
import com.bumptech.glide.Glide;

import com.ruifenglb.www.R;
import com.ruifenglb.www.banner.Data;
import com.ruifenglb.www.base.BaseItemClickListener;
import com.ruifenglb.www.bean.RecommendBean2;
import com.ruifenglb.www.bean.VodBean;
import com.ruifenglb.www.horizontal.HorizontalItemDecoration;
import com.ruifenglb.www.ui.home.MyDividerItemDecoration;
import com.ruifenglb.www.ui.home.Vod;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

public class CardItemViewBinder2 extends ItemViewBinder<RecommendBean2, CardItemViewBinder2.ViewHolder> implements BaseItemClickListener {

    private CardItemActionListener2 actionListener;

    public CardItemViewBinder2 setActionListener(CardItemActionListener2 actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    private boolean isNeedMore;
    private boolean isNeedFirst;
    private boolean isLimitCount;

    public CardItemViewBinder2(boolean isNeedMore, boolean isNeedFirst) {
        this.isNeedMore = isNeedMore;
        this.isNeedFirst = isNeedFirst;
    }

    public CardItemViewBinder2(boolean isNeedMore, boolean isNeedFirst, boolean isLimitCount) {
        this.isNeedMore = isNeedMore;
        this.isNeedFirst = isNeedFirst;
        this.isLimitCount = isLimitCount;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//        if (isNeedMore) {
//            return new ViewHolder(inflater.inflate(R.layout.item_card2, parent, false), isNeedMore, isNeedFirst);
//        } else {
        return new ViewHolder(inflater.inflate(R.layout.item_card, parent, false), isNeedMore, isNeedFirst);
//        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RecommendBean2 item) {
        if (holder.tvMore != null) {
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionListener != null) {
                        String s = v.getTag() + "";
                        actionListener.onClickMore(v, s, item.getVod_list(), item.getVod_type_name());
                    }
                }
            });
            holder.tvMore.setTag(item.getVod_type_name());
        }
        holder.tvMore.setTag(item.getVod_type_name());
        if (holder.more != null) {
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionListener != null) {
                        String s = v.getTag() + "";
                        actionListener.onClickMore(v, s, item.getVod_list(), item.getVod_type_name());
                    }
                }
            });
            holder.more.setTag(item.getVod_type_name());
        }
        if (holder.change != null) {
            holder.change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionListener != null) {
                        actionListener.onClickChange(v, v.getTag());
                    }
                }
            });
            holder.change.setTag(item);
        }
        if (holder.cardFirstChildItemViewBinder != null) {
            holder.cardFirstChildItemViewBinder.setBaseItemClickListener(this);
        }
        if (holder.cardChildItemViewBinder != null) {
            holder.cardChildItemViewBinder.setBaseItemClickListener(this);
        }
        holder.title.setText(item.getVod_type_name());

      if(Data.getQQ().equals("暗夜紫")) {
            holder.title.setTextColor(ColorUtils.getColor(R.color.white));
        }else if(Data.getQQ().equals("原始蓝")) {
            holder.title.setTextColor(ColorUtils.getColor(R.color.black));
        }

        // 类型图片
        Glide.with(holder.itemView.getContext()).load(item.getVod_type_img()).into(holder.typeIcon);
        holder.setVodList(item.getVod_list(), isLimitCount);
    }


    @Override
    public void onClickItem(View view, Object item) {
        if (actionListener != null) {
            actionListener.onClickItem(view, item);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private @NonNull
        final TextView title;
        private @NonNull
        final RecyclerView recyclerView;
        private MultiTypeAdapter adapter;
        private CardFirstChildItemViewBinder cardFirstChildItemViewBinder;
        private CardChildItemViewBinder cardChildItemViewBinder;
        private TextView tvMore;
        private ImageView typeIcon;
        private final Button more;
        private final Button change;

        ViewHolder(@NonNull View itemView, boolean isNeedMore, boolean isNeedFirst) {
            super(itemView);
            tvMore = itemView.findViewById(R.id.tv_check_more);


            if(Data.getQQ().equals("暗夜紫")) {
                tvMore.setTextColor(ColorUtils.getColor(R.color.white));
            }else if(Data.getQQ().equals("原始蓝")) {
                tvMore.setTextColor(ColorUtils.getColor(R.color.black));
            }
            if (isNeedMore) {
                tvMore.setVisibility(View.VISIBLE);
                more = itemView.findViewById(R.id.item_btn_card_more);
                change = itemView.findViewById(R.id.item_btn_card_change);
            } else {
                tvMore.setVisibility(View.GONE);
                more = null;
                change = null;
            }

            title = itemView.findViewById(R.id.item_tv_card_title);
            recyclerView = itemView.findViewById(R.id.item_rv_card);
            typeIcon = itemView.findViewById(R.id.type_icon);
            //设置一行显示的数目18
            GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 3, RecyclerView.VERTICAL, false);
            //根据数据类型显示占据的item个数
            //不显示单个视频的第一行，之前是7个视频
            isNeedFirst = false;
            if (isNeedFirst) {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position == 0) return 3;
                        return 1;
                    }
                });
            }
            // MyDividerItemDecoration dividerItemDecoration = new MyDividerItemDecoration(itemView.getContext(), RecyclerView.HORIZONTAL, false);
            //dividerItemDecoration.setDrawable(itemView.getContext().getResources().getDrawable(R.drawable.divider_image));
            //recyclerView.addItemDecoration(dividerItemDecoration);

            SimpleItemAnimator itemAnimator = (SimpleItemAnimator) recyclerView.getItemAnimator();
            itemAnimator.setSupportsChangeAnimations(false);

            recyclerView.addItemDecoration(new HorizontalItemDecoration(0, itemView.getContext()));
            recyclerView.setLayoutManager(gridLayoutManager);
            adapter = new MultiTypeAdapter();
            if (isNeedFirst) {
                adapter.register(Vod.class).to(
                        cardFirstChildItemViewBinder = new CardFirstChildItemViewBinder(),
                        cardChildItemViewBinder = new CardChildItemViewBinder()
                ).withClassLinker((position, data) -> {
                    if (position == 0) {
                        return CardFirstChildItemViewBinder.class;
                    } else {
                        return CardChildItemViewBinder.class;
                    }
                });
            } else {
                adapter.register(Vod.class, cardChildItemViewBinder = new CardChildItemViewBinder());
            }

            recyclerView.setAdapter(adapter);
        }

        private void setVodList(List<VodBean> list, boolean isLimit) {
            if (list == null) return;
            //if (list.size() > 7) list = list.subList(0, 7);
            //控制显示两行，每行3个
            if (isLimit) {
                if (list.size() > 6) list = list.subList(0, 6);
            }
            adapter.setItems(list);
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unused")
    public interface CardItemActionListener2 {
//        void onClickMore(View view, Object o);

        void onClickMore(View view, Object o, List<VodBean> list, String title);

        void onClickChange(View view, Object o);

        void onClickItem(View view, Object item);

    }

}
