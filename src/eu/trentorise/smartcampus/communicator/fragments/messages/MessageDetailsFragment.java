/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either   express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.communicator.fragments.messages;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.android.common.view.ViewHelper;
import eu.trentorise.smartcampus.communicator.HomeActivity;
import it.smartcampuslab.communicator.R;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.model.CommunicatorConstants;
import eu.trentorise.smartcampus.communicator.model.EntityObject;
import eu.trentorise.smartcampus.communicator.model.LabelObject;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class MessageDetailsFragment extends SherlockFragment {

        public static final String ARG_MSG = "message";
        Notification message = null;

        @Override
        public void onCreate(Bundle bundle) {        	
                super.onCreate(bundle);
                setHasOptionsMenu(true);
                if (getArguments() != null) {
                        message = (Notification) getArguments().getSerializable(ARG_MSG);
                }
                if (!getMessage().isReaded()) {
                        new ToggleReadProcessor().execute();
                }
        }

        private Notification getMessage() {
                if (message == null) {
                        message = (Notification) getArguments().getSerializable(ARG_MSG);
                }
                return message;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {
                return inflater.inflate(R.layout.messagedetails, container, false);
        }

        @Override
        public void onStart() {
                super.onStart();
        		HomeActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
            	HomeActivity.drawerState = "off";
                getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
                getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);

                if (getMessage() != null) {
                        // title
                        TextView tv = (TextView) this.getView().findViewById(R.id.messagedetails_title);
                        tv.setText(getMessage().getTitle());

                        // description, optional
                        tv = (TextView) this.getView().findViewById(R.id.messagedetails_descr);
                        if (getMessage().getDescription() != null && getMessage().getDescription().length() > 0) {
                                tv.setText(getMessage().getDescription());
                        }

                        // context
                        tv = (TextView) this.getView().findViewById(R.id.messagedetails_context);
                        tv.setText(CommunicatorConstants.DATE_TIME_FORMAT.format(new Date(getMessage().getTimestamp())));

			// link
			tv = (TextView) this.getView().findViewById(R.id.messagedetails_link);
			if (getMessage().getContent() != null && getMessage().getContent().containsKey("link")) {
				tv.setText(getMessage().getContent().get("link").toString());
			} else {
				tv.setVisibility(View.GONE);
			}
			
                        // properties
                        updateLabels();
                }
        }

        protected void updateLabels() {
                TextView tv = null;
                LinearLayout ll = (LinearLayout) getView().findViewById(R.id.messagedetails_labels);
                ll.removeAllViews();

                View sourceView = getActivity().getLayoutInflater().inflate(R.layout.label_label, null);
                TextView source = (TextView) sourceView.findViewById(R.id.label_label_text);
                source.setText(CommunicatorConstants.getChannelTypeLabel(getActivity(), getMessage().getType()));
                ll.addView(sourceView);
//
//                if (getMessage().getChannelIds() != null && getMessage().getChannelIds().size() > 0) {
//                        for (int i = 0; i < getMessage().getLabelIds().size(); i++) {
//                                Channel f = CommunicatorHelper.getChannel(getMessage().getChannelIds().get(i));
//                                if (f == null) continue;
//                                View v = getActivity().getLayoutInflater().inflate(R.layout.label_label, null);
//                                tv = (TextView) v.findViewById(R.id.label_label_text);
//                                tv.setTextColor(getActivity().getResources().getColor(R.color.sc_dark_gray));
//                                tv.setText(f.getTitle());
//                                ll.addView(v);
//                        }        
//                }

                if (getMessage().getLabelIds() != null && getMessage().getLabelIds().size() > 0) {
                        for (int i = 0; i < getMessage().getLabelIds().size(); i++) {
                                LabelObject lo = CommunicatorHelper.getLabel(getMessage().getLabelIds().get(i));
                                if (lo == null) continue;
                                
                                View v = getActivity().getLayoutInflater().inflate(R.layout.label_label, null);
                                tv = (TextView) v.findViewById(R.id.label_label_text);
                                tv.setTextColor(getView().getResources().getColor(R.color.sc_gray));
                                tv.setText(lo.getName());
                                tv.setBackgroundColor(Long.decode(lo.getColor()).intValue());
                                ll.addView(v);
                        }
                }
        }

        
        @Override
        public void onPrepareOptionsMenu(Menu menu) {
                menu.clear();
                getSherlockActivity().getSupportMenuInflater().inflate(R.menu.noticedetailsmenu, menu);
                updateStarIcon(menu.findItem(R.id.messagedetailsmenu_star));
                SubMenu submenu = menu.findItem(R.id.messagedetailsmenu_toggler).getSubMenu();
                if (getMessage().getEntities() != null && getMessage().getEntities().size() > 0) {
                        int i = 0;
                        for (EntityObject e : getMessage().getEntities()) {
                                submenu.add(Menu.CATEGORY_SYSTEM, i++, Menu.NONE, e.getTitle());
                        }
                }

                super.onPrepareOptionsMenu(menu);
        }

        protected void updateStarIcon(MenuItem menu) {
                if (getMessage().isStarred()) {
                        menu.setIcon(R.drawable.actionbar_star_s);
                } else {
                        menu.setIcon(R.drawable.actionbar_star);
                }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.messagedetailsmenu_star:
                        new SCAsyncTask<MenuItem, Void, MenuItem>(getActivity(), new ToggleStarProcessor(getActivity())).execute(item);
                        break;
                case R.id.notice_option_toggle_read:
                        new ToggleReadProcessor().execute();
                        return true;
                case R.id.notice_option_remove:
                        new SCAsyncTask<Void, Void, Void>(getActivity(), new RemoveNotificationProcessor(getActivity())).execute();
                        return true;
                case R.id.notice_option_assign_labels:
                        createLabelsDialog(getMessage());
                        return true;
//                case R.id.notice_option_view_funnel:
//                        viewFunnel(getMessage());
//                        return true;
                default:
                        break;
                }
                
                if (message.getEntities() != null && item.getItemId() < message.getEntities().size()) {
                        EntityObject e = message.getEntities().get(item.getItemId());
                        if (e != null) {
                                if (e.getEntityId() != null)
                                        ViewHelper.viewInApp(getActivity(), e.getType(), e.getEntityId(), new Bundle());
                                else if (e.getId() != null)
                                        ViewHelper.viewInApp(getActivity(), e.getType(), e.getId(), new Bundle());
                                return true;
                        }
                }
                
                return super.onOptionsItemSelected(item);
        }
        
        private void createLabelsDialog(final Notification content) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final CharSequence[] items = CommunicatorHelper.getLabelNames();
                
                builder.setTitle(R.string.notice_option_assign_labels);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                new SCAsyncTask<Object, Void, Void>(getActivity(), new AssignLabelProcessor(getActivity())).execute(content, items[which]);
                                dialog.dismiss();
                        }
                        
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                        }
                });
                
                builder.setPositiveButton(R.string.btn_new_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                LabelDialog d = new LabelDialog(getActivity(), new LabelDialog.OnLabelCreatedListener() {
                                        @Override
                                        public void OnLabelCreated(LabelObject label) {
                                                new SCAsyncTask<Object, Void, Void>(getActivity(), new AssignLabelProcessor(getActivity())).execute(content, label.getName());
                                        }
                                });
                                d.setOwnerActivity(getActivity());
                                d.show();
                        }
                });
                builder.show();
        }

//        protected void viewFunnel(Notification content) {
//                FragmentTransaction ft  = getSherlockActivity().getSupportFragmentManager().beginTransaction();
//                Fragment fragment = new ChannelViewFragment();
//                Bundle args = new Bundle();
//                args.putSerializable(ChannelViewFragment.ARG_FUNNEL, CommunicatorHelper.getFunnel(content.getFunnelId()));
//                fragment.setArguments(args);
//                // Replacing old fragment with new one
//                ft.replace(android.R.id.content, fragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
//        }

        private class ToggleStarProcessor extends AbstractAsyncTaskProcessor<MenuItem, MenuItem> {
                public ToggleStarProcessor(Activity activity) {
                        super(activity);
                }
                @Override
                public MenuItem performAction(MenuItem... params) throws SecurityException, Exception {
                        CommunicatorHelper.toggleStar(getMessage());
                        return params[0];
                }
                @Override
                public void handleResult(MenuItem result) {
                        updateStarIcon(result);
                }
        }
        private class ToggleReadProcessor extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... params) {
                        try {
                                CommunicatorHelper.toggleRead(getMessage());
                        } catch (Exception e) {
                                Log.e(MessageDetailsFragment.class.getName(), "Error marking message as read: "+e.getMessage());
                        }
                        return null;
                }
        }
        private class RemoveNotificationProcessor extends AbstractAsyncTaskProcessor<Void, Void> {
                public RemoveNotificationProcessor(Activity activity) {
                        super(activity);
                }
                @Override
                public Void performAction(Void ... params) throws SecurityException, Exception {
                        CommunicatorHelper.removeNotification(getMessage()); 
                        getMessage().markDeleted();
                        return null;
                }
                @Override
                public void handleResult(Void result) {
                        getSherlockActivity().getSupportFragmentManager().popBackStack();
                }
        }
        
        private class AssignLabelProcessor extends AbstractAsyncTaskProcessor<Object, Void> {
                public AssignLabelProcessor(Activity activity) {
                        super(activity);
                }
                @Override
                public Void performAction(Object ... params) throws SecurityException, Exception {
                        CommunicatorHelper.assignLabel((Notification)params[0], (String)params[1]);
                        return null;
                }
                @Override
                public void handleResult(Void result) {
                        updateLabels();
                }
        }
}