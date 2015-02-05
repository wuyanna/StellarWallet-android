package mobisocial.stellar.connect;

import org.json.JSONObject;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.JSONObjectBody;

public class SimpleHttpHelper extends AbstractConnectHelper {
	
	private static final String HTTP_URL = "";
	
	public void connect() {
		AsyncHttpPost post = new AsyncHttpPost(HTTP_URL);
		post.setBody(new JSONObjectBody(requestBody));
		AsyncHttpClient.getDefaultInstance().executeJSONObject(post, new AsyncHttpClient.JSONObjectCallback() {
		    // Callback is invoked with any exceptions/errors, and the result, if available.
		    @Override
		    public void onCompleted(Exception e, AsyncHttpResponse response, JSONObject result) {
		        if (e != null) {
		            e.printStackTrace();
		            return;
		        }
		        System.out.println("I got a JSONObject: " + result);
		        callback.onResult(result);
		    }
		});
	}

}
