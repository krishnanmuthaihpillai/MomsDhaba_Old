package in.ethicstech.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    Button b1;
    ArrayList<String> day_array_list = new ArrayList<String>();
    HashSet<String> day_hashset = new HashSet<String>();
    String[] items;
    String[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.button11);
        // get current week days date in days[]
        days = getcurrentweekdays();

        day_array_list.clear();

        day_array_list.add("ALL DAYS");
        for (int i = 0; i < days.length; i++) {
            // add days name to array list
            day_array_list.add(get_day(days[i]));
        }
        //convert array list array
        items = day_array_list.toArray(new String[day_array_list.size()]);


        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                show_dialoge();
            }
        });

    }

    private void show_dialoge() {
        day_hashset.clear();
        Dialog dialog;
        final ArrayList itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select MENU AVAILABLE FROM " + days[0] + " " + "TO " + " " + days[6]);
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isSelected) {
                        if (isSelected) {
                            if (position == 0) {
                                for (int i = 1; i <= 7; i++) {
                                    ((AlertDialog) dialog).getListView().setItemChecked(i, true);
                                    String item = items[i].toString();
                                    day_hashset.add(item);
                                    Log.d("day_hashset", "" + day_hashset);
                                }
                            }
                            String item = items[position].toString();
                            day_hashset.add(item);
                            Log.d("day_hashset", "" + day_hashset);
                            itemsSelected.add(position);


                        } else if (itemsSelected.contains(position)) {
                            if (position == 0) {
                                for (int i = 1; i <= 7; i++) {
                                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                    day_hashset.clear();
                                    Log.d("day_hashset", "" + day_hashset);
                                }
                            }
                            itemsSelected.remove(Integer.valueOf(position));
                            String item = items[position].toString();
                            day_hashset.remove(item);
                            Log.d("day_hashset", "" + day_hashset);
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (!day_hashset.isEmpty()) {
                            String main_str = get_total_string(day_hashset);
                            Log.d("fffffffffffff", "" + main_str);
                        } else {
                            Log.d("fffffffffffff", "empty");
                        }
                        dialog.dismiss();
                    }
                })
                .

                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        dialog = builder.create();
        dialog.show();

    }


    private String get_total_string(HashSet<String> d_hashset) {
        boolean all_daychecked = false;
        StringBuilder date_string = new StringBuilder();
        for (String str : d_hashset) {
            if (str.equals("ALL DAYS")) {
                date_string.append(days[0] + ",");
                date_string.append(days[1] + ",");
                date_string.append(days[2] + ",");
                date_string.append(days[3] + ",");
                date_string.append(days[4] + ",");
                date_string.append(days[5] + ",");
                date_string.append(days[6] + ",");
                date_string.toString();
                all_daychecked = true;

            } else {

                if (str.equals("Monday")) {
                    if (!all_daychecked) {
                        date_string.append(days[0] + ",");
                    }

                } else if (str.equals("Tuesday")) {

                    if (!all_daychecked) {
                        date_string.append(days[1] + ",");
                    }

                } else if (str.equals("Wednesday")) {

                    if (!all_daychecked) {
                        date_string.append(days[2] + ",");
                    }


                } else if (str.equals("Thursday")) {
                    if (!all_daychecked) {
                        date_string.append(days[3] + ",");
                    }


                } else if (str.equals("Friday")) {
                    if (!all_daychecked) {
                        date_string.append(days[4] + ",");
                    }


                } else if (str.equals("Saturday")) {

                    if (!all_daychecked) {
                        date_string.append(days[5] + ",");
                    }


                } else if (str.equals("Sunday")) {

                    if (!all_daychecked) {
                        date_string.append(days[6] + ",");
                    }

                }

            }

        }

        return date_string.toString();
    }

    private String get_day(String str) {
        String finalDay = null;
        try {
            String input_date = str;
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            Date dt1 = format1.parse(input_date);
            DateFormat format2 = new SimpleDateFormat("EEEE");
            finalDay = format2.format(dt1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDay;
    }

    private String[] getcurrentweekdays() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        System.out.println(Arrays.toString(days));

        return days;
    }
}
