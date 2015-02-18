package mobisocial.stellar.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Transaction implements Serializable {

	private static final String TAG = "Transaction";
	
	private static final long serialVersionUID = -4610522173455902214L;
	private static final String ACCOUNT_NAME_KEY = "Account";
	private static final String AMOUNT_KEY = "Amount";
	private static final String TRANSACTION_JSON_KEY = "tx";
	
	private String account;
	private String amount;
	private String destination;
	private String fee;
	private String transactionType;
	private String txHash;
	private int ledgerIndex;

	public void resolve(JSONObject obj) {
		// TODO implement
		try {
			JSONObject accObj = obj.getJSONObject(TRANSACTION_JSON_KEY);
			account = accObj.getString(ACCOUNT_NAME_KEY);
			amount = accObj.getString(AMOUNT_KEY);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public String getAccount() {
		return account;
	}

	public String getAmount() {
		return amount;
	}

	public String getDestination() {
		return destination;
	}

	public String getFee() {
		return fee;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String getTxHash() {
		return txHash;
	}

	public int getLedgerIndex() {
		return ledgerIndex;
	}

}
