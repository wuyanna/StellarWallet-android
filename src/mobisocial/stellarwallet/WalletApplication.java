package mobisocial.stellarwallet;

import android.app.Application;
import android.content.Context;

public class WalletApplication extends Application {
	private static Context context;

	public void onCreate() {
		super.onCreate();
		WalletApplication.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return WalletApplication.context;
	}
}
