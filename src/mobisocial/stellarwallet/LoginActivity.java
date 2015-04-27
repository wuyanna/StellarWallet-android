package mobisocial.stellarwallet;

import mobisocial.stellar.connect.StellarCallback;
import mobisocial.stellar.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends ActionBarActivity implements StellarCallback {
	private static final String TAG = "LoginActivity";
	private static final int MESSAGE_SHOW_PARAMS = 1;
	private static final int MESSAGE_LOGIN = 2;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mEmailLoginFormView;
	private View mLoginFormView;

	private Handler mTaskHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SHOW_PARAMS:
				attemptLogin();
				break;
			case MESSAGE_LOGIN:
				Log.i(TAG, "Logged in");
				Intent intent = new Intent();
				
				// TODO
				Account acc;
				if (mEmailView.getText().charAt(0) == 'u') {
					acc = Account.getDummyAccount(0);
				} else {
					acc = Account.getDummyAccount(1);
				}
				
				SharedResource.getInstance().setMyAccount(acc);
	            // Set result and finish this Activity
	            setResult(Activity.RESULT_OK, intent);
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO delete
				mTaskHandler.obtainMessage(MESSAGE_LOGIN, null).sendToTarget();
				
				
//				attemptLogin();
			}
		});

		Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
		mSignUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptSignup();
			}
		});
		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		mEmailLoginFormView = findViewById(R.id.email_login_form);
	}
	
	private void attemptSignup() {
		Intent intent = new Intent(this, SignupActivity.class);
		startActivity(intent);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		String username = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("email", username);
			obj.put("password", password);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		SharedResource.getInstance().getApi().login(obj, this);
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}


	@Override
	public void onResult(JSONObject res) {
		mTaskHandler.obtainMessage(MESSAGE_LOGIN, res).sendToTarget();
	}

	@Override
	public void onFailure(String msg) {
		if (msg != null) {
			Log.e(TAG, msg);
		}
	}

}
