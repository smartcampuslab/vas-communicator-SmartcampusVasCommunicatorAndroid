package eu.trentorise.smartcampus.communicator.custom;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OptionsListAdapter extends ArrayAdapter<String> {

	Context context;
	int layoutResourceId;
	List<String> contentsList;

	public OptionsListAdapter(Context context, int layoutResourceId, List<String> optionsList) {
		super(context, layoutResourceId, optionsList);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.contentsList = optionsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		DataHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new DataHolder();
			holder.optionTextView = (TextView) row.findViewById(android.R.id.text1);

			row.setTag(holder);
		} else {
			holder = (DataHolder) row.getTag();
		}

		String content = contentsList.get(position);
		holder.optionTextView.setText(content);

		// Log.e(this.getClass().getSimpleName(), "ROW DONE");
		return row;
	}

	static class DataHolder {
		TextView optionTextView;
	}
}
