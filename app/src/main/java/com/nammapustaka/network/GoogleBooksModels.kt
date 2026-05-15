package com.nammapustaka.network

import com.google.gson.annotations.SerializedName

data class GoogleBooksResponse(
    @SerializedName("items") val items: List<BookItem>? = null
)

data class BookItem(
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo? = null
)

data class VolumeInfo(
    @SerializedName("title") val title: String? = null,
    @SerializedName("authors") val authors: List<String>? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("imageLinks") val imageLinks: ImageLinks? = null
)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String? = null
)
