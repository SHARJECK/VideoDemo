package com.peasun.aispeech.aiopen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.sharjie.videodemo.MainActivity;

/**
 * Created by Shahsen on 2020/2/23.
 */
public class AIOpenService extends Service {
    private String TAG = "AIOpenService";

    AIOpenReceiver aiOpenReceiver = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle data = intent.getExtras();
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                switch (action) {
                    case AIOpenConstant.AI_OPEN_ACTION_VIDEO: {
                        if (data != null) {
                            //play text
                            String command = data.getString("common");
                            if (!TextUtils.isEmpty(command)) {
                                if ("search".equals(command)) {
                                    String keyword = data.getString("keyword");

                                    Log.d(TAG, "receive :" + command + ", " + keyword);
                                    //todo

                                } else if ("control".equals(command)) {
                                    String ctlAction = data.getString("action");

                                    if (data.containsKey("PlayIndex")) {
                                        String playIndex = data.getString("PlayIndex");
                                        Log.d(TAG, "receive :" + command + ", " + ctlAction + ", index " + playIndex);
                                        //todo
                                    }

                                    if (data.containsKey("FastForward")) {
                                        int fastForward = data.getInt("FastForward");//second
                                        Log.d(TAG, "receive :" + command + ", " + ctlAction + ", fastForward " + fastForward);
                                        //todo
                                    } else if (data.containsKey("FastBackward")) {
                                        int fastBackward = data.getInt("FastBackward");//second
                                        Log.d(TAG, "receive :" + command + ", " + ctlAction + ", fastBackward " + fastBackward);
                                        //todo
                                    } else if (data.containsKey("SeekTo")) {
                                        int seekTo = data.getInt("SeekTo");//second
                                        Log.d(TAG, "receive :" + command + ", " + ctlAction + ", seekTo " + seekTo);
                                        //todo
                                    }

                                    Log.d(TAG, "receive :" + command + ", " + ctlAction);
                                    //todo

                                }
                            }

                        }
                    }
                    break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startForce();
        lauchActivity();

        //register video app to ai server
        AIOpenUtils.registerVideoApp(this);

        //register receiver
        aiOpenReceiver = AIOpenUtils.registerVideoReciver(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(aiOpenReceiver);
    }

    private void lauchActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        this.startActivity(intent);
    }

    private void startForce() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String CHANNEL_ONE_ID = "com.sharjie.video";
                String CHANNEL_ONE_NAME = "VIDEO SERVICE";
                NotificationChannel notificationChannel =
                        new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(notificationChannel);
                startForeground(1, new NotificationCompat.Builder(this, CHANNEL_ONE_ID).build());
            }
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
