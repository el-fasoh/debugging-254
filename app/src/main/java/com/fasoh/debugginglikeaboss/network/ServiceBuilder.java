package com.fasoh.debugginglikeaboss.network;

import android.content.Context;

import com.fasoh.debugginglikeaboss.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
    static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    static Gson gson = new GsonBuilder().setLenient().create();
    static ChuckInterceptor chuckInterceptor = null;

    static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson));


    static <S> S createBaseService(Class<S> serviceClass, String baseUrl, Context context) {

        List<Interceptor> interceptors = httpClientBuilder.interceptors();

        //Add chuck interceptor
        if (BuildConfig.DEBUG && chuckInterceptor == null && context != null) {
            ChuckInterceptor interceptor = new ChuckInterceptor(context);
            chuckInterceptor = interceptor;
            httpClientBuilder.addInterceptor(interceptor);
        }

        //Logging interceptor
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (!interceptors.contains(loggingInterceptor)) {
                httpClientBuilder.addInterceptor(loggingInterceptor);
            }
        }

        Retrofit build = retrofitBuilder.client(httpClientBuilder.build())
                .baseUrl(baseUrl).build();

        return build.create(serviceClass);
    }
}
