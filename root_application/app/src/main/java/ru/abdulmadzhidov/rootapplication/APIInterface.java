package ru.abdulmadzhidov.rootapplication;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.abdulmadzhidov.rootapplication.model.User;

public interface APIInterface {

    @GET("/api/get_user/{token}")
    Observable<User> getUser(@Path("token") String token);

    @POST("/api/signup")
    Call<User> createUser(@Body User user);

}
