package com.ruifenglb.www.netservice;


import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.CardBean;
import com.ruifenglb.www.bean.PageResult;
import com.ruifenglb.www.bean.RecommendBean2;
import com.ruifenglb.www.bean.NewRecommendBean2;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CardService {
    @GET(ApiConfig.getCardList)
    Observable<PageResult<CardBean>> getCardList(@Query("need_vod") boolean need_vod);

    @GET(ApiConfig.getRecommendList)
    Observable<PageResult<RecommendBean2>> getRecommendList();

    @GET(ApiConfig.getRecommendList)
    Observable<NewRecommendBean2> newGetRecommendList();
}
