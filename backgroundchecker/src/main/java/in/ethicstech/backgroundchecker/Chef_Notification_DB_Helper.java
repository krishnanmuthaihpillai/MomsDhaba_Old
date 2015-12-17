package in.ethicstech.backgroundchecker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by krishnan on 10/12/15.
 */
public class Chef_Notification_DB_Helper extends SQLiteOpenHelper {


    public static final String TABLE_CHEF_NOTIFICATION_DETAIL_TABLE = "CHEF_NOTIFICATION_DETAIL_TABLE";

    public static final String KEY_FOOD_CHEF_ID = "FOOD_CHEF_ID";
    public static final String KEY_FOOD_ID = "FOOD_ID";
    public static final String KEY_FOOD_NAME = "FOOD_NAME";
    public static final String KEY_FOOD_ORDER_ID = "FOOD_ORDER_ID";
    public static final String KEY_FOOD_COUNT = "FOOD_COUNT";
    public static final String KEY_FOOD_CHEF_STATUS = "FOOD_CHEF_STATUS";
    public static final String KEY_FOOD_DATE_TIME = "FOOD_DATE_TIME";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MomsDhaba";

    public Chef_Notification_DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHEF_NOTIFICATION_DETAIL_TABLE = "CREATE TABLE CHEF_NOTIFICATION_DETAIL_TABLE ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "FOOD_CHEF_ID TEXT, " + "FOOD_ID TEXT, " + "FOOD_NAME TEXT, "
                + "FOOD_ORDER_ID TEXT, " + "FOOD_COUNT TEXT, " + "FOOD_CHEF_STATUS TEXT, " + "FOOD_DATE_TIME TEXT) ";

        db.execSQL(CREATE_CHEF_NOTIFICATION_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CHEF_FOOD_DETAILS");
        this.onCreate(db);
    }

    public void insert_data(String food_chef_id, String foodId, String food_name, String food_order_Id, String food_count, String food_chef_status, String food_date_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FOOD_CHEF_ID, food_chef_id);
        values.put(KEY_FOOD_ID, foodId);
        values.put(KEY_FOOD_NAME, food_name);
        values.put(KEY_FOOD_ORDER_ID, food_order_Id);
        values.put(KEY_FOOD_COUNT, food_count);
        values.put(KEY_FOOD_CHEF_STATUS, food_chef_status);
        values.put(KEY_FOOD_DATE_TIME, food_date_time);
        db.insertWithOnConflict(TABLE_CHEF_NOTIFICATION_DETAIL_TABLE, KEY_FOOD_CHEF_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CHEF_NOTIFICATION_DETAIL_TABLE);
        db.close();
    }
}