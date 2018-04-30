package com.example.craig.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.example.craig.camera.Utils.LetterImageView;

public class DetailDay extends AppCompatActivity {

    private ListView listView;
    private android.support.v7.widget.Toolbar toolbar;
    public static String[] Monday;
    public static String[] Tuesday;
    public static String[] Wednesday;
    public static String[] Thursday;
    public static String[] Friday;
    public static String[] Saturday;
    public static String[] Sunday;
    public static String[] Time1;
    public static String[] Time2;
    public static String[] Time3;
    public static String[] Time4;
    public static String[] Time5;
    public static String[] Time6;
    public static String[] Time7;
    private String[] PreferredDay;
    private String[] PreferredTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_day);

        setupUIViews();
        initToolbar();
        setupListView();
    }

    private void setupUIViews() {
        listView = (ListView) findViewById(R.id.lvDayDetail);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ToolbarDayDetail);
    }
    private void initToolbar(){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(Schedule.sharedPreferences.getString(Schedule.SEL_DAY, null));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        private void setupListView(){
            Monday = getResources().getStringArray(R.array.Monday);
            Tuesday = getResources().getStringArray(R.array.Tuesday);
            Wednesday = getResources().getStringArray(R.array.Wednesday);
            Thursday = getResources().getStringArray(R.array.Thursday);
            Friday = getResources().getStringArray(R.array.Friday);
            Saturday = getResources().getStringArray(R.array.Saturday);
            Sunday = getResources().getStringArray(R.array.Sunday);

            Time1 = getResources().getStringArray(R.array.time1);
            Time2 = getResources().getStringArray(R.array.time2);
            Time3 = getResources().getStringArray(R.array.time3);
            Time4 = getResources().getStringArray(R.array.time4);
            Time5 = getResources().getStringArray(R.array.time5);
            Time6 = getResources().getStringArray(R.array.time6);
            Time7 = getResources().getStringArray(R.array.time7);

            String selected_day = Schedule.sharedPreferences.getString(Schedule.SEL_DAY, null);

            if (selected_day.equalsIgnoreCase("Monday")) {
                PreferredDay = Monday;
                PreferredTime = Time1;
            } else if (selected_day.equalsIgnoreCase("Tuesday")) {
                PreferredDay = Tuesday;
                PreferredTime = Time2;
            } else if (selected_day.equalsIgnoreCase("Wednesday")) {
                PreferredDay = Wednesday;
                PreferredTime = Time3;
            } else if (selected_day.equalsIgnoreCase("Thursday")) {
                PreferredDay = Thursday;
                PreferredTime = Time4;
            } else if (selected_day.equalsIgnoreCase("Friday")) {
                PreferredDay = Friday;
                PreferredTime = Time5;
            }else if (selected_day.equalsIgnoreCase("Saturday")) {
                PreferredDay = Saturday;
                PreferredTime = Time6;
            } else {
                PreferredDay = Sunday;
                PreferredTime = Time7;
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(DetailDay.this, PreferredDay, PreferredTime);
            listView.setAdapter(simpleAdapter);
        }

        public class SimpleAdapter extends BaseAdapter {
            private Context mContext;
            private LayoutInflater layoutInflater;
            private TextView subject, time;
            private String[] subjectArray;
            private String[] timeArray;
            private LetterImageView letterImageView;

            public SimpleAdapter(Context context, String[] subjectArray, String[] timeArray) {
                mContext = context;
                this.subjectArray = subjectArray;
                this.timeArray = timeArray;
                layoutInflater = LayoutInflater.from(context);
            }

            @Override
            public int getCount() {
                return subjectArray.length;
            }

            @Override
            public Object getItem(int position) {
                return subjectArray[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.day_detail_single_item, null);
                }

                subject = (TextView)convertView.findViewById(R.id.tvSubjectDayDetails);
                time = (TextView)convertView.findViewById(R.id.tvTimeDayDetail);
                letterImageView = (LetterImageView) convertView.findViewById(R.id.ivDayDetails);

                subject.setText(subjectArray[position]);
                //time.setText(timeArray[position]);

                letterImageView.setOval(true);
                letterImageView.setLetter(subjectArray[position].charAt(0));

                return convertView;
            }
        }
    }

