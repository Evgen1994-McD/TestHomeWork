package com.example.testhomework.task3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testhomework.databinding.FragmentProgressRectangleBinding
import com.example.testhomework.task1.NavigationListener

class ProgressRectangleFragment : Fragment() {
    private var _binding: FragmentProgressRectangleBinding? = null
    private val binding get() = _binding!!
    private var navigationListener: NavigationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressRectangleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Последний экран - скрываем кнопку "Следующий"
        binding.btnNext.visibility = View.GONE
        
        // Настраиваем кастомную View
        binding.progressRectangleView.onProgressChanged = { progress ->
            val percent = (progress * 100).toInt()
            binding.tvProgress.text = "$percent%"
        }
        
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
