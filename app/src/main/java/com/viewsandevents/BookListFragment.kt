package com.viewsandevents

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.viewsandevents.databinding.FragmentBookListBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookListFragment : Fragment() {
    // Declare a binding variable
    private lateinit var binding: FragmentBookListBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var bookAdapter: BookAdapter

    private val gson = Gson()

    private val booksList = mutableListOf<Book>()
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentBookListBinding
            .inflate(layoutInflater)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("BookPrefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        binding.recyclerListView.layoutManager = LinearLayoutManager(requireContext())

        // Retrieve the saved data from SharedPreferences
        loadBooksFromSharedPreferences()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun loadBooksFromSharedPreferences() {
        // Retrieve the JSON string from SharedPreferences
        val booksJson = sharedPreferences.getString("books", null)
        Log.d("TAG", "$booksJson ")

        if (booksJson != null) {
            // Deserialize the JSON string back to a list of books
            val type = object : TypeToken<List<Book>>() {}.type
            booksList.clear()
            booksList.addAll(gson.fromJson(booksJson, type))

            // Update the adapter or UI here if needed (e.g., displaying in a ListView)
            Log.d("BookList", "loadBooksFromSharedPreferences: $booksList")
            bookAdapter = BookAdapter(booksList) { book ->
                // Navigate to detail activity
                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra("id", book.id.toString())
                intent.putExtra("book_name", book.bookName)
                intent.putExtra("author_name", book.authorName)
                intent.putExtra("genre", book.genre)
                intent.putExtra("type", book.type)
                intent.putExtra("launch_date", book.launchedDate)
                intent.putExtra("age_groups", book.ageGroups.toString())
                Log.d("ID3", "loadBooksFromSharedPreferences: ${book.id}")
                startActivity(intent)
            }
            binding.recyclerListView.adapter = bookAdapter
            // Add divider
            val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            binding.recyclerListView.addItemDecoration(dividerItemDecoration)

            binding.searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val filteredBooks = searchBooks(booksList, s.toString())
                    bookAdapter.updateBooks(filteredBooks)
                }

                override fun afterTextChanged(s: Editable?) {}
            })

        }
    }

    private fun searchBooks(
        booksList: List<Book>,
        query: String
    ): List<Book> {
        return booksList.filter { book ->
            book.bookName.contains(query, ignoreCase = true)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookListFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

