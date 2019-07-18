package com.fitbit.blesession;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @POST("/event")
    Call<ResponseBody> postTemp(@Body Data data);

    @GET("/event")
    Call <List<String>> getWeather();

    class Service{
        private static Api sInstance;

        public synchronized static Api Get(){
            if(sInstance == null){
                sInstance = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.115:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Api.class);
            }
            return sInstance;
        }
    }


}
