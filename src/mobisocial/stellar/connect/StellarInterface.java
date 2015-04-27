package mobisocial.stellar.connect;

import org.json.JSONObject;

public interface StellarInterface {
	
	public void initializeConnection(StellarCallback callback);
	
	public void terminateConnection(StellarCallback callback);
	
	public void createWallet(JSONObject requestBody, StellarCallback callback);
	
	public void accountInfo(JSONObject requestBody, StellarCallback callback);
	
	public void submitPayment(JSONObject requestBody, StellarCallback callback);
	
	public void login(JSONObject requestBody, StellarCallback callback);
	
	public void signup(JSONObject requestBody, StellarCallback callback);

}
