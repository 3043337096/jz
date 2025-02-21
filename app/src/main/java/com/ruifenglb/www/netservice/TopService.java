package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.VodBean;
import com.ruifenglb.www.bean.PageResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopService {
    @GET(ApiConfig.getTopList)
    Observable<PageResult<VodBean>> getTopList(@Query("request_type") String request_type, @Query("limit") int limit, @Query("page") int page);

    @GET(ApiConfig.getTopList)
    Observable<PageResult<VodBean>> getTopList(@Query("type") int type_id, @Query("request_type") String request_type, @Query("limit") int limit, @Query("page") int page);
}
