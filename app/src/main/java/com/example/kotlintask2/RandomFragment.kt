package com.example.kotlintask2

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList

class RandomFragment : Fragment(R.layout.random_fragment), FragmentCallback {

    private val adapter = AnimeAdapter(mutableListOf(), this)
    private lateinit var retryButton : Button
    private lateinit var recyclerView : RecyclerView
    private lateinit var krutilka : ProgressBar
    private lateinit var plashka : LinearLayout
    private var isStarted = false
    private var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retryButton = view.findViewById(R.id.retry_load)
        recyclerView = view.findViewById(R.id.anime_recycle)
        krutilka = view.findViewById(R.id.krutilka)
        plashka = krutilka.parent as LinearLayout
        val gridSize = if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) LANDSCAPE_SIZE else VERTICAL_SIZE
        val layoutManager = GridLayoutManager(activity, gridSize)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        retryButton.setOnClickListener {
            load()
            it.visibility = View.INVISIBLE
            plashka.visibility = View.GONE
        }

        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >=
                    layoutManager.itemCount && layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.childCount > 0
                    && !isLoading) {
                    load()
                }
            }
        })
        if(savedInstanceState != null) {
            val elements = savedInstanceState.getStringArrayList(SAVED_DATA)
            if (elements != null) {
                for (i in 0 until elements.size){
                    adapter.addData(elements[i])
                }
                isStarted = true
            } else load()
        }else load()
    }

    fun load(){
        isLoading = true
        lifecycleScope.launch {
            krutilka.visibility = View.VISIBLE
            plashka.visibility = View.VISIBLE
            retryButton.visibility = View.INVISIBLE
            val api = ApiWorker().createApi()
            try{
                val response = api.getPicRandom(ApiWorker.TAG, ApiWorker.ITEMS_PER_PAGE)
                for(i in 0 until response.results.size){
                    adapter.addData(response.results[i].url)
                }
                isStarted = true
                plashka.visibility = View.GONE
            } catch (e : Exception){
                if (!isStarted){
                    plashka.visibility = View.VISIBLE
                    retryButton.visibility = View.VISIBLE
                }
            }finally {
                isLoading = false
                krutilka.visibility = View.INVISIBLE
            }
        }
    }

    override fun getFragment() : Context {
        return requireContext()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(SAVED_DATA, adapter.getUrls() as ArrayList<String>)
        adapter.clearData()
    }
    companion object{
        const val SAVED_DATA = "urls"
        const val LANDSCAPE_SIZE = 4
        const val VERTICAL_SIZE = 2
    }
}