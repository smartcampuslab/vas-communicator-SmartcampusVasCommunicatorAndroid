package eu.trentorise.smartcampus.communicator.fragments.messages;

import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.communicator.model.NotificationFilter;

public class InboxFragment extends AbstractMessageListFragment {

	@Override
	protected CharSequence getTitle() {
		return getString(R.string.title_inbox);
	}

	@Override
	protected NotificationFilter initFilter() {
		return new NotificationFilter();
	}
	@Override
	protected boolean hasFunnelSelector() {
		return true;
	}
	@Override
	protected boolean hasLabelSelector() {
		return true;
	}
	@Override
	protected boolean isFunnelSelectorEnabled() {
		return true;
	}
	@Override
	protected boolean isLabelSelectorEnabled() {
		return true;
	}
}