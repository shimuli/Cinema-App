package com.cedric.shimuli.mycinema.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.databinding.ActivityBookMovieBinding
import com.cedric.shimuli.mycinema.databinding.ActivityMovieDetailsBinding
import com.cedric.shimuli.mycinema.model.MoviesModel
import com.cedric.shimuli.mycinema.utils.Utils
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class BookMovieActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBookMovieBinding
    private var bookMovie: MoviesModel? = null
    private var isEnable:Boolean = false
    var counter:Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayMovies()

        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_booking);
        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        //actionBar?.setTitle(bookMovie!!.name)
        counterMethod()

        val phone = Paper.book().read<String>("userPhone")
        binding.phone.setText(phone)
    }

    private fun counterMethod() {

        binding.addBtn.setOnClickListener(View.OnClickListener {
            counter++
            binding.ticketCounter.text = counter.toString()

            if (!isEnable){
                binding.minusBtn.isClickable = true
            }
        })
        binding.minusBtn.setOnClickListener(View.OnClickListener {
            counter--
            binding.ticketCounter.text = counter.toString()
            if (counter ==1){
                isEnable=false
                binding.minusBtn.isClickable = false
            }
            else if(counter >=1) {
                isEnable=true

                binding.minusBtn.isClickable = true
            }

        })
        binding.totalCost.setOnClickListener(View.OnClickListener {
            val amount = bookMovie!!.ticketPrice?.toInt()
            val total = amount?.times(counter)

            binding.totalCost.setText("Ksh."+ total.toString()+" @"+amount+" per ticket")
            Toast.makeText(this@BookMovieActivity, total.toString(), Toast.LENGTH_LONG).show()
        })

        binding.totalCostLabel.setOnClickListener(View.OnClickListener {
            val amount = bookMovie!!.ticketPrice?.toInt()
            val total = (amount?.times(counter))?.toDouble()

            val totalCost = Utils.convertCurrency(total)

            binding.totalCost.setText(totalCost.toString()+" @"+amount+" per ticket")
            Toast.makeText(this@BookMovieActivity, total.toString(), Toast.LENGTH_LONG).show()
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun displayMovies() {
        bookMovie = Utils.fetchMovieDetails(intent, this@BookMovieActivity)
        if (bookMovie != null) {
            binding.movieName.text = bookMovie!!.name
            binding.movieGenre.text = bookMovie!!.genre
            binding.movieRating.text = bookMovie!!.rating.toString()
            binding.movieLanguage.text = bookMovie!!.language
            binding.movieDuration.text = bookMovie!!.duration


            val url = bookMovie!!.imageurl
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.transformers)
                .error(R.drawable.transformers)
                .into(binding.movieBanner) // movie_holder

//            binding.ticketPrice.setText("Ksh. "+bookMovie!!.ticketPrice.toString())
//            binding.description.setText(bookMovie!!.description)
        }
    }

}