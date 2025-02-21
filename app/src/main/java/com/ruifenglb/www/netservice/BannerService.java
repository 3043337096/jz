package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.VodBean;
import com.ruifenglb.www.bean.PageResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BannerService {

    @GET(ApiConfig.getBannerList)
    Observable<PageResult<VodBean>> getBannerList(@Query("level") int level);

    @GET(ApiConfig.getBannerList)
    Observable<PageResult<VodBean>> getBannerList(@Query("type") int type_id, @Query("level") int level);
}
