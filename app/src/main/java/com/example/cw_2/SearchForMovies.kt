package com.example.cw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import com.example.cw_2.data.User
import com.example.cw_2.data.UserDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchForMovies : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchformovies)
        val db = Room.databaseBuilder(this, UserDatabase::class.java,"MyDatabase").build()
        val userDao = db.userDao()
        val edit = findViewById<EditText>(R.id.edit)
        val retrieveBtn = findViewById<Button>(R.id.retrieve)
        val saveBtn = findViewById<Button>(R.id.save)
        val tv = findViewById<TextView>(R.id.tv)


        retrieveBtn.setOnClickListener{
            var editValue = edit.getText().toString()
            tv.setText(editValue)
        }

        /*runBlocking {
            launch {
            }
        }*/
    }
}