package com.example.craig.camera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.craig.camera.Utils.LetterImageView;

public class Schedule extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    public static SharedPreferences sharedPreferences;
    public static String SEL_DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setupUIViews();
        initToolbar();
        setupListView();
    }

//seting proper layout of days of the week
    private void setupUIViews() {
        toolbar = (Toolbar) findViewById(R.id.ToolbarSchedule);
        listView = (ListView) findViewById(R.id.lvWeek);
        sharedPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);

    }
//setting toolbar and giving title to display
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

//setting up a switch statement for pressing the selected day of the week and allowing to open that days schedule
    private void setupListView() {
        String[] week = getResources().getStringArray(R.array.Schedule);
        WeekAdapter adapter = new WeekAdapter(this, R.layout.activity_schedule_single_item, week);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        startActivity(new Intent(Schedule.this, DetailDay.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Monday").apply();
                        break;
                    }
                    case 1: {
                        startActivity(new Intent(Schedule.this, DetailDay.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Tuesday").apply();
                        break;
                    }
                    case 2: {
                        startActivity(new Intent(Schedule.this, DetailDay.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Wednesday").apply();
                        break;
                    }
                    case 3: {
                        startActivity(new Intent(Schedule.this, DetailDay.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Thursday").apply();
                        break;
                    }
                    case 4: {
                        startActivity(new Intent(Schedule.this, DetailDay.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Friday").apply();
                        break;
                    }
                    case 5: {
                        startActivity(new Intent(Schedule.this, DetailDay.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Saturday").apply();
                        break;
                    }
                    case 6: {
                        startActivity(new Intent(Schedule.this, DetailDay.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Sunday").apply();
                        break;
                    }
                    default:break;
                }
            }
        });
    }

    public class WeekAdapter extends ArrayAdapter {


        private int resource;
        private LayoutInflater layoutInflater;
        private String[] week = new String[]{};

//creating the week adapter to pull from the proper arrays
        public WeekAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

//setting up the first letter image view layout to allow for clean interface
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = (LetterImageView) convertView.findViewById(R.id.ivLetter);
                holder.lvWeek = (TextView) convertView.findViewById(R.id.lvMain);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter(week[position].charAt(0));
            holder.lvWeek.setText(week[position]);

            return convertView;
        }

        class ViewHolder {
            private LetterImageView ivLogo;
            private TextView lvWeek;
        }
    }

    @Override
//allowing you to press the back arrow in the app to return you to main
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home : {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

