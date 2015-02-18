package mobisocial.stellar.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Payment implements Serializable {
	private static final String TAG = "Payment";
	private static final long serialVersionUID = -9075167004834767031L;
	private Double amount;
	private String destination;
	private String currency;
	
	public Payment(Double amount, String destination) {
		this(amount, destination, "STR");
	}

	public Payment(Double amount, String destination, String currency) {
		this.amount = amount;
		this.destination = destination;
		this.currency = currency;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}

	public String getDestination() {
		return destination;
	}

	public String getCurrency() {
		return currency;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public JSONObject toJsonObject() {
		JSONObject txJson = new JSONObject();
		try {
			txJson.put("amount", amount.toString());
			txJson.put("destination_address", destination);
			txJson.put("currency", currency);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		
		return txJson;
	}

}
