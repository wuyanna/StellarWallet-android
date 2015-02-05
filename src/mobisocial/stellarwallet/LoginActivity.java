package mobisocial.stellarwallet;

import mobisocial.stellar.connect.AbstractConnectTask;
import mobisocial.stellar.connect.SimpleHttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends ActionBarActivity {
	private static final String TAG = "LoginActivity";
	private static final int MESSAGE_SHOW_PARAMS = 1;
	private static final int MESSAGE_LOGIN = 2;
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	private ShowUserParamsTask mParamsTask = null;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mEmailLoginFormView;
	private View mSignOutButtons;
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
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

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
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		mEmailLoginFormView = findViewById(R.id.email_login_form);
		mSignOutButtons = findViewById(R.id.plus_sign_out_buttons);
	}

	private void getUserParams() {
		if (mAuthTask != null || mParamsTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String username = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(username)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mParamsTask = new ShowUserParamsTask();
			mParamsTask.setUsername(username);
			mParamsTask.connect();
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null || mParamsTask == null) {
			return;
		}
		String username = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		mAuthTask = new UserLoginTask();
		mAuthTask.setLoginParams(username,
				WalletUtils.deriveWalletId(username, password));
		mAuthTask.connect();
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

	private class UserLoginTask extends AbstractConnectTask {

		private String username;
		private String walletId;

		public UserLoginTask() {
			setConnectHelper(new SimpleHttpHelper());
		}

		void setLoginParams(String username, String walletId) {
			this.username = username;
			this.walletId = walletId;
		}

		@Override
		public JSONObject requestBody() {
			JSONObject obj = new JSONObject();
			try {
				obj.put("username", username);
				obj.put("walletId", walletId);
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				return null;
			}
			return obj;
		}

		@Override
		protected void onResultAvailable(JSONObject res) {
			mTaskHandler.obtainMessage(MESSAGE_LOGIN, res).sendToTarget();
		}

		@Override
		protected void onResultFailed(String msg) {
			Log.e(TAG, msg);
		}
	}

	private class ShowUserParamsTask extends AbstractConnectTask {
		private String username;

		public ShowUserParamsTask() {
			setConnectHelper(new SimpleHttpHelper());
		}

		public void setUsername(String username) {
			this.username = username;
		}

		@Override
		public JSONObject requestBody() {
			JSONObject obj = new JSONObject();
			try {
				obj.put("username", username);
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				return null;
			}
			return obj;
		}

		@Override
		protected void onResultAvailable(JSONObject res) {
			mTaskHandler.obtainMessage(MESSAGE_SHOW_PARAMS, res).sendToTarget();
		}

		@Override
		protected void onResultFailed(String msg) {
			Log.e(TAG, msg);
		}
	}

}
