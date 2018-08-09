package app.remotebindingserver.com.remotebindingserver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class BoundService extends Service {
    
    private static final String TAG = BoundService.class.getSimpleName();
    private boolean isRandomGeneratorOn = false;
    private int randomValue = 0;
    
    private static final int GET_RANDOM_NUMBER = 0;
    private Messenger randomNumberMessenger = new Messenger(new RandomNumberRequestHandler());
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return randomNumberMessenger.getBinder();
    }
    
    @Override
    public void onDestroy() {
        Log.e(TAG, "Service destroyed");
        stopRandomGenerator();
        super.onDestroy();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand on thread id: " + Thread.currentThread().getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRandomGeneratorOn = true;
                startRandomGenerator();
            }
        }).start();
        return START_STICKY;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "Service unbind");
        return super.onUnbind(intent);
    }
    
    private void startRandomGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (isRandomGeneratorOn) {
                    randomValue = new Random().nextInt(100);
                    Log.e(TAG, "Random number generated: " + randomValue + " in thread id: " + Thread.currentThread().getId());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void stopRandomGenerator() {
        isRandomGeneratorOn = false;
    }
    
    public int getRandomNumber() {
        return randomValue;
    }
    
    private class RandomNumberRequestHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_RANDOM_NUMBER:
                    Message message = Message.obtain(null, GET_RANDOM_NUMBER);
                    message.arg1 = getRandomNumber();
                    try {
                        Messenger messenger = msg.replyTo;
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
