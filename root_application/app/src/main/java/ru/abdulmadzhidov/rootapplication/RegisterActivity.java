package ru.abdulmadzhidov.rootapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;
import ru.abdulmadzhidov.rootapplication.model.User;

import static ru.abdulmadzhidov.rootapplication.APIClient.getApiInterface;

public class RegisterActivity extends AppCompatActivity {

    Button signup;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.button);

        if (!getSharedPreferences("shared", MODE_PRIVATE).getString("token", "").isEmpty()
                && !getSharedPreferences("shared", MODE_PRIVATE).getString("email", "").isEmpty()) {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        RxView.clicks(signup)
                .observeOn(Schedulers.io())
                .subscribe(view -> registerUser());
    }

    private void registerUser() throws IOException {
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fields should be filled", Toast.LENGTH_LONG).show();
            return;
        } else {
            User.instance.setEmail(email.getText().toString());
            User.instance.setPassword(password.getText().toString());
            User.instance.setToken(getApiInterface().createUser(User.instance).execute().body().getToken());
            runOnUiThread(() -> {
                getSharedPreferences("shared", MODE_PRIVATE).edit().putString("token", User.instance.getToken()).apply();
                getSharedPreferences("shared", MODE_PRIVATE).edit().putString("email", User.instance.getEmail()).apply();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            });
        }
    }
}
