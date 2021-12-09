package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private String ip = "47.250.50.72";


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Token:" + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("user_id", "1155162650")
                .add("token", token)
                .build();
        Request request = new Request.Builder()
                .url("http://" + ip + "/api/a4/submit_push_token")
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

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.d(TAG, "from:-------------------------------------------------" + remoteMessage.getFrom());
//        Log.d(TAG, "message" + remoteMessage.getData());
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From=========================: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        String id = null;
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
             id = remoteMessage.getData().get("id");

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification title: " + remoteMessage.getNotification().getTitle());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), id);
//            ChatActivity chatActivity = new ChatActivity();
//            chatActivity.sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void sendNotification(String title, String context, String id) {
        String CHANNEL_ID = String.valueOf(System.currentTimeMillis());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(context)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);

        Intent indent = new Intent(this, ChatActivity.class);
//        indent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        indent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        indent.addCategory(Intent.CATEGORY_LAUNCHER);
        indent.putExtra("id", id);
        indent.putExtra("name", title);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, indent, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(pendingIntent);
        builder.setFullScreenIntent(pendingIntent, true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());

    }
    private void scheduleJob() {
        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


}
