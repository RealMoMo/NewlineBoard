package com.newline.borad.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.newline.borad.IdeaListenerService
import com.newline.borad.IdeaService

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/9/9 14:31
 * @describe
 */
class BootBroadCast:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {
            startNewlineIdeaFunction(it)
        }

    }

    private fun startNewlineIdeaFunction(context: Context){

        val intent = Intent(context, IdeaListenerService::class.java)
        context.startService(intent)
    }
}