package com.example.cw_2

import android.graphics.Color
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

        val searchBtn = findViewById<Button>(R.id.searchBtn)                    // Search button
        val searchActor = findViewById<EditText>(R.id.searchActor)              // Edit text
        val tv = findViewById<TextView>(R.id.textView)                          // Textview to display results

        val db = Room.databaseBuilder(this, UserDatabase::class.java,"MyDatabase").build()
        val userDao = db.userDao()


        /*
        -
         */
        searchBtn.setOnClickListener{
            val editValue = searchActor.text.toString()
            try {
                runBlocking {
                    launch {
                        val movieName = java.lang.StringBuilder()                           // String builder to store movie names
                        val mnList: List<String> = userDao.search(editValue).toList()       // List to store movie names that recieved directly from the db
                        for (i in mnList) {
                            movieName.append("* $i\n\n")
                        }
                        tv.text = movieName
                    }
                }
            }catch (e : Exception){
                tv.text = "Enter a valid name."
                tv.setTextColor(Color.RED)
            }

        }
    }


}