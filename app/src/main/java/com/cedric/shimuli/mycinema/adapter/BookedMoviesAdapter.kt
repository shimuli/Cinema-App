package com.cedric.shimuli.mycinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.model.MoviesModel
import com.cedric.shimuli.mycinema.model.UserBookedMovies
import com.cedric.shimuli.mycinema.ui.home.MovieDetailsActivity
import com.cedric.shimuli.mycinema.utils.Utils
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookedMoviesAdapter(private val context: Context, movies: List<UserBookedMovies>) :  RecyclerView.Adapter<BookedMoviesAdapter.ViewHolder>()  {
  private val movies:List<UserBookedMovies>

  init{
      this.movies = movies
  }

     class ViewHolder internal  constructor(itemView:View):
     RecyclerView.ViewHolder(itemView), View.OnClickListener{
         val nameText: TextView
         val quantityText: TextView
         val priceText: TextView
         val dateText: TextView
         val userText: TextView
         val timeText: TextView
         val moviePoster:ImageView


         init {
             nameText = itemView.findViewById(R.id.movieName)
             quantityText = itemView.findViewById(R.id.MovieQuantity)
             priceText = itemView.findViewById(R.id.MoviePrice)
             dateText = itemView.findViewById(R.id.MovieDate)
             timeText = itemView.findViewById(R.id.MovieTime)
             userText = itemView.findViewById(R.id.MovieUser)
             moviePoster = itemView.findViewById(R.id.movieBanner)

             itemView.setOnClickListener(this)
         }

         override fun onClick(p0: View?) {
            return
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedMoviesAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.booked_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookedMoviesAdapter.ViewHolder, position: Int) {
        val movieList: UserBookedMovies = movies[position]
        holder.nameText.text = movieList.movieName
        holder.quantityText.text = movieList.quantity.toString()
        holder.userText.text = movieList.cusomerName


        val localDateTime: LocalDateTime = LocalDateTime.parse(movieList.playingDate.toString())
        val formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy")
        val airDate = formatter.format(localDateTime)
        holder.dateText.text = airDate

        val localTime: LocalDateTime = LocalDateTime.parse(movieList.playTime.toString())
        //DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val airTime = timeFormatter.format(localTime)
        holder.timeText.text = airTime

        holder.priceText.text = movieList.price.toString()

        val url = movieList.movieImage

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.transformers)
            .error(R.drawable.transformers)
            .into(holder.moviePoster) // movie_holder
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}