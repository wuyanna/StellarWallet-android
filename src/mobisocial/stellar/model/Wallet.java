package mobisocial.stellar.model;

import mobisocial.stellarwallet.WalletUtils;

import org.json.JSONObject;

public class Wallet {
	
	private String id;
	private KeyChainData keyChainData;
	private MainData mainData;
	
	public Wallet(JSONObject encryptedWallet) {
		
	}
	
	public void decrypt(JSONObject encryptedWallet, String id, byte[] key) {
		try {
			JSONObject keyChainJson = WalletUtils.decryptData(encryptedWallet.getString("keychainData"), key);
			JSONObject mainChainJson = WalletUtils.decryptData(encryptedWallet.getString("mainData"), key);
			keyChainData = new KeyChainData(keyChainJson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class KeyChainData {
		private String signingKey;
		
		public KeyChainData(JSONObject obj) {
			
		}
	}
	
	public class MainData {
		
	}

}
