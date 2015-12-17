package in.ethicstech.return_value_asynctask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by krishnan on 8/12/15.
 */
public class InternetChecker extends AsyncTask<String, Void, Boolean> {
    public Result_Updater delegate = null;
    Context context;
    int time;
    public InternetChecker(Result_Updater asyncResponse, Context cxt, int timer){
        this.delegate = asyncResponse;
        this.context=cxt;
        this.time=timer;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        ConnectivityManager  cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        boolean conExist = false;
        if (nInfo != null && nInfo.isConnectedOrConnecting() && cManager.getActiveNetworkInfo().isAvailable()) {
            try {
                HttpURLConnection urlcon = (HttpURLConnection) new URL("http://www.google.com").openConnection();
                urlcon.setRequestProperty("User-Agent", "Test");
                urlcon.setRequestProperty("Connection", "Close");
                urlcon.setConnectTimeout(time);
                urlcon.connect();
                if (urlcon.getResponseCode() == 200) {
                    conExist = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                conExist = false;
            }
        } else {
            conExist = false;
        }
        return conExist;




    }

    @Override
    protected void onPostExecute(Boolean result) {
        delegate.processFinish(result);
    }


}