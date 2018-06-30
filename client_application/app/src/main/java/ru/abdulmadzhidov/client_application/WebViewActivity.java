package ru.abdulmadzhidov.client_application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static ru.abdulmadzhidov.client_application.APIClient.URL;

public class WebViewActivity extends AppCompatActivity {

    String client_id = "test_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.loadUrl(URL + "/api/oauth?client_id="+client_id+"&scope=email&redirect_url=testapp.test");
    }

    class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (url != null) {
                if (url.startsWith("http://testapp.test")) {
                    String result = url.substring(url.indexOf("#")+1);
                    if (!result.equals("error")) {
                        Intent intent = new Intent();
                        intent.putExtra("token", result);
                        setResult(RESULT_OK, intent);
                    } else {
                        Intent intent = new Intent();
                        setResult(RESULT_CANCELED, intent);
                    }
                    finish();
                }
            }
        }
    }
}
