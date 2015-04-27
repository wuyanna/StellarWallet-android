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
	private String secret;
	
	public Account() {
		
	}
	
	public Account(String accountAddress) {
		account = accountAddress;
	}
	
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

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	// TODO Delete
	// Dummy account generator
	public static Account getDummyAccount(int index) {
		if (index == 0) {
			Account acc = new Account("gUuM2jUW8ifGZMm2tFdnkfcxsyDqmup5XD");
			acc.setSecret("sfYnf5suNQA9fvzCLFG4EZsBLsxZoHcsrazHFPhxF5QsKjWN1zU");
			return acc;
		} else {
			Account acc = new Account("gE7u95bndwGPiAPJnGdsaXXhM79Tqdojyv");
			acc.setSecret("sfJ7TTaSdebFJNAr1Vch1VCNNGpsKU5hJNBjWaocC5RX4aMLGch");
			return acc;
		}
	}

}
