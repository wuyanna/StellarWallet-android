package mobisocial.stellar.connect;

import org.json.JSONObject;

public abstract class AbstractConnectHelper {
	
	protected IConnectJSONObjectCallback callback;
	protected JSONObject requestBody;
	
	public void setJSONObjectCallback(IConnectJSONObjectCallback callback) {
		this.callback = callback;
	}
	
	public void setRequestBody(JSONObject obj) {
		requestBody = obj;
	}
	
	abstract public void connect();
}
