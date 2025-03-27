package com.example.reelygoodmovies.ui.filter_genre_movies

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.databinding.ItemGenreLayoutBinding

class GenreItemAdapter(
    private var items: List<Movie>,
    private val callBack: ItemListener
) : RecyclerView.Adapter<GenreItemAdapter.ItemViewHolder>() {


    interface ItemListener {
        fun onItemClicked(index: Int)
        fun onItemLongClicked(index: Int)
    }

    inner class ItemViewHolder(private val binding: ItemGenreLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener, OnLongClickListener {
        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            callBack.onItemClicked(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return true
        }

        fun bind(movie: Movie) {
            binding.tvItemMovieTitle.text = movie.title

            Glide.with(binding.root)
                .load(movie.photo.takeIf { !it.isNullOrEmpty() } ?: R.drawable.movie_picture)
                .circleCrop()
                .into(binding.ivItemMovieImage)

            binding.rbItemMovieRating.rating = movie.rate

    }
        }

    fun updateMovies(newMovies: List<Movie>) {
        this.items = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemGenreLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}





