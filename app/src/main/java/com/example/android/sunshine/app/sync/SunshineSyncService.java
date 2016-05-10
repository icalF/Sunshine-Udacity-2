package com.example.android.sunshine.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by icalF on 5/9/2016.
 */
public class SunshineSyncService extends Service {
  private static final Object sSyncAdapterLock = new Object();
  private static SunshineSyncAdapter sSunshineSyncAdapter = null;

  @Override
  public void onCreate() {
    Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
    synchronized (sSyncAdapterLock) {
      if (sSunshineSyncAdapter == null) {
        sSunshineSyncAdapter = new SunshineSyncAdapter(getApplicationContext(), true);
      }
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return sSunshineSyncAdapter.getSyncAdapterBinder();
  }
}
