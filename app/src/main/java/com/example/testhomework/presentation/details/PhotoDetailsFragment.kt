package com.example.testhomework.presentation.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.testhomework.databinding.FragmentPhotoDetailsBinding

class PhotoDetailsFragment : Fragment() {

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!

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

        val url = arguments?.getString("photoUrl").orEmpty()
        val title = arguments?.getString("photoTitle").orEmpty()

        binding.toolbar.title = title
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.progressBar.visibility = View.VISIBLE

        Glide.with(binding.photoView)
            .load(url)
            .fitCenter()
            .error(android.R.drawable.ic_dialog_alert)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    _binding?.let {
                        it.photoView.setImageDrawable(resource)
                        it.progressBar.visibility = View.GONE
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    _binding?.progressBar?.visibility = View.GONE
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.photoView?.let {
            Glide.with(this).clear(it)
        }
        _binding = null
    }
}


