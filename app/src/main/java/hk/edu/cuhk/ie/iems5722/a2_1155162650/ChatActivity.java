package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ChatActivity extends AppCompatActivity {
    private ImageView btn;
    private ListView listView;
    private EditText editText;
    private MyAdapter myAdapter;
    private List<ChatMsgEntity> lists = new ArrayList<>();
    private List<ChatMsgEntity> lists1 = new ArrayList<>();
    private Integer judge = 0;
    private Integer totalPage = 0;
    private Integer statusCode = 0;
    private Integer currentPage = 1;
    private MenuItem menuItem;
    private String id;
    private Integer firstItem;
    private Integer whetherClick = 0;
    private Integer whetherFresh = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        Intent i = getIntent();
        actionBar.setTitle(i.getStringExtra("name"));
//        System.out.println(i.getStringExtra("id"));
        id = i.getStringExtra("id");
        myAdapter = new MyAdapter();

        initView();

        //httpToGet(id, "1");

        getAsync(id, "1");

        //refresh();


//        ImageView view = findViewById(R.id.action_refresh);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view1) {
//                currentPage = 0;
//                lists.clear();
//                httpToGet(id, "1");
//            }
//        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whetherClick = 1;
                judge = 1;
                String content = editText.getText().toString();
                if (content != null && !content.equals("")) {
                    ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
                    chatMsgEntity.setMessage(content);
                    chatMsgEntity.setDate(getDate());
                    chatMsgEntity.setUser("william");
                    lists.add(chatMsgEntity);
                    ChatActivity.this.runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            myAdapter.setList(lists);
                            listView.setAdapter(myAdapter);
                            listView.setSelection(lists.size() - 1);
                            editText.setText("");
                        }
                    });
                    post("http://18.217.125.61/api/a3/send_message", id, "1155162650", chatMsgEntity.getUser(), chatMsgEntity.getMessage());


//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ChatActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                }
//                            });
//                        }
//                    }).start();


//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //myAdapter.notifyDataSetChanged();
//
//                        }
//                    });

                }
            }
        });


    }

    public void post(String... params) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("chatroom_id", params[1])
                .add("user_id", params[2])
                .add("name", params[3])
                .add("message", params[4])
                .build();
        Request request = new Request.Builder()
                .url(params[0])
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        //setResponse(new JSONObject(data));
                        System.out.println(new JSONObject(data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getAsync(String id, String page) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://18.217.125.61/api/a3/get_messages?chatroom_id=" + id + "&page=" + page).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    System.out.println(response);
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = null;
                    try {
                        if (jsonObj.has("data")) {
                            json = jsonObj.getJSONObject("data");
                            if (json.has("total_pages")) {
                                totalPage = json.getInt("total_pages");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray jsonArray = json.getJSONArray("messages");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
                            if (jsonObject.has("message")) {
                                chatMsgEntity.setMessage(jsonObject.getString("message"));
                            }
                            if (jsonObject.has("message_time")) {
                                chatMsgEntity.setDate(jsonObject.getString("message_time"));
                            }
                            if (jsonObject.has("name")) {
                                chatMsgEntity.setUser(jsonObject.getString("name"));
                            }
                            lists.add(0, chatMsgEntity);
                        }


                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                myAdapter.setList(lists);


                                //listView.setSelection(lists.size() - 1);
                            }
                        });
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                myAdapter = new MyAdapter();
//                                myAdapter.setList(lists);
//                                listView.setAdapter(myAdapter);
//                                listView.setSelection(lists.size() - 1);
//                            }
//                        });

                        //Collections.sort(lists, new SortClass());
                        //Collections.reverse(lists);
//                    lists = lists.stream().sorted((t1, t2) -> {
//                        return Long.compare(convertTimeToLong(t2.getDate()), convertTimeToLong(t1.getDate()));
//                    }).collect(Collectors.toList());
                        //lists.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    judge = 0;
                    //myAdapter = new MyAdapter();
                    //myAdapter.notifyDataSetChanged();
//                            HashSet set = new HashSet(lists);
//                            lists.clear();
//                            lists.addAll(set);



                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuItem = menu.findItem(R.id.action_refresh);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                whetherFresh = 1;
                currentPage = 0;
                lists.clear();
                getAsync(id, "1");
//                try {
//                    Thread.sleep(100);
//                    getAsync(id, "1");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //myAdapter.clearList();
                //System.out.println(myAdapter.getCount());
                //myAdapter.setList(lists);
                return true;
            }
        });

        return true;
    }

    @Override
    public void onPostCreate(Bundle saveInstanceState) {
        super.onPostCreate(saveInstanceState);

        refresh();
    }

    public void refresh() {


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                Log.d("kxflog", "onScrollStateChanged" + i);
                System.out.println("firstItem():" + absListView.getFirstVisiblePosition());
                statusCode = i;
                if (statusCode != 0 && firstItem == 0) {
                    if (currentPage < totalPage) {
                        System.out.println(currentPage);
                        currentPage++;
                        System.out.println("print times:");
                        System.out.println(currentPage.toString());
                        getAsync(id, currentPage.toString());

                    }
                }
            }

            //1表示在滑动手在屏幕，4表示在滑动但是手不在屏幕，0表示停止
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("youdi", "firstVisibleItem：" + firstVisibleItem + "visibleItemCount：" + visibleItemCount + "totalItemCount：" +totalItemCount);
                System.out.println("firstVisibleItem:" + firstVisibleItem);
                firstItem = firstVisibleItem;
            }
        });
    }

//    @Override
//    public void onPostCreate(Bundle saveInstanceState) {
//        super.onPostCreate(saveInstanceState);
//        //binding = ActivityMainBinding.inflate(getLayoutInflater());
//        Toolbar t = binding.appBarMain.toolbar;
//        if (t != null) {
//            t.setTitle("General room");
//        }
//
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static Long convertTimeToLong(String time) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime parse = LocalDateTime.parse(time, formatter);
//        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//
//    }

    private static class ViewHolder {
        TextView tv_receive;
        TextView tv_send;
    }

    public void initView() {
        listView = findViewById(R.id.listView);
        btn = findViewById(R.id.btn_send);
        editText = findViewById(R.id.edit_query);
    }

    public String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return format.format(new Date());
    }



    private class MyAdapter extends BaseAdapter {
        private List<ChatMsgEntity> devices = new ArrayList<>();

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int i) {
            return devices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //view = View.inflate(getApplicationContext(), R.layout.activity_chat, null);
//            TextView textView = new TextView(getApplicationContext());
//            textView.setText(lists.get(i).getMessage() + "\n" + lists.get(i).getDate());
//            textView.setTextSize(20);
//            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
//            textView.setBackgroundColor(Color.GREEN);
//            textView.setGravity(Gravity.RIGHT);
//            textView.setTextColor(Color.WHITE);
//            textView.setPadding(10, 2, 1, 2);
//            textView.set
//            //view.setTag(textView.getText());
//
//            return textView;
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                System.out.println(getApplicationContext());
                view = View.inflate(getApplicationContext(), R.layout.text_view, null);
                holder.tv_receive = (TextView) view.findViewById(R.id.tv_receive1);
                holder.tv_send = (TextView) view.findViewById(R.id.tv_send1);
                //view.setBackgroundColor(Color.GREEN);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
//            holder.tv_send.setVisibility(View.GONE);
//            holder.tv_receive.setVisibility(View.VISIBLE);
//            holder.tv_receive.setText("User: " + lists.get(i).getUser() + "\n" + lists.get(i).getMessage() + "\n" +
//                    "\t\t" + lists.get(i).getDate());
            try {
                System.out.println(devices.get(i));
                if (i < devices.size() && devices.get(i) != null && !devices.get(i).getUser().equals("william")) {
                    holder.tv_receive.setVisibility(View.VISIBLE);
                    holder.tv_send.setVisibility(View.GONE);
                    holder.tv_receive.setText("User: " + devices.get(i).getUser() + "\n" + devices.get(i).getMessage() + "\n" +
                            "\t\t" + devices.get(i).getDate());
                } else if (i < devices.size() && devices.get(i) != null && devices.get(i).getUser().equals("william")){
                    holder.tv_send.setVisibility(View.VISIBLE);
                    holder.tv_receive.setVisibility(View.GONE);
                    holder.tv_send.setText("User: " + devices.get(i).getUser() + "\n" + devices.get(i).getMessage() + "\n" +
                            "\t\t" + devices.get(i).getDate());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

//            if (judge == 1) {
//                holder.tv_send.setVisibility(View.VISIBLE);
//                holder.tv_receive.setVisibility(View.GONE);
//                holder.tv_send.setText("User: " + lists1.get(i).getUser() + "\n" + lists1.get(i).getMessage() + "\n" +
//                        "\t\t" + lists1.get(i).getDate());
//            }
            return view;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void setList(List<ChatMsgEntity> lists) {
            if (lists != null) {
                if (whetherFresh == 1) {
                    devices.clear();
                }

//                lists.stream().forEach(p -> {
//                    if (!devices.contains(p)) {
//                        devices.add(p);
//                    }
//                });
                for (ChatMsgEntity list: lists
                     ) {
                    if (!devices.contains(list)) {
                        devices.add(list);
                    }
                }

                try {
                    if (whetherClick == 0) {
                        if (devices != null && devices.size() != 0) {
                            Collections.sort(devices, new SortClass());
                        }
                    }else {
                        whetherClick = 0;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(lists.size());
                System.out.println("---------------------------------");
                System.out.println(lists.size());
                notifyDataSetChanged();
                listView.setAdapter(myAdapter);
                if (whetherFresh == 0) {
                }else {
                    System.out.println("lists_size" + lists.size());
                    listView.setSelection(myAdapter.getCount() - 1);
                    whetherFresh = 0;
                }
            }
        }

        public void clearList() {
            if (lists != null) {
                devices.clear();
            }
            notifyDataSetChanged();
        }

    }

}

