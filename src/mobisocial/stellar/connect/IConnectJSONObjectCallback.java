package mobisocial.stellar.connect;

import org.json.JSONObject;

public interface IConnectJSONObjectCallback {
	
	public void onResult(JSONObject obj);
	
	public void onFailed(String msg);

}
