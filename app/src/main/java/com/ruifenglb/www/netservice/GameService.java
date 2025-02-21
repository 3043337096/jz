package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.GameBean;
import com.ruifenglb.www.bean.PageResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GameService {
    @GET(ApiConfig.getGameList)
    Observable<PageResult<GameBean>> getGameList(@Query("limit") String limit, @Query("page") String page,@Query("size") String size);
}
