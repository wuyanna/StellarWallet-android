package mobisocial.stellar.connect;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.WebSocketConnectCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.WebSocket.StringCallback;
import com.koushikdutta.async.http.body.JSONObjectBody;

public class StellarApiImpl implements StellarInterface {
	
	private static final String COMMAND = "command";
	private static final String TAG = "StellarApiImpl";
	public static final String WALLET_SERVER = "http://192.169.44.63:9000/";
	public static final String API_SERVER = "http://192.169.44.63:9000/";
	public static final String STELLAR_SERVER = "http://live.stellar.org:9001";
	
	private static final String STATUS_KEY = "status";
	private static final String STATUS_SUCCESS = "success";
	private static final String STATUS_ERROR = "fail";
	private static final String ERROR_MSG_KEY = "error_message";
	private static final String RESULT_KEY = "result";
	
	private static final String COMMAND_ACCOUNT_INFO = "account_info";
	private static final String COMMAND_SUBMIT_PAYMENT = "submit";
	
	private WebSocket stellarWebsocket;
	
	@Override
	public void initializeConnection(final StellarCallback callback) {
		AsyncHttpGet get = new AsyncHttpGet(STELLAR_SERVER);
    	AsyncHttpClient.getDefaultInstance().websocket(get, null, new WebSocketConnectCallback() {
    	    @Override
    	    public void onCompleted(Exception ex, WebSocket webSocket) {
    	        if (ex != null) {
    	            Log.e(TAG, ex.toString());
    	            callback.onFailure(ex.toString());
    	            return;
    	        }
    	        stellarWebsocket = webSocket;
    	        callback.onResult(null);
    	    }
    	});
	}
	
	private void send(JSONObject requestBody, final StellarCallback callback) {
		if (stellarWebsocket == null) {
			throw new RuntimeException("Init websocket first");
		}
		stellarWebsocket.send(requestBody.toString());
		stellarWebsocket.setStringCallback(new StringCallback() {
            public void onStringAvailable(String s) {
            	Log.i(TAG, s);
                JSONObject obj;
				try {
					obj = new JSONObject(s);
					handleResult(obj, callback);
				} catch (JSONException e) {
					callback.onFailure(e.getMessage());
				}
                
            }
        });
	}
	
	private void httpPost(String path, JSONObject requestBody, final StellarCallback callback) {
		AsyncHttpPost post = new AsyncHttpPost(WALLET_SERVER + path);
		post.setBody(new JSONObjectBody(requestBody));
		AsyncHttpClient.getDefaultInstance().executeJSONObject(post, new AsyncHttpClient.JSONObjectCallback() {
		    // Callback is invoked with any exceptions/errors, and the result, if available.
		    @Override
		    public void onCompleted(Exception e, AsyncHttpResponse response, JSONObject result) {
		        if (e != null) {
		            callback.onFailure(e.getMessage());
		            return;
		        }
		        callback.onResult(result);
		    }
		});
	}
	
	private void handleResult(JSONObject response, StellarCallback callback) {
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
			callback.onFailure(errmsg);
			return;
		}
		
		try {
			JSONObject result = response.getJSONObject(RESULT_KEY);
			callback.onResult(result);
			return;
		} catch (JSONException e) {
			callback.onResult(response);
			return;
		}
		
	}
	

	@Override
	public void terminateConnection(StellarCallback callback) {

	}

	@Override
	public void createWallet(JSONObject requestBody, StellarCallback callback) {
		send(requestBody, callback);
	}

	@Override
	public void accountInfo(JSONObject requestBody, StellarCallback callback) {
		try {
			requestBody.put(COMMAND, COMMAND_ACCOUNT_INFO);
		} catch (JSONException e) {
			throw new RuntimeException("Json exception");
		}
		send(requestBody, callback);
	}

	@Override
	public void submitPayment(JSONObject requestBody, StellarCallback callback) {
		try {
			requestBody.put(COMMAND, COMMAND_SUBMIT_PAYMENT);
		} catch (JSONException e) {
			throw new RuntimeException("Json exception");
		}
		send(requestBody, callback);
	}

	@Override
	public void login(JSONObject requestBody, StellarCallback callback) {
		httpPost("login", requestBody, callback);	
	}

	@Override
	public void signup(JSONObject requestBody, StellarCallback callback) {
		httpPost("signup", requestBody, callback);
	}

}
