package com.newline.borad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newline.borad.utils.DisappearingDoodleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(DisappearingDoodleView(this))
        val intent = Intent(this,IdeaService::class.java)
        startService(intent)
        finish()
    }
}
