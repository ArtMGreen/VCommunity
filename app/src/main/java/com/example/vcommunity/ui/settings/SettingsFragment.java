package com.example.vcommunity.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.vcommunity.R;
import com.vk.api.sdk.VK;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final Button logoutButton = root.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(v -> {
            VK.logout();
            requireActivity().finishAffinity();
        });
        return root;
    }
}
