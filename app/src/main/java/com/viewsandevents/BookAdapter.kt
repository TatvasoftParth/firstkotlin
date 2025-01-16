package com.viewsandevents

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val books: List<Book>,
    private val onItemClick: (Book) -> Unit // Lambda for click listener
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookName: TextView = itemView.findViewById(R.id.book_name)
        val authorName: TextView = itemView.findViewById(R.id.author_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        Log.d("Book Item", "onBindViewHolder: $book")
        holder.bookName.text = book.bookName
        holder.authorName.text = book.authorName

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(book) // Invoke the click listener
        }
    }

    override fun getItemCount(): Int = books.size
}