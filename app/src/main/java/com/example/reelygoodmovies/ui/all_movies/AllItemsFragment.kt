package com.example.reelygoodmovies.ui.all_movies

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.databinding.AllItemsLayoutBinding
import com.example.reelygoodmovies.ui.ActivityViewModel
import com.example.reelygoodmovies.utils.Error
import com.example.reelygoodmovies.utils.Loading
import com.example.reelygoodmovies.utils.Success
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.reelygoodmovies.ui.add_edit_movie.EditViewModel
import com.example.reelygoodmovies.utils.ErrorMessages


@AndroidEntryPoint
class AllItemsFragment : Fragment() {
    private var _binding: AllItemsLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActivityViewModel by activityViewModels()
    private val editViewModel: EditViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter
    private var currentMovie: Movie? = null

    private val recognizeSpeechLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let { it1 ->
                    viewModel.setRecognition(
                        it1
                            .joinToString(" ")
                    )
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                currentMovie?.let {
                    showContactsDialog(requireContext(), it)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun requestContactPermission(movie: Movie) {
        currentMovie = movie

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentMovie?.let { showContactsDialog(requireContext(), it) }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllItemsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initializeSearch()

        viewModel.movies.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                is Loading -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                }

                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    val errorMessage =
                        ErrorMessages.getErrorMessage(requireContext(), result.status.message)
                    binding.tvNoMoviesFound.text = errorMessage
                }

                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    val movieList = result.status.data
                    if (!movieList.isNullOrEmpty()) {
                        binding.tvNoMoviesFound.text = ""
                        binding.recycler.visibility = View.VISIBLE
                        if (!binding.searchView.query.isNullOrEmpty()) {
                            viewModel.searchQuery.value?.let { viewModel.filterMovies(it) }
                        } else {
                            adapter.updateMovies(movieList)
                        }
                    }
                }
            }
        }

        viewModel.recognition.observe(viewLifecycleOwner) { recognitionText ->
            binding.searchView.setQuery(recognitionText, false)
        }

        binding.ibMicSearch.setOnClickListener {
            askLanguagePreference()
        }

        adapter = ItemAdapter(
            emptyList(),
            object : ItemAdapter.ItemListener {

                override fun onItemClicked(index: Int) {
                    val movie = adapter.getItem(index)
                    viewModel.setMovie(movie)

                    findNavController().navigate(
                        R.id.action_allItemsFragment2_to_detailedItemFragment,
                        bundleOf("id" to movie.id)
                    )
                }

                override fun onItemLongClicked(index: Int) {
                    val movie = adapter.getItem(index)
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_CONTACTS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestContactPermission(movie)
                    } else {
                        showContactsDialog(requireContext(), movie)
                    }
                }

                override fun onEditButtonClick(index: Int) {
                    val movie = adapter.getItem(index)
                    viewModel.setMovie(movie)
                    editViewModel.setEditMode(true)
                    findNavController().navigate(R.id.action_allItemsFragment2_to_addOrEditItemFragment)
                }

                override fun onFavButtonClick(index: Int) {
                    val movie = adapter.getItem(index)
                    movie.favorite = !movie.favorite
                    viewModel.updateMovie(movie)
                    adapter.notifyItemChanged(index)
                    val query = binding.searchView.query
                    viewModel.setSearchQuery(query.toString())
                }
            })

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.filteredMovies.observe(viewLifecycleOwner) { filteredMovies ->
            adapter.updateMovies(filteredMovies)

        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val movie = adapter.getItem(viewHolder.adapterPosition)
                if (movie.localGen)
                    deleteMovieDialog(viewHolder)
                else {
                    findNavController().navigate(
                        R.id.action_allItemsFragment2_to_trailerFragment,
                        bundleOf("id" to movie.id)
                    )
                }

            }
        }).attachToRecyclerView(binding.recycler)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterMovies(newText.orEmpty())
                if (viewModel.filteredMovies.value?.isEmpty() == true) {
                    binding.tvNoMoviesFound.text = getString(R.string.no_movies_found)
                } else
                    binding.tvNoMoviesFound.text = ""

                return true
            }
        })

        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            binding.searchView.setQuery(query, false)
        }

        binding.fabAddItem.setOnClickListener {
            editViewModel.setEditMode(false)
            editViewModel.clearAllData()
            findNavController().navigate(R.id.action_allItemsFragment2_to_addOrEditItemFragment)
        }
    }

    private fun deleteMovieDialog(viewHolder: RecyclerView.ViewHolder) {
        val movieToDelete = adapter.getItem(viewHolder.adapterPosition)
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.delete_item_dialog, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            adapter.notifyItemChanged(viewHolder.adapterPosition)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            viewModel.deleteMovie(movieToDelete, binding.searchView.query?.toString())

            Toast.makeText(
                requireContext(),
                getString(R.string.item_deletion, movieToDelete.title),
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }

        Glide.with(binding.root)
            .load(movieToDelete.photo.takeIf { !it.isNullOrEmpty() }
                ?: R.drawable.baseline_warning_red_24)
            .into(dialogView.findViewById(R.id.iv_warning_image_movie))
        dialog.show()
    }

    private fun askLanguagePreference() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.language_speak))
            .setPositiveButton(getString(R.string.hebrew)) { dialog, _ ->
                startSpeechRecognition("he-IL")
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.english)) { dialog, _ ->
                startSpeechRecognition("en-US")
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun startSpeechRecognition(languageCode: String) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.mic_talk))
        }
        recognizeSpeechLauncher.launch(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}