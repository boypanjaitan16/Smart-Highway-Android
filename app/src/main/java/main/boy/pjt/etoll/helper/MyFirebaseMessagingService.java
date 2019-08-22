package main.boy.pjt.etoll.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.beforelogin.RegisterActivity;
import main.boy.pjt.etoll.response.ResponseBalance;
import main.boy.pjt.etoll.response.ResponseDefault;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static main.boy.pjt.etoll.helper.MyConstant.System.ADMIN_CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

        int notificationId = new Random().nextInt(60000);

        Intent intent   = new Intent(this, CoreActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", remoteMessage.getNotification().getTitle());
        intent.putExtra("body", remoteMessage.getNotification().getBody());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, 0);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)  //a resource for your custom small icon
                .setContentTitle(remoteMessage.getNotification().getTitle()) //the "title" value you sent in your notification
                .setContentText(remoteMessage.getNotification().getBody()) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("NEW TOKEN", s);

        if(s != null) {
            MySession session = new MySession(this);
            MyRetrofitInterface retrofitInterface = MyRetrofit.getClient().create(MyRetrofitInterface.class);
            Call<ResponseDefault> call = retrofitInterface.updateFbmToken(session.getCOSTUMER_ID(), s);
            call.enqueue(
                    new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                            if (response.body().getStatus().equals(MyConstant.System.responseSuccess)) {
                                Log.d(getClass().getSimpleName(), "UPDATE TOKEN");
                            } else {
                                Log.d(getClass().getSimpleName(), response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDefault> call, Throwable t) {
                            Log.d(getClass().getSimpleName(), t.getMessage());
                        }
                    }
            );

            session.setFbmToken(s);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
