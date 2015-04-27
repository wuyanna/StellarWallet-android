package mobisocial.stellar.connect;

import org.json.JSONObject;

public interface StellarCallback {
	public void onResult(JSONObject res);

	public void onFailure(String msg);
}
