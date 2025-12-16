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
    private var savedScrollPosition: Int = 0

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
        
        // Восстанавливаем позицию прокрутки
        savedScrollPosition = savedInstanceState?.getInt("scroll_position", 0) ?: 0
        
        setupRecyclerView()
        setupObservers()
        setupRetry()

        // Загружаем данные только при первом создании и если их еще нет
        if (savedInstanceState == null) {
            val currentState = viewModel.uiState.value
            val hasData = currentState is GalleryUiState.Success && currentState.photos.isNotEmpty()
            if (!hasData) {
                viewModel.loadFirstPage()
            }
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем позицию прокрутки
        _binding?.recyclerView?.layoutManager?.let { manager ->
            val layoutManager = manager as? GridLayoutManager
            layoutManager?.let {
                val firstVisiblePosition = it.findFirstVisibleItemPosition()
                if (firstVisiblePosition != RecyclerView.NO_POSITION) {
                    outState.putInt("scroll_position", firstVisiblePosition)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        photosAdapter = PhotosAdapter(::onPhotoClicked)
        val spanCount = resources.getInteger(R.integer.gallery_span_count)
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerView.apply {
            adapter = photosAdapter
            this.layoutManager = layoutManager
            
            // Восстанавливаем позицию прокрутки при возврате
            if (savedScrollPosition > 0) {
                post { layoutManager.scrollToPositionWithOffset(savedScrollPosition, 0) }
            }
            
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
                    binding.paginationErrorGroup.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                }
                is GalleryUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarBottom.visibility = 
                        if (state.isLoadingMore) View.VISIBLE else View.GONE
                    binding.errorGroup.visibility = View.GONE
                    binding.paginationErrorGroup.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    // Восстанавливаем стандартный padding при успешной загрузке
                    binding.recyclerView.setPadding(
                        binding.recyclerView.paddingLeft,
                        binding.recyclerView.paddingTop,
                        binding.recyclerView.paddingRight,
                        0
                    )
                    photosAdapter.submitList(state.photos)
                }
                is GalleryUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarBottom.visibility = View.GONE
                    photosAdapter.submitList(state.photos)
                    
                    if (state.isPaginationError) {
                        // Ошибка пагинации - показываем список и ошибку внизу
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.errorGroup.visibility = View.GONE
                        binding.paginationErrorText.text = state.message
                        binding.paginationErrorGroup.visibility = View.VISIBLE
                        // Добавляем padding снизу, чтобы контент не перекрывался ошибкой
                        binding.paginationErrorGroup.post {
                            val errorHeight = binding.paginationErrorGroup.height
                            if (errorHeight > 0) {
                                binding.recyclerView.setPadding(
                                    binding.recyclerView.paddingLeft,
                                    binding.recyclerView.paddingTop,
                                    binding.recyclerView.paddingRight,
                                    errorHeight
                                )
                            }
                        }
                    } else {
                        // Ошибка первой загрузки - показываем только ошибку
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.errorText.text = state.message
                        binding.errorGroup.visibility = View.VISIBLE
                        binding.paginationErrorGroup.visibility = View.GONE
                        // Восстанавливаем стандартный padding
                        binding.recyclerView.setPadding(
                            binding.recyclerView.paddingLeft,
                            binding.recyclerView.paddingTop,
                            binding.recyclerView.paddingRight,
                            0
                        )
                    }
                }
            }
        })
    }

    private fun setupRetry() {
        binding.retryButton.setOnClickListener {
            viewModel.loadFirstPage()
        }
        binding.paginationRetryButton.setOnClickListener {
            viewModel.loadNextPage()
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


