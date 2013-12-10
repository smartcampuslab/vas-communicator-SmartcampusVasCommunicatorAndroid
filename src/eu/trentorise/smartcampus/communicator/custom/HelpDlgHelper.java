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
package eu.trentorise.smartcampus.communicator.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import eu.trentorise.smartcampus.communicator.R;

public class HelpDlgHelper {

	public static void helpHint(final Context ctx, int txtId, final String key) {
		boolean shown = ctx.getSharedPreferences("_help_dialog_shown", Context.MODE_PRIVATE).getBoolean(key, false);
		if (shown) return;
		helper(ctx, txtId, key, true);
	}

	public static void help(final Context ctx, int txtId) {
		helper(ctx, txtId, null, false);
	}

	private static void helper(final Context ctx, int txtId, final String key, boolean isHint) {
		LayoutInflater inflatter = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		View view = inflatter.inflate(R.layout.helpdialog, null);
		builder.setView(view);
//		builder.setPositiveButton("OK", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//			}
//		});
		WebView wv = ((WebView)view.findViewById(R.id.helpMessage));

		String str= ctx.getString(txtId);
		wv.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
		
		final AlertDialog dialog = builder.create();
		
		if (isHint) {
			CheckBox cb = (CheckBox) view.findViewById(R.id.helpCheckbox);
			cb.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ctx.getSharedPreferences("_help_dialog_shown", Context.MODE_PRIVATE).edit().putBoolean(key, true).commit();
					dialog.dismiss();
				}
			});
			
		} else {
			view.findViewById(R.id.help_show).setVisibility(View.GONE);
		}
		dialog.show();
	}

}
