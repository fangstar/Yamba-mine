package com.wordpress.mfcoding.yamba;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity {
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT, StatusData.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_text, R.id.text_created_at};
	//ListView listView;
	Cursor cursor;
	SimpleCursorAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.timeline);
		//listView = (ListView) findViewById(R.id.listOut);

		cursor = ((YambaApp) getApplication()).statusData.query();
		
		adapter = new SimpleCursorAdapter(this, 
				android.R.layout.two_line_list_item, cursor, FROM, TO);
		//adapter = new SimpleCursorAdapter(this, R, c, from, to, flags)
//		adapter = new SimpleCursorAdapter(this, 
//				R.layout.row, cursor, FROM, TO, 0);

		adapter.setViewBinder(VIEW_BINDER);
		//getListView().setAdapter(adapter);
		setTitle(R.string.timeline);
		setListAdapter(adapter);
		
/*		while (cursor.moveToNext()) {
			String user = cursor.getString(cursor
					.getColumnIndex(StatusData.C_USER));
			String text = cursor.getString(cursor
					.getColumnIndex(StatusData.C_TEXT));
			//textOut.append(String.format("%s\n %s", user, text));
		}*/

	} // onCreate
	
	static final ViewBinder VIEW_BINDER = new ViewBinder() {

		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			// following two if check for the same thing
			if (view.getId() != R.id.text_created_at) return false;
			if (cursor.getColumnIndex(StatusData.C_CREATED_AT) != columnIndex) return false;
			long time = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
			CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
			((TextView) view).setText(relativeTime);
			return true;
		}		
	};
}
