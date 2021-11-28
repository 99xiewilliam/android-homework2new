package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        System.out.println("-------------------------");
        Log.d("tag", "token:" + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("tag", "From" + remoteMessage.getFrom());
        Log.d("tag", "message:" + remoteMessage.getData());
    }
}
