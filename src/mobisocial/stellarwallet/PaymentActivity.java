package mobisocial.stellarwallet;

import mobisocial.stellar.connect.StellarCallback;
import mobisocial.stellar.model.Payment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentActivity extends ActionBarActivity implements
		StellarCallback {
	private static final String TAG = "PaymentActivity";

	private static final int REQUEST_CODE_CONTACT = 1;

	private Handler mTaskHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			new AlertDialog.Builder(PaymentActivity.this)
					.setCancelable(false)
					.setTitle("Payment submitted")
					.setMessage("Your payment has been successfully submitted!")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									PaymentActivity.this.finish();
								}

							}).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		final EditText amountTxt = (EditText) findViewById(R.id.amount_txt);
		final EditText destTxt = (EditText) findViewById(R.id.receiver_txt);

		Button contactBtn = (Button) findViewById(R.id.contact_btn);
		contactBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent contactListIntent = new Intent(PaymentActivity.this,
						ContactListActivity.class);
				startActivityForResult(contactListIntent, REQUEST_CODE_CONTACT);
			}

		});

		Button submitBtn = (Button) findViewById(R.id.submit_payment_button);
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String amount = amountTxt.getText().toString();
				String dest = destTxt.getText().toString();

				try {
					Double amountValue = Double.parseDouble(amount);
					Payment p = new Payment(amountValue, dest);
					submitPayment(p);
				} catch (NumberFormatException nfe) {
					Toast.makeText(getApplicationContext(), "Invalid amount",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		SharedResource.getInstance().registerNdefPushMessageActivity(this);
	}
	

	private void submitPayment(Payment payment) {
		JSONObject param = new JSONObject();
		try {
			param.put("secret",
					SharedResource.getInstance().getMyAccount().getSecret());
			JSONObject paymentJson = new JSONObject();
			paymentJson.put("Account", SharedResource.getInstance().getMyAccount().getAccount());
			paymentJson.put("Amount", payment.getAmount());
			paymentJson.put("Destination", payment.getDestination());
			paymentJson.put("TransactionType", "Payment");
			param.put("tx_json", paymentJson);

			SharedResource.getInstance().getApi().submitPayment(param, this);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	@Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
        	if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
    	    	Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
    	                NfcAdapter.EXTRA_NDEF_MESSAGES);
    	        // only one message sent during the beam
    	        NdefMessage msg = (NdefMessage) rawMsgs[0];
    	        // record 0 contains the MIME type, record 1 is the AAR, if present
    	        EditText destTxt = (EditText) findViewById(R.id.receiver_txt);
    	        destTxt.setText(new String(msg.getRecords()[0].getPayload()));
    	    }
        }
    }
	
	@Override
	protected void onNewIntent(Intent intent){
		setIntent(intent);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			EditText destTxt = (EditText) findViewById(R.id.receiver_txt);
			destTxt.setText(data
					.getStringExtra(ContactListActivity.CONTACT_ADDRESS));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.payment, menu);
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

	@Override
	public void onResult(JSONObject res) {
		mTaskHandler.obtainMessage().sendToTarget();
		Log.i(TAG, res.toString());
	}

	@Override
	public void onFailure(String msg) {
		Log.e(TAG, msg);
	}
}
