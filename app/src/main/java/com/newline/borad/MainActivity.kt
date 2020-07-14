package com.newline.borad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newline.borad.widget.note.NoteWidget

class MainActivity : AppCompatActivity() {

    private lateinit var noteView : NoteWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //testNoteView()

        testWindowEffect()


        finish()
    }


    private fun testNoteView(){
        noteView = NoteWidget(this)
        setContentView(noteView)
        noteView.startShowNoteAnimation()
    }


    private fun testWindowEffect(){
        //setContentView(DisappearingDoodleView(this))

        val intent = Intent(this,IdeaService::class.java)
        startService(intent)


        val intent2 = Intent(this,IdeaListenerService::class.java)
        startService(intent2)
    }
}
