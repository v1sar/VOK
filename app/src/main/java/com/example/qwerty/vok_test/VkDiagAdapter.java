package com.example.qwerty.vok_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

/**
 * Created by qwerty on 01.11.16.
 */

public class VkDiagAdapter extends BaseAdapter {
    private ArrayList<String> users, messages;
    private Context context;
    private VKList<VKApiDialog> list;

    public VkDiagAdapter(Context context, ArrayList<String> users, ArrayList<String> messages,  VKList<VKApiDialog> list) {
        this.users = users;
        this.messages = messages;
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SetData setData = new SetData();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.style_list_view, null);

        setData.user_name = (TextView) view.findViewById(R.id.user_name);
        setData.msg = (TextView) view.findViewById(R.id.msg);

        setData.user_name.setText(users.get(position));
        setData.msg.setText(messages.get(position));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, list.get(position).message.user_id,
                        VKApiConst.MESSAGE, "Test msg"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        System.out.println("Soobwenie otpravleno");
                    }
                });
            }
        });

        return view;
    }

    public class SetData{
        TextView user_name, msg;
    }
}
