package attention.manager.nanickydev.myapplication

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

import java.util.ArrayList
import java.util.HashSet

fun getAllMedia(context: Context): ArrayList<String> {
    val videoItemHashSet = HashSet<String>()
    val projection = arrayOf(MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME)
    val cursor =
        context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
    try {
        cursor!!.moveToFirst()
        do {
            videoItemHashSet.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)))
        } while (cursor.moveToNext())

        cursor.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val downloadedList = ArrayList(videoItemHashSet)
    return downloadedList
}
