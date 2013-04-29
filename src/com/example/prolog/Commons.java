package com.example.prolog;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import com.example.prolog.db.ContactsDBOpenHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Commons {

	public final static String pleaseEnterNameContact="Please enter the name of your contact";
	public final static String pleaseEnterNameGroup="Please enter a name for the group";
	public final static String pleaseEnterTitleFollowUp="Please enter a title for the follow up";
	public final static int TAB_ID_FOLLOWUP_DETAIL=9;
	public final static int TAB_ID_NEW_FOLLOW_UP=10;

	public final static int TAB_ID_ADD_INTERACTION=6;
	public final static int NEXT_ACTION_NEW_INTERACTION=20;
	public final static int NEXT_ACTION_NEW_FOLLOW_UP=19;

	public final static String TAB_ID="tabId";
	public final static String callingActivity="callingActivity";
	public final static String NEXT_ACTION="nextAction";

	public final static String FOLLOWUP_ID="followUpId";
	public final static String CONTACT_ID="contactId";
	public final static String INTERACTION_ID="interactionId";

	public static Bitmap getImageFromBlob(byte[] blob) {
		return BitmapFactory.decodeByteArray(blob, 0, blob.length);
	}
	
	public static byte[] getBlobfromImage(Bitmap bitmap)	{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

		return out.toByteArray();
	}
}
