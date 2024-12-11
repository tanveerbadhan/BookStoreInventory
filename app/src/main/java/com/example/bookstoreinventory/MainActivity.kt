package com.example.bookstoreinventory

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstoreinventory.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var bookDao: BookDao
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(this)
        bookDao = database.bookDao()

        setupBookList()
        setupEventListeners()
        loadBooks()
    }

    // Sets up RecyclerView and Adapter
    private fun setupBookList() {
        bookAdapter = BookAdapter(
            onEditBook = { book -> openEditDialog(book) },
            onDeleteBook = { book -> removeBook(book) },
            onViewDetails = { book -> showBookDetails(book) }
        )
        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = bookAdapter
    }

    // Initializes listeners for button clicks
    private fun setupEventListeners() {
        binding.btnAddBook.setOnClickListener { openAddDialog() }
    }

    // Loads all books from the database and observes changes
    private fun loadBooks() {
        lifecycleScope.launch {
            bookDao.getAllBooks().collect { books ->
                bookAdapter.submitList(books.toList())
            }
        }
    }

    // Opens dialog for adding a new book
    private fun openAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book, null)
        val bookIdInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewBookId) // Updated ID
        val titleInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewTitle) // Updated ID
        val authorInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewAuthor) // Updated ID
        val priceInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewPrice) // Updated ID
        val quantityInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewQuantity) // Updated ID

        AlertDialog.Builder(this)
            .setTitle("Add New Book")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleInput.text.toString()
                val bookId = bookIdInput.text.toString()
                val author = authorInput.text.toString()
                val price = priceInput.text.toString().toDoubleOrNull()
                val quantity = quantityInput.text.toString().toIntOrNull()

                if (title.isNotBlank() && author.isNotBlank() && bookId.isNotBlank() && price != null && quantity != null) {
                    lifecycleScope.launch {
                        bookDao.insertBook(BookEntity(bookId = bookId, title = title, author = author, price = price, quantity = quantity))
                    }
                    Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show()
                    loadBooks()
                } else {
                    Toast.makeText(this, "Please fill in all the fields correctly.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Opens dialog to edit an existing book
    private fun openEditDialog(book: BookEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book, null)
        val bookIdInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewBookId) // Updated ID
        val titleInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewTitle) // Updated ID
        val authorInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewAuthor) // Updated ID
        val priceInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewPrice) // Updated ID
        val quantityInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewQuantity) // Updated ID

        // Pre-fill the dialog with existing book data
        titleInput.setText(book.title)
        bookIdInput.setText(book.bookId)
        authorInput.setText(book.author)
        priceInput.setText(book.price.toString())
        quantityInput.setText(book.quantity.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Book Details")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val title = titleInput.text.toString()
                val author = authorInput.text.toString()
                val bookId = bookIdInput.text.toString()
                val price = priceInput.text.toString().toDoubleOrNull()
                val quantity = quantityInput.text.toString().toIntOrNull()

                if (title.isNotBlank() && author.isNotBlank() && bookId.isNotBlank() && price != null && quantity != null) {
                    lifecycleScope.launch {
                        bookDao.updateBook(BookEntity(bookId = bookId, title = title, author = author, price = price, quantity = quantity))
                    }
                    Toast.makeText(this, "Book updated successfully.", Toast.LENGTH_SHORT).show()
                    loadBooks()
                } else {
                    Toast.makeText(this, "Please fill in all the fields correctly.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Removes a book from the database
    private fun removeBook(book: BookEntity) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("'${book.title}' will be deleted?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    bookDao.deleteBook(book)
                    Toast.makeText(this@MainActivity, "Book deleted successfully.", Toast.LENGTH_SHORT).show()
                    loadBooks()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Displays detailed information about the book
    private fun showBookDetails(book: BookEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book, null)
        val bookIdInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewBookId) // Updated ID
        val titleInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewTitle) // Updated ID
        val authorInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewAuthor) // Updated ID
        val priceInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewPrice) // Updated ID
        val quantityInput = dialogView.findViewById<AppCompatEditText>(R.id.etNewQuantity) // Updated ID

        // Display the book's current information
        titleInput.setText(book.title)
        bookIdInput.setText(book.bookId)
        authorInput.setText(book.author)
        priceInput.setText(book.price.toString())
        quantityInput.setText(book.quantity.toString())

        AlertDialog.Builder(this)
            .setTitle("Book Details")
            .setView(dialogView)
            .setNegativeButton("Close", null)
            .show()
    }
}
