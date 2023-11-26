package com.example.kotlintask2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Exception
import kotlin.random.Random

//https://www.waifu.im/search/?order_by=FAVORITES&is_nsfw=FALSE
//https://nekos.best/api/v2/neko
interface Api {
    @GET("{tag}")
    suspend fun getPicRandom(
        @Path("tag") tag : String, @Query("amount") amount : Int
    ) : NekoResponse
    @GET("search")
    suspend fun searchPic(
        @Query("query") query : String, @Query("type") type : Int, @Query("amount") amount : Int
    ) : NekoResponse
}

data class NekoResponse(
    val results: List<NekoItem>
)

data class NekoItem(
    val artistHref: String,
    val artistName: String,
    val sourceUrl: String,
    val url: String
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val randomNekos : Button = findViewById(R.id.nekoButton)
        val searchNekos : Button = findViewById(R.id.searchButton)
        randomNekos.setOnClickListener { checkFragment(RANDOM_FRAGMENT) }
        searchNekos.setOnClickListener { checkFragment(SEARCH_FRAGMENT) }
    }

    fun checkFragment(tag : String){
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)
        val type : Fragment = when(tag){
            RANDOM_FRAGMENT -> RandomFragment()
            SEARCH_FRAGMENT -> SearchFragment()
            else -> RandomFragment()
        }

        if(existingFragment == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, type, tag)
                addToBackStack(tag)
                commit()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, existingFragment)
                commit()

            }
        }
    }

    fun createApi(): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(Api::class.java)
    }


    companion object{
        const val TAG = "neko"
        const val API_URL = "https://nekos.best/api/v2/"
        const val ITEMS_PER_PAGE = 14
        const val RANDOM_FRAGMENT = "random"
        const val SEARCH_FRAGMENT = "search"
    }

}