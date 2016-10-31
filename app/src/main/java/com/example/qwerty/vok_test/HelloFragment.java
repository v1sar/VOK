package com.example.qwerty.vok_test;

/**
 * Created by qwerty on 31.10.16.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelloFragment extends Fragment {
    private final static String TAG = MainActivity.class.getSimpleName();
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "heloooooo");
        return inflater.inflate(R.layout.fragment_hello, container, false);
    }

}
