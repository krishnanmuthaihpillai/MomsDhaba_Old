package in.ethicstech.backgroundchecker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by krishnan on 16/12/15.
 */
public class Chef_BackGroundService extends Service {
    private NotificationManager mNM;
    Chef_Notification_DB_Helper db;
    private static final String TAG = "Chef_BackGroundService";
    public static int sInterval = 30000;
    private boolean mIsRunning = false;
    private Handler mHandler;
    private Timer mTimer;
    Context app_context;

    private int NOTIFICATION = 1;

    private int TIME_INTERVAL = 3000;

    boolean isRunning;

    boolean notification_sound;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isRunning = false;
        notification_sound=false;
        super.onCreate();
        Log.i(TAG, "Service onCreate");
        mHandler = new Handler();
        mTimer = new Timer();
        app_context = getApplicationContext();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start_background(sInterval);
        return Service.START_STICKY;
    }

    private void start_background(int interval) {
        if (!isRunning) {
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        public void run() {
                            isRunning = true;

                            String url = "http://momsdhaba.com/mobileapp/chefnotification/";
                            String user_id = "22";

                            check_internet(app_context, TIME_INTERVAL,url,user_id);
//                            check_order(app_context, url, user_id);

//                            make_toast(app_context, "Moms dhaba Background Service is Running");
                        }
                    });
                }
            };
            mTimer.schedule(doAsynchronousTask, 0, sInterval); // execute in every
        } else {
            // isrunning true
            Log.d("is Running ", "already");
        }
    }

    private void check_internet(final Context cxt, final int time_invl,final String m_url,final String m_user_id) {
        InternetChecker asyncTask = new InternetChecker(new ResultUpdater_Boolean() {
            @Override
            public void processFinish(boolean output) {
                if (output) {
                    // Internet is available
                    Log.d("output",""+output);
                    check_order(cxt, m_url, m_user_id);
                } else {
                    // Internet is not  available
                    Log.d("output",""+output);
                }
            }

        }, cxt, time_invl);
        asyncTask.execute();




    }

    private void check_order(final Context context, final String url, final String userId) {

        Chef_Notification_check chef_notification_check = new Chef_Notification_check(new ResultUpdater_String() {
            @Override
            public void processFinish(String output) {
                if ((output != "") && (output != null)) {
                    Log.d("output", "" + output);
                    if (output.equals("order_is_available")) {
                        //order is available
                        String tittle = "Moms Dhaba";
                        String message = "Order is available for your food";
                        showNotification(tittle, message);
                    } else if (output.equals("orders_are_not_available")) {
                        notification_sound = false;
                        // orders are not available
                    }
                }
            }
        }, context, url, userId,db);
        chef_notification_check.execute();

    }

    private void showNotification(String tittle, String message) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(message)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(tittle)
                .setContentText(message)
                .setContentIntent(contentIntent);
        if(!notification_sound) {
            notification_sound=true;
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }
        mNM.notify(NOTIFICATION, mBuilder.build());

    }

    private void make_toast(Context app_cxt, String s) {
        Toast.makeText(app_cxt, "" + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        isRunning = false;
        notification_sound=false;
    }

}
