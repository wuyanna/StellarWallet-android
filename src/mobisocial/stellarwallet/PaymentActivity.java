package mobisocial.stellarwallet;

import mobisocial.stellar.connect.AbstractConnectTask;
import mobisocial.stellar.connect.WebsocketHelper;
import mobisocial.stellar.model.Payment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentActivity extends ActionBarActivity {
	private static final String TAG = "PaymentActivity";
	private static final String COMMAND_SUBMIT_PAYMENT = "submit";
	private static final int REQUEST_CODE_CONTACT = 1;

	private SubmitPaymentTask mPaymentTask;

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
					mPaymentTask = new SubmitPaymentTask();
					mPaymentTask.setPayment(p);
					mPaymentTask.connect();
				} catch (NumberFormatException nfe) {
					Toast.makeText(getApplicationContext(), "Invalid amount",
							Toast.LENGTH_LONG).show();
				}
			}
		});
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

	private class SubmitPaymentTask extends AbstractConnectTask {

		private Payment payment;

		public SubmitPaymentTask() {
			WebsocketHelper socketHelper = new WebsocketHelper();
			setConnectHelper(socketHelper);
		}

		@Override
		public JSONObject requestBody() {
			JSONObject param = new JSONObject();
			try {
				param.put("command", COMMAND_SUBMIT_PAYMENT);
				param.put("secret",
						"sfYnf5suNQA9fvzCLFG4EZsBLsxZoHcsrazHFPhxF5QsKjWN1zU");
				JSONObject paymentJson = new JSONObject();
				paymentJson
						.put("Account", "gUuM2jUW8ifGZMm2tFdnkfcxsyDqmup5XD");
				paymentJson.put("Amount", payment.getAmount());
				paymentJson.put("Destination", payment.getDestination());
				paymentJson.put("TransactionType", "Payment");
				param.put("tx_json", paymentJson);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return param;
		}

		@Override
		protected void onResultAvailable(JSONObject res) {
			mTaskHandler.obtainMessage().sendToTarget();
			Log.i(TAG, res.toString());
		}

		@Override
		protected void onResultFailed(String msg) {
			Log.e(TAG, msg);
		}

		public Payment getPayment() {
			return payment;
		}

		public void setPayment(Payment payment) {
			this.payment = payment;
		}

	}
}
