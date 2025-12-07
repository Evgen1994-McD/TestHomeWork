package com.example.testhomework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testhomework.databinding.ItemTestBinding

class TestAdapter : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    private val items = mutableListOf<TestItem>()

    fun updateItems(newItems: List<TestItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val binding = ItemTestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class TestViewHolder(
        private val binding: ItemTestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TestItem) {
            binding.tvItemTitle.text = item.title
            binding.tvItemSubtitle.text = item.subtitle
            binding.root.setOnClickListener{
                MainActivity.ItemClickSubject.positionSubject.onNext(position)
            }
        }
    }
}



