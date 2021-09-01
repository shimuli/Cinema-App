package com.cedric.shimuli.mycinema.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.databinding.ActivityMovieDetailsBinding
import com.cedric.shimuli.mycinema.model.MoviesModel
import com.cedric.shimuli.mycinema.utils.Utils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMovieDetailsBinding
    private var clickedMovie: MoviesModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayMovies()
        lifecycle.addObserver(binding.trailerView);

        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        // showing the back button in action bar
        actionBar?.title = clickedMovie!!.name
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun displayMovies() {
        clickedMovie = Utils.fetchMovieDetails(intent, this@MovieDetailsActivity)
        if (clickedMovie != null) {
          //  Toast.makeText(this@MovieDetailsActivity, clickedMovie!!.trailer, Toast.LENGTH_LONG).show()
            binding.movieName.text = clickedMovie!!.name
            binding.movieGenre.text = clickedMovie!!.genre
            binding.movieRating.text = clickedMovie!!.rating.toString()
            binding.movieLanguage.text = clickedMovie!!.language
            binding.movieDuration.text = clickedMovie!!.duration

            binding.description.text = clickedMovie!!.description

            val amount = clickedMovie!!.ticketPrice?.let { Utils.convertCurrency(it) }
            binding.ticketPrice.text = amount.toString()

            val localDateTime: LocalDateTime = LocalDateTime.parse(clickedMovie!!.playDate.toString())
            val formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy")
            val airDate = formatter.format(localDateTime)
            binding.playDateTitle.text = airDate

            val localTime: LocalDateTime = LocalDateTime.parse(clickedMovie!!.playTime.toString())
            //DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val airTime = timeFormatter.format(localTime)
            binding.playTime.text = airTime

            val url = clickedMovie!!.imageurl

            Picasso.get()
                .load(url)
                .placeholder(R.drawable.transformers)
                .error(R.drawable.transformers)
                .into(binding.movieBanner) // movie_holder

            binding.playVideo.setOnClickListener(View.OnClickListener {
                binding.trailerView.visibility= View.VISIBLE
                binding.playVideo.visibility = View.INVISIBLE
                binding.trailerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = clickedMovie!!.trailer
                    if (videoId != null) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            })
            })
            binding.bookBtn.setOnClickListener(View.OnClickListener {
                Utils.sendDataActivity(this@MovieDetailsActivity, clickedMovie, BookMovieActivity::class.java)
            })
        }
    }

}