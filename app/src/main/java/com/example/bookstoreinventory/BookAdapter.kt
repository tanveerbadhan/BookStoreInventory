package com.example.bookstoreinventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreinventory.databinding.ItemBookBinding

class BookAdapter(
    private val onEditBook: (BookEntity) -> Unit,
    private val onDeleteBook: (BookEntity) -> Unit,
    private val onViewDetails: (BookEntity) -> Unit
) : ListAdapter<BookEntity, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookEntity) {
            binding.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author
                tvPrice.text = "$${book.price}"
                tvQuantity.text = "${book.quantity}"

                // Updated method names for button click listeners
                btnEdit.setOnClickListener { onEditBook(book) }
                btnDelete.setOnClickListener { onDeleteBook(book) }
                btnDetail.setOnClickListener { onViewDetails(book) }
            }
        }
    }
}

// DiffCallback for efficient list updating
class BookDiffCallback : DiffUtil.ItemCallback<BookEntity>() {
    override fun areItemsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
        return oldItem.bookId == newItem.bookId
    }

    override fun areContentsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
        return oldItem == newItem
    }
}
