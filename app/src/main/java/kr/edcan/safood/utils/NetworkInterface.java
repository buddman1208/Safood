/*
 * Created by KOHA on 2016.
 * Copyright by Sunrin Internet High School EDCAN
 * All rights reversed.
 */

package kr.edcan.safood.utils;

import java.util.ArrayList;
import java.util.Date;

import kr.edcan.safood.models.Food;
import kr.edcan.safood.models.Group;
import kr.edcan.safood.models.GroupMemo;
import kr.edcan.safood.models.SafoodGroup;
import kr.edcan.safood.models.User;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
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


    @POST("/user/destroySelf")
    @FormUrlEncoded
    Call<ResponseBody> destroyUser(@Field("apikey") String apikey);

    @POST("/user/getSelfInfo")
    @FormUrlEncoded
    Call<User> getSelfInfo(@Field("apikey") String apikey);

//    @POST("/user/getSearchHistory")
//    @FormUrlEncoded
//    Call<ArrayList<>> getSearchHistory(@Field("apikey") String apikey);

    @POST("/user/updateSelfinfo")
    @Multipart
    Call<User> updateSelfInfo(
            @Part("apikey") RequestBody apikey,
            @Part("userid") RequestBody userid,
            @Part("password") RequestBody password,
            @Part("username") RequestBody username,
            @Part("profileImage") RequestBody imageFile
    );

    @POST("/user/updateException")
    @FormUrlEncoded
    Call<ResponseBody> updateException(
            @Field("apikey") String apikey, @Field("query") String query);

    @POST("/user/addKeywordException")
    @FormUrlEncoded
    Call<ResponseBody> addKeywordException(
            @Field("apikey") String apikey, @Field("keyword") String keyword);


    @POST("/user/removeKeywordException")
    @FormUrlEncoded
    Call<ResponseBody> removeKeywordException(
            @Field("apikey") String apikey, @Field("keyword") String keyword);


    @POST("/user/searchUser")
    @FormUrlEncoded
    Call<ArrayList<User>> searchUser(
            @Field("searchByName") boolean searchByName,
            @Field("username") String username,
            @Field("userid") String userid);

    @POST("/group/searchGroup")
    @FormUrlEncoded
    Call<ArrayList<Group>> searchGroup(
            @Field("query") String groupname);


    @POST("/group/joinGroup")
    @FormUrlEncoded
    Call<ResponseBody> joinGroup(
            @Field("apikey") String apikey, @Field("groupid") String groupid);

    @POST("/group/leaveGroup")
    @FormUrlEncoded
    Call<ResponseBody> leaveGroup(
            @Field("apikey") String apikey, @Field("groupid") String groupid);

    @POST("/group/getGroupInfo")
    @FormUrlEncoded
    Call<Group> getGroupInfo(
            @Field("groupid") String groupid);

    @POST("/group/admin/checkGroupName")
    @FormUrlEncoded
    Call<ResponseBody> checkGroupName(
            @Field("groupname") String groupname);

    @POST("/group/admin/createGroup")
    @Multipart
    Call<Group> createGroup(
            @Part("file\"; filename=\"image.jpg\"") RequestBody image,
            @Part("groupname") RequestBody groupname,
            @Part("apikey") RequestBody groupAdminid,
            @Part("limit") RequestBody limit);

    @POST("/group/admin/destroyGroup")
    @FormUrlEncoded
    Call<Group> destroyGroup(
            @Field("userid") String userid,
            @Field("groupid") String groupid);

    @POST("/group/admin/modifyGroupInfo")
    @FormUrlEncoded
    Call<ResponseBody> modifyGroupInfo(
            @Field("userid") String userid,
            @Field("groupname") String groupname,
            @Field("grouptag") String grouptag);


    @POST("/group/admin/addUser")
    @FormUrlEncoded
    Call<ResponseBody> addUserToGroup(
            @Field("userid") String userid,
            @Field("groupid") String groupid,
            @Field("targetid") String targetid);


    @POST("/group/admin/removeUser")
    @FormUrlEncoded
    Call<ResponseBody> removeUserToGroup(
            @Field("userid") String userid,
            @Field("groupid") String groupid,
            @Field("targetid") String targetid);

    @POST("/search/barcode")
    @FormUrlEncoded
    Call<Food> searchByBarcode(
            @Field("apikey") String apikey,
            @Field("barcode") String barcode,
            @Field("date") Date date
    );
    @POST("/safood/newSafoodGroup")
    @FormUrlEncoded
    Call<ArrayList<SafoodGroup>> newSafoodGroup(
            @Field("groupname") String groupname,
            @Field("apikey") String apikey,
            @Field("color") int color
    );
    @POST("/safood/addToSafoodGroup")
    @FormUrlEncoded
    Call<ArrayList<SafoodGroup>> addToSafoodGroup(
            @Field("groupid") String groupid,
            @Field("apikey") String apikey,
            @Field("foodid") String foodid
    );
    @POST("/safood/removeFromSafoodGroup")
    @FormUrlEncoded
    Call<ArrayList<SafoodGroup>> removeFromSafoodGroup(
            @Field("groupid") String groupid,
            @Field("apikey") String apikey,
            @Field("foodid") String foodid
    );
    @POST("/safood/getSafoodGroupList")
    @FormUrlEncoded
    Call<ArrayList<SafoodGroup>> getSafoodGroupList(
            @Field("apikey") String apikey
    );

    @POST("/search")
    @FormUrlEncoded
    Call<ArrayList<Food>> searchFoodbyKeyword(
            @Field("foodname") String foodname
    );
    @POST("/group/memoAdd")
    @FormUrlEncoded
    Call<ArrayList<GroupMemo>> addMemo(
            @Field("title") String title,
            @Field("content") String content,
            @Field("color") int color,
            @Field("foods") String foods,
            @Field("groupid") String groupid
    );

    @POST("/group/memo")
    @FormUrlEncoded
    Call<ArrayList<GroupMemo>> getGroupMemo(
            @Field("groupid") String groupid
    );



}

