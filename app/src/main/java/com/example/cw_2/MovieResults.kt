package com.example.cw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_movie_results.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MovieResults : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_results)

        var MovieName = intent.getStringExtra("MovieName")
        val stb = StringBuilder()
        val stb1 = StringBuilder()
        var xx = 0

        for(i in 1..10){
            val url_string = "https://www.omdbapi.com/?s=$MovieName&apikey=c7e832d8&page=$i";
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
                        stb1.append(parseJSON(stb))
                        /*val jsonObject = JSONTokener(stb.toString()).nextValue() as JSONObject
                        ss = jsonObject.getString("totalResults")*/

                    }
                }
            }catch (e : Exception){

            }
            ++xx
        }

        textView1.text = stb1
        textView2.text = xx.toString()
    }

    suspend fun parseJSON(stb: java.lang.StringBuilder) : StringBuilder{
        // this contains the full JSON returned by the Web Service
        val json = JSONObject(stb.toString())
        // Information about all the movies extracted by this function
        var allMovies = java.lang.StringBuilder()
        var jsonArray: JSONArray = json.getJSONArray("Search")
        // extract all the movies from the JSON array
        for (i in 0 until jsonArray.length()) {
            val movie: JSONObject = jsonArray[i] as JSONObject // this is a json object
            // extract the title
            val title = movie["Title"] as String
            allMovies.append("* $title ")
            allMovies.append("\n\n")

        }
        return allMovies
    }
}