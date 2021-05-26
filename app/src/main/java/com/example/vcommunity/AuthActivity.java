package com.example.vcommunity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import java.util.ArrayList;
import java.util.Objects;


public class AuthActivity extends AppCompatActivity {

    protected Button auth_button;
    protected ArrayList<VKScope> scopes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_auth);
        auth_button = findViewById(R.id.auth_button);
        scopes = new ArrayList<>();
        scopes.add(VKScope.GROUPS);
        scopes.add(VKScope.OFFLINE);
        auth_button.setOnClickListener(v -> VK.login(this, scopes));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(VKAccessToken token) {
                setResult(RESULT_OK);
                finish();
            }

            public void onLoginFailed(int errorCode) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
