package ru.abdulmadzhidov.rootapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.abdulmadzhidov.rootapplication.model.ClientAccessRequest;
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
        emailText.setText("My email: " + User.instance.getEmail());

        Button giveAccessToClientAppButton = findViewById(R.id.button2);

        RxView.clicks(giveAccessToClientAppButton)
                .subscribe(view -> giveAccessToClient());


        if (getIntent().getStringExtra("client_id") != null && !getIntent().getStringExtra("client_id").isEmpty()) {
            alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Alert")
                    .setMessage(getIntent().getStringExtra("client_id") + " app is requesting access to your " + getIntent().getStringExtra("scope"))
                    .setPositiveButton("OK",
                            (dialog, which) -> {
                                requestAccessForClient();
                            })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        Intent intent = new Intent();
                        setResult(200, intent);
                        finish();
                    }).create();
            alertDialog.show();
        }
    }

    private void giveAccessToClient() {
        Intent i = getPackageManager().getLaunchIntentForPackage("ru.abdulmadzhidov.client_application");
        i.putExtra("isGivingAccess", true);
        startActivity(i);
    }

    private void requestAccessForClient() {
        ClientAccessRequest clientAccessRequest = new ClientAccessRequest();
        clientAccessRequest.setClient_id(getIntent().getStringExtra("client_id"));
        clientAccessRequest.setClient_secret(getIntent().getStringExtra("client_secret"));
        clientAccessRequest.setScope(getIntent().getStringExtra("scope"));

        getApiInterface().requestAccess(clientAccessRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Intent intent = new Intent();
                    intent.putExtra("token", response.getClient_token());
                    setResult(RESULT_OK, intent);
                    alertDialog.dismiss();
                    finish();
                });
    }

    private void fetchUserData() {
        getApiInterface().getUser(getSharedPreferences("shared", MODE_PRIVATE).getString("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    User.instance.setEmail(response.getEmail());
                    User.instance.setToken(getSharedPreferences("shared", MODE_PRIVATE).getString("token", ""));
                    emailText.setText("My email: " + User.instance.getEmail());
                });
    }
}
