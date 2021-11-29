package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.edu.cuhk.ie.iems5722.a2_1155162650.databinding.ActivityMainBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    private List<String> list = new ArrayList<>();
    private ListView listView;
    private Map<String, String> map = new HashMap<>();
    private String ip = "47.119.128.222";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Toolbar t = binding.appBarMain.toolbar;
        setContentView(binding.getRoot());
        Context context = this;
        t.setTitle("IEMS5722");
        //binding.appBarMain.toolbar.setTitle("IEMS5722");

        setSupportActionBar(t);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getHttpInfo(context);


//        new HttpTask() {
//
//            @Override
//            public void success() {
//                JSONObject json = super.getResponse();
//                System.out.println(json.toString());
//                String name = "null";
//                String id = "null";
//                try {
//                    JSONArray jsonArray = json.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        if (jsonObject.has("name")) {
//                            name = jsonObject.getString("name");
//                        }
//                        if (jsonObject.has("id")) {
//                            id = jsonObject.getString("id");
//                        }
//                        list.add(name);
//                        map.put(name, id);
//                    }
//                    listView = findViewById(R.id.chatrooms);
//                    listView.setAdapter(new ArrayAdapter<String>(context, R.layout.activity_list_view, R.id.testView, list));
//
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            Object item = listView.getAdapter().getItem(i);
//                            Intent it = new Intent(context, ChatActivity.class);
//                            it.putExtra("name", item.toString());
//                            it.putExtra("id", map.get(item.toString()));
//                            startActivity(it);
//
//                            //Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void failed() {
//
//            }
//        }.execute("http://18.217.125.61/api/a3/get_chatrooms");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    public void runtimeEnableAutoInit() {
//        // [START fcm_runtime_enable_auto_init]
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
//        // [END fcm_runtime_enable_auto_init]
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPostCreate(Bundle saveInstanceState) {
        super.onPostCreate(saveInstanceState);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        Toolbar t = binding.appBarMain.toolbar;
        if (t != null) {
            t.setTitle("IEMS5722");
        }

    }

    public void getHttpInfo(Context context) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://"+ ip +"/api/a3/get_chatrooms").get().build();
        int i = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   System.out.println("12312312");
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    if (response.isSuccessful()){
                        System.out.println(response.toString());
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.body().string());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(json.toString());
                        String name = "null";
                        String id = "null";
                        try {
                            JSONArray jsonArray = json.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.has("name")) {
                                    name = jsonObject.getString("name");
                                }
                                if (jsonObject.has("id")) {
                                    id = jsonObject.getString("id");
                                }
                                list.add(name);
                                map.put(name, id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView = findViewById(R.id.chatrooms);
                                listView.setAdapter(new ArrayAdapter<String>(context, R.layout.activity_list_view, R.id.testView, list));

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Object item = listView.getAdapter().getItem(i);
                                        Intent it = new Intent(context, ChatActivity.class);
                                        it.putExtra("name", item.toString());
                                        it.putExtra("id", map.get(item.toString()));
                                        startActivity(it);

                                        //Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getAsync(Context context) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://" + ip + "/api/a3/get_chatrooms").get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    System.out.println(response.toString());
                    JSONObject json = null;
                    try {
                         json = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(json.toString());
                    String name = "null";
                    String id = "null";
                    try {
                        JSONArray jsonArray = json.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.has("name")) {
                                name = jsonObject.getString("name");
                            }
                            if (jsonObject.has("id")) {
                                id = jsonObject.getString("id");
                            }
                            list.add(name);
                            map.put(name, id);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView = findViewById(R.id.chatrooms);
                            listView.setAdapter(new ArrayAdapter<String>(context, R.layout.activity_list_view, R.id.testView, list));

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Object item = listView.getAdapter().getItem(i);
                                    Intent it = new Intent(context, ChatActivity.class);
                                    it.putExtra("name", item.toString());
                                    it.putExtra("id", map.get(item.toString()));
                                    startActivity(it);

                                    //Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            }
        });
    }

}