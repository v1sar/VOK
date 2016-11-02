package com.example.qwerty.vok_test;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


/**
 * Created by qwerty on 01.11.16.
 */

public class SendMessageVK extends AppCompatActivity {

    ArrayList<String> inListDate = new ArrayList<String>();
    ArrayList<String> outListDate = new ArrayList<String>();
    ArrayList<String> inList = new ArrayList<>();
    ArrayList<String> outList = new ArrayList<>();
    int id = 0;

    EditText text;
    Button send;
    ListView listView;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "IM HERE");
        setContentView(R.layout.dialogs);
        inList = getIntent().getStringArrayListExtra("in");
        outList = getIntent().getStringArrayListExtra("out");
        inListDate = getIntent().getStringArrayListExtra("inDate");
        outListDate = getIntent().getStringArrayListExtra("outDate");
        id = getIntent().getIntExtra("id", 0);
//
//        Arrays.sort(inList.toArray(), Collections.reverseOrder());
//        Arrays.sort(outList.toArray(), Collections.reverseOrder());

        text = (EditText) findViewById(R.id.textMsgSend);
        listView = (ListView) findViewById(R.id.listMsg);

        listView.setAdapter(new VkDiagAdapter(SendMessageVK.this, inList, outList, inListDate, outListDate));

        send = (Button) findViewById(R.id.sendMsg);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, id,
                        VKApiConst.MESSAGE, text.getText().toString()));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        System.out.println("Soobwenie otpravleno");
                    }
                });
            }
        });
    }
}
