package com.shelten.ailensstudio.data.model

import android.net.Uri

data class GalleryImage(
    val id: Long,
    val uri: Uri,
    val name: String,
    val dateAdded: Long
)