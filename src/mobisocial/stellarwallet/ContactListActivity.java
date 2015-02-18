package mobisocial.stellarwallet;

import mobisocial.stellar.model.Contact;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactListActivity extends ActionBarActivity {
	
	private static final String TAG = "ContactListActivity";
	public static final String CONTACT_ADDRESS = "contact_addr";
	private ArrayAdapter<Contact> mContactArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
		// Set result CANCELED in case the user backs out
		setResult(Activity.RESULT_CANCELED);
		mContactArrayAdapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1);
		mContactArrayAdapter.add(new Contact("StellarFoundation", "gDSSa75HPagWcvQmwH7D51dT5DPmvsKL4q"));
		ListView contactList = (ListView) findViewById(R.id.contact_list);
		contactList.setAdapter(mContactArrayAdapter);
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Contact c = mContactArrayAdapter.getItem(position);
				String address = c.getAddress();

	            // Create the result Intent and include the MAC address
	            Intent intent = new Intent();
	            intent.putExtra(CONTACT_ADDRESS, address);

	            // Set result and finish this Activity
	            setResult(Activity.RESULT_OK, intent);
	            finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
