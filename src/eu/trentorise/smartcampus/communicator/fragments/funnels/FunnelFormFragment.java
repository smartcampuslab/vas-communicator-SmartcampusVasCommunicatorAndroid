package eu.trentorise.smartcampus.communicator.fragments.funnels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import eu.trentorise.smartcampus.communicator.R;
import eu.trentorise.smartcampus.android.common.SCAsyncTask;
import eu.trentorise.smartcampus.communicator.custom.AbstractAsyncTaskProcessor;
import eu.trentorise.smartcampus.communicator.custom.FunnelFilterRenderer;
import eu.trentorise.smartcampus.communicator.custom.data.CommunicatorHelper;
import eu.trentorise.smartcampus.communicator.model.Action;
import eu.trentorise.smartcampus.communicator.model.CommunicatorConstants;
import eu.trentorise.smartcampus.communicator.model.Funnel;
import eu.trentorise.smartcampus.communicator.model.LabelObject;
import eu.trentorise.smartcampus.communicator.model.Preference;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class FunnelFormFragment extends SherlockFragment {

	public static final String ARG_FUNNEL = "funnel";
	Funnel funnel = null;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if (getArguments() != null)
			funnel = (Funnel) getArguments().getSerializable(ARG_FUNNEL);
	}

	private Funnel getFunnel() {
		if (funnel == null && getArguments() != null) {
			funnel = (Funnel) getArguments().getSerializable(ARG_FUNNEL);
		}
		return funnel;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.funnelform, container, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onStart() {
		super.onStart();
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(false);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);

		Spinner spinner = (Spinner) getView().findViewById(R.id.funnel_sourcetype);
		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		if (getFunnel() != null) {
			// title
			TextView tv = (TextView) this.getView().findViewById(R.id.funnel_name);
			tv.setText(getFunnel().getTitle());
			// source type
			((ArrayAdapter<String>)spinner.getAdapter()).add(CommunicatorConstants.getFunnelTypeLabel(getActivity(), getFunnel().getSourceType()));
			spinner.setEnabled(false);
			// labels
			updateLabels();
			getSherlockActivity().setTitle("Edit Channel: "+getFunnel().getTitle());
		} else {
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String sourceTypeLabel = dataAdapter.getItem(position);
					TreeMap<String, String> funnelTypeLabels = CommunicatorConstants.getFunnelTypes(getActivity());
					for (Entry<String,String> entry : funnelTypeLabels.entrySet()) {
						if (entry.getValue().equals(sourceTypeLabel)) {
							getFunnel().setSourceType(entry.getKey());
							getFunnel().setFunnelFilterData(null);
							setUpFilters(getFunnel());
							break;
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			
			funnel = new Funnel();
			TreeMap<String, String> funnelTypeLabels = CommunicatorConstants.getFunnelTypes(getActivity());
			funnel.setSourceType(funnelTypeLabels.firstKey());
			for (String f : funnelTypeLabels.values()) {
				((ArrayAdapter<String>)spinner.getAdapter()).add(f);
			}

			getSherlockActivity().setTitle("New Channel");
		}
		
		((Button)getView().findViewById(R.id.funnel_cancel)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSherlockActivity().getSupportFragmentManager().popBackStack();
			}
		}); 
		
		((Button)getView().findViewById(R.id.funnel_ok)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveFunnel();
			}
		}); 

		((ImageButton)getView().findViewById(R.id.funnel_add_labels)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createLabelsDialog(getFunnel());
			}
		});
		
		// destinations
		setUpDestinations(getFunnel());
		
		// filters
		setUpFilters(getFunnel());
	}

	private void setUpFilters(final Funnel funnel) {
		ImageButton addFilterButton = (ImageButton)getView().findViewById(R.id.funnel_add_filters);
		final FunnelFilterRenderer renderer = CommunicatorConstants.getFunnelTypeRenderer(getActivity(), funnel.getSourceType());
		final int layoutId = CommunicatorConstants.getFunnelTypeLayout(getActivity(), funnel.getSourceType());
		if (renderer != null && layoutId > 0) {
			updateFilters(renderer.getFilterDataDescription(funnel.getFunnelFilterData()));
			addFilterButton.setEnabled(true);
			addFilterButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setInverseBackgroundForced(true);
					final View layout = getActivity().getLayoutInflater().inflate(layoutId, null);
					builder.setView(layout);
					builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Map<String,Object> data = renderer.validate(layout);
							if (data != null) {
								funnel.setFunnelFilterData(data);
								updateFilters(renderer.getFilterDataDescription(data));
								dialog.dismiss();
							}
						}
					});
					AlertDialog dlg = builder.create();
					renderer.setUpView(dlg, getActivity(), layout, funnel.getFunnelFilterData());
				}
			});
		} else {
			updateFilters("");
			addFilterButton.setEnabled(false);
		}
	}

	private void setUpDestinations(Funnel funnel) {
		Preference prefs = CommunicatorHelper.getPreferences();
		ImageButton addDestButton = (ImageButton)getView().findViewById(R.id.funnel_add_destinations);
		
		final LinearLayout layout = (LinearLayout)getView().findViewById(R.id.funnel_fields); 
		int idx = layout.indexOfChild(addDestButton);
		int startIdx = layout.indexOfChild(getView().findViewById(R.id.funnel_destinations));
		if (idx - startIdx > 1) return;
		
		List<Action> defaultActions = CommunicatorConstants.getDefaultFunnelActions(getActivity());
		HashSet<String> added = new HashSet<String>();
		for (Action a : defaultActions) {
			CheckBox checkbox = new CheckBox(getActivity());
			checkbox.setTag(a);
			checkbox.setText(a.getLabel());
			added.add(a.getLabel());
			checkbox.setTextColor(getResources().getColor(R.color.sc_dark_gray));
			if (funnel != null && funnel.asActionMap().containsKey(a.getLabel())) {
				checkbox.setChecked(true);
			}
			layout.addView(checkbox,idx++);
		}
		
		if (prefs != null && prefs.getActions() != null && prefs.getActions().size() > 0) {
			for (Action a : prefs.getActions()) {
				added.add(a.getLabel());
				CheckBox checkbox = new CheckBox(getActivity());
				checkbox.setTag(a);
				checkbox.setText(a.getLabel());
				checkbox.setTextColor(getResources().getColor(R.color.sc_dark_gray));
				if (funnel != null && funnel.asActionMap().containsKey(a.getLabel())) {
					checkbox.setChecked(true);
				}
				layout.addView(checkbox,idx++);
			}
		}
		if (funnel != null && funnel.getActions() != null && funnel.getActions().size() > 0) {
			for (Action a : funnel.getActions()) {
				if (added.contains(a.getLabel())) continue;
				
				CheckBox checkbox = new CheckBox(getActivity());
				checkbox.setTag(a);
				checkbox.setText(a.getLabel());
				checkbox.setTextColor(getResources().getColor(R.color.sc_dark_gray));
				if (funnel != null && funnel.asActionMap().containsKey(a.getLabel())) {
					checkbox.setChecked(true);
				}
				layout.addView(checkbox,idx++);
			}
		}
		
		addDestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int newIdx = layout.indexOfChild(v);
				final View nv = getActivity().getLayoutInflater().inflate(R.layout.destination, layout, false);
				layout.addView(nv,newIdx);
				((EditText)nv.findViewById(R.id.destination_txt)).setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							((CheckBox)nv.findViewById(R.id.destination_chk)).setChecked(true);
						}
					}
				});
			}
		});
	}
	
	protected void saveFunnel() {
		String title = ((EditText)getView().findViewById(R.id.funnel_name)).getText().toString();
		if (title != null && title.trim().length() > 0) {
			getFunnel().setTitle(title.trim());
		} else {
			Toast.makeText(getActivity(), R.string.funnel_name_empty, Toast.LENGTH_LONG).show();
			getView().findViewById(R.id.funnel_name).requestFocus();
			return;
		}
		
		LinearLayout layout = (LinearLayout)getView().findViewById(R.id.funnel_fields); 
		TextView textView = (TextView)getView().findViewById(R.id.funnel_destinations);
		ImageButton addDestButton = (ImageButton)getView().findViewById(R.id.funnel_add_destinations);
		int positionFrom = layout.indexOfChild(textView)+1;
		int positionTo = layout.indexOfChild(addDestButton);
		List<Action> actionList = new ArrayList<Action>();
		List<Action> userDefined = new ArrayList<Action>();
		for (int i = positionFrom; i < positionTo; i++) {
			View view = layout.getChildAt(i);
			CheckBox check = null;
			if (view.getTag() != null && view.getTag() instanceof Action && view instanceof CheckBox) {
				check = (CheckBox) view;
				if (check.isChecked()) actionList.add((Action)view.getTag());
			} else if (view instanceof LinearLayout) {
				check = (CheckBox)view.findViewById(R.id.destination_chk);
				EditText edit = (EditText)view.findViewById(R.id.destination_txt);
				if (check.isChecked() && edit.getText() != null && edit.getText().toString().trim().length() > 0) {
					if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit.getText()).matches()) {
						Toast.makeText(getActivity(), R.string.email_format, Toast.LENGTH_LONG).show();
						edit.requestFocus();
						return;
					}

					Action a = new Action();
					a.setLabel(edit.getText().toString().trim());
					a.setValue(a.getLabel());
					a.setType(Action.EMAIL_ACTION_TYPE);
					userDefined.add(a);
				}

			}
		}
		actionList.addAll(userDefined);
		getFunnel().setActions(actionList);
		new SCAsyncTask<Object, Void, Boolean>(getActivity(), new SaveFunnelProcessor(getActivity())).execute(getFunnel(), userDefined);
	}

	protected void updateLabels() {
		TextView tv = null;
		LinearLayout ll = (LinearLayout) getView().findViewById(R.id.funnel_labels);
		ll.removeAllViews();

		if (getFunnel().getLabelIds() != null && getFunnel().getLabelIds().size() > 0) {
			for (int i = 0; i < getFunnel().getLabelIds().size(); i++) {
				LabelObject lo = CommunicatorHelper.getLabel(getFunnel().getLabelIds().get(i));
				if (lo == null) continue;
				tv = new TextView(getActivity());
				tv.setText(lo.getName());
				tv.setPadding(5, 0, 0, 0);
				tv.setBackgroundColor(Long.decode(lo.getColor()).intValue());
				ll.addView(tv);
			}
		}
	}

	protected void updateFilters(CharSequence data) {
		((TextView)getView().findViewById(R.id.funnel_filters)).setText(data);
	}

	private void createLabelsDialog(final Funnel content) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		final List<LabelObject> labels = CommunicatorHelper.getLabels();
		final CharSequence[] items = new CharSequence[labels.size()];
		final boolean[] checked = new boolean[items.length];
		for (int i = 0; i < checked.length; i++) {
			checked[i] = false;
			items[i] = labels.get(i).getName();
			if (content != null && content.getLabelIds() != null) {
				for (String l : content.getLabelIds()) {
					if (l.equals(labels.get(i).getId())) {
						checked[i] = true;
						break;
					}
				}
			}
		}
		builder.setTitle(R.string.funnel_add_labels_dlg_title);
		builder.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				((AlertDialog) dialog).getListView().setItemChecked(which, isChecked);
			}
		});
		builder.setPositiveButton(android.R.string.ok, null);
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		final AlertDialog dlg = builder.create();
		dlg.show();
		dlg.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> list = new ArrayList<String>();
				SparseBooleanArray array = dlg.getListView()
						.getCheckedItemPositions();
				for (int i = 0; i < labels.size(); i++) {
					if (array.get(i))
						list.add(labels.get(i).getId());
				}
				getFunnel().setLabelIds(list);
				updateLabels();
				dlg.dismiss();
			}
		});
		
	}

	private class SaveFunnelProcessor extends AbstractAsyncTaskProcessor<Object, Boolean> {
		public SaveFunnelProcessor(Activity activity) {
			super(activity);
		}
		@SuppressWarnings("unchecked")
		@Override
		public Boolean performAction(Object... params) throws SecurityException, Exception {
			return CommunicatorHelper.saveFunnel((Funnel)params[0], (List<Action>)params[1]);
		}

		@Override
		public void handleResult(Boolean result) {
			if (result) {
				getSherlockActivity().getSupportFragmentManager().popBackStack();
			}
		}
		
	}
}
