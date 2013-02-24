package com.example.prolog;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyTabActivity extends Activity {

	
	public static final String LOGTAG=MyTabActivity.class.getSimpleName();
	public long contactId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		contactId = b.getLong("contactId");
		Log.i(LOGTAG,"Contact id: "+ contactId );
		
		ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		String label1 = getResources().getString(R.string.tab_label_1);
		Tab tab = actionBar.newTab();
		tab.setText(label1);
		TabListener<ViewContactFragment> tl = new TabListener<ViewContactFragment>(this,
				label1, ViewContactFragment.class);
		tab.setTabListener(tl);
		actionBar.addTab(tab);

		String label2 = getResources().getString(R.string.tab_label_2);
		tab = actionBar.newTab();
		tab.setText(label2);
		TabListener<ViewInteractionFragment> tl2 = new TabListener<ViewInteractionFragment>(this,
				label2, ViewInteractionFragment.class);
		tab.setTabListener(tl2);
		actionBar.addTab(tab);
		
		String label3 = getResources().getString(R.string.tab_label_3);
		tab = actionBar.newTab();
		tab.setText(label3);
		TabListener<ViewFollowUpFragment> tl3 = new TabListener<ViewFollowUpFragment>(this,
				label2, ViewFollowUpFragment.class);
		tab.setTabListener(tl3);
		actionBar.addTab(tab);

	}

	
	
	private class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				//tab.setTag(contactId);
				Bundle b = new Bundle(); 
				b.putLong("contactId", contactId); 
		       // mFragment.setArguments(args); 
				mFragment = Fragment.instantiate(mActivity, mClass.getName(),b);
			
			//mFragment = Fragment.instantiate(mActivity, mClass.getName(), (Bundle) tab.getTag());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

}