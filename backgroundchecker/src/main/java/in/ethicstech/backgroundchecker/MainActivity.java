package in.ethicstech.backgroundchecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, Chef_BackGroundService.class);
        startService(serviceIntent);
    }
}
