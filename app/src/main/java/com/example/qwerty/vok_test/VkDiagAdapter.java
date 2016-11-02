package com.example.qwerty.vok_test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by qwerty on 01.11.16.
 */

public class VkDiagAdapter extends BaseAdapter {
    private ArrayList<String> users, messages;
    private ArrayList<Integer> messagesOut;
    private Context context;
    private VKList<VKApiDialog> list;
    private final static String TAG = MainActivity.class.getSimpleName();

    public VkDiagAdapter(Context context, ArrayList<String> users, ArrayList<String> messages,  VKList<VKApiDialog> list) {
        this.users = users;
        this.messages = messages;
        this.context = context;
        this.list = list;
    }

    public VkDiagAdapter(Context context, ArrayList<String> users, ArrayList<Integer> messages) {
        this.users = users;
        this.messagesOut = messages;
        this.context = context;
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

//messages - out, users - in

        if (list == null) {
            if (messagesOut.get(position) == 0) {
                setData.user_name.setText(users.get(position));
            } else {
                setData.msg.setText(users.get(position));
            }

        } else {
            setData.msg.setText(messages.get(position));
            setData.user_name.setText(users.get(position));
        }

        if (list != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> inList = new ArrayList<>();
                    final ArrayList<Integer> outList = new ArrayList<>();
                    final int id = list.get(position).message.user_id;
                    Log.d(TAG, "1");
                    VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id,
                            VKApiConst.COUNT, "10"));
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            super.onComplete(response);
                            try {
                                JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                                VKApiMessage[] msg = new VKApiMessage[array.length()];
                                for (int i = 0; i < array.length(); i++) {
                                    VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                                    msg[i] = mes;
                                }

                                for (VKApiMessage mess : msg) {
                                    if (mess.out) {
                                        outList.add(1);
                                    } else {
                                        outList.add(0);
                                    }
                                        inList.add(mess.body);
                                }
                                Log.d(TAG, "2");
                                Intent intent = new Intent(context, SendMessageVK.class).putExtra("id", id)
                                        .putExtra("in", inList)
                                        .putExtra("out", outList);
                                context.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }

            });
        }
        return view;
    }

    public class SetData{
        TextView user_name, msg;
    }
}
