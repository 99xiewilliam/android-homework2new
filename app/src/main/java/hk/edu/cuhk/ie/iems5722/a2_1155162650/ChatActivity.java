package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import hk.edu.cuhk.ie.iems5722.a2_1155162650.databinding.ActivityMainBinding;

public class ChatActivity extends AppCompatActivity {
    private ImageView btn;
    private ListView listView;
    private EditText editText;
    private MyAdapter myAdapter;
    private List<ChatMsgEntity> lists = new ArrayList<>();
    private List<ChatMsgEntity> lists1 = new ArrayList<>();
    private Integer judge = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        Intent i = getIntent();
        actionBar.setTitle(i.getStringExtra("name"));
//        System.out.println(i.getStringExtra("id"));
        String id = i.getStringExtra("id");

        initView();

        new HttpTask() {
            @Override
            public void success() {
                JSONObject jsonObj = super.getResponse();
                JSONObject json = null;
                try {
                    if (jsonObj.has("data")) {
                        json = jsonObj.getJSONObject("data");
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
                        lists.add(chatMsgEntity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                judge = 0;
                myAdapter = new MyAdapter();
                listView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                listView.setSelection(lists.size() - 1);
            }

            @Override
            public void failed() {

            }
        }.execute("http://18.217.125.61/api/a3/get_messages?chatroom_id=" + id + "&page=1");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judge = 1;
                String content = editText.getText().toString();
                if (content!= null && !content.equals("")) {
                    ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
                    chatMsgEntity.setMessage(content);
                    chatMsgEntity.setDate(getDate());
                    chatMsgEntity.setUser("william");
                    new HttpPostTask() {
                        @Override
                        public void success() {
                            JSONObject json = super.getResponse();
                            System.out.println(json.toString());
                        }

                        @Override
                        public void failed() {

                        }
                    }.execute("http://18.217.125.61/api/a3/send_message", id, "1155162650", chatMsgEntity.getUser(), chatMsgEntity.getMessage());
                    lists.add(chatMsgEntity);
                    myAdapter.notifyDataSetChanged();
                    listView.setSelection(lists.size() - 1);
                    editText.setText("");
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        return format.format(new Date());
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int i) {
            return lists.get(i);
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
            if (lists.get(i).getUser() != null && !lists.get(i).getUser().equals("william")) {
                holder.tv_receive.setVisibility(View.VISIBLE);
                holder.tv_send.setVisibility(View.GONE);
                holder.tv_receive.setText("User: " + lists.get(i).getUser() + "\n" + lists.get(i).getMessage() + "\n" +
                        "\t\t" + lists.get(i).getDate());
            } else {
                holder.tv_send.setVisibility(View.VISIBLE);
                holder.tv_receive.setVisibility(View.GONE);
                holder.tv_send.setText("User: " + lists.get(i).getUser() + "\n" + lists.get(i).getMessage() + "\n" +
                        "\t\t" + lists.get(i).getDate());
            }
//            if (judge == 1) {
//                holder.tv_send.setVisibility(View.VISIBLE);
//                holder.tv_receive.setVisibility(View.GONE);
//                holder.tv_send.setText("User: " + lists1.get(i).getUser() + "\n" + lists1.get(i).getMessage() + "\n" +
//                        "\t\t" + lists1.get(i).getDate());
//            }
            return view;
        }

    }

}

