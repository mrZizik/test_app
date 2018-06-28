package ru.abdulmadzhidov.client_application;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit;

    public static APIInterface apiInterface;


    static Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://199.247.30.27:5000")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    static APIInterface getApiInterface() {
        if (apiInterface == null) {
            apiInterface = getRetrofit().create(APIInterface.class);
        }
        return apiInterface;
    }

}
