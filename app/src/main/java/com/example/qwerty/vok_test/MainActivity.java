package com.example.qwerty.vok_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.flurry.android.FlurryAgent;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.util.VKUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    private ListView listView;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View.OnClickListener oclVK = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(MainActivity.this, scope);
            }
        };
       // findViewById(R.id.logVK).setOnClickListener(oclVK);
///////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ///////
        /*** Flurry Analitics ***/
        final String FLURRY_KEY = "P5J926ZM3YPF4WTPRPVY";
        FlurryAgent.init(MainActivity.this, FLURRY_KEY);
        /*** ***/

        ///
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main, new HelloFragment());
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FlurryAgent.onStartSession(MainActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        FlurryAgent.onEndSession(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                /*** Flurry Analitics ***/
                String eventId = "login";
                FlurryAgent.logEvent(eventId);
                /*** ***/
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                ///some shit

//                listView = frag.getListView();
                bundle.clear();
                bundle.putString("status", "success");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);
                firebaseAnalytics.logEvent("login",bundle);
                VKRequest request = VKApi.messages().get(VKParameters.from(VKApiConst.COUNT, 10));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                       // listView = (ListView) frag.getView().findViewById(R.id.list);

                        VKApiGetMessagesResponse getMessagesResponse = (VKApiGetMessagesResponse) response.parsedModel;
                        VKList<VKApiMessage> list = getMessagesResponse.items;

                        ArrayList<String> arrayList = new ArrayList<>();

                        for(VKApiMessage msg: list) {
                            arrayList.add(msg.body);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1, arrayList);
                        listView.setAdapter(arrayAdapter);


                    }
                });

            }
            @Override
            public void onError(VKError error) {
                bundle.clear();
                bundle.putString("status", "failed");
                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);
                firebaseAnalytics.logEvent("login",bundle);
                /*** Flurry Analitics ***/
                String eventId = "login failed";
                FlurryAgent.logEvent(eventId);
                /*** ***/
                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_vk) {
            setDiagFrag();//VKSdk.login(MainActivity.this, scope);
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setDiagFrag(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment hello = fragmentManager.findFragmentById(R.id.content_main);
        if (hello != null && hello.isAdded())
        {
            transaction.remove(hello);
        }

        transaction.replace(R.id.content_main, new DialogFragment());
        transaction.commit();
    }

}
