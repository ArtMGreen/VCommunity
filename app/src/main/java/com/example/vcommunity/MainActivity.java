package com.example.vcommunity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.vcommunity.ui.main.MainFragment;
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
    protected int group_id = 0;
    protected static final int AUTH_REQ_CODE = 385394;
    protected static final int GROUP_CHOICE_CODE = 837927;
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
        Intent intent = new Intent(MainActivity.this, GroupChoiceActivity.class);
        startActivityForResult(intent, GROUP_CHOICE_CODE);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_main, R.id.navigation_settings)
                .build();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment =
                (NavHostFragment) supportFragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public int getGroupId() {
        return group_id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_REQ_CODE) {
            if (resultCode == RESULT_OK) {}
            else {
                Toast.makeText(this, R.string.auth_fail_text, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivityForResult(intent, AUTH_REQ_CODE);
            }
        }
        else if (requestCode == GROUP_CHOICE_CODE) {
            if (resultCode == RESULT_OK) {
                group_id = Integer.parseInt(data.getStringExtra("group_id"));
            }
            else {
                Toast.makeText(this, R.string.auth_fail_text, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, GroupChoiceActivity.class);
                startActivityForResult(intent, GROUP_CHOICE_CODE);
            }
        }
    }
}