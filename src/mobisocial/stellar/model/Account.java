package mobisocial.stellar.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Account implements Serializable {

	private static final String TAG = "Account";
	
	private static final long serialVersionUID = 2565900046706303685L;
	private static final String ACCOUNT_NAME_KEY = "Account";
	private static final String ACCOUNT_BALANCE_KEY = "Balance";
	private static final String ACCOUNT_JSON_KEY = "account_data";
	
	private String balance;
	private String account;
	
	public void resolve(JSONObject obj) {
		try {
			JSONObject accObj = obj.getJSONObject(ACCOUNT_JSON_KEY);
			account = accObj.getString(ACCOUNT_NAME_KEY);
			balance = accObj.getString(ACCOUNT_BALANCE_KEY);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public String getBalance() {
		return balance;
	}

	public String getAccount() {
		return account;
	}

	

}
