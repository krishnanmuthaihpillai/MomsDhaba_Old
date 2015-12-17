package in.ethicstech.backgroundchecker;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krishnan on 16/12/15.
 */
public class Chef_Notification_check extends AsyncTask<String, Void, String> {
    public ResultUpdater_String resultUpdater = null;
    Context context = null;
    String url = null;
    String userId = null;
    Chef_Notification_DB_Helper db;
    String status = "";

    public Chef_Notification_check(ResultUpdater_String resultUpdater, Context context, String url, String userId, Chef_Notification_DB_Helper db) {
        this.resultUpdater = resultUpdater;
        this.context = context;
        this.userId = userId;
        this.url = url;
        this.db = db;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            JsonParser jParser = new JsonParser();
            List<NameValuePair> sFood = new ArrayList<NameValuePair>();
            sFood.add(new BasicNameValuePair("chefid", userId));
            JSONObject json = jParser.getJSONFromUrlPOST(sFood, url);
            status = json.getString("status");
            if (status.equals("order_is_available")) {
                insert_values_in_database(json);
            }

        } catch (Exception e) {
            status = "";
        }

        return status;
    }

    private void insert_values_in_database(JSONObject json) {
        try {
            db = new Chef_Notification_DB_Helper(context);
            db.deleteAll();
            JSONArray jarray = json.getJSONArray("order_history");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String food_chef_id = object.getString("id");
                String food_id = object.getString("food_id");
                String food_name = object.getString("food");
                String food_order_id = object.getString("order_id");
                String food_count = object.getString("count");
                String food_chef_status = object.getString("chef_status");
                String food_datetime = object.getString("datetime");
                db.insert_data(food_chef_id, food_id, food_name, food_order_id, food_count, food_chef_status, food_datetime);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPostExecute(String result) {
        resultUpdater.processFinish(result);
    }

}
