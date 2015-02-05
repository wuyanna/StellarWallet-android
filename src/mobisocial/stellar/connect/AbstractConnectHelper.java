package mobisocial.stellar.connect;

import org.json.JSONObject;

public abstract class AbstractConnectHelper {
	
	static public interface ConnectCallback {
		public void onResult(JSONObject res);
		public void onFailure(String msg);
	}
	
	protected JSONObject requestBody;
	protected ConnectCallback callback;
	
	public void setConnectCallback(ConnectCallback callback) {
		this.callback = callback;
	}
	
	public void setRequestBody(JSONObject obj) {
		requestBody = obj;
	}
	
	abstract public void connect();
}
