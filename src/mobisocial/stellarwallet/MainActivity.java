package mobisocial.stellarwallet;

import mobisocial.stellar.connect.AbstractConnectTask;
import mobisocial.stellar.connect.IConnectJSONObjectCallback;
import mobisocial.stellar.connect.WebsocketHelper;
import mobisocial.stellar.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		IConnectJSONObjectCallback {
	private static final String TAG = "MainActivity";
	private static final int INTENT_LOGIN = 1;
	private static final int INTENT_CREATE_PAYMENT = 2;
	private static final int ACCOUNT_INFO_RESULT = 1;

	private TextView accountNameLabel;
	private TextView accountBalanceLabel;
	private Button createPaymentBtn;
	private Button viewTxHistoryBtn;

	private AccountInfoTask mAccountTask;

	private Handler mTaskHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Account acc = (Account) msg.obj;
			String accountName = acc.getAccount();
			String accountBalance = acc.getBalance();

			showAccountInfo(accountName, accountBalance);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// user must log in first
		// ensureLoggedIn();

		mAccountTask = new AccountInfoTask(this);
		mAccountTask.setAccountInfo("gUuM2jUW8ifGZMm2tFdnkfcxsyDqmup5XD");
		mAccountTask.connect();

		setContentView(R.layout.activity_main);
		initContentView();
	}

	private void ensureLoggedIn() {
		// open login activity
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivityForResult(loginIntent, INTENT_LOGIN);
	}

	private void createPayment() {
		// open payment activity
		Intent paymentIntent = new Intent(this, PaymentActivity.class);
		startActivityForResult(paymentIntent, INTENT_CREATE_PAYMENT);
	}

	private void viewTransactionHistory() {
		// open transaction list activity
		Intent txHistoryIntent = new Intent(this, TransactionListActivity.class);
		startActivity(txHistoryIntent);
	}

	private void showAccountInfo(String accountName, String accountBalance) {
		if (accountNameLabel != null && accountBalanceLabel != null) {
			accountNameLabel.setText(accountName);
			accountBalanceLabel.setText(accountBalance);
		} else {
			Log.e("MainActivity",
					"showAccountInfo error: contentView not initialized");
		}
	}

	private void initContentView() {
		accountNameLabel = (TextView) findViewById(R.id.account_name);
		accountBalanceLabel = (TextView) findViewById(R.id.account_balance);
		createPaymentBtn = (Button) findViewById(R.id.create_payment_btn);
		createPaymentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createPayment();
			}

		});
		viewTxHistoryBtn = (Button) findViewById(R.id.view_history_btn);
		viewTxHistoryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewTransactionHistory();
			}

		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == INTENT_LOGIN) {
			if (resultCode == Activity.RESULT_OK) {

			} else {
				throw new RuntimeException("Login error");
			}
		} else if (requestCode == INTENT_CREATE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				// TODO refreshAccountInfo
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	private class AccountInfoTask extends AbstractConnectTask {

		private static final String COMMAND_ACCOUNT_INFO = "account_info";
		private String accountAddr;

		public AccountInfoTask(IConnectJSONObjectCallback callback) {
			super(callback);
			WebsocketHelper socketHelper = new WebsocketHelper();
			setConnectHelper(socketHelper);
		}

		public void setAccountInfo(String accountAddr) {
			this.accountAddr = accountAddr;
		}

		@Override
		public JSONObject requestBody() {
			JSONObject param = new JSONObject();
			try {
				param.put("command", COMMAND_ACCOUNT_INFO);
				param.put("account", accountAddr);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
			return param;
		}

	}

	@Override
	public void onResult(JSONObject obj) {
		try {
			JSONObject result = obj.getJSONObject("result");
			Account accountObj = new Account();
			accountObj.resolve(result);
			mTaskHandler.obtainMessage(ACCOUNT_INFO_RESULT, accountObj)
					.sendToTarget();
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public void onFailed(String msg) {
		// TODO Auto-generated method stub

	}
}
