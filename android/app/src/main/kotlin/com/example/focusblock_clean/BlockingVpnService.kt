package com.example.focusblock_clean

import android.content.Intent
import android.net.VpnService
import kotlin.concurrent.thread

class BlockingVpnService : VpnService() {

    private var vpnThread: Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        vpnThread = thread {
            startVpnService()
        }
        return START_STICKY
    }

    private fun startVpnService() {
        try {
            val builder = Builder()
            builder.setSession("FocusBlock")
            builder.addAddress("192.0.2.1", 24)
            builder.addRoute("0.0.0.0", 0)
            builder.addDnsServer("8.8.8.8")
            builder.addDnsServer("8.8.4.4")

            val vpnInterface = builder.establish() ?: return

            vpnInterface.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vpnThread?.interrupt()
    }
}
