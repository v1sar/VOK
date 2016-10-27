package com.example.qwerty.vok_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(Arrays.asList(fingerprints));
        View.OnClickListener oclVK = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(MainActivity.this, scope);
            }
        };
        findViewById(R.id.logVK).setOnClickListener(oclVK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(MainActivity.this, "Good", Toast.LENGTH_SHORT).show();
// Пользователь успешно авторизовался
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(MainActivity.this, "Bad", Toast.LENGTH_SHORT).show();
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
