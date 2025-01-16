package com.viewsandevents

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.viewsandevents.databinding.FragmentAddBookBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddBookFragment : Fragment() {
    // Declare a binding variable
    private lateinit var binding: FragmentAddBookBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val gson = Gson()

    private val booksList = mutableListOf<Book>()
    private var updatedBookList = mutableListOf<Book>()
    private var param1: String? = null
    private var param2: String? = null
    private var bookId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        val options = arrayOf("Young adult literature", "Novel", "Narrative", "Mystery")
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentAddBookBinding.inflate(layoutInflater)

        // Initialize SharedPreferences
        sharedPreferences =
            requireActivity().getSharedPreferences("BookPrefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        if (arguments != null) {
            Log.d("Book Name", "onCreate: ${arguments?.getString("id")}")
            bookId = arguments?.getString("id")?.toInt() ?: 0
            binding.editBookName.setText(arguments?.getString("book_name"))
            binding.editAuthorName.setText(arguments?.getString("author_name"))
            binding.spinner.setSelection(options.indexOf(arguments?.getString("genre")))
            when (arguments?.getString("type")) {
                "Fiction" -> binding.radioGroup.check((R.id.radio_button_1))
                "Non-Fiction" -> binding.radioGroup.check((R.id.radio_button_2))
            }
            binding.dateValue.text = arguments?.getString("launch_date")

            val checkBoxMap = mapOf(
                "5-12 years old" to binding.checkboxAge512,
                "13-19 years old" to binding.checkboxAge1319,
                "20-39 years old" to binding.checkboxAge2039,
                "40-59 years old" to binding.checkboxAge4059,
                "60+ years old" to binding.checkboxAge60Plus
            )

            val ages = arguments?.getString("age_groups").toString().replace("[", "") // Remove opening bracket
                .replace("]", "") // Remove closing bracket
                .trim()
//            Log.d("Ages", "onCreate: $ages")
//            val selectedValues = arrayOf("5-12 years old", "20-39 years old") // Example selected values

//            for (value in selectedValues) {
//                checkBoxMap[value]?.isChecked = true
//            }
        }

        // Retrieve the saved data from SharedPreferences
        loadBooksFromSharedPreferences()

        // Create an ArrayAdapter
        val adapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, options)

        // Set the layout for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinner
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (arguments?.getString("genre") !== null) {
                    binding.spinner.setSelection(options.indexOf(arguments?.getString("genre")))
                    val selectedItem =
                        parentView?.getItemAtPosition(options.indexOf(arguments?.getString("genre")))
                            .toString()
                    Log.d("Selected Item", "onItemSelected: $selectedItem")

                } else {
                    val selectedItem = parentView?.getItemAtPosition(position).toString()
                    binding.spinner.setSelection(position)
                    Log.d("Selected Item", "onItemSelected: $selectedItem, $position")
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle case when no item is selected (if needed)
            }
        }

        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(Calendar.getInstance().timeInMillis)  // Optional: set a minimum date
                    .build()
            ).build()

        // Set a listener for when a date is selected
        datePicker.addOnPositiveButtonClickListener { selection ->
            onDateSelected(selection)
        }

        // Show the date picker when the button is clicked
        binding.selectDateButton.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, datePicker.tag)
        }
        binding.arrowDropdown.setOnClickListener {
            binding.spinner.performClick()
        }

        binding.addBook.setOnClickListener {
            onClickAddBook()
        }
    }

    private fun onClickAddBook() {
        if (isFormValid()) {
            saveDataToSharedPreferences()
            Toast.makeText(requireContext(), "Data saved to local storage!", Toast.LENGTH_SHORT)
                .show()
//            resetAllValues()
            // Proceed with the form submission
        }
    }

    private fun isFormValid(): Boolean {
        // Validate Book Name
        if (binding.editBookName.text.isNullOrEmpty()) {
            binding.editBookName.error = "Book name is required"
            return false
        }

        // Validate Author Name
        if (binding.editAuthorName.text.isNullOrEmpty()) {
            binding.editAuthorName.error = "Author name is required"
            return false
        }

        // Validate Spinner selection (ensure an option is selected)
        Log.d("Spinner", "isFormValid: ${binding.spinner.selectedItemPosition}")
        if (binding.spinner.selectedItemPosition == 0) {
            Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate RadioGroup selection
        if (binding.radioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(
                requireContext(), "Please select Fiction or Non-Fiction", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // Validate at least one checkbox is checked
        if (!binding.checkboxAge512.isChecked && !binding.checkboxAge1319.isChecked && !binding.checkboxAge2039.isChecked && !binding.checkboxAge4059.isChecked && !binding.checkboxAge60Plus.isChecked) {
            Toast.makeText(
                requireContext(), "Please select at least one age group", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // Validation passed
        return true
    }

    // Method to save data to SharedPreferences
    private fun saveDataToSharedPreferences() {
        val bookJson = sharedPreferences.getString("books", null)
        var id: Int = 1

        if (bookJson != null) {
            // Deserialize the JSON string back to a list of books
            val type = object : TypeToken<List<Book>>() {}.type
            booksList.clear()
            booksList.addAll(gson.fromJson(bookJson, type))

            id = booksList.size + 1 // ID is the next number in sequence


            // Update the adapter or UI here if needed (e.g., displaying in a ListView)
        }
        val bookName = binding.editBookName.text.toString()
        val authorName = binding.editAuthorName.text.toString()
        val genre = binding.spinner.selectedItem.toString()
        val radioButton = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radio_button_1 -> "Fiction"
            R.id.radio_button_2 -> "Non-Fiction"
            else -> ""
        }
        val launchedDate = binding.dateValue.text.toString()

        val ageGroups = mutableListOf<String>()
        if (binding.checkboxAge512.isChecked) ageGroups.add("5-12 years old")
        if (binding.checkboxAge1319.isChecked) ageGroups.add("13-19 years old")
        if (binding.checkboxAge2039.isChecked) ageGroups.add("20-39 years old")
        if (binding.checkboxAge4059.isChecked) ageGroups.add("40-59 years old")
        if (binding.checkboxAge60Plus.isChecked) ageGroups.add("60+ years old")
        Log.d("AgeGroups", "saveDataToSharedPreferences: $ageGroups")

        // Create a Book object
        val book = Book(id, bookName, authorName, radioButton, genre, launchedDate, ageGroups)

        // Add the new book to the list

        Log.d("ID", "ID::: $bookId")
        if(bookId == 0) {
            booksList.add(book)
        } else {
            updatedBookList = booksList.map { book ->
                Log.d("ID5", "saveDataToSharedPreferences: ${book.id == bookId}")
                if (book.id == bookId) {
                    book.copy(
                        bookName = bookName,
                        authorName = authorName,
                        type = radioButton,
                        genre = genre,
                        launchedDate = launchedDate,
                        ageGroups = ageGroups
                    )
                } else {
                    book // Keep the book unchanged if the ID does not match
                }
            }.toMutableList()

        }

        // Convert the list of books to JSON
        val result = if (bookId == 0) booksList else updatedBookList
        val booksJson = gson.toJson(result)

        // Save the JSON string to SharedPreferences
        with(sharedPreferences.edit()) {
            putString("books", booksJson)
            apply()
        }
        resetAllValues()
    }

    private fun loadBooksFromSharedPreferences() {
        // Retrieve the JSON string from SharedPreferences
        val booksJson = sharedPreferences.getString("books", null)

        if (booksJson != null) {
            // Deserialize the JSON string back to a list of books
            val type = object : TypeToken<List<Book>>() {}.type
            booksList.clear()
            booksList.addAll(gson.fromJson(booksJson, type))

            // Update the adapter or UI here if needed (e.g., displaying in a ListView)
        }
    }

    // Method to reset all values in the form and SharedPreferences
    private fun resetAllValues() {
        // Reset form fields (EditTexts, Spinner, Radio Buttons, Checkboxes, etc.)
        binding.editBookName.text.clear()
        binding.editAuthorName.text.clear()
        binding.spinner.setSelection(0) // Reset Spinner selection
        binding.radioGroup.clearCheck() // Deselect any selected radio button
        binding.dateValue.text = "" // Clear the selected date
        binding.checkboxAge512.isChecked = false
        binding.checkboxAge1319.isChecked = false
        binding.checkboxAge2039.isChecked = false
        binding.checkboxAge4059.isChecked = false
        binding.checkboxAge60Plus.isChecked = false

        // Clear SharedPreferences
//        with(editor) {
//            clear() // Clear all data
//            apply()
//        }

        // Optionally, reset any other UI elements if needed
    }


    private fun onDateSelected(selection: Long) {
        val selectedDate = Date(selection)
        val dateFormat = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)
        binding.dateValue.text = formattedDate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Return the root view of the binding
        return binding.root

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddBookFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) = AddBookFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}