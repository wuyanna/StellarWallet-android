package mobisocial.stellarwallet;

import mobisocial.stellar.connect.StellarCallback;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends ActionBarActivity implements StellarCallback {
	private static final String TAG = "SignupActivity";
	private static final int MESSAGE_SIGN_UP = 1;
	private TextView hintLbl;
	private EditText emailTxt;
	private EditText pswdTxt;
	private EditText confirmTxt;
	
	private Handler mTaskHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(SignupActivity.this, "Signup succeeded", Toast.LENGTH_LONG).show();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		hintLbl = (TextView) findViewById(R.id.signup_hint);
		emailTxt = (EditText) findViewById(R.id.signup_email);
		pswdTxt = (EditText) findViewById(R.id.choose_password);
		confirmTxt = (EditText) findViewById(R.id.confirm_password);
		
		Button signupBtn = (Button) findViewById(R.id.sign_up_button);
		signupBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				attempSignup();
				
			}
			
		});
	}
	
	private void attempSignup() {
		if (emailTxt.getText().length() == 0) {
			hintLbl.setText("Please enter valid email");
			return;
		}
		
		if (pswdTxt.getText().length() == 0) {
			hintLbl.setText("Please enter valid password");
			return;
		}
		
		if (!pswdTxt.getText().equals(confirmTxt.getText())) {
			hintLbl.setText("Confirm password doesn't match");
			return;
		}
		
		String username = emailTxt.getText().toString();
		String password = pswdTxt.getText().toString();
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("email", username);
			obj.put("password", password);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		SharedResource.getInstance().getApi().signup(obj, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
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
		mTaskHandler.obtainMessage(MESSAGE_SIGN_UP, res).sendToTarget();
		
	}

	@Override
	public void onFailure(String msg) {
		// TODO Auto-generated method stub
		
	}
}
