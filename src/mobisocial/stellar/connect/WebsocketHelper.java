package mobisocial.stellar.connect;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.WebSocketConnectCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.WebSocket.StringCallback;

public class WebsocketHelper extends AbstractConnectHelper {
	
	private static final String SOCKET_ADDR = "http://live.stellar.org:9001";
	
	@Override
	public void connect() {
		AsyncHttpGet get = new AsyncHttpGet(SOCKET_ADDR);
    	AsyncHttpClient.getDefaultInstance().websocket(get, null, new WebSocketConnectCallback() {
    	    @Override
    	    public void onCompleted(Exception ex, WebSocket webSocket) {
    	        if (ex != null) {
    	            ex.printStackTrace();
    	            return;
    	        }
//    	        String json = "{\"command\": \"account_lines\",\"id\": 1234567,\"account\": \"ganVp9o5emfzpwrG5QVUXqMv8AgLcdvySb\"}";
    	        webSocket.send(requestBody.toString());
    	        webSocket.setStringCallback(new StringCallback() {
    	            public void onStringAvailable(String s) {
    	            	Log.i("websockethelper", s);
    	                JSONObject obj;
						try {
							obj = new JSONObject(s);
							callback.onResult(obj);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    	                
    	            }
    	        });
    	    }
    	});
	}
}
