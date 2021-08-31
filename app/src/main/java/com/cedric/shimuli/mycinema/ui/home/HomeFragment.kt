package com.cedric.shimuli.mycinema.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.adapter.MoviesAdapter
import com.cedric.shimuli.mycinema.databinding.FragmentHomeBinding
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
    var pageNumber:Int = 1
    var pageSize:Int= 5
    private var isScrolling = false
    private var currentScientists = 0
    private var totalScientists = 0
    private var scrolledOutScientists = 0
    var newpage:Int = 1

    private val a: HomeFragment =this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initializeViews()
        setupRecyclerView()
        getMovies(pageNumber ,pageSize, "desc")
        listenToRecyclerViewScroll()

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
                        getMovies(pageNumber ,pageSize, "desc")
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
            getMovies(pageNumber ,pageSize, "desc")

        })
        binding!!.refreshBtn.setOnClickListener(View.OnClickListener {
            getMovies(pageNumber ,pageSize, "desc")
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

    private fun listenToRecyclerViewScroll() {
        binding!!.recyclerview.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScientists = layoutManager!!.childCount
                totalScientists = layoutManager!!.itemCount
                scrolledOutScientists = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()

                if (isScrolling && (totalScientists > currentScientists)) {
                    isScrolling=false
                    if(dy>0){
                        newpage += 1
                        getMovies(newpage ,pageSize, "desc")
                       // Toast.makeText(activity, "total= $totalScientists and  current= $currentScientists and newpage = $newpage and scrolledOutScientists = $scrolledOutScientists", Toast.LENGTH_LONG).show()
                    }
                    else if (dy < 0) {
                        //newpage -= 1
                        //getMovies(newpage ,pageSize, "desc")
                       // Toast.makeText(activity, "totalScientists= $totalScientists and  currentScientists= $currentScientists and newpage = $newpage and scrolledOutScientists = $scrolledOutScientists", Toast.LENGTH_LONG).show()
                    }

                }
                if (isScrolling && currentScientists+scrolledOutScientists== totalScientists){
                    isScrolling=false

                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_menu, menu)
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

    private fun getMovies(pageNumber:Int ,pageSize:Int, sort: String) {
        binding!!.noConnection.visibility = View.INVISIBLE
        binding!!.pb.isIndeterminate = true
        binding!!.pb.visibility= View.VISIBLE
        val token = Paper.book().read<String>("token")
        val myMovies = RestCall.client.fetchMovies(token,pageNumber, pageSize, sort )
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
                binding!!.recyclerview.visibility = View.INVISIBLE

                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun searchMovies(searchText: String) {
        binding!!.noConnection.visibility = View.INVISIBLE
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
                binding!!.recyclerview.visibility = View.INVISIBLE
                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun todayMovies() {
        binding!!.noConnection.visibility = View.INVISIBLE
        binding!!.pb.isIndeterminate = true
        binding!!.pb.visibility= View.VISIBLE
        val token = Paper.book().read<String>("token")
        val myMovies = RestCall.client.todayMovies(token)
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
                binding!!.recyclerview.visibility = View.INVISIBLE

                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.highestRated -> {
                newpage=1
                val desc:String= "desc"
                if(movieList !=null){
                    movieList.clear()
                    pageNumber=1
                    getMovies(newpage ,pageSize, desc)

                }
                else{
                    getMovies(newpage ,pageSize, desc)

                }
                true
            }
            R.id.lowestRated -> {
                val asc:String= "asc"
                newpage=1
                if(movieList !=null){
                    movieList.clear()
                    getMovies(newpage ,pageSize, asc)
                }
                else{
                    getMovies(newpage ,pageSize, asc)
                }
                true
            }
            R.id.todayMovies -> {
                if(movieList !=null){
                    movieList.clear()
                    todayMovies()
                }
                else{
                    todayMovies()
                }
                true
            }
            R.id.refresh -> {
                newpage=1
                if(movieList !=null){
                    movieList.clear()
                    getMovies(newpage ,pageSize, "desc")
                }
                else{
                    getMovies(newpage ,pageSize, "desc")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()

    }
}

private fun <T> Call<T>?.enqueue(callback: Callback<List<MoviesModel?>>) {

}
