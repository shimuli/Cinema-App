package com.cedric.shimuli.mycinema.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.cedric.shimuli.mycinema.adapter.MoviesAdapter
import com.cedric.shimuli.mycinema.databinding.FragmentHomeBinding
import com.cedric.shimuli.mycinema.databinding.FragmentProfileBinding
import com.cedric.shimuli.mycinema.model.MoviesModel
import com.cedric.shimuli.mycinema.network.RestCall
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private  var binding: FragmentHomeBinding? = null
    private val _binding get() = binding
    private var movieAdapter:MoviesAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    var movieList: MutableList<MoviesModel> = ArrayList<MoviesModel>()

    private val a: HomeFragment =this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initializeViews()
        setupRecyclerView()
        getMovies()

        binding!!.searchBtn.setOnClickListener(View.OnClickListener {
           when{
               binding!!.searchQuery.query.isNullOrEmpty() -> {
                   Toast.makeText(activity, "Enter movie name to search", Toast.LENGTH_LONG).show()
               }
               else->{
                   val searchText:String = binding!!.searchQuery.query.toString()
                   Toast.makeText(activity, searchText, Toast.LENGTH_LONG).show()
               }
           }
        })

        binding!!.searchQuery.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                when {
                    binding!!.searchQuery.query.isNullOrEmpty() -> {
                        movieList.clear()
                        getMovies()
                    }
                    else -> {
                        val searchText: String = binding!!.searchQuery.query.toString()
                        Toast.makeText(activity, searchText, Toast.LENGTH_LONG).show()
                        movieList.clear()
                        searchMovies(searchText)
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })


        binding!!.fab.setOnClickListener(View.OnClickListener {
            movieList.clear()
            getMovies()

        })
        binding!!.refreshBtn.setOnClickListener(View.OnClickListener {
            getMovies()
        })

        binding!!.sortAscending.setOnClickListener(View.OnClickListener {

                binding!!.sortDescending.visibility =View.VISIBLE
                binding!!.sortAscending.visibility =View.GONE

        })

        binding!!.sortDescending.setOnClickListener(View.OnClickListener {

            binding!!.sortDescending.visibility =View.GONE
            binding!!.sortAscending.visibility =View.VISIBLE

        })
        return binding!!.root
    }



    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(activity)
        movieAdapter = activity?.let { MoviesAdapter(it, movieList) }
        binding!!.recyclerview.adapter = movieAdapter
        binding!!.recyclerview.layoutManager = layoutManager
    }

    private fun initializeViews() {
        binding!!.pb.isIndeterminate = true
        binding!!.pb.visibility= View.VISIBLE
    }

    private fun getMovies() {
        binding!!.noConnection.visibility = View.INVISIBLE
        binding!!.fab.visibility = View.VISIBLE
        binding!!.pb.isIndeterminate = true
        binding!!.pb.visibility= View.VISIBLE
        val token = Paper.book().read<String>("token")
        val myMovies = RestCall.client.fetchMovies(token)
        myMovies.enqueue(object: Callback<List<MoviesModel>>{
            override fun onResponse(
                call: Call<List<MoviesModel>>,
                response: Response<List<MoviesModel>>) {
                if(response.code()==200){
                    binding!!.pb.isIndeterminate = false
                    binding!!.pb.visibility= View.INVISIBLE
                    val allMovies = response.body()
                    allMovies?.let { movieList.addAll(it) }
                    movieAdapter!!.notifyDataSetChanged()
                }
                else{
                    binding!!.pb.isIndeterminate = false
                    binding!!.pb.visibility= View.INVISIBLE
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<MoviesModel>>, t: Throwable) {
                binding!!.pb.isIndeterminate = false
                binding!!.pb.visibility= View.INVISIBLE
                Log.e("RETROFIT", "ERROR: " + t.message)
                binding!!.noConnection.visibility = View.VISIBLE
                binding!!.fab.visibility = View.INVISIBLE
                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun searchMovies(searchText: String) {
        binding!!.noConnection.visibility = View.INVISIBLE
        binding!!.fab.visibility = View.VISIBLE
        binding!!.pb.isIndeterminate = true
        binding!!.pb.visibility= View.VISIBLE
        val token = Paper.book().read<String>("token")
        val myMovies = RestCall.client.searchMovie(token,searchText)
        myMovies.enqueue(object: Callback<List<MoviesModel>>{
            override fun onResponse(
                call: Call<List<MoviesModel>>,
                response: Response<List<MoviesModel>>) {
                if(response.code()==200){

                    binding!!.pb.isIndeterminate = false
                    binding!!.pb.visibility= View.INVISIBLE
                    val allMovies = response.body()
                    if(allMovies!!.isEmpty()){
                        Toast.makeText(activity, "No movie found", Toast.LENGTH_LONG).show()
                    }
                    else{
                        allMovies?.let { movieList.addAll(it) }
                        movieAdapter!!.notifyDataSetChanged()
                    }

                }
                else{
                    binding!!.pb.isIndeterminate = false
                    binding!!.pb.visibility= View.INVISIBLE
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<MoviesModel>>, t: Throwable) {
                binding!!.pb.isIndeterminate = false
                binding!!.pb.visibility= View.INVISIBLE
                Log.e("RETROFIT", "ERROR: " + t.message)
                binding!!.noConnection.visibility = View.VISIBLE
                binding!!.fab.visibility = View.INVISIBLE
                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }



    override fun onDestroyView() {
        binding = null
        super.onDestroyView()

    }
}

private fun <T> Call<T>?.enqueue(callback: Callback<List<MoviesModel?>>) {

}
