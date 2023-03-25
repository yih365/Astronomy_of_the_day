package com.example.codepathand101project5

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var reloadButton: Button
    private lateinit var dateText: TextView
    private lateinit var imageView: ImageView
    private lateinit var descText: TextView

    var date = ""
    var description = ""
    var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reloadButton = findViewById(R.id.reloadButton)
        dateText = findViewById(R.id.dateText)
        imageView = findViewById(R.id.imageView)
        descText = findViewById(R.id.descText)

        reloadButton.setOnClickListener{
            getNextImage()
        }
        getNextImage()
    }

    private fun getNextImage() {
        val params = RequestParams()
        val year = (1996..2022).random()
        val month = (1..12).random()
        val day = (1..28).random()
        params["date"] = "$year-$month-$day"
        getData(params)
    }

    private fun loadViews() {
        dateText.text = date
        descText.text = description
        Glide.with(this)
            . load(imageUrl)
            .fitCenter()
            .into(imageView)
    }

    private fun getData(params: RequestParams) {
        val client = AsyncHttpClient()
        client["https://api.nasa.gov/planetary/apod?api_key=${getString(R.string.nasaApiKey)}", params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Image", "response successful$json")
                val resultsJSON = json.jsonObject
                date = resultsJSON.getString("date")
                description = resultsJSON.getString("explanation")
                imageUrl = resultsJSON.getString("url")
                loadViews()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Image Error", errorResponse)
            }
        }]
    }
}