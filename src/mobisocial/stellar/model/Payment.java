package mobisocial.stellar.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Payment implements Serializable {
	private static final String TAG = "Payment";
	private static final long serialVersionUID = -9075167004834767031L;
	private String account;
	private String amount;
	private String destination;
	private String transactionType;

	public Payment(String account, String amount, String destination,
			String transactionType) {
		this.account = account;
		this.amount = amount;
		this.destination = destination;
		this.transactionType = transactionType;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public JSONObject toJsonObject() {
		JSONObject rootJson = new JSONObject();
		JSONObject txJson = new JSONObject();
		try {
			txJson.put("Account", account);
			txJson.put("Amount", amount);
			txJson.put("Destination", destination);
			txJson.put("TransactionType", transactionType);
			rootJson.put("tx_json", txJson);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		
		return rootJson;
	}

}
