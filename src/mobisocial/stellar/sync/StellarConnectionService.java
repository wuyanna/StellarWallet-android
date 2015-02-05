package mobisocial.stellar.sync;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class StellarConnectionService extends Service {
	
	private final IBinder mBinder = new LocalBinder();
	
	public class LocalBinder extends Binder {
		public StellarConnectionService getService() {
			return StellarConnectionService.this;
		}
	}
	
	public void registerConnectionListener() {
		
	}

	public void unregisterConnectionListener() {
		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

}
