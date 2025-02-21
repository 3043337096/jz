package com.ruifenglb.www.ui.specialtopic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.util.ColorUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import com.ruifenglb.www.R;
import com.ruifenglb.www.banner.Data;
import com.ruifenglb.www.base.BaseMainFragment;
import com.ruifenglb.www.bean.PageResult;
import com.ruifenglb.www.bean.SpecialtTopicBean;
import com.ruifenglb.www.netservice.TopicService;
import com.ruifenglb.www.network.RetryWhen;
import com.ruifenglb.www.utils.AgainstCheatUtil;
import com.ruifenglb.www.utils.Retrofit2Utils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
public class SpecialtTopicFragment extends BaseMainFragment {
    @BindView(R.id.topic_listview)
    ListView topicListView;

    @BindView(R.id.uuu)
    View uuu;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    List<SpecialtTopicBean> topicEntities;
    SpecialtopicAdpter2 activityLevelAdpter;

    Disposable  disposable1;

    boolean isInit;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_specialtopic;
    }

    public static SpecialtTopicFragment newInstance() {
        Bundle args = new Bundle();
        SpecialtTopicFragment fragment = new SpecialtTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();



        if (Data.getQQ() == "暗夜紫")  {
            uuu.setBackgroundResource(R.color.xkh);
            Data.setB("1");
            topicListView.setBackgroundResource(R.color.xkh);
        }
        if (Data.getQQ() == "原始蓝")  {
            Data.setB("1");
            uuu.setBackgroundResource(R.color.ls);
        }

        if (!isInit) {
            isInit = true;
            topicEntities = new ArrayList<>();
            activityLevelAdpter = new SpecialtopicAdpter2(getActivity(), topicEntities);
            topicListView.setAdapter(activityLevelAdpter);
            activityLevelAdpter.notifyDataSetChanged();

            refreshLayout.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
            refreshLayout.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
            refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
            refreshLayout.setEnableRefresh(false);//是否启用上拉加载功能;
            refreshLayout.setEnableAutoLoadMore(false);
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    refreshlayout.finishRefresh(1000);
                }
            });

            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    refreshLayout.finishRefresh(1000);
                }
            });

            topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                    intent.putExtra("topicid", topicEntities.get(position).getToppic_id());
                    getActivity().startActivity(intent);
                }
            });
            getTopicData();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
       
      //  ToastUtils.showShort("签到成功");

        if (Data.getQQ() == "暗夜紫" && Data.getB()=="2")  {
            uuu.setBackgroundResource(R.color.xkh);
            getTopicData();
            topicListView.setBackgroundResource(R.color.xkh);
        }
        if (Data.getQQ() == "原始蓝" && Data.getB()=="2")  {
            topicListView.setBackgroundResource(R.color.white);
            uuu.setBackgroundResource(R.color.ls);
        }


    }

    private void getTopicData() {
        TopicService cardService = Retrofit2Utils.INSTANCE.createByGson(TopicService.class);
        if (AgainstCheatUtil.showWarn(cardService)) {
            return;
        }
        cardService.getTopicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .retryWhen(new RetryWhen(3, 3))
                .subscribe(new Observer<PageResult<SpecialtTopicBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (disposable1 != null && !disposable1.isDisposed()) {
                            disposable1.dispose();
                            disposable1 = null;
                        }
                        disposable1 = d;
                    }

                    @Override
                    public void onNext(PageResult<SpecialtTopicBean> result) {
                        if (result != null) {
                            if (result.isSuccessful()) {
                                List<SpecialtTopicBean> list = result.getData().getList();
                                topicEntities.clear();
                                topicEntities.addAll(list);
                                activityLevelAdpter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    @Override
    public void onDestroyView() {
        if (disposable1 != null && !disposable1.isDisposed()) {
            disposable1.dispose();
            disposable1 = null;
        }

        super.onDestroyView();
    }

}
