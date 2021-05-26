package com.example.vcommunity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiConfig;
import com.vk.api.sdk.VKTokenExpiredHandler;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    protected static final int AUTH_REQ_CODE = 385394;
    protected VKTokenExpiredHandler expiredTokenHandler = new VKTokenExpiredHandler() {
        @Override
        public void onTokenExpired() {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivityForResult(intent, AUTH_REQ_CODE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        VK.addTokenExpiredHandler(expiredTokenHandler);
        if (!VK.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivityForResult(intent, AUTH_REQ_CODE);
        }
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_main, R.id.navigation_messages, R.id.navigation_notifications,
                R.id.navigation_stats, R.id.navigation_settings)
                .build();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment =
                (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_REQ_CODE) {
            if (!(resultCode == RESULT_OK)) {
                Toast.makeText(this, R.string.auth_fail_text, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivityForResult(intent, AUTH_REQ_CODE);
            }
        }
    }
}