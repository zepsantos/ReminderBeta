package com.example.josep.reminderbeta.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josep.reminderbeta.R;


public class AppSettingsAdapter extends ArrayAdapter<String> {
	private Context context;
	private  String[] settings;
	private  Integer[] imgid;
	public AppSettingsAdapter(Context context, String[] settings, Integer[] imgid) {
		super(context, 0, settings);
		this.context = context;
		this.settings = settings;
		this.imgid = imgid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			// If there's no view to re-use, inflate a brand new view for row
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.appsettingslv, parent, false);
			viewHolder.settings = (TextView) convertView.findViewById(R.id.item);
			// Cache the viewHolder object inside the fresh view
			viewHolder.imgView = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(viewHolder);


		} else {
			// View is being recycled, retrieve the viewHolder object from tag
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		viewHolder.settings.setText(settings[position]);

		viewHolder.imgView.setImageResource(imgid[position]);
		// Return the completed view to render on screen
		return convertView;
	}

	private static class ViewHolder {
		TextView settings;
		ImageView imgView;
		TextView extratxt;

	}
}