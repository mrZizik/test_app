package ru.abdulmadzhidov.client_application;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.abdulmadzhidov.client_application.model.User;

public interface APIInterface {
    @GET("/api/get_user/{token}")
    Observable<User> getUser(@Path("token") String token);
}
