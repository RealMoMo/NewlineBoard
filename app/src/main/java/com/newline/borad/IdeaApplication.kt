package com.newline.borad

import android.app.Application
import com.newline.borad.utils.ScreenAdapterUtils

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/9/9 14:38
 * @describe
 */
class IdeaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //假4k
//        ScreenAdapterUtils.setDensity(this,640F,true)

        //真实适配 1920 hdpi 一套图适配
        ScreenAdapterUtils.setDensity(this,1280F,true)
    }
}