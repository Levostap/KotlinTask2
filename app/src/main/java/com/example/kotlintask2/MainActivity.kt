package com.example.kotlintask2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

//https://www.waifu.im/search/?order_by=FAVORITES&is_nsfw=FALSE
//https://nekos.best/api/v2/neko


class MainActivity : AppCompatActivity() {

    private lateinit var randomNekos : Button
    private lateinit var searchNekos : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        randomNekos  = findViewById(R.id.nekoButton)
        searchNekos = findViewById(R.id.searchButton)
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


    companion object{
        const val RANDOM_FRAGMENT = "random"
        const val SEARCH_FRAGMENT = "search"
    }

}