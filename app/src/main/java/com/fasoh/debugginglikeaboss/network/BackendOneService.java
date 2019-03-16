package com.fasoh.debugginglikeaboss.network;

import com.fasoh.debugginglikeaboss.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BackendOneService {
    @GET("people")
    Call<List<User>> getPeople();

    @POST("people")
    Call<User> postUser(@Body User user);
}
