package com.example.subooksubook;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import java.util.Calendar;
import java.util.Date;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

    String channelId = "channel";
    String channelName = "channel Name";
    int importance = NotificationManager.IMPORTANCE_LOW;
    int requestID;

    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notiManager = (NotificationManager) context.getSystemService  (Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
        notiManager.createNotificationChannel(mChannel);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context.getApplicationContext(), channelId);

        Intent notificationIntent = new Intent(context.getApplicationContext()
                , MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        requestID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent
                = PendingIntent.getActivity(context.getApplicationContext()
                , requestID
                , notificationIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("수북수북 ") // required
                .setContentText("오늘 읽은 책을 기록해보세요!  ")  // required
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .setSound(RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notilogo))
                .setBadgeIconType(R.drawable.notilogo)
                .setContentIntent(pendingIntent);

        if (notiManager != null) {

            // 노티피케이션 동작시킴
            notiManager.notify(1234, builder.build());

            Calendar nextNotifyTime = Calendar.getInstance();

            // 내일 같은 시간으로 알람시간 결정
            nextNotifyTime.add(Calendar.DATE, 1);

            //  Preference에 설정한 값 저장
            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
            editor.apply();

            Date currentDateTime = nextNotifyTime.getTime();
            // String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
            // Toast.makeText(context.getApplicationContext(),"다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
        }
    }
}
