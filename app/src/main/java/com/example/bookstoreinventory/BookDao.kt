package com.example.bookstoreinventory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    //Get all books
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>

    //Add a new book
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    //Update an existing book
    @Update
    suspend fun updateBook(book: BookEntity)

    //Delete a book
    @Delete
    suspend fun deleteBook(book: BookEntity)



}
