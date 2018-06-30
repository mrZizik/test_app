package ru.abdulmadzhidov.rootapplication;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.abdulmadzhidov.rootapplication.model.User;

import static ru.abdulmadzhidov.rootapplication.APIClient.getApiInterface;

public class MainActivity extends AppCompatActivity {

    private TextView emailText;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailText = findViewById(R.id.textView);
        if (User.instance.getEmail() == null) {
            fetchUserData();
        }
        emailText.setText("My email: " + User.instance.getEmail() + " \nMy Token: " + getSharedPreferences("shared", MODE_PRIVATE).getString("token", ""));
    }
    private void fetchUserData() {
        getApiInterface().getUser(getSharedPreferences("shared", MODE_PRIVATE).getString("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    User.instance.setEmail(response.getEmail());
                    User.instance.setToken(getSharedPreferences("shared", MODE_PRIVATE).getString("token", ""));
                    emailText.setText("My email: " + User.instance.getEmail() + " \nMy Token: " + getSharedPreferences("shared", MODE_PRIVATE).getString("token", ""));
                });
    }
}
