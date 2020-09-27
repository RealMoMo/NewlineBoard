package com.newline.borad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.newline.borad.widget.note.NoteWidget
import com.newline.draw.toolbar.DrawBarManager

class MainActivity : AppCompatActivity() {

    private lateinit var noteView : NoteWidget
    private lateinit var cl : ConstraintLayout
    private lateinit var drawBarManager: DrawBarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ///startNoteView()

        startWindowEffect()


        finish()
    }


    private fun startNoteView(){
        noteView = NoteWidget(this)
        setContentView(noteView)
        noteView.startShowNoteAnimation()

    }


    private fun startWindowEffect(){
        val intent = Intent(this,IdeaService::class.java)
        startService(intent)


        val intent2 = Intent(this,IdeaListenerService::class.java)
        startService(intent2)
    }
}
