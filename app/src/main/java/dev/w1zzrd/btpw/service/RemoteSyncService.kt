package dev.w1zzrd.btpw.service

import android.app.Service
import android.content.Intent
import android.os.Binder

data class SyncServiceBinder(val service: RemoteSyncService): Binder()
class RemoteSyncService: Service() {
    override fun onBind(intent: Intent?) = SyncServiceBinder(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}