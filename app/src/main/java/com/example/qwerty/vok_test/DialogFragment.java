package com.example.qwerty.vok_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



/**
 * Created by qwerty on 31.10.16.
 */

public class DialogFragment extends Fragment {
    private final static String TAG = MainActivity.class.getSimpleName();
    protected View tView;
//    private ListView listView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "ggggggggg");
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        tView = view;
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "statatatatatatat");
    }
}
