package com.example.cw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import com.example.cw_2.data.User
import com.example.cw_2.data.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchForMovies : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchformovies)
        val db = Room.databaseBuilder(this, UserDatabase::class.java,"MyDatabase").build()
        val userDao = db.userDao()
        val edit = findViewById<EditText>(R.id.edit)
        val retrieveBtn = findViewById<Button>(R.id.retrieve)
        val saveBtn = findViewById<Button>(R.id.save)

        var stb = StringBuilder()

        retrieveBtn.setOnClickListener{
            var editValue = edit.getText().toString()
            var url_string = "https://www.omdbapi.com/?t=$editValue&apikey=c7e832d8";
            val url = URL(url_string)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection

            runBlocking {
                launch {
                    // run the code of the coroutine in a new thread
                    stb.clear()
                    withContext(Dispatchers.IO) {
                        var bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                    }
                    parseJSON(stb)
                }
            }
        }
    }
    suspend fun parseJSON(stb: java.lang.StringBuilder) {
        val tv = findViewById<TextView>(R.id.tv)
        // this contains the full JSON returned by the Web Service
        //val json = JSONObject(stb.toString())
        val jsonObject = JSONTokener(stb.toString()).nextValue() as JSONObject
        var movieDetails = java.lang.StringBuilder()
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
        tv.setText(movieDetails)

    }
}