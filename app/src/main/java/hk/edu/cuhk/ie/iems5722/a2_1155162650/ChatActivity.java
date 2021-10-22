package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ImageView btn;
    private ListView listView;
    private EditText editText;
    private List<ChatMsgEntity> lists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }

    private static class ViewHolder {
        TextView tv_receive;
        TextView tv_send;
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
                view = View.inflate(getApplicationContext(), R.layout.text_view, null);
                holder.tv_receive = (TextView) view.findViewById(R.id.tv_receive1);
                holder.tv_send = (TextView) view.findViewById(R.id.tv_send1);
                //view.setBackgroundColor(Color.GREEN);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.tv_send.setVisibility(View.VISIBLE);
            holder.tv_receive.setVisibility(View.GONE);
            holder.tv_send.setText(lists.get(i).getMessage() + "\n" +
                    lists.get(i).getDate());

//            Msg msg = getItem(i);
//            if (msg.type == Msg.TYPE_RECEIVE) {
//                holder.tv_receive.setVisibility(View.VISIBLE);
//                holder.tv_send.setVisibility(View.GONE);
//                holder.tv_receive.setText(msg.content);
//            } else if (msg.type == Msg.TYPE_SEND) {
//                holder.tv_send.setVisibility(View.VISIBLE);
//                holder.tv_receive.setVisibility(View.GONE);
//                holder.tv_send.setText(msg.content);
//            }
            return view;
        }

    }

}

