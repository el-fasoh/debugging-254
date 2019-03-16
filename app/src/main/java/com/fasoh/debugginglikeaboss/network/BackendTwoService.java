package com.fasoh.debugginglikeaboss.network;


import com.fasoh.debugginglikeaboss.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackendTwoService {
    @POST("account")
    Call<User> createAccount(@Body User user);
}
