package eu.trentorise.smartcampus.communicator.fragments.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class SearchFragment extends AbstractMessageListFragment {

	
	@Override
	protected boolean loadOnStart() {
		return false;
	}

	@Override
	public void onStart() {
		super.onStart();
		ImageButton img = (ImageButton)getView().findViewById(R.id.message_search_img);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText txt = (EditText) getView().findViewById(R.id.messages_search);
				if (txt != null && txt.getText() != null) {
					filter.setSearchText(txt.getText().toString().trim());
				}
				position = 0;
				load();
			}
		});
	}

	@Override
	protected CharSequence getTitle() {
		return getString(R.string.title_search);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.messages_search, container, false);
	}

	@Override
	protected NotificationFilter initFilter() {
		NotificationFilter filter = new NotificationFilter();
		return filter;
	}
	@Override
	protected boolean hasFunnelSelector() {
		return false;
	}
	@Override
	protected boolean hasLabelSelector() {
		return false;
	}
	@Override
	protected boolean isFunnelSelectorEnabled() {
		return false;
	}
	@Override
	protected boolean isLabelSelectorEnabled() {
		return false;
	}
}
