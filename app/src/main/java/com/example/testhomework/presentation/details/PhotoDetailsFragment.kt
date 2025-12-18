package com.example.testhomework.presentation.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.testhomework.databinding.FragmentPhotoDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoDetailsFragment : Fragment() {

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoDetailsViewModel by viewModel()
    private val handler = Handler(Looper.getMainLooper())
    private var timeoutRunnable: Runnable? = null
    
    private var currentPhotoUrl: String = ""
    private var currentPhotoTitle: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentPhotoUrl = arguments?.getString("photoUrl").orEmpty()
        currentPhotoTitle = arguments?.getString("photoTitle").orEmpty()

        setupToolbar(currentPhotoTitle)
        setupObservers()
        setupRetryButton()
        loadImage(currentPhotoUrl, currentPhotoTitle)
    }

    private fun setupToolbar(title: String) {
        binding.toolbar.title = title
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is PhotoDetailsUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorGroup.visibility = View.GONE
                }
                is PhotoDetailsUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorGroup.visibility = View.GONE
                }
                is PhotoDetailsUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorText.text = state.message
                    binding.errorGroup.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupRetryButton() {
        binding.retryButton.setOnClickListener {
            loadImage(currentPhotoUrl, currentPhotoTitle)
        }
    }

    private fun loadImage(url: String, title: String) {
        // Очищаем предыдущий таймаут, если он есть
        timeoutRunnable?.let { handler.removeCallbacks(it) }
        
        viewModel.loadPhoto(url, title)

        // Устанавливаем таймаут для скрытия прогресс-бара при ошибке загрузки (5 секунд)
        timeoutRunnable = Runnable {
            val currentState = viewModel.uiState.value
            if (currentState is PhotoDetailsUiState.Loading) {
                viewModel.onImageLoadError(Exception("Failed to load image"))
            }
        }
        handler.postDelayed(timeoutRunnable!!, 5000)

        // Получаем размер экрана для ограничения размера загружаемого изображения
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val target = object : CustomTarget<Drawable>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                timeoutRunnable?.let { handler.removeCallbacks(it) }
                _binding?.let {
                    it.photoView.setImageDrawable(resource)
                    viewModel.onImageLoaded()
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                timeoutRunnable?.let { handler.removeCallbacks(it) }
                _binding?.progressBar?.visibility = View.GONE
            }
        }

        Glide.with(binding.photoView)
            .load(url)
            .override(screenWidth, screenHeight)
            .fitCenter()
            .error(android.R.drawable.ic_dialog_alert)
            .into(target)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeoutRunnable?.let { handler.removeCallbacks(it) }
        _binding?.photoView?.let {
            Glide.with(this).clear(it)
        }
        _binding = null
    }
}


