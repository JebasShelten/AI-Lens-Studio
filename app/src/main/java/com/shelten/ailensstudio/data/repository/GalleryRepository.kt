package com.shelten.ailensstudio.data.repository

import android.content.ContentUris
import android.content.ContentResolver
import android.provider.MediaStore
import com.shelten.ailensstudio.data.model.GalleryImage

class GalleryRepository(
    private val contentResolver: ContentResolver
) {

    fun getImages(): List<GalleryImage> {

        val result = mutableListOf<GalleryImage>()

        val collection =
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder =
            "${MediaStore.Images.Media.DATE_ADDED} DESC"

        contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->

            val idColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media._ID
                )

            val nameColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media.DISPLAY_NAME
                )

            val dateColumn =
                cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media.DATE_ADDED
                )

            while (cursor.moveToNext()) {

                val id = cursor.getLong(idColumn)

                val name = cursor.getString(nameColumn)

                val dateAdded = cursor.getLong(dateColumn)

                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                result.add(
                    GalleryImage(
                        id = id,
                        uri = uri,
                        name = name,
                        dateAdded = dateAdded
                    )
                )
            }
        }

        return result
    }
}