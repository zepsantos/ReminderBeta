package com.example.josep.reminderbeta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SubjectAdapter extends ArrayAdapter<Subject> {
	public SubjectAdapter(Context context, ArrayList<Subject> subjects) {
		super(context, 0, subjects);
	}
	private static class ViewHolder {
		TextView subject;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Subject subject = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			// If there's no view to re-use, inflate a brand new view for row
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.subjectlv, parent, false);
			viewHolder.subject = (TextView) convertView.findViewById(R.id.tvSubject);
			// Cache the viewHolder object inside the fresh view
			convertView.setTag(viewHolder);
		} else {
			// View is being recycled, retrieve the viewHolder object from tag
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		viewHolder.subject.setText(subject.Subject);
		// Return the completed view to render on screen
		return convertView;
	}
}