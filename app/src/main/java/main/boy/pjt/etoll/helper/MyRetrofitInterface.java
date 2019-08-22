package main.boy.pjt.etoll.helper;

import main.boy.pjt.etoll.request.RequestLogin;
import main.boy.pjt.etoll.response.ResponseActivity;
import main.boy.pjt.etoll.response.ResponseBalance;
import main.boy.pjt.etoll.response.ResponseDefault;
import main.boy.pjt.etoll.response.ResponseProfile;
import main.boy.pjt.etoll.response.ResponseRoad;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Boy Panjaitan on 04/03/2018.
 */

public interface MyRetrofitInterface {

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.getActivities)
    Call<ResponseActivity.RetrofitResponse> getActivities(@Field("id") String id);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.topUpBalance)
    Call<ResponseDefault> topUpBalance(@Field("id") String id, @Field("nominal") int nominal);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.checkBalance)
    Call<ResponseDefault> checkBalance(@Field("memberId") String memberId,
                                       @Field("roadId") String roadId,
                                       @Field("gateId") String gateId);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.placeRoadOrder)
    Call<ResponseBalance.RetrofitResponse> placeRoadOrder(@Field("memberId") String memberId,
                                                          @Field("roadId") String roadId,
                                                          @Field("gateId") String gateId);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.getRoadList)
    Call<ResponseRoad.RetrofitResponse> getRoadList(@Field("id") String id);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.getBalance)
    Call<ResponseBalance.RetrofitResponse> getBalance(@Field("id") String id);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.registerPath)
    Call<ResponseDefault> register(@Field("username") String username,
                                   @Field("name") String name,
                                   @Field("password") String password,
                                   @Field("email") String email,
                                   @Field("phone") String phone);

    /*
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept: application/json, text/javascript"})
    @POST(MyConstant.Endpoint.loginPath)
    Call<ResponseProfile.RetrofitResponse> login(@Body RequestLogin body);
    */

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.updateFbmToken)
    Call<ResponseDefault> updateFbmToken(@Field("id") String id, @Field("token") String token);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.loginPath)
    Call<ResponseProfile.RetrofitResponse> loginField(@Field("id") String id, @Field("password") String password, @Field("token") String token);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.loadProfile)
    Call<ResponseProfile.RetrofitResponse> loadProfile(@Field("token") String token, @Field("costumer") String costumer);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.updateProfile)
    Call<ResponseDefault> updateProfile(@Field("id") String username,
                                        @Field("name") String name,
                                        @Field("password") String password,
                                        @Field("email") String email,
                                        @Field("phone") String phone);
}
