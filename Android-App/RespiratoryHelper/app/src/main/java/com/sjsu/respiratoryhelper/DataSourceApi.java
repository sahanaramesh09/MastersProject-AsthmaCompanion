package com.sjsu.respiratoryhelper;


import com.sjsu.respiratoryhelper.appconfig.BaseHelper;
import com.sjsu.respiratoryhelper.model.ResponseModel;
import com.sjsu.respiratoryhelper.model.UserDetails;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DataSourceApi {

    @GET(BaseHelper.RESULT_API_ENDPOINT)
    Observable<ResponseModel> getResults();

    @Headers({
            "Content-type: application/json"
    })
    @POST(BaseHelper.API_SIGN_UP)
    Call<UserDetails> sendPosts(@Body UserDetails userDetails);

    @POST(BaseHelper.API_LOGIN)
    Call<UserDetails> login(@Body UserDetails userDetails);

}
