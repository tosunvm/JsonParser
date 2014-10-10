package com.vmware.android.jsonparser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListFragment extends ListFragment {
	
	private static final String TAG = "ContactListFragment";
	// URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";
    private ProgressDialog pDialog;

    private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";

 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;
    
    private Button mCreateCrimeButton;
	private TextView mEmptyListTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setHasOptionsMenu(true);
		getActivity().setTitle(R.string.contacts_title);
		contactList = new ArrayList<HashMap<String, String>>();
		
		// Calling async task to get json
        new GetContacts().execute();		
	}

	//@TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.fragment_generic_list, parent, false);

		mEmptyListTextView = (TextView) v.findViewById(R.id.empty_list_text_view);
        mEmptyListTextView.setText("There are no crimes, please add one.");
        mCreateCrimeButton = (Button)v.findViewById(R.id.create_crime_button);

        return v;
	}

	private class ContactAdapter extends ArrayAdapter<HashMap<String, String>> {

		public ContactAdapter(ArrayList<HashMap<String, String>> contacts) {
			super(getActivity(), 0, contacts);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// If we weren't given a view, inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_contact, null);
			}
			// Configure the view for this Crime
			HashMap<String, String> c = getItem(position);

			TextView nameTextView = (TextView) convertView
					.findViewById(R.id.contact_list_item_nameTextView);
			nameTextView.setText(c.get(TAG_NAME));
			TextView emailTextView = (TextView) convertView
					.findViewById(R.id.contact_list_item_emailTextView);
			emailTextView.setText(c.get(TAG_EMAIL));

			return convertView;
		}

	}
	
	/**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            //contactList = new ContactsFetcher().javaDownloadContactItems(url);
            contactList = new ContactsFetcher().apacheDownloadContactItems(url);
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ContactAdapter adapter = new ContactAdapter(contactList);
            setListAdapter(adapter);
        }
 
    }

}
