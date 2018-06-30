package ru.abdulmadzhidov.client_application;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static ru.abdulmadzhidov.client_application.APIClient.getApiInterface;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        if (getIntent().getBooleanExtra("isGivingAccess", false)) {
            requestInfoFromRootApp();
        }


        RxView.clicks(button)
                .subscribe(view -> requestInfoFromRootApp());
    }

    private void requestInfoFromRootApp() {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                token = data.getStringExtra("token");
                fetchUserEmail();
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fetchUserEmail() {
        getApiInterface().getUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> textView.setText("Email: " + response.getEmail() + " \nClient token: " + token));
    }
}
