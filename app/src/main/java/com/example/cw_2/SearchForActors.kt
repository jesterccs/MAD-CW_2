package com.example.cw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import com.example.cw_2.data.UserDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchForActors : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_actors)

        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val searchActor = findViewById<EditText>(R.id.searchActor)
        val tv = findViewById<TextView>(R.id.textView)

        val db = Room.databaseBuilder(this, UserDatabase::class.java,"MyDatabase").build()
        val userDao = db.userDao()

        searchBtn.setOnClickListener{
            val editValue = searchActor.text.toString()

            runBlocking {
                launch {
                    val movieName = java.lang.StringBuilder()
                    val mnList : List<String> = userDao.search(editValue).toList()
                    for(i in mnList){
                        movieName.append("* $i\n\n")
                    }
                    tv.text=movieName
                }
                //tv.text=stb
            }

        }
    }


}