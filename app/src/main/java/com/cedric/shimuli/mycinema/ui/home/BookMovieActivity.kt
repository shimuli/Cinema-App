package com.cedric.shimuli.mycinema.ui.home

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.databinding.ActivityBookMovieBinding
import com.cedric.shimuli.mycinema.databinding.ActivityMovieDetailsBinding
import com.cedric.shimuli.mycinema.model.BookingResponse
import com.cedric.shimuli.mycinema.model.MoviesModel
import com.cedric.shimuli.mycinema.network.RestCall
import com.cedric.shimuli.mycinema.ui.auth.LoginActivity
import com.cedric.shimuli.mycinema.utils.Utils
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import java.text.SimpleDateFormat
import java.util.*

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
            val total = (amount?.times(counter))?.toDouble()

            val totalCost = Utils.convertCurrency(total)
            val movieAmount = Utils.convertCurrency(amount?.toDouble())
            binding.totalCost.setText(totalCost.toString()+" @"+movieAmount+" per ticket")
            //Toast.makeText(this@BookMovieActivity, total.toString(), Toast.LENGTH_LONG).show()
        })

        binding.totalCostLabel.setOnClickListener(View.OnClickListener {
            val amount = bookMovie!!.ticketPrice?.toInt()
            val total = (amount?.times(counter))?.toDouble()
            val totalCost = Utils.convertCurrency(total)
            binding.totalCost.setText(totalCost.toString()+" @"+amount+" per ticket")
            Toast.makeText(this@BookMovieActivity, total.toString(), Toast.LENGTH_LONG).show()
        })

        binding.payBtn.setOnClickListener(View.OnClickListener {
            when {
                binding.phone.text.isNullOrEmpty() -> {
                    binding.phone.error = "Phone number is required please!"
                }
                binding.phone.text!!.length<10 -> {
                    binding.phone.error = "Invalid phone number!"
                }
                else->{
                    bookTicket()
                }
            }
        })

    }

    private fun bookTicket() {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())

        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Booking..."
        pDialog.setCancelable(false)
        pDialog.show()
        val phone = Paper.book().read<String>("userPhone")
        val token = Paper.book().read<String>("token")
        val userId = Paper.book().read<String>("userId").toInt()
        val movieId = bookMovie!!.id
        val booking = movieId?.let { RestCall.client.bookMovie(token, counter,phone, it, userId,currentDate) }
        booking?.enqueue(object :Callback<BookingResponse?>{
            override fun onResponse(
                call: Call<BookingResponse?>,
                response: Response<BookingResponse?>) {
                if (response.code() == 201){
                    pDialog.dismiss()
                    SweetAlertDialog(this@BookMovieActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Booking Successful!")
                        .setContentText("Ticket was booked successfully, you will receive an SMS shortly")
                        .setConfirmText("Okay")
                        .setConfirmButtonBackgroundColor(Color.GREEN)
                        .setConfirmButtonTextColor(Color.BLACK)
                        .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }

                        .show()
                      //Toast.makeText(this@BookMovieActivity, "Booking was successful", Toast.LENGTH_LONG).show()
                }
                else{
                    pDialog.dismiss()
                    Toast.makeText(this@BookMovieActivity, "Faailed ", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<BookingResponse?>, t: Throwable) {
                pDialog.dismiss()
                Toast.makeText(this@BookMovieActivity, "Wacha mchezo ", Toast.LENGTH_LONG).show()
            }
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