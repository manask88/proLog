package com.example.prolog.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;



public class BlobHelper {

	public static final String LOGERR = "ERR";
	
	/**
	 * Convert ArratList<String> to bytes
	 * @param newCustomField
	 * @param customField
	 * @return
	 */
	public static byte [] object2Byte(Object obj) {
				
	    ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    ObjectOutputStream  dout = null;   
	    try {
	    	dout= new ObjectOutputStream (bout);
	    	dout.writeObject(obj);
			dout.close();
		} catch (IOException e) {
			Log.e(LOGERR,"Unable to object to bytes", e);			
		}
	   return bout.toByteArray();
	}
	
	
	/**
	 * Convert bytes to Object
	 * @param blob
	 * @return
	 */
	public static Object byte2Object(byte[] blob) {
		Object obj = new Object();
		
		ObjectInputStream bin;
		try {
			bin = new ObjectInputStream (new ByteArrayInputStream(blob));
			obj= bin.readObject();
		} catch (IOException e) {
			Log.e(LOGERR,"Unable to convert bytes to ArrayList<String> ", e);
		} catch (ClassNotFoundException e) {
			Log.e(LOGERR,"Unable to convert bytes to ArrayList<String> ", e);
		}
		
					
		return obj;
	}
	
	/**
	 * Convert bytes to HashMap<String, Object>
	 * @param blob
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> byte2HashMap(byte[] blob) {		
					
		return (HashMap<String, Object>) byte2Object(blob);
	}
	
}
