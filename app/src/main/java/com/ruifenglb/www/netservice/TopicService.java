package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.PageResult;
import com.ruifenglb.www.bean.SpecialtTopicBean;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface TopicService {
    @GET(ApiConfig.getTopicList)
    Observable<PageResult<SpecialtTopicBean>> getTopicList();
}
