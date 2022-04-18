package com.example.cw_2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import com.example.cw_2.data.User
import com.example.cw_2.data.UserDao
import com.example.cw_2.data.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class SearchForMovies : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchformovies)
        val edit = findViewById<EditText>(R.id.edit)                            // Edit text button
        val retrieveBtn = findViewById<Button>(R.id.retrieve)                   // retrieve button
        val saveBtn = findViewById<Button>(R.id.save)                           // save to the database button
        val tv = findViewById<TextView>(R.id.tv)                                // textview that displays results
        val stb = StringBuilder()                                               // to store data that received from OMDb API
        val db = Room.databaseBuilder(this, UserDatabase::class.java,"MyDatabase").build()
        val userDao = db.userDao()


        /*
        - THis will retrieve movie details that user searched
         */
        retrieveBtn.setOnClickListener{
            val editValue = edit.text.toString()                                            // to store value of the edit text
            val url_string = "https://www.omdbapi.com/?t=$editValue&apikey=c7e832d8";       // link to the OMDb API
            val url = URL(url_string)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                runBlocking {
                    launch {
                        stb.clear()
                        withContext(Dispatchers.IO) {
                            val bf = BufferedReader(InputStreamReader(con.inputStream))
                            var line: String? = bf.readLine()
                            while (line != null) {
                                stb.append(line + "\n")
                                line = bf.readLine()
                            }
                        }
                        tv.text = parseJSON(stb).toString()
                    }
                }
            }catch(e : Exception){
                tv.text="Enter a valid movie name."
                tv.setTextColor(Color.RED)
            }
        }


        /*
        - Act as same way as the retrieve button does.
        - When user click this button, it will store the retrieved movie details to the database.
         */
        saveBtn.setOnClickListener{
            val editValue = edit.text.toString()                                            // to store value of the edit text
            val url_string = "https://www.omdbapi.com/?t=$editValue&apikey=c7e832d8";       // link to the OMDb API
            val url = URL(url_string)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                runBlocking {
                    launch {
                        stb.clear()
                        withContext(Dispatchers.IO) {
                            val bf = BufferedReader(InputStreamReader(con.inputStream))
                            var line: String? = bf.readLine()
                            while (line != null) {
                                stb.append(line + "\n")
                                line = bf.readLine()
                            }
                        }
                        parseJSON(stb)                          // parser function
                        saveMovie(stb)                          // save movie function
                    }
                }
            }catch(e : Exception){
                tv.text="Enter a valid movie name."
                tv.setTextColor(Color.RED)
            }
        }
    }


    /*
    - Parser function
     */
    private suspend fun parseJSON(stb: java.lang.StringBuilder) : StringBuilder{
        val jsonObject = JSONTokener(stb.toString()).nextValue() as JSONObject
        val movieDetails = java.lang.StringBuilder()
        val title = jsonObject.getString("Title")
        val year = jsonObject.getString("Year")
        val rated = jsonObject.getString("Rated")
        val released = jsonObject.getString("Released")
        val runTime = jsonObject.getString("Runtime")
        val genre = jsonObject.getString("Genre")
        val director = jsonObject.getString("Director")
        val writer = jsonObject.getString("Writer")
        val actors = jsonObject.getString("Actors")
        val plot = jsonObject.getString("Plot")

        movieDetails.append("Title : \"$title\" \n")
        movieDetails.append("Year : $year \n")
        movieDetails.append("Rated : $rated \n")
        movieDetails.append("Released : $released \n")
        movieDetails.append("Runtime : $runTime \n")
        movieDetails.append("Genre : $genre \n")
        movieDetails.append("Director : $director \n")
        movieDetails.append("Writer : $writer \n")
        movieDetails.append("Actors : $actors \n\n")
        movieDetails.append("Plot : \"$plot\" \n")
        return movieDetails
    }


    private suspend fun saveMovie(stb: java.lang.StringBuilder) {
        val tvv = findViewById<TextView>(R.id.tvv)
        val db = Room.databaseBuilder(this, UserDatabase::class.java,"MyDatabase").build()
        val userDao = db.userDao()
        val jsonObject = JSONTokener(stb.toString()).nextValue() as JSONObject

        val title = jsonObject.getString("Title")
        val year = jsonObject.getString("Year")
        val rated = jsonObject.getString("Rated")
        val released = jsonObject.getString("Released")
        val runTime = jsonObject.getString("Runtime")
        val genre = jsonObject.getString("Genre")
        val director = jsonObject.getString("Director")
        val writer = jsonObject.getString("Writer")
        val actors = jsonObject.getString("Actors")
        val plot = jsonObject.getString("Plot")
        userDao.insertMovies(User(title,year,rated,released,runTime,genre,director,writer, actors,plot))
        tvv.text = userDao.getAll().toString()
    }

}