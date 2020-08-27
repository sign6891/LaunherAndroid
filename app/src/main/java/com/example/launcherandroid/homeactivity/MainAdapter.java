package com.example.launcherandroid.homeactivity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.launcherandroid.R;

import java.util.List;

public class MainAdapter extends ArrayAdapter {

    private List appList = null;
    private Context context;
    private PackageManager packageManager;

    public MainAdapter(Context context, int resource,
                      List objects) {
        super(context, resource, objects);

        this.context = context;
        this.appList = objects;
        packageManager = context.getPackageManager();
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
            view = layoutInflater.inflate(R.layout.list_item, null);
        }

        ApplicationInfo data = (ApplicationInfo) appList.get(position);

        if(null != data) {
            TextView appName = view.findViewById(R.id.item_app_label);
            //TextView packageName = view.findViewById(R.id.item_app_name);
            ImageView iconView = view.findViewById(R.id.item_app_icon);

            appName.setText(data.loadLabel(packageManager));
            //packageName.setText(data.packageName);
            iconView.setImageDrawable(data.loadIcon(packageManager));

        }
        return view;
    }
}
