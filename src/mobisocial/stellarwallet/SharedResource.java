package mobisocial.stellarwallet;

import mobisocial.stellar.connect.StellarApiImpl;
import mobisocial.stellar.connect.StellarInterface;
import mobisocial.stellar.model.Account;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;

public class SharedResource {
	private static SharedResource mInstance = null;
	private static String MY_PREFS_NAME = "prefs";
	
	private StellarInterface api = new StellarApiImpl();
	private Account myAccount;
	private NfcAdapter mNfcAdapter;
	
	public Account getMyAccount() {
		if (myAccount == null) {
			SharedPreferences prefs = WalletApplication.getAppContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
			String accountAddr = prefs.getString("account", null);
			String secret = prefs.getString("secret", null);
			if (accountAddr != null && secret != null) {
				myAccount = new Account(accountAddr);
				myAccount.setSecret(secret);
			}
		}
		return myAccount;
	}

	public void setMyAccount(Account myAccount) {
		this.myAccount = myAccount;
		SharedPreferences prefs = WalletApplication.getAppContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("account", myAccount.getAccount());
		editor.putString("secret", myAccount.getSecret());
		editor.commit();
	}

	private SharedResource() {
		
	}
	
	public static SharedResource getInstance() {
		if (mInstance == null) {
			mInstance = new SharedResource();
		}
		return mInstance;
	}

	public StellarInterface getApi() {
		return api;
	}
	
	public NfcAdapter getDefaultNfcAdapter() {
		// Check for available NFC Adapter
        if (mNfcAdapter == null) {
        	mNfcAdapter = NfcAdapter.getDefaultAdapter(WalletApplication.getAppContext());
        }
        return mNfcAdapter;
	}

	public void registerNdefPushMessageActivity(Activity activity) {
		// Register callback
        getDefaultNfcAdapter().setNdefPushMessageCallback(callback, activity);
	}
	
	private CreateNdefMessageCallback callback = new CreateNdefMessageCallback() {

		@Override
		public NdefMessage createNdefMessage(NfcEvent arg0) {
			String text = SharedResource.getInstance().getMyAccount().getAccount();
	        NdefMessage msg = new NdefMessage(NdefRecord.createMime(
	                "text/plain", text.getBytes())
	         /**
	          * The Android Application Record (AAR) is commented out. When a device
	          * receives a push with an AAR in it, the application specified in the AAR
	          * is guaranteed to run. The AAR overrides the tag dispatch system.
	          * You can add it back in to guarantee that this
	          * activity starts when receiving a beamed message. For now, this code
	          * uses the tag dispatch system.
	          */
	          //,NdefRecord.createApplicationRecord("com.example.android.beam")
	        );
	        return msg;
		}
    	
    };
}
