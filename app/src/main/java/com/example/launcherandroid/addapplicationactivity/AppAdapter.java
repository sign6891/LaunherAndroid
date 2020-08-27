package com.example.launcherandroid.addapplicationactivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.launcherandroid.R;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends ArrayAdapter {

    private List appList = null;
    private Context context;
    private PackageManager packageManager;
    static boolean[] checked;

    public AppAdapter(Context context, int resource,
                      List objects) {
        super(context, resource, objects);

        this.context = context;
        this.appList = objects;
        packageManager = context.getPackageManager();
        checked = new boolean[appList.size()];
    }

    @Override
    public int getCount() {
        return ((null != appList) ? appList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return (null != appList) ? (ApplicationInfo) appList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_add_application, null);
        }

        ApplicationInfo data = (ApplicationInfo) appList.get(position);

        if(null != data) {
            TextView appName = view.findViewById(R.id.item_app_label);
            TextView packageName = view.findViewById(R.id.item_app_name);
            ImageView iconView = view.findViewById(R.id.item_app_icon);
            CheckBox checkBox = view.findViewById(R.id.checkbox);

            appName.setText(data.loadLabel(packageManager));
            packageName.setText(data.packageName);
            iconView.setImageDrawable(data.loadIcon(packageManager));

            checkBox.setChecked(checked[position]);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checked[position] = !checked[position];
                }
            });

        }
        return view;
    }

    public List<ApplicationInfo> listOfSelectedApplications(){
        ArrayList<ApplicationInfo> list = new ArrayList<ApplicationInfo>();
        for (int i = 0; i < appList.size(); i++) {
            if (checked[i] == true) {
                list.add((ApplicationInfo) appList.get(i));
            }
        }
       /* if (list != null) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("key", list);
            context.startActivity(intent);
        }*/
        return list;
    }

    static class Barr{
        static boolean[] getChecked() {
            return checked;
        }
    }
}
