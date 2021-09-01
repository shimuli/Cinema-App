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
import com.cedric.shimuli.mycinema.ui.home.MovieDetailsActivity
import com.cedric.shimuli.mycinema.utils.Utils
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MoviesAdapter(private val context: Context, movies: List<MoviesModel>) :  RecyclerView.Adapter<MoviesAdapter.ViewHolder>()  {
  private val movies:List<MoviesModel>

  init{
      this.movies = movies
  }

    interface ItemClickListener {
        fun onItemClick(pos: Int)
    }
     class ViewHolder internal  constructor(itemView:View):
     RecyclerView.ViewHolder(itemView), View.OnClickListener{
         val nameText: TextView
         val ratingText: TextView
         val languageText: TextView
         val durationText: TextView
         val genreText: TextView
         val timeText: TextView
         val moviePoster:ImageView
         private var itemClickListener: ItemClickListener? = null
         override fun onClick(view: View?) {
             itemClickListener!!.onItemClick(this.layoutPosition)
         }
         fun setItemClickListener(itemClickListener: ItemClickListener?) {
             this.itemClickListener = itemClickListener
         }

         init {
             nameText = itemView.findViewById(R.id.movieName)
             ratingText = itemView.findViewById(R.id.movieRating)
             durationText = itemView.findViewById(R.id.movieDuration)
             languageText = itemView.findViewById(R.id.movieLanguage)
             timeText = itemView.findViewById(R.id.airTime)
             genreText = itemView.findViewById(R.id.movieGenre)
             moviePoster = itemView.findViewById(R.id.movieBanner)

             itemView.setOnClickListener(this)
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.movie_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        val movieList: MoviesModel = movies[position]
        holder.nameText.text = movieList.name
        holder.ratingText.text = movieList.rating+ "/10"
       // holder.durationText.setText(movieList.playDate.toString().replace("T00:00:00", "")) // 2021-08-31T00:00:00  HH:mm
        holder.languageText.text = movieList.language
        holder.genreText.text = movieList.genre

        val date = movieList.playDate.toString().replace("T00:00:00", "")

        //holder.durationText.setText(formattedDate)

        val localDateTime: LocalDateTime = LocalDateTime.parse(movieList.playDate.toString())
        //DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy")
        val airDate = formatter.format(localDateTime)
        holder.durationText.text = airDate

        val localTime: LocalDateTime = LocalDateTime.parse(movieList.playTime.toString())
        //DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val airTime = timeFormatter.format(localTime)
        holder.timeText.text = airTime

        val url = movieList.imageurl

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.transformers)
            .error(R.drawable.transformers)
            .into(holder.moviePoster) // movie_holder

        holder.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                Utils.sendDataActivity(context, movieList, MovieDetailsActivity::class.java)

            }
        })
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}