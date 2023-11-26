package com.example.kotlintask2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.http.Query
import java.lang.Exception

class SearchFragment : Fragment(R.layout.search_fragment) {

    private val adapter = AnimeAdapter(mutableListOf(), this)
    private lateinit var recyclerView : RecyclerView
    private var isLoading = false
    private var isStarted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val search : Button = view.findViewById(R.id.search_button)
        val query : EditText = view.findViewById(R.id.search_bar)
        recyclerView =  view.findViewById(R.id.anime_recycle_Search)
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        search.setOnClickListener {
            if(query.text.isNotEmpty() && query.text.isNotBlank()){
                if(isStarted){
                    adapter.clearData()
                }
                    load(query.text.toString())
            }
        }
    }

    fun load(query : String){
        isLoading = true
        lifecycleScope.launch {
            val api = MainActivity().createApi()
            try{
                val response = api.searchPic(query, 1, MainActivity.ITEMS_PER_PAGE)
                for(i in 0 until response.results.size){
                    adapter.addData(response.results[i].url)
                }
                isStarted = true
            } catch (e : Exception){
                Log.d("Exception", e.toString())
            }finally {
                isLoading = false
            }
        }
    }

}