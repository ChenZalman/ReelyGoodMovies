package com.example.reelygoodmovies.ui.trailer_movie

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.databinding.TrailerLayoutBinding
import com.example.reelygoodmovies.ui.ActivityViewModel
import com.example.reelygoodmovies.ui.detail_movie.DetailedMovieViewModel
import com.example.reelygoodmovies.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailerFragment : Fragment() {
    private var _binding: TrailerLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModelTrailer: TrailerViewModel by viewModels()
    private val viewModel: DetailedMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrailerLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.getInt("id")?.let {
            viewModel.setId(it)
            viewModelTrailer.setId(it)
        }

        viewModelTrailer.trailer.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }

                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    val trailerKey = resource.status.data
                    if (trailerKey?.isNotEmpty() == true) {
                        openYouTube(trailerKey)
                    }
                }

                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    val errorMessage =
                        ErrorMessages.getErrorMessage(requireContext(), resource.status.message)
                    binding.tvError.text = errorMessage
                }
            }
        }

        viewModel.movie.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }

                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    val movie = resource.status.data
                    if (movie != null) {
                        binding.tvMovieTitle.text = movie.title
                        Glide.with(requireContext())
                            .load(movie.photo.takeIf { !it.isNullOrEmpty() }
                                ?: R.drawable.movie_picture)
                            .circleCrop()
                            .into(binding.ivMoviePic)
                    }
                }

                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    val errorMessage =
                        ErrorMessages.getErrorMessage(requireContext(), resource.status.message)
                    binding.tvError.text = errorMessage
                }

            }
        }
    }

    private fun openYouTube(videoId: String) {
        try {
            val youtubeUrl = "https://www.youtube.com/watch?v=$videoId"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val packageManager = requireContext().packageManager
            val resolveInfo =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

            if (resolveInfo.isNotEmpty()) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.open_youtube_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.error_opening_video, e.message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
