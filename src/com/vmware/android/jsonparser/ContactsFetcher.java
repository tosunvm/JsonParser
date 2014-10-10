package com.vmware.android.jsonparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ContactsFetcher {

	private static final String TAG = "ContactsFetcher";
	// JSON Node names
	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_PHONE_MOBILE = "mobile";
	private static final String TAG_PHONE_HOME = "home";
	private static final String TAG_PHONE_OFFICE = "office";

	void parseItems(ArrayList<HashMap<String, String>> items, String jsonStr) {

		// contacts JSONArray
		JSONArray contacts = null;

		try {
			JSONObject jsonObj = new JSONObject(jsonStr);

			// Getting JSON Array node
			contacts = jsonObj.getJSONArray(TAG_CONTACTS);

			// looping through All Contacts
			for (int i = 0; i < contacts.length(); i++) {
				JSONObject c = contacts.getJSONObject(i);

				String id = c.getString(TAG_ID);
				String name = c.getString(TAG_NAME);
				String email = c.getString(TAG_EMAIL);
				String address = c.getString(TAG_ADDRESS);
				String gender = c.getString(TAG_GENDER);

				// Phone node is JSON Object
				JSONObject phone = c.getJSONObject(TAG_PHONE);
				String mobile = phone.getString(TAG_PHONE_MOBILE);
				String home = phone.getString(TAG_PHONE_HOME);
				String office = phone.getString(TAG_PHONE_OFFICE);

				// tmp hashmap for single contact
				HashMap<String, String> contact = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				contact.put(TAG_ID, id);
				contact.put(TAG_NAME, name);
				contact.put(TAG_EMAIL, email);
				contact.put(TAG_PHONE_MOBILE, mobile);

				// adding contact to contact list
				items.add(contact);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<HashMap<String, String>> apacheDownloadContactItems(
			String url) {
		ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		Log.i(TAG, "URL called: " + url);
		// Creating service handler class instance
		HttpApache sh = new HttpApache();
		// Making a request to url and getting response
		String jsonStr = sh.makeHttpCall(url, HttpApache.GET);

		longInfo("Apache Response: > " + jsonStr);
		if (jsonStr != null) {

			parseItems(items, jsonStr);
		} else {
			Log.e(TAG, "Couldn't get any data from the url");
		}

		return items;

	}

	public ArrayList<HashMap<String, String>> javaDownloadContactItems(
			String url) {
		ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		Log.i(TAG, "URL called: " + url);
		// Creating service handler class instance
		HttpJava jh = new HttpJava();
		// Making a request to url and getting response
		try {
			String jsonStr = jh.getUrl(url);
			longInfo("Java Response: > " + jsonStr);

			if (jsonStr != null) {

				parseItems(items, jsonStr);
			} else {
				Log.e(TAG, "Couldn't get any data from the url");
			}

		} catch (IOException ioe) {
			Log.e(TAG, "Failed to fetch items", ioe);
		}

		return items;

	}

	public static void longInfo(String str) {
		if (str.length() > 4000) {
			Log.i(TAG, str.substring(0, 4000));
			longInfo(str.substring(4000));
		} else
			Log.i(TAG, str);
	}
}
