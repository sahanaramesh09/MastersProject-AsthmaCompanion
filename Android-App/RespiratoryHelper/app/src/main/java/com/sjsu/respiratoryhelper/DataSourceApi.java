package com.sjsu.respiratoryhelper;


import com.sjsu.respiratoryhelper.appconfig.BaseHelper;
import com.sjsu.respiratoryhelper.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface DataSourceApi {

    /*@GET(BaseHelper.RESULT_API_ENDPOINT)
    Call<ResponseModel> getResults();*/

    @GET(BaseHelper.RESULT_API_ENDPOINT)
    Observable<ResponseModel> getResults();
}
