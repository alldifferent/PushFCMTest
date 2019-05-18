package com.example.pushfcmtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pushfcmtest.fcm.MyMessageServer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "FCM 등록 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

                String token =task.getResult().getToken();
                Log.d("발급된토큰", token);
            }
        })
    }

//    public void onNewToken(String s){
//
//        String pushTitle ;
//        String pushBody;
//
//        showNotification(pushTitle, pushBody);
//
//    }

    void showNotification(String title, String content){

        Intent intent = new Intent(MyMessageServer.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultNotiUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel("app_name", "app_name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("푸시 알림 테스트용 채널입니다.");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1, 1000});
            channel.setSound(defaultNotiUri, null);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, channel.getId());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(title);
            builder.setContent(content);
            builder.setSound(defaultNotiUri);
            builder.setVibrate(new long[]{1, 1000});
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(1, builder.build());

        }else {

            builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(title);
            builder.setContent(content);
            builder.setSound(defaultNotiUri);
            builder.setVibrate(new long[]{1, 1000});
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(1, builder.build());

        }

    }
}
