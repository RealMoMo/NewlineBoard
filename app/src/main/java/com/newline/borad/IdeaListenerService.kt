package com.newline.borad

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.newline.borad.global.*

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
        intentFilter.addAction(ACTION_V811_GESTURES)
        intentFilter.addAction(ACTION_MSTAR_BOTTOM_TWO_FINGER_GESTURES)
        registerReceiver(newlineBroadcast,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(newlineBroadcast)
        newlineBroadcast = null
    }

    private fun startIdeaService(){
        val ideaIntent = Intent(this,IdeaService::class.java)
        startService(ideaIntent)
    }


    inner class NewlineBroadcast :BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                ACTION_V811_GESTURES->{
                    intent.getStringExtra(MODE_GESTURES_TYPE)?.let {
                        when(it){
                            //只有底部或顶部滑动 才启动便签
                            GESTURES_TOP, GESTURES_BOTTOM->{
                                startIdeaService()
                            }
                        }
                    }
                }
                ACTION_MSTAR_BOTTOM_TWO_FINGER_GESTURES->{
                    startIdeaService()
                }
            }
        }

    }
}