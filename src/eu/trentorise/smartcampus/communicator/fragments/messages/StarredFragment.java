package eu.trentorise.smartcampus.communicator.fragments.messages;

import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class StarredFragment extends AbstractMessageListFragment {

	@Override
	protected CharSequence getTitle() {
		return getString(R.string.title_starred);
	}

	@Override
	protected NotificationFilter initFilter() {
		NotificationFilter filter = new NotificationFilter();
		filter.setStarred(true);
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
