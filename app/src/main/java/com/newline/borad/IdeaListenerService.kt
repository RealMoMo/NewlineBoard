package com.newline.borad

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/14 14:32
 * @describe
 */
class IdeaListenerService : Service() {

    private var newlineBroadcast: NewlineBroadcast?=null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        newlineBroadcast = NewlineBroadcast()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.ist.broadcast.system.Gestures")
        intentFilter.addAction("com.android.internal.action.swipe.from.bottom2")
        registerReceiver(newlineBroadcast,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(newlineBroadcast)
        newlineBroadcast = null
    }


    inner class NewlineBroadcast :BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            val ideaIntent = Intent(context,IdeaService::class.java)
            startService(ideaIntent)
        }

    }
}