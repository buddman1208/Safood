/*
 * Created by KOHA on 2016.
 * Copyright by Sunrin Internet High School EDCAN
 * All rights reversed.
 */

package kr.edcan.safood.utils;

import kr.edcan.safood.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetworkInterface {
    // Authenticate

    @POST("/auth/login")
    @FormUrlEncoded
    Call<User> nativeLogin(@Field("userid") String email, @Field("password") String password);

    @POST("/auth/login/auto")
    @FormUrlEncoded
    Call<User> authenticateUser(@Field("userid") String userid, @Field("apikey") String token);

    @POST("/auth/register")
    @FormUrlEncoded
    Call<User> registerUser(@Field("username") String name,
                            @Field("password") String password, @Field("userid") String email);

}

