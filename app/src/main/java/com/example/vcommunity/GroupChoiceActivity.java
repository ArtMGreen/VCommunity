package com.example.vcommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.sdk.api.account.AccountService;
import com.vk.sdk.api.account.dto.AccountUserSettings;
import com.vk.sdk.api.groups.GroupsService;
import com.vk.sdk.api.groups.dto.GroupsFields;
import com.vk.sdk.api.groups.dto.GroupsFilter;
import com.vk.sdk.api.groups.dto.GroupsGetExtendedResponse;
import com.vk.sdk.api.groups.dto.GroupsGroupFull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupChoiceActivity extends AppCompatActivity {
    RecyclerView groups_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_choice);


        final int[] userIdarr = new int[1];
        ArrayList<GroupsFilter> filter = new ArrayList<>();
        filter.add(GroupsFilter.ADMIN);
        ArrayList<GroupsFields> fields = new ArrayList<>();
        fields.add(GroupsFields.MEMBERS_COUNT);
        VK.execute(new AccountService().accountGetProfileInfo(), new VKApiCallback<AccountUserSettings>() {
            @Override
            public void success(AccountUserSettings result) {
                userIdarr[0] = result.getId();
            }

            @Override
            public void fail(@NotNull Exception error) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        int user_id = userIdarr[0];

        ArrayList<ArrayList<String>> groupsParameters = new ArrayList<>();
        VK.execute(new GroupsService().groupsGetExtended(user_id, filter, fields, 0, 1000),
                new VKApiCallback<GroupsGetExtendedResponse>() {
            @Override
            public void success(GroupsGetExtendedResponse result) {
                List<GroupsGroupFull> groups = result.getItems();
                for (GroupsGroupFull group: groups) {
                    ArrayList<String> entry = new ArrayList<>();
                    entry.add(group.getName());
                    entry.add("Members:" + group.getMembersCount().toString());
                    entry.add(group.getId().toString());
                    groupsParameters.add(entry);
                    // я/мы ждём АПИ ответа как соловей лета
                    Activity act = GroupChoiceActivity.this;
                    groups_list_view = findViewById(R.id.groups_list_view);
                    groups_list_view.setLayoutManager(new LinearLayoutManager(act));
                    GroupsChoiceAdapter adapter = new GroupsChoiceAdapter(groupsParameters, act);
                    groups_list_view.setAdapter(adapter);
                }
            }

            @Override
            public void fail(@NotNull Exception error) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        // раньше код, ждавший как соловей лета, был тут. Умора, список групп показывался пустым, ха-ха!
    }
}