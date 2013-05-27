package com.example.prolog;

import java.util.HashMap;
import java.util.Set;

import com.example.prolog.ViewGroupActivity.MyAlertDialogFragment;
import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContactFragment extends Fragment {
    ImageButton imagebuttonEdit,imagebuttonDelete;
	private Fragment fragment=this;

	ContactsDataSource datasource;
	Contact contact;
	public final static String TAG = ViewContactFragment.class.getSimpleName();
	long contactId;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		contactId = getArguments().getLong("contactId");

		datasource = new ContactsDataSource(getActivity());
		datasource.open();
		contact = datasource.findContactbyId(contactId);

		QuickContactBadge quickContactBadge = (QuickContactBadge) getActivity()
				.findViewById(R.id.quickContactBadge);
		Log.i(TAG, contactId + "    this one");
		if (contact.getPhoto() != null)
			quickContactBadge.setImageBitmap(contact.getPhoto());
		else
			quickContactBadge.setImageResource(R.drawable.face);

		TextView tvName = (TextView) getActivity().findViewById(
				R.id.textViewName);
		TextView tvTitle = (TextView) getActivity().findViewById(
				R.id.textViewTitle);
		TextView tvCompany = (TextView) getActivity().findViewById(
				R.id.textViewContactCompany);
		TextView tvPhone = (TextView) getActivity().findViewById(
				R.id.textViewPhone);
		TextView tvEmail = (TextView) getActivity().findViewById(
				R.id.textViewEmail);
		TextView tvLocation = (TextView) getActivity().findViewById(
				R.id.textViewLocation);
		
		
		
		  /***
         * Display custom fields
         */
        if (contact.getAllCustomFields() != null && contact.getAllCustomFields().size() > 0){
            
            TableLayout tl=(TableLayout)getActivity().findViewById(R.id.viewContactDetailFrame);
                        
            TextView tvFieldName = null;
            TextView tvFielValue = null;
            
            TableRow.LayoutParams lparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);  
            
            HashMap<String, Object> customFields = contact.getAllCustomFields();
            Set<String> keys = customFields.keySet();
            
            // for each custom field
            for (String string : keys) {
                tvFieldName = new TextView(ViewContactFragment.this.getActivity());
                tvFieldName.setText(string);
                tvFieldName.setLayoutParams(lparams);
                tvFieldName.setTextAppearance(ViewContactFragment.this.getActivity(),android.R.style.TextAppearance_Medium);
                tvFieldName.setTextColor(Color.WHITE);
                tvFieldName.setEms(4);
                
                tvFielValue = new TextView(ViewContactFragment.this.getActivity());
                tvFielValue.setText((String)contact.getCustomField(string));
                tvFielValue.setLayoutParams(lparams);
                tvFielValue.setTextAppearance(ViewContactFragment.this.getActivity(),android.R.style.TextAppearance_Medium);
                tvFielValue.setTextColor(Color.WHITE);              
                tvFielValue.setEms(10);
                
                // Make text type specific
                Linkify.addLinks(tvFielValue, Linkify.EMAIL_ADDRESSES|Linkify.MAP_ADDRESSES | Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
                
                // create a new row for label and edit text field
                TableRow.LayoutParams tparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);                    
                TableRow tr = new TableRow(ViewContactFragment.this.getActivity());
                tr.setLayoutParams(tparams);
                
                tr.addView(tvFieldName);
                tr.addView(tvFielValue);
                
                                            
                tl.addView(tr, tl.getChildCount() - 1);
            }
            
        }
		 imagebuttonEdit = (ImageButton) getActivity().findViewById(
				R.id.imageButtonEdit);
		 imagebuttonDelete = (ImageButton) getActivity()
				.findViewById(R.id.imageButtonDelete);

		tvName.setText(contact.getName());
		tvTitle.setText(contact.getTitle());
		tvCompany.setText(contact.getCompany());
		tvPhone.setText(contact.getHome_phone());
		tvEmail.setText(contact.getEmail());
		tvLocation.setText(contact.getLocation());
		
		// Make text type specific
        Linkify.addLinks(tvPhone, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(tvLocation, Linkify.MAP_ADDRESSES);
		

		

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datasource.open();
		
		imagebuttonEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putLong("contactId", contactId);
				EditContactFragment editContactFragment = new EditContactFragment();
				editContactFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, editContactFragment).detach(fragment)
						.addToBackStack(null).commit();
			}
		});

		imagebuttonDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment newFragment = MyAlertDialogFragment
						.newInstance(contactId);
				newFragment.show(getFragmentManager(), "dialog");

			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		datasource.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (RelativeLayout) inflater.inflate(R.layout.fragment_view_contact,
				container, false);
	}

	public static class MyAlertDialogFragment extends DialogFragment {

		public static MyAlertDialogFragment newInstance(long contactId) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putLong("contactId", contactId);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final long contactId = getArguments().getLong("contactId");

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Are you sure you want to delete this Contact?");
			alertDialogBuilder.setPositiveButton(R.string.OK,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							ContactsDataSource datasource = new ContactsDataSource(
									getActivity());
							;
							datasource.open();
							// TODO should also delete the groups in which the
							// contact is in
							// and anything else?
							datasource.deleteContactCompletely(contactId);
							datasource.close();
							getActivity().finish();
							startActivity(new Intent(getActivity(),
									ContactListActivity.class));

						}
					});
			alertDialogBuilder.setNegativeButton(R.string.Cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});

			return alertDialogBuilder.create();
		}
	}

	protected void deleteContact(long contactId2) {

	}
}
