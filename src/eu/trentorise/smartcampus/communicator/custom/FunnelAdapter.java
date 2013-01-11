package eu.trentorise.smartcampus.communicator.custom;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.model.CommunicatorConstants;
import eu.trentorise.smartcampus.communicator.model.Funnel;

public class FunnelAdapter extends ArrayAdapter<Funnel> {

	private Activity context;
	private int layoutResourceId;

	public FunnelAdapter(Activity context, int layoutResourceId, List<Funnel> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Funnel tag = getItem(position);


		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			row.setTag(tag);
		}

		((TextView)row.findViewById(R.id.funnel_name)).setText(tag.getTitle());
		ImageView img = (ImageView)row.findViewById(R.id.funnel_img);
		img.setImageDrawable(CommunicatorConstants.getFunnelTypeImage(getContext(), tag.getSourceType()));
		return row;
	}
	
}
