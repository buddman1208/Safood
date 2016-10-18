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

    @POST("/auth/local/login")
    @FormUrlEncoded
    Call<User> nativeLogin(@Field("email") String email, @Field("password") String password);

    @POST("/auth/local/authenticate")
    @FormUrlEncoded
    Call<User> authenticateUser(@Field("token") String token);

    @POST("/auth/local/register")
    @FormUrlEncoded
    Call<User> registerUser(@Field("name") String name,
                            @Field("password") String password, @Field("email") String email);

}

