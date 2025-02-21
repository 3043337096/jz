package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.BaseResult;
import com.ruifenglb.www.bean.Page;
import com.ruifenglb.www.bean.PlayLogBean;
import com.ruifenglb.www.bean.TypeBean;
import com.ruifenglb.www.bean.PageResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TypeService {
    @GET(ApiConfig.getTypeList)
    Observable<PageResult<TypeBean>> getTypeList();

    @GET(ApiConfig.getPlayLogList)
    Observable<BaseResult<Page<PlayLogBean>>> getPlayLogList(@Query("page") String page,
                                                             @Query("limit") String limit);

}
