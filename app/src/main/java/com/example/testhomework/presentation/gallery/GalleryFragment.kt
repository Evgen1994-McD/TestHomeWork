package com.example.testhomework.presentation.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testhomework.R
import com.example.testhomework.databinding.FragmentGalleryBinding
import com.example.testhomework.domain.model.Photo

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GalleryViewModel by viewModels()

    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupRetry()

        if (savedInstanceState == null) {
            viewModel.loadFirstPage()
        }
    }

    private fun setupRecyclerView() {
        photosAdapter = PhotosAdapter(::onPhotoClicked)
        val spanCount = resources.getInteger(R.integer.gallery_span_count)
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.apply {
            adapter = photosAdapter
            this.layoutManager = layoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0) return
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (lastVisibleItem >= totalItemCount - 5) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is GalleryUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBarBottom.visibility = View.GONE
                    binding.errorGroup.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                }
                is GalleryUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarBottom.visibility = 
                        if (state.isLoadingMore) View.VISIBLE else View.GONE
                    binding.errorGroup.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    photosAdapter.submitList(state.photos)
                }
                is GalleryUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarBottom.visibility = View.GONE
                    binding.errorText.text = state.message
                    binding.errorGroup.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.INVISIBLE
                    photosAdapter.submitList(state.photos)
                }
            }
        })
    }

    private fun setupRetry() {
        binding.retryButton.setOnClickListener {
            viewModel.loadFirstPage()
        }
    }

    private fun onPhotoClicked(photo: Photo) {
        findNavController().navigate(
            R.id.action_galleryFragment_to_photoDetailsFragment,
            Bundle().apply {
                putString("photoUrl", photo.fullUrl)
                putString("photoTitle", photo.title)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


