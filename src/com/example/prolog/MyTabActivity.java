package com.example.prolog;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MyTabActivity extends Activity {

	
	public static final String TAG=MyTabActivity.class.getSimpleName();
	public long contactId;
	ActionBar actionBar;
	int tabId;
	
	
	/* @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            finish();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	 }
	*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTitle("proLog");
		super.onCreate(savedInstanceState);
	
		Bundle b = getIntent().getExtras();
		contactId = b.getLong("contactId");
		Log.i(TAG,"Contact id: "+ contactId );
		
		actionBar = getActionBar();

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
				label3, ViewFollowUpFragment.class);
		tab.setTabListener(tl3);
		actionBar.addTab(tab);
		
		
		
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG,"onActivityResult");

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Bundle  b = getIntent().getExtras();
		Log.i(TAG,"onResume");
		Log.i(TAG,"followUpId: "+ b.getLong(Commons.FOLLOWUP_ID) );
		Log.i(TAG,"contactId: "+ b.getLong(Commons.CONTACT_ID) );

		String label3 = getResources().getString(R.string.tab_label_3);
		String label2 = getResources().getString(R.string.tab_label_2);

		Tab tab = actionBar.newTab();
		tabId = b.getInt(Commons.TAB_ID);
		Log.i(TAG,"tabId:"+tabId);
		switch(tabId)
		{
		case Commons.TAB_ID_FOLLOWUP_DETAIL: 
		
			
			tab = actionBar.newTab();
			tab.setText(label3);
			TabListener<ViewFollowUpDetailsFragment> tl3 = new TabListener<ViewFollowUpDetailsFragment>(this,
					label3, ViewFollowUpDetailsFragment.class);
			tab.setTabListener(tl3);
			actionBar.removeTabAt(2);
			actionBar.addTab(tab, 2, true);
			break;
		 //actionBar.setSelectedNavigationItem(2);
		case Commons.TAB_ID_NEW_FOLLOW_UP: 
		
			
			tab.setText(label3);
			TabListener<NewFollowUpFragment> tl3_new_follow_up = new TabListener<NewFollowUpFragment>(this,
					label3, NewFollowUpFragment.class);
			tab.setTabListener(tl3_new_follow_up);
			actionBar.removeTabAt(2);
			actionBar.addTab(tab, 2, true);
			break;
		case Commons.TAB_ID_ADD_INTERACTION: 
			
			tab.setText(label2);
			TabListener<NewInteractionFragment> tl2 = new TabListener<NewInteractionFragment>(this,
					label2, NewInteractionFragment.class);
			tab.setTabListener(tl2);
			actionBar.removeTabAt(1);
			actionBar.addTab(tab, 1, true);
			break;
			
		}
		
		
		
		
		
		
		
		
		
		
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
				
				switch (tabId)
				{
				case Commons.TAB_ID_FOLLOWUP_DETAIL:
					
					b.putLong(Commons.FOLLOWUP_ID, getIntent().getExtras().getLong(Commons.FOLLOWUP_ID)); 
					Log.i(TAG,"followUpId: "+ getIntent().getExtras().getLong(Commons.FOLLOWUP_ID) );
					break;
				case Commons.TAB_ID_ADD_INTERACTION:	
					b.putLong(Commons.CONTACT_ID, getIntent().getExtras().getLong(Commons.CONTACT_ID)); 
					break;
				case Commons.TAB_ID_NEW_FOLLOW_UP:	
					b.putLong(Commons.CONTACT_ID, getIntent().getExtras().getLong(Commons.CONTACT_ID)); 
					break;
				}
				
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