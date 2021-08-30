package com.cedric.shimuli.mycinema.adapter

import android.content.Context
import android.icu.number.NumberFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.bumptech.glide.request.RequestOptions
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.model.MoviesModel
import com.squareup.picasso.Picasso

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
        holder.nameText.setText(movieList.name)
        holder.ratingText.setText(movieList.rating+ "/10")
        holder.durationText.setText(movieList.duration)
        holder.languageText.setText(movieList.language)
        holder.genreText.setText(movieList.genre)

        val url = movieList.imageurl

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.transformers)
            .error(R.drawable.transformers)
            .into(holder.moviePoster) // movie_holder

        holder.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(pos: Int) {
//                Utils.sendScientistToActivity(
//                    context, scientist,
//                    DetailActivity::class.java
//                )
                Toast.makeText(context, movieList.name, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}