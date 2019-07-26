package org.allatra.wisdom.library.model

data class Book(
    val name: String,
    val pdfBookName: String,
    val description: String,
    val actualPage: Int,
    val totalPages: Int,
    val thumbNail: Int
)