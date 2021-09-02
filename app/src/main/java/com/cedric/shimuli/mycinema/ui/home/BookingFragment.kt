package com.cedric.shimuli.mycinema.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.adapter.BookedMoviesAdapter
import com.cedric.shimuli.mycinema.databinding.FragmentBookingBinding
import com.cedric.shimuli.mycinema.databinding.FragmentProfileBinding
import com.cedric.shimuli.mycinema.model.UserBookedMovies
import com.cedric.shimuli.mycinema.network.RestCall
import com.cedric.shimuli.mycinema.ui.auth.LoginActivity
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingFragment : Fragment() {

    private  var binding: FragmentBookingBinding? = null
    private val _binding get() = binding
    private var movieAdapter:BookedMoviesAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    var movieList: MutableList<UserBookedMovies> = ArrayList<UserBookedMovies>()
    private val a: BookingFragment =this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(activity)
        setHasOptionsMenu(true)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        initializeViews()
        setupRecyclerView()
        val watched:Boolean = false
        getMovies(watched)


        return binding!!.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.watched -> {
                val watched:Boolean= true
                if(movieList !=null){
                    movieList.clear()
                    getMovies(watched)
                }
                else{
                    getMovies(watched)
                }
                true
            }

            R.id.notWatched -> {
                val watched:Boolean= false
                if(movieList !=null){
                    movieList.clear()
                    getMovies(watched)
                }
                else{
                    getMovies(watched)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMovies(watched: Boolean) {
        binding!!.noConnection.visibility = View.INVISIBLE
        binding!!.pb.isIndeterminate = true
        binding!!.pb.visibility= View.VISIBLE

        val token = Paper.book().read<String>("token")
        val userId = Paper.book().read<String>("userId")
        val movies = RestCall.client.fetchBookings(token,userId, watched)
        movies?.enqueue(object: Callback<List<UserBookedMovies>>{
            override fun onResponse(
                call: Call<List<UserBookedMovies>>,
                response: Response<List<UserBookedMovies>>) {
                if(response.code()==200){
                    val size = response.body()?.size
                    if(size!! >0){
                        binding!!.pb.isIndeterminate = false
                        binding!!.pb.visibility= View.INVISIBLE
                        binding!!.recyclerview.visibility = View.VISIBLE
                        binding!!.noConnection.visibility = View.INVISIBLE
                        val allMovies = response.body()
                        allMovies?.let { movieList.addAll(it) }
                        movieAdapter!!.notifyDataSetChanged()
                    }
                    else if(size <=0){
                        binding!!.noDataImage.setImageResource(R.drawable.ic_search_magnifier_with_a_cross)
                        binding!!.noDataTitle.setText("No movies tickets availabe!")
                        binding!!.noDataText.setText("You will see all your Movie tickets here")
                        binding!!.refreshBtn.visibility = View.INVISIBLE
                        binding!!.pb.isIndeterminate = false
                        binding!!.pb.visibility= View.INVISIBLE
                        binding!!.recyclerview.visibility = View.INVISIBLE
                        binding!!.noConnection.visibility = View.VISIBLE
                    }

                }
                else{
                    binding!!.pb.isIndeterminate = false
                    binding!!.pb.visibility= View.INVISIBLE
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<UserBookedMovies>>, t: Throwable) {
                binding!!.pb.isIndeterminate = false
                binding!!.pb.visibility= View.INVISIBLE
                Log.e("RETROFIT", "ERROR: " + t.message)
                binding!!.noConnection.visibility = View.VISIBLE
                binding!!.recyclerview.visibility = View.INVISIBLE

                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(activity)
        movieAdapter = activity?.let { BookedMoviesAdapter(it, movieList) }
        binding!!.recyclerview.adapter = movieAdapter
        binding!!.recyclerview.layoutManager = layoutManager


    }

    private fun initializeViews() {
        binding!!.pb.isIndeterminate = true
        binding!!.pb.visibility= View.VISIBLE
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()

    }

}