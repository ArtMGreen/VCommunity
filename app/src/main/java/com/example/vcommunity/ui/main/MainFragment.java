package com.example.vcommunity.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vcommunity.MainActivity;
import com.example.vcommunity.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.sdk.api.base.dto.BaseBoolInt;
import com.vk.sdk.api.base.dto.BaseOkResponse;
import com.vk.sdk.api.groups.GroupsService;
import com.vk.sdk.api.groups.dto.GroupsGetSettingsResponse;
import com.vk.sdk.api.groups.dto.GroupsGroupAudio;
import com.vk.sdk.api.groups.dto.GroupsGroupDocs;
import com.vk.sdk.api.groups.dto.GroupsGroupPhotos;
import com.vk.sdk.api.groups.dto.GroupsGroupTopics;
import com.vk.sdk.api.groups.dto.GroupsGroupVideo;
import com.vk.sdk.api.groups.dto.GroupsGroupWall;
import com.vk.sdk.api.groups.dto.GroupsGroupWiki;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    // со всех этих элементов впоследствии "пылесосится" пользовательский ввод
    TextView titleEdit;
    TextView screenNameEdit;
    TextView descEdit;
    TextView websiteEdit;
    CheckBox wallCheckBox;
    CheckBox topicsCheckBox;
    CheckBox photosCheckBox;
    CheckBox videoCheckBox;
    CheckBox audioCheckBox;
    CheckBox docsCheckBox;
    CheckBox wikiCheckBox;
    CheckBox obsFCheckBox;
    Button saveButton;
    int group_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity main_act = (MainActivity)getActivity();
        group_id = main_act.getGroupId();
        // Баг, который стал фичей! Предлагаем пользователю попробовать переключение между вкладками
        if (group_id == 0) {
            View root;
            root = inflater.inflate(R.layout.fragment_main, container, false);
            final TextView textView = root.findViewById(R.id.text_main);
            textView.setText(R.string.tabs_text);
            return root;
        }
        else {
            View root;
            root = inflater.inflate(R.layout.fragment_main_alternative, container, false);
            titleEdit = root.findViewById(R.id.titleEdit);
            screenNameEdit = root.findViewById(R.id.screenNameEdit);
            descEdit = root.findViewById(R.id.descEdit);
            websiteEdit = root.findViewById(R.id.websiteEdit);
            wallCheckBox = root.findViewById(R.id.wallCheckBox);
            topicsCheckBox = root.findViewById(R.id.topicsCheckBox);
            photosCheckBox = root.findViewById(R.id.photosCheckBox);
            videoCheckBox = root.findViewById(R.id.videoCheckBox);
            audioCheckBox = root.findViewById(R.id.audioCheckBox);
            docsCheckBox = root.findViewById(R.id.docsCheckBox);
            wikiCheckBox = root.findViewById(R.id.wikiCheckBox);
            obsFCheckBox = root.findViewById(R.id.obsFCheckBox);
            saveButton = root.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(v -> sendSettings());
            fillViews();
            return root;
        }
    }

    public void fillViews() {
        VK.execute(new GroupsService().groupsGetSettings(group_id), new VKApiCallback<GroupsGetSettingsResponse>() {
            @Override
            public void success(GroupsGetSettingsResponse result) {
                titleEdit.setText(result.getTitle());
                descEdit.setText(result.getDescription());
                screenNameEdit.setText(result.getAddress());
                websiteEdit.setText(result.getWebsite());

                GroupsGroupWall wall = result.getWall();
                wallCheckBox.setActivated(wall != GroupsGroupWall.DISABLED);

                GroupsGroupTopics topics = result.getTopics();
                topicsCheckBox.setActivated(topics != GroupsGroupTopics.DISABLED);

                GroupsGroupPhotos photos = result.getPhotos();
                photosCheckBox.setActivated(photos != GroupsGroupPhotos.DISABLED);

                GroupsGroupVideo video = result.getVideo();
                videoCheckBox.setActivated(video != GroupsGroupVideo.DISABLED);

                GroupsGroupAudio audio = result.getAudio();
                audioCheckBox.setActivated(audio != GroupsGroupAudio.DISABLED);

                GroupsGroupDocs docs = result.getDocs();
                docsCheckBox.setActivated(docs != GroupsGroupDocs.DISABLED);

                GroupsGroupWiki wiki = result.getWiki();
                wikiCheckBox.setActivated(wiki != GroupsGroupWiki.DISABLED);

                BaseBoolInt obsf = result.getObsceneFilter();
                obsFCheckBox.setActivated(obsf != BaseBoolInt.NO);
            }

            @Override
            public void fail(@NotNull Exception e) {
                // Странно, попытка автозаполнения полей ВСЕГДА приводит меня сюда!
                // Обратился в Поддержку ВКонтакте по поводу этого метода API
                Toast.makeText(getActivity(), R.string.fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendSettings() {
        String title = titleEdit.getText().toString();
        title = title.trim();
        if (title.isEmpty()) title = null;
        String desc = descEdit.getText().toString();
        desc = desc.trim();
        if (desc.isEmpty()) desc = null;
        String screen_name = screenNameEdit.getText().toString();
        screen_name = screen_name.trim();
        if (screen_name.isEmpty()) screen_name = null;
        String website = websiteEdit.getText().toString();
        website = website.trim();
        if (website.isEmpty()) website = null;

        int wall;
        int topics;
        int photos;
        int video;
        int audio;
        int docs;
        int wiki;
        boolean obsf;

        if (wallCheckBox.isChecked()) {
            wall = 1;
        } else {
            wall = 0;
        }

        if (topicsCheckBox.isChecked()) {
            topics = 1;
        } else {
            topics = 0;
        }

        if (photosCheckBox.isChecked()) {
            photos = 1;
        } else {
            photos = 0;
        }

        if (videoCheckBox.isChecked()) {
            video = 1;
        } else {
            video = 0;
        }

        if (audioCheckBox.isChecked()) {
            audio = 1;
        } else {
            audio = 0;
        }

        if (docsCheckBox.isChecked()) {
            docs = 1;
        } else {
            docs = 0;
        }

        if (wikiCheckBox.isChecked()) {
            wiki = 1;
        } else {
            wiki = 0;
        }

        obsf = obsFCheckBox.isChecked();
        // кошмар программиста: нет аргументов по умолчанию,
        // 10 тысяч null в аргументах уже здесь и ещё несколько на подходе
        VK.execute(new GroupsService().groupsEdit(group_id, title, desc, screen_name, null,
                website, null, null, null, null, null, null,
                null, null, null, null, wall, topics,
                photos, video, audio, null, null, null, null, docs, wiki,
                null, null, null, null, null, null,
                null, null, null, null, null,
                obsf, null, null, null, null, null,
                null), new VKApiCallback<BaseOkResponse>() {
            @Override
            public void success(BaseOkResponse result) {
                Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_LONG).show();
            }

            @Override
            public void fail(@NotNull Exception e) {
                Toast.makeText(getActivity(), R.string.fail, Toast.LENGTH_LONG).show();
            }
        });
    }
}