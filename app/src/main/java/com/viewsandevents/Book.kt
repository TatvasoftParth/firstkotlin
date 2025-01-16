package com.viewsandevents

/**
 * A simple [Fragment] subclass.
 * Use the [AddBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

data class Book(
    val id: Int,
    val bookName: String,
    val authorName: String,
    val type: String,
    val genre: String,
    val launchedDate: String,
    val ageGroups: List<String>
)