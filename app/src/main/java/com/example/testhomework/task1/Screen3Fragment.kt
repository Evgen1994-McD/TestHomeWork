package com.example.testhomework.task1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testhomework.databinding.FragmentScreen3Binding

class Screen3Fragment : Fragment() {
    private var _binding: FragmentScreen3Binding? = null
    private val binding get() = _binding!!
    private var navigationListener: NavigationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreen3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnNext.setOnClickListener {
            navigationListener?.onNextClicked()
        }
        
        binding.btnPrevious.setOnClickListener {
            navigationListener?.onPreviousClicked()
        }
    }

    fun setNavigationListener(listener: NavigationListener) {
        navigationListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

