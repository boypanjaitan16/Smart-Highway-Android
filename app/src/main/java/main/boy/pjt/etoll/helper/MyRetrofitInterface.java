package main.boy.pjt.etoll.helper;

import main.boy.pjt.etoll.values.ValueActivity;
import main.boy.pjt.etoll.values.ValueBalance;
import main.boy.pjt.etoll.values.ValueResponse;
import main.boy.pjt.etoll.values.ValueProfile;
import main.boy.pjt.etoll.values.ValueRoad;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Boy Panjaitan on 04/03/2018.
 */

public interface MyRetrofitInterface {

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.getActivities)
    Call<ValueActivity.RetrofitResponse> getActivities(@Field("id") String id);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.topUpBalance)
    Call<ValueResponse> topUpBalance(@Field("id") String id, @Field("nominal") int nominal);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.checkBalance)
    Call<ValueResponse> checkBalance(@Field("memberId") String memberId,
                                       @Field("roadId") String roadId,
                                       @Field("roadType") String roadType);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.placeRoadOrder)
    Call<ValueBalance.RetrofitResponse> placeRoadOrder(@Field("memberId") String memberId,
                                       @Field("roadId") String roadId,
                                       @Field("roadType") String roadType);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.getRoadList)
    Call<ValueRoad.RetrofitResponse> getRoadList(@Field("id") String id);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.getBalance)
    Call<ValueBalance.RetrofitResponse> getBalance(@Field("id") String id);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.registerPath)
    Call<ValueResponse> register(@Field("username") String username,
                                 @Field("name") String name,
                                 @Field("password") String password,
                                 @Field("email") String email,
                                 @Field("phone") String phone);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.loginPath)
    Call<ValueProfile.RetrofitResponse> login(@Field("id") String id, @Field("password") String password);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.loadProfile)
    Call<ValueProfile.RetrofitResponse> loadProfile(@Field("token") String token, @Field("costumer") String costumer);

    @FormUrlEncoded
    @POST(MyConstant.Endpoint.updateProfile)
    Call<ValueResponse> updateProfile(@Field("id") String username,
                                      @Field("name") String name,
                                      @Field("password") String password,
                                      @Field("email") String email,
                                      @Field("phone") String phone);
}
