package com.example.prolog;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import com.example.prolog.db.ContactsDBOpenHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Commons {

	public final static String pleaseEnterNameContact="Please enter the name of your contact";
	public final static String pleaseEnterNameGroup="Please enter a name for the group";
	public final static String callingActivity="callingActivity";
	
	public static Bitmap getImageFromBlob(byte[] blob) {
		return BitmapFactory.decodeByteArray(blob, 0, blob.length);
	}
	
	public static byte[] getBlobfromImage(Bitmap bitmap)	{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

		return out.toByteArray();
	}
}
