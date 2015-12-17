package in.ethicstech.internetchecker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Internet_Detector(getApplicationContext()).execute();
//        Log.d("result",""+result);
    }
    
    private class Internet_Detector extends AsyncTask<String, String, Boolean> {
        ConnectionDetector cd;
        boolean check=false;
        Context mctx;
        Internet_Detector(Context ctx) {
            mctx=ctx;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            cd = new ConnectionDetector(mctx);
            if (cd.isConnectingToInternet()) {
                check= detect_internet(30000);
            }else{
                check=false;
            }
            return check;
        }

        @Override
        protected void onPostExecute(Boolean mresult) {
//            Log.d("onPostExecute", "" + check);

            result=mresult;
            Log.d("onPostExecute", "" + mresult);
        }
    }

    private boolean detect_internet(int timeout) {
     boolean result=false;
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://momsdhaba.com/").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(timeout);
            urlc.connect();
            result=(urlc.getResponseCode() == 200);
            return result ;
        } catch (IOException e) {
            result=false;
        }
        return result;
    }

}
