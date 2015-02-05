package mobisocial.stellar.connect;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractConnectTask implements AbstractConnectHelper.ConnectCallback {
	
	public static final String WALLET_SERVER = "";
	public static final String API_SERVER = "";
	public static final String STELLAR_SERVER = "";
	
	private static final String STATUS_KEY = "status";
	private static final String STATUS_SUCCESS = "success";
	private static final String STATUS_ERROR = "error";
	private static final String ERROR_MSG_KEY = "error_message";
	private static final String RESULT_KEY = "result";
	
	protected AbstractConnectHelper connectHelper;

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
		this.connectHelper.setConnectCallback(this);
	}
	
	abstract public JSONObject requestBody();
	
	public void onResult(JSONObject response) {
		String status = null;
		try {
			status = response.getString(STATUS_KEY);
		} catch (JSONException e) {
			// Ignore if missing status key, will check if has result
		}
		// Server returns error
		if (status != null && status.equals(STATUS_ERROR)) {
			String errmsg = null;
			try {
				errmsg = response.getString(ERROR_MSG_KEY);
			} catch (JSONException e) {
				errmsg = "unknown error";
			}
			onResultFailed(errmsg);
			return;
		}
		
		try {
			JSONObject result = response.getJSONObject(RESULT_KEY);
			onResultAvailable(result);
			return;
		} catch (JSONException e) {
			onResultFailed("No result data");
			return;
		}
		
	}
	
	public void onFailure(String msg) {
		onResultFailed(msg);
	}
	
	abstract protected void onResultAvailable(JSONObject res);
	abstract protected void onResultFailed(String msg);
	
}
