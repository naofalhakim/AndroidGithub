package com.coba.cermatiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.coba.cermatiproject.model.Item
import com.coba.cermatiproject.model.User
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var numberOfList : MutableList<Item> = ArrayList()

    var page = 1
    var isLoading = false
    val TAG = "MainActivity"
    var islastPage = false
    var query : String = ""
    lateinit var mHandler : Handler

    lateinit var layoutManager: LinearLayoutManager
    lateinit var numberAdapter: NumberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler()
        numberAdapter = NumberAdapter(this,numberOfList)
        layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = numberAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){
                    var visibleItem = layoutManager.childCount //ini untuk no.1 childCount bisa menampilkan jumlah item yang sedang tampil sekarang
                    var topItemIndex = layoutManager.findFirstCompletelyVisibleItemPosition()
                    var recylerViewSize = numberAdapter.itemCount
                    if(!isLoading){
                        if((visibleItem+topItemIndex >= recylerViewSize) && !islastPage){
                            page++
                            getRequestData(query, page.toString())
                        }
                    }
                    if(islastPage){
                        Toast.makeText(applicationContext,"You are in last page",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                page = 1
                query = newText ?: ""
                if(newText.equals("")){
                    emptyView.visibility = View.VISIBLE
                    numberOfList.clear()
                }else{
                    mHandler.removeCallbacksAndMessages(null)
                    mHandler.postDelayed({
                        emptyView.visibility = View.INVISIBLE
                        loadingView.visibility = View.VISIBLE
                        NetworkConfig().getService()
                            .getUsers(page.toString(),newText.toString())
                            .enqueue(object : Callback<User>  {
                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    loadingView.visibility = View.INVISIBLE
                                    Toast.makeText(this@MainActivity, "Terjadi Kesalahan dengan  Koneksi anda", Toast.LENGTH_SHORT).show()
                                }
                                override fun onResponse(
                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    Log.e(TAG,response.toString())
                                    loadingView.visibility = View.INVISIBLE
                                    if(response.code() == 200){
                                        numberOfList.clear()
                                        response.body()?.items?.let {
                                            it.forEach {
                                                numberOfList.add((it))
                                            }
                                        }
                                    }
                                    numberAdapter.notifyDataSetChanged()
                                }})
                    },500)

                }
                return true

            }
        })
    }

    fun getRequestData(query:String,page: String){
        isLoading = true
        loadingView.visibility = View.VISIBLE
        NetworkConfig().getService()
            .getUsers(page, query)
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    Log.e(TAG,response.toString())

                    if(response.code() == 422 && query.equals("")){//422 for last page indicator message=Unprocessable Entity
                        islastPage = true
                    }
                    if(response.code() == 200 && !islastPage){
                        response.body()?.items?.let {
                            it.forEach {
                                numberOfList.add((it))
                            }
                        }
                        numberAdapter.notifyDataSetChanged()
                    }
                    isLoading = false
                    loadingView.visibility = View.GONE
                }})
    }
}
