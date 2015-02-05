package mobisocial.stellar.connect;

import org.json.JSONObject;

public abstract class AbstractConnectTask {
	
	public static final String WALLET_SERVER = "";
	public static final String API_SERVER = "";
	public static final String STELLAR_SERVER = "";
	
	protected AbstractConnectHelper connectHelper;
	private IConnectJSONObjectCallback callback;
	
	public AbstractConnectTask(IConnectJSONObjectCallback callback) {
		this.callback = callback;
	}

	public void connect() {
		if (connectHelper == null) {
			throw new RuntimeException("Set connect helper first");
		}
		JSONObject param = requestBody();
		if (param != null) {
			connectHelper.setRequestBody(param);
		}
		connectHelper.connect();
	}
	
	public void setConnectHelper(AbstractConnectHelper connectHelper) {
		this.connectHelper = connectHelper;
		this.connectHelper.setJSONObjectCallback(callback);
	}
	
	abstract public JSONObject requestBody();
	
}
