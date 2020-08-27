package com.example.launcherandroid.homeactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.launcherandroid.addapplicationactivity.AddApplicationActivity;
import com.example.launcherandroid.allapplicationactivity.AppListActivity;
import com.example.launcherandroid.R;

import java.util.ArrayList;
import java.util.TreeSet;


public class MainActivity extends AppCompatActivity {

    private MainAdapter mMainAdapter;
    ArrayList<ApplicationInfo> mListApplications = new ArrayList<>();
    ArrayList<String> mListNamePackage = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.list_view);
        mListNamePackage.addAll(loadArrayList("key12345"));

        //если запускаем активити из AddApplicationActivity
        if (getIntent().getBooleanExtra("keyBoolean", false)) {
            addListNamePackageAndList();
        } else {
            showApplications();
        }
    }

    //Добавление новых приложений в список избранных и их сохранение
    private void addListNamePackageAndList() {
        ArrayList<ApplicationInfo> list = (ArrayList<ApplicationInfo>) getIntent().getSerializableExtra("key");
        if (list != null) {
            for (ApplicationInfo ap : list) {
                mListNamePackage.add(ap.packageName);
            }
            TreeSet<String> set = new TreeSet<>();
            set.addAll(mListNamePackage);
            mListNamePackage.clear();
            mListNamePackage.addAll(set);
            saveArrayList("key12345", mListNamePackage);
        }
        //показать список через адаптер
        showApplications();
    }

    //Метод получение объектов ApplicationInfo из  packageName и отправка их в Адаптер для отображения
    private void showApplications() {

        if (mListNamePackage != null) {
            try {
                for (String packageName : mListNamePackage) {
                    ApplicationInfo app = this.getPackageManager().getApplicationInfo(packageName, 0);
                    mListApplications.add(app);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Toast toast = Toast.makeText(this, "error in getting icon", Toast.LENGTH_SHORT);
                toast.show();
                e.printStackTrace();
            }
            //Отдаем данные в адаптер
            mMainAdapter = new MainAdapter(MainActivity.this, R.layout.list_item, mListApplications);
            mListView.setAdapter(mMainAdapter);
        }
    }

    private void saveArrayList(String name, ArrayList<String> list) {
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s).append("<s>");
        //sb.delete(sb.length() - 3, sb.length());
        if (list.size() > 0) sb.delete(sb.length() - 3, sb.length());
        editor.putString(name, sb.toString()).apply();
    }

    private ArrayList<String> loadArrayList(String name) {
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String[] strings = prefs.getString(name, "").split("<s>");
        ArrayList<String> list = new ArrayList<>();
        for (String str : strings) {
            if (!str.equals("")) list.add(str);
        }
        return list;
    }


    public void showAllApps(View v) {
        Intent i = new Intent(this, AppListActivity.class);
        startActivity(i);
    }

    public void addApps(View view) {
        Intent i = new Intent(this, AddApplicationActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                mListApplications.clear();
                mMainAdapter.notifyDataSetChanged();
                mListNamePackage.clear();
                saveArrayList("key12345", mListNamePackage);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}