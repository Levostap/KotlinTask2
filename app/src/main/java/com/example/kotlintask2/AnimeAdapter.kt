package com.example.kotlintask2


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AnimeAdapter(private val data : MutableList<String>, private val callback : FragmentCallback) : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    private val urls = mutableListOf<String>()
    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val img : ImageView = item.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_frame, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = callback.getFragment()
        urls.add(data[position])
        Glide.with(context)
            .load(data[position])
            .centerCrop()
            .into(holder.img)

        //Picasso.get()
        //    .load(url)
        //    .fit()
        //    .centerInside()
        //    .into(holder.img)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getUrls() : List<String>{
        return urls
    }

    fun addData(newData: String) {
        data.add(data.size, newData)
        //notifyDataSetChanged()
        notifyItemInserted(data.size)
    }

    fun clearData(){
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
    }

}