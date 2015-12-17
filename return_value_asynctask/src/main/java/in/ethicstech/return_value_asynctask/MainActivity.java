package in.ethicstech.return_value_asynctask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  {
Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b1=(Button)findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_internet(30000);


            }
        });

    }

    private void check_internet(final int time) {
        InternetChecker asyncTask =new InternetChecker(new Result_Updater() {
            @Override
            public void processFinish(boolean output) {
                if(output){
                    Log.d("INTERNET","AVAILABLE");
                }else{
                    Log.d("INTERNET","NOT AVAILABLE");
                }
            }

        },getApplicationContext(),time);asyncTask.execute();

    }



}
