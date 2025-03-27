package com.example.reelygoodmovies.ui.all_movies

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.databinding.ItemLayoutBinding


class ItemAdapter(private var items: List<Movie>, val callBack: ItemListener) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemClicked(index: Int)
        fun onItemLongClicked(index: Int)
        fun onEditButtonClick(index: Int)
        fun onFavButtonClick(index: Int)
    }

    inner class ItemViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener, OnLongClickListener {
        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
            binding.ibItemEdit.setOnClickListener {
                callBack.onEditButtonClick(adapterPosition)
            }
            binding.ibItemFavorite.setOnClickListener {
                callBack.onFavButtonClick(adapterPosition)
            }
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

            binding.ibItemEdit.isVisible = movie.localGen
            Glide.with(binding.root)
                .load(movie.photo.takeIf { !it.isNullOrEmpty() } ?: R.drawable.movie_picture)
                .circleCrop()
                .into(binding.ivItemMovieImage)

            binding.rbItemMovieRating.rating = movie.rate.toFloat()

            val genreIds = movie.genre

            val genreNames = genreIds.joinToString(", ") { genreId ->
                binding.root.context.getString(genreId)
            }
            binding.tvItemMovieGenre.text = genreNames

            binding.ibItemFavorite.setImageResource(if (movie.favorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24)
        }
    }

    fun updateMovies(newMovies: List<Movie>) {
        this.items = newMovies
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemLayoutBinding.inflate(
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