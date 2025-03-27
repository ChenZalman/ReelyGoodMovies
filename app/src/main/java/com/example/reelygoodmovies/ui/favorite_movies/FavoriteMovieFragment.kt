package com.example.reelygoodmovies.ui.favorite_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.databinding.FavoriteLayoutBinding
import com.example.reelygoodmovies.ui.ActivityViewModel
import com.example.reelygoodmovies.ui.add_edit_movie.EditViewModel
import com.example.reelygoodmovies.ui.all_movies.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment() {
    private var _binding: FavoriteLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActivityViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter
    private val editViewModel: EditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemAdapter(emptyList(), object : ItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                val movie = adapter.getItem(index)
                findNavController().navigate(
                    R.id.action_favoriteMovieFragment_to_detailedItemFragment,
                    bundleOf("id" to movie.id)
                )
            }

            override fun onItemLongClicked(index: Int) {
                val movie = adapter.getItem(index)
                Toast.makeText(requireContext(), movie.title, Toast.LENGTH_SHORT).show()
            }

            override fun onEditButtonClick(index: Int) {
                val movie = adapter.getItem(index)
                viewModel.setMovie(movie)
                editViewModel.setEditMode(true)
                findNavController().navigate(R.id.action_favoriteMovieFragment_to_addOrEditItemFragment)
            }

            override fun onFavButtonClick(index: Int) {
                val movie = adapter.getItem(index)
                movie.favorite = !movie.favorite
                viewModel.updateMovie(movie)
                adapter.notifyItemChanged(index)
            }
        })

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewModel.favoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
            adapter.updateMovies(favoriteMovies)
            if (favoriteMovies.isEmpty()) {
                binding.tvNoMoviesFound.text = getString(R.string.no_movies_found)
                binding.recycler.visibility = View.GONE
            } else {
                binding.tvNoMoviesFound.text = ""
                binding.recycler.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
